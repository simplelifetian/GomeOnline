package com.gome.haoyuangong.activity;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.gome.haoyuangong.fragments.CongenialListFragment;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.congenia.TitleResult;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.views.MyViewPageIndicator;
import com.gome.haoyuangong.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

@EActivity(R.layout.main_activity_find_congenial_list)
public class FindCongenialListActivity extends BaseActivity {

	private static final String TAG = FindCongenialListActivity.class.getName();

	public static final int OPT_HOT = 0;// /功能常量 热门
	public static final int OPT_SATISFACTION = 1;// /功能常量 满意度
	public static final int OPT_FANS = 2;// /功能常量 粉丝量

	public int OPT_COUNT = 3;// 总共

	@ViewById(R.id.viewpager)
	ViewPager mViewPager;
	@ViewById(R.id.indicator)
	MyViewPageIndicator indicator;

	MyFragmentAdapter mAdapter;

	
	ArrayList<String> keyList=new ArrayList<String>();
	ArrayList<String> valueList=new ArrayList<String>();

	private LinearLayout guideLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setTitle("找投顾");
//		titleRight2.setBackgroundResource(R.drawable.top_search_icon);
//		titleRight2.setBackgroundResource(R.drawable.find_adviser);
//		titleRight2.setOnClickListener(this);
		
//		guideLayout = (LinearLayout)findViewById(R.id.guide_layout);
//		if (!getGuideHome()) {
//			guideLayout.setVisibility(View.VISIBLE);
//			guideLayout.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					guideLayout.setVisibility(View.GONE);
//					SaveGuideHome();
//				}
//			});
//		}			
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.title_right2:
			startActivity(new Intent(this,
					FindAdviserCustomCondition.class));
//			startActivity(new Intent(this,
//					SearchCongenialAndContentActivity.class));

			break;
		}
	}

	@AfterViews
	void initView() {
		indicator.setVisibility(View.GONE);
		loadIv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getTitleList(1, 1, 1);
			}
		});
		getTitleList(1, 1, 1);
	}

	public class MyFragmentAdapter extends FragmentStatePagerAdapter {

		private SparseArray<Fragment> mapFragment = new SparseArray<Fragment>();

		public MyFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			CongenialListFragment f = (CongenialListFragment) mapFragment
					.get(position);

//			if (f == null) {
//				f = new com.jrj.tougu.fragments.CongenialListFragment_();
//				
//				Bundle args = new Bundle();
//				args.putString(CongenialListFragment.BUNDLE_PARAM_FROM, "观点");
//				args.putString(CongenialListFragment.BUNDLE_PARAM_URL, "");
//				f.setArguments(args);
//				f.setType(valueList.get(position));
//				mapFragment.put(position, f);
//			}

			return f;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return keyList.get(position);
		}

		@Override
		public int getCount() {
			return keyList.size();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
//			super.destroyItem(container, position, object);
		}
		
	}

	private void getTitleList(final int p, final int pz, final int requestType) {

		String url = "http://mapi.itougu.jrj.com.cn/wireless/account/label";
		Log.e(TAG, url);
		JsonRequest<TitleResult> request = new JsonRequest<TitleResult>(
				Method.GET, url, new RequestHandlerListener<TitleResult>(
						getContext()) {

					@Override
					public void onFailure(String id, int code, String str,
							Object obj) {
						// TODO Auto-generated method stub
						super.onFailure(id, code, str, obj);
					}

					@Override
					public void onStart(Request request) {
						super.onStart(request);
						showLoading(request);
					}

					@Override
					public void onEnd(Request request) {
						super.onEnd(request);
						hideLoading(request);
					}

					@Override
					public void onSuccess(String id, TitleResult data) {
						// TODO Auto-generated method stub
						if (data == null) {
							Toast.makeText(FindCongenialListActivity.this,
									"请求标题栏失败", Toast.LENGTH_SHORT).show();
//						}
						} else {
							if(data.getRetCode() == 0){
								int size=data.getData().getList().size();
								if(size > 0){
									ArrayList<TitleResult.Item> list=data.getData().getList();
									for(int i=0;i<list.size();i++){
										keyList.add(list.get(i).getKey());
										valueList.add(list.get(i).getValue());
									}
									Message msg = uiHandler.obtainMessage(1);
									uiHandler.sendMessage(msg);
								}else{
									Toast.makeText(FindCongenialListActivity.this, "无此类信息", Toast.LENGTH_SHORT).show();
								}
							}else{
								Toast.makeText(FindCongenialListActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
							}
						}

						// TODO Auto-generated method stub

					}
				}, TitleResult.class);

		send(request);

	}

	private Handler uiHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				initUI();
				break;
			}
		}
	};

	private void initUI() {
		mAdapter = new MyFragmentAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mAdapter);
		indicator.setViewPager(mViewPager, keyList.toArray(new String[keyList.size()]));
		indicator.setVisibility(View.VISIBLE);
	}

	private boolean getGuideHome() {
		// 建立一个缓存时间的文件
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		return sp.getBoolean("guideFindCongenial", false);
	}

	private void SaveGuideHome() {
		// 建立一个缓存时间的文件
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(this).edit();
		editor.putBoolean("guideFindCongenial", true);
		editor.commit();
	}
}
