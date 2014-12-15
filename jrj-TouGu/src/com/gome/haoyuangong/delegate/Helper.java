package com.gome.haoyuangong.delegate;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Helper {
    private final static String TAG = Helper.class.getSimpleName();
    
    /**
     * ****************************************************************************************
     */
    public static final int NOTIFY_ID_UPDATE = 1569;
    public static final int NOTIFY_ID_SYNC = 2784;
    public static final int NOTIFICATION_ID = 123;
    public static final int MESSAGE_FAILED_NOTIFICATION_ID = 789;
    public static final int DOWNLOAD_FAILED_NOTIFICATION_ID = 531;
    public static final int NOTIFY_ID_YOUNI_CONTACTS_INFO = 4953;
    public static final int NOTIFY_ID_KOUFEI = 3186;
    public static final int NOTIFY_ID_ARCHIVE_BKG = 240;
    public static final int NOTIFY_ID_SMS_BS_AUTO = 512;

    /**
     * ****************************************************************************************
     */
    public static final long MSG_STATUS_REC_TO_DISPLAY_INTERVAL = 2000;
    public static final long MSG_SEND_INTERVAL = 1000;
    public static final long MSG_MORESEND_TO_DISPLAY_INTERVAL = 1500;

    public static final String PREF_UI = "ui";
    public static final String PREF_SER = "services";

    private static long lastMsgRecTime = System.currentTimeMillis();
    private static long lastSendTime = System.currentTimeMillis();

    
    public static synchronized void status_interval() {
        if (Math.abs(System.currentTimeMillis() - lastMsgRecTime) < MSG_STATUS_REC_TO_DISPLAY_INTERVAL) {
            try {
                Thread.sleep(MSG_STATUS_REC_TO_DISPLAY_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lastMsgRecTime = System.currentTimeMillis();
        }
    }

    public static synchronized void send_interval() {
        if (Math.abs(System.currentTimeMillis() - lastSendTime) < MSG_SEND_INTERVAL) {
            try {
                Thread.sleep(MSG_SEND_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lastSendTime = System.currentTimeMillis();
        }
    }

    /**
     * ****************************************************************************************
     */
    private static List<WeakReference<Activity>> sVisibilityActivity = 
        new ArrayList<WeakReference<Activity>>();
    private static WeakReference<Activity> sCurrentActivity = null;

 
//    public static void setCurrentActivity(BaseChatActivity current){
//    	sCurrentActivity = new WeakReference<Activity>(current);
//    }
    
    
//    public static void setCurrentActivity(MucChatActivity current){
//    	sCurrentActivity = new WeakReference<Activity>(current);
//    }
    
    
    
    
    public static void clearCurrentActivity(){
    	sCurrentActivity = new WeakReference<Activity>(null);
    }
    
    
    
    public static Activity getCurrentActivity() {
    	if (sCurrentActivity == null) {
    		return null;
    	}
        return sCurrentActivity.get();
    }


    //同步控制sVisibilityActivity的访问
    //修正sVisibilityActivity.remove(new WeakReference<Activity>(activity)) 无法真正删除目标的问题   lixiaohua01@1026
    public static synchronized void startActivity(Activity activity) {
        sVisibilityActivity.add(new WeakReference<Activity>(activity));
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
//        sVisibilityActivity.remove(new WeakReference<Activity>(activity));
    }
    
    public static synchronized boolean isYouniVisibility() {
        return !sVisibilityActivity.isEmpty();
    }
    
    public static synchronized void clearYouniActivity() {
        for (WeakReference<Activity> activity : sVisibilityActivity) {
            if (activity.get() != null)
                activity.get().finish();
        }
    }
    
    /**
     * ****************************************************************************************
     */
    public static SharedPreferences getSharedPreferences(Context context, String name) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static SharedPreferences getUISharedPreferences(Context context) {
    	return context.getSharedPreferences(PREF_UI, Context.MODE_PRIVATE);
    }
    
    public static SharedPreferences getServicesSharedPreferences(Context context) {
    	return context.getSharedPreferences(PREF_SER, Context.MODE_PRIVATE);
    }
}
