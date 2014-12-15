package com.gome.haoyuangong.fragments;

import java.util.ArrayList;
import java.util.List;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.activity.AttentionDetailActivity;
import com.gome.haoyuangong.activity.CommentActionActivity;
import com.gome.haoyuangong.views.xlistview.XListView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SearchCongenialRecordListFragment extends BaseFragment {

	private static final String TAG = SearchCongenialRecordListFragment.class.getName();

	private XListView listView;

	private MyListAdapter myAdapter;

	private List<Object> dataList = new ArrayList<Object>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dataList.add(0);
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
		final View v1=mActivity.getLayoutInflater().inflate(R.layout.item_search_congenial_list_header, null, false);
		listView.addHeaderView(v1);
		RelativeLayout mRelativeLayout=(RelativeLayout)v1.findViewById(R.id.first);
		mRelativeLayout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				listView.removeHeaderView(v1);
			}
			
		});
//		ImageView imageView=(ImageView)v1.findViewById(R.id.delete_item);
//		imageView.setOnClickListener(new OnClickListener(){
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				listView.removeHeaderView(v1);
//			}
//			
//		});
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
				if (position >= 0 && position < dataList.size()) {
					Intent aIntent = new Intent(mActivity, AttentionDetailActivity.class);
					startActivity(aIntent);
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
			
			AttentionViewHolder aViewHolder;
			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.item_congenial_list, parent, false);
				aViewHolder = new AttentionViewHolder();
				
				convertView.setTag(aViewHolder);
			} else {
				aViewHolder = (AttentionViewHolder) convertView.getTag();
			}
			

			return convertView;
		}

		class AttentionViewHolder {
			TextView support;
			TextView comment;
			TextView share;
		}
		
	}
}
