package com.demo.dto;

import java.io.FileOutputStream;

public class TFile {
	/**
	 * �ļ�����
	 */
	public String name;
	/**
	 * �ļ���С
	 */
	public long size;
	/**
	 * �ͻ���ͨѶ����
	 */
	public String ip;
	/**
	 * �ļ�����������������ؿͻ����ļ���
	 */
	public FileOutputStream fops;
	/**
	 * �ļ��Ѷ�����
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
