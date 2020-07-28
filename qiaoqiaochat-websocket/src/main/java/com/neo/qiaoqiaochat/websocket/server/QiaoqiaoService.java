package com.neo.qiaoqiaochat.websocket.server;

import com.neo.qiaoqiaochat.websocket.handler.MessageHandler;
import com.neo.qiaoqiaochat.websocket.model.QiaoqiaoConst;
import com.neo.qiaoqiaochat.websocket.model.protobuf.QiaoQiaoHua;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import com.neo.qiaoqiaochat.websocket.config.NettyConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.net.InetSocketAddress;

//@Component
public class QiaoqiaoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QiaoqiaoService.class);

    @Resource
    private NettyConfig nettyConfig;
    @Autowired
    private MessageHandler messageHandler;
    private EventLoopGroup group = new NioEventLoopGroup();

    private ProtobufDecoder decoder = new ProtobufDecoder(QiaoQiaoHua.Model.getDefaultInstance());
    private ProtobufEncoder encoder = new ProtobufEncoder();


    /**
     * 关闭服务器方法
     */
    @PreDestroy
    public void close() {
        LOGGER.info("关闭服务器....");
        //优雅退出
        group.shutdownGracefully();
    }
    @PostConstruct
    public void start() throws Exception {


        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    .localAddress(new InetSocketAddress(nettyConfig.getPort()))
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
//                            pipeline.addLast(new HttpServerCodec());
//                            pipeline.addLast(new ChunkedWriteHandler());
//                            pipeline.addLast(new HttpObjectAggregator(64 *1024));
//                            pipeline.addLast(new MessageEncoder());
//                            pipeline.addLast(new MessageDecoder());
                            pipeline.addLast("frameDecoder", new ProtobufVarint32FrameDecoder());
                            pipeline.addLast("decoder", decoder);
                            pipeline.addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
                            pipeline.addLast("encoder",encoder);
                            pipeline.addLast(new IdleStateHandler(QiaoqiaoConst.ServerConfig.READ_IDLE_TIMEOUT,
                                    QiaoqiaoConst.ServerConfig.WRITE_IDLE_TIMEOUT,QiaoqiaoConst.ServerConfig.ALL_IDLE_TIMEOUT));

                            pipeline.addLast(messageHandler);
                        }
                    });
            ChannelFuture future = bootstrap.bind().sync();
            System.out.println(QiaoqiaoService.class.getName() +
                    " started and listening for connections on " + future.channel().localAddress());
//            future.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully().sync();
        }
    }
}
