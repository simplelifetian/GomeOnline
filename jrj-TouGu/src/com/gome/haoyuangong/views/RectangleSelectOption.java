package com.gome.haoyuangong.views;

import java.util.ArrayList;
import java.util.List;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.activity.BaseActivity;
import com.gome.haoyuangong.layout.self.Function;

import android.R.integer;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RectangleSelectOption extends LinearLayout {
	Context context;
	int itemWidth;
	List<String> optionItems;
	List<SkillItem> selectedItems;
	List<SkillItem> optionViews;
	int fontSize=40;
	int fontColor;
	int backColor;
	int maxSelectCount = 3;
	int selectedBackColor;
	int selectedFontColor;
	int titleFontColor;
	String title;
	int titleBackColor;
	int screeWidth;
	boolean canSelectItem = true;
	TextView titleView;
	boolean autoArrange=false;
	int pad;
	public RectangleSelectOption(Context context) {
		super(context);
		this.context = context;
		if (context instanceof BaseActivity){
			screeWidth = ((BaseActivity)context).getScreenW();
			itemWidth = screeWidth/3-Function.getFitPx(context, 60);
		}
		pad = Function.getFitPx(context, 6);
		optionItems = new ArrayList<String>();
		selectedItems = new ArrayList<SkillItem>();
		optionViews = new ArrayList<SkillItem>();
		fontColor = getResources().getColor(R.color.font_595959);
		backColor = Color.WHITE;
		selectedBackColor = getResources().getColor(R.color.font_4c87c6);
		selectedFontColor = Color.WHITE;
		titleFontColor = getResources().getColor(R.color.font_727272);
		titleBackColor = getResources().getColor(R.color.background_f5f5f5);
		titleView = createTextView(title);
	}

	public boolean isAutoArrange() {
		return autoArrange;
	}

	public void setAutoArrange(boolean autoArrange) {
		this.autoArrange = autoArrange;
		itemWidth = LayoutParams.WRAP_CONTENT;
	}

	public boolean isCanSelectItem() {
		return canSelectItem;
	}

	public void setCanSelectItem(boolean canSelectItem) {
		this.canSelectItem = canSelectItem;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public int getFontColor() {
		return fontColor;
	}

	public void setFontColor(int fontColor) {
		this.fontColor = fontColor;
	}

	public int getBackColor() {
		return backColor;
	}

	public void setBackColor(int backColor) {
		this.backColor = backColor;
	}

	public int getMaxSelectCount() {
		return maxSelectCount;
	}

	public void setMaxSelectCount(int maxSelectCount) {
		this.maxSelectCount = maxSelectCount;
	}

	public int getSelectedBackColor() {
		return selectedBackColor;
	}

	public void setSelectedBackColor(int selectedBackColor) {
		this.selectedBackColor = selectedBackColor;
	}

	public int getSelectedFontColor() {
		return selectedFontColor;
	}

	public void setSelectedFontColor(int selectedFontColor) {
		this.selectedFontColor = selectedFontColor;
	}

	public int getTitleFontColor() {
		return titleFontColor;
	}

	public void setTitleFontColor(int titleFontColor) {
		this.titleFontColor = titleFontColor;
	}

	public int getTitleBackColor() {
		return titleBackColor;
	}

	public void setTitleBackColor(int titleBackColor) {
		this.titleBackColor = titleBackColor;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		titleView.setText(title);
	}
	private int getSelectedCount(){
		return selectedItems.size();
	}

	public void setOptionItems(List<String> items) {
		optionItems.clear();
		optionItems.addAll(items);
		optionViews.clear();
		for(String s:optionItems){
			SkillItem item = new SkillItem(getContext());
			item.setContent(s);
			optionViews.add(item);
		}
		getView();
	}
	private TextView createTextView(String text){
		TextView tvTextView = new TextView(context);
		tvTextView.setText(text);
		tvTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(context, fontSize));
		tvTextView.setGravity(Gravity.CENTER_VERTICAL);
		tvTextView.setTextColor(titleFontColor);
		tvTextView.setBackgroundColor(titleBackColor);
		tvTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,Function.px2sp(context, 40));
		tvTextView.setPadding(Function.getFitPx(context, 20), 0, 0, 0);
		return tvTextView;
	}
	public void hideTitle() {
		if (titleView != null)
			titleView.setVisibility(View.GONE);
	}
	private void clearView() {	
		for(SkillItem item:optionViews){
			if (item.getParent() != null)
				((LinearLayout)item.getParent()).removeAllViews();
		}
		removeAllViews();
		if (titleView.getParent() != null)
			((LinearLayout)titleView.getParent()).removeAllViews();
	}
	private LinearLayout getView(){	
		clearView();
		LinearLayout layout = new LinearLayout(context);
		layout.setBackgroundColor(backColor);		
		layout.setOrientation(LinearLayout.VERTICAL);
		
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Function.getFitPx(context, 120));		
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Function.getFitPx(context, 60));
		layout.addView(titleView,p);	
		
		if (!autoArrange)
			arrangeFix(layout);
		else
			arrangeAuto(layout);
		
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);	
		addView(layout,p);
		return layout;
	}
	private void arrangeAuto(LinearLayout layout){
		int w=0;
		int margin = Function.getFitPx(context, 40);
		LinearLayout.LayoutParams  p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);		
		LinearLayout itemLayout = new LinearLayout(context);
		layout.addView(itemLayout,p);
		for(int i=0;i<optionItems.size();i++){					
			itemLayout.setOrientation(LinearLayout.HORIZONTAL);
			p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);	
