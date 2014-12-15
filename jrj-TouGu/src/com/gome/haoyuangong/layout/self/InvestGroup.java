package com.gome.haoyuangong.layout.self;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import com.gome.haoyuangong.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class InvestGroup extends LinearLayout {
	private ListView myListView;
	private Adapter myAdapter;
	private List<Object> items;
	public InvestGroup(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		items = new ArrayList<Object>();
		addItems();
		myListView = new ListView(context);
		myAdapter = new Adapter(context);
		myListView.setAdapter(myAdapter);
		doLayout();
	}
	private void addItems(){
		items.clear();		
		for(int i=1;i<11;i++){
			items.add(i);
		}
	}
	private void doLayout(){
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		addView(myListView,p);
	}
	
	private class InvestGroupItem extends LinearLayout{
		private TextView titleView;
		private TextView monthProfitView;
		private TextView weekTradeView;
		private Chart chart;		
		private List<CategoryData> dataList;
		public InvestGroupItem(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			this.setOrientation(VERTICAL);
			dataList = new ArrayList<InvestGroup.CategoryData>();
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
			titleView.setGravity(Gravity.CENTER_VERTICAL);
			titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP,Function.px2sp(context, 30));
			titleView.setTextColor(Color.BLACK);
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
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Function.getFitPx(getContext(), 60));
			LinearLayout titleLayout = new LinearLayout(getContext());
			titleLayout.setOrientation(HORIZONTAL);
			addView(titleLayout,p);
			
			int margin = Function.getFitPx(getContext(), 20);
			
			//标题
			p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			p.setMargins(margin, 0, margin, margin);
			titleLayout.addView(titleView,p);
			
			//chart
			p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Function.getFitPx(getContext(), 300));
			p.setMargins(margin, 0, margin, margin);	
			chart.setBackgroundColor(Color.WHITE);
			addView(chart,p);
			
			//底部
			p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Function.getFitPx(getContext(), 100));
			LinearLayout bottomLayout = new LinearLayout(getContext());
			bottomLayout.setOrientation(HORIZONTAL);
			addView(bottomLayout,p);
			//近一月收益
			p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1);
			LinearLayout monthLayout = new LinearLayout(getContext());
			monthLayout.setOrientation(VERTICAL);
			monthLayout.setBackgroundColor(getContext().getResources().getColor(R.color.background_ECECEC));
			p.setMargins(2*margin, 0, margin, margin);
			bottomLayout.addView(monthLayout,p);
			TextView tv = createTextView("近一月收益",30,getContext().getResources().getColor(R.color.font_595959));
			tv.setGravity(Gravity.CENTER);
			p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1);
			monthLayout.addView(tv,p);
			tv = createTextView("50%",30,Color.RED);
			tv.setGravity(Gravity.CENTER);
			monthLayout.addView(tv,p);
			//周交易
			p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1);
			LinearLayout weekLayout = new LinearLayout(getContext());
			weekLayout.setOrientation(VERTICAL);
			weekLayout.setBackgroundColor(getContext().getResources().getColor(R.color.background_ECECEC));
			p.setMargins(2*margin, 0, margin, margin);
			bottomLayout.addView(weekLayout,p);
			tv = createTextView("交易频率",30,getContext().getResources().getColor(R.color.font_595959));
			tv.setGravity(Gravity.CENTER);
			p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1);
			weekLayout.addView(tv,p);
			tv = createTextView("5（W）",30,Color.RED);
			tv.setGravity(Gravity.CENTER);
			weekLayout.addView(tv,p);
			//风险水平
			p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1);
			LinearLayout levelLayout = new LinearLayout(getContext());
			levelLayout.setOrientation(VERTICAL);
			levelLayout.setBackgroundColor(getContext().getResources().getColor(R.color.background_ECECEC));
			p.setMargins(2*margin, 0, margin, margin);
			bottomLayout.addView(levelLayout,p);
			tv = createTextView("风险水平",30,getContext().getResources().getColor(R.color.font_595959));
			tv.setGravity(Gravity.CENTER);
			p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1);
			levelLayout.addView(tv,p);
			tv = createTextView("中",30,Color.RED);
			tv.setGravity(Gravity.CENTER);
			levelLayout.addView(tv,p);
		}
		private TextView createTextView(String text,int fontSize,int fontColor){
			TextView tv = new TextView(getContext());
			tv.setText(text);
			tv.setTextColor(fontColor);
			tv.setTextSize(Function.px2sp(getContext(), fontSize));
			return tv;
		}
		private void setTitle(String title){
			titleView.setText(title);
		}
		public TextView getTitleView() {
			return titleView;
		}
		
	}
	private class CategoryData{
		public CategoryData(){
			items = new ArrayList<InvestGroup.CategoryDatas>();
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
			dataList = new ArrayList<InvestGroup.CategoryData>();
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
	class Adapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public Adapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = new InvestGroupItem(getContext());
				viewHolder = new ViewHolder();
				viewHolder.titleView = ((InvestGroupItem)convertView).getTitleView();
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.titleView.setText("品种"+String.valueOf(items.get(position)));
			return convertView;
		}

		class ViewHolder {
			TextView titleView;			
		}
	}

}
