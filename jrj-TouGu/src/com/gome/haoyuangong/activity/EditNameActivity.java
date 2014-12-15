package com.gome.haoyuangong.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.layout.self.Function;

public class EditNameActivity extends BaseActivity {

	private String name="";
	private EditText nameText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		name = intent.getStringExtra("name");
		nameText = new EditText(this);
		nameText.setHint("请输入昵称");
		nameText.setText(name);
		nameText.setTextColor(this.getResources().getColor(R.color.font_595959));
		nameText.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(this, 46));
		nameText.setBackgroundColor(Color.WHITE);
		content.removeAllViews();
		content.addView(getView());
		setTitle("修改昵称");
		titleRight2.setText("完成");
//	    titleRight2.setTextSize(this.getResources().getDimension(R.dimen.text_size_8));
	    titleRight2.setGravity(Gravity.CENTER);
	    titleRight2.setOnClickListener(this);
		
	}
	private View getView(){
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,Function.getFitPx(this, 120) ,1);
//		layout.setBackgroundColor(Color.WHITE);
		p.setMargins(0, Function.getFitPx(this, 40), 0, 0);
		layout.addView(nameText,p);
		ImageView image = new ImageView(this);
		image.setBackgroundColor(Color.WHITE);
		image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				nameText.setText("");
			}
		});
		image.setScaleType(ScaleType.CENTER_INSIDE);
		image.setImageDrawable(this.getResources().getDrawable(R.drawable.icon_delete));
		p = new LinearLayout.LayoutParams(Function.getFitPx(this, 120),Function.getFitPx(this, 120));
		p.setMargins(0, Function.getFitPx(this, 40), 0, 0);
		layout.addView(image,p);
		return layout;
	}
	@Override
	public void onClick(View v){
		super.onClick(v);
		switch(v.getId()){
			case R.id.title_right2:		
				if (TextUtils.isEmpty(nameText.getText().toString()))
				{
					Toast.makeText(EditNameActivity.this, "请输入昵称", Toast.LENGTH_SHORT).show();
				}
				Intent newIntent = new Intent();
				newIntent.putExtra("returnvalue", nameText.getText().toString());
				setResult(RESULT_OK, newIntent);
				finish();
				break;
		}
	}

}
