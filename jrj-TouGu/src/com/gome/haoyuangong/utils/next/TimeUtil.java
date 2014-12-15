
package com.gome.haoyuangong.utils.next;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.text.style.TextAppearanceSpan;

public class TimeUtil {
	
//	public static String formatSendAtTime(Context context, long t){
//        Time time = new Time();
//        time.set(t);
//        int year = time.year;
//        int month = time.month;
//        int day = time.monthDay;
//        int hour = time.hour;
//        int minute = time.minute;
//        
//        long current_time = System.currentTimeMillis();
//        
//        time.set(current_time);
//        int now_year = time.year;
//        int now_month = time.month;
//        int now_day = time.monthDay;
//        int now_hour = time.hour;
//        int now_minute = time.minute;
//        
//        long diff = t-current_time;
////        if(diff<=0 || Math.abs(diff)<60*1000){
////        	return context.getString(R.string.send_time_less_than_one_minute);
////        }else if(Math.abs(diff)<60*60*1000){
////        	return context.getString(R.string.send_time_less_than_one_hour,diff/(1000*60));
////        }else if(year!=now_year){
////        	return context.getString(R.string.send_time_different_year,year,month+1,day);
////        }else if(now_month!=month || now_day!=day){
////        	return context.getString(R.string.send_time_same_year, month+1, day);
////        }else{
////        	return context.getString(R.string.send_time_same_day, hour, minute);
////        }  
//        
//	}
	
	public static String formatVideoRecordingTime(long t){
		int m = (int)t/60;
		int s = (int)t - m*60;
		return String.format("%02d:%02d", m,s);
	}

    public static String getTimeString(long t) {
//        final Calendar c = Calendar.getInstance();
//        c.setTimeInMillis(t);
//        int h = c.get(Calendar.HOUR_OF_DAY);
//        int m = c.get(Calendar.MINUTE);
//        int s = c.get(Calendar.SECOND);
//        return String.valueOf(h) + ":" + String.valueOf(m) + ":" + String.valueOf(s);
        Time time = new Time();
        time.set(t);
        return time.format("%T");
    }

    public static String getDateString(long t) {
//        final Calendar c = Calendar.getInstance();
//        c.setTimeInMillis(t);
//        int y = c.get(Calendar.YEAR);
//        int m = c.get(Calendar.MONTH) + 1;
//        int d = c.get(Calendar.DAY_OF_MONTH);
//        return String.valueOf(y) + "-" + String.valueOf(m) + "-" + String.valueOf(d);
        Time time = new Time();
        time.set(t);
        return time.format("%y-%m-%d");
    }
    
    public static String getDateString2(long t){
        Time time = new Time();
        time.set(t);
        return time.format("%Y-%m-%d");
    }

    public static boolean isToday(long t) {
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(t);
        int y = c.get(Calendar.YEAR);
        int dy = c.get(Calendar.DAY_OF_YEAR);
        long time = System.currentTimeMillis();
        c.setTimeInMillis(time);
        int cy = c.get(Calendar.YEAR);
        int cdy = c.get(Calendar.DAY_OF_YEAR);
        if (y == cy && dy == cdy) {
            return true;
        }
        return false;
    }
   
