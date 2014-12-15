/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright (C) 2011 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gome.haoyuangong.views;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import java.text.SimpleDateFormat;

import org.xmlpull.v1.XmlPullParser;

import com.gome.haoyuangong.R;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;


/**
 * This widget implements the dynamic action bar tab behavior that can change
 * across different configurations or circumstances.
 */
public class MyViewPageIndicator extends HorizontalScrollView implements PageIndicator {
	/** Title text used when no title is provided by the adapter. */
	private static final CharSequence EMPTY_TITLE = "";

	/**
	 * Interface for a callback when the selected tab has been reselected.
	 */
	public interface OnTabReselectedListener {
		/**
		 * Callback when the selected tab has been reselected.
		 * 
		 * @param position
		 *            Position of the current center item.
		 */
		void onTabReselected(int position);
	}

	private Runnable mTabSelector;

	private final OnClickListener mTabClickListener = new OnClickListener() {
		public void onClick(View view) {
			TabView tabView = (TabView)((LinearLayout) view).findViewWithTag("tavView");
			final int oldSelected = mViewPager.getCurrentItem();
			final int newSelected = tabView.getIndex();
			mViewPager.setCurrentItem(newSelected);
			if (oldSelected == newSelected && mTabReselectedListener != null) {
				mTabReselectedListener.onTabReselected(newSelected);
			}
		}
	};

	private final LinearLayout mTabLayout;

	private ViewPager mViewPager;
	private ViewPager.OnPageChangeListener mListener;

	private int mMaxTabWidth;
	private int mSelectedTabIndex;

	private OnTabReselectedListener mTabReselectedListener;

	private String[] tabs;
	private int padding ;
	private int pointSize;

	public MyViewPageIndicator(Context context) {
		this(context, null);
	}

	public MyViewPageIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		setHorizontalScrollBarEnabled(false);
		padding = dipToPixels(10);
		pointSize = dipToPixels(8);
		mTabLayout = new LinearLayout(context);
		addView(mTabLayout, new ViewGroup.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
	}

	public void setOnTabReselectedListener(OnTabReselectedListener listener) {
		mTabReselectedListener = listener;
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		final boolean lockedExpanded = widthMode == MeasureSpec.EXACTLY;
		setFillViewport(lockedExpanded);

		final int childCount = mTabLayout.getChildCount();
		if (childCount > 1 && (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST)) {
			if (childCount > 2) {
				mMaxTabWidth = (int) (MeasureSpec.getSize(widthMeasureSpec) * 0.4f);
			} else {
				mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
			}
		} else {
			mMaxTabWidth = -1;
		}

		final int oldWidth = getMeasuredWidth();
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int newWidth = getMeasuredWidth();

		if (lockedExpanded && oldWidth != newWidth) {
			// Recenter the tab display if we're at a new (scrollable) size.
			setCurrentItem(mSelectedTabIndex);
		}
	}

	private void animateToTab(final int position) {
		final View tabView = mTabLayout.getChildAt(position);
		if (mTabSelector != null) {
			removeCallbacks(mTabSelector);
		}
		mTabSelector = new Runnable() {
			public void run() {
				final int scrollPos = tabView.getLeft() - (getWidth() - tabView.getWidth()) / 2;
				smoothScrollTo(scrollPos, 0);
				mTabSelector = null;
			}
		};
		post(mTabSelector);
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		if (mTabSelector != null) {
			// Re-post the selector we saved
			post(mTabSelector);
		}
	}

	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (mTabSelector != null) {
			removeCallbacks(mTabSelector);
		}
	}

	private void addTab(int index, CharSequence text, int iconResId) {
		final TabView tabView = new TabView(getContext());
		tabView.mIndex = index;
		tabView.setTag("tavView");
		tabView.setId(index);
//		tabView.setFocusable(true);
//		tabView.setOnClickListener(mTabClickListener);
		tabView.setText(text);
		tabView.setPadding(pointSize, 0, pointSize, 0);
		tabView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
		tabView.setMaxLines(1);
		tabView.setGravity(Gravity.CENTER);
		XmlPullParser xrp = getResources().getXml(R.drawable.tab_button);
		try {
			ColorStateList csl = ColorStateList.createFromXml(getResources(), xrp);
			tabView.setTextColor(csl);
		} catch (Exception e) {

		}

		if (iconResId != 0) {
			tabView.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0, 0);
		}
		
		FrameLayout.LayoutParams tabTvL = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		tabView.setLayoutParams(tabTvL);
		
		measureView(tabView);
		
		LinearLayout layoutWraper1 = new LinearLayout(getContext());
//		LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT,1);
//		layoutWraper1.setLayoutParams(lp1);
		layoutWraper1.setGravity(Gravity.CENTER);
		layoutWraper1.setFocusable(true);
		layoutWraper1.setOnClickListener(mTabClickListener);
		
		FrameLayout frameLayout = new FrameLayout(getContext());
		frameLayout.setTag("framelayout");
		FrameLayout.LayoutParams fl2 = new FrameLayout.LayoutParams(tabView.getMeasuredWidth() + padding,tabView.getMeasuredHeight()+ (int)(padding * 1.5));
//		frameLayout.setPadding(padding, padding, padding, padding);
		frameLayout.setLayoutParams(fl2);
