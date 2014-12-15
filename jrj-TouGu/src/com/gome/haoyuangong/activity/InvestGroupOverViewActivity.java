package com.gome.haoyuangong.activity;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.fragments.BaseFragment;
import com.gome.haoyuangong.fragments.InvestGroupChartFragment;
import com.gome.haoyuangong.views.MyViewPageIndicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.widget.LinearLayout;


public class InvestGroupOverViewActivity extends BaseActivity {
	private static final String TAG = InvestGroupOverViewActivity.class.getName();
	private ViewPager mViewPager;
	private PagerAdapter mPagerAdapter;
	private String[] tableBtnStrs = { "累计", "1周","2周","3周" };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invest_group_overview);
		LinearLayout view = (LinearLayout)findViewById(R.id.headlayout);
		mViewPager = (ViewPager)findViewById(R.id.overview_viewpager);
		mPagerAdapter = new MyFragmentAdapter(getSupportFragmentManager());
		 mViewPager.setAdapter(mPagerAdapter);
		 MyViewPageIndicator indicator = (MyViewPageIndicator)findViewById(R.id.overview_indicator);
		 
	     indicator.setViewPager(mViewPager,tableBtnStrs);
		setTitle("组合概况");
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
				
				InvestGroupChartFragment _f = new InvestGroupChartFragment();
				f = _f;		
				
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
