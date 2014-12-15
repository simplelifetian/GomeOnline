package com.gome.haoyuangong.views;

import com.gome.haoyuangong.AppInfo;
import com.gome.haoyuangong.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 自选股管理界面列表，拖拽更新列表数据顺序
 * **/
public class ListViewInterceptor extends ListView {

	private DropListener mDropListener;// 拖动监听回调对象
	private ImageView mDragView;// 拖拽图片
	private int mDragPos; // which item is being dragged
	private int mFirstDragPos; // where was the dragged item originally
	private int mDragPoint; // at what offset inside the item did the user grab
	// it
	private int mCoordOffset; // the difference between screen coordinates and

	private Rect mTempRect = new Rect();// 显示区域
	private final int mTouchSlop;// 最小滑动间距
	private int mHeight;// 当前项高度
	private int mUpperBound;// 上移间距
	private int mLowerBound;// 下移间距
	private WindowManager mWindowManager;// 屏幕管理对象
	private WindowManager.LayoutParams mWindowParams;// 屏幕布局对象
	private int dragndropBackgroundColor = 0x00000000;// 滑动列表背景色
	private Bitmap mDragBitmap;// 当前拖拽图片
	private int mItemHeightHalf = 32;
	private int mItemHeightNormal = 64;
	private int mItemHeightExpanded = 128;

	private boolean isDelModel = false;

