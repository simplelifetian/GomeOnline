package com.gome.haoyuangong.crash;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

//import com.jrj.stock.trade.utils.StringUtils;

import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.log.Logger;
import com.gome.haoyuangong.net.NetManager;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.TouguBaseResult;
import com.gome.haoyuangong.net.volley.JsonRequest;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 * 
 * @author ww
 * 
 */
public class CrashHandler implements UncaughtExceptionHandler {

	public static final String TAG = "CrashHandler";
	
	public static String CRASH_DIR ;
	static{
		CRASH_DIR = Environment.getExternalStorageDirectory().getPath() + "/jrj/tougu/.crash/";
	}

	// 系统默认的UncaughtException处理类
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	// CrashHandler实例
	private static CrashHandler INSTANCE = new CrashHandler();
	// 程序的Context对象
	private Context mContext;
	// 用来存储设备信息和异常信息
	private Map<String, String> infos = new HashMap<String, String>();

	// 用于格式化日期,作为日志文件名的一部分
	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

	private NetManager mNetManager;

	/** 保证只有一个CrashHandler实例 */
	private CrashHandler() {
	}

	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler getInstance() {
		return INSTANCE;
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	public void init(Context context) {
		mContext = context;
		mNetManager = new NetManager(mContext);
		// 获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// 设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				Log.d(TAG, "error : " + e);
			}
			// 退出程序
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		Logger.error(TAG, "post crash",ex);
		// 使用Toast来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.",Toast.LENGTH_SHORT).show();
				Looper.loop();
			}
		}.start();
		// 收集设备参数信息
		collectDeviceInfo(mContext);
		// 保存日志文件
		String fileName=saveCrashInfo2File(ex);
		// web接口有人接管之后上报后台服务器。
		// new Thread() {
		// public void run() {
		// //待有接口上传异常
		// // Upload.upLoadLogFile();
		// };
		// }.start();
		
		
//		final String content = readFromFile(fileName);
//		
//		if(StringUtils.isEmpty(content)){
//			return false;
//		}
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("log", content);
//		params.put("versionId", "1.0.0");
//		params.put("deviceId","ai32");
//		params.put("uid", "120");
//		params.put("os", "android-2.3.3");
//		params.put("productId", "test");
//		uploadFile(getPostUrl("http://sjcms.jrj.com.cn/api/coredump"),params);
//		final Map<String, String> params = new HashMap<String, String>();
//		params.put("Content-Type", "application/octet-stream");
//		params.put("Content-Encoding", "gzip");
//		Thread upThread = new Thread(){
//			public void run(){
////				Looper.prepare(); 
//				doPostCrash(getPostUrl("http://sjcms.jrj.com.cn/api/coredump"),params,content);
////				Looper.loop();
//			}
//		};
//		upThread.start();
		return true;
	}

	/**
	 * 收集设备参数信息
	 * 
	 * @param ctx
	 */
	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			Log.d(TAG, "an error occured when collect package info " + e);
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
				Log.d(TAG, field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				Log.d(TAG, "an error occured when collect crash info " + e);
			}
		}
	}

	/**
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 * @return 返回文件名称,便于将文件传送到服务器
	 */
	private String saveCrashInfo2File(Throwable ex) {
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Log.i("异常错误",ex.toString());
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		try {
			long timestamp = System.currentTimeMillis();
			String time = formatter.format(new Date());
			String path = CRASH_DIR;
			String fileName = "crash-" + time + "-" + timestamp + ".log";
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				
				File dir = new File(path);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				String errorfile = path + fileName;
				FileOutputStream fos = new FileOutputStream(errorfile);
				Log.i(TAG, "an error file " + path + "/" + fileName);
				fos.write(sb.toString().getBytes());
				fos.close();
				saveFileInfo2DB(path + fileName);
			} else {
				Log.i(TAG, "sdcard not mounted");
			}
			return path + fileName;
		} catch (Exception e) {
			Log.d(TAG, "an error occured while writing file... " + e);
		}
		return null;
	}

	private void saveFileInfo2DB(String filePath) {
		// TODO Auto-generated method stub

	}

//	public String readFromFile(String logFileName){
//		Logger.error("crash_handler", "logfile:"+logFileName);
//		if(StringUtils.isEmpty(logFileName)){
//			return null;
//		}
//		StringBuilder sb = new StringBuilder();
//        try {
//        	File file = new File(logFileName);
//        	if(file.exists()){
//                 BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(logFileName)));
////                 Scanner scanner=new Scanner(fis);
//                 String line = null;
//                 while((line = br.readLine()) != null){
//                 	sb.append(line).append("\n");
//                 }
//                 br.close();
//        	}
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return sb.toString();
//	}

