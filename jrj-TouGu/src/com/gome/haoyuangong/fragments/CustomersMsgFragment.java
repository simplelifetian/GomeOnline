package com.gome.haoyuangong.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.adapter.MyBaseAdapter;
import com.gome.haoyuangong.bean.CustomerMsgDataBean;
import com.gome.haoyuangong.utils.DataUtilsAther;

public class CustomersMsgFragment extends BaseFragment {
	private EditText mEtSearch;
	private ImageView mIvDelete;
	private ImageView mIvSearch;
	private ListView mMsgList;
	private List<CustomerMsgDataBean> mListData;
	private CustomerMsgFragmentAdapter mAdapter;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mListData = new ArrayList<CustomerMsgDataBean>();
		for (int i = 0; i < 7; i++) {
			CustomerMsgDataBean item = new CustomerMsgDataBean();
			item.setTime(System.currentTimeMillis() + i*300000);
			item.setType(i);
			mListData.add(item);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View parent = super.onCreateView(inflater, container, savedInstanceState);
		View child = inflater.inflate(R.layout.customer_msg_fragment, container, false);
		content.addView(child);
		titleWhole.setVisibility(View.GONE);
		initView(child);
		return parent;
	}
	
	private void initView(View v) {
		hideTitle();
		mEtSearch = (EditText) v.findViewById(R.id.et_search);
		mIvDelete = (ImageView) v.findViewById(R.id.iv_delete);
		mIvSearch = (ImageView) v.findViewById(R.id.iv_search);
		mMsgList = (ListView) v.findViewById(R.id.lv);
		
		mAdapter = new CustomerMsgFragmentAdapter(getContext(), mListData);
		mMsgList.setAdapter(mAdapter);
	}

	class CustomerMsgFragmentAdapter extends MyBaseAdapter<CustomerMsgDataBean>{

		public CustomerMsgFragmentAdapter(Context context,
				List<CustomerMsgDataBean> list) {
			super(context, list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = ViewHolder.getInstance(context, convertView,
					parent, R.layout.customer_msg_list_item);
			if (convertView == null) {
				convertView = holder.getView();
				convertView.setTag(holder);
			}
			
			CustomerMsgDataBean itemData = (CustomerMsgDataBean) getItem(position);
			ImageView icon = (ImageView) holder.getView().findViewById(R.id.iv_icon);
			TextView name = (TextView) holder.getView().findViewById(R.id.tv_name);
			TextView msgType = (TextView) holder.getView().findViewById(R.id.tv_msg_type);
			TextView time = (TextView) holder.getView().findViewById(R.id.tv_time);
			time.setText(DataUtilsAther.getTime(itemData.getTime(), DataUtilsAther.HOURS_24_MAKE));
			if(itemData.getType() == 1) {
				msgType.setText(getResources().getString(R.string.text_msg_one));
				icon.setImageResource(R.drawable.icon_msg_one);
				name.setText("张三");
			} 
			else if(itemData.getType() == 2) {
				msgType.setText(getResources().getString(R.string.text_msg_two));
				icon.setImageResource(R.drawable.icon_msg_two);
				name.setText("田老师");
			}
			else if(itemData.getType() == 3) {
				msgType.setText(getResources().getString(R.string.text_msg_three));
				icon.setImageResource(R.drawable.icon_msg_three);
				name.setText("王晓明");
			}
			else if(itemData.getType() == 4) {
				msgType.setText(getResources().getString(R.string.text_msg_three));
				icon.setImageResource(R.drawable.icon_msg_four);
				name.setText("李连杰");
			}
			else if(itemData.getType() == 5) {
				msgType.setText(getResources().getString(R.string.text_msg_three));
				icon.setImageResource(R.drawable.icon_msg_five);
				name.setText("张曼玉");
			}
			else if(itemData.getType() == 6) {
				msgType.setText(getResources().getString(R.string.text_msg_three));
				icon.setImageResource(R.drawable.icon_msg_six);
				name.setText("杨颖");
			}
			else if(itemData.getType() == 0) {
				msgType.setText(getResources().getString(R.string.text_msg_three));
				icon.setImageResource(R.drawable.icon_msg_zero);
				name.setText("侃侃");
			}
			return convertView;
		}
	}
}
