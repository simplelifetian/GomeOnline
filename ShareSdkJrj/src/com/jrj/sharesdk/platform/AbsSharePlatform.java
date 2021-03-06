package com.jrj.sharesdk.platform;

import java.util.Map;

import android.app.Activity;
import android.content.Context;

import com.jrj.sharesdk.CallbackListener;
import com.jrj.sharesdk.msg.AbsShareMsg;

public abstract class AbsSharePlatform {
	public static final String KEY_APPID="appid";
	public AbsSharePlatform(Context appCtx, Map<String, String> params){
		init(appCtx);
	}
	public abstract void init(Context appCtx);
	public abstract void shareMsg(Activity activity,AbsShareMsg msg,CallbackListener listener);
}
