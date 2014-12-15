package com.gome.haoyuangong.net;

import java.util.ArrayList;
import java.util.List;


public class NetDisplayCounter {
	private RequestingDisplay mDisplay;
	private int mCount;
	private List<Request<Object>> mlist;
	private boolean isSuccess;
	public NetDisplayCounter(RequestingDisplay display){
		mDisplay=display;
		mCount=0;
		mlist = new ArrayList<Request<Object>>();
	}
	public void showDisplay(Request<Object> request){
		if(mCount==0){
			isSuccess = true;
			mDisplay.show();
		}
		if(request!=null){
			mlist.add(request);
		}
		mCount++;
	}
	public void hideDisplay(Request<Object> request){
		if(mCount>0){
			if(request!=null&&mlist.contains(request)){
				if(!request.isSuccess()) isSuccess = false;//只要有一个返回失败，则认为此次的所有请求失败
				mlist.remove(request);
			}
			mCount--;
			if(mCount<=0){
				mDisplay.hide(isSuccess);
			}
		}
	}
	public void cancel(){
		for(Request<Object> request:mlist){
			request.cancel();
		}
	}
	
	public interface RequestingDisplay{
		public void show();
		public void hide(boolean isSuccess);
	}
}
