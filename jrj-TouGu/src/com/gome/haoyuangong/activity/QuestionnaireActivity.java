package com.gome.haoyuangong.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.BaseSignResult;
import com.gome.haoyuangong.net.result.TouguBaseResult;
import com.gome.haoyuangong.net.result.Questionnaire.QuestionnarieBean;
import com.gome.haoyuangong.net.result.Questionnaire.QuestionnarieData;
import com.gome.haoyuangong.net.url.NetUrlMyInfo;
import com.gome.haoyuangong.net.volley.ImageLoader;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.views.xlistview.XListView;
import com.gome.haoyuangong.views.xlistview.XListView.IXListViewListener;

public class QuestionnaireActivity extends BaseActivity implements IXListViewListener{

	private static final String TAG = QuestionnaireActivity.class.getName();
	
	public static final String REFRESH_QUESTIONNAIRE_LIST = "questionnaire_list";

	private XListView listView;

	private ImageLoader imageLoader;

	private MyAdapter myAdapter;

	private ScrollView mScrollView;

	private Button mButton;

	private List<QuestionnarieData> mCongeniaListData = new ArrayList<QuestionnarieData>();

	private TranslateAnimation mShowAction;

	private TranslateAnimation mHiddenAction;

	private RadioGroup radioGroup;
	private RadioGroup radioGroup2;
	private RadioGroup radioGroup3;

	private RadioButton radioButton;
	private RadioButton radioButton2;
	private RadioButton radioButton3;

	private String radioButtonString;
	private String radioButtonString2;
	private String radioButtonString3;
	
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
	
	public void updateView(int status, int itemIndex) {
		// TODO Auto-generated method stub

		// 得到第一个可显示控件的位置，
		int visiblePosition = listView.getFirstVisiblePosition();
		// 只有当要更新的view在可见的位置时才更新，不可见时，跳过不更新
		if (itemIndex - visiblePosition >= 0) {
			// 得到要更新的item的view
			View view = listView.getChildAt(itemIndex - visiblePosition);
			ViewHolder holder = (ViewHolder) view.getTag();
			ImageView addButton = (ImageView) holder.addIcon;
			if (status == 1) {
				addButton
						.setImageResource(R.drawable.android_icon_add_old);
//				addButton.invalidateDrawable(mActivity.getResources().getDrawable(R.drawable.android_icon_add_old));
			} else {
				addButton
						.setImageResource(R.drawable.android_icon_add_new);
			}
			view.postInvalidate();
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_questionnaire);
		setTitle("选投顾");
		init();
	}

