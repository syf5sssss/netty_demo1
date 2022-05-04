package com.demo.file.client;

import java.io.File;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.demo.enums.FuncType;
import com.demo.enums.Info;
import com.demo.enums.Priority;
import com.demo.enums.TStatus;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.DefaultFileRegion;

public class PojoHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		JSONObject jo = (JSONObject)msg;
		System.out.println("pojo rec: "+jo.toJSONString());
		String func = jo.getString(Info.Type.name());
		if(!StringUtils.isBlank(func)) {
			if(func.equals(FuncType.File.name())) {
				if(TStatus.AskOK.name().equals(jo.get(Info.Status.name()))) {
					Long size = jo.getLong(Info.FileSize.name());
					String name = jo.getString(Info.FileName.name());
					System.out.println("文件名称: "+name);
					System.out.println("文件大小: "+size);
					String address = Client.filelist.get(name);
					File file = new File(address);
					Date d1 = new Date();
					long t1 = d1.getTime();
					System.out.println("开始发送文件 "+name+" "+d1.toString());
					DefaultFileRegion fileRegion = new DefaultFileRegion(file, 0, file.length());
					ctx.writeAndFlush(fileRegion).addListener(future -> {
						if (future.isSuccess()) {
							Date d2 = new Date();
							long t2 = d2.getTime();
							System.out.println(file.getName()+" 发送完成..."+d2.toString()+"   耗时: "+(t2-t1));
						}
					});
				}else if(TStatus.Que.name().equals(jo.get(Info.Status.name()))){
					System.out.println("查询当前下载任务情况TD");
				}else {
					System.out.println("非法的状态类型,本次请求作废");
				}
			}else if(func.equals(FuncType.Pojo.name())) {
				System.out.println("FuncType名称: Pojo");
			}else {
				System.out.println("非法的方法类型,本次请求作废");
			}
		}else {
			System.out.println("没有方法类型,本次请求作废");
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		JSONObject jo = new JSONObject();
		String address = "D:\\SQLEXPRADV_x64_CHS.exe";//SQLEXPRADV_x64_CHS.exe   jiejie.jpg
		File file = new File(address);
		jo.put(Info.Type.name(), FuncType.File.name());
		jo.put(Info.Priority.name(), Priority.Image.name());
		jo.put(Info.FileName.name(), file.getName());
		jo.put(Info.FileSize.name(), file.length());
		jo.put(Info.Status.name(), TStatus.Req.name());
		String message = JSONObject.toJSONString(jo);
		Client.filelist.put(file.getName(), address);
		ctx.writeAndFlush(jo);
		System.out.println("Client Pojo send :"+message);
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

