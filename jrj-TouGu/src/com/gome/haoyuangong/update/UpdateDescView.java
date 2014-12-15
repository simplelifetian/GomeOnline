package com.gome.haoyuangong.update;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class UpdateDescView extends View {

    private Bitmap mBm;
    
    private Paint mPaint = new Paint();
    private Matrix mMatrix = new Matrix();
    
    public UpdateDescView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public void setDescBitmap(Bitmap bm){
        this.mBm = bm;
    }
    

    @Override
    protected void onDraw(Canvas canvas) {
        float widthRate = getMeasuredWidth() * 1.0f / mBm.getWidth();

        mMatrix.setScale(widthRate, widthRate);
        canvas.drawBitmap(mBm, mMatrix, mPaint);
    }

    
}
