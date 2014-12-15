package com.gome.haoyuangong.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.android.volley.Request.Method;
import com.gome.haoyuangong.BaseViewImpl;
import com.gome.haoyuangong.log.Logger;
import com.gome.haoyuangong.net.NetManager;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.volley.FileDownloadRequest;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.utils.Base64Coder;
import com.gome.haoyuangong.utils.FileUtils;

public class NetRequestService extends Service implements BaseViewImpl{
	private static final int THREAD_POOL_SIZE_DEFAULT=4;
	
	private ExecutorService pool;
	NetManager mNetManager;
	@Override
	public void onCreate() {
		super.onCreate();
		mNetManager = new NetManager(this);
		init();
	}
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		if(intent==null){
			return;
		}
		String[] extras = NRShelper.getIntentExtra(intent);
		if(extras.length>0){
			String type = extras[0];
			if(NRShelper.TYPE_ALARM_DELETE.equals(type)){
				doDeleteAlarm(extras);
			}else if(NRShelper.TYPE_SYSN_MYSTOCK.equals(type)){
				doSyncMyStock(extras);
			}
		}
	}
	
	public void sendImage(String[] extras){
		if(extras.length<3)return;
		String url = extras[1];
		String imageUrl = extras[2];
		if(url==null||imageUrl==null)return;
		String data = Base64Coder.encode(FileUtils.getBytesFromFile(imageUrl)).toString() ;
		
//		String url = "http://sso.jrj.com.cn/sso/m/userDevice.jsp?UserIDs=140619010046123662";
		Map<String, String> params = new HashMap<String, String>();
		params.put("data", data);
		JsonRequest<Object> request = new JsonRequest<Object>(Method.GET, url, params,new RequestHandlerListener(this) {
			
			@Override
			public void onSuccess(String id, Object data) {
				System.out.println("onSuccess:"+id+" "+data);
			}
			@Override
			public void onFailure(String id, int code, String str,Object obj) {
				super.onFailure(id, code, str,obj);
				
			}
		}, Object.class);
		mNetManager.send(request);
	}
	
	
	private void init(){
		pool = Executors.newFixedThreadPool(THREAD_POOL_SIZE_DEFAULT); 
	}
	
	
	private void downLoadFile(String[] extras){
		if(extras.length<3)return;
		String url = extras[1];
		String fileUrl = extras[2];
		if(url==null||fileUrl==null)return;
		
		FileDownloadRequest request = new FileDownloadRequest(url, fileUrl, new RequestHandlerListener(this) {
			@Override
			public void onSuccess(String id, Object data) {
				Logger.info("file download", (String) data);
			}
			@Override
			public void onFailure(String id, int code, String str,Object obj) {
				super.onFailure(id, code, str,obj);
			}
		});
		pool.execute(request);
	}
	private void doDeleteAlarm(String[] extras){
		if(extras.length<4)return;
	}
	private void doSyncMyStock(String[] extras){
	}
	
	
	@Override
	public void send(Request<Object> request) {
		mNetManager.send(request);
	}
	@Override
	public void showDialog(Request<Object> request) {
		
	}
	@Override
	public void hideDialog(Request<Object> request) {
		
	}
	@Override
	public void showLoading(Request<Object> request) {
		
	}
	@Override
	public void hideLoading(Request<Object> request) {
		
	}
	@Override
	public void showToast(String str) {
		
	}
	@Override
	public Context getContext() {
		return super.getBaseContext();
	}
	@Override
	public void showDialog(Request<Object> request, String textStr) {
		// TODO Auto-generated method stub
		
	}
	
}
	
