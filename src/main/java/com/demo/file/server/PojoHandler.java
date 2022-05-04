package com.demo.file.server;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.demo.dto.TFile;
import com.demo.enums.FuncType;
import com.demo.enums.Info;
import com.demo.enums.Priority;
import com.demo.enums.TStatus;
import com.demo.util.ConnUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class PojoHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("Server Pojo - rec: " + msg);
		String ip = ConnUtil.GetIp(ctx);
		System.out.println("Զ��IP: "+ip);
		JSONObject jo = (JSONObject)msg;
		String func = jo.getString(Info.Type.name());
		if(!StringUtils.isBlank(func)) {
			if(func.equals(FuncType.File.name())) {
				if(TStatus.Req.name().equals(jo.get(Info.Status.name()))) {
					Long size = jo.getLong(Info.FileSize.name());
					String name = jo.getString(Info.FileName.name());
					String Prior = jo.getString(Info.Priority.name());
					System.out.println("�ļ�����: "+name);
					System.out.println("�ļ���С: "+size);
					System.out.println("�ļ����ȼ�: "+Prior);
					TFile tf = new TFile();
					tf.name = name;
					tf.size = size;
					tf.ip = ip;
					FileNameQueue qu = Server.queue.get(ip);
					if(!StringUtils.isBlank(Prior)) {
						if(Prior.equals(Priority.Image.name())) {
							qu.addImageQueueData(tf);
						}else if(Prior.equals(Priority.Office.name())) {
							qu.addOfficeQueueData(tf);
						}else {
							qu.addFileQueueData(tf);
						}
					}else {
						qu.addFileQueueData(tf);
					}
					System.out.println("����������: "+qu.ShowQueueInfo());
				}else if(TStatus.Que.name().equals(jo.get(Info.Status.name()))){
					System.out.println("��ѯ��ǰ�����������TD");
				}else {
					System.out.println("�Ƿ���״̬����,������������");
				}
			}else if(func.equals(FuncType.Pojo.name())) {
				System.out.println("FuncType����: Pojo");
			}else {
				System.out.println("�Ƿ��ķ�������,������������");
			}
		}else {
			System.out.println("û�з�������,������������");
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelActive(ctx);
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
