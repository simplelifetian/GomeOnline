package com.gome.haoyuangong.net;

public interface RequestListener<T> {
	public  void onStart(Request  request);
	public  void onSuccess(String id,T data);
	public  void onFailure(String id,int code,String str,Object obj);
	public  void onEnd(Request  request);
	public  void onProgress(String url,int size,int allCount);
}
