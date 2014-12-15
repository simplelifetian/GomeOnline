package com.gome.haoyuangong.activity;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.layout.self.Function;
import com.gome.haoyuangong.layout.self.PersonalInfoItem;
import com.gome.haoyuangong.net.Request;
/**
 * 群发消息
 * @author menghui
 *
 */
public class GroupMessageActivity extends BaseActivity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		content.removeAllViews();
		content.addView(getView());
		setTitle("群发记录");
	}
	
	private LinearLayout getView(){
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Function.getFitPx(this, 200));
		
		PersonalInfoItem item = new PersonalInfoItem(this);
		item.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(GroupMessageActivity.this, GroupMessagSendActivity.class));
			}
		});
		item.setNameFontColor(getResources().getColor(R.color.font_595959));
		item.setNameFontSize(50);
		item.setName("对关注用户群发");
		item.setHeadPicSize(120, 120);
		item.setHeadPic(R.drawable.icon_share_qq);
		item.doLayout();
		p.setMargins(0, 0, 0, 1);
		layout.addView(item,p);
		
		item = new PersonalInfoItem(this);
item.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(GroupMessageActivity.this, GroupMessagSendActivity.class));
			}
		});
		item.setNameFontColor(getResources().getColor(R.color.font_595959));
		item.setNameFontSize(50);
		item.setName("对签约用户群发");
		item.setHeadPicSize(120, 120);
		item.setHeadPic(R.drawable.icon_share_qq);
		item.doLayout();
		layout.addView(item,p);
		
		return layout;
	}

}
