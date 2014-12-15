package com.gome.haoyuangong.layout.self;

import java.util.ArrayList;
import java.util.List;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.activity.InvestGroupActivity;
import com.gome.haoyuangong.activity.InvestGroupDetailActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class InvestGroupItem extends LinearLayout {

	private TextView titleView;
	private TextView dateView;
	private TextView monthProfitView;
	private TextView weekTradeView;
	private Chart chart;		
	private List<CategoryData> dataList;
	public InvestGroupItem(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setOrientation(VERTICAL);
		dataList = new ArrayList<CategoryData>();
		initComponent(context);
		addItems();			
		doLayout();		
	}
	private void addItems(){
		
		dataList.clear();
		dataList.add(new CategoryData());
		dataList.add(new CategoryData());
		for(int i=1;i<11;i++){
			CategoryDatas data = new CategoryDatas();
			data.setDate(20141016);
			data.setValue(i);
			dataList.get(0).getItems().add(data);
			dataList.get(1).getItems().add(data);
		}
		chart.setDataList(dataList);
	}
	private void initComponent(Context context){
		titleView = new TextView(context);
		titleView.setId(1001);
		titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP,Function.px2sp(context, 50));
		titleView.setTextColor(context.getResources().getColor(R.color.font_595959));
		dateView = new TextView(context);
		dateView.setId(1002);
		dateView.setGravity(Gravity.CENTER_VERTICAL);
		dateView.setTextSize(TypedValue.COMPLEX_UNIT_SP,Function.px2sp(context, 36));
		dateView.setTextColor(context.getResources().getColor(R.color.font_b2b2b2));
		monthProfitView = new TextView(context);
		monthProfitView.setGravity(Gravity.CENTER);
		monthProfitView.setTextSize(TypedValue.COMPLEX_UNIT_SP,Function.px2sp(context, 30));
		monthProfitView.setTextColor(Color.RED);
		weekTradeView = new TextView(context);
		weekTradeView.setGravity(Gravity.CENTER);
		weekTradeView.setTextSize(TypedValue.COMPLEX_UNIT_SP,Function.px2sp(context, 30));
		weekTradeView.setTextColor(Color.RED);
		chart = new Chart(context);
		chart.setBackgroundColor(Color.LTGRAY);
	}
	private void doLayout(){
		RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		RelativeLayout titleLayout = new RelativeLayout(getContext());
		titleLayout.setGravity(Gravity.TOP);
		addView(titleLayout,p);
		
		int margin = Function.getFitPx(getContext(), 40);
		
		//标题
//		p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.setMargins(margin, margin, 0, Function.getFitPx(getContext(), 110));
		titleLayout.addView(titleView,rp);
		//日期
//		p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rp.addRule(RelativeLayout.ALIGN_BOTTOM,titleView.getId());
		rp.addRule(RelativeLayout.RIGHT_OF,titleView.getId());
		rp.setMargins(margin/2, 0, 0, Function.getFitPx(getContext(), 110));
		titleLayout.addView(dateView,rp);
		
//		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		LinearLayout flagLayout = new LinearLayout(getContext());
		flagLayout.setOrientation(VERTICAL);
		rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		titleLayout.addView(flagLayout,rp);
		TextView tv = new TextView(getContext());
		tv.setText("组合收益");
		tv.setTextColor(getContext().getResources().getColor(R.color.font_727272));
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(getContext(), 30));
		p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rp.setMargins(0, margin, margin, 0);
		flagLayout.addView(tv,p);
		tv = new TextView(getContext());
		tv.setText("沪深300");
		tv.setTextColor(getContext().getResources().getColor(R.color.font_727272));
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(getContext(), 30));
		p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rp.setMargins(0, margin, margin, 0);
		flagLayout.addView(tv,p);
