/**
 * 
 */
package com.gome.haoyuangong.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gome.haoyuangong.R;

/**
 * 
 */
public class AaaViewActivity extends BaseActivity {

	private static final String TAG = AaaViewActivity.class.getName();

	private ListView myListView;

	private List<Object> commentData = new ArrayList<Object>();

	private AttentionAdapter myAdapter;
	
//	private LinearLayout share;
//	private LinearLayout comment;
//	private LinearLayout zan;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_refresh_listview_layout);
		setTitle("回答");
		myListView = (ListView) findViewById(R.id.listView);
		myAdapter = new AttentionAdapter(this);
		myListView.setAdapter(myAdapter);
		
		myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent(AaaViewActivity.this,com.jrj.tougu.activity.AskDetailActivity_.class);
//				startActivity(intent);
			}
		});
		
//		share = (LinearLayout)findViewById(R.id.share_lo);
//		share.setOnClickListener(this);
//		comment = (LinearLayout)findViewById(R.id.comment_lo);
//		comment.setOnClickListener(this);
//		zan = (LinearLayout)findViewById(R.id.zan_lo);
//		zan.setOnClickListener(this);

		commentData.add(1);
		commentData.add(1);
		commentData.add(1);
		commentData.add(1);
	}

	class AttentionAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public AttentionAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return commentData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return commentData.get(position);
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
//				convertView = mInflater.inflate(R.layout.message_listitem_aaa_tougu, parent, false);
				
				convertView = mInflater.inflate(R.layout.message_listitem_aaa_user, parent, false);
				
				viewHolder = new ViewHolder();
				viewHolder.reask = (LinearLayout) convertView.findViewById(R.id.lo_reask);
				viewHolder.comment = (LinearLayout) convertView.findViewById(R.id.lo_comment);
				viewHolder.tvAnswer = (TextView) convertView.findViewById(R.id.tv_answer);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
//			viewHolder.reask.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					Toast.makeText(AaaViewActivity.this, "追问", Toast.LENGTH_SHORT).show();
//				}
//			});
//			viewHolder.comment.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					Intent intent = new Intent(AaaViewActivity.this, ReplyActivity.class);
//					startActivity(intent);
//				}
//			});
			
			if(viewHolder.tvAnswer != null){
				viewHolder.tvAnswer.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(AaaViewActivity.this,ReplyActivity.class);
						intent.putExtra(ReplyActivity.BUNDLE_TYPE, ReplyActivity.TYPE_ANSWER);
						startActivity(intent);
					}
				});
			}
			return convertView;
		}

		class ViewHolder {
			LinearLayout reask;
			LinearLayout comment;
			
			TextView tvAnswer;
		}
	}
	
}
