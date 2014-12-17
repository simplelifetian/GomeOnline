/**
 * 
 */
package com.gome.haoyuangong.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;

/**
 * @author tongzhui.peng
 * 
 */
@SuppressLint("SimpleDateFormat")
public final class DateUtils {

	public static String format(Date date, String format) {

		if (date == null || StringUtils.isEmpty(format)) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	public static String format(long timeMillion, String format){
		if(timeMillion < 31507200000L){
			timeMillion *= 1000;
		}
		return format(new Date(timeMillion), format);
	}

	/**
	 * 
	 * @param date
	 *            long 20140507
	 * @param format
	 * @return
	 */
	public static String format(long date, long time, String format) {
		try {
			Date d = getDateFromNumber(date,time);
			return format(d, format);
		} catch (Exception e) {
			
		}
		return null;
	}

	/**
	 * 处理符合时间格式字符串
	 * @param dateStr
	 * @param format
	 * @return
	 */
	public static Date parser(String dateStr, String format) {

		if (StringUtils.isEmpty(dateStr) || StringUtils.isEmpty(format)) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	

	public static String getAmonthAgo(String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.MONTH, -1);
		return format(calendar.getTime(), format);
	}

	public static String getAmonthAgo(Date endDate, String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endDate);
		calendar.add(Calendar.MONTH, -1);
		return format(calendar.getTime(), format);
	}

	/**
	 * 处理非时间格式字符串如：date=20141010 time=163022
	 * @param date
	 * @param time
	 * @return
	 */
	public static Date getDateFromNumber(long date, long time) {

		long year = date / 10000;
		long month = (date % 10000) / 100;
		long day = date % 100;
		long hour = time / 10000;
		long minute = (time % 10000) / 100;
		long second = time % 100;

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, (int) year);
		calendar.set(Calendar.MONTH, (int) month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, (int) day);

		calendar.set(Calendar.HOUR_OF_DAY, (int) hour);
		calendar.set(Calendar.MINUTE, (int) minute);
		calendar.set(Calendar.SECOND, (int) second);

		return calendar.getTime();
	}

	public static String getDateFromNumber(long date, long time, String format) {

		return DateUtils.format(getDateFromNumber(date, time), format);

	}

	public static boolean isInOneMonth(String startDateStr, String endDateStr) {

		Date start = parser(startDateStr, "yyyyMMdd");
		if (start == null) {
			return false;
		}
		Date end = parser(endDateStr, "yyyyMMdd");
		if (end == null) {
			return false;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(end);
		calendar.add(Calendar.MONTH, -1);

		Date monthAgo = calendar.getTime();

		if (start.compareTo(monthAgo) >= 0) {
			return true;
		}
		return false;
	}

	public static boolean isInOneMonth(Date startDate, Date endDate) {

		if (startDate == null || endDate == null) {
			return false;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endDate);
		calendar.add(Calendar.MONTH, -1);

		Date monthAgo = calendar.getTime();

		if (startDate.compareTo(monthAgo) >= 0) {
			return true;
		}
		return false;
	}
	/**
	 * 
	 * @param dateStr
	 * @param format
	 * @return null 时间格式错误
	 */
	public static String getTimeAgoString(String dateStr,String paramFormat){
		Date date = parser(dateStr, paramFormat);
		if(date == null){
			return dateStr;
		}
		long timeMillion = date.getTime();
		String result = getTimeAgoString(timeMillion,paramFormat);
		if(StringUtils.isEmpty(result)){
			return dateStr;
		}
		return result;
	}
	
	/**
	 * 
	 * @param dateStr
	 * @param format
	 * @return null 时间格式错误
	 */
	public static String getTimeAgoString(long timeMillion,String resultFormat){
		
		if(timeMillion < 31507200000L){
			timeMillion *= 1000;
		}
		long currTime = System.currentTimeMillis();
		
		long timeSub = currTime - timeMillion;
		if(timeSub < 0){
			return null;
		}
		
		if(timeSub < (3 * 60 * 1000)){
			return "刚刚";
		}
		
		if(timeSub < (60 * 60 * 1000)){
			long t = timeSub / (60 * 1000);
			return t+"分钟前";
		}
		
//		if(timeSub < (60 * 60 * 1000)){
//			return "半小时前";
//		}
		
		if(timeSub < (24 * 60 * 60 * 1000)){
			long t = timeSub / (60 * 60 * 1000);
			return t+"小时前";
		}
		
		if(timeSub < (2 * 24 * 60 * 60 * 1000)){
			
			return "昨天"+format(timeMillion,"HH:mm");
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeMillion);
		int defYear = calendar.get(Calendar.YEAR);
		
		Calendar currCalendar = Calendar.getInstance();
		currCalendar.setTimeInMillis(System.currentTimeMillis());
		int currYear = currCalendar.get(Calendar.YEAR);
		if(defYear == currYear){
			return format(timeMillion,"MM-dd HH:mm");
		}else{
			return format(timeMillion,"yyyy-MM-dd");
		}
		
	}
	
}