//		
//		//chart
//		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Function.getFitPx(getContext(), 300));
//		p.setMargins(margin, 0, margin, margin);	
//		chart.setBackgroundColor(Color.WHITE);
//		addView(chart,p);
//		
//		//底部
//		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Function.getFitPx(getContext(), 100));
//		LinearLayout bottomLayout = new LinearLayout(getContext());
//		bottomLayout.setOrientation(HORIZONTAL);
//		addView(bottomLayout,p);
//		//近一月收益
//		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1);
//		LinearLayout monthLayout = new LinearLayout(getContext());
//		monthLayout.setOrientation(VERTICAL);
//		monthLayout.setBackgroundColor(getContext().getResources().getColor(R.color.background_ECECEC));
//		p.setMargins(2*margin, 0, margin, margin);
//		bottomLayout.addView(monthLayout,p);
//		TextView tv = createTextView("近一月收益",30,getContext().getResources().getColor(R.color.font_595959));
//		tv.setGravity(Gravity.CENTER);
//		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1);
//		monthLayout.addView(tv,p);
//		tv = createTextView("50%",30,Color.RED);
//		tv.setGravity(Gravity.CENTER);
//		monthLayout.addView(tv,p);
//		//周交易
//		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1);
//		LinearLayout weekLayout = new LinearLayout(getContext());
//		weekLayout.setOrientation(VERTICAL);
//		weekLayout.setBackgroundColor(getContext().getResources().getColor(R.color.background_ECECEC));
//		p.setMargins(2*margin, 0, margin, margin);
//		bottomLayout.addView(weekLayout,p);
//		tv = createTextView("交易频率",30,getContext().getResources().getColor(R.color.font_595959));
//		tv.setGravity(Gravity.CENTER);
//		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1);
//		weekLayout.addView(tv,p);
//		tv = createTextView("5（W）",30,Color.RED);
//		tv.setGravity(Gravity.CENTER);
//		weekLayout.addView(tv,p);
//		//风险水平
//		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1);
//		LinearLayout levelLayout = new LinearLayout(getContext());
//		levelLayout.setOrientation(VERTICAL);
//		levelLayout.setBackgroundColor(getContext().getResources().getColor(R.color.background_ECECEC));
//		p.setMargins(2*margin, 0, margin, margin);
//		bottomLayout.addView(levelLayout,p);
//		tv = createTextView("风险水平",30,getContext().getResources().getColor(R.color.font_595959));
//		tv.setGravity(Gravity.CENTER);
//		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1);
//		levelLayout.addView(tv,p);
//		tv = createTextView("中",30,Color.RED);
//		tv.setGravity(Gravity.CENTER);
//		levelLayout.addView(tv,p);
	}
	private TextView createTextView(String text,int fontSize,int fontColor){
		TextView tv = new TextView(getContext());
		tv.setText(text);
		tv.setTextColor(fontColor);
		tv.setTextSize(Function.px2sp(getContext(), fontSize));
		return tv;
	}
	public void setDate(String date){
		dateView.setText(date);
	}
	public void setTitle(String title){
		titleView.setText(title);
	}
	public String getTitle() {
		return titleView.getText().toString();
	}
	
	private class CategoryData{
		public CategoryData(){
			items = new ArrayList<CategoryDatas>();
		}
		private String categoryName;
		private List<CategoryDatas> items;
		public String getCategoryName() {
			return categoryName;
		}
		public void setCategoryName(String categoryName) {
			this.categoryName = categoryName;
		}
		public List<CategoryDatas> getItems() {
			return items;
		}
		
	}
	private class CategoryDatas{
		private int date;
		private int value;
		public int getDate() {
			return date;
		}
		public void setDate(int date) {
			this.date = date;
		}
		public int getValue() {
			return value;
		}
		public void setValue(int value) {
			this.value = value;
		}
		
	}
	private class Chart extends View{
		private List<CategoryData> dataList;
		private float minValue = Float.MAX_VALUE;
		private float maxValue = Float.MIN_VALUE;
		private Rect drawRect;
		public Chart(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			dataList = new ArrayList<CategoryData>();
			drawRect = new Rect();
		}
		
		protected void onLayout(boolean changed, int left, int top, int right, int bottom)
	    {
	        if (changed)
	        {
	        	drawRect.set(5, 5, this.getWidth()-5, this.getHeight()-5);
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
			int count = 4;
			Paint paint = new Paint();
			paint.setStyle(Style.STROKE);
			paint.setColor(Color.GRAY);
			canvas.drawRect(drawRect, paint);
			float h = (maxValue - minValue) / count;
			for(int i=1;i<count;i++){
				float y = Function.getYCoordinate(drawRect, minValue+i*h, maxValue, minValue);
				canvas.drawLine(drawRect.left, y, drawRect.right, y, paint);
			}
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
	

}
