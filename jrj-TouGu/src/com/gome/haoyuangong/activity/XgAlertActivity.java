package com.gome.haoyuangong.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.xg.MessageType;
import com.gome.haoyuangong.xg.XgMessageDeal;

public class XgAlertActivity extends FragmentActivity {
	public final static String CONTENT = "content";
	public final static String TITLE = "title";
	public final static String CUSTOMCONTENT = "customcontent";
	String customContent;
	Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		intent = getIntent();
		setContentView(R.layout.xg_alert);
		Button cancelBtn = (Button)findViewById(R.id.negativeButton);
		if (cancelBtn != null){
			cancelBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			});
		}
		Button okBtn = (Button)findViewById(R.id.positiveButton);
		if (okBtn != null){
			okBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					XgMessageDeal.DealMessage(XgAlertActivity.this, customContent);
					finish();
				}
			});
		}
		if (intent != null){
			((TextView)findViewById(R.id.title)).setText(intent.getStringExtra(TITLE));
			((TextView)findViewById(R.id.message)).setText(intent.getStringExtra(CONTENT));
			customContent = intent.getStringExtra(CUSTOMCONTENT);
			if (TextUtils.isEmpty(customContent)){
				okBtn.setVisibility(View.GONE);
				cancelBtn.setText("知道了");
				return;
			}
			try {
				JSONObject jsonObject = new JSONObject(customContent);
				int msgType = jsonObject.getInt("msgType");
				if (msgType !=MessageType.MESSAGE_STOCK_ALERT && msgType != MessageType.MESSAGE_ANWSER 
						&& msgType != MessageType.MESSAGE_ASK 
						&& msgType != MessageType.MESSAGE_SYSTEM && msgType != MessageType.MESSAGE_ADVISER_GROUPMESSAGE){
					okBtn.setVisibility(View.GONE);
					cancelBtn.setText("知道了");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
