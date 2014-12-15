package com.gome.haoyuangong.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.layout.self.Function;
import com.gome.haoyuangong.layout.self.SelfInfo.BusinessType;
import com.gome.haoyuangong.views.KeyboardLayout;
import com.gome.haoyuangong.views.KeyboardLayout.onKybdsChangeListener;

public class SelectSkillActivity extends BaseActivity {
	BusinessType businessType;
	List<String> items;
	List<SkillItem> selectedItems;
	private String skillName;
	LinearLayout inputLayout;
	KeyboardLayout keyLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		items = new ArrayList<String>();
		selectedItems = new ArrayList<SelectSkillActivity.SkillItem>();
		Intent intent = getIntent();
		skillName = intent.getStringExtra("skillname");
		if (intent.getIntExtra("businessType", BusinessType.btSkill.ordinal()) == BusinessType.btSkill.ordinal()){
			businessType = BusinessType.btSkill;	
			items.add("货币基金");
			items.add("货币基金");
			items.add("货币基金");
			items.add("货币基金");
			items.add("货币基金");			
			items.add("+");
			setTitle("能力标签");
		}
		else{
			businessType = BusinessType.btExpertArea;
			items.add("A股");
			items.add("美股");
			items.add("港股");
			items.add("基金");
			items.add("贵金属");
			items.add("其他领域");
			setTitle("专业技能");
		}
		keyLayout = new KeyboardLayout(this);
		content.removeAllViews();
		content.addView(getView());
		inputLayout.setVisibility(View.GONE);
	}
	private KeyboardLayout getView(){
		keyLayout.removeAllViews();
		inputLayout = new LinearLayout(this);
		inputLayout.setGravity(Gravity.CENTER_VERTICAL);
		inputLayout.setOrientation(LinearLayout.HORIZONTAL);
		inputLayout.setBackgroundColor(Color.WHITE);
		LinearLayout layout = new LinearLayout(this);
		
		layout.setOrientation(LinearLayout.VERTICAL);
		for(int i=0;i<items.size();i+=3){
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Function.getFitPx(this, 120));	
			p.setMargins(Function.getFitPx(this, 40), Function.getFitPx(this, 40), Function.getFitPx(this, 40), 0);
			LinearLayout topLayout = new LinearLayout(this);	
			topLayout.setOrientation(LinearLayout.HORIZONTAL);
			layout.addView(topLayout,p);
			
			p = new LinearLayout.LayoutParams(Function.getFitPx(this, 292), Function.getFitPx(this, 120));
			SkillItem item = new SkillItem(this);			
			item.setContent(items.get(i));
			p.setMargins(0, 0, Function.getFitPx(this, 60), 0);
			topLayout.addView(item,p);
			
			LinearLayout itemLayout = new LinearLayout(this);
			
			if (i+1 <= items.size() - 1){
				p = new LinearLayout.LayoutParams(Function.getFitPx(this, 292), Function.getFitPx(this, 120));
				item = new SkillItem(this);
				item.setContent(items.get(i+1));
				p.setMargins(0, 0, Function.getFitPx(this, 60), 0);
				topLayout.addView(item,p);
			}
			
			if (i+2 <= items.size() - 1){
				p = new LinearLayout.LayoutParams(Function.getFitPx(this, 292), Function.getFitPx(this, 120));
				item = new SkillItem(this);
				item.setContent(items.get(i+2));
				p.setMargins(0, 0, Function.getFitPx(this, 60), 0);
				topLayout.addView(item,p);
			}
		}		
		
//		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Function.getFitPx(this, 140));			
//		p.setMargins(0, Function.getFitPx(this, 120), 0, 0);
//		layout.addView(inputLayout,p);				
		
		final EditText edit = new EditText(this);
		edit.setBackgroundResource(R.drawable.roundcorner_edit_background);
