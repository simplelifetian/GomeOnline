package com.gome.haoyuangong.log;

import com.gome.haoyuangong.LogDataUtils;
import com.gome.haoyuangong.LogUpdate;
import com.gome.haoyuangong.utils.StringUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class LogAddReceiver extends BroadcastReceiver{
	public static final String BUNDLE_FUNCTION="function_id";
	public static final String BUNDLE_CONTENT="content";
	public static final String BUNDLE_REQUEST_URL="request_url";
	public static final String BUNDLE_REQUEST_RCODE="request_rcode";
	@Override
	public void onReceive(Context context, Intent intent) {
		String functionId=intent.getStringExtra(BUNDLE_FUNCTION);
		String requestUrl=intent.getStringExtra(BUNDLE_REQUEST_URL);
		String content=intent.getStringExtra(BUNDLE_CONTENT);
		int rCode=intent.getIntExtra(BUNDLE_REQUEST_RCODE,-2);
		if(StringUtils.isEmpty(requestUrl)||StringUtils.isEmpty(functionId)){
			return;
		}
		Logger.info("addlogfromtrade", functionId+" "+requestUrl+" "+rCode+" "+content);
		LogUpdate.getInstance().addLog(functionId,requestUrl ,rCode+"", content);
	}

}
