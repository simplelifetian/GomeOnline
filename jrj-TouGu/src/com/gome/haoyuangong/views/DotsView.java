
package com.gome.haoyuangong.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DotsView extends View {

    private int mDotNum = 5;
    private Paint mPaint = new Paint();
    private int mGap;
    private int mSelectedIndex = 0;
    private Bitmap mNormalDotBmp;
    private Bitmap mSelectedDotBmp;
    private int mDotWidth;
    private int mHeight;

    public DotsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(int dotNum, int selectedIndex, int gap, int dotNormal, int dotSelected) {
        mDotNum = dotNum;
        mSelectedIndex = selectedIndex;
        float density = getContext().getResources().getDisplayMetrics().density;
        mGap = (int) (gap * density + 0.5f);
        mNormalDotBmp = BitmapFactory.decodeResource(getContext().getResources(),
                dotNormal);
        mSelectedDotBmp = BitmapFactory.decodeResource(getContext().getResources(),
                dotSelected);
        mDotWidth = mNormalDotBmp.getWidth();
        mHeight = mNormalDotBmp.getHeight();
    }

    public void setSelected(int index) {
        mSelectedIndex = index;
        invalidate();
    }
    
    public void change(int dotNum){
        mDotNum = dotNum;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int offset = (getMeasuredWidth() - mDotNum * mDotWidth - (mDotNum - 1) * mGap) >> 1;
        for (int i = 0; i < mDotNum; i++) {
            Bitmap bitmap;
            if (mSelectedIndex == i) {
                bitmap = mSelectedDotBmp;
            } else {
                bitmap = mNormalDotBmp;
            }
            canvas.drawBitmap(bitmap, offset + mGap * i + mDotWidth * i, 0, mPaint);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mNormalDotBmp != null) {
            mNormalDotBmp.recycle();
        }
        if (mSelectedDotBmp != null) {
            mSelectedDotBmp.recycle();
        }
    }
}