	public ListViewInterceptor(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public ListViewInterceptor(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		if (attrs != null) {
			TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TouchListView, 0, 0);
			mItemHeightNormal = a.getDimensionPixelSize(R.styleable.TouchListView_normal_height, 0);
			mItemHeightExpanded = a.getDimensionPixelSize(R.styleable.TouchListView_expanded_height, mItemHeightNormal);
			// System.out.println(mItemHeightNormal +
			// "  expanded:"+mItemHeightExpanded);
			a.recycle();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		// Log.v(">>>>>>>>>>onTouchEvent", ">>>>>>>>>>onTouchEvent");
		if ((mDropListener != null) && mDragView != null) {
			int action = ev.getAction();
			switch (action) {
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				Rect r = mTempRect;
				mDragView.getDrawingRect(r);
				stopDragging();
				if (mDropListener != null && mDragPos >= 0 && mDragPos < getCount()) {
					mDropListener.drop(mFirstDragPos, mDragPos);
				}
				unExpandViews(false);
				break;

			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				int x = (int) ev.getX();
				int y = (int) ev.getY();
				dragView(x, y);

				int itemnum = getItemForPosition(y);
				// System.out.println("itemnum:"+itemnum);
				if (itemnum >= 0) {
					if (action == MotionEvent.ACTION_DOWN || itemnum != mDragPos) {
						mDragPos = itemnum;
						// doExpansion();
						// Log.v(">>>doExpansion", ">>>>>>>>>>doExpansion");
					}
					int speed = 0;
					adjustScrollBounds(y);
					if (y > mLowerBound) {
						// scroll the list up a bit
						speed = y > (mHeight + mLowerBound) / 2 ? 16 : 4;
					} else if (y < mUpperBound) {
						// scroll the list down a bit
						speed = y < mUpperBound / 2 ? -16 : -4;
					}
					// System.out.println("speed:"+speed);
					if (speed != 0) {
						int ref = pointToPosition(10, mHeight / 2);
						if (ref == AdapterView.INVALID_POSITION) {
							// we hit a divider or an invisible view, check
							// somewhere else
							ref = pointToPosition(10, mHeight / 2 + getDividerHeight() + 64);
						}
						View v = getChildAt(ref - getFirstVisiblePosition());
						if (v != null) {
							int pos = v.getTop();
							setSelectionFromTop(ref, pos - speed);

						}
					}
				}
				break;
			}
			return true;
		}
		return super.onTouchEvent(ev);

	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		
		if (mDropListener != null) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				int x = (int) ev.getX();
				int y = (int) ev.getY();
				int itemnum = pointToPosition(x, y);
				// Log.v("itemnum>>>", ">>>>>>>>" + itemnum);
				if (itemnum == AdapterView.INVALID_POSITION) {
					break;
				}
				ViewGroup item = (ViewGroup) getChildAt(itemnum - getFirstVisiblePosition());
				View dragger = item.findViewById(R.id.movingIV);
				if (dragger.getVisibility() == View.GONE) {
					break;
				}
				// System.out.println("dragger view:"+dragger.getVisibility());
				// Log.v("itemnum>>>", ">>>>>>>>" + getFirstVisiblePosition()
				// + "---" + ev.getRawY() + "----" + ev.getY()+"-----"+item.getTop());
				mCoordOffset = ((int) ev.getRawY()) - y;
				Rect r = mTempRect;
				// dragger.getDrawingRect(r);
				int[] location = new int[2];
				mDragPoint = y - item.getTop();
				dragger.getLocationOnScreen(location);
				// System.out.println( dragger.getTop()+"   X:"+x+"  "+location[0]);
				r.left = location[0];
				r.right = location[0] + dragger.getWidth();
				r.top = dragger.getTop();
				r.bottom = dragger.getBottom();
				if ((location[0] < x) && (x < location[0] + r.width())) {
					item.destroyDrawingCache();
					item.buildDrawingCache();
					// item.setDrawingCacheEnabled(true);
					// Create a copy of the drawing cache so that it does not
					// get recycled
					// by the framework when the list tries to clean up memory
					Bitmap bitmap = Bitmap.createBitmap(item.getDrawingCache());
					startDragging(bitmap, y);
					mDragPos = itemnum;
					mFirstDragPos = mDragPos;
					mHeight = getHeight();
					int touchSlop = mTouchSlop;
					mUpperBound = Math.min(y - touchSlop, mHeight / 3);
					mLowerBound = Math.max(y + touchSlop, mHeight * 2 / 3);
					return false;
				}
				mDragView = null;
				break;
			}
		}
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (isDelModel){
			boolean tag =false;
			if(inRangeOfView(delBtn, ev)){
				return super.dispatchTouchEvent(ev);
			}
			if(!tag){
				if(ev.getAction()==MotionEvent.ACTION_CANCEL||ev.getAction()==MotionEvent.ACTION_UP){
					hideDelBtn();
				}
			}
			return true;
		}
		return super.dispatchTouchEvent(ev);
	}
	
	
	/**
	 * 开始滑动当前选中项
	 * **/
	private void startDragging(Bitmap bm, int y) {
		stopDragging();

		mWindowParams = new WindowManager.LayoutParams();
		mWindowParams.gravity = Gravity.TOP;
		mWindowParams.x = 0;
		mWindowParams.y = y - mDragPoint + mCoordOffset;

		mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
		mWindowParams.format = PixelFormat.TRANSLUCENT;
		mWindowParams.windowAnimations = 0;

		ImageView v = new ImageView(getContext());
		// int backGroundColor =
		// getContext().getResources().getColor(R.color.dragndrop_background);
		v.setBackgroundColor(dragndropBackgroundColor);
		v.setImageBitmap(bm);
		mDragBitmap = bm;

		mWindowManager = (WindowManager) getContext().getSystemService("window");
		mWindowManager.addView(v, mWindowParams);
		mDragView = v;
	}

	private void stopDragging() {
		if (mDragView != null) {
			WindowManager wm = (WindowManager) getContext().getSystemService("window");
			wm.removeView(mDragView);
			mDragView.setImageDrawable(null);
			mDragView = null;
		}
		if (mDragBitmap != null) {
			mDragBitmap.recycle();
			mDragBitmap = null;
		}
	}

	/**
	 * 更新拖拽选项位置
	 * **/
	private void dragView(int x, int y) {
		float alpha = 1.0f;
		mWindowParams.alpha = alpha;
		// }
		mWindowParams.y = y - mDragPoint + mCoordOffset;
		mWindowManager.updateViewLayout(mDragView, mWindowParams);
	}

	/**
	 * 得到当前选择项索引
	 * **/
	private int getItemForPosition(int y) {
		int adjustedy = y - mDragPoint - mItemHeightHalf;
		int pos = myPointToPosition(10, adjustedy);
		// System.out.println("pos:"+pos);
		if (pos >= 0) {
			if (pos <= mFirstDragPos) {
				pos += 1;
			}
		} else if (adjustedy < 0) {
			pos = 0;
		}
		return pos;
	}

	/**
	 * 调整滚动间距
	 * **/
	private void adjustScrollBounds(int y) {
		if (y >= mHeight / 3) {
			mUpperBound = mHeight / 3;
		}
		if (y <= mHeight * 2 / 3) {
			mLowerBound = mHeight * 2 / 3;
		}
	}

	/*
	 * Restore size and visibility for all listitems
	 */
	private void unExpandViews(boolean deletion) {
		for (int i = 0;; i++) {
			View v = getChildAt(i);
			if (v == null) {
				if (deletion) {
					// HACK force update of mItemCount
					int position = getFirstVisiblePosition();
					int y = getChildAt(0).getTop();
					setAdapter(getAdapter());
					setSelectionFromTop(position, y);
					// end hack
				}
				layoutChildren(); // force children to be recreated where needed
				v = getChildAt(i);
				if (v == null) {
					break;
				}
			}
			ViewGroup.LayoutParams params = v.getLayoutParams();
			params.height = mItemHeightNormal;
			v.setLayoutParams(params);
			v.setVisibility(View.VISIBLE);
		}
	}

	/*
	 * Adjust visibility and size to make it appear as though an item is being
	 * dragged around and other items are making room for it: If dropping the item
	 * would result in it still being in the same place, then make the dragged
	 * listitem's size normal, but make the item invisible. Otherwise, if the
	 * dragged listitem is still on screen, make it as small as possible and
	 * expand the item below the insert point. If the dragged item is not on
	 * screen, only expand the item below the current insertpoint.
	 */
	private void doExpansion() {
		int childnum = mDragPos - getFirstVisiblePosition();
		if (mDragPos > mFirstDragPos) {
			childnum++;
		}

		View first = getChildAt(mFirstDragPos - getFirstVisiblePosition());

		for (int i = 0;; i++) {
			View vv = getChildAt(i);
			if (vv == null) {
				break;
			}
			int height = mItemHeightNormal;
			int visibility = View.VISIBLE;
			if (vv.equals(first)) {
				// processing the item that is being dragged
				if (mDragPos == mFirstDragPos) {
					// hovering over the original location
					visibility = View.INVISIBLE;
				} else {
					// not hovering over it
					height = 1;
				}
			} else if (i == childnum) {
				if (mDragPos < getCount() - 1) {
					height = mItemHeightExpanded;
				}
			}

			// System.out.println(i+" height:"+height);
			ViewGroup.LayoutParams params = vv.getLayoutParams();
			params.height = height;
			vv.setLayoutParams(params);
			vv.setVisibility(visibility);
		}
	}

	/*
	 * pointToPosition() doesn't consider invisible views, but we need to, so
	 * implement a slightly different version.
	 */
	private int myPointToPosition(int x, int y) {
		Rect frame = mTempRect;
		final int count = getChildCount();
		for (int i = count - 1; i >= 0; i--) {
			final View child = getChildAt(i);
			child.getHitRect(frame);
			if (frame.contains(x, y)) {
				return getFirstVisiblePosition() + i;
			}
		}
		return INVALID_POSITION;
	}

	public interface DropListener {
		void drop(int from, int to);
	}

	public void setDropListener(DropListener onDrop) {
		// TODO Auto-generated method stub
		mDropListener = onDrop;
	}

	// public void setRemoveListener(RemoveListener l) {
	// mRemoveListener = l;
	// }
	public interface RemoveListener {
		void remove(int which);
	}

	View delBtn;
	View content;
	public void showDelBtn(int pos) {
		isDelModel = true;
		ViewGroup item = (ViewGroup) getChildAt(pos - getFirstVisiblePosition());
		final View v = item.findViewById(R.id.deleteStockTv);
		final View content = item.findViewById(R.id.item_content);
		final int w = AppInfo.dip2px(getContext(), 50);
		v.setVisibility(View.VISIBLE);
		Animation ani = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f) {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				super.applyTransformation(interpolatedTime, t);
				int distance = (int) (w * interpolatedTime);
				content.scrollTo(distance, 0);
			}
		};
		ani.setDuration(300);
		v.startAnimation(ani);
		delBtn = v;
		this.content = content;
	}
	public void hideDelBtn(){
		Animation ani = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f) {
			final int w = AppInfo.dip2px(getContext(), 50);
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				super.applyTransformation(interpolatedTime, t);
				int distance = (int) (w * (1-interpolatedTime));
				content.scrollTo(distance, 0);
			}
		};
		ani.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				delBtn.setVisibility(View.GONE);
				isDelModel = false;
			}
		});
		ani.setDuration(300);
		delBtn.startAnimation(ani);
	}
	public void hideDelBtnIm(){
		content.scrollTo(0, 0);
		delBtn.setVisibility(View.GONE);
		isDelModel = false;
		delBtn=null;
		content=null;
	}
	private boolean inRangeOfView(View view, MotionEvent ev) {
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		int x = location[0];
		int y = location[1];
		if (ev.getRawX() < x || ev.getRawX() > (x + view.getWidth()) || ev.getRawY() < y || ev.getRawY() > (y + view.getHeight())) {
			return false;
		}
		return true;
	}
}
