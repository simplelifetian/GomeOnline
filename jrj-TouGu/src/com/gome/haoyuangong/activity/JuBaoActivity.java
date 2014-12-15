package com.gome.haoyuangong.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.log.Logger;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.BaseResultWeb;
import com.gome.haoyuangong.net.url.NetUrlLoginAndRegist;
import com.gome.haoyuangong.net.url.NetUrlTougu;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.utils.ImageUtils;
import com.gome.haoyuangong.utils.StringUtils;

public class JuBaoActivity extends BaseActivity {

	private static final String TAG = JuBaoActivity.class.getName();

	public static final String BUNDLE_PARAM_VIEWPOINTID = "BUNDLE_PARAM_VIEWPOINTID";
	public static final String BUNDLE_PARAM_ADVISERID = "BUNDLE_PARAM_ADVISERID";
	public static final String BUNDLE_PARAM_TITLE = "BUNDLE_PARAM_TITLE";
	public static final String BUNDLE_PARAM_CONTENT_TYPE = "BUNDLE_PARAM_CONTENT_TYPE";
	
	public static final int TYPE_OPINION = 1;
	public static final int TYPE_LIVE = 1;
	public static final int TYPE_ANSWER = 1;
	
	private TextView jubaoTitle;
	private CheckBox[] jubaoItems;
	private EditText jubaoContent;
	private TextView submit;
	
	private String viewpointId;
	private String adviserId;
	private String opTitle;
	private int contentType;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jubao);
		setTitle("举报");
		titleRight2.setText("提交");
		
		viewpointId = getIntent().getStringExtra(BUNDLE_PARAM_VIEWPOINTID);
		adviserId = getIntent().getStringExtra(BUNDLE_PARAM_ADVISERID);
		opTitle = getIntent().getStringExtra(BUNDLE_PARAM_TITLE);
		contentType = getIntent().getIntExtra(BUNDLE_PARAM_CONTENT_TYPE, -1);
		if(StringUtils.isEmpty(viewpointId)||StringUtils.isEmpty(adviserId) || contentType == -1){
			Toast.makeText(this, "无效举报对象", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		
		jubaoTitle = (TextView)findViewById(R.id.jubao_title);
		jubaoItems = new CheckBox[]{
				(CheckBox)findViewById(R.id.jubao_item_1),
				(CheckBox)findViewById(R.id.jubao_item_2),
				(CheckBox)findViewById(R.id.jubao_item_3),
				(CheckBox)findViewById(R.id.jubao_item_4),
				(CheckBox)findViewById(R.id.jubao_item_5)
		};
		jubaoContent = (EditText)findViewById(R.id.jubao_content);
		
		for(int i = 0 ; i< jubaoItems.length ; i++){
			jubaoItems[i].setTag(i);
			jubaoItems[i].setOnClickListener(MyCheckBoxListener);
		}
		submit = (TextView)findViewById(R.id.tv_submit);
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getJubao();
			}
		});
		
		jubaoTitle.setText("您举报【"+opTitle+"】观点的理由：");
		
	}

	
	private View.OnClickListener MyCheckBoxListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			CheckBox checkBox = (CheckBox)v;
			int index = (Integer)checkBox.getTag();
			for(int i = 0 ; i< jubaoItems.length ; i++){
				if(index == i){
					jubaoItems[i].setChecked(true);
				}else{
					jubaoItems[i].setChecked(false);
				}
			}
		}
	};
	
	
	private void getJubao() {
		if(StringUtils.isEmpty(UserInfo.getInstance().getDeivceId())){
			Toast.makeText(this, "服务异常", Toast.LENGTH_SHORT).show();
			return;
		}
		String url = NetUrlTougu.JUBAO.replace("_adviserId", adviserId).replace("_deviceId", UserInfo.getInstance().getDeivceId()).replace("_contentId", viewpointId).replace("_contentType", ""+contentType);
		for(CheckBox checkBox : jubaoItems){
			if(checkBox.isChecked()){
				int index = (Integer)checkBox.getTag();
				String content = checkBox.getText().toString().trim();
				try {
					url = url.replace("_reasonId", ""+(index + 1)).replace("_reasonValue", URLEncoder.encode(content, "utf8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					Toast.makeText(this, "无效理由", Toast.LENGTH_SHORT).show();
					return;
				}
				break;
			}
		}
		
		StringBuilder sb = new StringBuilder();
		if(!StringUtils.isEmpty(UserInfo.getInstance().getUserId())){
			sb.append("userId=").append(UserInfo.getInstance().getUserId());
		}
		
		String jubaoContentString = jubaoContent.getText().toString();
		if(!StringUtils.isEmpty(jubaoContentString)){
			if(sb.length() > 0){
				sb.append("&");
			}
			try {
				String enContent = URLEncoder.encode(jubaoContentString, "utf8");
				sb.append("content=").append(enContent);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
			}
		}
		if(sb.length() > 0){
			url = url+"?"+sb.toString();
		}
				
		Log.e(TAG, url);
		JsonRequest<BaseResultWeb> request = new JsonRequest<BaseResultWeb>(Method.GET, url,
				new RequestHandlerListener<BaseResultWeb>(getContext()) {

					@Override
					public void onStart(Request request) {
						super.onStart(request);
						 showDialog(request);
					}

					@Override
					public void onEnd(Request request) {
						super.onEnd(request);
						 hideDialog(request);
					}

					@Override
					public void onSuccess(String id, BaseResultWeb data) {
						// TODO Auto-generated method stub
						if(data.getRetCode() == 0){
							Toast.makeText(JuBaoActivity.this, "举报成功", Toast.LENGTH_SHORT).show();
							finish();
						}
					}
				}, BaseResultWeb.class);

		send(request);

	}
	
	@Override
	public void onClick(View v) {
//		super.onClick(v);
		switch (v.getId()) {
		case R.id.title_left1:
			finish();
			break;
		case R.id.title_right2:
			//判断用户是否登录
			getJubao();
			break;
		}
	}
}
