package com.gome.haoyuangong.views;



import com.gome.haoyuangong.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.text.GetChars;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.CheckBox;

public class MySwitchButton extends CheckBox {
  private Paint mPaint;

  private ViewParent mParent;

  private Bitmap mCheckedBitmap; // check为true时显示的bitmap
  private Bitmap mUnCheckedBitmap;// check为false时显示的bitmap
  private boolean preChecked;

  private int mButtonWidth;// 按钮宽度
  private int mButtonHeight;
  private RectF mSaveLayerRectF;
  VelocityTracker mVelocityTracker;
  int mMaximumFlingVelocity;
  int mMinimumFlingVelocity;
  private float mFirstDownY; // 首次按下的Y

  private float mFirstDownX; // 首次按下的X

  private int mClickTimeout;

  private int mTouchSlop;

  private final int MAX_ALPHA = 255;

  private int mAlpha = MAX_ALPHA;

  private boolean mChecked = false;

  private boolean mBroadcasting;

  private OnCheckedChangeListener mOnCheckedChangeListener;

  private OnCheckedChangeListener mOnCheckedChangeWidgetListener;

  private final float VELOCITY = 350;

  private float mVelocity;

  private final float EXTENDED_OFFSET_Y = 0;

  private float mExtendOffsetY; // Y轴方向扩大的区域,增大点击区域

  public MySwitchButton(Context context, AttributeSet attrs) {
    this(context, attrs, android.R.attr.checkboxStyle);
  }

  public MySwitchButton(Context context) {
    this(context, null);
  }

  public MySwitchButton(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    initView(context);
  }

  private void initView(Context context) {
    mPaint = new Paint();
    mPaint.setColor(Color.WHITE);
    Resources resources = context.getResources();

    // get viewConfiguration
    mClickTimeout = ViewConfiguration.getPressedStateDuration()
        + ViewConfiguration.getTapTimeout();
    mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

    // get Bitmap
    mCheckedBitmap = BitmapFactory.decodeResource(resources,
        R.drawable.icon_check_checked);
    mUnCheckedBitmap = BitmapFactory.decodeResource(resources,
        R.drawable.icon_check_normal);

    mButtonWidth = mCheckedBitmap.getWidth();
    preChecked = false;

    mSaveLayerRectF = new RectF(0, mExtendOffsetY, mCheckedBitmap.getWidth(),
        mCheckedBitmap.getHeight() + mExtendOffsetY);
    final float density = getResources().getDisplayMetrics().density;
    mMaximumFlingVelocity = ViewConfiguration.getMaximumFlingVelocity();
    mMinimumFlingVelocity = ViewConfiguration.getMinimumFlingVelocity();
    mVelocity = (int) (VELOCITY * density + 0.5f);
    mExtendOffsetY = (int) (EXTENDED_OFFSET_Y * density + 0.5f);
    mButtonHeight = (int) (mCheckedBitmap.getHeight() + mExtendOffsetY * 2);

  }

  @Override
  public void setEnabled(boolean enabled) {
    mAlpha = enabled ? MAX_ALPHA : MAX_ALPHA / 2;
    super.setEnabled(enabled);
  }

  public boolean isChecked() {
    return mChecked;
  }

  public void toggle() {
    setChecked(!mChecked);
  }

  /**
   * 内部调用此方法设置checked状态，此方法会延迟执行各种回调函数，保证动画的流畅度
   * 
   * @param checked
   */
  private void setCheckedDelayed(final boolean checked) {
    this.postDelayed(new Runnable() {

      @Override
      public void run() {
        setChecked(checked);
      }
    }, 10);
  }

  /**
   * <p>
   * Changes the checked state of this button.
   * </p>
   * 
   * @param checked
   *          true to check the button, false to uncheck it
   */
  public void setChecked(boolean checked) {

    if (mChecked != checked) {
      mChecked = checked;
      preChecked = checked;
      invalidate();

      // Avoid infinite recursions if setChecked() is called from a
      // listener
      if (mBroadcasting) {
        return;
      }

      mBroadcasting = true;
      if (mOnCheckedChangeListener != null) {
        mOnCheckedChangeListener
            .onCheckedChanged(MySwitchButton.this, mChecked);
      }
      if (mOnCheckedChangeWidgetListener != null) {
        mOnCheckedChangeWidgetListener.onCheckedChanged(MySwitchButton.this,
            mChecked);
      }

      mBroadcasting = false;
    }
  }
  public void setCheckedWithNoListener(boolean checked) {
  	
  	if (mChecked != checked) {
  		mChecked = checked;
  		preChecked = checked;
  		invalidate();
  	}
  }