//	private void uploadFile(String url,Map<String, String> params) {
//		Logger.error("crash_handler", url);
//		Logger.error("crash_handler", params.toString());
////		FileUploadRequest request = new FileUploadRequest(Method.POST, url,
////				filePath, params, paramF, new RequestHandlerListener<String>(
////						mContext) {
////					@Override
////					public void onSuccess(String id, String data) {
////						Logger.info("", data);
////
////					}
////
////					@Override
////					public void onFailure(String id, int code, String str,
////							Object obj) {
////						super.onFailure(id, code, str, obj);
////					}
////
////					@Override
////					public void onStart(Request request) {
////						super.onStart(request);
////					}
////
////					@Override
////					public void onEnd(Request request) {
////						super.onEnd(request);
////						mNetManager.cancelAll();
////						mNetManager = null;
////					}
////				});
//		
//		JsonRequest<TouguBaseResult> request = new JsonRequest<TouguBaseResult>(
//				Method.TEXTPOST, url, params,
//				new RequestHandlerListener<TouguBaseResult>(mContext) {
//
//					@Override
//					public void onStart(Request request) {
//						super.onStart(request);
//						// showDialog(request);
//					}
//
//					@Override
//					public void onEnd(Request request) {
//						super.onEnd(request);
//						// hideDialog(request);
//					}
//
//					@Override
//					public void onSuccess(String id, TouguBaseResult data) {
//						// TODO Auto-generated method stub
//						Logger.error("crash_handler", "success");
//						if(data.getRetCode() == 0){
//							Logger.error("crash_handler", "success22");
//						}
//					}
//					
//				}, TouguBaseResult.class){
//			public void addCustomHeader(Map<String,String> header){
//				header.put("Content-Encoding", "gzip");
//			}
//		};
//		
//		if (mNetManager != null) {
//			mNetManager.send(request);
//		}
//	}
	
//	private String getPostUrl(String baseUrl){
//		Map<String, String> params = new HashMap<String, String>();
//		
//		params.put("channelId", "111");
//		params.put("versionId", getVersion());
//		params.put("deviceId", getDeviceId());
//		if(StringUtils.isEmpty(UserInfo.getInstance().getLoginName())){
//			
//		}else{
//			params.put("usrname", UserInfo.getInstance().getLoginName());
//		}
//		if(StringUtils.isEmpty(UserInfo.getInstance().getUserId())){
//			
//		}else{
//			params.put("uid", UserInfo.getInstance().getUserId());
//		}
//		params.put("os", "android-"+android.os.Build.VERSION.RELEASE);
//		params.put("productId", "111");
//		String paramsEncoding = "UTF-8";
//		StringBuilder encodedParams = new StringBuilder();
//        try {
//            for (Map.Entry<String, String> entry : params.entrySet()) {
//                encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
//                encodedParams.append('=');
//                encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
//                encodedParams.append('&');
//            }
//        } catch (UnsupportedEncodingException uee) {
//            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
//        }
//        params.clear();
//        
//        return baseUrl+"?"+encodedParams.toString();
//	}
	
//	public void doPostCrash(String urlStr,Map<String,String> heads,String body){
//		Logger.error("crash_handler", "doPostCrash");
//		HttpURLConnection urlConnection = null;
//		InputStream in = null;
//		OutputStream out = null;
//		try{
//			URL request = new URL(urlStr);
//			urlConnection = (HttpURLConnection) request.openConnection();
//			//ensure that we are using a StrictHostnameVerifier
//			urlConnection.setRequestMethod("POST");
//			urlConnection.setConnectTimeout(20000);
//			urlConnection.setRequestProperty("keep-alive", "false");
//			urlConnection.setReadTimeout(20000);
//			for(Map.Entry<String, String> head : heads.entrySet()){
//				urlConnection.setRequestProperty(head.getKey(), head.getValue());
//			}
//			urlConnection.setDoInput(true);
//			urlConnection.setDoOutput(true);
//			
//			Logger.error("crash_handler", "doPostCrash.........................");
//			
//			out = urlConnection.getOutputStream();
//			out.write(body.getBytes("utf8"));
//			out.close();
//
//			Logger.error("crash_handler", "doPostCrash.........................DONE");
//			in = urlConnection.getInputStream();
//			//I don't want to change my function's return type (laziness) so I'm building an HttpResponse
//			ByteArrayOutputStream baos =  new ByteArrayOutputStream();
//			byte[] buffer = new byte[1024];
//			int line = -1;
//			while(-1!=(line = in.read(buffer))){
//				baos.write(buffer, 0, line);
//			}
//			
//			Logger.error("crash_handler", "doPostCrash.........................DONE@");
//			Logger.error("carsh_handler", baos.toString());
//		}catch(Exception e0){
//			Logger.error("crash_handler", "Exception",e0);
//			try {
//				if(in != null){
//					in.close();
//				}
//				if(out != null){
//					out.close();
//				}
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//			}
//		}finally{
//			if(urlConnection != null){
//				urlConnection.disconnect();
//			}
//		}
//	}
	
	private String getVersion(){
        PackageInfo pkg;
        try {
            pkg = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            String versionName = pkg.versionName; 
            return versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        } 
     }
	private String getDeviceId(){
		TelephonyManager telephonyManager= (TelephonyManager) mContext.getSystemService(mContext.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}
}
