package com.lib_common.util;

public class DimensUtil {

	/**
	 * 数字已最少两位数形式显示
	 * 
	 * @param number
	 * @return
	 */
	public static String doubleDigit(int number) {
		String result = null;
		if (number < 10) {
			result = "0" + number;
		} else {
			result = "" + number;
		}
		return result;
	}
}