  /**
   * Register a callback to be invoked when the checked state of this button
   * changes.
   * 
   * @param listener
   *          the callback to call on checked state change
   */
  public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
    mOnCheckedChangeListener = listener;
  }

  /**
   * Register a callback to be invoked when the checked state of this button
   * changes. This callback is used for internal purpose only.
   * 
   * @param listener
   *          the callback to call on checked state change
   * @hide
   */
  void setOnCheckedChangeWidgetListener(OnCheckedChangeListener listener) {
    mOnCheckedChangeWidgetListener = listener;
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    int action = event.getAction();
    float x = event.getX();
    float y = event.getY();
    float deltaX = Math.abs(x - mFirstDownX);
    float deltaY = Math.abs(y - mFirstDownY);
    if (mVelocityTracker == null) {
      mVelocityTracker = VelocityTracker.obtain();
    }
    mVelocityTracker.addMovement(event);
    switch (action) {
    case MotionEvent.ACTION_DOWN:
      attemptClaimDrag();
      mFirstDownX = x;
      mFirstDownY = y;
      break;
    case MotionEvent.ACTION_MOVE:
      float time = event.getEventTime() - event.getDownTime();
      int moveLenth = (int) (event.getX() - mFirstDownX);
      if (moveLenth > 0) {
        if (moveLenth > mButtonWidth >> 1) {
          preChecked = true;
        } else {
          preChecked = isChecked();
        }
      } else {
        if (moveLenth < (-mButtonWidth >> 1)) {
          preChecked = false;
        } else {
          preChecked = isChecked();
        }
      }
      break;
    case MotionEvent.ACTION_UP:
      time = event.getEventTime() - event.getDownTime();
      if (deltaY < mTouchSlop && deltaX < mTouchSlop && time < mClickTimeout) {
        preChecked = !preChecked;
      } else {
        final VelocityTracker velocityTracker = mVelocityTracker;
        final int pointerId = event.getPointerId(0);
        velocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
        final float velocityY = velocityTracker.getYVelocity(pointerId);
        final float velocityX = velocityTracker.getXVelocity(pointerId);

        if ((Math.abs(velocityX) > mMinimumFlingVelocity)) {
          if (velocityX > 0) {
            preChecked = true;
          } else {
            preChecked = false;
          }

        }
        if (mVelocityTracker != null) {
          // This may have been cleared when we called out to the
          // application above.
          mVelocityTracker.recycle();
          mVelocityTracker = null;
        }
      }
      setCheckedDelayed(preChecked);
      break;
    }

    invalidate();
    return isEnabled();
  }

  /**
   * Tries to claim the user's drag motion, and requests disallowing any
   * ancestors from stealing events in the drag.
   */
  private void attemptClaimDrag() {
    mParent = getParent();
    if (mParent != null) {
      mParent.requestDisallowInterceptTouchEvent(true);
    }
  }

  @Override
  protected void onDraw(Canvas canvas) {
    canvas.saveLayerAlpha(mSaveLayerRectF, mAlpha, Canvas.MATRIX_SAVE_FLAG
        | Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
        | Canvas.FULL_COLOR_LAYER_SAVE_FLAG | Canvas.CLIP_TO_LAYER_SAVE_FLAG);

    // 绘制边框
    if (preChecked) {
      canvas.drawBitmap(mCheckedBitmap, 0, mExtendOffsetY, mPaint);
    } else {
      canvas.drawBitmap(mUnCheckedBitmap, 0, mExtendOffsetY, mPaint);
    }

    canvas.restore();
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    setMeasuredDimension(mButtonWidth, mButtonHeight);
  }

}
