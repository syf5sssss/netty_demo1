package com.demo.enums;

public enum TStatus {
	/**
	 * 请求
	 */
	Req,
	/**
	 * 回复正常,可以传输
	 */
	AskOK,
	/**
	 * 回复异常,不可传输
	 */
	AskNg,
	/**
	 * 查询当前下载任务
	 */
	Que;

}
