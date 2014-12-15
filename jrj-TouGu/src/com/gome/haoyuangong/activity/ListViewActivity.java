package com.gome.haoyuangong.activity;

import java.util.ArrayList;
import java.util.List;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.interfaces.OnDataRefreshListener;
import com.gome.haoyuangong.layout.self.SideBar;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.views.PinYinListView;
import com.gome.haoyuangong.views.RefreshListView;
import com.gome.haoyuangong.views.RefreshListView.OnSendDataListener;
import com.gome.haoyuangong.views.xlistview.XListView;
import com.gome.haoyuangong.views.xlistview.XListView.IXListViewListener;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ListViewActivity extends BaseActivity implements OnDataRefreshListener,IXListViewListener {
	
	private RefreshListView _freshView;
	protected View emptyView;
	private boolean isPinYinList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String viewtype = getIntent().getStringExtra("viewtype");
		isPinYinList = !(TextUtils.isEmpty(viewtype) || !viewtype.equals("pinyin"));
		initComponent();	
		_freshView.setModuleTag(getModuleTag());
		content.removeAllViews();	
		setOnDateRefreshListener(this);
		content.addView(_freshView);
		emptyView = LayoutInflater.from(this).inflate(R.layout.empty, null);
		emptyView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				requestData(true);
			}
		});
		((TextView)emptyView.findViewById(R.id.empty_txt)).setText("暂时没有数据，点击刷新");
	}
	protected void requestData(boolean pull){
		
	}
	protected void setEmptyText(String text){
		if (emptyView == null)
			return;
		((TextView)emptyView.findViewById(R.id.empty_txt)).setText(text);
	}
	protected void showEmptyView() {
		content.removeAllViews();
		content.addView(emptyView);
	}
	protected void showDataView() {
		content.removeAllViews();
		content.addView(_freshView);
	}
	private void initComponent(){
		_freshView = new RefreshListView(this);	
		_freshView.setPinYinList(isPinYinList);
	}
	protected String getModuleTag(){
		return this.getClass().getName();
	}
	protected void setAdapter(BaseAdapter adapter){
		_freshView.setAdapter(adapter);
	}
	protected SideBar getSideBar(){
		return _freshView.getSideBar();
	}
	protected XListView getListView(){
		return _freshView.getListView();
	}
	protected void setOnDateRefreshListener(OnDataRefreshListener onDateRefreshListener) {
		_freshView.setOnDateRefreshListener(onDateRefreshListener);
	}
	protected void setOnSendDataListener(OnSendDataListener onSendDataListener) {
		_freshView.setOnSendDataListener(onSendDataListener);
	}
	protected void reFresh(){
		_freshView.reFresh();
	}
	protected void addItem(Object obj){
		_freshView.addItem(obj);
	}
	protected void addItem(Object obj,int index){
		_freshView.addItem(obj,index);
	}
	protected int indexOf(Object obj){
		return _freshView.indexOf(obj);
	}
	protected void clear() {
		_freshView.clear();
	}
	protected void stopRefresh(){
		_freshView.stopFresh();
	}
	protected void stopLoadMore() {
		_freshView.stopLoadMore();
	}
	protected void setPullRefreshEnable(boolean enable){
		_freshView.setPullRefreshEnable(enable);
	}
	protected void setPullLoadEnable(boolean visible){
		_freshView.setPullLoadEnable(visible);
	}
	protected void addListViewHeadView(View view) {
		_freshView.addListViewHeadView(view);
	}
	protected void addHeadView(View view){
		_freshView.addHeadView(view);
	}
	protected void hideHead(){
		_freshView.hideHead();
	}
	protected void setDividerHeight(int height) {
		_freshView.setDividerHeight(height);
	}
	public void setPinYinDatas(List<String> items){
		_freshView.setDatas(items);
	}
	@Override
	public void OnStartRefresh() {
		// TODO Auto-generated method stub
//		Toast.makeText(getApplicationContext(), "OnStartRefresh",Toast.LENGTH_SHORT).show();
	}

	@Override
	public void OnStartLoadMore() {
		// TODO Auto-generated method stub
//		Toast.makeText(getApplicationContext(), "OnStartLoadMore",Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}

}
