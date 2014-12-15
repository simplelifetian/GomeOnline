/**
 * 
 */
package com.gome.haoyuangong.spashad;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.gome.haoyuangong.DeviceStatus;
import com.gome.haoyuangong.UserInfo;

//import com.xld.ylb.common.DeviceStatus;
//import com.xld.ylb.common.Setting;
//import com.xld.ylb.log.YLBLog;

/**
 * 
 */
public class SplashADService extends Service{

	private static final String TAG = SplashADService.class.getName();

	private static final long MIN_SDCARD_SIZE = 1024 * 1024;

	public static final String AD_PATH = "jrj/ylb/splash";

	private static SplashADService INSTANCE;

	private static final String baseUrl = "http://mobilead.jrj.com.cn:8080/adSpash";

	private static Object LOCK = new Object();
	
	private static long UPDATE_INTERVAL = 60 * 60 * 1000;

	private ADData currADData;
	
	private DisplayMetrics dm;
	
	public class SimpleBinder extends Binder{
        /**
         * 获取 Service 实例
         * @return
         */
        public SplashADService getService(){
            return SplashADService.this;
        }
        
    }

	private SimpleBinder sBinder;
	
	private Handler handler;
	
	private Runnable task;
	@Override
	public void onCreate(){
		super.onCreate();
		sBinder = new SimpleBinder();
		handler = new Handler();
		task = new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				getDrawableAdFromNet();
//				handler.postDelayed(this, UPDATE_INTERVAL);
			}
			
		};
		handler.postDelayed(task, 10 * 1000);
	}

	public ADData getDrawableAdFromLocal() throws IOException {
		// TODO Auto-generated method stub
		File sdPath = getSDPath();
		if (null == sdPath) {
			return null;
		}
		File adPath = new File(sdPath, AD_PATH);
		if (!adPath.exists()) {
			return null;
		}
		File[] splashs = adPath.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				// TODO Auto-generated method stub
				if (pathname.getName().endsWith(".info")) {
					return true;
				}
				return false;
			}
		});

		if (splashs == null || splashs.length == 0) {
			return null;
		}

		ADData adData = null;
		int currentVersion = -1;
		for (File f : splashs) {
			DataInputStream in = new DataInputStream(new FileInputStream(f));
			ADData _adData = new ADData();
			_adData.readObject(in);
//			if (_adData.getImgVersion() > currentVersion && _adData.getStatus() == 1 && _adData.getPubDate() < System.currentTimeMillis()) {
//			if (_adData.getImgVersion() > currentVersion && _adData.getStatus() == 1) {
				adData = _adData;
				currentVersion = _adData.getImgVersion();
				if(adData != null){
					File image = new File(adPath,adData.getImageFileName());
					image.deleteOnExit();
					File info = new File(adPath,adData.getInfoFileName());
					info.deleteOnExit();
				}
//			}

		}
		if(adData != null){
			File image = new File(adPath,adData.getImageFileName());
			adData.setLocaPath(image.getAbsolutePath());
		}
		return currADData = adData;
	}

	private void getDrawableAdFromNet() {
		// TODO Auto-generated method stub
//		YLBLog.e(TAG, "get ad from network .......");
		ADInfoAsyncTask aDInfoAsyncTask = new ADInfoAsyncTask(currADData);
		TelephonyManager tel = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		
		//获取屏幕分辨率
		int screenType = 1;
		 if(null != dm){
//			 YLBLog.d(TAG, "分辨率 "+dm.widthPixels+"*"+dm.heightPixels);
			 if(dm.widthPixels > 320 || dm.heightPixels > 480){
				 screenType = 2;
			 }
		 }
		 
		 aDInfoAsyncTask.execute(baseUrl + "?productId=" + 1010165 +"&screenType="+screenType +"&device="
					+ UserInfo.getInstance().getDeivceId() + "&version=" + DeviceStatus.getInstance(this).getAppver() + "&platform=android");
		//正式 
//		aDInfoAsyncTask.execute(baseUrl + "?productId=" + DeviceStatus.getInstance(this).getProductid() +"&screenType="+screenType +"&device="
//				+ DeviceStatus.getInstance(this).getDevid() + "&version=" + DeviceStatus.getInstance(this).getAppver() + "&platform=android");
		
		this.stopSelf();
	}

	public static File getSDPath() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File sdRoot = Environment.getExternalStorageDirectory();
			return sdRoot;
		}
		return null;
	}
	
	public static void clear(){
		File sdPath = getSDPath();
		if (null == sdPath) {
			return;
		}
		File adPath = new File(sdPath, AD_PATH);
		if (!adPath.exists()) {
			return;
		}
		
		File[] splashs = adPath.listFiles();
		for(File f : splashs){
			f.delete();
		}
	}
	
	public void setDm(DisplayMetrics dm) {
		this.dm = dm;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return sBinder;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		handler.removeCallbacks(task);
	}
}
