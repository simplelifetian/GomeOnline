package com.gome.haoyuangong.activity;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.fragments.BaseFragment;
import com.gome.haoyuangong.fragments.MyConsultAnswered;
import com.gome.haoyuangong.fragments.MyConsultUnAnswered;
import com.gome.haoyuangong.fragments.MyOpinionGraftFragment;
import com.gome.haoyuangong.fragments.MyOpinionPublishedFragment;
import com.gome.haoyuangong.layout.self.SelfView.UserType;
import com.gome.haoyuangong.views.MyViewPageIndicator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;

public class MyConsultActivity extends BaseActivity {
	private static final String TAG = MyConsultActivity.class.getName();
	public static String PARAMS_PAGEINDEX="pageindex";
	private ViewPager mViewPager;
	private PagerAdapter mPagerAdapter;
	private String[] tableBtnStrs = { "已回答", "未回答" };
	private int firstIndex = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UserType userType = UserType.values()[getIntent().getIntExtra("usertype", 0)];
		int pageindex = getIntent().getIntExtra(PARAMS_PAGEINDEX, 0);
		if (userType == userType.utUserViewAdviser){
			setTitle("回答记录");
			tableBtnStrs = new String[]{ "已回答" };
			firstIndex = 0;
		}
		else if (UserInfo.getInstance().isLogin() && UserInfo.getInstance().isTougu()){
			setTitle("回答记录");
			tableBtnStrs = new String[]{ "未回答", "已回答" };
			firstIndex = 1;
		}
		else if (UserInfo.getInstance().isLogin() && !UserInfo.getInstance().isTougu()){
			setTitle("咨询记录");
		}
		setContentView(R.layout.simple_viewpager_layout);
		
		mViewPager = (ViewPager)findViewById(R.id.viewpager);
		mPagerAdapter = new MyFragmentAdapter(getSupportFragmentManager());
		 mViewPager.setAdapter(mPagerAdapter);
		 MyViewPageIndicator indicator = (MyViewPageIndicator)findViewById(R.id.indicator);
	     indicator.setViewPager(mViewPager,tableBtnStrs);
	     if (tableBtnStrs.length <= 1)
	    	 indicator.setVisibility(View.GONE);
	     if (pageindex != 0 && pageindex<=tableBtnStrs.length-1)
	    	 indicator.setCurrentItem(pageindex);
	}
	
public class MyFragmentAdapter extends FragmentStatePagerAdapter {
		
		private SparseArray<BaseFragment> mapFragment = new SparseArray<BaseFragment>();
		
		public MyFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			
			BaseFragment f = mapFragment.get(position);
			if(f == null){
				
				CharSequence title = getPageTitle(position);
				Bundle args = new Bundle();
				if(position == firstIndex){
					MyConsultAnswered _f = new MyConsultAnswered();					
					args.putString("viewid", getIntent().getStringExtra("viewid"));
					args.putString("viewname", getIntent().getStringExtra("name"));
					args.putInt("usertype", getIntent().getIntExtra("usertype", -1));
					_f.setArguments(args);
					f = _f;
				}else{
					MyConsultUnAnswered _f = new MyConsultUnAnswered();
					args.putString("viewid", getIntent().getStringExtra("viewid"));
					args.putInt("usertype", getIntent().getIntExtra("usertype", -1));
					_f.setArguments(args);
					f = _f;
				}
				
				mapFragment.put(position, f);
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
	}
	
}
