package com.demo.file.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.util.UUID;

import com.alibaba.fastjson.JSONObject;
import com.demo.dto.TFile;
import com.demo.enums.FuncType;
import com.demo.enums.Info;
import com.demo.enums.TStatus;

public class RunServerDownTask extends Thread {

	/***
	 * 执行下载任务
	 */
	@Override
	public void run() {
		while (true) {
			try {
				ReceiveFileHandler.ChanGroup.forEach(ch -> {
					String sip = ((InetSocketAddress) ch.remoteAddress()).getAddress().getHostAddress();
					if (!Server.currentTFile.containsKey(sip)) {// 该连接没有下载任务
						// 一个连接同时只发送一个文件流
						FileNameQueue fnq = Server.queue.get(sip);
						Object obj = fnq.getCMD();// 查看该连接是否存在未执行任务
						if (obj != null) {
							TFile tf = (TFile) obj;
							tf.readlength = 0;
							String temp = UUID.randomUUID().toString().split("-")[0];
							try {
								// 创建接受文件的输出流
								tf.fops = new FileOutputStream(new File("./ServerFile/" + temp +"-" + tf.name));
								Server.currentTFile.put(sip, tf);// 更新该连接下载条目
								// 通知客户端可以发送某个文件
								JSONObject jo = new JSONObject();
								jo.put(Info.Type.name(), FuncType.File.name());
								jo.put(Info.FileName.name(), tf.name);
								jo.put(Info.FileSize.name(), tf.size);
								jo.put(Info.Status.name(), TStatus.AskOK.name());
								ch.writeAndFlush(jo);
								System.out.println("Server askok: " + jo.toJSONString());
							} catch (FileNotFoundException e) {
								System.out.println("创建文件输出流失败: " + "./server-receive-" + temp + tf.name);
								e.printStackTrace();
							}
						}
					}
				});
			} catch (Exception e) {
				System.out.println("获取下载任务睡眠出错: " + e.getMessage());
				e.printStackTrace();
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				System.out.println("获取下载任务睡眠出错: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

}
