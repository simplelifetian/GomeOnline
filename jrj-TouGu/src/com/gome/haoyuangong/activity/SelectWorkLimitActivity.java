package com.gome.haoyuangong.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.layout.self.BarItem;
import com.gome.haoyuangong.layout.self.Function;
import com.gome.haoyuangong.net.Request;

public class SelectWorkLimitActivity extends ListViewActivity {

	List<String> items;
	Intent intent;
	String workLimit="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setPullRefreshEnable(false);
		setPullLoadEnable(false);
		intent = getIntent();
		setDividerHeight(0);
		if (intent.getStringExtra("name") != null)
			workLimit = intent.getStringExtra("name");
		items = new ArrayList<String>();
		items.add("3年以下");
		items.add("3~5年");
		items.add("5~10年");
		items.add("10年以上");
		addItems();
		reFresh();
		setTitle("从业年限");
	}
	private void addItems(){
		for(int i=0;i<items.size();i++){			
			LinearLayout layout = new LinearLayout(this);
			layout.setBackgroundColor(0xffe9e9e9);
			final BarItem item = new BarItem(this);
			item.setTag(i);
			item.setHeadImageVisible(true);
			item.setRightArrowVisible(View.INVISIBLE);
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, item.getItemHeight());
			if (i == 0)
				p.setMargins(0, Function.getFitPx(getContext(), 40), 0, 1);
			else
				p.setMargins(0, 0, 0, 1);
			item.setTitle(items.get(i));
			if (items.get(i).equals(workLimit)){
				item.getRightImageView().setImageDrawable(getResources().getDrawable(R.drawable.icon_check_selected));
				item.setRightArrowVisible(View.VISIBLE);
			}			
			layout.addView(item,p);
			item.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent newIntent = new Intent();
					newIntent.putExtra("returnvalue", item.getTitle());
					newIntent.putExtra("returnindex", Integer.parseInt(item.getTag().toString())+1);
					setResult(RESULT_OK, newIntent);
					finish();
				}
			});
			addItem(layout);
		}
	}

}
