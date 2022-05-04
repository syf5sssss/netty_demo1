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
	 * 用来操作与客户端的通讯（该连接组的元素都是可用连接）
	 */
	public static ChannelGroup ChanGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	/**
	 * 新加的连接
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
				System.out.println("删除之前的连接,确保使用最新的连接"+ch.toString());
			}
        });
		ChanGroup.add(ctx.channel());
		System.out.println("ChanGroup after size:"+ChanGroup.size());
		ChanGroup.forEach(ch -> {
			System.out.println("show: "+ch.toString());
        });
		if(!Server.queue.containsKey(host)) {
			Server.queue.put(host, new FileNameQueue());
			System.out.println("任务队列添加一条连接: "+host);
		}else {
			System.out.println("任务队列重新连接一条连接"+host+"  -"+Server.queue.get(host).ShowQueueInfo());
		}
	}

	/**
	 * 连接读取信息
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			ByteBuf byteBuf = (ByteBuf) msg;
			int type = byteBuf.getInt(0);
			System.out.println("type: " + type);
			if (Message.TYPE == type) {// 信息
				System.out.println("Server RecFile 向下传递 ");
				super.channelRead(ctx, msg);
			} else {// 数据包
				System.out.println("RecFile read 处理数据包");
				TFile tf = Server.currentTFile.get(ConnUtil.GetIp(ctx));
				tf.readlength += byteBuf.readableBytes();
				System.out.println("下载进度: "+NumberUtil.GetPercent(tf.readlength, tf.size));
				byte[] bytes = new byte[byteBuf.readableBytes()];
				byteBuf.readBytes(bytes);
				tf.fops.write(bytes);
				byteBuf.release();
				if (tf.readlength >= tf.size) {
					System.out.println("文件接收完成...");
					tf.fops.close();
					Server.currentTFile.remove(ConnUtil.GetIp(ctx));
					System.out.println("删掉该连接执行的任务: "+ConnUtil.GetIp(ctx));
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
