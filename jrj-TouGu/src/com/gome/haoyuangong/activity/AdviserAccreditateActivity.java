package com.gome.haoyuangong.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.crop.CropHandler;
import com.gome.haoyuangong.crop.CropHelper;
import com.gome.haoyuangong.crop.CropParams;
import com.gome.haoyuangong.dialog.BottomDialog;
import com.gome.haoyuangong.dialog.BottomDialog.OnConfirmListener;
import com.gome.haoyuangong.layout.self.Function;
import com.gome.haoyuangong.layout.self.SelfInfo.BusinessType;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.result.tougu.RegistResultBean;
import com.gome.haoyuangong.net.url.NetUrlLoginAndRegist;
import com.gome.haoyuangong.net.volley.ImageLoader;
import com.gome.haoyuangong.presenter.IFileUploadPresenter;
import com.gome.haoyuangong.presenter.IRegistPresenter;
import com.gome.haoyuangong.utils.FileUtils;
import com.gome.haoyuangong.R;

public class AdviserAccreditateActivity extends BaseActivity implements CropHandler {

	Intent intent;
	ImageLoader imageLoader;
	CropParams mCropParams;
	ImageView targetImageView;
	ImageView headpic1,headpic2;
	private String headPath;
	private String path1,path2;
	private int uploadImageType;// 1:头像  2:身份证
	IRegistPresenter rigist = new IRegistPresenter(this) {
		public void onRigistOk(RegistResultBean bean) {
			super.onRigistOk(bean);
			Toast.makeText(AdviserAccreditateActivity.this, "上传头像成功", Toast.LENGTH_SHORT).show();

		};
		public void onUpdateHead() {
			startActivity(new Intent(getContext(), LoginActivity.class));
		};
		public void onUpdateHeadError(){
			Toast.makeText(AdviserAccreditateActivity.this, "上传头像失败", Toast.LENGTH_SHORT).show();
		}
	};
	IFileUploadPresenter mIFileUploadPresenter = new IFileUploadPresenter(this) {
		public void onSuccessed(String jsonData) {
//			super.onSuccessed(jsonData);
			try {
				JSONObject object = new JSONObject(jsonData);
				String filePath = object.optString("filename");
				if (uploadImageType == 1)					
					InvestmentAdvisorCertificationActivity.ParamsMap.put("headImage", filePath);
				else
					InvestmentAdvisorCertificationActivity.ParamsMap.put("identityImage", filePath);
				Bitmap bitmap = CropHelper.decodeUriAsBitmap(AdviserAccreditateActivity.this, mCropParams.uri);
				targetImageView.setImageBitmap(bitmap);
				UserInfo user = UserInfo.getInstance();
//				rigist.updateHead(filePath, user.getUserId());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Toast.makeText(AdviserAccreditateActivity.this, "上传头像失败", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		};
		public void onFailed() {
			Toast.makeText(AdviserAccreditateActivity.this, "上传头像失败", Toast.LENGTH_SHORT).show();
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		headpic1 = new ImageView(this);
		headpic2 = new ImageView(this);
		imageLoader = new ImageLoader(this);
		intent = getIntent();
		mCropParams = new CropParams();
		content.removeAllViews();
		content.addView(getView());
		setTitle("投资顾问认证");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CropHelper.REQUEST_CROP || requestCode == CropHelper.REQUEST_CAMERA){
			 CropHelper.handleResult(this, requestCode, resultCode, data);
		 }
	}
	
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
	
	private ScrollView getView(){
		ScrollView scrollView = new ScrollView(getContext());
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);		
		
		LinearLayout layout = new LinearLayout(this);		
		layout.setOrientation(LinearLayout.VERTICAL);
		scrollView.addView(layout,p);
		
		
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Function.getFitPx(this, 75));
		LinearLayout headLayout = new LinearLayout(this);
		headLayout.setBackgroundColor(Color.WHITE);
		p.setMargins(0, Function.getFitPx(this, 40), 0, 0);
		layout.addView(headLayout,p);
		
		p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		TextView tv = new TextView(this);
		tv.setText("本人头像");
		tv.setGravity(Gravity.CENTER_VERTICAL);
		tv.setBackgroundResource(R.drawable.adviser_accredidate_title);
		tv.setTextColor(Color.WHITE);
//		tv.setBackgroundColor(getResources().getColor(R.color.font_dd3030));
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(this, 40));
		
