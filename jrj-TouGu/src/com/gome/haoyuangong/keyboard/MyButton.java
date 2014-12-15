package com.gome.haoyuangong.keyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.TextView;

/**
 * 自定义键盘自定义按钮，重新绘制按钮上面字体
 * @author ghb
 * **/
public class MyButton extends TextView {

  public MyButton(Context context, AttributeSet attrs) {
    super(context, attrs);
    setGravity(Gravity.CENTER);
   }

   public MyButton(Context context) {
    super(context);
    setGravity(Gravity.CENTER);
   }

   private Paint mPaint = null;
   private String mText;
   private int mX, mY;

   /**
    * 设置按钮显示数据信息，内容，颜色，布局
    * **/
   public void onSetText(String text, int nLeft, int nBottom, int nTextSize,
     int nTextColor) {
    mPaint = new Paint();
    mPaint.setAntiAlias(true);
    mPaint.setTextSize(nTextSize);
    mPaint.setColor(nTextColor);
    mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
//    mPaint.setTypeface(Typeface.DEFAULT_BOLD);  
    this.mText = text;
    this.mX = nLeft;
    this.mY = nBottom;
   }
   
   public String getMyText(){
     return mText;
   }

   private int mDownBmpId, mUpBmpId;

   /**
    * 设置按下背景以及抬手显示背景
    * **/
   public void onSetBmp(int nDownID, int nUpID) {
    this.mDownBmpId = nDownID;
    this.mUpBmpId = nUpID;
    super.setBackgroundResource(mUpBmpId);
   }

   @Override
   public void onDraw(Canvas canvas) {
    if (mPaint != null)
     canvas.drawText(mText, mX, mY+2, mPaint);
    super.onDraw(canvas);
   }

   @Override
   public boolean onTouchEvent(MotionEvent event) {
    if (event.getAction() == MotionEvent.ACTION_DOWN) {
     super.setBackgroundResource(mDownBmpId);
    } else if (event.getAction() == MotionEvent.ACTION_UP) {
     super.setBackgroundResource(mUpBmpId);
    }
    return super.onTouchEvent(event);
   }
  }


