package com.demo.util;

import java.net.InetSocketAddress;

import io.netty.channel.ChannelHandlerContext;

public class ConnUtil {
	
	/**
	 * ��ȡԶ�� IP
	 */
	public static String GetIp(ChannelHandlerContext ctx) {
		String host = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
		return host;
	}

}
