package com.demo.file.server;

import java.util.concurrent.ArrayBlockingQueue;

public class FileNameQueue {
	
	/**
	 * ��С��ͼƬ�ļ����ȼ����
	 */
	ArrayBlockingQueue<Object> imageQueue = new ArrayBlockingQueue<Object>(2000);
	/**
	 * ������С�İ칫�ļ��е����ȼ�
	 */
	ArrayBlockingQueue<Object> officeQueue = new ArrayBlockingQueue<Object>(2000);
	/**
	 * �����ļ����ȼ����
	 */
	ArrayBlockingQueue<Object> fileQueue = new ArrayBlockingQueue<Object>(2000);
	
	/**
	 * isHaveCMD(�ж�����������Ƿ���ָ��) true��ʾ��ָ��,false��ʾ��ָ��
	 */
	public boolean isHaveCMD() {
		if (imageQueue.isEmpty() && officeQueue.isEmpty() && fileQueue.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * getCMD(����һ���������ָ�)
	 */
	public Object getCMD() {
		if (this.isHaveCMD()) {
			if (!imageQueue.isEmpty()) {
				return imageQueue.poll();
			}
			if (!officeQueue.isEmpty()) {
				return officeQueue.poll();
			}
			return fileQueue.poll();
		} else {
			return null;
		}
	}

	/**
	 * �������ͼƬ������,��ӳɹ�����true,ʧ�ܷ���false
	 */
	public boolean addImageQueueData(Object object) {
		boolean flag = false;
		try {
			// pullCmdQueue.put(object);//���queue������������ǰ�̻߳������ֱ���пռ��ټ���
			flag = imageQueue.offer(object);// ����offer�����Ǽ����ģ��ڵ�ǰ��Array��count�������������ʱ��Ҳ������������ʱ�򷵻�false�����û������ô�������ݵ�Array�У�����������true��
		} catch (Exception e) {
			System.err.println(e);
		}
		return flag;
	}

	/**
	 * ������񵽰칫������,��ӳɹ�����true,ʧ�ܷ���false
	 */
	public boolean addOfficeQueueData(Object object) {
		boolean flag = false;
		try {
			// blinkCmdQueue.put(object);//���queue������������ǰ�̻߳������ֱ���пռ��ټ���
			flag = officeQueue.offer(object);
		} catch (Exception e) {
			System.err.println(e);
		}
		return flag;
	}

	/**
	 * ��������ļ�������,��ӳɹ�����true,ʧ�ܷ���false
	 */
	public boolean addFileQueueData(Object object) {
		boolean flag = false;
		try {
			// pushCmdQueue.put(object);//���queue������������ǰ�̻߳������ֱ���пռ��ټ���
			flag = fileQueue.offer(object);
		} catch (Exception e) {
			System.err.println(e);
		}
		return flag;
	}

	/**
	 * չʾ�����������
	 */
	public String ShowQueueInfo() {
		return "image: "+this.imageQueue.size()+"  -office: "+this.officeQueue.size()+"  -file: "+this.fileQueue.size();
	}
	/**
	 * �õ�ͼƬ��������������
	 */
	public int getImageQueueDataSize() {
		return this.imageQueue.size();
	}

	/**
	 * �õ��칫��������������
	 */
	public int getOfficeQueueDataSize() {
		return this.officeQueue.size();
	}

	/**
	 * �õ��ļ���������������
	 */
	public int getfileQueueDataSize() {
		return this.fileQueue.size();
	}

	/**
	 * ���ͼƬ��������������
	 */
	public void clearImageQueue() {
		this.imageQueue.clear();
	}

	/**
	 * ��հ칫��������������
	 */
	public void clearOfficeQueue() {
		 this.officeQueue.clear();;
	}

	/**
	 * ����ļ���������������
	 */
	public void clearFileQueue() {
		 this.fileQueue.clear();;
	}

}
