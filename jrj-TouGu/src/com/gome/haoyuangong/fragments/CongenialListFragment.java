package com.gome.haoyuangong.fragments;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.activity.LoginActivity;
import com.gome.haoyuangong.activity.OpenConsultingActivity;
import com.gome.haoyuangong.activity.ViewInvesterInfoActivity;
import com.gome.haoyuangong.adapter.MyBaseAdapter2;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.TouguBaseResult;
import com.gome.haoyuangong.net.result.congenia.CongenialResult;
import com.gome.haoyuangong.net.result.congenia.CongenialResult.Item;
import com.gome.haoyuangong.net.url.NetUrlMyInfo;
import com.gome.haoyuangong.net.volley.ImageLoader;
import com.gome.haoyuangong.net.volley.JsonRequest;

@EFragment
public class CongenialListFragment extends XListFragment {

	private static final String TAG = CongenialListFragment.class.getName();

	public static final int OPT_HOT = 0;// /功能常量 热门
	public static final int OPT_SATISFACTION = 1;// /功能常量 满意度
	public static final int OPT_FANS = 2;// /功能常量 粉丝量

	public static final String BUNDLE_PARAM_FROM = "param_from";
	public static final String BUNDLE_PARAM_URL = "param_url";
	
	public static final String REFRESH_CONGENIAL_LIST = "congenial_list";

	private int id = 0;

	ArrayList<CongenialResult.Item> congenialListData;
	MyCongenialAdapter adapter;

	private ImageLoader imageLoader;

	private int curPage = 1;

	private String direction = "f";


	private String type;
	class ReceiveBroadCast extends BroadcastReceiver  
    {  
            @Override  
            public void onReceive(Context context, Intent intent)  
            {  
                //得到广播中得到的数据，并显示出来  
                int itemKey = intent.getExtras().getInt(ViewInvesterInfoActivity.ITEM_KEY);  
                int optionKey = intent.getExtras().getInt(ViewInvesterInfoActivity.OPTION_STATE);  
                CongenialResult.Item ex=congenialListData.get(itemKey);
                if(ViewInvesterInfoActivity.UNATENTION==optionKey){
                	ex.setAttentionFlag(2);
                }else if(ViewInvesterInfoActivity.ATENTION==optionKey){
                	ex.setAttentionFlag(1);
                }
//                congenialListData.remove(itemKey);
//                congenialListData.add(itemKey, ex);
//                Message msg=Message.obtain();
//				msg.what=1;
//				msg.arg1=itemKey;
//				mHandler.sendMessage(msg);
                adapter.notifyDataSetChanged();
            }  
    }  
	private ReceiveBroadCast receiveBroadCast;
	
	

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		/** 注册广播 */  
        receiveBroadCast = new ReceiveBroadCast();  
        IntentFilter filter = new IntentFilter();  
        filter.addAction(ViewInvesterInfoActivity.ATENTION_ACTION_NAME);      
        activity.registerReceiver(receiveBroadCast, filter);  
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		getActivity().unregisterReceiver(receiveBroadCast); 
	}

	public void setType(String type) {
		this.type = type;
	}

	@AfterViews
	void viewsInjectComplete() {
		congenialListData = new ArrayList<CongenialResult.Item>();

		imageLoader = new ImageLoader(mActivity);
		adapter = new MyCongenialAdapter(getActivity(), congenialListData,mList);
		mList.setAdapter(adapter);
		mList.setRefreshTime(getRefreshTime(REFRESH_CONGENIAL_LIST));
		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(mActivity,
						ViewInvesterInfoActivity.class);
				intent.putExtra("USERNAME", congenialListData.get((int) id)
						.getUserName());
				intent.putExtra("USERID", congenialListData.get((int) id)
						.getLoginId());
				intent.putExtra(ViewInvesterInfoActivity.ITEM_KEY, (int)id);
				startActivity(intent);
			}
		});
		hideTitle();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.title_right1:
			break;
		case R.id.title_right2:

			break;
		case R.id.search:
			break;

		default:
			break;
		}
	}

	@SuppressLint("HandlerLeak")
	class MyCongenialAdapter extends MyBaseAdapter2<CongenialResult.Item> {

		public MyCongenialAdapter(Context context, List<Item> list,
				ListView listView) {
			super(context, list, listView);
		}

		private Handler mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					int itemIndex=msg.arg1;
					updateView(1,itemIndex);// 用我们自己写的方法
				}
				// notifyDataSetChanged();不用了
			}

		};

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = ViewHolder.getInstance(context, convertView,
					parent, R.layout.item_congenial_list);
			if (convertView == null) {
				convertView = holder.getView();
				convertView.setTag(holder);
			}

			CongenialResult.Item data = (CongenialResult.Item) getItem(position);

			ImageView talkButton = (ImageView) holder.getView(R.id.talk_button);
			final String userName = data.getUserName();
			final String userLonginId = data.getLoginId();
			talkButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (UserInfo.getInstance().isLogin()) {
						if (userLonginId == UserInfo.getInstance().getUserId()) {
							showToast("自己不能咨询自己哟！");
						} else {
							Intent intent = new Intent(mActivity,
									OpenConsultingActivity.class);
							intent.putExtra(OpenConsultingActivity.BUNDLE_TYPE,
									OpenConsultingActivity.SPECIAL_CONSULTING);
							intent.putExtra(
									OpenConsultingActivity.BUNDLE_PARAM_NAME,
									userName);
							intent.putExtra(
									OpenConsultingActivity.BUNDLE_PARAM_ID,
									userLonginId);
							startActivity(intent);
						}

					} else {
						Intent intent = new Intent(mActivity,
								LoginActivity.class);
						intent.putExtra(
								LoginActivity.BUNDLE_PARAM_TARGET_ACTIVITY,
								"com.jrj.tougu.activity.OpenConsultingActivity");
						intent.putExtra(OpenConsultingActivity.BUNDLE_TYPE,
								OpenConsultingActivity.SPECIAL_CONSULTING);
						intent.putExtra(
								OpenConsultingActivity.BUNDLE_PARAM_NAME,
								userName);
						intent.putExtra(OpenConsultingActivity.BUNDLE_PARAM_ID,
								userLonginId);
						startActivity(intent);
					}
				}

			});

			final int attentionFlag=data.getAttentionFlag();
			ImageView addButton = (ImageView) holder.getView(R.id.add_icon);
			if (UserInfo.getInstance().isLogin()) {
				if (userLonginId.equals(UserInfo.getInstance().getUserId())) {
					addButton.setVisibility(View.INVISIBLE);
				}else{
					addButton.setVisibility(View.VISIBLE);
				}
			}else{
				addButton.setVisibility(View.VISIBLE);
			}
			if (attentionFlag == 1) {
				addButton
						.setBackgroundResource(R.drawable.android_icon_add_old);
			} else {
				addButton
						.setBackgroundResource(R.drawable.android_icon_add_new);
			}
			final int itemIndex = position;
			addButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (!UserInfo.getInstance().isLogin()) {
						Intent intent = new Intent(mActivity,
								LoginActivity.class);
						startActivityForResult(intent, 1);
					} else {
						if(attentionFlag != 1){
							doAttention(itemIndex, userLonginId);
						}
					}

				}

			});
			ImageView userIcon = holder.getView(R.id.user_icon);
			TextView userNameTextView = holder.getView(R.id.user_name);
			userNameTextView.setText(data.getUserName());
			TextView labelTextView1 = holder.getView(R.id.labelTextView1);
