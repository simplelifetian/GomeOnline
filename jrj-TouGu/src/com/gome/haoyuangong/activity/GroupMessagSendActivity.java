package com.gome.haoyuangong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.bean.Stock;
import com.gome.haoyuangong.utils.StringUtils;

public class GroupMessagSendActivity extends BaseActivity {
	private ImageView imageView2;
	public final int STOCK_REQUEST_CODE = 1001;
	private EditText etContent;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_message_send);
		etContent = (EditText) findViewById(R.id.et_reply);
		imageView2 = (ImageView) findViewById(R.id.imageView_2);
		if (imageView2 != null)
		{
			imageView2.setOnClickListener(new View.OnClickListener() {
	
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				}
			});
		}
		setTitle("群发消息");
	}
	
	@Override
	public void onActivityResult(int requestCode, int responseCode, Intent data) {
		if (STOCK_REQUEST_CODE == requestCode) {
			return;
		}
		super.onActivityResult(requestCode, responseCode, data);
	}

}
