package com.gome.haoyuangong.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.haoyuangong.Environment;
import com.gome.haoyuangong.LogDataUtils;
import com.gome.haoyuangong.LogUpdate;
import com.gome.haoyuangong.R;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.db.QuoteDic;
import com.gome.haoyuangong.fragments.CustomersFragment;
import com.gome.haoyuangong.fragments.EmptyFragment;
import com.gome.haoyuangong.fragments.FoundFragments;
import com.gome.haoyuangong.fragments.GoodsISellFragment;
import com.gome.haoyuangong.fragments.ResultsFragment;
import com.gome.haoyuangong.fragments.SelfFragment;
import com.gome.haoyuangong.log.Logger;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.BaseResultWeb;
import com.gome.haoyuangong.net.result.XinGeBaseResult;
import com.gome.haoyuangong.net.result.tougu.UserIdentifiedResult;
import com.gome.haoyuangong.net.result.version.VersionInfBean;
import com.gome.haoyuangong.net.url.NetUrlTougu;
import com.gome.haoyuangong.net.url.XinGeURL;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.update.UpdateManager;
import com.gome.haoyuangong.views.MyFragmentTabHost;
import com.gome.haoyuangong.xg.MessageReceiver;
import com.gome.haoyuangong.xg.XgMessageDeal;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.service.cache.CacheManager;

public class MainActivity extends BaseActivity{

	private static final String TAG = MainActivity.class.getName();
	// 信鸽用
	public final static String CONTENT = "content";
	public final static String TITLE = "title";
	public final static String CUSTOMCONTENT = "customcontent";

	private static final int QUEST_LOGINED_CODE = 8;
	//
	private String myTabId;

	// 定义MyFragmentTabHost对象
	private MyFragmentTabHost mTabHost;
	// 定义一个布局
	private LayoutInflater layoutInflater;

	// 定义数组来存放Fragment界面
	private Class fragmentArray[] = { ResultsFragment.class, CustomersFragment.class,
			GoodsISellFragment.class, SelfFragment.class };
	// 定义数组来存放按钮图片
	private int mImageViewArray[] = { R.drawable.tab_results, R.drawable.tab_customer, 
			R.drawable.tab_product, R.drawable.tab_sales_promotion};

	// Tab选项卡的文字
	private int mTextViewArray[] = { R.string.actionbar_bottom_results, R.string.actionbar_bottom_customer,
			R.string.actionbar_bottom_product, R.string.actionbar_bottom_sales };

	private SharedPreferences sharedPreferences;
	BroadcastReceiver registDeviceReceiver;

