package com.gome.haoyuangong.activity;

import java.util.HashMap;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.crop.CropHandler;
import com.gome.haoyuangong.crop.CropHelper;
import com.gome.haoyuangong.crop.CropParams;
import com.gome.haoyuangong.dialog.BottomDialog;
import com.gome.haoyuangong.dialog.BottomDialog.OnConfirmListener;
import com.gome.haoyuangong.log.Logger;
import com.gome.haoyuangong.net.NetConfig;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.XinGeBaseResult;
import com.gome.haoyuangong.net.result.tougu.LoginResultBean;
import com.gome.haoyuangong.net.result.tougu.RegistResultBean;
import com.gome.haoyuangong.net.result.tougu.UserIdentifiedResult;
import com.gome.haoyuangong.net.url.NetUrlLoginAndRegist;
import com.gome.haoyuangong.net.url.NetUrlTougu;
import com.gome.haoyuangong.net.url.XinGeURL;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.presenter.IFileUploadPresenter;
import com.gome.haoyuangong.presenter.IRegistPresenter;
import com.gome.haoyuangong.utils.CheckUtils;
import com.gome.haoyuangong.utils.FileUtils;
import com.gome.haoyuangong.utils.StringUtils;
import com.gome.haoyuangong.utils.CheckUtils.CheckResponse;
import com.gome.haoyuangong.R;

@EActivity(R.layout.regist_3)
public class Regist3Activity extends BaseActivity implements CropHandler {
	static private final int TYPE_UPDATE_0 = 0;
	static private final int TYPE_UPDATE_1 = 1;
	static private final int TYPE_UPDATE_2 = 2;
	static private final int TYPE_UPDATE_3 = 3;
	static public final int REQUEST_CODE_PICK_IMAGE = 0;// 从相册取照片
	static public final int REQUEST_CODE_CAPTURE_CAMEIA = 1;// 从相机取照片
	@ViewById(R.id.name)
	protected EditText mEditTextName;
	@ViewById(R.id.passwd)
	protected EditText mEditTextPw;
	@ViewById(R.id.head)
	protected ImageView mHead;
	@ViewById(R.id.passwd_check)
	protected CheckBox passwdCheck;
	@Extra(Regist2Activity.BUNDLE_PHONE)
	String phone;
	private String headPath;
	private String headUrl;
	private String userName;
	private String passwd;
	private int type = TYPE_UPDATE_0;

	private boolean isGotoAdvisorCertification = false;
	CropParams mCropParams = new CropParams();
	IRegistPresenter rigist = new IRegistPresenter(this) {
		public void onRigistOk(RegistResultBean bean) {
			super.onRigistOk(bean);
			loginBean = new LoginResultBean();
			loginBean.setMobile(bean.getMobile());
			loginBean.setAccessToken(bean.getAccessToken());
			loginBean.setLoginToken(bean.getLoginToken());
			loginBean.setPassportId(bean.getPassportId());
			loginBean.setUserName(bean.getUserName());
//			 doLogin(userName, passwd);
			type = TYPE_UPDATE_2;
			request3();
		};

		public void onUpdateHead() {
			startActivity(new Intent(getContext(), LoginActivity.class));
			request1();
		};

		public void onUpdateHeadError() {
			showRegistDialog();
		}
	};
	IFileUploadPresenter mIFileUploadPresenter = new IFileUploadPresenter(this) {
		public void onSuccessed(String jsonData) {
			super.onSuccessed(jsonData);
			type = TYPE_UPDATE_1;
			Regist3Activity.this.headUrl = headUrl;
			request2();
		};

		public void onFailed() {
			showRegistDialog();
		};
	};

