package com.gome.haoyuangong.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.activity.SearchCongenialResultActivity;
import com.gome.haoyuangong.activity.SearchContentResultActivity;
import com.gome.haoyuangong.activity.SearchQuesstionAnswerResultActivity;
import com.gome.haoyuangong.adapter.MyBaseAdapter;

public class SearchHistoryRecordFragment extends BaseFragment {

	private static final String TAG = SearchHistoryRecordFragment.class
			.getName();

	public static final int CONTENT_ATTENTION = 0;
	public static final int CONTENT_MESSAGE = 1;
	public static final int CONTENT_OPINION = 2;
	public static final int CONTENT_INFO = 3;

	@SuppressWarnings("rawtypes")
	private SearchHistoryRecordListAdapter myAdapter;

	private ListView listView;

	private String userInput = "";

	private List<String> dataList = new ArrayList<String>();

	OnRecentSearchHistoryRecordClickedListener mOnRecentSearchHistoryRecordClickedListener = msOnRecentSearchHistoryRecordClickedListener;
	private static OnRecentSearchHistoryRecordClickedListener msOnRecentSearchHistoryRecordClickedListener = new OnRecentSearchHistoryRecordClickedListener() {

		@Override
		public void onRecordItemClicked(String serarchKey) {
			// TODO Auto-generated method stub

		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View parent = super.onCreateView(inflater, container,
				savedInstanceState);
		View child = inflater.inflate(
				R.layout.search_history_record_listview_layout, container,
				false);
		content.addView(child);
		initChildTitle();
		findView(parent);
		return parent;
	}

	private void initChildTitle() {
		hideTitle();
		EditText inputView = (EditText) getActivity().findViewById(
				R.id.SearchInput);

		inputView.addTextChangedListener(textWatcher);

	}

	@SuppressWarnings("unchecked")
	private void findView(View v) {
		listView = (ListView) v
				.findViewById(R.id.search_history_record_listview);
		dataList.add("搜索包含“”的观点");
		dataList.add("搜索包含“”的投顾");
		dataList.add("搜索包含“”的问题");
		myAdapter = new SearchHistoryRecordListAdapter<String>(mActivity,
				dataList);
		listView.setAdapter(myAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// mOnRecentSearchHistoryRecordClickedListener
				// .onRecordItemClicked(dataList.get(position));

				if ("".equals(userInput) || userInput == null) {
					showToast("请输入搜索内容");
				} else {
					if (position == 0) {
						Intent intent = new Intent(mActivity,
								SearchContentResultActivity.class);
						intent.putExtra("KEYWORD", userInput);
						startActivity(intent);
					} else if (position == 1) {
						Intent intent = new Intent(mActivity,
								SearchCongenialResultActivity.class);
						intent.putExtra("KEYWORD", userInput);
						startActivity(intent);
					} else {
						Intent intent = new Intent(mActivity,
								SearchQuesstionAnswerResultActivity.class);
						intent.putExtra("KEYWORD", userInput);
						startActivity(intent);
					}
				}
			}
		});
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		if (activity instanceof OnRecentSearchHistoryRecordClickedListener) {
			mOnRecentSearchHistoryRecordClickedListener = (OnRecentSearchHistoryRecordClickedListener) activity;
		} else {
			throw new ClassCastException(
					activity.toString()
							+ " must implementmOnRecentSearchHistoryRecordClickedListener");
		}
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		mOnRecentSearchHistoryRecordClickedListener = msOnRecentSearchHistoryRecordClickedListener;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {

		default:
			break;
		}
	}

	class SearchHistoryRecordListAdapter<T> extends MyBaseAdapter<T> {
		List<T> mlist;

		public SearchHistoryRecordListAdapter(Context ctx, List<T> list) {
			super(ctx, list);
			mlist = list;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = ViewHolder
					.getInstance(context, convertView, parent,
							R.layout.search_history_record_listview_item_layout);
			if (convertView == null) {
				convertView = holder.getView();
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			TextView textView = (TextView) convertView
					.findViewById(R.id.search_history_record_listview_item_textview);

			String searchKey = mlist.get(position).toString();

			Log.d("wangwei_test", "**1*** " + searchKey);

			if (null != searchKey && searchKey.contains(userInput)) {
				Log.d("wangwei_test", "**2*** " + userInput);
				int index = searchKey.indexOf(userInput);

				int len = userInput.length();

				Spanned temp = Html.fromHtml(searchKey.substring(0, index)
						// + "<u><font color=#FF0000>"
						+ "<u><font color=#3082c6>"
						+ searchKey.substring(index, index + len)
						+ "</font></u>"
						+ searchKey.substring(index + len, searchKey.length()));

				textView.setText(temp);
			} else {
				textView.setText(searchKey);
			}

			return convertView;

		}

	}

	public interface OnRecentSearchHistoryRecordClickedListener {
		public void onRecordItemClicked(String serarchKey);
	}

	private TextWatcher textWatcher = new TextWatcher() {

		String beforeInput = null;

		@Override
		public void afterTextChanged(Editable edit) {
			// TODO Auto-generated method stub
		}

		@Override
		public void beforeTextChanged(CharSequence input, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			beforeInput = input.toString();
		}

		@Override
		public void onTextChanged(CharSequence input, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			Log.d("wangwei_test", input.toString() + " test");
			userInput = input.toString();
			if (userInput.length() > 0) {
				dataList.remove("搜索包含“”的观点");
				dataList.remove("搜索包含“”的投顾");
				dataList.remove("搜索包含“”的问题");
				dataList.add("搜索包含“" + userInput + "”的观点");
				dataList.add("搜索包含“" + userInput + "”的投顾");
				dataList.add("搜索包含“" + userInput + "”的问题");
				dataList.remove("搜索包含“" + beforeInput + "”的观点");
				dataList.remove("搜索包含“" + beforeInput + "”的投顾");
				dataList.remove("搜索包含“" + beforeInput + "”的问题");
			} else {
				dataList.remove("搜索包含“" + beforeInput + "”的观点");
				dataList.remove("搜索包含“" + beforeInput + "”的投顾");
				dataList.remove("搜索包含“" + beforeInput + "”的问题");
				dataList.add("搜索包含“”的观点");
				dataList.add("搜索包含“”的投顾");
				dataList.add("搜索包含“”的问题");
			}

			myAdapter.notifyDataSetChanged();
		}

	};

}
