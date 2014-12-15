package com.gome.haoyuangong.presenter;

import android.content.Context;

import com.gome.haoyuangong.BaseViewImpl;
import com.gome.haoyuangong.log.Logger;
import com.gome.haoyuangong.net.Request;


public class IBasePresenter {
	protected BaseViewImpl mVImpl;
	public IBasePresenter(BaseViewImpl vImpl){
		this.mVImpl = vImpl;
	}
	
	protected Context getContext(){
		return mVImpl.getContext();
	}
	
	protected void send(Request request){
		Logger.info("Request", request.getUrl());
		mVImpl.send(request);
	}
	
	protected void showDialog(Request request){
		mVImpl.showDialog(request);
	}
	protected void showDialog(Request request,String textStr){
		mVImpl.showDialog(request,textStr);
	}
	protected void hideDialog(Request request){
		mVImpl.hideDialog(request);
	}
	protected void showLoading(Request request){
		mVImpl.showLoading(request);
	}
	protected void hideLoading(Request request){
		mVImpl.hideLoading(request);
	}
	protected void showToast(String str){
		mVImpl.showToast(str);
	}
}
