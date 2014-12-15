package com.gome.haoyuangong.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.bean.Stock;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.tougu.ConsultingResultBean;
import com.gome.haoyuangong.net.result.tougu.ConsultingTimesResultBean;
import com.gome.haoyuangong.net.url.NetUrlTougu;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.utils.StringUtils;
import com.gome.haoyuangong.views.MySwitchButton;

public class OpenConsultingActivity extends BaseActivity {

	private static final String TAG = OpenConsultingActivity.class.getName();
	public static final int STOCK_REQUEST_CODE = 1001;

	public static final String BUNDLE_TYPE = "BUNDLE_TYPE";
	public static final String BUNDLE_PARAM_NAME = "BUNDLE_PARAM_NAME";
	public static final String BUNDLE_PARAM_ID = "BUNDLE_PARAM_ID";

	public static final int OPEN_CONSULTING = 1;
	public static final int SPECIAL_CONSULTING = 2;
	
	public static final int MAX_COUNT_EDIT = 200;

	private MySwitchButton mSbAllredeem;
	private CheckBox mySwitchButton;
	
	private LinearLayout specialBottom;
	private CheckBox[] checkBoxs;
	
	private EditText consultingContent;
	
//	private KeyboardLayout mainLayout;
	private LinearLayout writeOpBottom;
	private ImageView stockSrc;
	private TextView avaliableTimes;
	
	private int consultType;
	
	private String touGuName;
	private String touGuId;
	
	private LinearLayout askTouguLo;
	private ImageView imageView1;
	
	private TextView tvConsoleCount;
	
	private LinearLayout guideLayout;
	
	private ProgressBar askCountProcessor;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_open_consulting);
		setTitle("咨询");
		titleRight2.setText("发布");
		
		initViews();
		consultType = getIntent().getIntExtra(BUNDLE_TYPE, 1);
		
		switch (consultType) {
		case OPEN_CONSULTING:
			setTitle("公开咨询");
			initOpenConsulting();
			break;
		case SPECIAL_CONSULTING:
			touGuName = getIntent().getStringExtra(BUNDLE_PARAM_NAME);
			setTitle("咨询"+(null == touGuName?"":touGuName));
			initSpecialConsulting();
			break;
		}
		overridePendingTransition(R.anim.dialog_enter, R.anim.activity_default);
		getConsultingTimes();
	}
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.activity_default, R.anim.dialog_exit);
	}
	
	private void initViews(){
//		specialBottom = (LinearLayout)findViewById(R.id.special_bottom);
		consultingContent = (EditText)findViewById(R.id.consulting_content);
		tvConsoleCount = (TextView)findViewById(R.id.tv_console_count);
		tvConsoleCount.setText(0 + "/" + MAX_COUNT_EDIT);
		consultingContent.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				tvConsoleCount.setText(s.toString().length() + "/" + MAX_COUNT_EDIT);

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
//		writeOpBottom = (LinearLayout)findViewById(R.id.write_opinion_bottom);
//		mainLayout = (KeyboardLayout)findViewById(R.id.main_lo);
//		mainLayout.setOnkbdStateListener(new onKybdsChangeListener() {
//            
//            public void onKeyBoardStateChange(int state) {
//                    switch (state) {
//                    case KeyboardLayout.KEYBOARD_STATE_HIDE:
//                    		uiHandler.sendEmptyMessage(1);
////                    		Toast.makeText(WriteOpinionActivity.this, "键盘隐藏", Toast.LENGTH_SHORT).show();
//                    		
//                            break;
//                    case KeyboardLayout.KEYBOARD_STATE_SHOW:
//                    		uiHandler.sendEmptyMessage(2);
////                    		Toast.makeText(WriteOpinionActivity.this, "键盘显示", Toast.LENGTH_SHORT).show();
//                    		
//                            break;
//                    }
//            }
//		});
//		stockSrc = (ImageView) findViewById(R.id.stock_src);
//		stockSrc.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(OpenConsultingActivity.this, StockSearchActivity.class);
//				intent.putExtra(StockSearchFragment.BUNDLE_TYPE, StockSearchFragment.TYPE_SELECT);
//				startActivityForResult(intent, STOCK_REQUEST_CODE);
//			}
//		});
		avaliableTimes = (TextView)findViewById(R.id.igid_date);
		avaliableTimes.setText("");
		avaliableTimes.setVisibility(View.GONE);
		avaliableTimes.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getConsultingTimes();
			}
		});
		
		askTouguLo = (LinearLayout)findViewById(R.id.ask_tougu_lo);
		imageView1 = (ImageView) findViewById(R.id.imageView_1);
		imageView1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});
		askCountProcessor = (ProgressBar)findViewById(R.id.ask_count_processor);
		
	}
	
