package com.gome.haoyuangong;

import java.io.Serializable;

import android.content.Context;

/**
 * 设置
 * @author menghui
 *
 */
public class SetupData implements Serializable {
	public static final String SetupFilePath = android.os.Environment.getExternalStorageDirectory()+"/jrj/tougu/setup.tg";
	private static SetupData instance;
	boolean onlyWifiDown;
	int freshFrequency; //刷新频率（秒）,0不刷新

	public boolean getOnlyWifiDown() {
		return onlyWifiDown;
	}

	public void setOnlyWifiDown(boolean onlyWifiDown) {
		this.onlyWifiDown = onlyWifiDown;
	}

	public int getFreshFrequency() {
		return freshFrequency;
	}

	public void setFreshFrequency(int freshFrequency) {
		this.freshFrequency = freshFrequency;
	}
	public static SetupData getInstance(){
		if(instance==null){
//			throw new IllegalArgumentException("this should be inited in application first");
		}
		return instance;
	}
	public static void setInstance(SetupData setupData) {
		instance = setupData;
	}
	public static void init(Context app){
		instance = new SetupData();
		instance.setOnlyWifiDown(true);
	}
}
