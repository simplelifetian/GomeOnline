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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.activity.AttentionDetailActivity;
import com.gome.haoyuangong.activity.ViewInvesterInfoActivity;
import com.gome.haoyuangong.log.Logger;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.tougu.OpinionListBean;
import com.gome.haoyuangong.net.volley.ImageLoader;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.utils.DateUtils;
import com.gome.haoyuangong.utils.StringUtils;

/**
 * 
 */
public class DTOpinionListFragment extends XListFragment {

	private static final String TAG = DTOpinionListFragment.class.getName();

	public static final String BUNDLE_PARAM_FROM = "param_from";
	public static final String BUNDLE_PARAM_URL = "param_url";
	public static final String BUNDLE_PARAM_PAGE_DOWN = "page_down";
	public static final String BUNDLE_PARAM_PAGE_UP = "page_up";

	private MyListAdapter myAdapter;

	private List<OpinionListBean.OpinionItem> dataList = new ArrayList<OpinionListBean.OpinionItem>();

	// private int currPage = 0;
	private int pageSize = 20;
	private String direction;
	private String directionDown = "down";
	private String directionUp = "up";
	private long pointId = 0;

	private ImageLoader imageLoader;

	private String fromName;
	private String opinionUrl;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if (args != null) {
			fromName = args.getString(BUNDLE_PARAM_FROM);
			opinionUrl = args.getString(BUNDLE_PARAM_URL);
			directionDown = args.getString(BUNDLE_PARAM_PAGE_DOWN);
			directionUp = args.getString(BUNDLE_PARAM_PAGE_UP);
		}
		direction = directionDown;
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
						startActivity(aIntent);
					} else {
						Toast.makeText(mActivity, "无效观点", Toast.LENGTH_SHORT).show();
					}

				}
			}
		});

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
			aViewHolder.userRole.setText(itemData.getAdviserInfo().getCategory() == 1 ? "财经名人" : (itemData.getAdviserInfo().getCategory() == 2 ? "投资顾问"
					: ""));
			aViewHolder.userCompany.setText(itemData.getAdviserInfo().getCompany());
			aViewHolder.time.setText(DateUtils.getTimeAgoString(itemData.getPointInfo().getCtime(), "MM-dd HH:mm"));
			aViewHolder.opinionTitle.setText(itemData.getPointInfo().getTitle());
			aViewHolder.opinionContent.setText(itemData.getPointInfo().getSummary());
			aViewHolder.comment.setText(""+itemData.getPointInfo().getCommentNum());
			aViewHolder.support.setText(""+itemData.getPointInfo().getLikeNum());
			if (1 == itemData.getPointInfo().getLimits()) {
				aViewHolder.imageLimit.setVisibility(View.GONE);
			} else {
				aViewHolder.imageLimit.setVisibility(View.VISIBLE);
			}
			if (1 == itemData.getAdviserInfo().getIsVerify()) {
				aViewHolder.ivCertif.setVisibility(View.GONE);
			} else {
				aViewHolder.ivCertif.setVisibility(View.VISIBLE);
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
				imageLoader.downLoadImage(itemData.getPointInfo().getPointPic(), aViewHolder.opImage[0],R.drawable.icon_img_error,R.drawable.icon_img_error);
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

	@Override
	protected <T> Request<T> getRequest() {
		// TODO Auto-generated method stub
		String url = opinionUrl.replace("_pageSize", "" + pageSize).replace("_direction", direction)
				.replace("_pointId", "" + pointId);

		Logger.error(TAG, url);
		JsonRequest request = new JsonRequest<OpinionListBean>(Method.GET, url, null, OpinionListBean.class);
		return request;
	}

	@Override
	protected void onRefreshPrepear() {
		// TODO Auto-generated method stub
//		if (dataList.isEmpty()) {
//			pointId = 0;
//			direction = directionDown;
//		} else {
//			pointId = dataList.get(0).getId();
//			direction = directionUp;
//		}
		pointId = 0;
		direction = directionDown;
	}

	@Override
	protected void onLoadMorePrepear() {
		// TODO Auto-generated method stub
		if (dataList.isEmpty()) {
			pointId = 0;
			direction = directionDown;
		} else {
			pointId = dataList.get(dataList.size() - 1).getPageId();
			direction = directionDown;
		}
	}

	@Override
	protected void onReceive(boolean isLoadMore, String id, Object data) {
		// TODO Auto-generated method stub
		OpinionListBean livelistBean = (OpinionListBean) data;
		if (!isLoadMore) {
			dataList.clear();
		}
		dataList.addAll(livelistBean.getData().getList());
		myAdapter.notifyDataSetChanged();
		if(livelistBean.getData().getList().size() < pageSize){
			mList.setPullLoadEnable(false);
		}else{
			mList.setPullLoadEnable(true);
		}
	}

}
