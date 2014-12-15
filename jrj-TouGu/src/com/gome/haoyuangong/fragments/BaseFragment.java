package com.gome.haoyuangong.fragments;

import com.gome.haoyuangong.BaseViewImpl;
import com.gome.haoyuangong.R;
import com.gome.haoyuangong.RefreshTimeInfo;
import com.gome.haoyuangong.activity.BaseActivity;
import com.gome.haoyuangong.net.NetDisplayCounter;
import com.gome.haoyuangong.net.NetManager;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.NetDisplayCounter.RequestingDisplay;
import com.gome.haoyuangong.utils.StringUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BaseFragment extends Fragment implements OnClickListener ,BaseViewImpl{
	/** 用于显示加载中 */
	protected Dialog mProgressDialog;
	ViewGroup content;
	// title控件从左到右
	protected TextView titleLeft1;
	protected TextView titleLeft2;
	protected TextView titleCenter;
	protected TextView titleRight1;
	protected TextView titleRight2;
	protected ViewGroup titleWhole;
	protected View titleLeft1Ly;
	protected ViewGroup loadVg;
	protected ImageView loadIv;
	protected TextView loadTv;

//	private NetManager mNetManager;
	private NetDisplayCounter mDialogCounter;
	private NetDisplayCounter mLoadCounter;
	Activity mActivity;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
//		mNetManager = NetManager.init(activity);
	}
	@Override
	public void onDetach() {
		super.onDetach();
		mActivity=null;
//		mNetManager.cancelAll();
//		mNetManager=null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_base, null);
		initView(v);
		v.post(new Runnable() {
			@Override
			public void run() {
				startLoad();
			}
		});
		return v;
	}
	private void initView(View v) {
		content = (FrameLayout) v.findViewById(R.id.content);
		titleLeft1 = (TextView) v.findViewById(R.id.title_left1);
		titleLeft2 = (TextView) v.findViewById(R.id.title_left2);
		titleCenter = (TextView) v.findViewById(R.id.title_center);
		titleRight1 = (TextView) v.findViewById(R.id.title_right1);
		titleRight2 = (TextView) v.findViewById(R.id.title_right2);
		titleWhole = (ViewGroup) v.findViewById(R.id.title_whole);
		titleLeft1Ly = v.findViewById(R.id.title_left1_ly);

		loadVg = (ViewGroup) v.findViewById(R.id.load);
		loadIv = (ImageView) v.findViewById(R.id.load_img);
		loadTv = (TextView) v.findViewById(R.id.load_text);

		titleLeft1.setOnClickListener(this);
		titleLeft2.setOnClickListener(this);
		titleRight1.setOnClickListener(this);
		titleRight2.setOnClickListener(this);
		loadVg.setOnClickListener(this);
	}
	protected void setContent(View v){
		content.addView(v);
	}
	
	protected void setTitle(String title){
		titleCenter.setText(title);
	}
	public TextView getTitleRight1(){
		return titleRight1;
	}
	public TextView getTitleRight2(){
		return titleRight2;
	}
	protected void hideTitle(){
		titleWhole.setVisibility(View.GONE);
	}
	protected void showTitle(){
		titleWhole.setVisibility(View.VISIBLE);
	}
	protected void hideBack(){
		titleLeft1.setVisibility(View.GONE);
	}
	
	protected Bundle getExtra(){
		return mActivity.getIntent().getExtras();
	}

	public void showToast(String text) {
		Toast.makeText(mActivity, text, Toast.LENGTH_LONG).show();
	}

	protected void showToast(int resId) {
		String text = getString(resId);
		Toast.makeText(mActivity, text, Toast.LENGTH_LONG).show();
	}

	private void initDialogDisplay(final String textStr) {
		mDialogCounter = new NetDisplayCounter(new RequestingDisplay() {
			@Override
			public void show() {
				if (mProgressDialog == null) {
					// TODO init
					mProgressDialog = new Dialog(mActivity, R.style.DialogTransparent);
					View v = LayoutInflater.from(mActivity).inflate(R.layout.progress_dialog_common, null);
					mProgressDialog.setContentView(v);
					mProgressDialog.setCanceledOnTouchOutside(false);
					mProgressDialog.setOnCancelListener(new OnCancelListener() {
						@Override
						public void onCancel(DialogInterface dialog) {
							mDialogCounter.cancel();
							mDialogCounter = null;
						}
					});
				}
				TextView text = (TextView) mProgressDialog.getWindow().getDecorView().findViewById(R.id.dialog_common_text);
				if(!StringUtils.isEmpty(textStr)){
					text.setText(textStr);
					text.setVisibility(View.VISIBLE);
				}else{
					text.setText("加载中...");
					text.setVisibility(View.VISIBLE);
				}
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
				final AnimationDrawable anim = (AnimationDrawable) loadIv.getBackground();
				loadIv.post(new Runnable() {
					@Override
					public void run() {
						anim.start();
					}
				});
				loadTv.setText("加载中...");
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

	/**
	 * 发送网络请求
	 * 
	 * @param request
	 */
	public void send(Request request) {
	((BaseActivity)mActivity).send(request);
//		mNetManager.send(request);
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
	protected void replaceFragment(int id, Fragment fragment, String tag, boolean addBack) {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.replace(id, fragment, tag);
		if (addBack) {
			ft.addToBackStack(null);
		} else {
			if (fragmentManager.getBackStackEntryCount() > 0) {
				fragmentManager.popBackStack();
				ft.addToBackStack(null);
			}
		}
		ft.commit();
	}
	protected void back() {
		Activity a = getActivity();
		if (a != null) {
			a.onBackPressed();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left1:
			back();
			break;
		case R.id.title_left1_ly:
			titleLeft1.performClick();
			break;
		case R.id.load:
			if (mLoadCounter == null) {
				startLoad();
			}
			break;
		}
	}
	
	protected int getScreenW(){
		if(mActivity!=null){
			return((BaseActivity)mActivity).getScreenW();
		}
		return 0;
	}
	protected int getScreenH(){
		if(mActivity!=null){
			return((BaseActivity)mActivity).getScreenH();
		}
		return 0;
	}
	@Override
	public Context getContext() {
		return mActivity;
	}
	
	public void setResult(int resultCode,Intent data){
		if(mActivity!=null){
			mActivity.setResult(resultCode, data);
		}
	}
	public void finish(){
		if(mActivity!=null){
			mActivity.finish();
		}
	}
	
	protected void hideSoftInput() {
		BaseActivity a = (BaseActivity)mActivity;
		a.hideSoftInput();

	}

	protected void showSoftInput(View v) {
		BaseActivity a = (BaseActivity)mActivity;
		a.showSoftInput(v);
	}
	
	public long getRefreshTime(String key){
		return ((BaseActivity)mActivity).getRefreshTime(key);
	}
	public String getRefreshTimeStr(String key){
		return ((BaseActivity)mActivity).getRefreshTimeStr(key);
	}
	public void saveRefreshTime(String key){
		((BaseActivity)mActivity).saveRefreshTime(key);
	}
}
