package com.gome.haoyuangong.layout.self;

import java.util.ArrayList;
import java.util.List;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.layout.self.InvestGroupData.CategoryData;
import com.gome.haoyuangong.layout.self.InvestGroupData.CategoryDatas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.view.View;

public class InvestGroupChart extends View {

	private List<CategoryData> dataList;
	private float minValue = Float.MAX_VALUE;
	private float maxValue = Float.MIN_VALUE;
	private Rect drawRect;
	public InvestGroupChart(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		dataList = new ArrayList<CategoryData>();
		drawRect = new Rect();
	}
	
	protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        if (changed)
        {
        	drawRect.set(Function.getFitPx(getContext(), 140), 0, this.getWidth()-1, this.getHeight()-1);
			invalidate();
        }
    }

	public List<CategoryData> getDataList() {
		return dataList;
	}

	public void setDataList(List<CategoryData> dataList) {
		this.dataList = dataList;
		getMinMaxValue();
		invalidate();
	}

	protected void onDraw(Canvas canvas){
		Paint paint = new Paint();
		paint.setStyle(Style.STROKE);
		paint.setAntiAlias(true);
		paint.setColor(0xffe9e9e9);
		paint.setTextSize(Function.getFitPx(getContext(), 30));
		FontMetrics fm = paint.getFontMetrics();
//		canvas.drawRect(drawRect, paint);
		canvas.drawLine(drawRect.left, drawRect.top, drawRect.right, drawRect.top, paint);
		canvas.drawLine(drawRect.left, drawRect.bottom, drawRect.right, drawRect.bottom, paint);
		float h = (maxValue - minValue) / 2;
		float y = Function.getYCoordinate(drawRect, minValue+h, maxValue, minValue);
		paint.setColor(0xffbbbbbc);
		canvas.drawLine(drawRect.left, y, drawRect.right, y, paint);
		paint.setTextAlign(Align.RIGHT);
		int baseline = (int) (drawRect.top + (drawRect.bottom - drawRect.top - fm.bottom + fm.top) / 2 - fm.top); 
		canvas.drawText("0%", drawRect.left-Function.getFitPx(getContext(), 20), baseline, paint);
		paint.setColor(getContext().getResources().getColor(R.color.font_de3031));			
		canvas.drawText("3.4%", drawRect.left-Function.getFitPx(getContext(), 20), drawRect.top-(fm.top-fm.bottom)/2 + 2, paint);
		paint.setColor(getContext().getResources().getColor(R.color.font_32a632));			
		canvas.drawText("-3.4%", drawRect.left-Function.getFitPx(getContext(), 20), drawRect.bottom, paint);
	}
	
	private void getMinMaxValue(){
		for(CategoryData data:dataList){
			for(CategoryDatas datas:data.getItems()){
				if (datas.getValue() > maxValue)
					maxValue = datas.getValue();
				if (datas.getValue() < minValue)
					minValue = datas.getValue();
			}
		}
	}

}
