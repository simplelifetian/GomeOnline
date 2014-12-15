package com.gome.haoyuangong.activity;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.log.Logger;
import com.gome.haoyuangong.net.url.AskUrl;
import com.qq.taf.jce.dynamic.StringField;

import android.os.Bundle;
import android.webkit.WebView;

public class AskDetailWebActivity extends BaseActivity{
	public static final String BUNDLE_ID="ask_id";
	private int askId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ask_detail_web);
		setTitle("咨询详情");
		askId = getIntent().getIntExtra(BUNDLE_ID, -1);
		if(askId<0){
			Logger.error("AskDetailWebActivity", "invalid askId");
			finish();
			return;
		}
		WebView view  = (WebView) findViewById(R.id.webview);
		view.loadUrl(String.format(AskUrl.ASK_DETAIL_WEB, askId));
	}
}
