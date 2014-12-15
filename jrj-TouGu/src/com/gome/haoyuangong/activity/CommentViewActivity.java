/**
 * 
 */
package com.gome.haoyuangong.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.fragments.BaseFragment;
import com.gome.haoyuangong.fragments.CommentListFragment;
import com.gome.haoyuangong.fragments.FenSiListFragment;
import com.gome.haoyuangong.views.MyViewPageIndicator;

/**
 * 
 */
public class CommentViewActivity extends BaseActivity {

	private static final String TAG = CommentViewActivity.class.getName();
	
	private ViewPager mViewPager;
	private PagerAdapter mPagerAdapter;

	private String[] tableBtnStrs = { "评论", "粉丝" };
	private MyViewPageIndicator indicator;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_viewpager_layout);
		setTitle("评论");
		
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mPagerAdapter = new MyFragmentAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
		 
		indicator = (MyViewPageIndicator)findViewById(R.id.indicator);
	    indicator.setViewPager(mViewPager,tableBtnStrs);
	     
	    indicator.setItemIndex(1,"5");
	    indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
		
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				if(position == 1){
					indicator.setItemIndex(position, "");
				}
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				
			}
		});
		
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
				
				switch (position) {
				case 0:
					CommentListFragment cf = new CommentListFragment();
					f = cf;
					break;
				case 1:
					FenSiListFragment ff = new FenSiListFragment();
					f = ff;
					break;
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
