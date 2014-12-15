/**
 * 
 */
package com.gome.haoyuangong.activity;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.ShareConstants;
import com.gome.haoyuangong.utils.StringUtils;
import com.gome.haoyuangong.views.KeyboardLayout;
import com.gome.haoyuangong.views.KeyboardLayout.onKybdsChangeListener;
import com.jrj.sharesdk.CallbackListener;
import com.jrj.sharesdk.PType;
import com.jrj.sharesdk.msg.MsgImageText;
import com.jrj.sharesdk.platform.ShareToSina;

/**
 * 
 */
@SuppressLint("NewApi")
public class ShareToSinaActivity extends BaseActivity{
	
	private static final String TAG = ShareToSinaActivity.class.getName();
	public static final String BUNDLE_PARAM_SHARE_CONTENT = "BUNDLE_PARAM_SHARE_CONTENT";

	private KeyboardLayout mainLayout;
	private LinearLayout writeOpBottom;
	
	private EditText shareContent;
	private TextView tvCharCount;
	private MsgImageText shareContentStr;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		shareContentStr = getIntent().getParcelableExtra(BUNDLE_PARAM_SHARE_CONTENT);
		if(shareContentStr == null){
			Toast.makeText(this, "无分享内容", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		setContentView(R.layout.share_sina_layout);
		setTitle("分享到微博");
		titleRight2.setText("发布");
		
		writeOpBottom = (LinearLayout)findViewById(R.id.write_opinion_bottom);
		mainLayout = (KeyboardLayout)findViewById(R.id.main_lo);
		mainLayout.setOnkbdStateListener(new onKybdsChangeListener() {
            
            public void onKeyBoardStateChange(int state) {
                    switch (state) {
                    case KeyboardLayout.KEYBOARD_STATE_HIDE:
                    		uiHandler.sendEmptyMessage(1);
                            break;
                    case KeyboardLayout.KEYBOARD_STATE_SHOW:
                    		uiHandler.sendEmptyMessage(2);
                            break;
                    }
            }
		});
		shareContent = (EditText)findViewById(R.id.share_content);
		tvCharCount = (TextView)findViewById(R.id.tv_char_count);
		shareContent.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String text = s.toString();
				tvCharCount.setText(""+text.length());
			}
			
		});
		shareContent.setText(shareContentStr.summary);
	}
	
	@Override
	public void onClick(View v) {
//		super.onClick(v);
		switch (v.getId()) {
		case R.id.title_left1:
			finish();
			break;
		case R.id.title_right2:
			sendShare(PType.PLATFORM_SINA);
			break;
		}
	}
	

	public Handler uiHandler = new Handler(){
		
		@Override
        public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				writeOpBottom.setVisibility(View.GONE);
				break;
			case 2:
				writeOpBottom.setVisibility(View.VISIBLE);
				break;
			}
		}
	};
	
//	private void initShare(){
//		
//		if(ShareManager.getInstance() == null){
//			HashMap<String, String> map = new HashMap<String, String>();
//			map.put(ShareManager.KEY_APPID_QQ,ShareConstants.QQ_APP_ID);
//			map.put(ShareManager.KEY_APPID_WX,ShareConstants.WX_APP_ID);
//			
//			map.put(ShareManager.KEY_APPID_SINA,ShareConstants.APP_KEY);
//			map.put(ShareManager.KEY_APPID_CALLBACK,ShareConstants.REDIRECT_URL);
//			map.put(ShareManager.KEY_APPID_SCOPE,ShareConstants.SCOPE);
//			ShareManager.init(this,map);
//		}
//	}
	
	private void sendShare(int pType){
		String shareC = shareContent.getText().toString();
		if(StringUtils.isEmpty(shareC)){
			Toast.makeText(this, "输入分享内容", Toast.LENGTH_SHORT).show();
			return;
		}
		MsgImageText msg = new MsgImageText();
		msg.appName=getString(R.string.app_name);
		msg.pType=pType;
		msg.summary = shareC;
		msg.image = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
//		msg.share(new CallbackListener() {
//			@Override
//			public void onSuccess() {
//				Toast.makeText(ShareToSinaActivity.this,"分享成功",Toast.LENGTH_SHORT).show();
//			}
//			
//			@Override
//			public void onFailure() {
//				Toast.makeText(ShareToSinaActivity.this,"分享失败",Toast.LENGTH_SHORT).show();
//			}
//			
//			@Override
//			public void onCancel() {
////				showToast(ImageEditActivity.this,"取消分享");
//			}
//		});
		ShareToSina shareToSina = ShareToSina.getInstannce(this,ShareConstants.APP_KEY,ShareConstants.REDIRECT_URL,ShareConstants.SCOPE);
		boolean success = shareToSina.shareMsg(this, shareContentStr, new CallbackListener(){

				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					Toast.makeText(ShareToSinaActivity.this,"分享成功",Toast.LENGTH_SHORT).show();
					finish();
				}

				@Override
				public void onFailure() {
					// TODO Auto-generated method stub
					Toast.makeText(ShareToSinaActivity.this,"分享失败",Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onCancel() {
					// TODO Auto-generated method stub
//					showToast(ShareToSinaActivity.this,"取消分享");
				}
				
			});
		if(success){
			finish();
		}
	}

}
