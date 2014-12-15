package com.gome.haoyuangong.layout.self.data;

import com.gome.haoyuangong.SetupData;
import com.gome.haoyuangong.net.volley.ImageLoader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;

public class NetworkConnectChangedReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
        	 int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);   
        	 switch (wifiState) {   
             case WifiManager.WIFI_STATE_DISABLED:   
            	 if (SetupData.getInstance().getOnlyWifiDown())
                 	ImageLoader.isOk = false;
                 break;   
             case WifiManager.WIFI_STATE_ENABLED:   
            	 ImageLoader.isOk = true;
                 break;   
            }
        }
	}

}
