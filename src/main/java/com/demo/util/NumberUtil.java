package com.demo.util;

public class NumberUtil {

	/**
	 * 获取两个long类型数字的百分比
	 */
	public static String GetPercent(long nummin, long nummax) {
		// 创建一个数值格式化对象
		java.text.NumberFormat numberformat = java.text.NumberFormat.getInstance();
		// 设置精确到小数点后2位
		numberformat.setMaximumFractionDigits(2);
		String result1 = numberformat.format((float) nummin / (float) nummax * 100);
		return result1 + "%";
	}
	
	/**
	 * 获取两个int类型数字的百分比
	 */
	public static String GetPercent(int nummin, int nummax) {
		// 创建一个数值格式化对象
		java.text.NumberFormat numberformat = java.text.NumberFormat.getInstance();
		// 设置精确到小数点后2位
		numberformat.setMaximumFractionDigits(2);
		String result1 = numberformat.format((float) nummin / (float) nummax * 100);
		return result1 + "%";
	}

}