    public static String time2String(long time) {
    	SimpleDateFormat df = new SimpleDateFormat("yy-MM-dd HH:mm");
        Calendar toyear = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        toyear.set(Calendar.MONTH, Calendar.JANUARY);
        toyear.set(Calendar.DATE, 1);
        toyear.set(Calendar.HOUR_OF_DAY, 0);
        toyear.set(Calendar.MINUTE, 0);
        toyear.set(Calendar.SECOND, 0);
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        if (time > toyear.getTime().getTime() && time < today.getTime().getTime())
            df = new SimpleDateFormat("MM-dd");
        else if (time > today.getTime().getTime())
            df = new SimpleDateFormat("HH:mm");
        return df.format(new Date(time));
    }
    public static String time2String4Trans(long t,Context context) {
    	String CURRENT_YEAR="MM-dd HH:mm";
    	String BEFORE_YEAR="yyyy-MM-dd HH:mm";
    	String lRecentDateString="";
    	String lDateTimeFormat=" HH:mm";
    	long currTime = System.currentTimeMillis();
        Time time = new Time();
            time.set(currTime);
            int currYear = time.year;
            int currDay = time.yearDay;
            time.set(t);
            int day = time.yearDay;
            if(day == currDay){
//            	lRecentDateString= context.getString(R.string.list_today_format);
            }else if(currDay - day == 1){
//            	lRecentDateString= context.getString(R.string.list_yesterday_format);
            }else{
            	lRecentDateString="";
                if(time.year < currYear){
                	lDateTimeFormat = BEFORE_YEAR;
                }else{
                	lDateTimeFormat = CURRENT_YEAR;
                }
            }

           SimpleDateFormat df = new SimpleDateFormat(lDateTimeFormat);
           return lRecentDateString+df.format(new Date(t));
    }
//    public static String getFormatDateStr(long t, Context context){
//        long currTime = System.currentTimeMillis();
//        int overTime = (int) ((currTime - t)/1000);
//        if(LogUtil.IDBG){
//            LogUtil.i("MessageItem","currTime , overTime = "+currTime+" "+overTime);
//        }
//
////        if(overTime/3600 != 0){
//            Time time = new Time();
//            time.set(currTime);
//            int currYear = time.year;
//            int currDay = time.yearDay;
//            time.set(t);
//            int day = time.yearDay;
//            if(day == currDay){
//            	return context.getString(R.string.list_today_format);
//            }else if(currDay - day == 1){
//                return context.getString(R.string.list_yesterday_format);
//            }else if(currDay - day == 2){
//                return context.getString(R.string.list_before_yesterday_format);
//            }else{
//                if(time.year < currYear){
//                    return time.format(context.getString(R.string.list_before_year_format));
//                }else{
//                    return time.format(context.getString(R.string.list_this_year_format));
//                }
//            }
////        }
////        else
////        {
////        	return null;
////        }
//    }
    
//    public static String getFormatDateTimeStrIgnoreFuture(long t, Context context) {
//    	if(t > (System.currentTimeMillis() - 1000)){
//    		t = System.currentTimeMillis() - 1000;
//    	}
//    	
//    	return getFormatDateTimeStr(t, context);
//    }
    
//    public static String getFormatDateTimeStr(long t, Context context) {
//        long currTime = System.currentTimeMillis();
//        int overTime = (int) ((currTime - t) / 1000);
//        if (overTime == 0) {
//            return context.getString(R.string.just_now);
//        }
//        if (overTime < 0) {
//            overTime = -overTime;
//            if (overTime < 60) {
//                return context.getString(R.string.list_right_away);
//            }
//            if (overTime < 3600) {
//                return context.getString(R.string.list_minutes_later, overTime / 60);
//            }
//            Time tCurrTime = new Time();
//            tCurrTime.set(currTime);
//            Time time = new Time();
//            time.set(t);
//            String language = context.getString(R.string.time_language);
//            if ("cn".equals(language)) {
//                if (tCurrTime.year == time.year) {
//                    if (tCurrTime.yearDay == time.yearDay) {
//                        return time.format("%R");
//                    }
//                    return time.format("%m-%d");
//                }
//                return time.format("%y-%m-%d");
//            }
//            if (tCurrTime.year == time.year) {
//                if (tCurrTime.yearDay == time.yearDay) {
//                    return time.format("%I:%M%p");
//                }
//                return time.format("%b %d");
//            }
//            return time.format("%b %d %y");
//        }
//        
//        String language = context.getString(R.string.time_language);
//        if ("cn".equals(language)) {
//            if (overTime < 60) {
//                return overTime + context.getString(R.string.list_seconds_passed);
//            }
//            if (overTime < 3600) {
//                return overTime / 60 + context.getString(R.string.list_minutes_passed);
//            }
//            Time tCurrTime = new Time();
//            tCurrTime.set(currTime);
//            Time time = new Time();
//            time.set(t);
//            TimeZone tz = TimeZone.getDefault();
//            int currJulianDay = Time.getJulianDay(tCurrTime.toMillis(false), tz.getRawOffset());
//            int julianDay = Time.getJulianDay(time.toMillis(false), tz.getRawOffset());
//            int dayGap = currJulianDay - julianDay;
//            if (dayGap == 0) {
//                return time.format("%R");
//            } else if (dayGap == 1) {
//                return context.getString(R.string.list_yesterday) + time.format(" %R");
//            } else if (dayGap == 2) {
//                return context.getString(R.string.list_before_yesterday) + time.format(" %R");
//            } else if (2 < dayGap && dayGap < 7) {
//                TypedArray chineseWeekDayArray = context.getResources().obtainTypedArray(
//                        R.array.weekday);
//                String chineseWeekDay = chineseWeekDayArray.getString(time.weekDay);
//                if (tCurrTime.weekDay < time.weekDay || time.weekDay == Time.SUNDAY) {
//                    return time.format(context.getString(R.string.list_last_week_format)
//                            + chineseWeekDay);
//                } else {
//                    return chineseWeekDay;
//                }
//            }
//            if (tCurrTime.year == time.year) {
//                return time.format("%m-%d");
//            } else {
//                return time.format("%y-%m-%d");
//            }
//        }
//        Time tCurrTime = new Time();
//        tCurrTime.set(currTime);
//        Time time = new Time();
//        time.set(t);
//        if (tCurrTime.year == time.year) {
//            if (tCurrTime.yearDay == time.yearDay) {
//                return time.format("%I:%M%p");
//            }
//            return time.format("%b %d");
//        }
//        return time.format("%b %d %y");
//    }
    
//    public static String getFormatDateAndTimeStr(long t, Context context) {
//        long currTime = System.currentTimeMillis();
//        int overTime = (int) ((currTime - t) / 1000);
//        if (overTime <= 0) {
//            return context.getString(R.string.just_now);
//        }
//        
//        String language = context.getString(R.string.time_language);
//        if ("cn".equals(language)) {
//            
//            TypedArray chineseWeekDayArray = context.getResources().obtainTypedArray(
//                    R.array.weekday);
//            
//            if (overTime / 3600 == 0) {
//                if (overTime / 60 == 0) {
//                    return overTime + context.getString(R.string.list_seconds_passed);
//                } else {
//                    return overTime / 60 + context.getString(R.string.list_minutes_passed);
//                }
//            } else {
//                Time tCurrTime = new Time();
//                tCurrTime.set(currTime);
//                Time time = new Time();
//                time.set(t);
//
//                TimeZone tz = TimeZone.getDefault();
//                int currJulianDay = Time.getJulianDay(tCurrTime.toMillis(false), tz.getRawOffset());
//                int julianDay = Time.getJulianDay(time.toMillis(false), tz.getRawOffset());
//                int dayGap = currJulianDay - julianDay;
//
//                if (dayGap == 0) {
//                    return time.format("%R");
//                } else if (dayGap == 1) {
//                    return context.getString(R.string.list_yesterday) + time.format(" %R");
//                } else if (dayGap == 2) {
//                    return context.getString(R.string.list_before_yesterday) + time.format(" %R");
//                } else if (2 < dayGap && dayGap < 7) {
//                    String chineseWeekDay = chineseWeekDayArray.getString(time.weekDay);
//                    if (tCurrTime.weekDay < time.weekDay || time.weekDay == Time.SUNDAY) {
//                        return time.format(context.getString(R.string.list_last_week_format) + chineseWeekDay+" %R");
//                    } else {
//                        return chineseWeekDay+time.format(" %R");
//                    }
//                }
//                if (tCurrTime.year == time.year) {
//                    return time.format("%m-%d %R");
//                } else {
//                    return time.format("%y-%m-%d %R");
//                }
//            }
//        } else {
//            Time time = new Time();
//            time.set(currTime);
//            int currYear = time.year;
//            int currDay = time.yearDay;
//            time.set(t);
//            int day = time.yearDay;
//            if (day == currDay) {
//                return time.format("%I:%M%p");
//            } else {
//                if (time.year < currYear) {
//                    return time.format("%b %d %y %I:%M%p");
//                } else {
//                    return time.format("%b %d %I:%M%p");
//                }
//            }
//        }
//    }
    
//	public static CharSequence getTimeTitleString(Context context,long t) {
//		long currTime =System.currentTimeMillis();
//		Time tCurrTime = new Time();
//        tCurrTime.set(currTime);
//        Time time = new Time();
//        time.set(t);
//		String longDate="yyyy-MM-dd";
//		String shortDate="MM-dd";
//		String timeFormat=" kk:mm";
//		
//		CharSequence timeC = DateFormat.format(timeFormat,t);
//		CharSequence dateC =DateFormat.format(shortDate,t);
//		
//        if(time.year!=tCurrTime.year){
//        	dateC=DateFormat.format(longDate,t);
//        }
//        int dateLen=dateC.length();
//        int timeLen=timeC.length();
//		SpannableStringBuilder dateStr = new SpannableStringBuilder();
//		dateStr.append(dateC);
//		dateStr.append(timeC);
//		TextAppearanceSpan dateSp = new TextAppearanceSpan(context,
//				R.style.TimeMenuTitleDateFormat);
//		TextAppearanceSpan TimeSp = new TextAppearanceSpan(context,
//				R.style.TimeMenuTitleTimeFormat);
//		dateStr.setSpan(dateSp, 0, dateLen, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); 
//		dateStr.setSpan(TimeSp, dateLen, dateLen+timeLen, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); 
//		return dateStr;
//
//	}
    
//    public static String getFormatDateTimeStr(long t, Context context){
//        return formatTimeStampString(context, t);
//    }
//    
//    public static String formatTimeStampString(Context context, long when) {
//        return formatTimeStampString(context, when, false);
//    }
//
//    public static String formatTimeStampString(Context context, long when, boolean fullFormat) {
//        Time then = new Time();
//        then.set(when);
//        Time now = new Time();
//        now.setToNow();
//
//        // Basic settings for formatDateTime() we want for all cases.
//        int format_flags = DateUtils.FORMAT_NO_NOON_MIDNIGHT |
//                           DateUtils.FORMAT_ABBREV_ALL |
//                           DateUtils.FORMAT_CAP_AMPM;
//
//        // If the message is from a different year, show the date and year.
//        if (then.year != now.year) {
//            format_flags |= DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE;
//        } else if (then.yearDay != now.yearDay) {
//            // If it is from a different day than today, show only the date.
//            format_flags |= DateUtils.FORMAT_SHOW_DATE;
//        } else {
//            // Otherwise, if the message is from today, show the time.
//            format_flags |= DateUtils.FORMAT_SHOW_TIME;
//        }
//
//        // If the caller has asked for full details, make sure to show the date
//        // and time no matter what we've determined above (but still make showing
//        // the year only happen if it is a different year from today).
//        if (fullFormat) {
//            format_flags |= (DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME);
//        }
//
//        return DateUtils.formatDateTime(context, when, format_flags);
//    }
	
	 public static String getYMDString(long t){
	        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
	        return sdf.format(new Date(t));
	    }
}
