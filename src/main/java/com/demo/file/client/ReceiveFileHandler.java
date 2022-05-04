package com.demo.file.client;

import com.demo.dto.Message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ReceiveFileHandler extends ChannelInboundHandlerAdapter{

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelActive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf byteBuf = (ByteBuf) msg;
		int type = byteBuf.getInt(0);
		System.out.println("type: " + type);
		if (Message.TYPE == type) {// ��Ϣ
			System.out.println("Client RecFile ���´��� ");
			super.channelRead(ctx, msg);
		} else {// ���ݰ�
			System.out.println("RecFile read �������ݰ�");
		}
		
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelReadComplete(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
	}

	
}
