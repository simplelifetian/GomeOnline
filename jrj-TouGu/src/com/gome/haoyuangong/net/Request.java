package com.gome.haoyuangong.net;

import java.util.Map;

import android.os.Looper;

public abstract class Request<T> {
	
	public interface Method {
    int DEPRECATED_GET_OR_POST = -1;
    int GET = 0;
    int POST = 1;
    int PUT = 2;
    int DELETE = 3;
    int TEXTPOST = 8;
}
	private RequestHandlerListener listener;
	protected int method;
	protected String url;
	private String id;
	protected Map<String, String> params;
	protected boolean isNormalPost=true;
	private boolean isSuccess=false;
	protected boolean isJsonPost=false;
	public Request(int method,String url,RequestHandlerListener listener){
		this.method = method;
		this.url = url;
		this.listener = listener;
		init();
	}
	public Request(int method,String url,Map<String, String> params,RequestHandlerListener listener){
		this.method = method;
		this.url = url;
		this.listener = listener;
		this.params=params;
		init();
	}
	public Request(int method,String url,Map<String, String> params,RequestHandlerListener listener,boolean isJsonPost){
		this.method = method;
		this.url = url;
		this.listener = listener;
		this.params=params;
		this.isJsonPost=isJsonPost;
		init();
	}
	protected abstract void init();
	public String onStart(){
		id = java.util.UUID.randomUUID().toString();
		if(listener!=null){
			if(Looper.myLooper() == Looper.getMainLooper()){
				listener.handleOnStartImmediately(this);
			}else{
				listener.obtainMessage(RequestHandlerListener.MSG_START,this).sendToTarget();
			}
		}
		return id;
	}
	public void onSuccess(String response,T data){
		isSuccess = true;
		if(listener!=null){
			listener.obtainMessage(RequestHandlerListener.MSG_SUCCESS,new Object[]{response,data}).sendToTarget();
		}
	}
	public void onFailure(int code,String str){
		if(listener!=null){
			listener.obtainMessage(RequestHandlerListener.MSG_FAILURE,code,-1,str).sendToTarget();
		}
	}
	public void onEnd(){
		if(listener!=null){
			listener.obtainMessage(RequestHandlerListener.MSG_END).sendToTarget();
		}
	}
	public void onProgress(String url,int size,int allSize){
		if(listener!=null){
			listener.obtainMessage(RequestHandlerListener.MSG_PROGRESS,size,allSize,url).sendToTarget();
		}
	}
	public void cancel(){
		if(listener!=null){
			listener.setCancel(true);
		}
	}
	public boolean isSuccess(){
		return isSuccess;
	}
	public abstract Object getTargetRequest();
	public abstract void setTag(Object obj);
	public String getId(){
		return id;
	}
	
	public String getUrl(){
		return url;
	}
	/**
	 * 默认为表单提交
	 * @param isNormalPost
	 */
	public void setHeaderType(boolean isNormalPost){
		this.isNormalPost = isNormalPost;
	}
	public RequestHandlerListener getListener() {
		return listener;
	}
	public void setListener(RequestHandlerListener listener) {
		this.listener = listener;
	}
	
	private int currRequestTimes = 1;
	
	public int getRequestedTimes(){
		return currRequestTimes;
	}
	
	public void addRequestedTimes(){
		currRequestTimes++;
	}
}
