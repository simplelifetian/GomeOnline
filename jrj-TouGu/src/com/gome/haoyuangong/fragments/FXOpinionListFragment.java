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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.activity.AttentionDetailActivity;
import com.gome.haoyuangong.activity.OpinionShareActivity;
import com.gome.haoyuangong.activity.ReplyActivity;
import com.gome.haoyuangong.views.xlistview.XListView;

/**
 * 
 */
public class FXOpinionListFragment extends BaseFragment {

	private static final String TAG = FXOpinionListFragment.class.getName();

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
				convertView = layoutInflater.inflate(R.layout.opinion_listitem_faxian, parent, false);
				aViewHolder = new AttentionViewHolder();
				aViewHolder.zan = (LinearLayout) convertView.findViewById(R.id.zan_lo);
				aViewHolder.comment = (LinearLayout) convertView.findViewById(R.id.comment_lo);
				aViewHolder.share = (LinearLayout) convertView.findViewById(R.id.share_lo);
				convertView.setTag(aViewHolder);
			} else {
				aViewHolder = (AttentionViewHolder) convertView.getTag();
			}
			
			aViewHolder.zan.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(mActivity, "赞成功", Toast.LENGTH_SHORT).show();
				}
			});
			aViewHolder.comment.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(mActivity,ReplyActivity.class);
					startActivity(intent);
				}
			});
			
			aViewHolder.share.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(mActivity, OpinionShareActivity.class);
					startActivity(intent);
				}
			});

			return convertView;
		}

		class AttentionViewHolder {
			LinearLayout zan;
			LinearLayout comment;
			LinearLayout share;
		}
		
	}
}
