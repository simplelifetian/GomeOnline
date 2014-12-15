package com.gome.haoyuangong.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.bean.Stock;
import com.gome.haoyuangong.dialog.BottomDialog;
import com.gome.haoyuangong.dialog.BottomDialog.OnConfirmListener;
import com.gome.haoyuangong.log.Logger;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.TouguBaseResult;
import com.gome.haoyuangong.net.result.tougu.CommentActionResult;
import com.gome.haoyuangong.net.url.NetUrlComment;
import com.gome.haoyuangong.net.url.NetUrlLoginAndRegist;
import com.gome.haoyuangong.net.url.NetUrlMyInfo;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.presenter.IAskListPresenter;
import com.gome.haoyuangong.presenter.IFileUploadPresenter;
import com.gome.haoyuangong.utils.FileUtils;
import com.gome.haoyuangong.utils.HtmlUtils;
import com.gome.haoyuangong.utils.ImageUtils;
import com.gome.haoyuangong.utils.StringUtils;
import com.gome.haoyuangong.utils.HtmlUtils.EmoBean;
import com.gome.haoyuangong.views.EmotionsLayout;
import com.gome.haoyuangong.views.KeyboardLayout;

public class ReplyActivity extends BaseActivity {

	private static final String TAG = ReplyActivity.class.getName();

	public static final String BUNDLE_TYPE = "BUNDLE_TYPE";
	public static final int STOCK_REQUEST_CODE = 1001;

	public static final int COMMENT_SUCCESS_CODE = 1101;
	private static final int MAX_PIC_SIZE = 1024 * 1024;
	public static final int TYPE_COMMENT = 1;
	public static final int TYPE_REASK = 2;
	public static final int TYPE_EVALUATE = 3;
	public static final int TYPE_ANSWER = 4;
	public static final int TYPE_AGAINASK = 5;
	public static final int TYPE_AGAINANSWER = 6;
	public static final int MAX_COUNT_EDIT = 200;
	// 评论参数
	public static final String BUNDLE_PARAM_APPID = "APPID";// 频道id
	public static final String BUNDLE_PARAM_APPITEMID = "APPITEMID";// 观点id
	public static final String BUNDLE_PARAM_SENDERID = "SENDERID";// 评论者id
	public static final String BUNDLE_PARAM_SENDERNAME = "SENDERNAME";// 评论者姓名
	public static final String BUNDLE_PARAM_SENDSOURCE = "SENDSOURCE";// 评论者姓名

	public static final String BUNDLE_PARAM_REPLYROOTID = "REPLYROOTID";// 观点id
	public static final String BUNDLE_PARAM_REPLYTOID = "REPLYTOID";// 评论对象id
	public static final String BUNDLE_PARAM_ANSWERID = "ANSWERID";// 回答id
	public static final String BUNDLE_PARAM_ASKTOUSERID = "ASKTOUSERID";// 被问人的id
	public static final String BUNDLE_PARAM_ANSWERTOUSERID = "ANSWERTOUSERID";// 被回答的追问人的id

	private EditText etContent;
	private ImageView imageView1;
	private ImageView imageView2;
	private ImageView imageView3;
	private TextView tvContentCount;
	private Button doReply;

	private RatingBar ratingBar;
	private LinearLayout pingfenLayout;
	private View btnLyCenter;

	private KeyboardLayout mainLayout;

	private View mediaLayout;
	private boolean isMediaModel = false;
	private LinearLayout loSelectedImage;
	private ImageView[] selectedImages = new ImageView[3];
	private String[] selectImagePath = new String[3];
	private final ImageView[] selectDel = new ImageView[3];
	private final RelativeLayout[] selectedLayout = new RelativeLayout[3];
	private EmotionsLayout emotionLayout;
	private int type;
	protected int askId;
	private String imageUrl = null;
	private IAskListPresenter mIAskListPresenter = new IAskListPresenter(this);
	private IFileUploadPresenter mIFileUploadPresenter = new IFileUploadPresenter(this) {
		public void onSuccessed(String jsonData) {
			super.onSuccessed(jsonData);
			imageUrl = this.headUrl;
			if (type == TYPE_AGAINANSWER) {
				doAgainAnaswer();
			} else {
				postTextAnswer(etContent.getText().toString());
			}
		};
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reply);
		// hideTitle();
		type = getIntent().getIntExtra(BUNDLE_TYPE, 1);
		initViews();

