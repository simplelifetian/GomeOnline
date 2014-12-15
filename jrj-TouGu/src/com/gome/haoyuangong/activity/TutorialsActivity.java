package com.gome.haoyuangong.activity;

import java.util.ArrayList;

import com.gome.haoyuangong.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;


public class TutorialsActivity extends BaseActivity {

	Bundle bundle;

	/** 滑动控件 */
	private ViewPager viewPager;
	/** 引导进度条 */
	private LinearLayout layout;
	/** 开始按钮 */
	private TextView beginButton;

	private ImageView imageView;
	private Display display;
	private static final float SCALE = 0.33f;

	/** 存放引导进度条需要的View */
	private ArrayList<View> pageViews;
	/** 存放引导进度条中需要的ImageView */
	private ImageView[] imageViews;

	// /是否为从帮助进入标致，第一次运行也帮助共用，返回逻辑不一样
	private boolean isHelpFlag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		bundle = getIntent().getExtras();
		if (bundle != null) {
			isHelpFlag = bundle.getBoolean("isHelpFlag", false);
		}
		hideTitle();
		setContentView(R.layout.activity_tutorials);
		
		SaveTutorials();
		
		initView();
	}

	private void SaveTutorials() {
		// 建立一个缓存时间的文件
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(this).edit();
		editor.putBoolean("Tutorialsed", true);
		editor.commit();
	}

	private void initView() {
		// 控件赋值
		layout = (LinearLayout) findViewById(R.id.viewGroup);
		viewPager = (ViewPager) findViewById(R.id.guidePages);
		display = getWindowManager().getDefaultDisplay();
		imageView = (ImageView) findViewById(R.id.guide_bg);

		// 初始化需要展示的视图
		LayoutInflater inflater = getLayoutInflater();
		pageViews = new ArrayList<View>();
		pageViews.add(inflater.inflate(R.layout.item01, null));
		pageViews.add(inflater.inflate(R.layout.item02, null));
		pageViews.add(inflater.inflate(R.layout.item03, null));
		pageViews.add(inflater.inflate(R.layout.item04, null));

		// 初始化引导进度条
		imageViews = new ImageView[pageViews.size()];
		for (int i = 0; i < pageViews.size(); i++) {
			ImageView imageView = new ImageView(TutorialsActivity.this);
			android.widget.LinearLayout.LayoutParams params = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			DisplayMetrics metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
			params.leftMargin = (int) (5 * metrics.density + 0.5f);
			params.rightMargin = (int) (5 * metrics.density + 0.5f);
			imageView.setLayoutParams(params);
			imageViews[i] = imageView;

			if (i == 0) {
				imageViews[i]
						.setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				imageViews[i].setBackgroundResource(R.drawable.page_indicator);
			}
			layout.addView(imageViews[i]);
		}

		viewPager.setAdapter(new GuidePageAdapter());
		viewPager.setOnPageChangeListener(new GuidePageChangeListener());

		beginButton = (TextView) pageViews.get(pageViews.size() - 1)
				.findViewById(R.id.beginButton);
		beginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = getIntent();
				 intent.setClass(TutorialsActivity.this,
				 MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	private class GuidePageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return pageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(pageViews.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(pageViews.get(arg1));
			return pageViews.get(arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

		@Override
		public void finishUpdate(View arg0) {
		}
	}

	private class GuidePageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			int distance = (int) ((display.getWidth() * arg0 + arg2) * SCALE);
			imageView.layout(0 - distance, 0, imageView.getWidth() - distance,
					imageView.getHeight());
		}

		@Override
		public void onPageSelected(int arg0) {
			for (int i = 0; i < imageViews.length; i++) {
				imageViews[arg0]
						.setBackgroundResource(R.drawable.page_indicator_focused);
				if (arg0 != i) {
					imageViews[i]
							.setBackgroundResource(R.drawable.page_indicator);
				}
			}
		}
	}

}
