package com.gome.haoyuangong.activity;

import java.io.IOException;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.log.Logger;
import com.gome.haoyuangong.spashad.ADData;
import com.gome.haoyuangong.spashad.SplashADService;
import com.gome.haoyuangong.update.UpdateManager;

public class WaitingActivity extends BaseActivity {

	private Handler jumpHandler;

	private Bundle bundle = null;

	private DisplayMetrics dm;
	
	private SplashADService.SimpleBinder sBinder;
	
	private LinearLayout adContainer;

	private ServiceConnection sc = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			sBinder = (SplashADService.SimpleBinder)service;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			sBinder = null;
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_waiting);
		hideTitle();
		adContainer = (LinearLayout)findViewById(R.id.waitingLayout);
		/***********ad************/
		dm = new DisplayMetrics();   
        getWindowManager().getDefaultDisplay().getMetrics(dm); 
		startService(new Intent(WaitingActivity.this, SplashADService.class));
		bindService(new Intent(WaitingActivity.this, SplashADService.class), sc, Context.BIND_AUTO_CREATE);
		
		bundle = getIntent().getExtras(); 

		
//		jumpHandler = new Handler();
//		jumpHandler.postDelayed(new Runnable() {
//			public void run() {
//				pageChangeTo();
//			}
//		}, 1000);
		splashHandler.postDelayed(new Runnable(){
			@Override
			public void run(){
				Message msg = splashHandler.obtainMessage();
				splashHandler.sendMessage(msg);
			}
		}, 100);
		
	}
	
	private Handler splashHandler = new Handler(){
		
		@Override
		public void handleMessage(Message msg){
			
//			try {
//				SplashADService splashADService = sBinder.getService();
//				splashADService.setDm(dm);
//				ADData adData = splashADService.getDrawableAdFromLocal();
//				adData = null;
//				if (adData != null) {
//					ImageView imageView = new ImageView(WaitingActivity.this);
//					imageView.setScaleType(ScaleType.FIT_XY);
//					imageView.setImageDrawable(Drawable.createFromPath(adData.getLocaPath()));
//					ViewGroup.LayoutParams vgl = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
//					imageView.setLayoutParams(vgl);
//					adContainer.addView(imageView);
//					adContainer.invalidate();
//					Handler adHandler = new Handler();
//					adHandler.postDelayed(new Runnable() {
//						public void run() {
//							adContainer.removeAllViews();
//							jumpHandler = new Handler();
//							jumpHandler.postDelayed(new Runnable() {
//								public void run() {
//									pageChangeTo();
//								}
//							}, 1000);
//						}
//					}, 3000);
//					
//				}else{
					jumpHandler = new Handler();
					jumpHandler.postDelayed(new Runnable() {
						public void run() {
							pageChangeTo();
						}
					}, 1000);
//				}
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				Logger.error("LoginActivity", "初始化广告失败 : ",e);
//			}
			
		}
	};

	private boolean getTutorials() {
		// 建立一个缓存时间的文件
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		return sp.getBoolean("Tutorialsed", false);
	}

	private void pageChangeTo() {
		Intent itent = null;
		if (getTutorials()) {
			itent = new Intent(WaitingActivity.this, MainActivity.class);
			if (bundle != null) {
				itent.putExtras(bundle);
			}
		} else {
			itent = new Intent(WaitingActivity.this, TutorialsActivity.class);
			if (bundle != null) {
				itent.putExtras(bundle);
			}

		}
		startActivity(itent);
		finish();
		// }
	}
	
	/**
	 * 公告
	 */
	public void ad() {
		
	}
	
	protected void onDestroy() {
		super.onDestroy();
		this.unbindService(sc);
	}
}
