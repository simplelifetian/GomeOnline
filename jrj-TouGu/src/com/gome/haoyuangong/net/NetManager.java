package com.gome.haoyuangong.net;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.gome.haoyuangong.log.Logger;

import android.content.Context;

public class NetManager {
	RequestQueue mQueue ;
	Context ctx;
	public NetManager(Context ctx){
		this.ctx = ctx;
		mQueue = Volley.newRequestQueue(ctx); 
	}
	public static NetManager init(Context ctx){
		return new NetManager(ctx);
	}
	public void send(Request request){
		Object obj = request.getTargetRequest();
		if(obj!=null&&obj instanceof  com.android.volley.Request){
			request.setTag(this);
			Logger.info("", "mQueue.getSequenceNumber() f:"+mQueue.getSequenceNumber());
			mQueue.add((com.android.volley.Request) obj);
			request.onStart();
			Logger.info("", "mQueue.getSequenceNumber(): a"+mQueue.getSequenceNumber());
		}else if(request instanceof Runnable){
			request.onStart();
			Logger.error("NetManager", "Thread((Runnable) request).start()");
			new Thread((Runnable) request).start();
		}
	}
	public void cancel(Request request){
		request.cancel();
	}
	public void cancelAll(){
		mQueue.cancelAll(this);
		mQueue.stop();
		mQueue=null;
	}
	public void cancelByTag(Object tag){
		mQueue.cancelAll(tag);
	}
	public boolean isHaveRequest(){
		if(mQueue.getSequenceNumber()>0){
			return true;
		}
		return false;
	}
}
