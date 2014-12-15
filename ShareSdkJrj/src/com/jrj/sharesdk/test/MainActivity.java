package com.jrj.sharesdk.test;

import java.util.ArrayList;
import java.util.HashMap;

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.wxapi.WXEntryActivity;

import com.jrj.sharesdk.CallbackListener;
import com.jrj.sharesdk.PType;
import com.jrj.sharesdk.ShareManager;
import com.jrj.sharesdk.msg.MsgImage;
import com.jrj.sharesdk.msg.MsgImageText;
import com.jrj.sharesdk.msg.MsgImagesText;
import com.jrj.sharesdk.msg.MsgText;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity{
	ArrayList<String> urls = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		urls.add("http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(ShareManager.KEY_APPID_QQ,"222222");
		ShareManager.init(this,map);
		findViewById(R.id.btn1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MsgImage msg = new MsgImage();
				msg.appName="测试应用";
				msg.pType=PType.PLATFORM_WX_friends;
				msg.imageUrl = "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif";
				msg.share(new CallbackListener() {
					@Override
					public void onSuccess() {
						System.out.println("onSuccess");
					}
					
					@Override
					public void onFailure() {
						System.out.println("onFailure");
					}
					
					@Override
					public void onCancel() {
						System.out.println("onCancel");
					}
				});
			}
		});
		
	}
}
