package com.neo.qiaoqiaochat.websocket.handler;

import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;
import com.neo.qiaoqiaochat.websocket.model.Message;

import java.util.List;

public class MessageDecoder  extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int length = in.readableBytes();
        ByteBuf byteBuf = in.readBytes(length);
        String msg = byteBuf.toString(CharsetUtil.UTF_8);
        Message message = new Gson().fromJson(msg, Message.class);
        out.add(message);
    }
}
