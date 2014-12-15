package com.gome.haoyuangong.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.TextView;

public class ClickTextView extends TextView{
  private int mAlpha = 255;
  private RectF mSaveLayerRectF;
  public ClickTextView(Context context, AttributeSet attrs) {
	super(context, attrs);
	// TODO Auto-generated constructor stub
  }
  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    mSaveLayerRectF = new RectF(0, 0, w, h);
  }
  @Override
  public void setPressed(boolean pressed) {
    // TODO Auto-generated method stub
    super.setPressed(pressed);
    invalidate();
  }
  @Override
	public void draw(Canvas canvas) {
    // TODO Auto-generated method stub
//	getBackground().draw(canvas);
	if(isPressed()){
	  mAlpha = 50;
	}else{
	  mAlpha = 255;
	}
	canvas.saveLayerAlpha(mSaveLayerRectF, mAlpha, Canvas.MATRIX_SAVE_FLAG
        | Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
        | Canvas.FULL_COLOR_LAYER_SAVE_FLAG | Canvas.CLIP_TO_LAYER_SAVE_FLAG);
    super.draw(canvas);
    canvas.restore();
  }

  @Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		String text = getText().toString();
		if((text == null || text.length() == 0) && getBackground() != null){
			int size = getMeasuredWidth() > getMeasuredHeight() ? getMeasuredWidth() : getMeasuredHeight();
			super.onMeasure(MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY));
		}
	}
}
