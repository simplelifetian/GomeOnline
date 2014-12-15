package com.gome.haoyuangong;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;


//import com.jrj.stock.trade.utils.StringUtils;

import com.gome.haoyuangong.activity.BaseActivity;
import com.gome.haoyuangong.crash.CrashHandler;
import com.gome.haoyuangong.log.Logger;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.TouguBaseResult;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.utils.DateUtils;
import com.gome.haoyuangong.utils.StringUtils;

public class LogUpdate {
	
	private static final String TAG = LogUpdate.class.getName();
	
	private static LogUpdate instance;
	private static Context context;
	private String logFileName = "log.tg";
	private SharedPreferences sharedPreferences;
	private boolean posted=false;
	private long postDate = 0L;
	private static long postInterval = 1000 * 60 * 30;

	static void init(Context context) {
		LogUpdate.context = context;
		instance = new LogUpdate();
	}
	private LogUpdate(){
		sharedPreferences = context.getSharedPreferences("logupdate", Activity.MODE_PRIVATE);
		posted = sharedPreferences.getBoolean("posted", false);
		try {
			postDate = sharedPreferences.getLong("postDate", 0L);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	public static LogUpdate getInstance() {
		if (instance == null)
			instance = new LogUpdate();
		return instance;
	}
	public void saveToFile(String str,int mode){
		FileOutputStream fos=null;
        try {
            fos=context.openFileOutput(logFileName, mode);
            fos.write(str.getBytes());
            fos.close();
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
	}
	public String readFromFile(){
		StringBuilder sb = new StringBuilder();
		FileInputStream fis=null;
        try {
            fis=context.openFileInput(logFileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
//            Scanner scanner=new Scanner(fis);
            String line = null;
            while((line = br.readLine()) != null){
            	sb.append(line).append("\n");
            }
            br.close();
            fis.close();            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sb.toString();
	}
	/**
	 * 添加日志
	 * @param functionId 功能点id
	 * @param eventId  事件id
	 * @param content  状态信息
	 */
	public void addLog(String functionId,String eventId,String content) {
		String s = String.format("%s,%s,%s,%s\n", DateUtils.format(new Date(), "yyyy/MM/dd HH:mm:ss"),functionId,eventId,content);
		saveToFile(s,Context.MODE_APPEND);
	}
	/**
	 * 添加日志
	 * @param functionId 功能点id
	 * @param url 请求url
	 * @param eventId  事件id
	 * @param content  状态信息
	 */
	public void addLog(String functionId,String url,String eventId,String content) {
		String s = String.format("%s,%s,%s,%s,%s\n", DateUtils.format(new Date(), "yyyy/MM/dd HH:mm:ss"),functionId,url,eventId,content);
		saveToFile(s,Context.MODE_APPEND);
	}
	public void postLog(BaseActivity activity){
//		if (postDate > 0 && (System.currentTimeMillis() - postDate) < postInterval)
//			return;
		String logStr = readFromFile();
		if(StringUtils.isEmpty(logStr)){
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
        params.put("log", logStr);
        postLog(activity,getPostUrl(activity,"http://sjcms.jrj.com.cn/api/log"),params,null);
        
		
		File crashDir = new File(CrashHandler.CRASH_DIR);
		if(!crashDir.exists()){
			return;
		}
		String[] crashFiles = crashDir.list(new FilenameFilter(){

			@Override
			public boolean accept(File dir, String filename) {
				// TODO Auto-generated method stub
				if(filename.startsWith("crash") && filename.endsWith(".log")){
					return true;
				}
				return false;
			}
			
		});
		if(crashFiles.length > 0){
			for(String filename : crashFiles){
				String countent = readFromFile(crashDir+"/"+filename);
				if(StringUtils.isEmpty(countent)){
					continue;
				}
				Map<String, String> _params = new HashMap<String, String>();
				_params.put("log", countent);
		        postLog(activity,getPostUrl(activity,"http://sjcms.jrj.com.cn/api/coredump"),_params,crashDir+"/"+filename);
			}
		}
		
	}
	public String readFromFile(String logFileName){
		Logger.error(TAG, "logfile:"+logFileName);
		if(StringUtils.isEmpty(logFileName)){
			return null;
		}
		StringBuilder sb = new StringBuilder();
        try {
        	File file = new File(logFileName);
        	if(file.exists()){
                 BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(logFileName)));
//                 Scanner scanner=new Scanner(fis);
                 String line = null;
                 while((line = br.readLine()) != null){
                 	sb.append(line).append("\n");
                 }
                 br.close();
        	}
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sb.toString();
	}
	private void postLog(BaseActivity activity,String url,Map<String,String> params,final String filename){
		
		Logger.error(TAG, url);
		Logger.error(TAG, params.toString());
		
		JsonRequest<TouguBaseResult> request = new JsonRequest<TouguBaseResult>(
				Method.TEXTPOST, url, params,
				new RequestHandlerListener<TouguBaseResult>(context) {

					@Override
					public void onStart(Request request) {
						super.onStart(request);
						// showDialog(request);
					}

					@Override
					public void onEnd(Request request) {
						super.onEnd(request);
						// hideDialog(request);
					}

					@Override
					public void onSuccess(String id, TouguBaseResult data) {
						// TODO Auto-generated method stub
						if(data.getRetCode() == 0){
							Logger.error(TAG, "upsuccess");
							sharedPreferences.edit().putLong("postDate", System.currentTimeMillis()).commit();
							saveToFile("",Context.MODE_PRIVATE);
							if(filename != null){
								new File(filename).delete();
								Logger.error(TAG, "delete"+filename);
							}
						}
					}
					@Override
					public void onFailure(String id, int code, String str,
							Object obj) {
						// TODO Auto-generated method stub
						super.onFailure(id, code, str, obj);
					}
				}, TouguBaseResult.class){
			public void addCustomHeader(Map<String,String> header){
				header.put("Content-Encoding", "gzip");
			}
		};
		if (activity != null)
			activity.send(request);
		
	}
	
	public static String getPostUrl(Context activity,String baseUrl){
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("channelId", "test");
		params.put("versionId", getVersion());
		params.put("deviceId", getDeviceId());
		if(StringUtils.isEmpty(UserInfo.getInstance().getLoginName())){
		}else{
			params.put("userName", UserInfo.getInstance().getLoginName());
		}
		if(StringUtils.isEmpty(UserInfo.getInstance().getUserId())){
		}else{
			params.put("uid", UserInfo.getInstance().getUserId());
		}
		if(StringUtils.isEmpty(UserInfo.getInstance().getDeivceId())){
			params.put("deviceToken", "0");
		}else{
			params.put("deviceToken", UserInfo.getInstance().getDeivceId());
		}
		params.put("brand", android.os.Build.MODEL);
		params.put("os", "android-"+android.os.Build.VERSION.RELEASE);
		if(activity instanceof Activity){
			params.put("pixel", getDisplay((Activity)activity));
		}else{
			params.put("pixel", "unknown");
		}
		
		params.put("productId", "111");
		params.put("network", AppInfo.isWifi(activity)?"WIFI":"2G/3G/4G");
		
		String phoneNum = getPhoneNum(activity);
		if(!StringUtils.isEmpty(phoneNum)){
			params.put("phoneNumber", phoneNum);
		}
		
		String imsi = getIMSI(activity);
		if(!StringUtils.isEmpty(imsi)){
			params.put("imsi", imsi);
		}
		params.put("deviceType", "phone");
		String paramsEncoding = "UTF-8";
		StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                encodedParams.append('&');
            }
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
        }
        params.clear();
        
        return baseUrl+"?"+encodedParams.toString();
	}
	
	private static String getVersion(){
        PackageInfo pkg;
        try {
            pkg = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String versionName = pkg.versionName; 
            return versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        } 
     }
	private static String getDeviceId(){
		TelephonyManager telephonyManager= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}
	
	private static String getDisplay(Activity context){
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels+"*"+dm.widthPixels;
	}
	
	private static String getPhoneNum(Context context){
		TelephonyManager phoneMgr=(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

		return phoneMgr.getLine1Number();
	}
	
	private static String getIMSI(Context context){
		TelephonyManager phoneMgr=(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

		return phoneMgr.getSubscriberId();
	}
	
}
