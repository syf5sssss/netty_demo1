package com.demo.file.server;

import java.util.concurrent.ArrayBlockingQueue;

public class FileNameQueue {
	
	/**
	 * 较小的图片文件优先级最高
	 */
	ArrayBlockingQueue<Object> imageQueue = new ArrayBlockingQueue<Object>(2000);
	/**
	 * 正常大小的办公文件中等优先级
	 */
	ArrayBlockingQueue<Object> officeQueue = new ArrayBlockingQueue<Object>(2000);
	/**
	 * 大型文件优先级最低
	 */
	ArrayBlockingQueue<Object> fileQueue = new ArrayBlockingQueue<Object>(2000);
	
	/**
	 * isHaveCMD(判断任务队列中是否还有指令) true表示有指令,false表示无指令
	 */
	public boolean isHaveCMD() {
		if (imageQueue.isEmpty() && officeQueue.isEmpty() && fileQueue.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * getCMD(返回一个任务队列指令集)
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
	 * 添加任务到图片队列中,添加成功返回true,失败返回false
	 */
	public boolean addImageQueueData(Object object) {
		boolean flag = false;
		try {
			// pullCmdQueue.put(object);//如果queue容量已满，则当前线程会堵塞，直到有空间再继续
			flag = imageQueue.offer(object);// 首先offer方法是加锁的，在当前的Array的count等于数组的容量时，也就是数组满的时候返回false，如果没满，那么插入数据到Array中，最后解锁返回true。
		} catch (Exception e) {
			System.err.println(e);
		}
		return flag;
	}

	/**
	 * 添加任务到办公队列中,添加成功返回true,失败返回false
	 */
	public boolean addOfficeQueueData(Object object) {
		boolean flag = false;
		try {
			// blinkCmdQueue.put(object);//如果queue容量已满，则当前线程会堵塞，直到有空间再继续
			flag = officeQueue.offer(object);
		} catch (Exception e) {
			System.err.println(e);
		}
		return flag;
	}

	/**
	 * 添加任务到文件队列中,添加成功返回true,失败返回false
	 */
	public boolean addFileQueueData(Object object) {
		boolean flag = false;
		try {
			// pushCmdQueue.put(object);//如果queue容量已满，则当前线程会堵塞，直到有空间再继续
			flag = fileQueue.offer(object);
		} catch (Exception e) {
			System.err.println(e);
		}
		return flag;
	}

	/**
	 * 展示队列任务情况
	 */
	public String ShowQueueInfo() {
		return "image: "+this.imageQueue.size()+"  -office: "+this.officeQueue.size()+"  -file: "+this.fileQueue.size();
	}
	/**
	 * 得到图片队列中任务数量
	 */
	public int getImageQueueDataSize() {
		return this.imageQueue.size();
	}

	/**
	 * 得到办公队列中任务数量
	 */
	public int getOfficeQueueDataSize() {
		return this.officeQueue.size();
	}

	/**
	 * 得到文件队列中任务数量
	 */
	public int getfileQueueDataSize() {
		return this.fileQueue.size();
	}

	/**
	 * 清空图片队列中任务数量
	 */
	public void clearImageQueue() {
		this.imageQueue.clear();
	}

	/**
	 * 清空办公队列中任务数量
	 */
	public void clearOfficeQueue() {
		 this.officeQueue.clear();;
	}

	/**
	 * 清空文件队列中任务数量
	 */
	public void clearFileQueue() {
		 this.fileQueue.clear();;
	}

}
