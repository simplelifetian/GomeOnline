package com.gome.haoyuangong.layout.self;

import android.content.Context;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.os.Environment.getExternalStoragePublicDirectory;

/**
 * Created by menghui on 2014/8/6.
 */
public class Function {
    public static float convertIntValueToFloat(int value, int dignum) {
        return (float) (value / Math.pow(10.0f, dignum));
    }

    public static String FormatFloat(float value, int dignum) {
        String fmt = "0";
        if (dignum > 0) {
            fmt = "0.%0" + String.valueOf(dignum) + "d";
            fmt = String.format(fmt, 0);
        }
        NumberFormat format = new DecimalFormat(fmt);
        return format.format(value);
    }

    public static int px2sp(Context context, float pxValue) {
//        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
//        return (int) (pxValue / fontScale + 0.5f);
        return (int) pxValue / 3;
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
    
    public static int getFitPx(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue * scale / 3 + 0.5f);
    }

    public static void serialize(Object obj, String fileName) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(obj);
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            fileOutputStream.write(byteArrayOutputStream.toByteArray());
            fileOutputStream.flush();
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object deserialize(String fileName) {
        Object obj = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            try {
                obj = objectInputStream.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            fileInputStream.close();
            objectInputStream.close();
        } catch (IOException e) {
            obj = null;
            e.printStackTrace();
        }
        return obj;
    }
    
    /**
     * 是否在wifi下
     * @param mContext
     * @return
     */
    public static boolean isWifi(Context mContext) {  
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext  
                .getSystemService(Context.CONNECTIVITY_SERVICE);  
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();  
        if (activeNetInfo != null  
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {  
            return true;  
        }  
        return false;  
    }  

    /**
     * 将数值型日期转换为 yyyy-mm-dd格式
     *
     * @param date 数值型日期
     * @return 字符型日期
     */
    public static String convertDate(int date) {
        String sdate = String.valueOf(date);
        return String.format("%s-%s-%s", sdate.substring(0, 4), sdate.substring(4, 6), sdate.substring(6, 8));
    }

    /**
     * 将数值型日期转换为 mm-dd格式
     *
     * @param date 数值型日期
     * @return 字符型日期
     */
    public static String convertDateExceptYear(int date) {
        String sdate = String.valueOf(date);
        return String.format("%s-%s", sdate.substring(4, 6), sdate.substring(6, 8));
    }

    /**
     * 将数值型时间转为 HH:mm格式
     *
     * @param time 数值型时间
     * @return 字符型时间
     */
    public static String convertTime(int time) {
        return String.format("%d:%02d", time / 100, time % 100);
    }

    /**
     * 将字符串转为日期
     *
     * @param dt     日期字符串
     * @param format 格式
     * @return
     */
    public static Date convertStringToDate(String dt, String format) {
        DateFormat f = new SimpleDateFormat(format);
        try {
            return f.parse(dt);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int minutesBetweenDates(Date startDate, Date endDate) {
        Calendar sCalendar = Calendar.getInstance();
        sCalendar.setTime(startDate);
        Calendar eCalendar = Calendar.getInstance();
        eCalendar.setTime(endDate);
        int ms = (int) (eCalendar.getTime().getTime() - sCalendar.getTime().getTime());
        return ms / 1000 / 60;
    }

    /**
     * 判断网络状态
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mobNetInfo != null && mobNetInfo.isConnected()) {
            return true;
        }
        if (wifiNetInfo != null && wifiNetInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static String formatDateTime(Date date, String format) {
        SimpleDateFormat f = new SimpleDateFormat(format);
        return f.format(date);
    }

    public static void mkDirs(String dir) {
        File file = getExternalStoragePublicDirectory(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 格式化电话号码，用指定字符替换start和end直接的数字
     *
     * @param number  电话号码
     * @param start  开始位置
     * @param end    接收位置
     * @param format 替换字符串
     * @return 替换后字符串
     */
    public static String formatNumber(String number, int start, int end, String format) {
        if (TextUtils.isEmpty(number)) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(number.substring(0, start));
        for (int i = start; i <= end; i++) {
            stringBuilder.append("*");
        }
        stringBuilder.append(number.substring(end + 1, number.length()));
        return stringBuilder.toString();
    }
    public static String joinString(List<Object> list,String split){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<list.size();i++){
            stringBuilder.append(list.get(i).toString());
            if (i<list.size() - 1)
                stringBuilder.append(",");
        }
        return stringBuilder.toString();
    }
    
    public static float getYCoordinate(Rect rect,float yValue,float max,float min)
    {
        float x = 0;
        float h = rect.height();
        x = rect.top+h*(max - yValue)/(max - min);
        return x;
    }
    public static float getYValue(Rect rect,float y,float max,float min)
    {
        float x = 0;
        float h = rect.height();
        x = max-(max-min)*(y-rect.top)/h;
        return x;
    }
    public static void showToask(Context context, String content){
    	Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }
}