//		edit.setBackgroundColor(Color.WHITE);
		edit.setFocusable(true);
		edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
		edit.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(this, 46));
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT,1);			
		p.setMargins(Function.getFitPx(this, 40), 10, 10, 10);
		inputLayout.addView(edit,p);
		
		TextView tv = new TextView(this);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(this, 46));
		tv.setGravity(Gravity.CENTER);
		tv.setText("发送");
		tv.setTextColor(getResources().getColor(R.color.font_4c87c6));
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(edit.getText().toString()))
					return;
				items.add(items.size()-1, edit.getText().toString());
				content.removeAllViews();
				content.addView(getView());
//				inputLayout.setVisibility(View.GONE);
				hideSoftInput();
			}
		});
		p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);			
		p.setMargins(Function.getFitPx(this, 40), 0, Function.getFitPx(this, 40), 0);
		inputLayout.addView(tv,p);
		
		ScrollView scrollView = new ScrollView(this);
		scrollView.setFillViewport(true);
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);	
		scrollView.addView(layout,p);
		
		
		RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		keyLayout.addView(scrollView,rp);
		keyLayout.setOnkbdStateListener(new onKybdsChangeListener() {
            
            public void onKeyBoardStateChange(int state) {
                    switch (state) {
                    case KeyboardLayout.KEYBOARD_STATE_HIDE:
                    		
                    	inputLayout.setVisibility(View.GONE);
                        break;
                    case KeyboardLayout.KEYBOARD_STATE_SHOW:

                    		
                            break;
                    }
            }
		});
		
		
		
		rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, Function.getFitPx(this, 140));
		rp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		keyLayout.addView(inputLayout,rp);	
		
		return keyLayout;
	}
	private String getSelContactString(){
		StringBuilder sb = new StringBuilder();
		
		for(SkillItem item:selectedItems){
			if (sb.length() > 0)
				sb.append(" ");
			sb.append(item.getContent());
			
		}
		return sb.toString();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left1:
			Intent newIntent = new Intent();
			newIntent.putExtra("returnvalue", getSelContactString());
			setResult(RESULT_OK, newIntent);
			finish();
			break;
		}
	}
	
	private class SkillItem extends LinearLayout{
		private TextView contentTextView;
		private boolean selected;
		public SkillItem(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			this.setBackgroundColor(getResources().getColor(R.color.font_cccccc));
			contentTextView = new TextView(context);
			contentTextView.setBackgroundColor(Color.WHITE);
			doLayout();
			this.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (contentTextView.getText().toString().equals("+")){
						inputLayout.setVisibility(VISIBLE);
						inputLayout.getChildAt(0).setFocusable(true);
						inputLayout.getChildAt(0).requestFocus();
						showSoftInput(inputLayout.getChildAt(0));
						return;
					}
					inputLayout.setVisibility(View.GONE);
					if (selectedItems.size() == 3)
						return;
					setSelected(!selected);
					if (selected)
						selectedItems.add(SkillItem.this);
					else
						selectedItems.remove(SkillItem.this);
				}
			});
		}
		private void doLayout(){
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(Function.getFitPx(getContext(), 292), Function.getFitPx(getContext(), 120));
			LinearLayout itemLayout = new LinearLayout(getContext());
			p.setMargins(0, 0, Function.getFitPx(getContext(), 60), 0);
			itemLayout.setBackgroundColor(getResources().getColor(R.color.font_cccccc));
			addView(itemLayout,p);
			
			p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			p.setMargins(1, 1, 1, 1);
			itemLayout.addView(contentTextView,p);
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
				contentTextView.setBackgroundColor(getResources().getColor(R.color.font_4c87c6));
				contentTextView.setTextColor(Color.WHITE);
			}
			else{
				contentTextView.setBackgroundColor(Color.WHITE);
				contentTextView.setTextColor(getResources().getColor(R.color.font_595959));
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
				contentTextView.setTextColor(getResources().getColor(R.color.font_595959));
			}
			
			contentTextView.setGravity(Gravity.CENTER);
//			if (text.equals(skillName)){
//				contentTextView.setBackgroundColor(getResources().getColor(R.color.font_4c87c6));
//				contentTextView.setTextColor(Color.WHITE);
//			}
		}
		
	}

}
