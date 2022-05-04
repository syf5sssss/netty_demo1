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
	 * �����ļ� Map   key:filename   value:��һ�εǼǵ��ļ�·��
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
						pipeline.addLast(new ReceiveFileHandler());//�Ƚ����ļ��ļ���
						pipeline.addLast(new DecodeHandler());//������JSONObject
						pipeline.addLast(new PojoHandler());//�ٴ�����Ϣ��
						pipeline.addLast(new SendFileHandler());
						pipeline.addLast(new ChunkedWriteHandler());
					}
				});

		ChannelFuture future = bootstrap.connect(HOST, PORT).sync();
		if (future.isSuccess()) {
			System.out.println("���ӷ������ɹ�: "+PORT);
		} else {
			System.out.println("���ӷ�����ʧ��: "+PORT);
		}

		future.channel().closeFuture().sync();
	}
}

