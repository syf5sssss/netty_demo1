package com.demo.codec;

import java.nio.charset.StandardCharsets;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class DecodeHandler extends ByteToMessageDecoder {
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
		int type = byteBuf.readInt();
		int len = byteBuf.readInt();
		System.out.println("解码 type: "+type+" len: "+len);
		byte[] bytes = new byte[len];
		byteBuf.readBytes(bytes);
		String str = new String(bytes, StandardCharsets.UTF_8);
		System.out.println("解码为JSONObject向下传递");
		JSONObject jo = JSON.parseObject(str);
		list.add(jo);
	}

	
}
