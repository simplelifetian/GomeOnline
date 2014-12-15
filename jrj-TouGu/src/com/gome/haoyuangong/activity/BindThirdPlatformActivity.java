package com.gome.haoyuangong.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.layout.self.BarItem;
import com.gome.haoyuangong.layout.self.Function;

public class BindThirdPlatformActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		content.removeAllViews();
		content.addView(getView());
		
		setTitle("绑定其他平台");
	}
	
	private LinearLayout getView(){
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Function.getFitPx(this, 140));
		BarItem item = new BarItem(this);
		item.setRightArrowVisible(View.INVISIBLE);
		item.setDrawBottomLine(true);
		item.setTitle("腾讯QQ");
		item.setHeadImageVisible(true);
		item.setTitleFontColor(getResources().getColor(R.color.font_595959));
		item.setHeadImage(R.drawable.icon_share_qq,100);
		item.setInfoText("点击绑定");
		item.setInfoFontColor(getResources().getColor(R.color.font_4c87c6));
		p.setMargins(0, Function.getFitPx(this, 40), 0, 0);
		layout.addView(item,p);
		
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Function.getFitPx(this, 140));
		item = new BarItem(this);
		item.setRightArrowVisible(View.INVISIBLE);
		item.setDrawBottomLine(true);
		item.setTitle("新浪微博（小薇）");
		item.setHeadImageVisible(true);
		item.setTitleFontColor(getResources().getColor(R.color.font_595959));
		item.setHeadImage(R.drawable.icon_share_sina,100);
		item.setInfoText("解除绑定");
		item.setInfoFontColor(getResources().getColor(R.color.font_dd3030));
		layout.addView(item,p);

		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Function.getFitPx(this, 140));
		item = new BarItem(this);
		item.setRightArrowVisible(View.INVISIBLE);
		item.setTitle("微信");
		item.setHeadImageVisible(true);
		item.setTitleFontColor(getResources().getColor(R.color.font_595959));
		item.setHeadImage(R.drawable.icon_share_weixin,100);
		item.setInfoText("点击绑定");
		item.setInfoFontColor(getResources().getColor(R.color.font_4c87c6));		
		layout.addView(item,p);
		
		return layout;
	}

}
