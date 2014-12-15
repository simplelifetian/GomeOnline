/*
 * Copyright (C) 2013 The Android Open Source Project
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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.utils.DateUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;


/**
 * 布局下拉刷新类
 * 
 * @author winter
 * 
 */
public class SwipeRefreshLayout extends ViewGroup {
	private static final float ACCELERATE_INTERPOLATION_FACTOR = 1.5f;
	private static final float DECELERATE_INTERPOLATION_FACTOR = 2f;
	private static final float MAX_SWIPE_DISTANCE_FACTOR = .6f;
	private static final int REFRESH_TRIGGER_DISTANCE = 120;

	private final static int RELEASE_To_REFRESH = 0;// 松手状态
	private final static int PULL_To_REFRESH = 1;// 回弹状态
	private final static int REFRESHING = 2;// 刷新状态
	private final static int DONE = 3;// 初始状态
	private final static int REFRESHED = 4;// 刷新完成状态

	private View mTarget; // the header
	private int mOriginalOffsetTop;
	private OnRefreshListener mListener;
	private MotionEvent mDownEvent;
	private int mFrom;
	private boolean mRefreshing = false;
	private int mTouchSlop;
	private float mDistanceToTriggerSync = -1;
	private int mMediumAnimationDuration;
	private int mProgressBarHeight;
	private int mCurrentTargetOffsetTop;
	private int mDownTargetTop;
	// Target is returning to its start offset because it was cancelled or a
	// refresh was triggered.
	private boolean mReturningToStart;
	private final DecelerateInterpolator mDecelerateInterpolator;
	private final AccelerateInterpolator mAccelerateInterpolator;
	private static final int[] LAYOUT_ATTRS = new int[] { android.R.attr.enabled };
	private boolean isDisAllowIntercept = false;
	private int mHeaderHeight;
	private int state;// 当前状态
	private LinearLayout headView;// 下拉刷新头部
	private RotateAnimation animation;// 箭头下拉动画
	private RotateAnimation reverseAnimation;// 箭头回弹动画
	private TextView tipsTextview;// 头部提示文字
	private TextView lastUpdatedTextView;// 头部上次更新时间
	private ImageView arrowImageView;// 头部箭头图标
	private ProgressBar progressBar;// 头部等待进度条
	private String tag;
	private SharedPreferences sharedPreferences;

	private final Animation mAnimateToStartPosition = new Animation() {
		@Override
		public void applyTransformation(float interpolatedTime, Transformation t) {
			int targetTop = 0;
			if (mFrom != mOriginalOffsetTop) {
				targetTop = (mFrom + (int) ((mOriginalOffsetTop - mFrom) * interpolatedTime));

			}
			int offset = targetTop - mOriginalOffsetTop - mHeaderHeight;
			setTargetOffsetTopAndBottom(offset);
		}
	};
	private final Animation mAnimateToHeaderPosition = new Animation() {
		@Override
		public void applyTransformation(float interpolatedTime, Transformation t) {
			int targetTop = mHeaderHeight;
			int headerOriginal = mHeaderHeight + getPaddingTop();
			if (mFrom != headerOriginal) {
				targetTop = (mFrom + (int) ((headerOriginal - mFrom) * interpolatedTime));

			}
			int offset = targetTop - headerOriginal;
			setTargetOffsetTopAndBottom(offset);
		}
	};

	private final AnimationListener mReturnToStartPositionListener = new BaseAnimationListener() {
		@Override
		public void onAnimationEnd(Animation animation) {
			// Once the target content has returned to its start position, reset
			// the target offset to 0
			mReturningToStart = false;
		}
	};

	// Cancel the refresh gesture and animate everything back to its original
	// state.
	private final Runnable mReturnToStartPosition = new Runnable() {
		@Override
		public void run() {
			mReturningToStart = true;
			animateOffsetToStartPosition(mCurrentTargetOffsetTop + getPaddingTop(), mReturnToStartPositionListener);
		}

	};
	// animate move to header showing (refreshing)
	private final Runnable mMoveHeader = new Runnable() {
		@Override
		public void run() {
			mReturningToStart = true;
			animateOffsetToHeaderPosition(mCurrentTargetOffsetTop + getPaddingTop(), mReturnToStartPositionListener);
		}

	};

