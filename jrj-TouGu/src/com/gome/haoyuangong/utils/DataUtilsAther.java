package com.gome.haoyuangong.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 处理时间的基础类
 * @author clh
 *
 */
public class DataUtilsAther {

	private static final long FACTOR_Days2Millis = 1000*60*60*24L;
	public static final int HOURS_24_MAKE = 0; //24小时制
	public static final int HOURS_12_MAKE = 1; //12小时制

	public static String getDateFromTimestamp(long date){
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(date);
		return getStringByCalendar(c);

	}

	/**  
	 * 获取当前系统时间  形如 2011-3-30 10:30:10
	 * @return  
	 */  
	public static String getDate(){   

		Calendar ca = Calendar.getInstance();   
		int year = ca.get(Calendar.YEAR);//获取年份   
		int month=ca.get(Calendar.MONTH);//获取月份    
		int day=ca.get(Calendar.DATE);//获取日   
		int minute=ca.get(Calendar.MINUTE);//分    
		int hour=ca.get(Calendar.HOUR_OF_DAY);//24小时制    
		int second=ca.get(Calendar.SECOND);//秒   

		String date = year + "-" + (month + 1 )+ "-" + day + " "+ hour + ":" + minute + ":" + second;   
		return date;   

	}
	
	public static String getAfterDayReturnCalendarStr(int fewDay){
		Calendar ca = Calendar.getInstance();   
		ca.roll(Calendar.DAY_OF_YEAR, fewDay);
		String year = String.valueOf(ca.get(Calendar.YEAR));//获取年份   
		String month= String.valueOf(ca.get(Calendar.MONTH) + 1);//获取月份    
		String day= String.valueOf(ca.get(Calendar.DATE));//获取日   
		
		if (Integer.parseInt(day) < 10) {
			day = "0" + day;
		}
		if (Integer.parseInt(month) < 10) {
			month = "0" + month;
		}
		return year + month + day;
	}

	/**
	 * 当前时间是否是本月的第一天
	 * @param calendar
	 * @return
	 */
	public static boolean isFirstDayOfMonth(Calendar calendar) {
		if (calendar.get(Calendar.DAY_OF_MONTH) == 1) { 
			return true; 
		} 
		return false;
	}

	/**
	 * 当前时间是否是本月的最后一天
	 * @param calendar
	 * @return
	 */
	public static boolean isLastDayOfMonth(Calendar calendar) {
		if(calendar.get(Calendar.DATE) == calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
			return true;
		}
		return false;
	}

	// 得到天数的毫秒数
	public static long getMilliSecondByDay(int days) {
		return days * FACTOR_Days2Millis;
	}

	// 得到小时的毫秒数
	public static long getMilliSecondByHourAndMins(int hour, int min) {
		return hour * 60 * 60 * 1000 + min * 60 * 1000;
	}



	public static String getStringByCalendar(Calendar calendar) {
		int m = calendar.get(Calendar.MONTH) + 1;
		String month = "";

		if (m >= 10)
			month = "" + m;
		else
			month = "0" + m;

		int d = calendar.get(Calendar.DAY_OF_MONTH);
		String day = "";
		if (d >= 10)
			day += d;
		else
			day = "0" + d;

		String hour = "";
		int h = calendar.get(Calendar.HOUR_OF_DAY);
		if (h < 10) {
			hour = "0" + h;
		} else {
			hour = "" + h;
		}
		//更改日期字符串到
		String minite = "";
		int mi = calendar.get(Calendar.MINUTE);
		if (mi < 10) {
			minite = "0" + mi;
		} else {
			minite = "" + mi;
		}

		String s = calendar.get(Calendar.YEAR) + month + day + hour+minite;
		return s;
	}

	//for getStringByCalendar()
	public static long getLongTimeByString(String time) {
		Calendar timeCalendar = Calendar.getInstance();
		try{
			int year = Integer.parseInt(time.substring(0 , 4));
			int mon = Integer.parseInt(time.substring(4 , 6)) - 1;
			int day = Integer.parseInt(time.substring(6 , 8));
			int hou = Integer.parseInt(time.substring(8 , 10));
			int min = Integer.parseInt(time.substring(10 , 12));
			timeCalendar.set(year, mon,day, hou, min);
		}catch(Exception e){
			e.printStackTrace();
		}
		return timeCalendar.getTimeInMillis();
	}
	
	public static String getDayStringByCalendar(Calendar calendar) {
		int m = calendar.get(Calendar.MONTH) + 1;
		String month = "";

		if (m >= 10)
			month = "" + m;
		else
			month = "0" + m;

		int d = calendar.get(Calendar.DAY_OF_MONTH);
		String day = "";
		if (d >= 10)
			day += d;
		else
			day = "0" + d;

		String s = calendar.get(Calendar.YEAR) + month + day;
		return s;
	}

	public static String getDayStringByCalendar(Long timeMillis) {
		if (timeMillis == 0) {
			return "19800000";
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeMillis);
		int m = calendar.get(Calendar.MONTH) + 1;
		String month = "";

		if (m >= 10)
			month = "" + m;
		else
			month = "0" + m;

		int d = calendar.get(Calendar.DAY_OF_MONTH);
		String day = "";
		if (d >= 10)
			day += d;
		else
			day = "0" + d;

		String s = calendar.get(Calendar.YEAR) + month + day;
		return s;
	}
	




	public static String getCurrentTime() {
		return getStringByCalendar(Calendar.getInstance());
	}