	private void init() {
		titleRight1.setText("跳过");
		titleRight1.setVisibility(View.VISIBLE);
		mScrollView = (ScrollView) findViewById(R.id.scrollId);
		mButton = (Button) findViewById(R.id.btnnext);
		mButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if ("".equals(radioButtonString) || radioButtonString == null
						|| "".equals(radioButtonString2)
						|| radioButtonString2 == null
						|| "".equals(radioButtonString3)
						|| radioButtonString3 == null) {
					showToast("请完成问卷");
				} else {
					getList();
//					mScrollView.startAnimation(mHiddenAction);
////					mScrollView.setVisibility(View.GONE);
//					listView.startAnimation(mShowAction);
////					listView.setVisibility(View.VISIBLE);
//					
//					mHandler.postDelayed(new Runnable() {
//						public void run() {
//							mScrollView.setVisibility(View.GONE);
//							listView.setVisibility(View.VISIBLE);
//						}
//					}, 1000);
				}
			}

		});
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		radioGroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
		radioGroup3 = (RadioGroup) findViewById(R.id.radioGroup3);
		selectRadioBtn();
		selectRadioBtn2();
		selectRadioBtn3();
		radioGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						selectRadioBtn();
					}
				});
		radioGroup2
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						selectRadioBtn2();
					}
				});
		radioGroup3
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						selectRadioBtn3();
					}
				});
		listView = (XListView) findViewById(R.id.listView);
		listView.setXListViewListener(this);
		// final View
		// v1=getLayoutInflater().inflate(R.layout.item_search_congenial_list_header,
		// null, false);
		// listView.addHeaderView(v1);
		// RelativeLayout
		// mRelativeLayout=(RelativeLayout)v1.findViewById(R.id.first);
		// mRelativeLayout.setOnClickListener(new OnClickListener(){
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// listView.removeHeaderView(v1);
		// }
		//
		// });
		// ImageView imageView=(ImageView)v1.findViewById(R.id.delete_item);
		// imageView.setOnClickListener(new OnClickListener(){
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// listView.removeHeaderView(v1);
		// }
		//
		// });
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(QuestionnaireActivity.this,
						ViewInvesterInfoActivity.class);
				intent.putExtra("USERNAME", mCongeniaListData.get((int) id)
						.getUserName());
				intent.putExtra("USERID", mCongeniaListData.get((int) id)
						.getLoginId());
				startActivity(intent);
			}

		});
		
		titleLeft1.setVisibility(View.GONE);

		listView.setDivider(null);
		listView.setPullLoadEnable(false);
		myAdapter = new MyAdapter(this);
		listView.setAdapter(myAdapter);

		imageLoader = new ImageLoader(QuestionnaireActivity.this);

		mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		mShowAction.setDuration(1000);

		mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				-1.0f);
		mHiddenAction.setDuration(1000);
	}

	private void selectRadioBtn() {
		radioButton = (RadioButton) findViewById(radioGroup
				.getCheckedRadioButtonId());
		if(radioButton!=null){
			radioButtonString = radioButton.getText().toString();
		}
	}

	private void selectRadioBtn2() {
		radioButton2 = (RadioButton) findViewById(radioGroup2
				.getCheckedRadioButtonId());
		if(radioButton2!=null){
			radioButtonString2 = radioButton2.getText().toString();
		}
	}

	private void selectRadioBtn3() {
		radioButton3 = (RadioButton) findViewById(radioGroup3
				.getCheckedRadioButtonId());
		if(radioButton3!=null){
			radioButtonString3 = radioButton3.getText().toString();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.title_right1:
			setResult(1);
			finish();
			break;
		}
	}

	class MyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public MyAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}
		
		
		private void doAttention(final int itemIndex, String userId) {
			String url = "";
			// if (attended)
			// url = String.format(NetUrlMyInfo.ATTENTION,
			// userId,UserInfo.getInstance().getUserId(),0);
			// else
			url = String.format(NetUrlMyInfo.ATTENTION, userId, UserInfo
					.getInstance().getUserId(), 1);
			JsonRequest<BaseSignResult> request = new JsonRequest<BaseSignResult>(
					Method.GET, url,null,
					new RequestHandlerListener<BaseSignResult>(getContext()) {

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
						public void onSuccess(String id, BaseSignResult data) {
							// TODO Auto-generated method stub
//							try {
//								if (data.getRetCode() == 2) {
//									Message msg=Message.obtain();
//									msg.what=1;
//									msg.arg1=itemIndex;
//									mHandler.sendMessage(msg);
//									com.jrj.tougu.MyApplication.get().setNewConcent(true);
//								}
//								
////								Toast.makeText(mActivity, data.getMsg(),
////										Toast.LENGTH_SHORT).show();
//							} catch (Exception e) {
//								Toast.makeText(QuestionnaireActivity.this, e.toString(),
//										Toast.LENGTH_SHORT).show();
//							}
							Toast.makeText(QuestionnaireActivity.this,data.getMsg(), Toast.LENGTH_SHORT).show();

						}
						@Override
						public void onFailure(String id, int code, String str,Object obj) {
							if(obj != null && obj instanceof TouguBaseResult){
								TouguBaseResult data = (TouguBaseResult)obj;
								if(data.getRetCode() == 2){
									QuestionnarieData itemData = (QuestionnarieData) getItem(itemIndex);
									itemData.setAttentionFlag(1);
									myAdapter.notifyDataSetChanged();
									Toast.makeText(QuestionnaireActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
									com.gome.haoyuangong.MyApplication.get().setNewConcent(true);
								}else{
									if(data.getRetCode() == -3){
										QuestionnarieData itemData = (QuestionnarieData) getItem(itemIndex);
										itemData.setAttentionFlag(1);
										myAdapter.notifyDataSetChanged();
									}
									super.onFailure(id, code, str, obj);
								}
							}else{
								super.onFailure(id, code, str, obj);
							}
						}
					}, BaseSignResult.class);
			send(request);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mCongeniaListData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mCongeniaListData.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.item_questionnaire_list, parent, false);
				viewHolder = new ViewHolder();
				viewHolder.userIcon = (ImageView) convertView
						.findViewById(R.id.user_icon);
				viewHolder.userName = (TextView) convertView
						.findViewById(R.id.user_name);
				viewHolder.userRole = (TextView) convertView
						.findViewById(R.id.labelTextView1);
				viewHolder.company = (TextView) convertView
						.findViewById(R.id.labelTextView3);
				viewHolder.addIcon = (ImageView) convertView
						.findViewById(R.id.add_icon);
				viewHolder.experienceScope = (TextView) convertView
						.findViewById(R.id.labelTextView2);
				viewHolder.investDirection = (TextView) convertView
						.findViewById(R.id.labelTextView5);
				viewHolder.satisfaction = (TextView) convertView
						.findViewById(R.id.loveTextView);
				viewHolder.replyNum = (TextView) convertView
						.findViewById(R.id.messageTextView);
				viewHolder.fansNum = (TextView) convertView
						.findViewById(R.id.peopleTextView);
				viewHolder.position = (TextView) convertView
						.findViewById(R.id.labelTextView1);
				viewHolder.talkButton=(ImageView)convertView.findViewById(R.id.talk_button);
				
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			QuestionnarieData itemData = mCongeniaListData.get(position);
			ImageView userIcon = viewHolder.userIcon;

			viewHolder.userName.setText(itemData.getUserName());

			viewHolder.position.setText(itemData.getTypeDesc());

			viewHolder.investDirection.setText(itemData.getInvestDirection());

			viewHolder.experienceScope.setText(String.valueOf(itemData
					.getExperienceScope()));
			viewHolder.company.setText(itemData.getCompany());
			
			viewHolder.satisfaction.setText(String.valueOf(itemData.getUseSatisfaction())+"%");
			viewHolder.replyNum.setText(String.valueOf(itemData.getAnswerNumber()));
			viewHolder.fansNum.setText(String.valueOf(itemData.getFansNum()));
			
			ImageView talkButton = (ImageView) viewHolder.talkButton;
			final String userName = itemData.getUserName();
			final String userLonginId = itemData.getLoginId();
			talkButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (UserInfo.getInstance().isLogin()) {
						if (userLonginId == UserInfo.getInstance().getUserId()) {
							showToast("自己不能咨询自己哟！");
						} else {
							Intent intent = new Intent(QuestionnaireActivity.this,
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
						Intent intent = new Intent(QuestionnaireActivity.this,
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

			
			final int attentionFlag=itemData.getAttentionFlag();
			ImageView addButton = (ImageView)viewHolder.addIcon ;
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
						.setImageResource(R.drawable.android_icon_add_old);
			} else {
				addButton
						.setImageResource(R.drawable.android_icon_add_new);
			}
			final int itemIndex = position;
			addButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (!UserInfo.getInstance().isLogin()) {
						Intent intent = new Intent(QuestionnaireActivity.this,
								LoginActivity.class);
						startActivityForResult(intent, 1);
					} else {
						if(attentionFlag != 1){
							doAttention(itemIndex, userLonginId);
						}
					}

				}

			});
			// labelTextView4.setText();
			// viewHolder.satisfaction.setText(String.valueOf(itemData.getUseSatisfaction())+
			// "%");

			// viewHolder.replyNum.setText(String.valueOf(itemData.getAnswerNumber()));

			// viewHolder.fansNum.setText(String.valueOf(itemData.getFansNum()));
			imageLoader.downLoadImage(itemData.getHeadImage(), userIcon);
			return convertView;
		}

		
	}
	class ViewHolder {
		ImageView talkButton;
		ImageView userIcon;
		TextView userName;
		ImageView addIcon;
		TextView userRole;
		TextView company;
		TextView position;
		TextView experienceScope;
		TextView fansNum;
		TextView replyNum;
		TextView satisfaction;
		TextView investDirection;
		ImageView imageLimit;
		ImageView ivCertif;
	}
	private void getList() {

		String url = "http://mapi.itougu.jrj.com.cn/wireless/survey?uid=000822010000046691&ans=1&ans=2&ans=3";
		Log.e(TAG, url);
		JsonRequest<QuestionnarieBean> request = new JsonRequest<QuestionnarieBean>(
				Method.GET, url, new RequestHandlerListener<QuestionnarieBean>(
						getContext()) {

					@Override
					public void onStart(Request request) {
						super.onStart(request);
						// showDialog(request);
						mButton.setText("获取推荐投顾...");
						mButton.setEnabled(false);
					}

					@Override
					public void onEnd(Request request) {
						super.onEnd(request);
						// hideDialog(request);
						mButton.setText("获取推荐投顾");
						mButton.setEnabled(true);
					}

					@Override
					public void onSuccess(String id, QuestionnarieBean data) {
						// TODO Auto-generated method stub

						if (data == null) {
							Toast.makeText(QuestionnaireActivity.this,
									"请检查网络", Toast.LENGTH_SHORT).show();
						} else {
							if (data.getRetCode() == 0) {
								int size = data.getData().getList().size();
								if (size > 0) {
									mCongeniaListData.addAll(data.getData()
											.getList());
									listView.setRefreshTime(getRefreshTime(REFRESH_QUESTIONNAIRE_LIST));
									listView.stopRefresh();
									myAdapter.notifyDataSetChanged();
									if(listView.getVisibility() == View.GONE){
										showRocommendList();
									}
									
								} else {
									Toast.makeText(QuestionnaireActivity.this,
											"请检查网络", Toast.LENGTH_SHORT).show();
								}
							} else {
								Toast.makeText(QuestionnaireActivity.this,
										data.getMsg(), Toast.LENGTH_SHORT)
										.show();
							}
						}

					}
				}, QuestionnarieBean.class);

		send(request);

	}
	
	private void showRocommendList(){
		mScrollView.startAnimation(mHiddenAction);
//		mScrollView.setVisibility(View.GONE);
		listView.setVisibility(View.VISIBLE);
		listView.startAnimation(mShowAction);

		
		mHandler.postDelayed(new Runnable() {
			public void run() {
				mScrollView.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
			}
		}, 0);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		mCongeniaListData.clear();
		getList();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}

}
