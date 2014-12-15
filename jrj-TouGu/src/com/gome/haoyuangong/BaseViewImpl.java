package com.gome.haoyuangong;

import android.content.Context;

import com.gome.haoyuangong.net.Request;

public interface BaseViewImpl {
	/**
	 * 网络请求
	 * @param request
	 */
	public void send(Request<Object> request);
	
	public void showDialog(Request<Object> request);
	public void showDialog(Request<Object> request,String textStr);
	
	public void hideDialog(Request<Object> request);
	
	public void showLoading(Request<Object> request);
	
	public void hideLoading(Request<Object> request);
	public void showToast(String str);
	public Context getContext();
}
