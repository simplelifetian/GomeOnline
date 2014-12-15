package com.gome.haoyuangong.fragments;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.activity.InvestGroupActivity;
import com.gome.haoyuangong.activity.InvestOpinionActivity;
import com.gome.haoyuangong.activity.MyAttentionsActivity;
import com.gome.haoyuangong.activity.MyFansActivity;
import com.gome.haoyuangong.activity.MySelfInfoActivity;
import com.gome.haoyuangong.interfaces.OnDataRefreshListener;
import com.gome.haoyuangong.interfaces.OnListViewItemClickListener;
import com.gome.haoyuangong.layout.self.SelfView;
import com.gome.haoyuangong.layout.self.SelfView.BusinessType;
import com.gome.haoyuangong.layout.self.SelfView.IItemClicked;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.sortlistview.ActivityAddFriends;
import com.gome.haoyuangong.views.RefreshListView;
import com.gome.haoyuangong.views.xlistview.XListView.IXListViewListener;

public class ListViewFragment extends BaseFragment implements OnDataRefreshListener ,IXListViewListener,OnListViewItemClickListener {
	
	private RefreshListView _freshView;
	protected View emptyView;
	protected boolean showloading = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initComponent();
		emptyView = LayoutInflater.from(this.getActivity()).inflate(R.layout.empty, null);
		emptyView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showloading = true;
				requestData(true);
			}
		});
		((TextView)emptyView.findViewById(R.id.empty_txt)).setText("暂时没有数据，点击刷新");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {	
		ViewGroup vg = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);
//		initComponent();
		_freshView.setModuleTag(getModuleTag());
		_freshView.setOnDateRefreshListener(this);
		_freshView.setOnListViewItemClickListener(this);
		setContent(_freshView);
		hideTitle();
		return vg;
	}
	protected void setEmptyText(String text){
		if (emptyView == null)
			return;
		((TextView)emptyView.findViewById(R.id.empty_txt)).setText(text);
	}
	protected void showEmptyView() {
//		_freshView.setVisibility(View.INVISIBLE);
//		emptyView.setVisibility(View.VISIBLE);
		content.removeAllViews();
		setContent(emptyView);
		hideTitle();
	}
	protected void showDataView() {
//		emptyView.setVisibility(View.INVISIBLE);
//		_freshView.setVisibility(View.VISIBLE);
		content.removeAllViews();
		setContent(_freshView);
		hideTitle();
	}
	protected void requestData(boolean pull){
		
	}
	protected void addItem(Object obj){
		_freshView.addItem(obj);
	}
	protected void remove(int index) {
		_freshView.remove(index);
	}
	protected void remove(Object obj) {
		_freshView.remove(obj);
	}
	protected String getModuleTag(){
		return this.getClass().getName();
	}
	@SuppressWarnings("unchecked")
	protected <T> T getItemAt(int index) {
		return (T)_freshView.getItemAt(index);
	}
	protected int getCount(){
		return _freshView.getCount();
	}
	protected void addItem(Object obj,int index){
		_freshView.addItem(obj,index);
	}
	private void initComponent(){
		_freshView = new RefreshListView(getContext());
	}
	protected void setAdapter(BaseAdapter adapter){
		_freshView.setAdapter(adapter);
	}
	protected void setOnDateRefreshListener(OnDataRefreshListener onDateRefreshListener) {
		_freshView.setOnDateRefreshListener(onDateRefreshListener);
	}
	protected void reFresh(){
		_freshView.reFresh();
	}
	protected void setPullRefreshEnable(boolean enable){
		_freshView.setPullRefreshEnable(enable);
	}
	protected void setPullLoadEnable(boolean visible){
		_freshView.setPullLoadEnable(visible);
	}
	protected void addHeadView(View view){
		_freshView.addHeadView(view);
	}
	protected void hideHead(){
		_freshView.hideHead();
	}
	protected void setDividerHeight(int height){
		_freshView.setDividerHeight(height);
	}
	protected void clear(){
		_freshView.clear();
	}
	protected void stopRefresh(){
		_freshView.stopFresh();
	}
	protected void stopLoadMore() {
		_freshView.stopLoadMore();
	}
	@Override
	public void OnStartRefresh() {
		// TODO Auto-generated method stub
//		Toast.makeText(getContext(), "OnStartRefresh",Toast.LENGTH_SHORT).show();
	}

	@Override
	public void OnStartLoadMore() {
		// TODO Auto-generated method stub
//		Toast.makeText(getContext(), "OnStartLoadMore",Toast.LENGTH_SHORT).show();
	}
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnListViewItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		
	}

}
