package com.demo.file.client;

import java.util.HashMap;
import java.util.Map;

import com.demo.codec.DecodeHandler;
import com.demo.codec.EncodeHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.stream.ChunkedWriteHandler;

public class Client {

	private static final String HOST = System.getProperty("host", "127.0.0.1");

	private static final int PORT = Integer.parseInt(System.getProperty("port", "8080"));
	
	/**
	 * 发送文件 Map   key:filename   value:上一次登记的文件路径
	 */
	public static Map<String, String> filelist = new HashMap<String, String>();

	public static void main(String[] args) throws InterruptedException {

		Bootstrap bootstrap = new Bootstrap();
		NioEventLoopGroup group = new NioEventLoopGroup();
		bootstrap.group(group)
				.channel(NioSocketChannel.class)
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
				.option(ChannelOption.SO_KEEPALIVE, true)
				.option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel channel) throws Exception {
						ChannelPipeline pipeline = channel.pipeline();
						pipeline.addLast(new EncodeHandler());
						pipeline.addLast(new ReceiveFileHandler());//先接收文件文件流
						pipeline.addLast(new DecodeHandler());//解析成JSONObject
						pipeline.addLast(new PojoHandler());//再处理信息包
						pipeline.addLast(new SendFileHandler());
						pipeline.addLast(new ChunkedWriteHandler());
					}
				});

		ChannelFuture future = bootstrap.connect(HOST, PORT).sync();
		if (future.isSuccess()) {
			System.out.println("连接服务器成功: "+PORT);
		} else {
			System.out.println("连接服务器失败: "+PORT);
		}

		future.channel().closeFuture().sync();
	}
}