		headLayout.addView(tv,p);
		
		RelativeLayout picLayout = new RelativeLayout(this);
		picLayout.setBackgroundColor(Color.WHITE);
		picLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Function.getFitPx(this, 320));
		layout.addView(picLayout,p);
		RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(Function.getFitPx(this, 300), Function.getFitPx(this, 300));
		rp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//		ImageView headpic = new ImageView(this);
		headpic1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				targetImageView = (ImageView)v;
				uploadImageType = 1;
				headClicked();
			}
		});
		imageLoader.downLoadImage(InvestmentAdvisorCertificationActivity.ParamsMap.get("headImage"), headpic1);
//		if (InvestmentAdvisorCertificationActivity.ParamsMap.get("headImage") != null && !TextUtils.isEmpty(InvestmentAdvisorCertificationActivity.ParamsMap.get("headImage")))
//			imageLoader.downLoadImage(InvestmentAdvisorCertificationActivity.ParamsMap.get("headImage"), headpic1);
			headpic1.setScaleType(ScaleType.CENTER_CROP);
		picLayout.addView(headpic1,rp);
		
		rp = new RelativeLayout.LayoutParams(Function.getFitPx(this, 300), Function.getFitPx(getContext(), 60));
		tv = new TextView(this);
		tv.setGravity(Gravity.CENTER);
		tv.setText("点击更换头像");
		tv.setTextColor(getResources().getColor(R.color.font_595959));
		tv.setBackgroundColor(0x88ffffff);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(this, 36));
		rp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		picLayout.addView(tv,rp);
		
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		LinearLayout textlayout = new LinearLayout(this);
		textlayout.setOrientation(LinearLayout.VERTICAL);
		textlayout.setBackgroundColor(Color.WHITE);
		p.setMargins(0, 0, 0, 0);
		layout.addView(textlayout,p);
		
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		tv = new TextView(this);
		tv.setGravity(Gravity.CENTER);
		tv.setText("要求");
		tv.setTextColor(getResources().getColor(R.color.font_595959));		
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(this, 40));
		p.setMargins(0, Function.getFitPx(this, 20), 0, Function.getFitPx(this, 20));
		textlayout.addView(tv,p);
		
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		LinearLayout tLayout = new LinearLayout(getContext());
		tLayout.setBackgroundColor(0xffe9e9e9);
		textlayout.addView(tLayout,p);
		
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		tv = new TextView(this);
		tv.setBackgroundColor(Color.WHITE);
//		tv.setGravity(Gravity.CENTER);
		tv.setText("免冠，面部清晰可见，用于投顾审核及应用中的个人展示");
		tv.setTextColor(getResources().getColor(R.color.font_595959));		
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(this, 40));
		tv.setPadding(Function.getFitPx(this, 20), 0, Function.getFitPx(this, 20), Function.getFitPx(this, 20));
		p.setMargins(0, 0, 0, 1);
		tLayout.addView(tv,p);
		
		////////////////////////////////////
		
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Function.getFitPx(this, 75));
		headLayout = new LinearLayout(this);
		headLayout.setBackgroundColor(Color.WHITE);
//		p.setMargins(0, 1, 0, 0);
		layout.addView(headLayout,p);
		
		p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		tv = new TextView(this);
		tv.setText("身份证照片");
		tv.setTextColor(Color.WHITE);
		tv.setGravity(Gravity.CENTER_VERTICAL);
		tv.setBackgroundResource(R.drawable.adviser_accredidate_title);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(this, 40));
		
		headLayout.addView(tv,p);
		
		picLayout = new RelativeLayout(this);
		picLayout.setBackgroundColor(Color.WHITE);
		picLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Function.getFitPx(this, 320));
		layout.addView(picLayout,p);
		rp = new RelativeLayout.LayoutParams(Function.getFitPx(this, 300), Function.getFitPx(this, 300));
		rp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//		headpic = new ImageView(this);
		headpic2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				targetImageView = (ImageView)v;
				uploadImageType = 2;
				headClicked();
			}
		});
