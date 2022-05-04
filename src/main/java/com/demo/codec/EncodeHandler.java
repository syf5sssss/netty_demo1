package com.demo.codec;

import java.nio.charset.StandardCharsets;

import com.alibaba.fastjson.JSONObject;
import com.demo.dto.Message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

@ChannelHandler.Sharable
public class EncodeHandler extends MessageToByteEncoder<JSONObject> {
	@Override
	protected void encode(ChannelHandlerContext ctx, JSONObject o, ByteBuf byteBuf) throws Exception {
		//System.out.println("EncodeHandler:"+byteBuf);
		// Codec.INSTANCE.encode(byteBuf, (Packet) o);
		if (o != null && !o.isEmpty()) {
			String str = JSONObject.toJSONString(o);
			byte[] brr = str.getBytes(StandardCharsets.UTF_8);
			byteBuf.writeInt(Message.TYPE);
			System.out.println("±àÂë type: " + Message.TYPE);
			byteBuf.writeInt(brr.length);
			System.out.println("±àÂë len: " + brr.length);
			byteBuf.writeBytes(brr);
			System.out.println("±àÂë: " + str);
		} else {
			System.out.println("±àÂëÆ÷ÊÕµ½¿ÕÊý¾Ý");
		}
	}
}