	private LinearLayout guideLayout;

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		int xgflag = intent.getIntExtra("xgflag", 0);
		if (xgflag == 1) {
			dealXG(intent);
		}
	}
	public void onCreate(Bundle savedInstanceState) {
		MessageReceiver.activity = this;
		super.onCreate(savedInstanceState);
		Environment.init(this);
		setContentView(R.layout.activity_main);
		sharedPreferences = getContext().getSharedPreferences("xginfo",
				Context.MODE_PRIVATE);
		if (!TextUtils.isEmpty(sharedPreferences.getString("deviceid", ""))) {
			UserInfo.getInstance().setDeivceId(
					sharedPreferences.getString("deviceid", ""));
		}
		guideLayout = (LinearLayout) findViewById(R.id.guide_layout);
		if (!getGuideHome()) {
			guideLayout.setVisibility(View.VISIBLE);
			guideLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					guideLayout.setVisibility(View.GONE);
					SaveGuideHome();
				}
			});
		}
		registXG();
		initView();
		if (UserInfo.getInstance().isLogin()) {
			
		}
		if (UserInfo.getInstance().isLogin()) {
			getUserInfo(UserInfo.getInstance().getUserId());
		}

		int xgflag = getIntent().getIntExtra("xgflag", 0);
		if (xgflag == 1) {
			dealXG(getIntent());
		}
		LogUpdate.getInstance().postLog(this);
		UpdateManager.getInstance().autoUpdate();
	}

	private void dealXG(Intent intent) {
		XgMessageDeal.DealMessage(this,
				intent.getStringExtra(CUSTOMCONTENT));
	}

	@Override
	protected void onStart() {
		super.onResume();
		QuoteDic dic = new QuoteDic(this);
		dic.checkTime();
	}

	private void registXG() {
		XGPushManager.registerPush(getApplicationContext(),
				new XGIOperateCallback() {
					@Override
					public void onSuccess(Object data, int flag) {
						sharedPreferences.edit()
								.putString("deviceid", data.toString())
								.commit();
						System.out.println("信鸽注册成功:" + data.toString());
						UserInfo.getInstance().setDeivceId(data.toString());
						// registReceiver();
						CacheManager.getRegisterInfo(getApplicationContext());
						registDevice();
					}

					@Override
					public void onFail(Object data, int errCode, String msg) {
						Logger.info("xgfail", msg);
						System.out.println("信鸽注册失败:" + data.toString());
					}
				});
	}

	private void registDevice() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("deviceToken", UserInfo.getInstance().getDeivceId());
		params.put("deviceType", "3");
		// params.put("user_type", "1");
		// params.put("user_id", "141027010023086621");
		if (!TextUtils.isEmpty(UserInfo.getInstance().getUserId()))
			params.put("user_id", UserInfo.getInstance().getUserId());
		params.put("appid", "tougu");
		if (!UserInfo.getInstance().isLogin())
			params.put("user_type", "3");
		else if (UserInfo.getInstance().isTougu())
			params.put("user_type", "2");
		else
			params.put("user_type", "0");

		JsonRequest<XinGeBaseResult> request = new JsonRequest<XinGeBaseResult>(
				Method.POST, XinGeURL.REGISTDEVICE, params,
				new RequestHandlerListener<XinGeBaseResult>(getContext()) {

					@Override
					public void onStart(Request request) {
						super.onStart(request);
						// showDialog(request);
					}

					@Override
					public void onEnd(Request request) {
						super.onEnd(request);
						// hideDialog(request);
					}

					@Override
					public void onFailure(String id, int code, String str,
							Object obj) {
					}

					@Override
					public void onSuccess(String id, XinGeBaseResult data) {
						// TODO Auto-generated method stub
						// Toast.makeText(ReplyActivity.this, "赞成功",
						// Toast.LENGTH_SHORT).show();
						// Toast.makeText(MainActivity.this, data.getMsg(),
						// Toast.LENGTH_SHORT).show();
					}
				}, XinGeBaseResult.class);
		send(request);

	}

	private void initView() {
		hideTitle();
		// 实例化布局对象
		layoutInflater = LayoutInflater.from(this);
		mTabHost = (MyFragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realTabContent);
		mTabHost.getTabWidget().setDividerDrawable(null);

		// 得到fragment的个数
		int count = fragmentArray.length;

		for (int i = 0; i < count; i++) {
			// 为每一个Tab按钮设置图标、文字和内容
			String tab = getResources().getString(mTextViewArray[i]);
			TabHost.TabSpec tabSpec = mTabHost.newTabSpec(tab).setIndicator(
					getTabItemView(i));
			// 将Tab按钮添加进Tab选项卡中
			mTabHost.addTab(tabSpec, fragmentArray[i], null);
			// 设置Tab按钮的背景

			mTabHost.getTabWidget().getChildAt(i)
					.setBackgroundColor(getResources().getColor(R.color.tab_bg));

			mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
				@Override
				public void onTabChanged(String tabId) {
//					myTabId = tabId;

//					String _tabId = getResources().getString(mTextViewArray[3]);
//					if (tabId != null && tabId.equals(_tabId)
//							&& !UserInfo.getInstance().isLogin()) {
//						Intent intent = new Intent(MainActivity.this,
//								LoginActivity.class);
//						startActivityForResult(intent, QUEST_LOGINED_CODE);
//					} else {
						mTabHost.doChanged(tabId);
//					}
				}
			});
		}
		registDeviceReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (LoginActivity.LOGINED_ACTION.equals(intent.getAction())) {
					registDevice();
				}
			}
		};
		registerReceiver(registDeviceReceiver, new IntentFilter(
				LoginActivity.LOGINED_ACTION));
	}

	/**
	 * 给Tab按钮设置图标和文字
	 */
	private View getTabItemView(int index) {
		View view = layoutInflater.inflate(R.layout.tab_item_view, null);

		 TextView textView = (TextView) view.findViewById(R.id.textView);
		// Drawable image = getResources().getDrawable(mImageViewArray[index]);
		// textView.setCompoundDrawablesWithIntrinsicBounds(null, image, null,
		// null);
		textView.setText(mTextViewArray[index]);
		ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
		imageView.setImageResource(mImageViewArray[index]);

		return view;
	}

	private void getVersionInf() {

		String url = String
				.format("http://mobilead.jrj.com.cn/forceUpdate?productId=1019025&productVer=%s",
						"1.0.0");
		Log.e("wangwei_test", url);
		JsonRequest<VersionInfBean> request = new JsonRequest<VersionInfBean>(
				Method.GET, url, new RequestHandlerListener<VersionInfBean>(
						getContext()) {

					@Override
					public void onStart(Request request) {
						super.onStart(request);
						showDialog(request);
					}

					@Override
					public void onEnd(Request request) {
						super.onEnd(request);
						hideDialog(request);
					}

					@Override
					public void onSuccess(String id, VersionInfBean data) {
						// TODO Auto-generated method stub

						if (data.getVersionCold().compareTo(getVersion()) > 0) {
							showActivityAlert(data.getContent(), data.getUrl(),
									(data.getIsForce() > 1));
						}
					}
				}, VersionInfBean.class);

		send(request);

	}

	/**
	 * 测试版本信息
	 */
	private String getVersion() {
		PackageInfo pkg;
		try {
			pkg = getPackageManager().getPackageInfo(
					getApplication().getPackageName(), 0);
			String appName = pkg.applicationInfo.loadLabel(getPackageManager())
					.toString();
			String versionName = pkg.versionName;
			System.out.println("appName:" + appName);
			System.out.println("versionName:" + versionName);
			return versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 显示出更新提示dialog
	 * 
	 * @param content
	 * @param dlUrl
	 */
	private void showActivityAlert(String content, final String dlUrl,
			final boolean isForceU) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("更新提示");
		dialog.setMessage(content);
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (dlUrl != null && dlUrl.length() > 0) {
					String a_url = dlUrl;
					if (!a_url.startsWith("http://")) {
						a_url = "http://" + a_url;
					}
					Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri
							.parse(a_url));
					startActivity(viewIntent);
				}
			}
		});

		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (isForceU) {
					finish();
				}
			}
		});
		dialog.show();
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		switch (arg0) {
		case QUEST_LOGINED_CODE:
			if (LoginActivity.LOGINED_RESPONSE_CODE == arg1) {
				mTabHost.doChanged(myTabId);
			} else {
				if (mTabHost.getmLastTabString() != null) {
					mTabHost.setCurrentTabByTag(mTabHost.getmLastTabString());
				}
			}
			break;
		default:
			break;
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(registDeviceReceiver);
		super.onDestroy();
	}

	private void getUserInfo(String userid) {

		String url = String.format(NetUrlTougu.USER_IDENTIFY, userid);// 000822010000046691
		// String url =
		// String.format(NetUrlTougu.USER_IDENTIFY,"000822010000046691");
		Logger.error(TAG, "login url : " + url);
		JsonRequest<UserIdentifiedResult> request = new JsonRequest<UserIdentifiedResult>(
				Method.GET, url, null,
				new RequestHandlerListener<UserIdentifiedResult>(getContext()) {

					@Override
					public void onStart(Request request) {
						super.onStart(request);
						// showDialog(request);
					}

					@Override
					public void onEnd(Request request) {
						super.onEnd(request);
						// hideDialog(request);
					}

					@Override
					public void onSuccess(String id, UserIdentifiedResult data) {
						// TODO Auto-generated method stub
						// Toast.makeText(ReplyActivity.this, "赞成功",
						// Toast.LENGTH_SHORT).show();
						LogUpdate.getInstance().addLog(
								LogDataUtils.FUNCTIONID_LOG_USERINFO,
								NetUrlTougu.USER_IDENTIFY,
								String.valueOf(data.getRetCode()),
								data.getMsg());
						if (data.getRetCode() == 0 && data != null) {

							// Toast.makeText(MainActivity.this, "更新用户身份成功！",
							// Toast.LENGTH_SHORT).show();

							UserInfo.getInstance().setTouguUserBean(
									data.getData().getUser());

							UserInfo.saveUserInfo(MainActivity.this,
									UserInfo.getInstance());

						} else {
							// Toast.makeText(MainActivity.this, "更新用户身份失败！",
							// Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onFailure(String id, int code, String str,
							Object obj) {

						if (obj != null && obj instanceof BaseResultWeb) {
							BaseResultWeb data = (BaseResultWeb) obj;
							LogUpdate.getInstance().addLog(
									LogDataUtils.FUNCTIONID_LOG_USERINFO,
									NetUrlTougu.USER_IDENTIFY,
									String.valueOf(data.getRetCode()),
									data.getMsg());
						} else {
							super.onFailure(id, code, str, obj);
						}
					}

				}, UserIdentifiedResult.class);

		send(request);
	}

	private boolean getGuideHome() {
		// 建立一个缓存时间的文件
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		return sp.getBoolean("guideHome", false);
	}

	private void SaveGuideHome() {
		// 建立一个缓存时间的文件
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(this).edit();
		editor.putBoolean("guideHome", true);
		editor.commit();
	}

	private long exitTime = 0;

	private void exitApp() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitApp();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
}
