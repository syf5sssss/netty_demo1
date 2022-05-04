package com.demo.file.server;

import java.net.InetSocketAddress;

import com.demo.dto.Message;
import com.demo.dto.TFile;
import com.demo.util.ConnUtil;
import com.demo.util.NumberUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ReceiveFileHandler extends ChannelInboundHandlerAdapter {
	
	/**
	 * ����������ͻ��˵�ͨѶ�����������Ԫ�ض��ǿ������ӣ�
	 */
	public static ChannelGroup ChanGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	/**
	 * �¼ӵ�����
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		String host = ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress();
		System.out.println("host: " + host);
		System.out.println("ChanGroup size: " + ChanGroup.size());
		ChanGroup.forEach(ch -> {
			String ctr = ((InetSocketAddress) ch.remoteAddress()).getAddress().getHostAddress();
			System.out.println("ch: "+ctr);
			if(host.equals(ctr)) {
				ChanGroup.remove(ch);
				System.out.println("ɾ��֮ǰ������,ȷ��ʹ�����µ�����"+ch.toString());
			}
        });
		ChanGroup.add(ctx.channel());
		System.out.println("ChanGroup after size:"+ChanGroup.size());
		ChanGroup.forEach(ch -> {
			System.out.println("show: "+ch.toString());
        });
		if(!Server.queue.containsKey(host)) {
			Server.queue.put(host, new FileNameQueue());
			System.out.println("����������һ������: "+host);
		}else {
			System.out.println("���������������һ������"+host+"  -"+Server.queue.get(host).ShowQueueInfo());
		}
	}

	/**
	 * ���Ӷ�ȡ��Ϣ
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			ByteBuf byteBuf = (ByteBuf) msg;
			int type = byteBuf.getInt(0);
			System.out.println("type: " + type);
			if (Message.TYPE == type) {// ��Ϣ
				System.out.println("Server RecFile ���´��� ");
				super.channelRead(ctx, msg);
			} else {// ���ݰ�
				System.out.println("RecFile read �������ݰ�");
				TFile tf = Server.currentTFile.get(ConnUtil.GetIp(ctx));
				tf.readlength += byteBuf.readableBytes();
				System.out.println("���ؽ���: "+NumberUtil.GetPercent(tf.readlength, tf.size));
				byte[] bytes = new byte[byteBuf.readableBytes()];
				byteBuf.readBytes(bytes);
				tf.fops.write(bytes);
				byteBuf.release();
				if (tf.readlength >= tf.size) {
					System.out.println("�ļ��������...");
					tf.fops.close();
					Server.currentTFile.remove(ConnUtil.GetIp(ctx));
					System.out.println("ɾ��������ִ�е�����: "+ConnUtil.GetIp(ctx));
				}
			}
		} catch (Exception e) {
			System.out.println("Server RecFile Error: "+e.getMessage());
			e.printStackTrace();
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
