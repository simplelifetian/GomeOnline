package com.jrj.sharesdk.msg;

import android.app.Activity;
import android.os.Parcelable;

import com.jrj.sharesdk.CallbackListener;
import com.jrj.sharesdk.ShareManager;

public abstract class AbsShareMsg implements Parcelable{
	/**平台类型*/
	public int pType;
	public String appName;
	public final void share(CallbackListener listener){
		ShareManager.getInstance().sendMsg(this,listener);
	}
}
