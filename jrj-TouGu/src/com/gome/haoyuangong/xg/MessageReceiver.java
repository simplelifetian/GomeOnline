package com.gome.haoyuangong.xg;

import java.lang.reflect.Field;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.activity.MainActivity;
import com.gome.haoyuangong.activity.MySelfInfoActivity;
import com.gome.haoyuangong.activity.XgAlertActivity;
import com.gome.haoyuangong.dialog.CustomDialog;
import com.google.gson.JsonObject;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

public class MessageReceiver extends XGPushBaseReceiver {
	public static interface IProcessCheckFinishedListener{
        public void OnProcessCheckFinished(Boolean result);
    }
	Context context;
	Intent intent;
	Handler myHandler = new Handler() {  
        public void handleMessage(Message msg) {   
             switch (msg.what) {   
                  case 1001: 
                	  intent.setClass(activity, XgAlertActivity.class);
                	  activity.startActivity(intent);                	  
                       break;   
             }   
             super.handleMessage(msg);   
        }   
   }; 
	public static FragmentActivity activity;
	
	@Override
	public void onDeleteTagResult(Context arg0, int arg1, String arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNotifactionClickedResult(Context arg0,
			XGPushClickedResult arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNotifactionShowedResult(final Context arg0, final XGPushShowedResult arg1) {
		// TODO Auto-generated method stub
		if (arg0 == null || arg1 == null) {
			return;
		}
//		XGNotification notific = new XGNotification();
//		notific.setMsg_id(notifiShowedRlt.getMsgId());
//		notific.setTitle(notifiShowedRlt.getTitle());
//		notific.setContent(notifiShowedRlt.getContent());
//		// notificationActionType==1ΪActivity��2Ϊurl��3Ϊintent
//		notific.setNotificationActionType(notifiShowedRlt
//				.getNotificationActionType());
//		// Activity,url,intent������ͨ��getActivity()���
//		notific.setActivity(notifiShowedRlt.getActivity());
//		notific.setUpdate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//				.format(Calendar.getInstance().getTime()));
//		NotificationService.getInstance(context).save(notific);
//		context.sendBroadcast(intent);
//		String content = arg1.getCustomContent();
//		if (content!=null && content.length()>0){
//			JSONObject jsonObj;
//			try {
//				jsonObj = new JSONObject(content);
//				if (!jsonObj.isNull("type")){
//					show(arg0, jsonObj.getString("type"));
//				}
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		}
		ProcessTask task = new ProcessTask();
		task.context = arg0;
		task.setProcessCheckFinishedListener(new IProcessCheckFinishedListener() {
			
			@Override
			public void OnProcessCheckFinished(Boolean result) {
				// TODO Auto-generated method stub
				if (result){
					showNotification(arg0,arg1.getTitle(),arg1.getContent(),arg1.getCustomContent());
					return;
				}
				intent = new Intent();
				intent.putExtra(XgAlertActivity.TITLE, arg1.getTitle());
				intent.putExtra(XgAlertActivity.CONTENT, arg1.getContent());
				intent.putExtra(XgAlertActivity.CUSTOMCONTENT, arg1.getCustomContent());
				Message message = new Message();      
	            message.what = 1001;   
	            myHandler.sendMessage(message);	
			}
		});
		task.execute("");	
	}

	@Override
	public void onRegisterResult(Context arg0, int arg1,
			XGPushRegisterResult arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSetTagResult(Context arg0, int arg1, String arg2) {
		// TODO Auto-generated method stub

	}
//接受消息
	@Override
	public void onTextMessage(final Context arg0, final XGPushTextMessage arg1) {
		// TODO Auto-generated method stub
		
		ProcessTask task = new ProcessTask();
		task.context = arg0;
		task.setProcessCheckFinishedListener(new IProcessCheckFinishedListener() {
			
			@Override
			public void OnProcessCheckFinished(Boolean result) {
				// TODO Auto-generated method stub
				if (result){
					showNotification(arg0,arg1.getTitle(),arg1.getContent(),arg1.getCustomContent());
					return;
				}
				intent = new Intent();
				intent.putExtra(XgAlertActivity.TITLE, "新消息");
				intent.putExtra(XgAlertActivity.CONTENT, arg1.getContent());
				intent.putExtra(XgAlertActivity.CUSTOMCONTENT, arg1.getCustomContent());
				Message message = new Message();      
	            message.what = 1001;   
	            myHandler.sendMessage(message);		
			}
		});
		task.execute("");	
	}
	private void showNotification(Context context,String title,String content,String custContent){
		CharSequence contentTitle =title; //通知栏标题

		  CharSequence contentText = content;//通知栏内容
		  Notification notification = new Notification(R.drawable.xg_alert,contentText,System.currentTimeMillis());
//		  Notification notification = new Notification();
		  Intent notificationIntent = new Intent();
		  notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		  notificationIntent.setClass(context, MainActivity.class);
		  notificationIntent.putExtra("xgflag", 1);
		  notificationIntent.putExtra(MainActivity.CUSTOMCONTENT, custContent);
		  notificationIntent.putExtra(MainActivity.TITLE, title);
		  notificationIntent.putExtra(MainActivity.CONTENT, content);
		  PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
		  notification.defaults=Notification.DEFAULT_ALL;
		  notification.flags = Notification.FLAG_AUTO_CANCEL;
		  notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		  try  
		  {  
		      Class<?> clazz = Class.forName("com.android.internal.R$id");    
		                         
		      Field field = clazz.getField("icon");    
		      field.setAccessible(true);    
		      int id_icon = field.getInt(null);                 
		      if( notification.contentView!=null)  
		    	  notification.contentView.setImageViewResource(id_icon, R.drawable.ic_launcher);//这个图标是通知下拉栏上显示的图标  
		  }catch(Exception e)  
		  {  
		       e.printStackTrace();  
		  } 
		  NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		  mNotificationManager.notify((int)System.currentTimeMillis(),notification);		
	}
	@Override
	public void onUnregisterResult(Context arg0, int arg1) {
		// TODO Auto-generated method stub

	}
	

	 private class ProcessTask extends AsyncTask<String,Integer,Boolean> {
		 Context context;
		 IProcessCheckFinishedListener processCheckFinishedListener;
		 
	        public void setProcessCheckFinishedListener(
				IProcessCheckFinishedListener processCheckFinishedListener) {
			this.processCheckFinishedListener = processCheckFinishedListener;
		}
			@Override
	        protected Boolean doInBackground(String... strings) {
	            boolean b=false;
	        	ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	        	List<RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
	        	if (!tasks.isEmpty()){
	        		ComponentName topActivity = tasks.get(0).topActivity;
	        		if (!topActivity.getPackageName().equals(context.getPackageName())){
	        			System.out.println("后台");
	        			b = true;
	        		}
	        		else {
	        			System.out.println("前台");
	        			b = false;
					}
	        	}
	    	    return b;
	        }
	        @Override
	        protected void onPostExecute(Boolean result) {	        	
	        	if (processCheckFinishedListener != null){
	        		processCheckFinishedListener.OnProcessCheckFinished(result);
	        	}
	        }
	    }
}
