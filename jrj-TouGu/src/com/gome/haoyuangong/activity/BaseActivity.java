package com.gome.haoyuangong.activity;

import java.util.List;

import com.gome.haoyuangong.AppInfo;
import com.gome.haoyuangong.BaseViewImpl;
import com.gome.haoyuangong.LogUpdate;
import com.gome.haoyuangong.MyApplication;
import com.gome.haoyuangong.R;
import com.gome.haoyuangong.RefreshTimeInfo;
import com.gome.haoyuangong.dialog.CustomDialog;
import com.gome.haoyuangong.dialog.CustomDialog.Builder;
import com.gome.haoyuangong.log.Logger;
import com.gome.haoyuangong.net.NetDisplayCounter;
import com.gome.haoyuangong.net.NetManager;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.NetDisplayCounter.RequestingDisplay;
import com.gome.haoyuangong.update.UpdateManager;
import com.gome.haoyuangong.utils.StringUtils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * <h1>所有activity类的父类，继承自{@link FragmentActivity}.</h1>
 * 
 * @author
 */
public class BaseActivity extends FragmentActivity implements OnClickListener, BaseViewImpl {

	/** 用于显示加载中 */
	protected Dialog mProgressDialog;
	private TextView mProgressText;
	ViewGroup content;
	// title控件从左到右
	protected TextView titleLeft1;
	protected TextView titleLeft2;
	protected TextView titleCenter;
	protected TextView titleCenter2;
	protected TextView titleRight1;
	protected TextView titleRight2;
	protected ViewGroup titleWhole;
	protected View titleLeft1Ly;
	protected ViewGroup loadVg;
	protected ImageView loadIv;
	protected TextView loadTv;