//			p.setMargins(margin, 0, margin, margin);
			SkillItem item = optionViews.get(i);
			item.setContent(optionItems.get(i));			
			item.setLayoutParams(p);
			item.measure(0, 0);
			int width = item.getMeasuredWidth()+15;
			if (w +width+40 < screeWidth){
				p = new LinearLayout.LayoutParams(width, LayoutParams.WRAP_CONTENT);	
				if (w== 0)
					p.setMargins(margin, 0, margin, margin);
				else
					p.setMargins(0, 0, margin, margin);
				w += width+40;
				itemLayout.addView(item,p);
			}
			else {
				w = width+40;;
				itemLayout = new LinearLayout(context);
				p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);		
				layout.addView(itemLayout,p);
				p = new LinearLayout.LayoutParams(width, LayoutParams.WRAP_CONTENT);	
				p.setMargins(margin, 0, margin, margin);
				item.setLayoutParams(p);
				itemLayout.addView(item,p);
			}
		}
	}
	private void arrangeFix(LinearLayout layout){
		for(int i=0;i<optionItems.size();i+=3){
			LinearLayout.LayoutParams  p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Function.getFitPx(context, 120));	
			if (optionItems.size() - i <= 3)
				p.setMargins(Function.getFitPx(context, 40), Function.getFitPx(context, 20), Function.getFitPx(context, 40), Function.getFitPx(context, 40));
			else {
				p.setMargins(Function.getFitPx(context, 40), Function.getFitPx(context, 20), Function.getFitPx(context, 40), 0);
			}
			LinearLayout topLayout = new LinearLayout(context);	
			topLayout.setOrientation(LinearLayout.HORIZONTAL);
			layout.addView(topLayout,p);			
			
			p = new LinearLayout.LayoutParams(itemWidth, Function.getFitPx(context, 120));
			SkillItem item = optionViews.get(i);		
			item.setContent(optionItems.get(i));
			p.setMargins(0, 0, Function.getFitPx(context, 40), 0);
			topLayout.addView(item,p);
			
			LinearLayout itemLayout = new LinearLayout(context);
			
			if (i+1 <= optionItems.size() - 1){
				p = new LinearLayout.LayoutParams(itemWidth, Function.getFitPx(context, 120));
				item = optionViews.get(i+1);
				item.setContent(optionItems.get(i+1));
				p.setMargins(0, 0, Function.getFitPx(context, 40), 0);
				topLayout.addView(item,p);
			}
			
			if (i+2 <= optionItems.size() - 1){
				p = new LinearLayout.LayoutParams(itemWidth, Function.getFitPx(context, 120));
				item = optionViews.get(i+2);
				item.setContent(optionItems.get(i+2));
				p.setMargins(0, 0, Function.getFitPx(context, 40), 0);
				topLayout.addView(item,p);
			}
		}
	}
	public void setReadOnly(int index,boolean readOnly) {
		if (index <0 || index > optionViews.size() - 1)
			return;
		SkillItem item = optionViews.get(index);
		item.setReadOnly(readOnly);
	}
	public SkillItem getItem(int index){
		if (index <0 || index > optionViews.size() - 1)
			return null;
		return optionViews.get(index);
	}
	public boolean addItem(String content){
		if (optionItems.indexOf(content) != -1){
			Function.showToask(getContext(), String.format("[%s]已经存在！", content));
			return false;
		}
		optionItems.add(content);
		SkillItem item = new SkillItem(getContext());
		item.setContent(content);
		item.setSelected(false);
		optionViews.add(item);		
		getView();
		return true;
	}
	private void remove(SkillItem item){
		optionItems.remove(item.getContent());
		optionViews.remove(item);
		selectedItems.remove(item);	
		getView();
	}
	public void addItem(String content,boolean canDel){
		if (!addItem(content))
			return;
		if (canDel)
			optionViews.get(optionViews.size()-1).showCloseView();		
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder stringBuilder = new StringBuilder();
		for(SkillItem item:selectedItems){
			stringBuilder.append(item.getContent());
			stringBuilder.append(",");
		}
		return stringBuilder.toString();
	}
	
	public class SkillItem extends RelativeLayout{
		private TextView contentTextView;
		private boolean selected;
		private boolean readOnly=false;
		private ImageView closeView;
		public SkillItem(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			this.setBackgroundColor(Color.WHITE);
			contentTextView = new TextView(context);
			contentTextView.setBackgroundColor(Color.WHITE);
			contentTextView.setBackgroundResource(R.drawable.roundcorner_answeredit);
			closeView  = new ImageView(context);
			closeView.setScaleType(ScaleType.FIT_XY);
			closeView.setImageDrawable(getResources().getDrawable(R.drawable.round_delete));
			closeView.setVisibility(View.GONE);
			closeView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					remove(SkillItem.this);
				}
			});
			doLayout();
			this.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {	
					// TODO Auto-generated method stub
					if (!canSelectItem){
						selectedItems.add(SkillItem.this);
						return;
					}
					if (contentTextView.getText().toString().equals("+")){
						return;
					}
					if (readOnly)
						return;
					
					if (!selected){						
						int count = getSelectedCount();
						int maxCount = maxSelectCount;
						if (count == maxCount){	
							Toast.makeText(getContext(), String.format("最多只能选择%d项", maxSelectCount), Toast.LENGTH_SHORT).show();
							return;
						}
						setSelected(true);
						selectedItems.add(SkillItem.this);
					}
					else{
						setSelected(false);
						selectedItems.remove(SkillItem.this);
					}
				}
			});
		}
		private void doLayout(){
			RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(itemWidth, Function.getFitPx(getContext(), 120));
			LinearLayout itemLayout = new LinearLayout(getContext());
//			itemLayout.setBackgroundColor(getResources().getColor(R.color.font_cccccc));
			rp.setMargins(0, Function.getFitPx(getContext(), 20), Function.getFitPx(getContext(), 20), 0);
			itemLayout.setLayoutParams(rp);
			addView(itemLayout,rp);
			
			rp = new RelativeLayout.LayoutParams(Function.getFitPx(getContext(), 40), Function.getFitPx(getContext(), 40));
			rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			closeView.setLayoutParams(rp);
			addView(closeView,rp);
		
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			p.setMargins(1, 1, 1, 1);
			contentTextView.setLayoutParams(p);
			itemLayout.addView(contentTextView,p);			
		}
		
		public boolean isReadOnly() {
			return readOnly;
		}
		public void setReadOnly(boolean readOnly) {
			this.readOnly = readOnly;
			if (readOnly){
				hideCloseView();
				selectedItems.remove(SkillItem.this);
			}
		}
		private void hideCloseView() {
			if (readOnly)
				closeView.setVisibility(View.GONE);
		}
		private void showCloseView() {
			closeView.setVisibility(View.VISIBLE);
		}
		public String getContent(){
			return contentTextView.getText().toString();
		}
		public boolean isSelected() {
			return selected;
		}
		public void setSelected(boolean selected) {
			this.selected = selected;
			if (this.selected){
				GradientDrawable gradientDrawable = (GradientDrawable)contentTextView.getBackground();
				gradientDrawable.setColor(selectedBackColor);
//				contentTextView.setBackgroundColor(selectedBackColor);
				contentTextView.setTextColor(selectedFontColor);
			}
			else{
				GradientDrawable gradientDrawable = (GradientDrawable)contentTextView.getBackground();
				gradientDrawable.setColor(Color.WHITE);
//				contentTextView.setBackgroundColor(Color.WHITE);
				contentTextView.setTextColor(fontColor);
			}
		}
		protected void setContent(String text){
			contentTextView.setText(text);
			
			if (text.equals("+")){
				contentTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(getContext(), 90));
				contentTextView.setTextColor(getResources().getColor(R.color.font_cccccc));
			}
			else{
				contentTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(getContext(), 50));
				contentTextView.setTextColor(fontColor);
			}
			
			contentTextView.setGravity(Gravity.CENTER);
		}
		
	}

}