	@AfterViews
	protected void init() {
		setTitle(R.string.regist3);
//		phone="11112345685";
		passwdCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
//					mEditTextPw.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);
					mEditTextPw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());  
					mEditTextPw.setSelection(mEditTextPw.getText().length());
				}else{
					mEditTextPw.setTransformationMethod(PasswordTransformationMethod.getInstance());  
					mEditTextPw.setSelection(mEditTextPw.getText().length());
//					mEditTextPw.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
			}
		});
	}

	@Click
	void submitClicked() {
		isGotoAdvisorCertification = false;
		switch (type) {
		case TYPE_UPDATE_0:
			request1();
			break;
		case TYPE_UPDATE_1:
			request2();
			break;
		case TYPE_UPDATE_2:
			request3();
			break;
		case TYPE_UPDATE_3:
			break;
		}

	}

	private void request2() {
		String name = mEditTextName.getText().toString();
		String password = mEditTextPw.getText().toString();
		if (StringUtils.isEmpty(name)) {
			showToast("请输入用户名");
			return;
		}
		if (StringUtils.isEmpty(password)) {
			showToast("请设置登录密码");
			return;
		}
		CheckResponse cr = CheckUtils.CheckUserName(name);
		if (!cr.isValid) {
			showToast(cr.rtMsg);
			return;
		}
		cr = CheckUtils.isValidPassword(password);
		if (!cr.isValid) {
			showToast(cr.rtMsg);
			return;
		}
		userName = name;
		passwd = password;

		rigist.regist(name, password, phone, headUrl);
	}

	private void request1() {
		if (!StringUtils.isEmpty(headPath)) {
			mIFileUploadPresenter.uploadHeadIcon(headPath);
		} else {
//			showToast("请先设置头像");
			request2();
		}
	}

	private void request3() {
//		UserInfo user = UserInfo.getInstance();
//		rigist.updateHead(user.getHeadPath(), user.getUserId());
		
		getUserInfo(loginBean.getPassportId());

//		registDevice();
	}

	private void showRegistDialog() {

	}

	@Click(R.id.auth_text)
	void authClicked() {
		isGotoAdvisorCertification = true;
		switch (type) {
		case TYPE_UPDATE_0:
			request1();
			break;
		case TYPE_UPDATE_1:
			request2();
			break;
		case TYPE_UPDATE_2:
			request3();
			break;
		case TYPE_UPDATE_3:
			break;
		}
	}

	private LoginResultBean loginBean;

	private void doLogin(String loginID, String passwd) {

		Map<String, String> params = new HashMap<String, String>();
		params.put("loginID", loginID);
		params.put("passwd", passwd);
		params.put("charset", "utf8");

		JsonRequest<LoginResultBean> request = new JsonRequest<LoginResultBean>(Method.POST, NetUrlLoginAndRegist.LOGIN, params, new RequestHandlerListener<LoginResultBean>(getContext()) {

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
			public void onSuccess(String id, LoginResultBean data) {
				if (data.getResultCode() == 0) {
					loginBean = data;
					getUserInfo(loginBean.getPassportId());

					registDevice();
				}
			}

			@Override
			public void onFailure(String id, int code, String str, Object obj) {

				if (obj != null && obj instanceof LoginResultBean) {
					LoginResultBean data = (LoginResultBean) obj;
					if (data.getResultCode() == 1) {
						showToast("用户名或密码错误");
					} else if (data.getResultCode() == -1) {
						showToast("非法参数");
					} else if (data.getResultCode() == 2) {
						showToast("服务内部错误");
					} else {
						showToast("未知错误");
					}
				} else {
					super.onFailure(id, code, str, obj);
				}
			}
		}, LoginResultBean.class);

		send(request);
	}

	private void registDevice() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("deviceToken", UserInfo.getInstance().getDeivceId());
		params.put("deviceType", "3");
		// params.put("user_type", "1");
		// params.put("user_id", "141027010023086621");
		if (!TextUtils.isEmpty(UserInfo.getInstance().getUserId()))
			params.put("user_id", UserInfo.getInstance().getUserId());
		params.put("appid", "tougu");
		if (!UserInfo.getInstance().isLogin())
			params.put("user_type", "3");
		else if (UserInfo.getInstance().isTougu())
			params.put("user_type", "1");
		else
			params.put("user_type", "2");

		JsonRequest<XinGeBaseResult> request = new JsonRequest<XinGeBaseResult>(Method.POST, XinGeURL.REGISTDEVICE, params, new RequestHandlerListener<XinGeBaseResult>(getContext()) {

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

			@Override
			public void onSuccess(String id, XinGeBaseResult data) {
				// TODO Auto-generated method stub
				// Toast.makeText(ReplyActivity.this, "赞成功", Toast.LENGTH_SHORT).show();
				if (data.getRet() == "1") {
					finish();
				}
				// Toast.makeText(LoginActivity.this, data.getMsg(),
				// Toast.LENGTH_SHORT).show();
			}
		}, XinGeBaseResult.class);
		send(request);

	}

	private void getUserInfo(String userid) {

		String url = String.format(NetUrlTougu.USER_IDENTIFY, userid);// 000822010000046691
		// String url =
		// String.format(NetUrlTougu.USER_IDENTIFY,"000822010000046691");
		JsonRequest<UserIdentifiedResult> request = new JsonRequest<UserIdentifiedResult>(Method.GET, url, null, new RequestHandlerListener<UserIdentifiedResult>(getContext()) {

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
			public void onSuccess(String id, UserIdentifiedResult data) {
				// TODO Auto-generated method stub
				// Toast.makeText(ReplyActivity.this, "赞成功", Toast.LENGTH_SHORT).show();
				if (data.getRetCode() == 0 && loginBean != null) {
//					Toast.makeText(Regist3Activity.this, "登录成功", Toast.LENGTH_SHORT).show();
					UserInfo.getInstance().setPassportId(loginBean.getPassportId());
					UserInfo.getInstance().setUserId(loginBean.getPassportId());
					UserInfo.getInstance().setLoginName(loginBean.getUserName());
					UserInfo.getInstance().setUserName(data.getData().getUser().getUserName());
					UserInfo.getInstance().setLoginToken(loginBean.getLoginToken());
					UserInfo.getInstance().setAccessToken(loginBean.getAccessToken());
					UserInfo.getInstance().setIsAdviser(data.getData().getIsAdviser());
					UserInfo.getInstance().setMobile(loginBean.getMobile());
					
					UserInfo.getInstance().setTouguUserBean(data.getData().getUser());
					UserInfo.saveUserInfo(Regist3Activity.this, UserInfo.getInstance());
					// setResult(LOGINED_RESPONSE_CODE);
					Intent intent = new Intent(getContext(), LoginActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					finish();
					if (isGotoAdvisorCertification) {
						intent = new Intent(getContext(), InvestmentAdvisorCertificationActivity.class);
						startActivity(intent);
					} else {
						intent = new Intent(getContext(), QuestionnaireActivity.class);
						startActivity(intent);
					}
				} else {
					Intent intent = new Intent(getContext(), LoginActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					finish();
//					Toast.makeText(Regist3Activity.this, "登录失败", Toast.LENGTH_SHORT).show();
				}
			}

		}, UserIdentifiedResult.class){
			@Override
			public void addCustomHeader(Map<String,String> header){
				header.put(NetConfig.PASSPORTID, loginBean.getPassportId());
				header.put(NetConfig.ACCESSTOKEN, loginBean.getAccessToken());
			}
		};

		send(request);
	}

	@Click
	void headClicked() {
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
		startActivityForResult(CropHelper.buildCropFromGalleryIntent(mCropParams), CropHelper.REQUEST_CROP);
	}

	public void getImageFromCamera() {
		if (FileUtils.isSdCardMounted()) {
			Intent intent = CropHelper.buildCaptureIntent(mCropParams.uri);
			startActivityForResult(intent, CropHelper.REQUEST_CAMERA);
		} else {
			showToast(R.string.warn_no_sdcard);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		CropHelper.handleResult(this, requestCode, resultCode, data);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onPhotoCropped(Uri uri) {
		headPath = mCropParams.uri.getPath();
		Bitmap bitmap = CropHelper.decodeUriAsBitmap(this, mCropParams.uri);
		mHead.setImageBitmap(bitmap);
		type = TYPE_UPDATE_0;
	}

	@Override
	public void onCropCancel() {
	}

	@Override
	public void onCropFailed(String message) {

	}

	@Override
	public CropParams getCropParams() {
		return mCropParams;
	}

	@Override
	public Activity getContext() {
		return this;
	}

}
