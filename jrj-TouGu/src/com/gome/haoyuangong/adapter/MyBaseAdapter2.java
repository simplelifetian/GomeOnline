package com.gome.haoyuangong.adapter;

import java.util.List;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

public abstract class MyBaseAdapter2<T> extends BaseAdapter {
	protected List<T> mList;
	protected Context context;
	protected ListView mListView;
	public MyBaseAdapter2(Context context,List<T> list) {
		mList = list;
		this.context = context;
	}
	
	public MyBaseAdapter2(Context context,List<T> list,ListView listView) {
		mList = list;
		this.context = context;
		mListView=listView;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}


	static public class ViewHolder {
		private final SparseArray<View> views;
		private View convertView;
		
		private ViewHolder(Context context, View convertView, ViewGroup parent, int layoutId) {
			views = new SparseArray<View>();
			this.convertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
		}
		private ViewHolder(View v){
			views = new SparseArray<View>();
			this.convertView = v;
		}
		static public ViewHolder getInstance(Context context, View convertView, ViewGroup parent, int layoutId){
			if(convertView==null){
				ViewHolder holder = new ViewHolder(context, convertView, parent, layoutId);
				return holder;
			}else{
				return (ViewHolder) convertView.getTag();
			}
		}
		static public ViewHolder getInstance(View v){
				ViewHolder holder = new ViewHolder(v);
				return holder;
		}
		public View getView(){
			return convertView;
		}

		@SuppressWarnings("unchecked")
		public <T extends View> T getView(int viewId) {
			View view = views.get(viewId);
			if (view == null) {
				view = convertView.findViewById(viewId);
				views.put(viewId, view);
			}
			return (T) view;
		}
	}

}
