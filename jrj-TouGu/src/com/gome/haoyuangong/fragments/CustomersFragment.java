package com.gome.haoyuangong.fragments;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.fragments.DongTaiFragment.PageIndicatorTipListener;
import com.gome.haoyuangong.views.MyViewPageIndicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class CustomersFragment extends BaseFragment{
	private ViewPager mViewPager;
	private PagerAdapter mPagerAdapter;
	private MyViewPageIndicator mIndicator;
	private ImageView mAdd;
	private String[] tableBtnStrs = { "消息", "名片", "动态" };
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View parent = super.onCreateView(inflater, container, savedInstanceState);
		View child = inflater.inflate(R.layout.customer_fragment, container, false);
		content.addView(child);
		initView(child);
		return parent;
	}
	
	private void initView(View v) {
		hideTitle();
		mViewPager = (ViewPager) v.findViewById(R.id.viewpager);
		mIndicator = (MyViewPageIndicator) v.findViewById(R.id.indicator);
		mAdd = (ImageView) v.findViewById(R.id.iv_add);
		mPagerAdapter = new MyFragmentAdapter(getChildFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
		
		mIndicator.setViewPager(mViewPager, tableBtnStrs);
		mViewPager.setCurrentItem(0);
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
					CustomersMsgFragment gf = new CustomersMsgFragment();
					f = gf;
					break;
				case 1:
					MessageListFragment mf = new MessageListFragment();
					mf.setPageIndicatorTipListener(new PageIndicatorTipListener() {
						
						@Override
						public void onTipChanged(int visable) {
							// TODO Auto-generated method stub
							if(View.VISIBLE == visable){
								mIndicator.setItemIndex(1, "5");
							}else{
								mIndicator.setItemIndex(1, "");
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
}
