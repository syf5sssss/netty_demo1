package com.demo.util;

public class NumberUtil {

	/**
	 * ��ȡ����long�������ֵİٷֱ�
	 */
	public static String GetPercent(long nummin, long nummax) {
		// ����һ����ֵ��ʽ������
		java.text.NumberFormat numberformat = java.text.NumberFormat.getInstance();
		// ���þ�ȷ��С�����2λ
		numberformat.setMaximumFractionDigits(2);
		String result1 = numberformat.format((float) nummin / (float) nummax * 100);
		return result1 + "%";
	}
	
	/**
	 * ��ȡ����int�������ֵİٷֱ�
	 */
	public static String GetPercent(int nummin, int nummax) {
		// ����һ����ֵ��ʽ������
		java.text.NumberFormat numberformat = java.text.NumberFormat.getInstance();
		// ���þ�ȷ��С�����2λ
		numberformat.setMaximumFractionDigits(2);
		String result1 = numberformat.format((float) nummin / (float) nummax * 100);
		return result1 + "%";
	}

}