	private NetManager mNetManager;
	private NetDisplayCounter mDialogCounter;
	private NetDisplayCounter mLoadCounter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.startActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.setContentView(R.layout.activity_base);
		SetScreenType();
		initNet();
		initView();
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				startLoad();
			}
		});
	}
	private Handler handler = new Handler();
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MyApplication.destroyActivity(this);
		mNetManager.cancelAll();
		mNetManager = null;
	}

	private void initView() {
		content = (FrameLayout) findViewById(R.id.content);
		titleLeft1 = (TextView) findViewById(R.id.title_left1);
		titleLeft2 = (TextView) findViewById(R.id.title_left2);
		titleCenter = (TextView) findViewById(R.id.title_center);
		titleCenter2 = (TextView) findViewById(R.id.title_center2);
		titleRight1 = (TextView) findViewById(R.id.title_right1);
		titleRight2 = (TextView) findViewById(R.id.title_right2);
		titleWhole = (ViewGroup) findViewById(R.id.title_whole);
		titleLeft1Ly = findViewById(R.id.title_left1_ly);

		loadVg = (ViewGroup) findViewById(R.id.load);
		loadIv = (ImageView) findViewById(R.id.load_img);
		loadTv = (TextView) findViewById(R.id.load_text);

		titleLeft1.setOnClickListener(this);
		titleLeft2.setOnClickListener(this);
		titleRight1.setOnClickListener(this);
		titleRight2.setOnClickListener(this);
		titleLeft1Ly.setOnClickListener(this);
		loadVg.setOnClickListener(this);
	}

	private void initNet() {
		mNetManager = new NetManager(this);
	}

	private void initDialogDisplay(final String textStr) {
		mDialogCounter = new NetDisplayCounter(new RequestingDisplay() {
			@Override
			public void show() {
				if (mProgressDialog == null) {
					// TODO init
					mProgressDialog = new Dialog(BaseActivity.this, R.style.DialogTransparent);
					View v = LayoutInflater.from(BaseActivity.this).inflate(R.layout.progress_dialog_common, null);
					
					mProgressDialog.setContentView(v);
					mProgressDialog.setCanceledOnTouchOutside(false);
					mProgressDialog.setOnCancelListener(new OnCancelListener() {
						@Override
						public void onCancel(DialogInterface dialog) {
							if (mDialogCounter != null) {
								mDialogCounter.cancel();
							}
							mDialogCounter = null;
						}
					});
				}
				setDialogText(textStr);
				mProgressDialog.show();

			}

			@Override
			public void hide(boolean isSuccess) {
				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
					mDialogCounter = null;
				}
			}
		});
	}
	private void setDialogText(String textStr){
		TextView text = (TextView) mProgressDialog.getWindow().getDecorView().findViewById(R.id.dialog_common_text);
		if(!StringUtils.isEmpty(textStr)){
			text.setText(textStr);
			text.setVisibility(View.VISIBLE);
		}else{
			text.setText("加载中...");
			text.setVisibility(View.VISIBLE);
		}
	}
	private void initLoadDisplay() {
		mLoadCounter = new NetDisplayCounter(new RequestingDisplay() {
			@Override
			public void show() {
				// TODO
				loadVg.setVisibility(View.VISIBLE);
				content.setVisibility(View.GONE);
				loadIv.setBackgroundResource(R.anim.frame_loading);
				loadTv.setText("加载中...");
				final AnimationDrawable anim = (AnimationDrawable) loadIv.getBackground();
				loadIv.post(new Runnable() {
					@Override
					public void run() {
						anim.start();
					}
				});
			}

			@Override
			public void hide(boolean isSuccess) {
				if (isSuccess) {
					loadVg.setVisibility(View.GONE);
					content.setVisibility(View.VISIBLE);

				} else {
					loadIv.setBackgroundResource(R.drawable.icon_nonet);
					loadTv.setText("网络连接异常，请点击屏幕重试");
					loadVg.setClickable(true);
				}
				mLoadCounter = null;
			}
		});
	}

	protected int activityWid = 0;// /界面宽度
	protected int activityHei = 0;// /界面高度

	/**
	 * 根据屏幕宽度设置当前屏幕类型
	 * **/
	private void SetScreenType() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		activityWid = dm.widthPixels;
		activityHei = dm.heightPixels;
		if (activityWid > 1080) {
			AppInfo.mScreenType = AppInfo.SCREEN_QUADHD;
		} else if (activityWid == 1080) {
			AppInfo.mScreenType = AppInfo.SCREEN_BIGLARGE;
		} else if (activityWid >= 720) {
			AppInfo.mScreenType = AppInfo.SCREEN_LARGE;
		} else if (activityWid >= 480) {
			AppInfo.mScreenType = AppInfo.SCREEN_MEDIUM;
		} else {
			AppInfo.mScreenType = AppInfo.SCREEN_SMALL;
		}
	}

	public int getScreenW() {
		return activityWid;
	}

	public int getScreenH() {
		return activityHei;
	}

	public void showTitle() {
		titleWhole.setVisibility(View.VISIBLE);
	}

	public void hideTitle() {
		titleWhole.setVisibility(View.GONE);
	}

	protected void dismiss(Dialog dialog) {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
			dialog = null;
		}
	}

	@Override
	public void setContentView(int layoutResID) {
		content.removeAllViews();
		View child = LayoutInflater.from(this).inflate(layoutResID, null);
		content.addView(child);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left1_ly:
			titleLeft1.performClick();
			break;
		case R.id.title_left1:
			finish();
			break;
		case R.id.load:
			if (mLoadCounter == null) {
				startLoad();
			}
			break;
		}

	}

	protected void setTitle(String str) {
		titleCenter.setText(str);
	}

	public void setTitle(int strId) {
		titleCenter.setText(getString(strId));
	}

	protected void setTitle2(String str) {
		titleCenter2.setVisibility(View.VISIBLE);
		titleCenter2.setText(str);
	}

	public void setTitle2(int strId) {
		titleCenter2.setVisibility(View.VISIBLE);
		titleCenter2.setText(getString(strId));
	}

	public void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}
	public Toast getToast(String text) {
		return Toast.makeText(this, text, Toast.LENGTH_LONG);
	}

	protected void showToast(int resId) {
		String text = getString(resId);
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}

	/**
	 * 发送网络请求
	 * 
	 * @param request
	 */
	public void send(Request request) {
		if (mNetManager != null) {
			mNetManager.send(request);
		}
	}

	/**
	 * 显示加载对话框
	 * 
	 * @param request
	 */
	public void showDialog(Request<Object> request) {
		if (mDialogCounter == null) {
			initDialogDisplay(null);
		}else{
			setDialogText(null);
		}
		mDialogCounter.showDisplay(request);
	}
	/**
	 * 显示加载对话框
	 * 
	 * @param request
	 */
	public void showDialog(Request<Object> request,String textStr) {
		if (mDialogCounter == null) {
			initDialogDisplay(textStr);
		}else{
			setDialogText(textStr);
		}
		mDialogCounter.showDisplay(request);
	}

	/**
	 * 隐藏加载对话框
	 * 
	 * @param request
	 */
	public void hideDialog(Request<Object> request) {
		if (mDialogCounter == null)
			return;
		mDialogCounter.hideDisplay(request);
	}

	/**
	 * 显示加载背景
	 * 
	 * @param request
	 */
	public void showLoading(Request<Object> request) {
		if (mLoadCounter == null) {
			initLoadDisplay();
		}
		mLoadCounter.showDisplay(request);
	}

	/**
	 * 隐藏加载对话框
	 * 
	 * @param request
	 */
	public void hideLoading(Request<Object> request) {
		mLoadCounter.hideDisplay(request);
	}

	/**
	 * 初始化请求数据 在onCreat 加载失败点击屏幕时触发 需要重写加载详细内容
	 */
	protected void onLoad() {

	}

	private void startLoad() {
		onLoad();
	}

	@Override
	public Context getContext() {
		return this;
	}

	public void hideSoftInput() {
		Activity a = this;
		if (a != null && a.getCurrentFocus() != null) {
			View fView = a.getCurrentFocus();
			if (fView != null) {
				InputMethodManager inputMethodManager = (InputMethodManager) a.getSystemService(Context.INPUT_METHOD_SERVICE);
				if (inputMethodManager.isActive()) {
					inputMethodManager.hideSoftInputFromWindow(fView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
		}

	}

	public void showSoftInput(View v) {
		Activity a = this;
		if (v != null) {
			InputMethodManager inputMethodManager = (InputMethodManager) a.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
		}
	}

	public void showAlert(String content) {
		// AlertDialog.Builder builder = new AlertDialog.Builder(this);
		CustomDialog.Builder builder = new Builder(this);
		builder.setMessage(content);
		builder.setPositiveButton("确定", new Dialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		Dialog dialog = builder.create();
		dialog.show();
	}

	private RefreshTimeInfo mRefreshTimeInfo;

	public long getRefreshTime(String key) {
		if (mRefreshTimeInfo == null) {
			mRefreshTimeInfo = new RefreshTimeInfo(this);
		}
		return mRefreshTimeInfo.getRefreshTime(key);
	}

	public String getRefreshTimeStr(String key) {
		if (mRefreshTimeInfo == null) {
			mRefreshTimeInfo = new RefreshTimeInfo(this);
		}
		return mRefreshTimeInfo.getRefreshTimeStr(key);
	}

	public void saveRefreshTime(String key) {
		if (mRefreshTimeInfo == null) {
			mRefreshTimeInfo = new RefreshTimeInfo(this);
		}
		mRefreshTimeInfo.saveRefreshTime(key);
	}

	@Override
	protected void onStop() {
		super.onStop();
		if(!isAppOnForeground()){
			Logger.info("baseactivity", "程序已经进入后台");
			LogUpdate.getInstance().postLog(this);
			MyApplication.runningType = -1;
		}
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		if(MyApplication.runningType == -1){
			MyApplication.runningType = 1;
			UpdateManager.getInstance().autoUpdate();
		}
	}

	public boolean isAppOnForeground() {
		ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = getApplicationContext().getPackageName();
		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		if (appProcesses == null)
			return false;
		for (RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			if (appProcess.processName.equals(packageName) && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}

		return false;
	}
}
