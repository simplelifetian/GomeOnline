/**
 * 
 */
package com.gome.haoyuangong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.fragments.BaseFragment;
import com.gome.haoyuangong.fragments.LiveExchangeListFragment;
import com.gome.haoyuangong.fragments.LiveOpinionListFragment;
import com.gome.haoyuangong.utils.StringUtils;
import com.gome.haoyuangong.views.MyViewPageIndicator;

/**
 * 
 */
public class LiveRoomActivity extends BaseActivity {

	private static final String TAG = LiveRoomActivity.class.getName();
	public static final String BUNDLE_PARAM_NAME = "user_name";
	public static final String BUNDLE_PARAM_ROOMID = "room_id";

	private ViewPager mViewPager;
	private PagerAdapter mPagerAdapter;

	private String[] tableBtnStrs = { "今日观点", "交流区" };
	private MyViewPageIndicator indicator;
	private String userName;
	private String roomId; 
	private TextView newMsgTip;
	private LinearLayout newMsgLayout;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.live_viewpager_layout);
		setTitle("直播室");
		
		userName = getIntent().getStringExtra(BUNDLE_PARAM_NAME);
		roomId = getIntent().getStringExtra(BUNDLE_PARAM_ROOMID);
		if(StringUtils.isEmpty(roomId)){
			Toast.makeText(this, "无此直播室", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		if(!StringUtils.isEmpty(userName)){
			setTitle(userName+"的直播室");
		}
		
		titleRight2.setBackgroundResource(R.drawable.top_ask_icon);
		titleRight2.setOnClickListener(this);

		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mPagerAdapter = new MyFragmentAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);

		indicator = (MyViewPageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(mViewPager, tableBtnStrs);
		indicator.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				if(arg0 == 0){
					LiveOpinionListFragment cf = (LiveOpinionListFragment)mapFragment.get(0);
					if(cf != null){
						cf.startRefreshing(false);
					}
					
				}else{
					LiveOpinionListFragment cf = (LiveOpinionListFragment)mapFragment.get(0);
					if(cf != null){
						cf.stopRefreshing();
					}
				}
				hideSoftInput();
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});

		newMsgTip = (TextView)findViewById(R.id.new_msg_tip);
		newMsgLayout = (LinearLayout)findViewById(R.id.new_msg_lo);
		newMsgLayout.setOnClickListener(this);
		newMsgLayout.setVisibility(View.GONE);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.title_right2:
			if (UserInfo.getInstance().isLogin()) {
				Intent intent = new Intent(LiveRoomActivity.this, OpenConsultingActivity.class);
				intent.putExtra(OpenConsultingActivity.BUNDLE_TYPE, OpenConsultingActivity.SPECIAL_CONSULTING);
				intent.putExtra(OpenConsultingActivity.BUNDLE_PARAM_ID, roomId);
				intent.putExtra(OpenConsultingActivity.BUNDLE_PARAM_NAME, userName);
				startActivity(intent);
			} else {
				Intent intent = new Intent(LiveRoomActivity.this, LoginActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.new_msg_lo:
			LiveOpinionListFragment f = (LiveOpinionListFragment)mapFragment.get(0);
			if(f != null){
				mViewPager.setCurrentItem(0);
				f.startRefreshing(true);
			}
			newMsgLayout.setVisibility(View.GONE);
			break;
		}
	}
	
	
	private SparseArray<BaseFragment> mapFragment = new SparseArray<BaseFragment>();

	public class MyFragmentAdapter extends FragmentStatePagerAdapter {

		public MyFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			BaseFragment f = mapFragment.get(position);
			if (f == null) {

				switch (position) {
				case 0:
					LiveOpinionListFragment cf = new LiveOpinionListFragment();
					cf.setOnNewCountListener(new OnNewCountListener(){

						@Override
						public void action(int count) {
							// TODO Auto-generated method stub
							onNewCount(count);
						}
						
					});
					Bundle args = new Bundle();
					args.putString(LiveOpinionListFragment.BUNDLE_PARAM_ROOMID, roomId);
					cf.setArguments(args);
					f = cf;
					break;
				case 1:
					LiveExchangeListFragment ff = new LiveExchangeListFragment();
					Bundle args1 = new Bundle();
					args1.putString(LiveExchangeListFragment.BUNDLE_PARAM_ROOMID, roomId);
					args1.putString(LiveExchangeListFragment.BUNDLE_PARAM_NAME, userName);
					ff.setArguments(args1);
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
	
	private void onNewCount(int count){
		newMsgLayout.setVisibility(View.VISIBLE);
		newMsgTip.setText("有"+count+"条新观点  点击查看");
	}
	
	public static interface OnNewCountListener{
		
		public void action(int count);
	}
	
}
