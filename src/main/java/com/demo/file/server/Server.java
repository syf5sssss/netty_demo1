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
	 * �����ļ�����   key:ip   value:FileNameQueue
	 */
	public static Map<String, FileNameQueue> queue = new HashMap<String, FileNameQueue>();
	/**
	 * ��ǰ���ܵ��ļ���Ϣ  key:ip   value:TFile
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
						pipeline.addLast(new ReceiveFileHandler());//�ȴ������ݰ�
						pipeline.addLast(new DecodeHandler());//������JSONObject
						pipeline.addLast(new PojoHandler());//�ٴ�����Ϣ��
						pipeline.addLast(new ChunkedWriteHandler());
					}
				});

		ChannelFuture future = bootstrap.bind(PORT).sync();
		if (future.isSuccess()) {
			System.out.println("�˿ڰ󶨳ɹ�: "+PORT);
			RunServerDownTask thd = new RunServerDownTask();
			thd.setDaemon(true);
			thd.start();
		} else {
			System.out.println("�˿ڰ�ʧ��: "+PORT);
		}

		future.channel().closeFuture().sync();
	}
	
	
}