//			labelTextView1.setText(data.getPosition());
			labelTextView1.setText(data.getTypeDesc());
			TextView labelTextView2 = holder.getView(R.id.labelTextView2);

			TextView labelTextView3 = holder.getView(R.id.labelTextView3);
			labelTextView3.setText(data.getCompany());
			
			TextView labelTextView4 = holder.getView(R.id.labelTextView4);
			// labelTextView4.setText();
			TextView labelTextView5 = holder.getView(R.id.labelTextView5);
			labelTextView5.setText(data.getInvestDirection());
			// labelTextView5.setText(String.valueOf(data.getUseSatisfaction())+"%");
			TextView labelTextView6 = holder.getView(R.id.labelTextView6);
			labelTextView6.setText(String.valueOf(data.getAnswerNumber()));
			TextView labelTextView7 = holder.getView(R.id.labelTextView7);
			labelTextView7.setText(String.valueOf(data.getFansNum()));
			id = data.getId();
			if (data.getHeadImage() == null) {
				System.out.println("********** 1" + data.getHeadImage());
			} else
				imageLoader.downLoadImage(data.getHeadImage(), userIcon);
			if (data.getExperienceScope() == 1) {
				labelTextView2.setText("1年以下");
			} else if (data.getExperienceScope() == 2) {
				labelTextView2.setText("1-3年");
			} else if (data.getExperienceScope() == 3) {
				labelTextView2.setText("3-5年");
			} else {
				labelTextView2.setText("5年以上");
			}
			TextView labelTextView8 = holder.getView(R.id.loveTextView);
			labelTextView8.setText(String.valueOf(data.getUseSatisfaction())+"%");
			TextView labelTextView9 = holder.getView(R.id.messageTextView);
			labelTextView9.setText(String.valueOf(data.getAnswerNumber()));
			TextView labelTextView0 = holder.getView(R.id.peopleTextView);
			labelTextView0.setText(String.valueOf(data.getFansNum()));
			return convertView;
		}

		private void doAttention(final int itemIndex, String userId) {
			String url = "";
			// if (attended)
			// url = String.format(NetUrlMyInfo.ATTENTION,
			// userId,UserInfo.getInstance().getUserId(),0);
			// else
			url = String.format(NetUrlMyInfo.ATTENTION, userId, UserInfo
					.getInstance().getUserId(), 1);
			JsonRequest<TouguBaseResult> request = new JsonRequest<TouguBaseResult>(
					Method.GET, url,null,
					new RequestHandlerListener<TouguBaseResult>(getContext()) {

						@Override
						public void onStart(Request request) {
							super.onStart(request);
							// showDialog(request);
						}

						@Override
						public void onEnd(Request request) {
							super.onEnd(request);
							// hideDialog(request);
						}

						@SuppressLint("ShowToast")
						@Override
						public void onSuccess(String id, TouguBaseResult data) {
							// TODO Auto-generated method stub
//							try {
//								if (data.getRetCode() == 2) {
////									Message msg=Message.obtain();
////									msg.what=1;
////									msg.arg1=itemIndex;
////									mHandler.sendMessage(msg);
//								}
//								
////								Toast.makeText(mActivity, data.getMsg(),
////										Toast.LENGTH_SHORT).show();
//							} catch (Exception e) {
////								Toast.makeText(mActivity, e.toString(),
////										Toast.LENGTH_SHORT).show();
//							}
							Toast.makeText(mActivity,data.getMsg(), Toast.LENGTH_SHORT).show();

						}
						
						@Override
						public void onFailure(String id, int code, String str,Object obj) {
							if(obj != null && obj instanceof TouguBaseResult){
								TouguBaseResult data = (TouguBaseResult)obj;
								if(data.getRetCode() == 2){
									CongenialResult.Item itemData = (CongenialResult.Item) getItem(itemIndex);
									itemData.setAttentionFlag(1);
									adapter.notifyDataSetChanged();
									Toast.makeText(mActivity, "关注成功", Toast.LENGTH_SHORT).show();
									com.gome.haoyuangong.MyApplication.get().setNewConcent(true);
								}else{
									if(data.getRetCode() == -3){
										CongenialResult.Item itemData = (CongenialResult.Item) getItem(itemIndex);
										itemData.setAttentionFlag(1);
										adapter.notifyDataSetChanged();
									}
									super.onFailure(id, code, str, obj);
								}
							}else{
								super.onFailure(id, code, str, obj);
							}
						}
					}, TouguBaseResult.class);
			send(request);
		}

		public void updateView(int status, int itemIndex) {
			// TODO Auto-generated method stub

			// 得到第一个可显示控件的位置，
			int visiblePosition = mListView.getFirstVisiblePosition();
			// 只有当要更新的view在可见的位置时才更新，不可见时，跳过不更新
			if (itemIndex - visiblePosition >= 0) {
				// 得到要更新的item的view
				View view = mListView.getChildAt(itemIndex - visiblePosition);
				ViewHolder holder = (ViewHolder) view.getTag();
				ImageView addButton = (ImageView) holder.getView(R.id.add_icon);
				if (status == 1) {
					addButton
							.setBackgroundResource(R.drawable.android_icon_add_old);
//					addButton.invalidateDrawable(mActivity.getResources().getDrawable(R.drawable.android_icon_add_old));
				} else {
					addButton
							.setBackgroundResource(R.drawable.android_icon_add_new);
				}
				view.postInvalidate();
			}

		}

	}

	@Override
	protected <T> Request<T> getRequest() {
		// TODO Auto-generated method stub
		String URL = "http://mapi.itougu.jrj.com.cn/wireless/account/adviserRankingList/%s/?ps=%d&d=%s&id=%d&userId=%s";
		String url;
		if (!UserInfo.getInstance().isLogin()) {
			url = String.format(URL, type, 10, direction, id, "0");
		} else {
			url = String.format(URL, type, 10, direction, id, UserInfo
					.getInstance().getUserId());
		}
     Log.e(TAG, url);
		JsonRequest<CongenialResult> request = new JsonRequest<CongenialResult>(
				Method.GET, url, null, CongenialResult.class);
		return (Request<T>) request;
	}

	@Override
	protected void onRefreshPrepear() {
		// TODO Auto-generated method stub
		id = 0;
		direction = "f";
	}

	@Override
	protected void onLoadMorePrepear() {
		// TODO Auto-generated method stub
		direction = "f";
	}

	@Override
	protected void onReceive(boolean isLoadMore, String id, Object data) {
		// TODO Auto-generated method stub
		if (data instanceof CongenialResult) {
			fillCongeniaData(isLoadMore,((CongenialResult) data).getData().getList());
			if (!isLoadMore) {
				mList.stopRefresh();
			} else {
				mList.stopLoadMore();
			}
		}
	}

	private void fillCongeniaData(boolean isLoadMore,List<CongenialResult.Item> list) {
		saveRefreshTime(REFRESH_CONGENIAL_LIST);
		mList.setRefreshTime(getRefreshTime(REFRESH_CONGENIAL_LIST));
		if (curPage == 1) {
			congenialListData.clear();
			mList.setPullLoadEnable(true);
		}
		if (!isLoadMore) {
			congenialListData.clear();
			mList.setPullLoadEnable(true);
		}
		congenialListData.addAll(list);
		curPage++;
		if (list.size() > 0 && list.size() < 10 || list.size() <= 0) {
			mList.setPullLoadEnable(false);
		}
		adapter.notifyDataSetChanged();

	}


}
