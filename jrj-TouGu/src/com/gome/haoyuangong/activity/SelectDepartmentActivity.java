package com.gome.haoyuangong.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.gome.haoyuangong.layout.self.BarItem;
/**
 * 选择所属营业部
 * @author Administrator
 *
 */
public class SelectDepartmentActivity extends ListViewActivity {

	List<String> items;
	Intent intent;
	String departmentName="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setPullRefreshEnable(false);
		setPullLoadEnable(false);
		intent = getIntent();
		if (intent.getStringExtra("name") != null)
			departmentName = intent.getStringExtra("name");
		items = new ArrayList<String>();
		items.add("国贸营业部");
		items.add("复兴门营业部");
		items.add("西城营业部");
		items.add("东城营业部");
		addItems();
		reFresh();
		setTitle("修改所属营业部");
	}
	private void addItems(){
		for(int i=0;i<items.size();i++){			
			LinearLayout layout = new LinearLayout(this);
			final BarItem item = new BarItem(this);
			item.setHeadImageVisible(true);
			item.setRightArrowVisible(View.INVISIBLE);
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, item.getItemHeight());
			item.setTitle(items.get(i));
//			if (items.get(i).equals(departmentName))
//				item.setHeadImage(R.drawable.icon_check_selected);
			layout.addView(item,p);
			item.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent newIntent = new Intent();
					newIntent.putExtra("returnvalue", item.getTitle());
					setResult(RESULT_OK, newIntent);
					finish();
				}
			});
			addItem(layout);
		}
	}

}
