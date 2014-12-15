package com.gome.haoyuangong;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.gome.haoyuangong.crash.CrashHandler;
import com.gome.haoyuangong.db.QuoteDic;
import com.gome.haoyuangong.global.GlobalApplication;
import com.gome.haoyuangong.layout.self.Function;
import com.gome.haoyuangong.utils.PushUtils;
import com.gome.haoyuangong.utils.next.Directories;


public class MyApplication extends GlobalApplication{
private static MyApplication mInstance;
private LruCache<String, Bitmap> mCache;
	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
		DeviceStatus.getInstance(this);
		QuoteDic.copyDatabase(this);
		UserInfo.init(this);
		UserInfo.readUserInfo(this, UserInfo.getInstance());
		LogUpdate.init(this);
		AppInfo.init(getApplicationContext());
		readSetupData();
//		if (SetupData.getInstance().getOnlyWifiDown()){
//			if (AppInfo.isWifi(this))
//				ImageLoader.isOk = true;
//			else
//				ImageLoader.isOk = false;
//		}
//		else
//			ImageLoader.isOk = true;
		initImageCache();
		Directories.init(this);
		PushUtils.appContext=getApplicationContext();
	}
	private void readSetupData() {
		File file = new File(SetupData.SetupFilePath);
		if (!file.exists()){
			SetupData.init(this);
			SetupData.getInstance().setOnlyWifiDown(true);
		}
		else{
			SetupData.setInstance((SetupData)Function.deserialize(SetupData.SetupFilePath));
		}
		if (SetupData.getInstance() == null)
		{
			SetupData.init(this);
		}
	}
	private void initImageCache(){
		int maxSize = 10 * 1024 * 1024;
		mCache = new LruCache<String, Bitmap>(maxSize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}

		};
	}
	public LruCache<String, Bitmap> getImageCache(){
		
		return mCache;
	}
	
	public static MyApplication get() {
		return mInstance;
	}
	
	public String getAgentId() {
		return "1";
	}
	
	public String getAgentCode() {
		return "8";
	}
	
	//关注刷新判断
	private boolean lastLogined = false;
	private boolean newConcent = false;
	
	public boolean needGzRefresh(boolean islogined) {
		
		if((!lastLogined && islogined) || newConcent){
			lastLogined = islogined;
			newConcent = false;
			return true;
		}
		lastLogined = islogined;
		return false;
	}
	
	public void setNewConcent(boolean newConcent) {
		this.newConcent = newConcent;
	}
	
	public void setLogined(boolean logined) {
		this.lastLogined = logined;
	}
	
	private static List<WeakReference<Activity>> sVisibilityActivity = new ArrayList<WeakReference<Activity>>();
	 
	public static synchronized void startActivity(Activity a){
		sVisibilityActivity.add(new WeakReference<Activity>(a));
	}
	
	public static synchronized void destroyActivity(Activity activity) {
    	Iterator<WeakReference<Activity>> iterator = sVisibilityActivity.iterator();
    	WeakReference<Activity> removeRef = null;
    	while(iterator.hasNext()){
    		WeakReference<Activity> ref = iterator.next();
    		if(ref!=null && ref.get()!=null && ref.get().equals(activity)){
    			removeRef = ref;
    			
    		}
    	}
    	sVisibilityActivity.remove(removeRef);
    	removeRef = null;
    }
	
	public static synchronized void clearTouguActivity() {
        for (WeakReference<Activity> activity : sVisibilityActivity) {
            if (activity.get() != null)
                activity.get().finish();
        }
    }
	
	public static synchronized boolean isTouguVisibility() {
        return !sVisibilityActivity.isEmpty();
    }
	
	public static int runningType = 1;//1:前台   -1:后台
	
	public static Map<Integer,Integer> messageMaxId = new HashMap<Integer,Integer>();
	
	public static synchronized void putMsgMaxId(int type,int maxId){
		Integer value = messageMaxId.get(type);
		if(value == null || value < maxId){
			messageMaxId.put(type, maxId);
		}
	}
	
	@SuppressLint("UseSparseArrays")
	public static synchronized Map<Integer,Integer> getAllChanged(){
		if(messageMaxId.isEmpty()){
			return null;
		}
		Map<Integer,Integer> newids = new HashMap<Integer,Integer>();
		newids.putAll(messageMaxId);
		messageMaxId.clear();
		return newids;
	}
	
}
