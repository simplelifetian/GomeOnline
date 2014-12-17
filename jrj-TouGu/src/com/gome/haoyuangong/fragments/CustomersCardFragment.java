package com.gome.haoyuangong.fragments;

import java.util.ArrayList;
import java.util.List;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.adapter.MyBaseAdapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CustomersCardFragment extends BaseFragment {
	private EditText mEtSearch;
	private ImageView mIvDelete;
	private ImageView mIvSearch;
	private ListView mCardList;
	private ListView mCardFooterList;
	private LayoutInflater mInflater;
	private List<String> mListData;
	//private List<Integer> mListFooterData;
	private CustomerCardFragmentAdapter mAdapter;
	//private CustomerListFooterFragmentAdapter mListFooterAdaper;
	private View mView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mListData = new ArrayList<String>();
//		mListFooterData = new ArrayList<Integer>();
//		for (int i = 0; i < 4; i++) {
//			mListFooterData.add(i);
//		}
		mListData.add("电器行业");
		mListData.add("国美门店");
		mListData.add("门店周边");
		mListData.add("住址周边");
		
		mInflater = LayoutInflater.from(getContext());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View parent = super.onCreateView(inflater, container, savedInstanceState);
		View child = inflater.inflate(R.layout.customer_card_fragment, container, false);
		content.addView(child);
		titleWhole.setVisibility(View.GONE);
		initView(child);
		return parent;
	}
	
	private void initView(View v) {
		hideTitle();
		mView = mInflater.inflate(R.layout.customer_card_list_footer, null);
		mEtSearch = (EditText) v.findViewById(R.id.et_search);
		mIvDelete = (ImageView) v.findViewById(R.id.iv_delete);
		mIvSearch = (ImageView) v.findViewById(R.id.iv_search);
		mCardList = (ListView) v.findViewById(R.id.lv);
		mAdapter = new CustomerCardFragmentAdapter(getContext(), mListData);
		mCardList.setAdapter(mAdapter);
		
//		mCardFooterList = (ListView) mView.findViewById(R.id.lv_footer);
//		mListFooterAdaper = new CustomerListFooterFragmentAdapter(getContext(), mListFooterData);
//		mCardFooterList.setAdapter(mListFooterAdaper);
//		if (mCardList.getFooterViewsCount() == 0){
			mCardList.addFooterView(mView);
//		}
	}

	class CustomerCardFragmentAdapter extends MyBaseAdapter<String>{

		public CustomerCardFragmentAdapter(Context context,
				List<String> list) {
			super(context, list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = ViewHolder.getInstance(context, convertView,
					parent, R.layout.customer_card_list_item);
			if (convertView == null) {
				convertView = holder.getView();
				convertView.setTag(holder);
			}
			TextView name = (TextView) holder.getView().findViewById(R.id.tv_card_type);
			name.setText(getItem(position) + "");
			return convertView;
		}
	}
	
//	class CustomerListFooterFragmentAdapter extends MyBaseAdapter<Integer>{
//
//		public CustomerListFooterFragmentAdapter(Context context,
//				List<Integer> list) {
//			super(context, list);
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			ViewHolder holder = ViewHolder.getInstance(context, convertView,
//					parent, R.layout.customer_card_footer_item);
//			if (convertView == null) {
//				convertView = holder.getView();
//				convertView.setTag(holder);
//			}
//			ImageView icon = (ImageView) holder.getView().findViewById(R.id.iv_icon);
//			TextView name = (TextView) holder.getView().findViewById(R.id.tv_card_footer_name);
//			if(mListFooterData.get(position) == 0) {
//				icon.setImageResource(R.drawable.icon_add_new);
//				name.setText("新的客户");
//			}
//			else if(mListFooterData.get(position) == 1) {
//				icon.setImageResource(R.drawable.icon_group);
//				name.setText("群聊");
//			}
//			else if(mListFooterData.get(position) == 2) {
//				icon.setImageResource(R.drawable.icon_public_num);
//				name.setText("公众号");
//			}
//			else if(mListFooterData.get(position) == 3) {
//				icon.setImageResource(R.drawable.icon_cooperation);
//				name.setText("合作商户");
//			}	
//			return convertView;
//		}
//	}
	
}
