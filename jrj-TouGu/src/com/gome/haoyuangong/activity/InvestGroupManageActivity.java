package com.gome.haoyuangong.activity;

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
import android.widget.RelativeLayout;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.activity.InvestOpinionActivity.MyFragmentAdapter;
import com.gome.haoyuangong.fragments.BaseFragment;
import com.gome.haoyuangong.fragments.MyOpinionGraftFragment;
import com.gome.haoyuangong.fragments.MyOpinionPublishedFragment;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.views.MyViewPageIndicator;
/**
 * 管理组合
 * @author Administrator
 *
 */
public class InvestGroupManageActivity extends BaseActivity {
	private static final String TAG = InvestGroupManageActivity.class.getName();
	private ViewPager mViewPager;
	private PagerAdapter mPagerAdapter;
	private String[] tableBtnStrs = { "持仓", "买入","卖出","撤单","查询","更多" };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("品种一");
		setContentView(R.layout.simple_viewpager_layout);
		
		mViewPager = (ViewPager)findViewById(R.id.viewpager);
		mPagerAdapter = new MyFragmentAdapter(getSupportFragmentManager());
		 mViewPager.setAdapter(mPagerAdapter);
		 MyViewPageIndicator indicator = (MyViewPageIndicator)findViewById(R.id.indicator);
	     indicator.setViewPager(mViewPager,tableBtnStrs);
	     titleRight2.setText("写日志");
//	     titleRight2.setTextSize(this.getResources().getDimension(R.dimen.text_size_8));
	     titleRight2.setGravity(Gravity.CENTER);
	     titleRight2.setOnClickListener(this);
	}
	@Override
	public void onClick(View v){
		super.onClick(v);
		switch(v.getId()){
			case R.id.title_right2:			
				startActivity(new Intent(this,WriteOpinionActivity.class));
				break;
		}
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
				if(position == 0){
					MyOpinionPublishedFragment _f = new MyOpinionPublishedFragment();
					f = _f;
				}else{
					MyOpinionGraftFragment _f = new MyOpinionGraftFragment();
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
