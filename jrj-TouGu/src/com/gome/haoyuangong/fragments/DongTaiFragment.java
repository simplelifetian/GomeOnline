package com.gome.haoyuangong.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.activity.LoginActivity;
import com.gome.haoyuangong.activity.OpenConsultingActivity;
import com.gome.haoyuangong.activity.SearchCongenialAndContentActivity;
import com.gome.haoyuangong.activity.WriteOpinionActivity;
import com.gome.haoyuangong.net.url.NetUrlTougu;
import com.gome.haoyuangong.utils.StringUtils;
import com.gome.haoyuangong.views.MyViewPageIndicator;

public class DongTaiFragment extends BaseFragment {

	public static boolean isOncreate = false;

	// public static final int OPT_ATTENTION = 0;// /功能常量关注
	// public static final int OPT_MESSAGE = 1;// /功能常量 消息
	// public static final int OPT_OPINION = 2;// /功能常量 观点
	// public static final int OPT_INFO = 3;// /功能常量 资讯

	private ViewPager mViewPager;
	private PagerAdapter mPagerAdapter;

	// private DongTaiListFragment currentInfoColumnFragment;

	private String[] tableBtnStrs = { "关注", "消息", "热点" };
	private MyViewPageIndicator indicator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View parent = super.onCreateView(inflater, container, savedInstanceState);
		View child = inflater.inflate(R.layout.simple_viewpager_layout, container, false);
		content.addView(child);
		initChildTitle();
		findViews(parent);
		return parent;
	}

	private void initChildTitle() {
		titleLeft1.setVisibility(View.GONE);
		titleCenter.setText("动态");
		titleRight1.setBackgroundResource(R.drawable.top_search_icon);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (UserInfo.getInstance().isLogin() && UserInfo.getInstance().isTougu()) {

			titleRight2.setBackgroundResource(R.drawable.icon_write_opinion);
			titleRight2.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(mActivity, WriteOpinionActivity.class);
					startActivity(intent);
				}
			});

		} else {
			titleRight2.setBackgroundResource(R.drawable.top_ask_icon);
			titleRight2.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (UserInfo.getInstance().isLogin()) {
						Intent intent = new Intent(mActivity, OpenConsultingActivity.class);
						startActivity(intent);
					} else {
						Intent intent = new Intent(mActivity, LoginActivity.class);
						intent.putExtra(LoginActivity.BUNDLE_PARAM_TARGET_ACTIVITY, "com.jrj.tougu.activity.OpenConsultingActivity");
						intent.putExtra(LoginActivity.BUNDLE_PARAM_TARGET_ACTIVITY_TYPE, 1);
						startActivity(intent);
						
//						Intent intent = new Intent(mActivity, LoginActivity.class);
//						intent.putExtra(LoginActivity.BUNDLE_PARAM_LOGIN_TYPE, LoginActivity.LOGIN_TYPE_AUTO);
//						intent.putExtra(LoginActivity.BUNDLE_PARAM_LOGIN_NAME, "hbg");
//						intent.putExtra(LoginActivity.BUNDLE_PARAM_LOGIN_PASSWD, "2014");
//						intent.putExtra(LoginActivity.BUNDLE_PARAM_TARGET_ACTIVITY, "com.jrj.tougu.activity.OpenConsultingActivity");
//						startActivity(intent);
					}
				}
			});
		}
	}

	private void findViews(View view) {
		mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
		mPagerAdapter = new MyFragmentAdapter(getChildFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);

		indicator = (MyViewPageIndicator) view.findViewById(R.id.indicator);
		indicator.setViewPager(mViewPager, tableBtnStrs);
		if(!firstTime()){
			mViewPager.setCurrentItem(2);
			saveFirstTime();
		}

//		indicator.setItemIndex(1, "5");
//		indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//			@Override
//			public void onPageSelected(int position) {
//				// TODO Auto-generated method stub
//				if (position == 1) {
//					indicator.setItemIndex(position, "");
//				}
//			}
//
//			@Override
//			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onPageScrollStateChanged(int state) {
//				// TODO Auto-generated method stub
//
//			}
//		});
	}

	public class MyFragmentAdapter extends FragmentStatePagerAdapter {

		private SparseArray<BaseFragment> mapFragment = new SparseArray<BaseFragment>();

		public MyFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			BaseFragment f = mapFragment.get(position);
			if (f == null) {

				switch (position) {
				case 0:
					GZOpinionListFragment gf = new GZOpinionListFragment();
					f = gf;
					break;
				case 1:
					MessageListFragment mf = new MessageListFragment();
					mf.setPageIndicatorTipListener(new PageIndicatorTipListener() {
						
						@Override
						public void onTipChanged(int visable) {
							// TODO Auto-generated method stub
							if(View.VISIBLE == visable){
								indicator.setItemIndex(1, "5");
							}else{
								indicator.setItemIndex(1, "");
							}
						}
					});
					f = mf;
					mapFragment.put(position, f);
					break;
				case 2:
					DTHotOpinionListFragment _f = new DTHotOpinionListFragment();
					f = _f;
					mapFragment.put(position, f);
					break;
				}
//				mapFragment.put(position, f);
			}
			return f;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return tableBtnStrs[position % tableBtnStrs.length];
		}

		@Override
		public int getCount() {
			return tableBtnStrs.length;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
//			container.removeViewAt(position);
	    }
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {

		case R.id.title_right1:
			startActivity(new Intent(mActivity, SearchCongenialAndContentActivity.class));
			break;

		// case R.id.title_right2:
		// Intent intent = new Intent(mActivity, WriteOpinionActivity.class);
		//
		// Intent intent = new Intent(mActivity,OpenConsultingActivity.class);
		// startActivity(intent);
		//
		// break;
		}
	}
	
	public static interface PageIndicatorTipListener{
		
		public void onTipChanged(int visable);
		
	}
	
	private boolean firstTime() {
		// 建立一个缓存时间的文件
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(mActivity);
		return sp.getBoolean("DongTaiFragment_first", false);
	}

	private void saveFirstTime() {
		// 建立一个缓存时间的文件
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(mActivity).edit();
		editor.putBoolean("DongTaiFragment_first", true);
		editor.commit();
	}

}
