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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.activity.AttentionDetailActivity;
import com.gome.haoyuangong.activity.ImageViewerActivity;
import com.gome.haoyuangong.activity.LoginActivity;
import com.gome.haoyuangong.activity.ViewInvesterInfoActivity;
import com.gome.haoyuangong.log.Logger;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.tougu.OpinionListBean;
import com.gome.haoyuangong.net.url.NetUrlTougu;
import com.gome.haoyuangong.net.volley.ImageLoader;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.utils.DateUtils;
import com.gome.haoyuangong.utils.StringUtils;
import com.gome.haoyuangong.views.xlistview.XListView;
import com.gome.haoyuangong.views.xlistview.XListView.IXListViewListener;

/**
 * 
 */
public class GZOpinionListFragment extends BaseFragment implements IXListViewListener {

	private static final String TAG = GZOpinionListFragment.class.getName();

	public static final String BUNDLE_PARAM_FROM = "param_from";
	public static final String BUNDLE_PARAM_URL = "param_url";
	public static final String BUNDLE_PARAM_PAGE_DOWN = "page_down";
	public static final String BUNDLE_PARAM_PAGE_UP = "page_up";
	
	private static final int LIST_TYPE_REFRESH=1;
	private static final int LIST_TYPE_LOADMORE=2;
	private static final int LIST_TYPE_FIRSTLOAD=3;
	protected XListView mList;

	private MyListAdapter myAdapter;

	private List<OpinionListBean.OpinionItem> dataList = new ArrayList<OpinionListBean.OpinionItem>();

	// private int currPage = 0;
	private int pageSize = 20;
	private String directionMore = "f";
	private String direction = directionMore;
	private long pointId = 0;

	private ImageLoader imageLoader;

	private String fromName = "关注";
	private String opinionUrl;
	
