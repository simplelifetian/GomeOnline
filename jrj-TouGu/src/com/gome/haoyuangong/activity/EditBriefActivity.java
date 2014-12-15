package com.gome.haoyuangong.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.layout.self.Function;
/**
 * 编辑简介
 * @author Administrator
 *
 */
public class EditBriefActivity extends BaseActivity {
	public static String TITLE = "title";
	private String breif="";
	private EditText briefText;
	public final static String BRIEFCONTENT = "brief";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		breif = intent.getStringExtra(BRIEFCONTENT);
		briefText = new EditText(this);
		briefText.setHint("请输入简介");
		briefText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});
		briefText.setText(breif);
		briefText.setEnabled(false);
		briefText.setGravity(Gravity.TOP);
		briefText.setTextColor(this.getResources().getColor(R.color.font_595959));
		briefText.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(this, 46));
		briefText.setBackgroundColor(Color.WHITE);
		content.removeAllViews();
		content.addView(getView());
		setTitle(getIntent().getStringExtra(TITLE));
//		titleRight2.setText("完成");
//	    titleRight2.setGravity(Gravity.CENTER);
//	    titleRight2.setOnClickListener(this);
		
	}
	private View getView(){
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,1);
//		layout.setBackgroundColor(Color.WHITE);
		layout.addView(briefText,p);
//		ImageView image = new ImageView(this);
//		image.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				briefText.setText("");
//			}
//		});
//		image.setScaleType(ScaleType.CENTER_INSIDE);
//		image.setImageDrawable(this.getResources().getDrawable(R.drawable.icon_delete));
//		p = new LinearLayout.LayoutParams(Function.getFitPx(this, 260),Function.getFitPx(this, 300),1);
//		layout.addView(image,p);
		return layout;
	}
	@Override
	public void onClick(View v){
		super.onClick(v);
		switch(v.getId()){
			case R.id.title_right2:		
				Intent newIntent = new Intent();
				newIntent.putExtra("returnvalue", briefText.getText().toString());
				setResult(RESULT_OK, newIntent);
				finish();
				break;
		}
	}

}
