package com.neo.qiaoqiaochat.server;

import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLiteOrBuilder;
import com.neo.qiaoqiaochat.config.NettyConfig;
import com.neo.qiaoqiaochat.handler.MessageWebSocketHandler;
import com.neo.qiaoqiaochat.model.QiaoqiaoConst;
import com.neo.qiaoqiaochat.model.protobuf.QiaoQiaoHua;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.List;

import static io.netty.buffer.Unpooled.wrappedBuffer;

@Component
public class QiaoqiaoWebSocketServer {

    private static final Logger logger = LoggerFactory.getLogger(QiaoqiaoService.class);

    @Resource
    private NettyConfig nettyConfig;
    @Autowired
    private MessageWebSocketHandler webSocketHandler;
    private EventLoopGroup group = new NioEventLoopGroup();

    private ProtobufDecoder decoder = new ProtobufDecoder(QiaoQiaoHua.Model.getDefaultInstance());


    /**
     * 关闭服务器方法
     */
    @PreDestroy
    public void close() {
        logger.info("关闭服务器....");
        //优雅退出
        group.shutdownGracefully();
    }

    @PostConstruct
    public void start() throws Exception {

        int port = nettyConfig.getWebsocketPort();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    .localAddress(new InetSocketAddress(port))
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();


                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new ChunkedWriteHandler());
                            pipeline.addLast(new HttpObjectAggregator(64 * 1024));
                            // WebSocket数据压缩
                            pipeline.addLast(new WebSocketServerCompressionHandler());
                            // 协议包长度限制
                            pipeline.addLast(new WebSocketServerProtocolHandler("/ws", null, true, QiaoqiaoConst.ServerConfig.MAX_FRAME_LENGTH));
                            // 协议包解码
                            pipeline.addLast(new MessageToMessageDecoder<WebSocketFrame>() {
                                @Override
                                protected void decode(ChannelHandlerContext ctx, WebSocketFrame frame, List<Object> objs) throws Exception {
                                    ByteBuf buf = ((BinaryWebSocketFrame) frame).content();
                                    objs.add(buf);
                                    buf.retain();
                                }
                            });
                            // 协议包编码
                            pipeline.addLast(new MessageToMessageEncoder<MessageLiteOrBuilder>() {
                                @Override
                                protected void encode(ChannelHandlerContext ctx, MessageLiteOrBuilder msg, List<Object> out) throws Exception {
                                    ByteBuf result = null;
                                    if (msg instanceof MessageLite) {
                                        result = wrappedBuffer(((MessageLite) msg).toByteArray());
                                    }
                                    if (msg instanceof MessageLite.Builder) {
                                        result = wrappedBuffer(((MessageLite.Builder) msg).build().toByteArray());
                                    }
                                    // 然后下面再转成websocket二进制流，因为客户端不能直接解析protobuf编码生成的
                                    WebSocketFrame frame = new BinaryWebSocketFrame(result);
                                    out.add(frame);
                                }
                            });
                            pipeline.addLast("decoder", decoder);
                            pipeline.addLast(new IdleStateHandler(QiaoqiaoConst.ServerConfig.READ_IDLE_TIMEOUT,
                                    QiaoqiaoConst.ServerConfig.WRITE_IDLE_TIMEOUT, QiaoqiaoConst.ServerConfig.ALL_IDLE_TIMEOUT));
                            pipeline.addLast(webSocketHandler);
//                            pipeline.addLast(new TextWebSocketFrameHandler());
                        }
                    });
            // 可选参数
            bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
            ChannelFuture future = bootstrap.bind().sync();
            future.addListener((ChannelFutureListener) future1 -> {
                if (future1.isSuccess()) {
                    logger.info("websocketserver have success bind to " + port);
                } else {
                    logger.error("websocketserver fail bind to " + port);
                }
            });
//            future.channel().closeFuture().sync();
        } finally {
//            group.shutdownGracefully().sync();
        }
    }
}