		switch (type) {
		case TYPE_COMMENT:
			setTitle("评论");
			initComment();
			break;
		case TYPE_REASK:
			setTitle("追问");
			initReask();
			break;
		case TYPE_EVALUATE:
			setTitle("评价");
			initEvaluate();
			break;
		case TYPE_ANSWER:
		case TYPE_AGAINANSWER:
			setTitle("回答");
			initAnswer();
			break;
		default:
			Toast.makeText(this, "无效类型", Toast.LENGTH_SHORT).show();
			finish();
		}
		overridePendingTransition(R.anim.dialog_enter, R.anim.activity_default);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.activity_default, R.anim.dialog_exit);
	}

	private void initViews() {
		etContent = (EditText) findViewById(R.id.et_reply);
		imageView1 = (ImageView) findViewById(R.id.imageView_1);
		imageView2 = (ImageView) findViewById(R.id.imageView_2);
		imageView3 = (ImageView) findViewById(R.id.imageView_3);
		tvContentCount = (TextView) findViewById(R.id.tv_reply_count);
		doReply = (Button) findViewById(R.id.bt_action);
		ratingBar = (RatingBar) findViewById(R.id.ratingBar);
		btnLyCenter = findViewById(R.id.image_ly);

		pingfenLayout = (LinearLayout) findViewById(R.id.pingfen_layout);
		mediaLayout = findViewById(R.id.media_layout);
		mediaLayout.setVisibility(View.GONE);

		emotionLayout = (EmotionsLayout) findViewById(R.id.emotion_layout);
		emotionLayout.setVisibility(View.GONE);
		if (type != TYPE_EVALUATE) {
			titleRight2.setEnabled(false);
		}
		tvContentCount.setText(0 + "/" + MAX_COUNT_EDIT);
		etContent.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (type != TYPE_EVALUATE) {
					if (StringUtils.isBlank(s.toString())) {
						titleRight2.setEnabled(false);
					} else {
						titleRight2.setEnabled(true);
					}
				}
				tvContentCount.setText(s.toString().length() + "/" + MAX_COUNT_EDIT);

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		emotionLayout.setOnItemClickListener(new EmotionsLayout.OnItemClickListener() {

			@Override
			public void onEmotionClick(String emoName) {
				// TODO Auto-generated method stub
				Logger.error(TAG, "emo name is : " + emoName);
				EmoBean emoBean = HtmlUtils.getEmotionResId(emoName);
				if (emoBean != null) {
					ImageSpan imageSpan = new ImageSpan(getContext(), HtmlUtils.getEmotionResId(emoName).getResId());
					String repText = emoBean.geteName();
					SpannableString spannableString = new SpannableString(repText);
					spannableString.setSpan(imageSpan, 0, repText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					// 将随机获得的图像追加到EditText控件的最后
					// etContent.append(spannableString);
					int index = etContent.getSelectionStart();
					Editable editable = etContent.getText();
					editable.insert(index, spannableString);
				}
			}
		});

		mainLayout = (KeyboardLayout) findViewById(R.id.main_lo);
		// mainLayout.setOnkbdStateListener(new onKybdsChangeListener() {
		//
		// public void onKeyBoardStateChange(int state) {
		// switch (state) {
		// case KeyboardLayout.KEYBOARD_STATE_HIDE:
		// uiHandler.sendEmptyMessage(1);
		// break;
		// case KeyboardLayout.KEYBOARD_STATE_SHOW:
		// uiHandler.sendEmptyMessage(2);
		// break;
		// }
		// }
		// });
	}

	private static final int INPUT_TYPE_KEYBOARD = 1;
	private static final int INPUT_TYPE_BIAOQING = 2;
	private int currentInputType = INPUT_TYPE_KEYBOARD;

	public Handler uiHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case INPUT_TYPE_KEYBOARD:
				setInputStatus(INPUT_TYPE_KEYBOARD);
				currentInputType = INPUT_TYPE_KEYBOARD;
				break;
			case INPUT_TYPE_BIAOQING:
				setInputStatus(INPUT_TYPE_BIAOQING);
				currentInputType = INPUT_TYPE_BIAOQING;
				break;
			}
		}
	};

	/**
	 * 
	 * @param status
	 *          1 键盘 2 表情
	 */
	private void setInputStatus(int status) {

		switch (status) {
		case 1: {
			showSoftInput(etContent);
			imageView2.setImageResource(R.drawable.icon_biaoqing);
			emotionLayout.setVisibility(View.GONE);
			break;
		}
		case 2: {
			hideSoftInput();
			imageView2.setImageResource(R.drawable.icon_keyboard);
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					emotionLayout.setVisibility(View.VISIBLE);
				}

			}, 100);
			break;
		}
		}
	}

	private void initComment() {

		final String appId = getIntent().getStringExtra(BUNDLE_PARAM_APPID);
		final String appItemid = getIntent().getStringExtra(BUNDLE_PARAM_APPITEMID);
		final String senderId = getIntent().getStringExtra(BUNDLE_PARAM_SENDERID);
		final String senderName = getIntent().getStringExtra(BUNDLE_PARAM_SENDERNAME);
		final String replyRootId = getIntent().getStringExtra(BUNDLE_PARAM_REPLYROOTID);
		final String replyToId = getIntent().getStringExtra(BUNDLE_PARAM_REPLYTOID);
		if (StringUtils.isEmpty(appId) || StringUtils.isEmpty(appItemid) || StringUtils.isEmpty(senderId)) {
			Toast.makeText(this, "参数不完整", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}

		imageView2.setImageResource(R.drawable.icon_biaoqing);
		imageView2.setVisibility(View.GONE);
		imageView3.setImageResource(R.drawable.icon_opinion_stock);
		imageView1.setVisibility(View.GONE);
		pingfenLayout.setVisibility(View.GONE);
		etContent.setHint("输入您的评论。。。");
		etContent.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (currentInputType == INPUT_TYPE_KEYBOARD) {

				} else {
					uiHandler.sendEmptyMessage(INPUT_TYPE_KEYBOARD);
				}
			}
		});
		// doReply.setText("评论");
		titleRight2.setText("提交");
		titleRight2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Toast.makeText(ReplyActivity.this, "评论成功",
				// Toast.LENGTH_SHORT).show();
				doComment(appId, appItemid, senderId, senderName, replyRootId, replyToId, HtmlUtils.getSenderText(etContent.getText()));
			}
		});
		imageView2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (currentInputType == INPUT_TYPE_KEYBOARD) {
					uiHandler.sendEmptyMessage(INPUT_TYPE_BIAOQING);
				} else {
					uiHandler.sendEmptyMessage(INPUT_TYPE_KEYBOARD);
				}

			}
		});
		uiHandler.sendEmptyMessage(INPUT_TYPE_KEYBOARD);
		imageView3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
			}
		});

	}

	private void initReask() {
		imageView3.setImageResource(R.drawable.icon_opinion_stock);
		imageView2.setVisibility(View.GONE);
		imageView1.setVisibility(View.GONE);
		pingfenLayout.setVisibility(View.GONE);
		etContent.setHint("输入您的回复。。。");
		// doReply.setText("追问");
		titleRight2.setText("提交");
		titleRight2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				doAgainAsk();
			}
		});

		imageView3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
			}
		});
	}

	private void initEvaluate() {
		imageView1.setVisibility(View.GONE);
		imageView2.setVisibility(View.GONE);
		imageView3.setVisibility(View.GONE);
		pingfenLayout.setVisibility(View.VISIBLE);
		etContent.setHint("请输入200字以内的评价内容");
		// doReply.setText("评论");
		titleRight2.setText("提交");
		titleRight2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				doEvaluate();
			}
		});
	}

	private void doEvaluate() {
		final int answerId = getIntent().getIntExtra(BUNDLE_PARAM_ANSWERID, -1);
		// final String senderId =
		// getIntent().getStringExtra(BUNDLE_PARAM_SENDERID);
		final String content = etContent.getText().toString();
		if (ratingBar.getRating() <= 0) {
			showToast("请对投顾的回答进行打分");
			return;
		}
		UserInfo info = UserInfo.getInstance();
		if (!info.isLogin()) {
			Logger.error("replyanswer", "not login");
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", info.getUserId());
		params.put("answerid", String.valueOf(answerId));
		params.put("evaluate", String.valueOf((int) ratingBar.getRating()));
		params.put("evaContent", content);

		JsonRequest<TouguBaseResult> request = new JsonRequest<TouguBaseResult>(Method.POST, NetUrlMyInfo.EVALUATE, params, new RequestHandlerListener<TouguBaseResult>(getContext()) {

			@Override
			public void onStart(Request request) {
				super.onStart(request);
				showDialog(request, "提交中...");
			}

			@Override
			public void onEnd(Request request) {
				super.onEnd(request);
				hideDialog(request);
			}

			@Override
			public void onSuccess(String id, TouguBaseResult data) {
				// Toast.makeText(ReplyActivity.this, "赞成功",
				// Toast.LENGTH_SHORT).show();
				if (data.getRetCode() == 0) {
					Intent intent = new Intent(IAskListPresenter.ACTION_ASK_REFRESH);
					intent.putExtra(IAskListPresenter.BUNDLE_ANSWERID, answerId);
					intent.putExtra(IAskListPresenter.BUNDLE_STATUS, IAskListPresenter.ANSWER_STATU_EVALUATED);
					intent.putExtra(IAskListPresenter.BUNDLE_EVALUATE_RATING, (int) ratingBar.getRating());
					intent.putExtra(IAskListPresenter.BUNDLE_EVALUATE_CONTENT, content);
					sendBroadcast(intent);
					finish();
					Toast.makeText(ReplyActivity.this, "评价成功", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(ReplyActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
				}
			}
		}, TouguBaseResult.class);

		send(request);
	}

	private void initAnswer() {
		initImageLayout();
		TextView keyboard = (TextView) findViewById(R.id.record_tv_keyboard);
		final View imageUpDivider = findViewById(R.id.image_up_divider);
		final View shandow = findViewById(R.id.shandow);
		final View imgview1Tip = findViewById(R.id.imageView_1_tip);
		imageView1.setImageResource(R.drawable.icon_speaker);
		imageView2.setImageResource(R.drawable.icon_opinion_pic);
		imageView3.setImageResource(R.drawable.icon_opinion_stock);
		pingfenLayout.setVisibility(View.GONE);
		imgview1Tip.setVisibility(View.VISIBLE);
		// imageView2.setVisibility(View.GONE);
		etContent.setHint("请输入200字以内的回答内容");
		// doReply.setText("回答");
		titleRight2.setText("提交");
		if (type == TYPE_AGAINANSWER) {
			imageView2.setVisibility(View.GONE);
		}
		final Animation animationEnter = AnimationUtils.loadAnimation(getContext(), R.anim.dialog_enter);
		// Animation animationExit = AnimationUtils.loadAnimation(getContext(),
		// R.anim.dialog_exit);
		animationEnter.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				shandow.setVisibility(View.VISIBLE);
			}
		});
		titleRight2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				imageUrl = null;
				if (StringUtils.isEmpty(etContent.getText().toString())) {
					showToast("请输入回答内容");
					return;
				}
				if (isUploadImage()) {
					return;
				}
				if (type == TYPE_AGAINANSWER) {
					doAgainAnaswer();
				} else {
					postTextAnswer(etContent.getText().toString());
				}
			}
		});
		imageView3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
			}
		});
		imageView2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				headClicked();
			}
		});
		keyboard.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				imageView1.setImageResource(R.drawable.icon_speaker);
				btnLyCenter.setVisibility(View.VISIBLE);
				titleRight2.setVisibility(View.VISIBLE);
				imageUpDivider.setVisibility(View.VISIBLE);
				mediaLayout.setVisibility(View.GONE);
				// imageView2.setVisibility(View.VISIBLE);
				// imageView3.setVisibility(View.VISIBLE);
				// doReply.setVisibility(View.VISIBLE);
				etContent.setVisibility(View.VISIBLE);
				shandow.setVisibility(View.GONE);
				etContent.setEnabled(true);
				showSoftInput(etContent);
			}
		});
		imageView1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				hideSoftInput();
				imageView1.setImageResource(R.drawable.icon_keyboard);
				btnLyCenter.setVisibility(View.GONE);
				titleRight2.setVisibility(View.GONE);
				imageUpDivider.setVisibility(View.GONE);
				etContent.setEnabled(false);
				mediaLayout.postDelayed(new Runnable() {
					@Override
					public void run() {
						mediaLayout.setVisibility(View.VISIBLE);
						mediaLayout.startAnimation(animationEnter);
					}
				}, 300);
			}
		});
	}

	private void initImageLayout() {
		loSelectedImage = (LinearLayout) findViewById(R.id.lo_selected_image);
		ViewGroup.LayoutParams vg = loSelectedImage.getLayoutParams();
		vg.height = getScreenW() / 3;
		loSelectedImage.setLayoutParams(vg);

		selectedImages[0] = (ImageView) findViewById(R.id.selected_1);
		selectedImages[1] = (ImageView) findViewById(R.id.selected_2);
		selectedImages[2] = (ImageView) findViewById(R.id.selected_3);

		selectDel[0] = (ImageView) findViewById(R.id.image_del_1);
		selectDel[1] = (ImageView) findViewById(R.id.image_del_2);
		selectDel[2] = (ImageView) findViewById(R.id.image_del_3);
		for (int i = 0; i < selectedImages.length; i++) {
			selectDel[i].setTag(i);
			selectDel[i].setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					int index = (Integer) v.getTag();
					delSelectImage(index);
				}
			});
		}

		selectedLayout[0] = (RelativeLayout) findViewById(R.id.lo_select_1);
		selectedLayout[1] = (RelativeLayout) findViewById(R.id.lo_select_2);
		selectedLayout[2] = (RelativeLayout) findViewById(R.id.lo_select_3);
		for (int i = 0; i < selectedImages.length; i++) {
			selectedLayout[i].setTag(i);
			selectedLayout[i].setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					int index = (Integer) v.getTag();
					String filePath = selectImagePath[index];
					Intent intent = new Intent(ReplyActivity.this, ImageViewerActivity.class);
					intent.putExtra(ImageViewerActivity.BUNDLE_PARAM_FILEPATH, filePath);
					startActivity(intent);
				}
			});
		}
	}

	public void delSelectImage(int index) {
		selectedImages[index].setImageBitmap(null);
		selectDel[index].setVisibility(View.GONE);
		selectImagePath[index] = null;

	}

	private void headClicked() {
		BottomDialog dialog = new BottomDialog(this);
		dialog.setText1(R.string.phoneSelect1);
		dialog.setText2(R.string.phoneSelect2);
		dialog.setOnConfirmListener(new OnConfirmListener() {
			@Override
			public void onConfirm(int index) {
				if (index == 0) {
					getImageFromCamera();
				} else if (index == 1) {
					getImageFromAlbum();
				}
			}
		});
		dialog.show();
	}

	public void getImageFromAlbum() {
		// Intent intent = new Intent(WriteOpinionActivity.this,
		// AlbumActivity.class);
		// startActivityForResult(intent, IMAGE_REQUEST_CODE);
		Intent intent = new Intent();
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");
		// 根据版本号不同使用不同的Action
		if (Build.VERSION.SDK_INT < 19) {
			intent.setAction(Intent.ACTION_GET_CONTENT);
		} else {
			intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
		}
		startActivityForResult(intent, WriteOpinionActivity.RESULT_LOAD_IMAGE);

	}

	public void getImageFromCamera() {
		if (FileUtils.isSdCardMounted()) {
			Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "image_photo.jpg"));
			openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			startActivityForResult(openCameraIntent, WriteOpinionActivity.TAKE_PICTURE);
		} else {
			showToast(R.string.warn_no_sdcard);
		}
	}

	/**
	 * 用于“我”中投顾回答用户
	 * 
	 * @param answer
	 */
	private void postTextAnswer(String answer) {
		// final String appId = getIntent().getStringExtra(BUNDLE_PARAM_APPID);
		final int appItemid = getIntent().getIntExtra(BUNDLE_PARAM_APPITEMID, 0);
		// final String senderId =
		// getIntent().getStringExtra(BUNDLE_PARAM_SENDERID);
		// final String senderName =
		// getIntent().getStringExtra(BUNDLE_PARAM_SENDERNAME);
		// final String sendSource =
		// getIntent().getStringExtra(BUNDLE_PARAM_SENDSOURCE);
		answer = mIAskListPresenter.handleInsertStock(answer);
		// answer = addImageToContent(answer);

		askId = appItemid;
		UserInfo info = UserInfo.getInstance();
		if (!info.isLogin()) {
			Logger.error("replyanswer", "not login");
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", info.getUserId());
		params.put("username", info.getUserName());
		params.put("qId", String.valueOf(appItemid));
		params.put("source", "爱投顾手机客户端");
		params.put("content", answer);
		if (!StringUtils.isEmpty(imageUrl)) {
			params.put("images", NetUrlLoginAndRegist.UPLOAD_PREFIX + imageUrl);
		}
		JsonRequest<TouguBaseResult> request = new JsonRequest<TouguBaseResult>(Method.POST, NetUrlMyInfo.ADVISER_ANSWER_USER, params, new RequestHandlerListener<TouguBaseResult>(getContext()) {

			@Override
			public void onStart(Request request) {
				super.onStart(request);
				showDialog(request, "提交中...");
			}

			@Override
			public void onEnd(Request request) {
				super.onEnd(request);
				hideDialog(request);
			}

			@Override
			public void onSuccess(String id, TouguBaseResult data) {
				// Toast.makeText(ReplyActivity.this, "赞成功",
				// Toast.LENGTH_SHORT).show();
				if (data.getRetCode() == 0) {
					Intent intent = new Intent(IAskListPresenter.ACTION_ASK_REFRESH);
					intent.putExtra(IAskListPresenter.BUNDLE_ASKID, appItemid);
					intent.putExtra(IAskListPresenter.BUNDLE_STATUS, IAskListPresenter.ANSWER_STATUS_ANSWERED);
					sendBroadcast(intent);
					Toast.makeText(ReplyActivity.this, "回答成功,可以去您的的回答记录中查看全部回答内容", Toast.LENGTH_SHORT).show();
					finish();
				}
			}
		}, TouguBaseResult.class);

		send(request);
	}

	private String addImageToContent(String content) {
		StringBuffer bf = new StringBuffer(content);
		if (!StringUtils.isEmpty(imageUrl)) {
			bf.append(getString(R.string.image_lable, NetUrlLoginAndRegist.UPLOAD_PREFIX + imageUrl, 480, 720));
		}
		return bf.toString();
	}

	private boolean isUploadImage() {
		if (selectImagePath == null)
			return false;
		int size = selectImagePath.length;
		for (int i = 0; i < size; i++) {
			String needUpLoad = selectImagePath[i];
			if (StringUtils.isEmpty(needUpLoad)) {
				return false;
			} else {
//				File f = new File(needUpLoad);
//				if (!f.exists())
//					return false;
//				long fileSize = f.length();
//				Logger.error(TAG, "old pic size : " + fileSize);
//				if (fileSize > MAX_PIC_SIZE) {
//					Bitmap oldPic = ImageUtils.compressImageFromFile(needUpLoad);
//					File cacheFile = getContext().getExternalCacheDir();
//					File tempImageFile = new File(cacheFile, "opinion_image.jpg");
//					FileOutputStream fos = null;
//					boolean compSuccess = false;
//					try {
//						fos = new FileOutputStream(tempImageFile);
//						oldPic.compress(CompressFormat.JPEG, 100, fos);
//						compSuccess = true;
//					} catch (FileNotFoundException e) {
//						// TODO Auto-generated catch block
//						Logger.error(TAG, "图片压缩失败", e);
//					} finally {
//						try {
//							fos.close();
//							oldPic.recycle();
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//						}
//					}
//					if (compSuccess) {
//						needUpLoad = tempImageFile.getAbsolutePath();
//						f = new File(needUpLoad);
//						fileSize = f.length();
//					}
//				}
				mIFileUploadPresenter.uploadImageNormal(needUpLoad);
				return true;
			}
		}
		return false;
	}

	private void doAgainAsk() {
		// final String appId = getIntent().getStringExtra(BUNDLE_PARAM_APPID);
		final int answerId = getIntent().getIntExtra(BUNDLE_PARAM_ANSWERID, -1);
		final String askToId = getIntent().getStringExtra(BUNDLE_PARAM_ASKTOUSERID);
		final int appItemid = getIntent().getIntExtra(BUNDLE_PARAM_APPITEMID, 0);
		// final String senderId =
		// getIntent().getStringExtra(BUNDLE_PARAM_SENDERID);
		// final String senderName =
		// getIntent().getStringExtra(BUNDLE_PARAM_SENDERNAME);
		// final String sendSource =
		// getIntent().getStringExtra(BUNDLE_PARAM_SENDSOURCE);
		UserInfo info = UserInfo.getInstance();
		if (!info.isLogin()) {
			Logger.error("replyanswer", "not login");
			return;
		}
		String content = etContent.getText().toString();
		if (StringUtils.isEmpty(content)) {
			showToast("请输入提问内容");
			return;
		}
		content = mIAskListPresenter.handleInsertStock(content);
		Map<String, String> params = new HashMap<String, String>();
		params.put("againaskUserid", info.getUserId());
		params.put("anwserId", String.valueOf(answerId));
		params.put("asktouserid", askToId);
		params.put("againaskUsername", info.getUserName());
		params.put("askid", String.valueOf(appItemid));
		params.put("source", "爱投顾手机客户端");
		params.put("content", content);
		// etContent.setHint("输入您的提问。。。");
		// doReply.setText("追问");

		JsonRequest<TouguBaseResult> request = new JsonRequest<TouguBaseResult>(Method.POST, NetUrlMyInfo.AGAINASK, params, new RequestHandlerListener<TouguBaseResult>(getContext()) {

			@Override
			public void onStart(Request request) {
				super.onStart(request);
				showDialog(request);
			}

			@Override
			public void onEnd(Request request) {
				super.onEnd(request);
				hideDialog(request);
			}

			@Override
			public void onSuccess(String id, TouguBaseResult data) {
				// Toast.makeText(ReplyActivity.this, "赞成功",
				// Toast.LENGTH_SHORT).show();
				if (data.getRetCode() == 0) {
					Intent intent = new Intent(IAskListPresenter.ACTION_ASK_REFRESH);
					intent.putExtra(IAskListPresenter.BUNDLE_ASKID, appItemid);
					intent.putExtra(IAskListPresenter.BUNDLE_STATUS, IAskListPresenter.ANSWER_STATU_REASK);
					sendBroadcast(intent);
					showToast("追问成功");
					finish();
				}
				// Toast.makeText(ReplyActivity.this, data.getMsg(),
				// Toast.LENGTH_SHORT).show();
			}
		}, TouguBaseResult.class);

		send(request);
	}

	private void doAgainAnaswer() {
		// final String appId = getIntent().getStringExtra(BUNDLE_PARAM_APPID);
		final int answerToId = getIntent().getIntExtra(BUNDLE_PARAM_ANSWERTOUSERID, 0);
		// final String senderId =
		// getIntent().getStringExtra(BUNDLE_PARAM_SENDERID);
		// final String senderName =
		// getIntent().getStringExtra(BUNDLE_PARAM_SENDERNAME);
		// final String sendSource =
		// getIntent().getStringExtra(BUNDLE_PARAM_SENDSOURCE);

		UserInfo info = UserInfo.getInstance();
		if (!info.isLogin()) {
			Logger.error("replyanswer", "not login");
			return;
		}
		String content = etContent.getText().toString();
		content = mIAskListPresenter.handleInsertStock(content);
		// content = addImageToContent(content);
		if (StringUtils.isEmpty(content)) {
			showToast("请输入回答内容");
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", info.getUserId());
		params.put("username", info.getUserName());
		params.put("againAskId", String.valueOf(answerToId));
		params.put("source", "爱投顾手机客户端");
		params.put("content", content);
		if (!StringUtils.isEmpty(imageUrl)) {
			params.put("images", NetUrlLoginAndRegist.UPLOAD_PREFIX + imageUrl);
		}
		// etContent.setHint("输入您的提问。。。");
		// doReply.setText("追问");

		JsonRequest<TouguBaseResult> request = new JsonRequest<TouguBaseResult>(Method.POST, NetUrlMyInfo.AGAINASK, params, new RequestHandlerListener<TouguBaseResult>(getContext()) {

			@Override
			public void onStart(Request request) {
				super.onStart(request);
				showDialog(request);
			}

			@Override
			public void onEnd(Request request) {
				super.onEnd(request);
				hideDialog(request);
			}

			@Override
			public void onSuccess(String id, TouguBaseResult data) {
				// Toast.makeText(ReplyActivity.this, "赞成功",
				// Toast.LENGTH_SHORT).show();
				if (data.getRetCode() == 0) {
					sendBroadcast(new Intent(IAskListPresenter.ACTION_ASK_REFRESH));
					finish();
				}
				Toast.makeText(ReplyActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
			}
		}, TouguBaseResult.class);

		send(request);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (STOCK_REQUEST_CODE == requestCode) {
			if (data != null) {
				
			}
			return;
		} else if (WriteOpinionActivity.IMAGE_REQUEST_CODE == requestCode) {
			if (data != null) {
				String[] photos = data.getStringArrayExtra(AlbumActivity.SELECTED_PHOTOS);
				if (photos != null) {
					for (int i = 0; i < selectedImages.length; i++) {
						selectedImages[i].setImageBitmap(null);
						selectDel[i].setVisibility(View.GONE);
						selectImagePath[i] = null;
					}

					int i = 0;
					for (String s : photos) {
						selectedImages[i].setImageBitmap(ImageUtils.createTumnbtail(s, 128, 128));
						selectImagePath[i] = s;
						selectDel[i].setVisibility(View.VISIBLE);
						i++;
					}
					if (loSelectedImage.getVisibility() == View.GONE) {
						loSelectedImage.setVisibility(View.VISIBLE);
					}
				}
			}
			return;
		} else if (WriteOpinionActivity.TAKE_PICTURE == requestCode && resultCode == RESULT_OK) {
			String[] photos = new String[1];
			photos[0] = Environment.getExternalStorageDirectory() + "/image_photo.jpg";
			if (photos != null) {
				for (int i = 0; i < selectedImages.length; i++) {
					selectedImages[i].setImageBitmap(null);
					selectImagePath[i] = null;
					selectDel[i].setVisibility(View.GONE);
				}

				int i = 0;
				for (String s : photos) {
					selectedImages[i].setImageBitmap(ImageUtils.createTumnbtail(s, 128, 128));
					selectImagePath[i] = s;
					selectDel[i].setVisibility(View.VISIBLE);
					i++;
				}
				if (loSelectedImage.getVisibility() == View.GONE) {
					loSelectedImage.setVisibility(View.VISIBLE);
				}
			}
			return;
		} else if (WriteOpinionActivity.RESULT_LOAD_IMAGE == requestCode && resultCode == RESULT_OK) {
			if (null != data) {
				Uri contentUri = data.getData();
				String img_path = WriteOpinionActivity.getPath(getContext(), contentUri);
				if (!StringUtils.isEmpty(img_path)) {
					String[] photos = { img_path };
					if (photos != null) {
						for (int i = 0; i < selectedImages.length; i++) {
							selectedImages[i].setImageBitmap(null);
							selectImagePath[i] = null;
							selectDel[i].setVisibility(View.GONE);
						}

						int i = 0;
						for (String s : photos) {
							selectedImages[i].setImageBitmap(ImageUtils.createTumnbtail(s, 128, 128));
							selectImagePath[i] = s;
							selectDel[i].setVisibility(View.VISIBLE);
							i++;
						}
						if (loSelectedImage.getVisibility() == View.GONE) {
							loSelectedImage.setVisibility(View.VISIBLE);
						}
					}
				}

			}
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void doComment(String appId, String appItemid, String senderId, String senderName, String replyRootid, String replyToid, String content) {

		if (StringUtils.isEmpty(content)) {
			Toast.makeText(this, "输入评论内容", Toast.LENGTH_SHORT).show();
			return;
		}
		content = mIAskListPresenter.handleInsertStock(content);
		Map<String, String> params = new HashMap<String, String>();
		params.put("appId", appId);
		params.put("appItemid", appItemid);
		params.put("senderId", senderId);
		params.put("replyRootid", replyRootid);
		params.put("replyToid", replyToid);
		params.put("content", content);
		params.put("senderName", senderName);
		params.put("ip", "127.0.0.1");
		params.put("frm", "android");

		Logger.error(TAG, params.toString());

		JsonRequest<CommentActionResult> request = new JsonRequest<CommentActionResult>(Method.POST, NetUrlComment.COMMENT_ACTION, params, new RequestHandlerListener<CommentActionResult>(getContext()) {

			@Override
			public void onStart(Request request) {
				super.onStart(request);
				showDialog(request);
			}

			@Override
			public void onEnd(Request request) {
				super.onEnd(request);
				hideDialog(request);
			}

			@Override
			public void onSuccess(String id, CommentActionResult data) {
				// TODO Auto-generated method stub
				// Toast.makeText(ReplyActivity.this, "赞成功",
				// Toast.LENGTH_SHORT).show();
				if (data.getReturnCode() == 1) {
					Toast.makeText(ReplyActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
					setResult(COMMENT_SUCCESS_CODE);
					finish();
				} else {
					Toast.makeText(ReplyActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		}, CommentActionResult.class);

		send(request);

	}

	public String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("WifiPreference IpAddress", ex.toString());
		}
		return "";
	}

	public static void goToEvaluate(Context ctx, int answerId) {
		Intent i = new Intent(ctx, ReplyActivity.class);
		i.putExtra(BUNDLE_TYPE, TYPE_EVALUATE);
		i.putExtra(BUNDLE_PARAM_ANSWERID, answerId);
		ctx.startActivity(i);
	}
}
