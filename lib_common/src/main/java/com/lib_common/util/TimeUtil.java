package com.lib_common.util;

public class TimeUtil {

	/**
	 * 时间去秒
	 * 
	 * @param time
	 *            "hh:mm:ss"
	 * @return "hh:mm"
	 */
	public static String getTimeWithoutSecend(String time) {
		String result = null;
		try {
			result = time.substring(0, time.lastIndexOf(":"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 02天23小时42分09秒
	 * 
	 * @param timeCount 秒数
	 * @return 02天23小时42分09秒
	 */
	public static String getTimeFormat(long timeCount) {
		String result = null;
		long sec = timeCount % 60;
		timeCount = timeCount / 60;
		long min = timeCount % 60;
		timeCount = timeCount / 60;
		long hour = timeCount % 24;
		long day = timeCount / 24;
		result = day + "天" + hour + "小时" + min + "分" + sec + "秒";
		return result;
	}
}