//		frameLayout.addView(tabView);
		
		View hasNew = new View(getContext());
		FrameLayout.LayoutParams hasNewL = new FrameLayout.LayoutParams(pointSize,pointSize);
		hasNew.setLayoutParams(hasNewL);
		hasNewL.gravity = Gravity.TOP | Gravity.RIGHT;
		hasNew.setBackgroundResource(R.drawable.shape_circle);
		
		FrameLayout tabTvframe = new FrameLayout(getContext());
		FrameLayout.LayoutParams tabTvframeL = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		tabTvframeL.gravity = Gravity.CENTER;
		tabTvframe.setLayoutParams(tabTvframeL);
		tabTvframe.addView(tabView);
		tabTvframe.addView(hasNew);
		hasNew.setVisibility(View.GONE);
		
		frameLayout.addView(tabTvframe);
		
		layoutWraper1.addView(frameLayout);

		tabView.setTagView(hasNew);
		
		mTabLayout.addView(layoutWraper1, new LinearLayout.LayoutParams(0, MATCH_PARENT, 1));
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		if (mListener != null) {
			mListener.onPageScrollStateChanged(arg0);
		}
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		if (mListener != null) {
			mListener.onPageScrolled(arg0, arg1, arg2);
		}
	}

	@Override
	public void onPageSelected(int arg0) {
		setCurrentItem(arg0);
		if (mListener != null) {
			mListener.onPageSelected(arg0);
		}
	}

	@Override
	public void setViewPager(ViewPager view, String[] tabs) {
		if (mViewPager == view) {
			return;
		}
		if (mViewPager != null) {
			mViewPager.setOnPageChangeListener(null);
		}
		this.tabs = tabs;
		final PagerAdapter adapter = view.getAdapter();
		if (adapter == null) {
			throw new IllegalStateException("ViewPager does not have adapter instance.");
		}
		mViewPager = view;
		view.setOnPageChangeListener(this);
		notifyDataSetChanged();
	}

	public void notifyDataSetChanged() {
		mTabLayout.removeAllViews();
		PagerAdapter adapter = mViewPager.getAdapter();
		final int count = adapter.getCount();
		for (int i = 0; i < count; i++) {

			CharSequence title;
			if (tabs != null && tabs.length > i) {
				title = tabs[i];
			} else {
				title = adapter.getPageTitle(i);
			}
			if (title == null) {
				title = EMPTY_TITLE;
			}
			int iconResId = 0;
			addTab(i, title, iconResId);
		}
		if (mSelectedTabIndex > count) {
			mSelectedTabIndex = count - 1;
		}
		setCurrentItem(mSelectedTabIndex);
		requestLayout();
	}

	@Override
	public void setViewPager(ViewPager view, int initialPosition, String[] tabs) {
		setViewPager(view, tabs);
		setCurrentItem(initialPosition);
	}

	@Override
	public void setCurrentItem(int item) {
		if (mViewPager == null) {
			throw new IllegalStateException("ViewPager has not been bound.");
		}
		mSelectedTabIndex = item;
		mViewPager.setCurrentItem(item);

		final int tabCount = mTabLayout.getChildCount();
		for (int i = 0; i < tabCount; i++) {
			final View child = mTabLayout.getChildAt(i);
			final boolean isSelected = (i == item);
			child.setSelected(isSelected);
			if (isSelected) {
				animateToTab(item);
			}
			FrameLayout f = (FrameLayout)child.findViewWithTag("framelayout");
			if(i == item){
				f.setBackgroundResource(R.drawable.icon_tab_focus);
			}else{
				f.setBackgroundResource(0);
			}
		}
	}

	@Override
	public void setOnPageChangeListener(OnPageChangeListener listener) {
		mListener = listener;
	}

	private class TabView extends TextView {
		private int mIndex;
		private View tagView;

		public TabView(Context context) {
			super(context, null, 0);
		}

		@Override
		public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);

			// Re-measure if we went beyond our maximum size.
			if (mMaxTabWidth > 0 && getMeasuredWidth() > mMaxTabWidth) {
				super.onMeasure(MeasureSpec.makeMeasureSpec(mMaxTabWidth, MeasureSpec.EXACTLY), heightMeasureSpec);
			}
		}

		public int getIndex() {
			return mIndex;
		}
		
		public void setTagView(View tagView){
			this.tagView = tagView;
		}
		
		public void setTagViewText(String str){
			if(str == null || str.trim().length() == 0 ||this.tagView == null){
				this.tagView.setVisibility(View.GONE);
				return;
			}
			this.tagView.setVisibility(View.VISIBLE);
		}
	}
	
	public void setItemIndex(int index,String s) {
		if (mViewPager == null) {
			throw new IllegalStateException("ViewPager has not been bound.");
		}

		TabView child = (TabView)((LinearLayout)mTabLayout.getChildAt(index)).findViewWithTag("tavView");
		child.setTagViewText(s);
	}
	
	private int dipToPixels(int dip) {
		Resources r = getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
		return (int) px;
	}
	
	  private void measureView(View child) {
		    ViewGroup.LayoutParams p = child.getLayoutParams();
		    if (p == null) {
		      p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
		          ViewGroup.LayoutParams.WRAP_CONTENT);
		    }
		    int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		    int lpHeight = p.height;
		    int childHeightSpec;
		    if (lpHeight > 0) {
		      childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
		          MeasureSpec.EXACTLY);
		    } else {
		      childHeightSpec = MeasureSpec.makeMeasureSpec(0,
		          MeasureSpec.UNSPECIFIED);
		    }
		    child.measure(childWidthSpec, childHeightSpec);
		  }
}
