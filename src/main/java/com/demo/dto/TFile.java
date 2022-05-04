package com.demo.dto;

import java.io.FileOutputStream;

public class TFile {
	/**
	 * 文件名称
	 */
	public String name;
	/**
	 * 文件大小
	 */
	public long size;
	/**
	 * 客户端通讯连接
	 */
	public String ip;
	/**
	 * 文件输出流（服务器下载客户端文件）
	 */
	public FileOutputStream fops;
	/**
	 * 文件已读长度
	 */
	public long readlength;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public FileOutputStream getFops() {
		return fops;
	}
	public void setFops(FileOutputStream fops) {
		this.fops = fops;
	}
	public long getReadlength() {
		return readlength;
	}
	public void setReadlength(long readlength) {
		this.readlength = readlength;
	}
	@Override
	public String toString() {
		return "TFile [name=" + name + ", size=" + size + ", ip=" + ip + ", fops=" + fops + ", readlength=" + readlength
				+ "]";
	}


}
