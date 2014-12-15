package com.gome.haoyuangong.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.views.xlistview.XListView;
import com.gome.haoyuangong.views.xlistview.XListView.IXListViewListener;

public abstract class XListFragment extends BaseFragment implements IXListViewListener {
	private static final int LIST_TYPE_LOAD=0;
	private static final int LIST_TYPE_REFRESH=1;
	private static final int LIST_TYPE_LOADMORE=2;
	protected XListView mList;
	private String refreshId;
	private String loadMoreId;
	private int type;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup vg = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.simple_fragment_list, null);
		setContent(v);
		init(v);
		return vg;
	}

	public void init(View v) {
		hideTitle();
		mList = (XListView) v.findViewById(R.id.list);
		mList.setXListViewListener(this);
	}

	
	private void  load(){
		Request request = getRequest();
		final RequestHandlerListener listener = request.getListener();
		request.setListener(new RequestHandlerListener(getContext()) {

			@Override
			public void onSuccess(String id, Object data) {
				onReceive(id.equals(loadMoreId),id, data);
				if (listener != null) {
					listener.onSuccess(id, data);
				}
			}

			@Override
			public void onFailure(String id, int code, String str,Object obj) {
				super.onFailure(id, code, str,obj);
				if (listener != null) {
					listener.onFailure(id, code, str,obj);
				}
			}

			@Override
			public void onStart(Request request) {
				super.onStart(request);
				handleStart(request);
				if (listener != null) {
					listener.onStart(request);
				}
			}

			@Override
			public void onEnd(Request request) {
				super.onEnd(request);
				handleEnd(request);
				if (listener != null) {
					listener.onEnd(request);
				}

			}

			@Override
			public void onProgress(String url, int size, int allCount) {
				super.onProgress(url, size, allCount);
				if (listener != null) {
					listener.onProgress(url, size, allCount);
				}
			}
		});

		send(request);
	}
	private void handleStart(Request request){
		if(type==LIST_TYPE_LOAD){
			showLoading(request);
		}else if(type==LIST_TYPE_REFRESH){
			refreshId = request.getId();
		}else if(type==LIST_TYPE_LOADMORE){
			loadMoreId = request.getId();
		}
		
	}
	private void handleEnd(Request request){
		if(type==LIST_TYPE_LOAD){
			hideLoading(request);
		}else{
			if(request.getId().equals(refreshId)){
				mList.stopRefresh();
				refreshId=null;
			}else if(request.getId().equals(loadMoreId)){
				mList.stopLoadMore();
				loadMoreId=null;
			}
		}
	}
	
	@Override
	protected void onLoad() {
		type=LIST_TYPE_LOAD;
		onRefreshPrepear();
		load();
	}
	
	@Override
	public void onRefresh() {
		type=LIST_TYPE_REFRESH;
		onRefreshPrepear();
		load();
	}

	@Override
	public void onLoadMore() {
		type=LIST_TYPE_LOADMORE;
		onLoadMorePrepear();
		load();
	}

	abstract protected <T> Request<T> getRequest();

	abstract protected void onRefreshPrepear();

	abstract protected void onLoadMorePrepear();
	
	abstract protected void onReceive(boolean isLoadMore, String id, Object data);
	
}
