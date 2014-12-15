package com.gome.haoyuangong.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.fragments.BaseFragment;
import com.gome.haoyuangong.fragments.DTOpinionListFragment;
import com.gome.haoyuangong.fragments.HotOpinionListFragment;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.tougu.HotLabelResult;
import com.gome.haoyuangong.net.url.NetUrlTougu;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.views.MyViewPageIndicator;

public class HotOpinionsActivity extends BaseActivity {

private static final String TAG = HotOpinionsActivity.class.getName();

	private ViewPager mViewPager;
	private PagerAdapter mPagerAdapter;
	private MyViewPageIndicator indicator;
	
//	private String[] tableBtnStrs = { "今日热点", "军工", "IPO", "中石化", "核电"};
	private String[] tableBtnStrs = { "今日热点"};
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_viewpager_layout);
		setTitle("投顾说");

		mViewPager = (ViewPager)findViewById(R.id.viewpager);
		indicator = (MyViewPageIndicator)findViewById(R.id.indicator);
		indicator.setVisibility(View.GONE);
		loadIv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getHotLabels(5);
			}
		});
		getHotLabels(5);
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
				String label = tableBtnStrs[position];
				try {
					label = URLEncoder.encode(tableBtnStrs[position],"utf8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
				}
				HotOpinionListFragment _f = new HotOpinionListFragment();
				Bundle args = new Bundle();
				args.putString(DTOpinionListFragment.BUNDLE_PARAM_FROM, "观点");
				args.putString(DTOpinionListFragment.BUNDLE_PARAM_PAGE_DOWN, "b");
				args.putString(DTOpinionListFragment.BUNDLE_PARAM_PAGE_UP, "f");
				args.putString(DTOpinionListFragment.BUNDLE_PARAM_URL, NetUrlTougu.OPINION_HOT.replace("_label", label));
				_f.setArguments(args);
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
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
//			container.removeViewAt(position);
	    }
	}
	
	private void getHotLabels(int count) {

		String url = String.format(NetUrlTougu.OPINION_HOT_LABELS, count);
		Log.e(TAG, url);
		JsonRequest<HotLabelResult> request = new JsonRequest<HotLabelResult>(Method.GET, url,
				new RequestHandlerListener<HotLabelResult>(getContext()) {

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
					public void onSuccess(String id, HotLabelResult data) {
						// TODO Auto-generated method stub
						if(data == null){
							Toast.makeText(HotOpinionsActivity.this, "未知异常", Toast.LENGTH_SHORT).show();
						}else{
							if(data.getRetCode() == 0){
								if(data.getData().getList().length > 0){
									Message msg = uiHandler.obtainMessage(1, data.getData().getList());
									uiHandler.sendMessage(msg);
								}else{
									Toast.makeText(HotOpinionsActivity.this, "无此类信息", Toast.LENGTH_SHORT).show();
								}
							}else{
								Toast.makeText(HotOpinionsActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
							}
						}
						
					}
				}, HotLabelResult.class);

		send(request);

	}
	
	private Handler uiHandler = new Handler(){
		
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case 1:
				String[] labels = (String[])msg.obj;
				initUI(labels);
				break;
			}
		}
	};
	
	private void initUI(String[] labels){
		tableBtnStrs = labels;
		mPagerAdapter = new MyFragmentAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
		indicator.setViewPager(mViewPager,tableBtnStrs);
		indicator.setVisibility(View.VISIBLE);
	}
}
