package com.neo.qiaoqiaochat.websocket.handler;

import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;
import com.neo.qiaoqiaochat.websocket.model.Message;

public class MessageEncoder extends MessageToByteEncoder<Message> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        String rtn = new Gson().toJson(msg);
        out.writeBytes(rtn.getBytes(CharsetUtil.UTF_8));
    }
}
