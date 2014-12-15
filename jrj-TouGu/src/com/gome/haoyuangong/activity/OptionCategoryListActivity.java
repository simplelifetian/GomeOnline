/**
 * 
 */
package com.gome.haoyuangong.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.utils.StringUtils;
import com.gome.haoyuangong.views.xlistview.XListView;

/**
 * 
 */
public class OptionCategoryListActivity extends BaseActivity {

	private static final String TAG = OptionCategoryListActivity.class.getName();
	
	public static final String BUNDLE_PARAM_CURRINDEX = "BUNDLE_PARAM_CURRINDEX";
	public static final int SELECT_RESPONSE_CODE = 1060;

	private XListView myListView;

	private List<String[]> liveListData = new ArrayList<String[]>();

	private OptionCategoryAdapter myAdapter;

	private String currIndex;
	
	private Drawable select;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_refresh_listview_layout);
		setTitle("选择分类");
		
		currIndex = getIntent().getStringExtra(BUNDLE_PARAM_CURRINDEX);
		
		myListView = (XListView) findViewById(R.id.listView);
		myListView.setPullRefreshEnable(false);
		myListView.setPullLoadEnable(false);
		myListView.setDividerHeight(1);
//		myListView.setDivider(null);
		
		liveListData.add(new String[]{"1","A股"});
		liveListData.add(new String[]{"2","港股"});
		liveListData.add(new String[]{"3","美股"});
		liveListData.add(new String[]{"4","基金"});
		liveListData.add(new String[]{"5","贵金属"});
		myAdapter = new OptionCategoryAdapter(this);
		myListView.setAdapter(myAdapter);
		select = getResources().getDrawable(R.drawable.icon_check_selected);
		myListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if(id >=0 && id < liveListData.size()){
					String[] item = liveListData.get((int)id);
					Intent data = new Intent();
					data.putExtra("category_index", item[0]);
					data.putExtra("category_name", item[1]);
					setResult(SELECT_RESPONSE_CODE, data);
					finish();
				}
			}
		});

	}

	class OptionCategoryAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public OptionCategoryAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return liveListData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return liveListData.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.opinion_category_item, parent, false);
				viewHolder = new ViewHolder();
				viewHolder.categoryName = (TextView) convertView.findViewById(R.id.category_name);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			String[] itemData = liveListData.get(position);
			viewHolder.categoryName.setText(itemData[1]);
			viewHolder.categoryName.setTag(itemData[0]);
			if(!StringUtils.isEmpty(currIndex) && currIndex.equals(itemData[0])){
				viewHolder.categoryName.setCompoundDrawablesWithIntrinsicBounds(null, null, select, null);
			}else{
				viewHolder.categoryName.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
			}
			return convertView;
		}

		class ViewHolder {
			TextView categoryName;
		}
	}

}
