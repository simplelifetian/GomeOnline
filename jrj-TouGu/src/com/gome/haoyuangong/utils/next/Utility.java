package com.gome.haoyuangong.utils.next;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

//import com.snda.sdw.woa.interfaces.OpenAPI;
//import com.snda.youni.AppContext;
//import com.snda.youni.Constants;
//import com.snda.youni.Helper;
//import com.snda.youni.YouNi;
//import com.snda.youni.activities.ChatActivity;
//import com.snda.youni.activities.ShowGuideActivity;
//import com.snda.youni.inbox.InboxListDialogFragment;
//import com.snda.youni.logs.YouniStatistics;
//import com.snda.youni.modules.muc.MucUtils;
//import com.snda.youni.providers.DataStructs.ContactsColumns;
//import com.snda.youni.receiver.YouniMessageReceiver;
//import com.snda.youni.services.ContactsService;
//import com.snda.youni.services.YouniService;

/**
 * 
 */
public class Utility {

    private final static boolean DBG = LogUtil.VDBG;

    private final static String TAG = "Utility";

    private final static String KROBOT_PREFIX = "krobot";
    
    private final static String ROBOT_LABEL = "robot";

    public static String stripSpaces(String number) {
        if (TextUtils.isEmpty(number))
            return number;
        StringBuilder builder = new StringBuilder();
        int len = number.length();
        for (int i = 0; i < len; i++) {
            char c = number.charAt(i);
            if (c == ' ')
                continue;
            builder.append(c);
        }
        return builder.toString();
    }

    public static String time2String(long time) {
        SimpleDateFormat df = new SimpleDateFormat("yy-MM-dd HH:mm");
        Calendar toyear = Calendar.getInstance();
        toyear.set(Calendar.MONTH, Calendar.JANUARY);
        toyear.set(Calendar.DATE, 1);
        toyear.set(Calendar.HOUR_OF_DAY, 0);
        toyear.set(Calendar.MINUTE, 0);
        toyear.set(Calendar.SECOND, 0);

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        if (time > toyear.getTime().getTime() && time < today.getTime().getTime())
            df = new SimpleDateFormat("MM-dd HH:mm");
        else if (time > today.getTime().getTime())
            df = new SimpleDateFormat("HH:mm:ss");

        return df.format(new Date(time));
    }

    public static void main(String[] args) {
        Date d = new Date();
        if(LogUtil.IDBG){
            LogUtil.i(TAG, time2String(d.getTime()));
        }
    }

