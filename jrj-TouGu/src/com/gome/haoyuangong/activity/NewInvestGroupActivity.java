package com.gome.haoyuangong.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.layout.self.Function;

public class NewInvestGroupActivity extends BaseActivity {
	private static final String TAG = NewInvestGroupActivity.class.getName();
	LayoutItem item;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("创建组合");
		content.removeAllViews();
		item = new LayoutItem(this);
		content.addView(item);
		titleRight2.setText("完成");
	    titleRight2.setTextSize(this.getResources().getDimension(R.dimen.text_size_8));
	    titleRight2.setGravity(Gravity.CENTER);
	    titleRight2.setOnClickListener(this);
	}
	@Override
	public void onClick(View v){
		super.onClick(v);
		switch(v.getId()){
			case R.id.title_right2:		
				Intent newIntent = new Intent();
				newIntent.putExtra("title", item.titleView.getText().toString());
				newIntent.putExtra("content", item.contentView.getText().toString());
				setResult(RESULT_OK, newIntent);
				finish();
				break;
		}
	}
	private class LayoutItem extends LinearLayout{
		EditText titleView;
		EditText contentView;
		public LayoutItem(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			titleView = new EditText(context);
			titleView.setBackgroundColor(Color.WHITE);
			titleView.setTextColor(context.getResources().getColor(R.color.font_595959));
			titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(context, 46));
			titleView.setHint("请输入组合名称");
			
			contentView = new EditText(context);
			contentView.setBackgroundColor(Color.WHITE);
			contentView.setTextColor(context.getResources().getColor(R.color.font_595959));
			contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(context, 46));
			contentView.setHint("请添加组合描述，方便更多人了解您的操作理念");
			
			doLayout();
		}
		
		private void doLayout(){
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			LinearLayout layout = new LinearLayout(getContext());
//			layout.setBackgroundColor(Color.WHITE);
			layout.setOrientation(VERTICAL);
			addView(layout,p);
			
			LinearLayout titleLayout = new LinearLayout(getContext());
			titleLayout.setBackgroundColor(Color.WHITE);
			p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			p.setMargins(0, 0, 0, 1);
			layout.addView(titleLayout,p);
			p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			p.setMargins(Function.getFitPx(getContext(), 40), Function.getFitPx(getContext(), 40), 0, 0);
			titleLayout.addView(titleView,p);
			
			LinearLayout contentLayout = new LinearLayout(getContext());
			contentLayout.setBackgroundColor(Color.WHITE);
			p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			layout.addView(contentLayout,p);
			p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Function.getFitPx(getContext(), 200));
			p.setMargins(Function.getFitPx(getContext(), 40), 0, 0, 0);
			contentLayout.addView(contentView,p);
		}
		
	}

}
