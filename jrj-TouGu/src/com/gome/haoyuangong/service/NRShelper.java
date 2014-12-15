package com.gome.haoyuangong.service;

import android.content.Context;
import android.content.Intent;

public class NRShelper {
	static public final String TYPE_SEND_IMAGE = "type_image";
	static public final String TYPE_DOWNLOAD_FILE = "type_download_file";
	static public final String TYPE_ALARM_DELETE = "type_alarm_delete";
	static public final String TYPE_SYSN_MYSTOCK = "type_sysn_mystock";

	static public void uploadImage(Context ctx, String url, String imageUrl) {
		Intent i = new Intent(ctx, NetRequestService.class);
		i.putExtra("type", TYPE_SEND_IMAGE);
		i.putExtra("url", url);
		i.putExtra("imageUrl", imageUrl);
		ctx.startService(i);
	}

	static public String[] getIntentExtra(Intent intent) {
		String type = intent.getStringExtra("type");
		String[] rt = null;
		if (TYPE_ALARM_DELETE.equals(type)) {
			rt = new String[4];
			rt[0] = type;
			rt[1] = intent.getStringExtra("userId");
			rt[2] = intent.getStringExtra("stockCode");
			rt[3] = intent.getStringExtra("stockType");
		}else{
			rt = new String[4];
			rt[0] = type;
		}
		return rt;
	}

	static public void deleteAlarm(Context ctx, String userId, String stockCode, String stockType) {
		Intent i = new Intent(ctx, NetRequestService.class);
		i.putExtra("type", TYPE_ALARM_DELETE);
		i.putExtra("userId", userId);
		i.putExtra("stockCode", stockCode);
		i.putExtra("stockType", stockType);
		ctx.startService(i);
	}
	static public void doSysnMyStock(Context ctx) {
		Intent i = new Intent(ctx, NetRequestService.class);
		i.putExtra("type", TYPE_SYSN_MYSTOCK);
		ctx.startService(i);
	}
}