	/**
	 * Simple constructor to use when creating a SwipeRefreshLayout from code.
	 * 
	 * @param context
	 */
	public SwipeRefreshLayout(Context context) {
		this(context, null);
		sharedPreferences = getContext().getSharedPreferences("freshtime",Context.MODE_PRIVATE);		
	}

	/**
	 * Constructor that is called when inflating SwipeRefreshLayout from XML.
	 * 
	 * @param context
	 * @param attrs
	 */
	public SwipeRefreshLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

		mMediumAnimationDuration = getResources().getInteger(android.R.integer.config_mediumAnimTime);

		setWillNotDraw(false);
		mDecelerateInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);
		mAccelerateInterpolator = new AccelerateInterpolator(ACCELERATE_INTERPOLATION_FACTOR);

		final TypedArray a = context.obtainStyledAttributes(attrs, LAYOUT_ATTRS);
		setEnabled(a.getBoolean(0, true));
		a.recycle();

		animation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);

		state = DONE;
		headView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.list_head, null);
		arrowImageView = (ImageView) headView.findViewById(R.id.head_arrowImageView);
//		arrowImageView.setMinimumWidth(70);
//		arrowImageView.setMinimumHeight(50);
		progressBar = (ProgressBar) headView.findViewById(R.id.head_progressBar);
		tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);
		lastUpdatedTextView = (TextView) headView.findViewById(R.id.head_lastUpdatedTextView);
		measureView(headView);
		mHeaderHeight = headView.getMeasuredHeight();
		headView.setPadding(0, -mHeaderHeight, 0, 0);
		addView(headView);

	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		removeCallbacks(mReturnToStartPosition);
		removeCallbacks(mMoveHeader);
	}

	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		removeCallbacks(mReturnToStartPosition);
		removeCallbacks(mMoveHeader);
	}
	public void setTag(String tag){
		this.tag = tag;
		getFreshTime();
	}
	public void setFreshTime(Date dt) {
		String dateString = DateUtils.format(dt, "yyyy-MM-dd HH:mm:ss");
		if (sharedPreferences != null && !TextUtils.isEmpty(tag))
			sharedPreferences.edit().putString(tag, dateString).commit();
		lastUpdatedTextView.setText("上次更新时间："+dateString);
	}
	private void getFreshTime() {
		if (sharedPreferences == null || TextUtils.isEmpty(tag))
			return;
		String dateString = sharedPreferences.getString(tag, "");
		sharedPreferences.edit().putString(tag, dateString).commit();
		lastUpdatedTextView.setText("上次更新时间："+dateString);
	}
	private void animateOffsetToStartPosition(int from, AnimationListener listener) {
		mFrom = from;
		mAnimateToStartPosition.reset();
		mAnimateToStartPosition.setDuration(mMediumAnimationDuration);
		mAnimateToStartPosition.setAnimationListener(listener);
		mAnimateToStartPosition.setInterpolator(mDecelerateInterpolator);
		mTarget.startAnimation(mAnimateToStartPosition);
	}

	private void animateOffsetToHeaderPosition(int from, AnimationListener listener) {
		mFrom = from;
		mAnimateToHeaderPosition.reset();
		mAnimateToHeaderPosition.setDuration(mMediumAnimationDuration);
		mAnimateToHeaderPosition.setAnimationListener(listener);
		mAnimateToHeaderPosition.setInterpolator(mDecelerateInterpolator);
		mTarget.startAnimation(mAnimateToHeaderPosition);
	}

	/**
	 * Set the listener to be notified when a refresh is triggered via the swipe
	 * gesture.
	 */
	public void setOnRefreshListener(OnRefreshListener listener) {
		mListener = listener;
	}

	/**
	 * Notify the widget that refresh state has changed. Do not call this when
	 * refresh is triggered by a swipe gesture.
	 * 
	 * @param refreshing
	 *          Whether or not the view should show refresh progress.
	 */
	public void setRefreshing(boolean refreshing) {
		if (mRefreshing != refreshing) {
			ensureTarget();
			mRefreshing = refreshing;
		}
	}

	/**
	 * @return Whether the SwipeRefreshWidget is actively showing refresh
	 *         progress.
	 */
	public boolean isRefreshing() {
		return mRefreshing;
	}

	private void ensureTarget() {
		// Don't bother getting the parent height if the parent hasn't been laid out
		// yet.
		if (mTarget == null) {
			if (getChildCount() > 2 && !isInEditMode()) {
				throw new IllegalStateException("SwipeRefreshLayout can host only one direct child");
			}
			mTarget = getChildAt(0);
			mOriginalOffsetTop = mTarget.getTop() + getPaddingTop();
		}
		if (mDistanceToTriggerSync == -1) {
			if (getParent() != null && ((View) getParent()).getHeight() > 0) {
				final DisplayMetrics metrics = getResources().getDisplayMetrics();
				mDistanceToTriggerSync = (int) Math.min(((View) getParent()).getHeight() * MAX_SWIPE_DISTANCE_FACTOR, REFRESH_TRIGGER_DISTANCE * metrics.density);
				mDistanceToTriggerSync = REFRESH_TRIGGER_DISTANCE * metrics.density;
			}
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		final int width = getMeasuredWidth();
		final int height = getMeasuredHeight();
		if (getChildCount() == 0) {
			return;
		}
		final View child0 = getChildAt(0);
		final View child = getChildAt(1);
		final int childLeft = getPaddingLeft();
		final int childTop = mCurrentTargetOffsetTop + getPaddingTop();
		final int childWidth = width - getPaddingLeft() - getPaddingRight();
		final int childHeight = height - getPaddingTop() - getPaddingBottom();
		child0.layout(childLeft, getPaddingTop(), childLeft + child0.getMeasuredWidth(), childTop + mHeaderHeight);
		child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (getChildCount() > 2 && !isInEditMode()) {
			throw new IllegalStateException("SwipeRefreshLayout can host only one direct child");
		}
		if (getChildCount() > 1) {
			getChildAt(1).measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY));
			getChildAt(0).measure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	/**
	 * @return Whether it is possible for the child view of this layout to scroll
	 *         up. Override this if the child view is a custom view.
	 */
	public boolean canChildScroll() {
		if (android.os.Build.VERSION.SDK_INT < 14) {
			if (mTarget instanceof AbsListView) {
				final AbsListView absListView = (AbsListView) mTarget;
				return absListView.getChildCount() > 0 && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0).getTop() < absListView.getPaddingTop());
			} else {
				return mTarget.getScrollY() > 0;
			}
		} else {
			return ViewCompat.canScrollVertically(mTarget, -1);
		}
	}

	private boolean canChildScroll(ViewGroup group, int direction, int startindex) {
		boolean canScroll = false;
		if (mCurrentTargetOffsetTop != 0) {
			return false;
		}
		int count = group.getChildCount();
		for (int i = startindex; i < count; i++) {
			View child = group.getChildAt(i);
			if (child instanceof LinearLayout || child instanceof RelativeLayout) {
				canScroll = canChildScroll((ViewGroup) child, direction, 0);
			} else {
				canScroll = canScroll(child, direction);
				return canScroll;
			}
		}
		return canScroll;
	}

	/**
	 * 此方法兼容两种子布局的判断，listview，scrollview 主要作用是判断两个子View是否滚动到了最上面，若是，则表示此次touch
	 * move事件截取然后让layout来处理，来移动下拉视图，反之则不然
	 * 
	 * @MethodDescription canScroll
	 * @return
	 * @exception
	 * @since 1.0.0
	 */
	private boolean canScroll(View v, int direction) {
		View childView = v;
		if (childView instanceof ListView) {
			if (((ListView) childView).getChildCount() == 0) {
				return false;
			}
			int top = ((ListView) childView).getChildAt(0).getTop();
			int pad = ((ListView) childView).getListPaddingTop();
			if (direction > 0) {
				if ((Math.abs(top - pad)) < 3 && ((ListView) childView).getFirstVisiblePosition() == 0) {
					return false;
				} else {
					return true;
				}
			} else if (direction < 0) {
				return true;
			}
		} else if (childView instanceof ScrollView) {
			if (direction > 0) {
				if (((ScrollView) childView).getScrollY() == 0) {
					return false;
				} else {
					return true;
				}
			} else if (direction < 0) {
				if (((ScrollView) childView).getScrollY() + ((ScrollView) childView).getHeight() >= ((ScrollView) childView).getChildAt(0).getMeasuredHeight()) {
					return false;
				} else {
					return true;
				}
			}
		}
		return false;
	}

	private int mInterceptY;
	private int mOldHandled = 0;// 0 未初始化 1 拦截事件 2不拦截事件

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		ensureTarget();
		boolean handled = false;
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			mInterceptY = (int) ev.getY();
		}
		if (isEnabled() && !canChildScroll(this, (int) ev.getY() - mInterceptY, 1)) {
			if (mReturningToStart && ev.getAction() == MotionEvent.ACTION_DOWN) {
				mReturningToStart = false;
				removeAnimateMove();
			}
			handled = onTouchEvent(ev);
		} else {
			mDownEvent = MotionEvent.obtain(ev);
			mDownTargetTop = mCurrentTargetOffsetTop;
		}
		mInterceptY = (int) ev.getY();
		if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			if (mOldHandled == 1 && !handled) {
				MotionEvent event = MotionEvent.obtain(ev);
				event.setAction(MotionEvent.ACTION_DOWN);
				getChildAt(1).dispatchTouchEvent(event);
				disableSubControls(getChildAt(1));
				event.recycle();
			} else if (mOldHandled == 2 && handled) {
				MotionEvent event = MotionEvent.obtain(ev);
				event.setAction(MotionEvent.ACTION_CANCEL);
				getChildAt(1).dispatchTouchEvent(event);
				event.recycle();
			} else if (mOldHandled == 0 && handled) {
				MotionEvent event = MotionEvent.obtain(ev);
				event.setAction(MotionEvent.ACTION_CANCEL);
				getChildAt(1).dispatchTouchEvent(event);
				event.recycle();
			}
			if (handled) {
				mOldHandled = 1;
			} else {
				mOldHandled = 2;
			}
		} else if (ev.getAction() == MotionEvent.ACTION_UP) {
			mOldHandled = 0;
		}
		if (!handled) {
			super.dispatchTouchEvent(ev);
		}
		return true;
	}

	private  void disableSubControls(View view) {
		if (view instanceof ViewGroup) {
			ViewGroup viewGroup = (ViewGroup) view;
			for (int i = 0; i < viewGroup.getChildCount(); i++) {
				View v = viewGroup.getChildAt(i);
				if (v instanceof ViewGroup) {
					if(v instanceof Spinner){
						v.setPressed(false);
					}else if(v instanceof ListView){
						removeListTapCallback((ListView) v);
					}else{
						removeViewTapCallback(v);
						disableSubControls((ViewGroup) v);
					}
				} else {
					removeViewTapCallback(v);
				}
			}
		}else{
			removeViewTapCallback(view);
		}
	}
	private void removeListTapCallback(ListView list){
		try {
			Field field = AbsListView.class.getField("mPendingCheckForTap");
			field.setAccessible(true);
			Runnable c = (Runnable)( field.get(list));
			if(c!=null){
				
				list.removeCallbacks(c);
			}
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void removeViewTapCallback(View v){
		try {
			Method m = View.class.getDeclaredMethod("removeTapCallback");
			m.setAccessible(true);
			m.invoke(v);
			System.out.println(m);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public boolean shouldDelayChildPressedState() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void requestDisallowInterceptTouchEvent(boolean b) {
		// Nope.
		isDisAllowIntercept = b;
		// System.out.println("requestDisallowInterceptTouchEvent:"+b);
	}

	private boolean isTouching = false;
	private boolean isScrolling = false;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		boolean handled = false;
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mDownEvent = MotionEvent.obtain(event);
			mDownTargetTop = mCurrentTargetOffsetTop;
			if (state == REFRESHED && mDownTargetTop == 0) {
				setState(DONE);
			}
			isTouching = true;
			isScrolling = false;
			break;
		case MotionEvent.ACTION_MOVE:
			if (mDownEvent != null && !mReturningToStart) {
				final float eventY = event.getY();
				float yDiff1 = (eventY - mDownEvent.getY());
				if (Math.abs(yDiff1) > mTouchSlop) {
					isScrolling = true;
				}
				if (state == DONE && Math.abs(yDiff1) > mTouchSlop) {
					setState(PULL_To_REFRESH);
				}
				float yDiff = (yDiff1 / 2);
				if (state == PULL_To_REFRESH || state == RELEASE_To_REFRESH) {
					updateContentOffsetTop((int) (yDiff) + mDownTargetTop);
					if (mCurrentTargetOffsetTop >= mHeaderHeight + 5) {
						setState(RELEASE_To_REFRESH);
					} else if (mCurrentTargetOffsetTop <= 0) {
						setState(DONE);
					} else {
						setState(PULL_To_REFRESH);
					}
					// if (yDiff < 0)
					// yDiff = 0;
					handled = true;
				}
				if ((state == REFRESHING || state == REFRESHED) && isScrolling) {
					updateContentOffsetTop((int) (yDiff) + mDownTargetTop);
					handled = true;
				}
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			isTouching = false;
			isScrolling = false;
			removeCallbacks(mReturnToStartPosition);
			removeCallbacks(mMoveHeader);
			if (state == PULL_To_REFRESH || state == REFRESHED) {
				post(mReturnToStartPosition);
			}
			if (state == RELEASE_To_REFRESH) {
				post(mMoveHeader);
				setState(REFRESHING);
			}
			if (state == REFRESHING) {
				if (mCurrentTargetOffsetTop > mHeaderHeight) {
					post(mMoveHeader);
				}
			}
			mDownTargetTop = -1;
			if (mDownEvent != null) {
				mDownEvent.recycle();
				mDownEvent = null;
			}
			break;
		}
		return handled;
	}

	private void removeAnimateMove() {
		removeCallbacks(mReturnToStartPosition);
		removeCallbacks(mMoveHeader);
		mTarget.clearAnimation();
	}

	private void setState(int state) {
		if (this.state == state)
			return;
		changeHeaderViewByState(this.state, state);
		this.state = state;

	}

	public void changeHeaderViewByState(int oldState, int newState) {
		switch (newState) {
		case RELEASE_To_REFRESH:
			arrowImageView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			// lastUpdatedTextView.setVisibility(View.VISIBLE);

			arrowImageView.clearAnimation();
			arrowImageView.startAnimation(animation);

			tipsTextview.setText("松开刷新数据");
			break;
		case PULL_To_REFRESH:
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			// lastUpdatedTextView.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.VISIBLE);
			// 是由RELEASE_To_REFRESH状态转变来的
			if (oldState == RELEASE_To_REFRESH) {
				arrowImageView.clearAnimation();
				arrowImageView.startAnimation(reverseAnimation);
			}
			tipsTextview.setText("下拉刷新");
			break;
		case REFRESHING:
			// headView.setPadding(0, 0, 0, 0);

			progressBar.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.GONE);
			tipsTextview.setText("正在加载...");
			// lastUpdatedTextView.setVisibility(View.VISIBLE);

			startRefresh();
			break;
		case DONE:
			// headView.setPadding(0, -1 * mHeaderHeight, 0, 0);

			progressBar.setVisibility(View.GONE);
			arrowImageView.clearAnimation();
			arrowImageView.setImageResource(R.drawable.goicon);
			tipsTextview.setText("下拉刷新");
			// lastUpdatedTextView.setVisibility(View.VISIBLE);

			break;
		case REFRESHED:
			progressBar.setVisibility(View.GONE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.GONE);
			tipsTextview.setText("刷新完成");
			setFreshTime(new Date());
			// lastUpdatedTextView.setVisibility(View.VISIBLE);
			break;
		}
	}

	private void startRefresh() {
		setRefreshing(true);
		if (mListener != null) {
			mListener.onRefresh();
		}
	}

	public void startRefreshAuto() {
		setState(REFRESHING);
		setTargetOffsetTopAndBottom(0);
	}

	public void stopRefresh() {
		if (state != REFRESHING) {
			return;
		}
		setState(REFRESHED);
		if (!isTouching) {
			post(mReturnToStartPosition);
		}
	}

	private void updateContentOffsetTop(int targetTop) {
		// final int currentTop = mTarget.getPaddingTop();
		if (targetTop > mDistanceToTriggerSync) {
			// targetTop = (int) mDistanceToTriggerSync;
		} else if (targetTop < 0) {
			targetTop = 0;
		}
		setTargetOffsetTopAndBottom(targetTop - mHeaderHeight);
	}

	private void setTargetOffsetTopAndBottom(int offset) {
		mTarget.setPadding(0, offset, 0, 0);
		mCurrentTargetOffsetTop = mTarget.getPaddingTop() + mHeaderHeight;
	}

	/**
	 * Classes that wish to be notified when the swipe gesture correctly triggers
	 * a refresh should implement this interface.
	 */
	public interface OnRefreshListener {
		public void onRefresh();
	}

	/**
	 * Simple AnimationListener to avoid having to implement unneeded methods in
	 * AnimationListeners.
	 */
	private class BaseAnimationListener implements AnimationListener {
		@Override
		public void onAnimationStart(Animation animation) {
		}

		@Override
		public void onAnimationEnd(Animation animation) {
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}
	}

	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}
}