	private FrameLayout imgLayout;
	private FrameLayout nodataLayout;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup vg = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.guanzhu_fragment_list, null);
		setContent(v);
		init(v);
		return vg;
	}
	
	public void init(View v) {
		hideTitle();
		mList = (XListView) v.findViewById(R.id.list);
		mList.setXListViewListener(this);
		imgLayout = (FrameLayout)v.findViewById(R.id.img_layout);
		imgLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mActivity,LoginActivity.class);
				startActivity(intent);
			}
		});
		nodataLayout = (FrameLayout)v.findViewById(R.id.nodata_layout);
		nodataLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				nodataLayout.setVisibility(View.GONE);
				getGZOptions(LIST_TYPE_FIRSTLOAD);
			}
		});
		imgLayout.setVisibility(View.GONE);
		nodataLayout.setVisibility(View.GONE);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		imageLoader = new ImageLoader(mActivity);
		myAdapter = new MyListAdapter();
		mList.setAdapter(myAdapter);
		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if (id >= 0 && id < dataList.size()) {
					OpinionListBean.OpinionItem d = dataList.get((int) id);
					if (d != null) {
						Intent aIntent = new Intent(mActivity, AttentionDetailActivity.class);
						aIntent.putExtra(AttentionDetailActivity.BUNDLE_PARAM_TITLE, fromName + "详情");
						aIntent.putExtra(AttentionDetailActivity.BUNDLE_PARAM_ID, d.getPointInfo().getId());
						aIntent.putExtra(AttentionDetailActivity.BUNDLE_PARAM_DETAIL_URL, d.getPointInfo().getWebUrl());
						aIntent.putExtra(AttentionDetailActivity.BUNDLE_PARAM_PRAISE_C, d.getPointInfo().getLikeNum());
						aIntent.putExtra(AttentionDetailActivity.BUNDLE_PARAM_TOUGUID, d.getAdviserInfo().getUserId());
						aIntent.putExtra(AttentionDetailActivity.BUNDLE_PARAM_OPTITLE, d.getPointInfo().getTitle());
						aIntent.putExtra(AttentionDetailActivity.BUNDLE_PARAM_LIMIT, d.getPointInfo().getLimits());
						aIntent.putExtra(AttentionDetailActivity.BUNDLE_PARAM_TOUGUNAME, d.getAdviserInfo().getUserName());
						startActivity(aIntent);
					} else {
						Toast.makeText(mActivity, "无效观点", Toast.LENGTH_SHORT).show();
					}

				}
			}
		});

	}
	
	@Override
	public void onResume(){
		super.onResume();
		if(UserInfo.getInstance().isLogin()){
			imgLayout.setVisibility(View.GONE);
			nodataLayout.setVisibility(View.GONE);
			mList.setVisibility(View.VISIBLE);
			if(com.gome.haoyuangong.MyApplication.get().needGzRefresh(UserInfo.getInstance().isLogin())){
				getGZOptions(LIST_TYPE_FIRSTLOAD);
			}
			
		}else{
			dataList.clear();
			myAdapter.notifyDataSetChanged();
			com.gome.haoyuangong.MyApplication.get().setLogined(UserInfo.getInstance().isLogin());
			imgLayout.setVisibility(View.VISIBLE);
			mList.setVisibility(View.GONE);
			nodataLayout.setVisibility(View.GONE);
		}
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
				convertView = layoutInflater.inflate(R.layout.opinion_listitem_dongtai, parent, false);
				aViewHolder = new AttentionViewHolder();
				aViewHolder.userIcon = (ImageView) convertView.findViewById(R.id.user_icon);
				aViewHolder.userName = (TextView) convertView.findViewById(R.id.user_name);
				aViewHolder.userRole = (TextView) convertView.findViewById(R.id.user_role);
				aViewHolder.userCompany = (TextView) convertView.findViewById(R.id.user_company);
				aViewHolder.time = (TextView) convertView.findViewById(R.id.time);
				aViewHolder.opinionTitle = (TextView) convertView.findViewById(R.id.opinion_title);
				aViewHolder.opinionContent = (TextView) convertView.findViewById(R.id.opinion_content);
				aViewHolder.support = (TextView) convertView.findViewById(R.id.support);
				aViewHolder.comment = (TextView)convertView.findViewById(R.id.comment);
				aViewHolder.imageLimit = (ImageView) convertView.findViewById(R.id.image_limit);
				aViewHolder.ivCertif = (ImageView) convertView.findViewById(R.id.iv_certif);

				aViewHolder.opImagesLayout = (LinearLayout) convertView.findViewById(R.id.op_images_layout);
//				ViewGroup.LayoutParams vgl = aViewHolder.opImagesLayout.getLayoutParams();
//				vgl.height = aViewHolder.opImagesLayout.getMeasuredWidth() / 3;
//				aViewHolder.opImagesLayout.setLayoutParams(vgl);
				aViewHolder.opImage[0] = (ImageView) convertView.findViewById(R.id.op_image_1);
				aViewHolder.opImage[1] = (ImageView) convertView.findViewById(R.id.op_image_2);
				aViewHolder.opImage[2] = (ImageView) convertView.findViewById(R.id.op_image_3);
				convertView.setTag(aViewHolder);
			} else {
				aViewHolder = (AttentionViewHolder) convertView.getTag();
			}

			final OpinionListBean.OpinionItem itemData = dataList.get(position);
			aViewHolder.userName.setText(itemData.getAdviserInfo().getUserName());
			aViewHolder.userRole.setText(itemData.getAdviserInfo().getTypeDesc());
			aViewHolder.userCompany.setText(itemData.getAdviserInfo().getCompany());
			aViewHolder.time.setText(DateUtils.getTimeAgoString(itemData.getPointInfo().getCtime(), "MM-dd HH:mm"));
			aViewHolder.opinionTitle.setText(StringUtils.replaceAllTag(itemData.getPointInfo().getTitle()));
			aViewHolder.opinionContent.setText(StringUtils.replaceAllTag(itemData.getPointInfo().getSummary()));
			aViewHolder.comment.setText(""+itemData.getPointInfo().getCommentNum());
			aViewHolder.support.setText(""+itemData.getPointInfo().getLikeNum());
			if (1 == itemData.getPointInfo().getLimits()) {
				aViewHolder.imageLimit.setVisibility(View.GONE);
			} else {
				aViewHolder.imageLimit.setVisibility(View.VISIBLE);
				aViewHolder.opinionContent.setText(R.string.limit_tip);
			}
			if (1 == itemData.getAdviserInfo().getIsVerify()) {
				aViewHolder.ivCertif.setVisibility(View.VISIBLE);
			} else {
				aViewHolder.ivCertif.setVisibility(View.GONE);
			}
			if(StringUtils.isEmpty(itemData.getAdviserInfo().getHeadImage())){
				aViewHolder.userIcon.setImageResource(R.drawable.icon_head_default);
			}else{
				imageLoader.downLoadImage(itemData.getAdviserInfo().getHeadImage(), aViewHolder.userIcon);
			}
			

			if (StringUtils.isEmpty(itemData.getPointInfo().getPointPic())) {
				aViewHolder.opImagesLayout.setVisibility(View.GONE);
				for (ImageView image : aViewHolder.opImage) {
					image.setImageBitmap(null);
				}
			} else {
				aViewHolder.opImagesLayout.setVisibility(View.VISIBLE);
				imageLoader.downLoadImage(itemData.getPointInfo().getPointPic(), aViewHolder.opImage[0],R.drawable.point_default_icon,R.drawable.point_default_icon);
				aViewHolder.opImage[0].setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(mActivity,ImageViewerActivity.class);
						intent.putExtra(ImageViewerActivity.BUNDLE_PARAM_FILEPATH, itemData.getPointInfo().getPointPic());
						startActivity(intent);
					}
				});
			}
			
			aViewHolder.userIcon.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(mActivity,ViewInvesterInfoActivity.class);
					intent.putExtra("USERNAME", itemData.getAdviserInfo().getUserName());
					intent.putExtra("USERID", itemData.getAdviserInfo().getUserId());
					startActivity(intent);
				}
			});
