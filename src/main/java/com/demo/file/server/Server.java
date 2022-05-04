package com.demo.file.server;

import java.util.HashMap;
import java.util.Map;

import com.demo.codec.DecodeHandler;
import com.demo.codec.EncodeHandler;
import com.demo.dto.TFile;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.stream.ChunkedWriteHandler;

public class Server {

	private static final int PORT = Integer.parseInt(System.getProperty("port", "8080"));
	/**
	 * 接受文件队列   key:ip   value:FileNameQueue
	 */
	public static Map<String, FileNameQueue> queue = new HashMap<String, FileNameQueue>();
	/**
	 * 当前接受的文件信息  key:ip   value:TFile
	 */
	public static Map<String, TFile> currentTFile = new HashMap<String, TFile>();

	public static void main(String[] args) throws InterruptedException {
		ServerBootstrap bootstrap = new ServerBootstrap();

		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();

		bootstrap.group(boss, worker)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 1024)
				.option(ChannelOption.TCP_NODELAY, true)
				.childHandler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel channel) throws Exception {
						ChannelPipeline pipeline = channel.pipeline();
						pipeline.addLast(new EncodeHandler());
						pipeline.addLast(new ReceiveFileHandler());//先处理数据包
						pipeline.addLast(new DecodeHandler());//解析成JSONObject
						pipeline.addLast(new PojoHandler());//再处理信息包
						pipeline.addLast(new ChunkedWriteHandler());
					}
				});

		ChannelFuture future = bootstrap.bind(PORT).sync();
		if (future.isSuccess()) {
			System.out.println("端口绑定成功: "+PORT);
			RunServerDownTask thd = new RunServerDownTask();
			thd.setDaemon(true);
			thd.start();
		} else {
			System.out.println("端口绑定失败: "+PORT);
		}

		future.channel().closeFuture().sync();
	}
	
	
}

