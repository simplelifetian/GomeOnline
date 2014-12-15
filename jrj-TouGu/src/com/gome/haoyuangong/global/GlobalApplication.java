package com.gome.haoyuangong.global;

import java.util.Map;

//import com.jrj.trade.base.JRJAppApplication;



import com.gome.haoyuangong.BuildConfig;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.util.Log;


/**
 * 
 * @author 
 */
public class GlobalApplication extends Application{

	private static final String COOKIE_FILE = "Cookie";
	private static final String SET_COOKIE_KEY = "Set-Cookie";
	private static final String COOKIE_KEY = "Cookie";

	private SharedPreferences mPreferences;
	private ServerManager mServerManager;

	private static GlobalApplication mInstance;
	private String mCookies;
	
	private NetworkReceiver mNetworkStateReceiver;

	public static GlobalApplication get() {
		return mInstance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		mPreferences = getSharedPreferences(COOKIE_FILE, MODE_PRIVATE);

//		mServerManager = new ServerManager(this);
		
//		VolleyTool.getInstance(this);
		
		registerNetworkReceiver();
	}
	
	private void registerNetworkReceiver() {
		mNetworkStateReceiver = new NetworkReceiver();
		IntentFilter filter = new IntentFilter();  
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mNetworkStateReceiver, filter);
	}
	
	private class NetworkReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
				boolean isConnected = !intent.getBooleanExtra(
						ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
				if (BuildConfig.DEBUG) Log.d("", "CONNECTIVITY_ACTION," + isConnected);
				//维护session
			}
		}
	}
	
	public String getAgentId() {
		return "1";
	}
	
	public String getAgentCode() {
		return "8";
	}

	public void changeServer(String ServerIp, String ServerPort,
			String ServerContext) {
		mServerManager.changeServerConfig(ServerIp, ServerPort, ServerContext);
	}

	/**
	 * Checks the response headers for session cookie and saves it if it finds
	 * it.
	 * 
	 * @param headers
	 *            Response Headers.
	 */
	public void checkSessionCookie(Map<String, String> headers) {
		if (BuildConfig.DEBUG) Log.d("", "cookie=" + headers);
		if (headers.containsKey(SET_COOKIE_KEY)) {
			String cookie = headers.get(SET_COOKIE_KEY);
			if (cookie.length() > 0) {
				String[] splitCookie = cookie.split(";");
				String nameValueString = splitCookie[0];
				if (splitCookie != null && nameValueString.length() > 0) {
					
					Editor prefEditor = mPreferences.edit();
					prefEditor.putString(COOKIE_KEY, nameValueString);
					prefEditor.commit();
				}
				
				mCookies = nameValueString;
			}
		}
	}

	/**
	 * Adds session cookie to headers if exists.
	 * 
	 * @param headers
	 */
	public void addSessionCookie(Map<String, String> headers) {
		String cookies = null;
		if (mCookies != null) {
			cookies = mCookies;
		} else {
			cookies = mPreferences.getString(COOKIE_KEY, "");
		}
		if (cookies != null && cookies.length() > 0) {
			
			this.mCookies = cookies;
			
			StringBuilder builder = new StringBuilder();
			builder.append(cookies);
			if (headers.containsKey(COOKIE_KEY)) {
				builder.append("; ");
				builder.append(headers.get(COOKIE_KEY));
			}
			headers.put(COOKIE_KEY, builder.toString());
			if (BuildConfig.DEBUG) Log.d("", "request header=" + headers);
		}
	}
	
	public void clearCookies() {
		mCookies = null;
		mPreferences.edit().putString(COOKIE_KEY, "").commit();
	}
}
