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
	 * ִ����������
	 */
	@Override
	public void run() {
		while (true) {
			try {
				ReceiveFileHandler.ChanGroup.forEach(ch -> {
					String sip = ((InetSocketAddress) ch.remoteAddress()).getAddress().getHostAddress();
					if (!Server.currentTFile.containsKey(sip)) {// ������û����������
						// һ������ͬʱֻ����һ���ļ���
						FileNameQueue fnq = Server.queue.get(sip);
						Object obj = fnq.getCMD();// �鿴�������Ƿ����δִ������
						if (obj != null) {
							TFile tf = (TFile) obj;
							tf.readlength = 0;
							String temp = UUID.randomUUID().toString().split("-")[0];
							try {
								// ���������ļ��������
								tf.fops = new FileOutputStream(new File("./ServerFile/" + temp +"-" + tf.name));
								Server.currentTFile.put(sip, tf);// ���¸�����������Ŀ
								// ֪ͨ�ͻ��˿��Է���ĳ���ļ�
								JSONObject jo = new JSONObject();
								jo.put(Info.Type.name(), FuncType.File.name());
								jo.put(Info.FileName.name(), tf.name);
								jo.put(Info.FileSize.name(), tf.size);
								jo.put(Info.Status.name(), TStatus.AskOK.name());
								ch.writeAndFlush(jo);
								System.out.println("Server askok: " + jo.toJSONString());
							} catch (FileNotFoundException e) {
								System.out.println("�����ļ������ʧ��: " + "./server-receive-" + temp + tf.name);
								e.printStackTrace();
							}
						}
					}
				});
			} catch (Exception e) {
				System.out.println("��ȡ��������˯�߳���: " + e.getMessage());
				e.printStackTrace();
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				System.out.println("��ȡ��������˯�߳���: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

}