    public static byte[] unGZIP(byte[] zipped) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(zipped);
        GZIPInputStream zipIn = null;
        try {
            zipIn = new GZIPInputStream(in);
            byte[] orginal = new byte[2048];
            int len = 0;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            while ((len = zipIn.read(orginal)) > 0) {
                out.write(orginal, 0, len);
            }
            out.flush();
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new Exception("unGZIP failed", e);
        } finally{
            if(zipIn != null){
                zipIn.close();
            }
            if(in != null){
                in.close();
            }
        }
    }
    public static byte[] gzip(String content){
        try {
            return gzip(content.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] gzip(byte[] content) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream zipOut = null;
        try {
            zipOut = new GZIPOutputStream(out);
            zipOut.write(content);
            zipOut.finish();
            return out.toByteArray();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new Exception("gzip failed", e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("gzip failed", e);
        } finally{
            if(zipOut != null){
                zipOut.close();
            }
            if(out != null){
                out.close();
            }
        }
    }

    /**
     * This function is used to revert the Chinese name. The family name should
     * be displayed at first of the whole name.
     * 
     * @param displayName the name will be formatted
     * @return the formatted name
     * @author sunzhibin
     */
    public static String formatChineseName(String displayName) {
        if (!TextUtils.isEmpty(displayName)) {
            String[] parts = displayName.split(" ");
            int len = parts.length;
            if (len > 1) {
                byte[] bytes = String.valueOf(parts[len - 1].charAt(0)).getBytes();
                if (bytes.length > 1) {
                    displayName = parts[len - 1];
                    for (int i = len - 2; i >= 0; i--) {
                        displayName = displayName.concat(parts[i]);
                    }
                }
            }
        }
        return displayName;
    }


    public static boolean isNumeric(String phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            if (phoneNumber.startsWith("+")) {
                phoneNumber = phoneNumber.substring(1);
            }
            if (TextUtils.isDigitsOnly(phoneNumber)) {
                return true;
            }
        }
        return false;
    }

    public static String getLikePhoneNumber(String phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            phoneNumber = phoneNumber.replaceAll("'", "");
            phoneNumber = phoneNumber.replaceAll("%", "");
            // if (phoneNumber.startsWith("+")) {
            // phoneNumber = phoneNumber.substring(1);
            int count = phoneNumber.length();
            if (count > 7) {
                phoneNumber = phoneNumber.substring(count - 7);
            }
            // }
        }
        return phoneNumber;
    }

    
    
    public static boolean isWap(Context context){
        ConnectivityManager con = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = con.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isAvailable()){
            String typeName = networkInfo.getTypeName();//mobile|wifi
            if("wifi".equalsIgnoreCase(typeName)){
                return false;
            }
            String extraInfo = networkInfo.getExtraInfo();//cmnet、cmwap、3gnet、3gwap
            if(extraInfo != null && extraInfo.toLowerCase().contains("wap")){
                return true;
            }
        }
        return false;
    }
    
    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) 
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }
    
    public static String getNetworkName(Context context){
        ConnectivityManager con = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = con.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            String typeName = networkInfo.getTypeName();//mobile|wifi
            if("wifi".equalsIgnoreCase(typeName)){
                return "wifi";
            }
            return networkInfo.getExtraInfo();//cmnet、cmwap、3gnet、3gwap
        }
        return null;
    }
    
    public static boolean isWifi(Context context){
        ConnectivityManager con = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = con.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            String typeName = networkInfo.getTypeName();//mobile|wifi
            if("wifi".equalsIgnoreCase(typeName)){
                return true;
            }
        }
        return false;
    }
    
    public static boolean isWifiEnable(Context context){
    	WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    	if(wm.isWifiEnabled()){
    	    return true;
    	}
    	return false;
    }
    
    public static String getCurrProcessName(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String name = "";
        List<RunningAppProcessInfo> list = activityManager.getRunningAppProcesses();
        if (list != null) {
            int pid = android.os.Process.myPid();
            for (RunningAppProcessInfo info : list) {
                if (pid == info.pid) {
                    name = info.processName;
                    if(LogUtil.DDBG){
                        LogUtil.d(TAG, "current process name:" + name);
                    }
                    break;
                }
            }
        }
        return name;
    }

    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        return sw.toString();
    }
    
    public static void printStackTrace(String tag) {
        try {
            throw new Exception();
        } catch (Exception e) {
            if(LogUtil.IDBG){
                LogUtil.i(tag, getStackTraceString(e));
            }
        }
    }
    
    public static boolean isContact(long cid) {
    	return cid > 0 || cid == -2;
    }
    
    
    public static boolean hideSoftInput(Activity activity) {
        final View v = activity.getWindow().peekDecorView();
        if (v != null && v.getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            return imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        return false;
    }
    
    public static Drawable getRepeatDrawable(Context context, int resid) {
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resid);
        BitmapDrawable drawable = new BitmapDrawable(bmp);
        drawable.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
        drawable.setDither(true);
        return drawable;
    }
    
    public static void setRepeatDrawableForView(Context context, View view, int resid){
        view.setBackgroundDrawable(getRepeatDrawable(context, resid));
    }
    
    private static Pattern sAccountPattern = Pattern.compile("(\\d{16})|账号|帐号", Pattern.CASE_INSENSITIVE);
    private static Pattern sBankPattern=Pattern.compile("中行|农行|建行|工行|招行|交行|银行|转账", Pattern.CASE_INSENSITIVE);
    private static Pattern sMoneyPattern = Pattern.compile("((\\d{2}|百|千|万)(元|块))|款|钱|费", Pattern.CASE_INSENSITIVE);
    private static Pattern sNameSenstivePattern = Pattern.compile("有你马|有你|由你|Youni|youni|有你小秘书|Youni小秘书|Youni团队|有你团队", Pattern.CASE_INSENSITIVE);
    
    
    public static boolean isMoneyRelated(CharSequence message) {
        if (TextUtils.isEmpty(message)) {
            return false;
        }
        String m = trimAll(message.toString());
        LogUtil.i(TAG, "message="+m);
        Matcher accountMatcher = sAccountPattern.matcher(m);
        Matcher bankMatcher = sBankPattern.matcher(m);
        Matcher moneyMatcher = sMoneyPattern.matcher(m);
        int count = 0;
        if (accountMatcher.find()) {
            LogUtil.i(TAG, "accountMatcher.find() true");
            count++;
        }
        if (bankMatcher.find()) {
            LogUtil.i(TAG, "bankMatcher.find() true");
            count++;
        }
        if (moneyMatcher.find()) {
            LogUtil.i(TAG, "moneyMatcher.find() true");
            count++;
        }
        return count>=2;
    }
    
    public static String trimAll(String s) {
        final int length = s.length();
        StringBuffer buffer=new StringBuffer();      
        int position=0;      
        char currentChar;       
        while(position < length)      {               
              currentChar=s.charAt(position++);        
              if(currentChar > ' ') {
                  buffer.append(currentChar); 
              }
         }
       return buffer.toString();     
    }
    
   
    
    
    /**
     * 跳转到某些activity
     * @param context
     * @param actionUrl：activity全名+?+key=value+&+key=value+...
     */
    public static void intoActivity(Context context, String actionUrl) {
        if (TextUtils.isEmpty(actionUrl)) {
            return;
        }
        Intent intent = new Intent();
        int index = actionUrl.indexOf("?");
        if (index != -1) {
            intent.setClassName(context, actionUrl.substring(0, index));
            String paramStr = actionUrl.substring(index + 1);
            String[] params = paramStr.split("&");
            for (String param : params) {
                String[] kv = param.split("=");
                //liujinhui modify so as to transmit boolean and url decode param;2013.11.04
                if (kv.length == 2) {
                    if( kv[1].equalsIgnoreCase("true") ){
                       intent.putExtra(kv[0], true);
                    } else if( kv[1].equalsIgnoreCase("false")){
                       intent.putExtra(kv[0], false);
                    } else {
                        String pam =null;
                        String value=null;
                        try {
                           pam =URLDecoder.decode(kv[0],"utf-8");
                           value = URLDecoder.decode(kv[1],"utf-8");
                        } catch (UnsupportedEncodingException e) {
                         e.printStackTrace();
                        }
                       intent.putExtra(pam, value);
                    }
                }
            }
        } else {
            intent.setClassName(context, actionUrl);
        }

        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public static void intoExternalBrowser(Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    
    public static float moneyMinus(float a, float b) {
    	final int intA = (int)(100 * a);
    	final int intB = (int)(100 * b);
    	final int intResult = intA - intB;
    	final float result = intResult/100.0f;
    	return result;
    }
    
    public static boolean isNameSenstive(CharSequence name) {
        if (TextUtils.isEmpty(name)) {
            return false;
        }
        String m = trimAll(name.toString());
        Matcher nameSenstiveMatcher = sNameSenstivePattern.matcher(m);
       
        int count = 0;
        if (nameSenstiveMatcher.find()) {
            count++;
        }
        return count>0;
    }


}
