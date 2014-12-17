package com.gome.haoyuangong.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.adapter.MyBaseAdapter;
import com.gome.haoyuangong.bean.ResultsIndicatorsDataBean;

public class ResultsFragment extends BaseFragment {
	
	private TextView mInComing;
	private TextView mInComingNum;
	private TextView mRankings;
	private TextView mProgressSize;
	private TextView mRankingsDescribe;
	private ProgressBar mProgress;
	private ListView mListView;
	private static final int RESULTS_TYPE_SOLD_PRODUCTS = 1;
	private static final int RESULTS_TYPE_SOLD_CUSTOMER = 2;
	private static final int RESULTS_TYPE_COMMUNICATION = 3;
	private static final int RESULTS_TYPE_GIFT = 4;
	private static final int RESULTS_TYPE_PROMOTE = 5;
	private ResultsFragmentAdapter mAdatper;
	private List<ResultsIndicatorsDataBean> mListData;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mListData = new ArrayList<ResultsIndicatorsDataBean>();
		for (int i = 0; i < 5; i++) {
			ResultsIndicatorsDataBean item = new ResultsIndicatorsDataBean();
			item.setType(i+1);
			item.setOverSize(30);
			item.setSize(i);
			mListData.add(item);
		}
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View parent = super.onCreateView(inflater, container, savedInstanceState);
		View child = inflater.inflate(R.layout.results_fragment, container, false);
		content.addView(child);
		initTitle();
		initView(parent);
		return parent;
	}
	
	private void initTitle() {
		titleLeft1.setVisibility(View.VISIBLE);
		titleLeft1.setBackgroundResource(R.drawable.actionbar_icon_me);
		titleCenter.setText(getResources().getString(R.string.actionbar_bottom_results));
		titleRight1.setBackgroundResource(R.drawable.icon_trophy);
	}
	
	private void initView(View v) {
		mInComing = (TextView) v.findViewById(R.id.tv_incoming);
		mInComingNum = (TextView) v.findViewById(R.id.tv_incoming_num);
		mRankings = (TextView) v.findViewById(R.id.tv_rankings);
		mProgressSize = (TextView) v.findViewById(R.id.tv_progress_size);
		mRankingsDescribe = (TextView) v.findViewById(R.id.tv_randkings_describe);
		mProgress = (ProgressBar) v.findViewById(R.id.progress_rankings);
		mListView = (ListView) v.findViewById(R.id.lv);
		
		mInComing.setText(getResources().getString(R.string.text_incoming, 11));
		mInComingNum.setText(getResources().getString(R.string.text_incoming_size, "68,000,000"));
		mRankings.setText(getResources().getString(R.string.text_rankings, 320));
		mProgressSize.setText(getResources().getString(R.string.text_progress_size, 770));
		mRankingsDescribe.setText(getResources().getString(R.string.text_rankings_describe, 80, 180));
		mProgress.setProgress(19);
		
		mAdatper = new ResultsFragmentAdapter(getContext(), mListData);
		mListView.setAdapter(mAdatper);
		
	}

	class ResultsFragmentAdapter extends MyBaseAdapter<ResultsIndicatorsDataBean>{

		public ResultsFragmentAdapter(Context context,
				List<ResultsIndicatorsDataBean> list) {
			super(context, list);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = ViewHolder.getInstance(context, convertView,
					parent, R.layout.results_indicators_list_item);
			if (convertView == null) {
				convertView = holder.getView();
				convertView.setTag(holder);
			}
			
			ResultsIndicatorsDataBean itemData = (ResultsIndicatorsDataBean) getItem(position);
			ImageView mIcon = (ImageView) holder.getView().findViewById(R.id.iv_icon);
			TextView mOverSize = (TextView) holder.getView().findViewById(R.id.tv_compare);
			TextView mSizeDescribte = (TextView) holder.getView().findViewById(R.id.tv_size_describte);
			TextView mNum = (TextView) holder.getView().findViewById(R.id.tv_num);
			Button mBtn = (Button) holder.getView().findViewById(R.id.btn);
			View.OnClickListener clickListener = new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				}
			};
			mBtn.setOnClickListener(clickListener);
			if(itemData.getType() == RESULTS_TYPE_SOLD_PRODUCTS) {
				mIcon.setImageResource(R.drawable.icon_sold_products);
				mOverSize.setText(getResources().getString(R.string.text_over_size, itemData.getOverSize() + "%"));
				mSizeDescribte.setText(getResources().getString(R.string.text_describte_sold_products));
				mBtn.setText(getResources().getString(R.string.text_btn_add_product));
				mNum.setText(getResources().getString(R.string.text_pieces, itemData.getSize() + 107));
				
			} else if(itemData.getType() == RESULTS_TYPE_SOLD_CUSTOMER) {
				mIcon.setImageResource(R.drawable.icon_sold_customers);
				mOverSize.setText(getResources().getString(R.string.text_over_size, itemData.getOverSize() + "%"));
				mSizeDescribte.setText(getResources().getString(R.string.text_describte_sold_customers));
				mBtn.setText(getResources().getString(R.string.text_btn_add_people));
				mNum.setText(getResources().getString(R.string.text_people, itemData.getSize() + 10));
				
			} else if(itemData.getType() == RESULTS_TYPE_GIFT) {
				mIcon.setImageResource(R.drawable.icon_gift);
				mOverSize.setText(getResources().getString(R.string.text_over_size, itemData.getOverSize() + "%"));
				mSizeDescribte.setText(getResources().getString(R.string.text_describte_gift));
				mBtn.setText(getResources().getString(R.string.text_btn_gift));
				mNum.setText(getResources().getString(R.string.text_time, itemData.getSize() + 4));
				
			} else if(itemData.getType() == RESULTS_TYPE_COMMUNICATION) {
				mIcon.setImageResource(R.drawable.icon_communication);
				mOverSize.setText(getResources().getString(R.string.text_over_size, itemData.getOverSize() + "%"));
				mSizeDescribte.setText(getResources().getString(R.string.text_describte_communication));
				mBtn.setText(getResources().getString(R.string.text_btn_communication));
				mNum.setText(getResources().getString(R.string.text_time, itemData.getSize() + 30));
				
			} else if(itemData.getType() == RESULTS_TYPE_PROMOTE) {
				mIcon.setImageResource(R.drawable.icon_promote);
				mOverSize.setText(getResources().getString(R.string.text_over_size, itemData.getOverSize() + "%"));
				mSizeDescribte.setText(getResources().getString(R.string.text_describte_promute));
				mBtn.setText(getResources().getString(R.string.text_btn_promute));
				mNum.setText(getResources().getString(R.string.text_percentage, itemData.getSize() + 10.7) + "%");
				
			}
			
			return convertView;
		}
	}
}
