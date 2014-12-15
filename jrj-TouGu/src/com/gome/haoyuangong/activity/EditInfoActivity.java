package com.gome.haoyuangong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.layout.self.Function;

public class EditInfoActivity extends BaseActivity {
	Intent intent;
	EditText editView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		intent = getIntent();
		editView = new EditText(this);
		editView.setHint("可对签约者所属券商等信息进行标注");
		editView.setText(intent.getStringExtra("bz"));
		editView.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(this, 40));
		editView.setTextColor(this.getResources().getColor(R.color.font_595959));
		setTitle("备注信息");
		doLayout();
	     titleRight2.setText("完成");
//	     titleRight2.setTextSize(this.getResources().getDimension(R.dimen.text_size_8));
	     titleRight2.setGravity(Gravity.CENTER);
	     titleRight2.setOnClickListener(this);
	}
	@Override
	public void onClick(View v){
		super.onClick(v);
		switch(v.getId()){
			case R.id.title_right2:		
				Intent newIntent = new Intent();
				String s = editView.getText().toString();
				newIntent.putExtra("data", s);
				setResult(RESULT_OK, newIntent);
				finish();
				break;
		}
	}
	private void doLayout(){
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		TextView tv = new TextView(this);
		tv.setText("详细备注信息");
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(this, 40));
		tv.setTextColor(this.getResources().getColor(R.color.font_595959));
		tv.setPadding(Function.getFitPx(this, 20), Function.getFitPx(this, 20), 0, 0);
		layout.addView(tv,p);

		p.setMargins(Function.getFitPx(this, 20), Function.getFitPx(this, 20), 0, 0);
		layout.addView(editView,p);
		
		content.removeAllViews();
		content.addView(layout);
	}
}