//		if (InvestmentAdvisorCertificationActivity.ParamsMap.get("identityImage") != null && !TextUtils.isEmpty(InvestmentAdvisorCertificationActivity.ParamsMap.get("identityImage")))
		imageLoader.downLoadImage(InvestmentAdvisorCertificationActivity.ParamsMap.get("identityImage"), headpic2);
		headpic2.setScaleType(ScaleType.CENTER_CROP);
		picLayout.addView(headpic2,rp);
		
		rp = new RelativeLayout.LayoutParams(Function.getFitPx(this, 300), Function.getFitPx(getContext(), 60));
		tv = new TextView(this);
		tv.setGravity(Gravity.CENTER);
		tv.setText("点击更换头像");
		tv.setTextColor(getResources().getColor(R.color.font_595959));
		tv.setBackgroundColor(0x88ffffff);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(this, 36));
		rp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		picLayout.addView(tv,rp);
		
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		textlayout = new LinearLayout(this);
		textlayout.setOrientation(LinearLayout.VERTICAL);
		textlayout.setBackgroundColor(Color.WHITE);
		p.setMargins(0, 0, 0, 0);
		layout.addView(textlayout,p);
		
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		tv = new TextView(this);
		tv.setGravity(Gravity.CENTER);
		tv.setText("要求");
		tv.setTextColor(getResources().getColor(R.color.font_595959));		
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(this, 40));
		p.setMargins(0, Function.getFitPx(this, 20), 0, Function.getFitPx(this, 20));
		textlayout.addView(tv,p);
		
		p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		tv = new TextView(this);
//		tv.setGravity(Gravity.CENTER);
		tv.setText("须本人手持身份证，保证头像清晰可辨析，保证身份证信息清晰可见");
		tv.setTextColor(getResources().getColor(R.color.font_595959));		
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(this, 40));
		p.setMargins(Function.getFitPx(this, 20), 0, Function.getFitPx(this, 20), Function.getFitPx(this, 20));
		textlayout.addView(tv,p);
		
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		LinearLayout nextLayout = new LinearLayout(this);
//		nextLayout.setBackgroundColor(Color.GRAY);
		p.setMargins(Function.getFitPx(this, 40), Function.getFitPx(this, 120), Function.getFitPx(this, 40), Function.getFitPx(this, 40));
		layout.addView(nextLayout,p);
		
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		tv = new TextView(this);
		tv.setBackgroundColor(Color.WHITE);
		tv.setBackgroundResource(R.drawable.selector_login_button);
		int pad=getResources().getDimensionPixelSize(R.dimen.base_btn_padding);		
		tv.setPadding(pad, pad, pad, pad);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,19);
		tv.setGravity(Gravity.CENTER);
		tv.setText("下一步");
		tv.setTextColor(getResources().getColor(R.color.font_de3031));		
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub	
				String imgsString = InvestmentAdvisorCertificationActivity.ParamsMap.get("headImage");
				if (imgsString == null || imgsString.indexOf("i5")!=-1){
					Toast.makeText(getContext(), "请上传头像", Toast.LENGTH_SHORT).show();
					return;
				}
				imgsString = InvestmentAdvisorCertificationActivity.ParamsMap.get("identityImage");
				if (imgsString == null || imgsString.indexOf("i5")!=-1){
					Toast.makeText(getContext(), "请上传身份证照片", Toast.LENGTH_SHORT).show();
					return;
				}
				Intent intent = new Intent();
				intent.setClass(AdviserAccreditateActivity.this, AdviserAccreditateSkillActivity.class);
				startActivity(intent);
			}
		});
		p.setMargins(1, 1, 1, 1);
		nextLayout.addView(tv,p);
		scrollView.scrollTo(0, 0);
		return scrollView;
	}

	@Override
	public Activity getContext() {
		return this;
	}

	@Override
	public void onPhotoCropped(Uri uri) {
		// TODO Auto-generated method stub
		headPath = mCropParams.uri.getPath();
		if (uploadImageType == 1)
			path1 = mCropParams.uri.getPath();
		else if (uploadImageType == 2)
			path2 = mCropParams.uri.getPath();
		uploadHeadPic();
//		if (targetImageView == null)
//			return;
//		Bitmap bitmap = CropHelper.decodeUriAsBitmap(this, mCropParams.uri);
//		targetImageView.setImageBitmap(bitmap);
	}
	private void uploadHeadPic(){
		if (uploadImageType == 1)
			mIFileUploadPresenter.uploadAccrediationHeadImage(path1);
		else if (uploadImageType == 2)
			mIFileUploadPresenter.uploadAccrediationIdentityImage(path2);
	}

	@Override
	public void onCropCancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCropFailed(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CropParams getCropParams() {
		// TODO Auto-generated method stub
		return mCropParams;
	}

}