//	public Handler uiHandler = new Handler(){
//		
//		@Override
//        public void handleMessage(Message msg) {
//			switch(msg.what){
//			case 1:
//				writeOpBottom.setVisibility(View.GONE);
//				break;
//			case 2:
//				writeOpBottom.setVisibility(View.VISIBLE);
//				break;
//			}
//		}
//	};
	
	private void initOpenConsulting() {
		askTouguLo.setVisibility(View.GONE);
		consultingContent.setHint("您发布咨询后，所有投顾都可回答您的提问。请清晰描述问题，能获得更准确的投顾解答");
		titleRight2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doConsulting();
			}
		});
		showSoftInput(consultingContent);
	}

	private void initSpecialConsulting() {
		touGuId = getIntent().getStringExtra(BUNDLE_PARAM_ID);
		if(StringUtils.isEmpty(touGuId)||StringUtils.isEmpty(touGuName)){
			Toast.makeText(OpenConsultingActivity.this, "对象参数错误", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		if(touGuId.equals(UserInfo.getInstance().getUserId())){
			Toast.makeText(OpenConsultingActivity.this, "投顾不能咨询自己", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
//		specialBottom.setVisibility(View.VISIBLE);
		askTouguLo.setVisibility(View.VISIBLE);
		consultingContent.setHint("请清晰描述问题，能获得更准确的投顾解答");
		mySwitchButton = (CheckBox) findViewById(R.id.my_switch_2);
		mySwitchButton.setChecked(true);
//		mSbAllredeem.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				if (isChecked) {
//					setCheckBoxesEnable(false);
//				} else {
//					setCheckBoxesEnable(true);
//				}
//			}
//		});
		
//		CheckBox time30m = (CheckBox)findViewById(R.id.time_30m);
//		CheckBox time1h = (CheckBox)findViewById(R.id.time_1h);
//		CheckBox time2h = (CheckBox)findViewById(R.id.time_2h);
//		checkBoxs = new CheckBox[]{time30m,time1h,time2h};
//		
//		for(int i = 0 ; i < checkBoxs.length ; i++){
//			checkBoxs[i].setTag(i);
//			checkBoxs[i].setOnClickListener(checkBoxListener);
//		}
//		
//		mSbAllredeem.setChecked(true);
//		setCheckBoxesEnable(false);
		
		guideLayout = (LinearLayout)findViewById(R.id.guide_layout);
		if (!getGuideHome()) {
			hideSoftInput();
			guideLayout.setBackgroundResource(R.drawable.user_guide_zixun);
			guideLayout.setVisibility(View.VISIBLE);
			guideLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					guideLayout.setVisibility(View.GONE);
					showSoftInput(consultingContent);
					SaveGuideHome();
				}
			});
		}else{
			showSoftInput(consultingContent);
		}
		
		titleRight2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doConsulting();
			}
		});
		
	}
	
	private View.OnClickListener checkBoxListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int checkBoxId = (Integer)v.getTag();
			setCheckBox(checkBoxId);
		}
	};
	
	private void setCheckBox(int index){
		for(int i = 0 ; i < checkBoxs.length ; i++){
			if(index == i && checkBoxs[i].isEnabled()){
				checkBoxs[i].setChecked(true);
			}else if(checkBoxs[i].isEnabled()){
				checkBoxs[i].setChecked(false);
			}
		}
	}
	
	private void setCheckBoxesEnable(boolean bool){
		for(int i = 0 ; i < checkBoxs.length ; i++){
			checkBoxs[i].setEnabled(bool);
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int responseCode, Intent data) {
		if (STOCK_REQUEST_CODE == requestCode) {
			if (data != null) {
				
			}
			return;
		}
		super.onActivityResult(requestCode, responseCode, data);
	}
	
	private void doConsulting() {

		String url = NetUrlTougu.OPINION_CONSULTING;
		Log.e(TAG, url);
		
		String content = consultingContent.getText().toString();
		if(StringUtils.isEmpty(content) || content.trim().length() == 0){
			Toast.makeText(OpenConsultingActivity.this, "请输入咨询内容", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(!UserInfo.getInstance().isLogin()){
			Intent intent = new Intent(OpenConsultingActivity.this,LoginActivity.class);
			startActivity(intent);
			return;
		}
		
		Map<String,String> params = new HashMap<String,String>();
		params.put("content", content);
		params.put("uid", UserInfo.getInstance().getUserId());
		params.put("uname", UserInfo.getInstance().getUserName());
		params.put("source", "android");
		
		switch (consultType) {
		case OPEN_CONSULTING:
			params.put("isopen", "1");
			break;
		case SPECIAL_CONSULTING:
			if(!mySwitchButton.isChecked()){
				params.put("isopen", "3");
				
			}else{
				params.put("isopen", "2");
//				for(CheckBox chBox : checkBoxs){
//					if(chBox.isChecked()){
//						int timeType = (Integer)chBox.getTag();
//						if( 0 == timeType){
//							params.put("opentime", "30");
//						}else if(1 == timeType){
//							params.put("opentime", "60");
//						}else if(2 == timeType){
//							params.put("opentime", "120");
//						}
//					}
//				}
				params.put("opentime", "120");
			}
			params.put("anwserUserId", touGuId);
			params.put("answerUsername", touGuName);
			break;
		}
		Log.e(TAG, url);
		Log.e(TAG, params.toString());
		JsonRequest<ConsultingResultBean> request = new JsonRequest<ConsultingResultBean>(Method.POST, url,params,
				new RequestHandlerListener<ConsultingResultBean>(getContext()) {

					@Override
					public void onStart(Request request) {
						super.onStart(request);
						 showDialog(request,"发布中...");
					}

					@Override
					public void onEnd(Request request) {
						super.onEnd(request);
						 hideDialog(request);
					}

					@Override
					public void onSuccess(String id, ConsultingResultBean data) {
						// TODO Auto-generated method stub
//						Toast.makeText(AttentionDetailActivity.this, "赞成功", Toast.LENGTH_SHORT).show();
						if(data.getRetCode() == 0){
							Toast.makeText(OpenConsultingActivity.this, "发布咨询成功", Toast.LENGTH_SHORT).show();
							finish();
						}else{
							Toast.makeText(OpenConsultingActivity.this, data.getData().getMsg(), Toast.LENGTH_SHORT).show();
						}
					}
				}, ConsultingResultBean.class);

		send(request);

	}
	
	private void getConsultingTimes() {

		String url = String.format(NetUrlTougu.OPINION_CONSULTING_TIMES,UserInfo.getInstance().getUserId());
		Log.e(TAG, url);
		JsonRequest<ConsultingTimesResultBean> request = new JsonRequest<ConsultingTimesResultBean>(Method.GET, url,
				new RequestHandlerListener<ConsultingTimesResultBean>(getContext()) {

					@Override
					public void onStart(Request request) {
						super.onStart(request);
//						 showDialog(request);
						askCountProcessor.setVisibility(View.VISIBLE);
						avaliableTimes.setVisibility(View.GONE);
					}

					@Override
					public void onEnd(Request request) {
						super.onEnd(request);
//						 hideDialog(request);
						askCountProcessor.setVisibility(View.GONE);
						avaliableTimes.setVisibility(View.VISIBLE);
					}

					@Override
					public void onSuccess(String id, ConsultingTimesResultBean data) {
						// TODO Auto-generated method stub
						if(data.getRetCode() == 0){
							avaliableTimes.setText(""+data.getData().getTimes());
						}else{
							Toast.makeText(OpenConsultingActivity.this, "获取可用次数失败", Toast.LENGTH_SHORT).show();
							avaliableTimes.setText("");
						}
					}
					
					@Override
					public void onFailure(String id, int code, String str,Object obj) {
						super.onFailure(id, code, str, obj);
//						avaliableTimes.setText("ERROR");
					}
				}, ConsultingTimesResultBean.class);

		send(request);

	}
	
	private boolean getGuideHome() {
		// 建立一个缓存时间的文件
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		return sp.getBoolean("guideSpecialConsulting", false);
	}

	private void SaveGuideHome() {
		// 建立一个缓存时间的文件
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(this).edit();
		editor.putBoolean("guideSpecialConsulting", true);
		editor.commit();
	}
}
