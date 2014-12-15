package com.gome.haoyuangong.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;

import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.fragments.AskListFragment;
import com.gome.haoyuangong.views.MyViewPageIndicator;
import com.gome.haoyuangong.R;
@EActivity(R.layout.activity_ask_tall)
public class AskTallActivity extends BaseActivity{
	@ViewById(R.id.viewpager)
	ViewPager mViewPager;
	@ViewById(R.id.indicator)
	MyViewPageIndicator indicator;
	private String[] tableBtnStrs = { "最新回答", "最新提问" };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@AfterViews
	void init(){
		setTitle("问投顾");
		mViewPager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager()));
    indicator.setViewPager(mViewPager,tableBtnStrs);
    if (UserInfo.getInstance().isLogin()
			&& !UserInfo.getInstance().isTougu()) {

		titleRight2.setBackgroundResource(R.drawable.top_ask_icon);
		titleRight2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getContext(),OpenConsultingActivity.class);
				startActivity(intent);
			}
		});

	}
	}
	
public class MyFragmentAdapter extends FragmentStatePagerAdapter {
		
		private SparseArray<Fragment> mapFragment = new SparseArray<Fragment>();
		
		public MyFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			
			AskListFragment f = (AskListFragment) mapFragment.get(position);
//			if(f == null){
//				f = new com.jrj.tougu.fragments.AskListFragment_();
//				int type=AskListFragment.TYPE_NEW_ANSWER;
//				if(position == 1){
//					type=AskListFragment.TYPE_NEW_ASK;
//				}
//				f.setType(position);
//				mapFragment.put(position, f);
//			}
			return f;
		}

		@Override
		public int getCount() {
			return 2;
		}
	}
}
