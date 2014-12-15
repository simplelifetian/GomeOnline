/**
 * 
 */
package com.gome.haoyuangong.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.activity.AttentionDetailActivity;
import com.gome.haoyuangong.fragments.CommentListFragment.MyListAdapter.CommentViewHolder;
import com.gome.haoyuangong.views.xlistview.XListView;

/**
 * 
 */
public class FenSiListFragment extends BaseFragment {

	private static final String TAG = FenSiListFragment.class.getName();

	private XListView listView;

	private MyListAdapter myAdapter;

	private List<Object> dataList = new ArrayList<Object>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dataList.add(0);
		dataList.add(0);
		dataList.add(0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.simple_refresh_listview_layout_withtoph, null);
		findView(v);
		return v;
	}

	private void findView(View v) {
		
		listView = (XListView) v.findViewById(R.id.listView);
		listView.setDivider(null);
//		listView.setonRefreshListener(new OnRefreshListener() {
//			@Override
//			public void onRefresh() {
//				
//			}
//		});
		myAdapter = new MyListAdapter();
		listView.setAdapter(myAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				position -= 1;
				if (position >= 0 && position < dataList.size()) {
					
				}
			}
		});
	}

	class MyListAdapter extends BaseAdapter {

		private LayoutInflater layoutInflater;

		public MyListAdapter() {
			layoutInflater = LayoutInflater.from(mActivity);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return dataList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return dataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			ViewHolder aViewHolder;
			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.dongtai_message_item_simple, parent, false);
				aViewHolder = new ViewHolder();
				aViewHolder.itemTitle = (TextView)convertView.findViewById(R.id.item_title);
				convertView.setTag(aViewHolder);
			} else {
				aViewHolder = (ViewHolder) convertView.getTag();
			}
			
			aViewHolder.itemTitle.setText("酸梅子 关注了我");
			
			return convertView;
		}

		class ViewHolder {
			TextView itemTitle;
		}
		
	}
}
