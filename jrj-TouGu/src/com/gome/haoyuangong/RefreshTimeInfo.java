package com.gome.haoyuangong;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.gome.haoyuangong.utils.DateUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
/**
 * 刷新时间类
 * @author chunbao.bai
 *
 */
public class RefreshTimeInfo {
	public static final String REFRESH_ASK_ANSWER = "ask_answer";
	public static final String REFRESH_ASK_ASK = "refresh_ask_ask";
	public static final String REFRESH_MY_STOCK_LIST_PRICE = "refresh_my_stock_list_price";

	private static final String REFRESH_NAME = "refresh_time";
	
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat formatDateYMD = new SimpleDateFormat("yyyy-MM-dd");
	private Context ctx;

	public RefreshTimeInfo(Context ctx) {
		this.ctx = ctx;
		if (ctx == null) {
			throw new IllegalStateException("context cannot be null");
		}
	}

	public void saveRefreshTime(String key) {
		SharedPreferences.Editor editor = ctx.getSharedPreferences(REFRESH_NAME, Context.MODE_PRIVATE).edit();
		editor.putLong(key, System.currentTimeMillis());
		editor.commit();
	}
	public String getRefreshTimeStr(String key){
		long time = getRefreshTime(key);
		return DateUtils.getTimeAgoString(time, "yyyy-MM-dd hh:mm:ss");
//		return formatDateYMD.format(new Date(time));
	}
	public long getRefreshTime(String key){
		SharedPreferences sp = ctx.getSharedPreferences(REFRESH_NAME, Context.MODE_PRIVATE);
		long time = sp.getLong(key, 0);
		if(time==0){
			time = System.currentTimeMillis();
		}
		return time;
	}

}