//			convertView.requestLayout();
			
//			imageLoader.downLoadImage(itemData.getPointPic(), aViewHolder.opImage[0]);
			return convertView;
		}

		class AttentionViewHolder {
			ImageView userIcon;
			TextView userName;
			TextView userRole;
			TextView userCompany;
			TextView time;
			TextView opinionTitle;
			TextView opinionContent;
			TextView support;
			TextView comment;
			ImageView imageLimit;
			ImageView ivCertif;

			LinearLayout opImagesLayout;
			ImageView opImage[] = new ImageView[3];
		}

	}

//	@Override
//	protected <T> Request<T> getRequest() {
//		// TODO Auto-generated method stub
//		String url = opinionUrl.replace("_pageSize", "" + pageSize).replace("_direction", direction)
//				.replace("_pointId", "" + pointId);
//
//		Logger.error(TAG, url);
//		JsonRequest request = new JsonRequest<OpinionListBean>(Method.GET, url, null, OpinionListBean.class);
//		return request;
//	}
//
//	@Override
//	protected void onRefreshPrepear() {
//		// TODO Auto-generated method stub
////		if (dataList.isEmpty()) {
////			pointId = 0;
////			direction = directionDown;
////		} else {
////			pointId = dataList.get(0).getId();
////			direction = directionUp;
////		}
//		pointId = 0;
//		direction = directionDown;
//	}
//
//	@Override
//	protected void onLoadMorePrepear() {
//		// TODO Auto-generated method stub
//		if (dataList.isEmpty()) {
//			pointId = 0;
//			direction = directionDown;
//		} else {
//			pointId = dataList.get(dataList.size() - 1).getPageId();
//			direction = directionDown;
//		}
//	}
//
//	@Override
//	protected void onReceive(boolean isLoadMore, String id, Object data) {
//		// TODO Auto-generated method stub
//		OpinionListBean livelistBean = (OpinionListBean) data;
//		if (!isLoadMore) {
//			dataList.clear();
//		}
//		dataList.addAll(livelistBean.getData().getList());
//		myAdapter.notifyDataSetChanged();
//		if(livelistBean.getData().getList().size() < pageSize){
//			mList.setPullLoadEnable(false);
//		}else{
//			mList.setPullLoadEnable(true);
//		}
//	}
	
	private void getGZOptions(final int loadType){
		if(loadType == LIST_TYPE_REFRESH){
			pointId = 0;
			direction = "b";
		}
		if(!UserInfo.getInstance().isLogin()){
			return;
		}
		String url = NetUrlTougu.OPINION_GUANZHU.replace("_userid", UserInfo.getInstance().getUserId()).replace("_pageSize", "" + pageSize).replace("_direction", direction).replace("_pointId", String.valueOf(pointId));
		Logger.error(TAG, url);
		JsonRequest request = new JsonRequest<OpinionListBean>(Method.GET, url,null,
				new RequestHandlerListener<OpinionListBean>(getContext()) {

					@Override
					public void onStart(Request request) {
						super.onStart(request);
						if(LIST_TYPE_FIRSTLOAD == loadType){
							showLoading(request);
						}
						
					}

					@Override
					public void onEnd(Request request) {
						super.onEnd(request);
						if(LIST_TYPE_FIRSTLOAD == loadType){
							hideLoading(request);
						}
						if(loadType == LIST_TYPE_REFRESH){
							mList.stopRefresh();
						}else{
							mList.stopLoadMore();
						}
					}

					@Override
					public void onSuccess(String id, OpinionListBean data) {
						// TODO Auto-generated method stub
						
						saveRefreshTime(NetUrlTougu.OPINION_GUANZHU);
						mList.setRefreshTime(getRefreshTime(NetUrlTougu.OPINION_GUANZHU));
						
						OpinionListBean livelistBean = (OpinionListBean) data;
						if (loadType == LIST_TYPE_REFRESH || LIST_TYPE_FIRSTLOAD == loadType) {
							dataList.clear();
						}
						dataList.addAll(livelistBean.getData().getList());
						myAdapter.notifyDataSetChanged();
						if(loadType == LIST_TYPE_REFRESH){
							mList.stopRefresh();
						}else{
							mList.stopLoadMore();
						}
						if(livelistBean.getData().getList().size() < pageSize){
							mList.setPullLoadEnable(false);
						}else{
							mList.setPullLoadEnable(true);
						}
						
						if(dataList.isEmpty()){
							nodataLayout.setVisibility(View.VISIBLE);
							mList.setVisibility(View.GONE);
						}else{
							nodataLayout.setVisibility(View.GONE);
							mList.setVisibility(View.VISIBLE);
						}
						
					}
				}, OpinionListBean.class);

		send(request);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		pointId = 0;
		direction = directionMore;
		getGZOptions(LIST_TYPE_REFRESH);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		if (dataList.isEmpty()) {
			pointId = 0;
			direction = directionMore;
			getGZOptions(LIST_TYPE_REFRESH);
		} else {
//			pointId = pointId + 1;
			pointId = dataList.get(dataList.size() - 1).getPageId();
			direction = directionMore;
			getGZOptions(LIST_TYPE_LOADMORE);
		}
		
	}
	
	@Override
	protected void onLoad() {
		pointId = 0;
		direction = directionMore;
		com.gome.haoyuangong.MyApplication.get().setLogined(UserInfo.getInstance().isLogin());
		getGZOptions(LIST_TYPE_FIRSTLOAD);
	}

}