	public static long getCurrentTimeInMillis(){
		return (Calendar.getInstance().getTimeInMillis());
	}

	/**
	 * 计算两个时间间隔天数, timeInMillis2-timeInMillis1
	 * @param timeInMillis
	 * @return
	 */
	public static int getGapDays2Current(long timeInMillis1, long timeInMillis2){
		int gapDays = (int) ((timeInMillis2 - timeInMillis1) / FACTOR_Days2Millis);
		return gapDays;
	}

	/**
	 * 计算与当前时间的间隔天数
	 * @param timeInMillis
	 * @return
	 */
	public static int getGapDays2Current(long timeInMillis){
		long currten = getCurrentTimeInMillis();
		int gapDays = (int) ((currten - timeInMillis) / FACTOR_Days2Millis);
		return gapDays;
	}

	/**
	 * 
	 * @param milliseconds
	 * @return
	 * boolean
	 * zhengdianfang
	 * 2012-5-24下午02:18:04
	 * @comment 是否是今天
	 */
	public static boolean isToday(long milliseconds){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliseconds);
		if (getCurrentMonth() == getMonth(milliseconds) && getCurrentDay() == getDay(milliseconds)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param milliseconds
	 * @return
	 * boolean
	 * zhengdianfang
	 * 2012-5-24下午02:18:04
	 * @comment 是否是昨天
	 */
	public static boolean isYesterday(long milliseconds) {
		if (getCurrentMonth() == getMonth(milliseconds) && getCurrentDay()  == getDay(milliseconds) + 1) {
			return true;
		}
		return false;
	}


	public static int getCurrentDay(){
		return currentDateTime(System.currentTimeMillis(), Calendar.DAY_OF_MONTH);
	}

	public static int getCurrentMonth(){
		return currentDateTime(System.currentTimeMillis(), Calendar.MONTH);
	}

	/**
	 * 
	 * @param time
	 * @return
	 * int
	 * zhengdianfang
	 * 2012-5-24下午02:31:14
	 * @comment 获取日期
	 */
	public static int getDay(long time){
		return currentDateTime(time, Calendar.DAY_OF_MONTH);
	}

	/**
	 * 
	 * @param time
	 * @return
	 * int
	 * zhengdianfang
	 * 2012-5-24下午02:31:06
	 * @comment 获取月
	 */
	public static int getMonth(long time){
		return currentDateTime(time, Calendar.MONTH);
	}

	/**
	 * 
	 * @param time
	 * @return
	 * int
	 * zhengdianfang
	 * 2012-5-24下午02:30:59
	 * @comment 获取年
	 */
	@SuppressWarnings("unused")
	private int getYear(long time){
		return currentDateTime(time, Calendar.YEAR);
	}

	/**
	 * 
	 * @param time
	 * @return
	 * int
	 * zhengdianfang
	 * 2012-5-24下午02:27:49
	 * @comment 获取时，24小时制时间
	 */
	private static int getHourOf24(long time) {
		return currentDateTime(time, Calendar.HOUR_OF_DAY);
	}

	/**
	 * 
	 * @param time
	 * @return
	 * int
	 * zhengdianfang
	 * 2012-5-24下午02:27:49
	 * @comment 获取时，12小时制时间
	 */
	private static int getHourOf12(long time) {
		int hour = currentDateTime(time, Calendar.HOUR);
		if (hour == 0)
			hour = 12;
		return hour;
	}

	/**
	 * 
	 * @param time
	 * @return
	 * int
	 * zhengdianfang
	 * 2012-5-24下午02:27:49
	 * @comment 获取分
	 */
	private static int getMinute(long time) {
		return currentDateTime(time, Calendar.MINUTE);
	}

	private static int currentDateTime(long time, int type) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return calendar.get(type);
	}

	private static String getTimeStr(int hour, int minute, boolean is12) {
		StringBuilder temp = new StringBuilder();
		if (is12) {
			temp.append(hour);
		} else {
			temp.append(toTwoDigits(hour));
		}
		temp.append(":");
		temp.append(toTwoDigits(minute));
		return temp.toString();
	}

	/**
	 * 对小于10的数补0显示，如04
	 * 
	 * @param digit
	 * @return
	 */
	private static String toTwoDigits(int digit) {
		if (digit >= 10) {
			return "" + digit;
		} else {
			return "0" + digit;
		}
	}

	public static String getTime(long time, int timeFormat) {
		return timeFormat == HOURS_24_MAKE ? getTimeOf24(time) : getTimeOf12(time);
	}

	/**
	 * 得到12小时制时间，如7:30 AM，2:12 PM
	 * 
	 * @param time
	 * @return
	 */
	private static String getTimeOf12(long time) {
		return getAmPm(time) + " " + getTimeStr(getHourOf12(time), getMinute(time), true);
	}

	/**
	 * 获得AM/PM
	 * 
	 * @param time
	 * @return
	 */
	private static String getAmPm(long time) {
		SimpleDateFormat sf = new SimpleDateFormat("aa");
		return sf.format(new Date(time));
		// return getAmPmStr(time) == 0 ? mContext.getString(AM) : mContext.getString(PM);
	}

	/**
	 * 得到24小时制时间，如07:30，14:12
	 * 
	 * @param time
	 * @return
	 */
	private static String getTimeOf24(long time) {
		return getTimeStr(getHourOf24(time), getMinute(time), false);
	}

}
