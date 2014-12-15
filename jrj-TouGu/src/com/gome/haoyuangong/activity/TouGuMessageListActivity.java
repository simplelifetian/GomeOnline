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
import android.widget.Toast;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.fragments.BaseFragment;
import com.gome.haoyuangong.fragments.CommonMessageListFragment;
import com.gome.haoyuangong.fragments.MessageListFragment;
import com.gome.haoyuangong.utils.StringUtils;
import com.gome.haoyuangong.views.MyViewPageIndicator;

/**
 * 
 */
public class TouGuMessageListActivity extends BaseActivity {

	private static final String TAG = TouGuMessageListActivity.class.getName();
	
	public static final String BUNDLE_PARAM_USERID = "BUNDLE_PARAM_USERID";
	
	private ViewPager mViewPager;
	private PagerAdapter mPagerAdapter;

	private String[] tableBtnStrs = { "投顾通知", "投资组合提醒" };
	private MyViewPageIndicator indicator;
	
	private String userid;
	private String mtype;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userid = getIntent().getStringExtra(BUNDLE_PARAM_USERID);
		if(StringUtils.isEmpty(userid)){
			Toast.makeText(this, "无效用户", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		setContentView(R.layout.simple_viewpager_layout);
		setTitle("签约投顾");
		
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mPagerAdapter = new MyFragmentAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
		 
		indicator = (MyViewPageIndicator)findViewById(R.id.indicator);
	    indicator.setViewPager(mViewPager,tableBtnStrs);
	     
//	    indicator.setItemIndex(1,"5");
//	    indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//		
//			@Override
//			public void onPageSelected(int position) {
//				// TODO Auto-generated method stub
//				if(position == 1){
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
			if(f == null){
				
				switch (position) {
				case 0:{
					CommonMessageListFragment cf = new CommonMessageListFragment();
					Bundle args = new Bundle();
					args.putString(CommonMessageListFragment.BUNDLE_PARAM_USERID, userid);
					args.putInt(CommonMessageListFragment.BUNDLE_PARAM_MTYPE, MessageListFragment.MSG_TYPE_GROUP_MSG);
					cf.setArguments(args);
					f = cf;
					break;
				}
				case 1:{
					CommonMessageListFragment ff = new CommonMessageListFragment();
					Bundle args = new Bundle();
					args.putString(CommonMessageListFragment.BUNDLE_PARAM_USERID, userid);
					args.putInt(CommonMessageListFragment.BUNDLE_PARAM_MTYPE, MessageListFragment.MSG_TYPE_STOCK_GROUP);
					ff.setArguments(args);
					f = ff;
					break;
				}
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
