package com.gome.haoyuangong.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.crop.CropHandler;
import com.gome.haoyuangong.crop.CropHelper;
import com.gome.haoyuangong.crop.CropParams;
import com.gome.haoyuangong.dialog.BottomDialog;
import com.gome.haoyuangong.dialog.BottomDialog.OnConfirmListener;
import com.gome.haoyuangong.layout.self.BarItem;
import com.gome.haoyuangong.layout.self.Function;
import com.gome.haoyuangong.layout.self.SelfInfo;
import com.gome.haoyuangong.layout.self.SelfInfo.BusinessType;
import com.gome.haoyuangong.layout.self.SelfInfo.IItemClicked;
import com.gome.haoyuangong.layout.self.SelfView.UserType;
import com.gome.haoyuangong.layout.self.data.AdvisorCertification;
import com.gome.haoyuangong.layout.self.data.InvestAdviserInfo;
import com.gome.haoyuangong.layout.self.data.InvestAdviserListInfo;
import com.gome.haoyuangong.layout.self.data.InvestAdviserInfo.Item;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.TouguBaseResult;
import com.gome.haoyuangong.net.result.tougu.RegistResultBean;
import com.gome.haoyuangong.net.url.NetUrlLoginAndRegist;
import com.gome.haoyuangong.net.url.NetUrlMyInfo;
import com.gome.haoyuangong.net.volley.ImageLoader;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.presenter.IFileUploadPresenter;
import com.gome.haoyuangong.presenter.IRegistPresenter;
import com.gome.haoyuangong.utils.FileUtils;
import com.gome.haoyuangong.utils.StringUtils;

public class MySelfInfoActivity extends ListViewActivity implements CropHandler {	
	SelfInfo infoView;
	CropParams mCropParams;
	ImageLoader imageLoader;
	private String headPath;
	int REACCEDIATION = 10001;
	boolean showwait=false;	
	IRegistPresenter rigist = new IRegistPresenter(this) {
		public void onRigistOk(RegistResultBean bean) {
			super.onRigistOk(bean);
			Toast.makeText(MySelfInfoActivity.this, "上传头像成功", Toast.LENGTH_SHORT).show();

		};
		public void onUpdateHead() {
			Toast.makeText(MySelfInfoActivity.this, "更新头像成功", Toast.LENGTH_SHORT).show();
			startActivity(new Intent(getContext(), LoginActivity.class));
		};
		public void onUpdateHeadError(){
			Toast.makeText(MySelfInfoActivity.this, "上传头像失败", Toast.LENGTH_SHORT).show();
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		imageLoader = new ImageLoader(this);
		setPullRefreshEnable(false);
		setPullLoadEnable(false);
		mCropParams = new CropParams();
		addItems();
		reFresh();
		setTitle("个人信息");
		setDividerHeight(0);
		if (UserInfo.getInstance().isTougu() || SelfInfo.userType == UserType.utUserViewAdviser)
			requestData();
		else {
			fillUserData();
		}
		doAdvisorCertification(true);
	}

	private void addItems(){
		infoView = new SelfInfo(this);
		infoView.setIsUser(false);
		infoView.setItemClicked(new IItemClicked() {
			
			@Override
			public void OnItemClicked(BusinessType businessType, BarItem item) {
				// TODO Auto-generated method stub
				if (businessType == BusinessType.btNone)
					return;
				Intent intent = new Intent();
				intent.putExtra("name", item.getInfoText());
				switch (businessType){
					case btHeadPic:
						if (UserInfo.getInstance().isLogin() && UserInfo.getInstance().isTougu())
							return;
						if (SelfInfo.userType == UserType.utUserViewAdviser)
							return;
						headClicked();
						return;
//					case btNikeName:						
//						intent.setClass(MySelfInfoActivity.this, EditNameActivity.class);
//						break;
//					case btAddress:
//						intent.putExtra("viewtype", "pinyin");
//						intent.setClass(MySelfInfoActivity.this, SelectAddressActivity.class);
//						break;
//					case btOrgnization:
//						intent.putExtra("viewtype", "pinyin");
//						intent.setClass(MySelfInfoActivity.this, SelectOrgnizationActivity.class);
//						break;
//					case btDepartment:
//						intent.setClass(MySelfInfoActivity.this, SelectDepartmentActivity.class);
//						break;
//					case btWorkLimit:
//						intent.setClass(MySelfInfoActivity.this, SelectWorkLimitActivity.class);
//						break;
					case btBrief:
						intent.putExtra(EditBriefActivity.BRIEFCONTENT, item.getInfoText());
						intent.putExtra(EditBriefActivity.TITLE, "简介");
						intent.setClass(MySelfInfoActivity.this, EditBriefActivity.class);
						break;
					case btApplyAdviser:
						doAdvisorCertification(false);
						return;
					case btSkill:
						intent.putExtra(EditBriefActivity.BRIEFCONTENT, item.getInfoText().replace(" ", ","));
						intent.putExtra(EditBriefActivity.TITLE, "能力标签");
						intent.setClass(MySelfInfoActivity.this, EditBriefActivity.class);
//						intent.putExtra("businessType", businessType.ordinal());						
//						intent.setClass(MySelfInfoActivity.this, SelectSkillActivity.class);
						break;
//					case btExpertArea:
//						intent.putExtra("businessType", businessType.ordinal());
//						intent.putExtra("skillname", "A股");
//						intent.setClass(MySelfInfoActivity.this, SelectSkillActivity.class);
//						break;
					default:
						return;
//						break;
				}
				startActivityForResult(intent, businessType.ordinal());
			}
		});
		addItem(infoView);
	}
	private void doAdvisorCertification(final boolean query){
		String url = String.format(NetUrlMyInfo.AdvisorCertification, UserInfo.getInstance().getUserId());
		JsonRequest<AdvisorCertification> request = new JsonRequest<AdvisorCertification>(Method.GET, url,
				new RequestHandlerListener<AdvisorCertification>(getContext()) {

					@Override
					public void onStart(Request request) {
						super.onStart(request);
						if (showwait)
						 showDialog(request);
					}

					@Override
					public void onEnd(Request request) {
						super.onEnd(request);
						if (showwait)
						 hideDialog(request);
						else
							showwait = true;
					}
					@Override
					public void onFailure(String id, int code, String str,
							Object obj) {
						// TODO Auto-generated method stub
						super.onFailure(id, code, str, obj);
//						Function.showToask(MySelfInfoActivity.this, "操作超时，请稍后再试");
					}
					@SuppressLint("ShowToast") @Override
					public void onSuccess(String id, AdvisorCertification data) {
						// TODO Auto-generated method stub
						try{
							Intent intent = new Intent();
							if (!query){
								//审核中
								if (data.getData().getStatus() == 4){
									AdviserAccreditationActivity.TIPTYPE = 2;
									intent.putExtra("needback", 1);
									intent.setClass(MySelfInfoActivity.this, AdviserAccreditationActivity.class);	
									startActivity(intent);
								}
								//审核失败
								else if (data.getData().getStatus() == 9){
									AdviserAccreditationActivity.TIPTYPE = 3;
									intent.putExtra("needback", 1);
									intent.putExtra("failereason", data.getData().getError_message());
									intent.setClass(MySelfInfoActivity.this, AdviserAccreditationActivity.class);
									startActivity(intent);
//									startActivityForResult(intent, REACCEDIATION);
								}
								//审核通过
								if (data.getData().getStatus() == 5){
									AdviserAccreditationActivity.TIPTYPE = 4;
									intent.putExtra("needback", 1);
									intent.setClass(MySelfInfoActivity.this, AdviserAccreditationActivity.class);	
									startActivity(intent);
								}
								else if (data.getData().getStatus() == 0){
									intent.setClass(MySelfInfoActivity.this, InvestmentAdvisorCertificationActivity.class);
									startActivity(intent);
								}
							}
							else {
								if (data.getData().getStatus() != 0){
									if (infoView.getItem(BusinessType.btApplyAdviser) != null)
										infoView.getItem(BusinessType.btApplyAdviser).setInfoText("查看审核状态");
								}
							}
						}
						catch(Exception e){
						}
						
					}
				}, AdvisorCertification.class);
		send(request);
	}
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
    super.onActivityResult(requestCode, resultCode, data);  
    
	    if (requestCode == CropHelper.REQUEST_CROP || requestCode == CropHelper.REQUEST_CAMERA){
			 CropHelper.handleResult(this, requestCode, resultCode, data);
			 return;
		 }
	    if (requestCode == REACCEDIATION){
	    	Intent intent = new Intent();
			intent.setClass(MySelfInfoActivity.this, InvestmentAdvisorCertificationActivity.class);
			startActivity(intent);	    	
	    	return;
	    }
	    
		 if (data == null)
			return;
		 
		 BusinessType businessType = BusinessType.values()[requestCode];
		 switch (businessType){
			 case btNikeName:
			 case btAddress:
			 case btOrgnization:
			 case btDepartment:
			 case btWorkLimit:
			 case btBrief:
			 case btSkill:
			 case btExpertArea:
				 infoView.getItem(businessType).setInfoText(data.getStringExtra("returnvalue"));	 
				 break;
			 case btHeadPic:
				 CropHelper.handleResult(this, requestCode, resultCode, data);
				 break;
			default:
				break;
		 }
    }
	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		super.onLoad();
		doAdvisorCertification(true);
//		if (UserInfo.getInstance().isTougu())
//			requestData();
//		else {
//			fillUserData();
//		}
	}
	private void fillUserData(){
		if (infoView.getItem(BusinessType.btNikeName) != null)
			infoView.getItem(BusinessType.btNikeName).setInfoText(UserInfo.getInstance().getUserName());
		if (infoView.getItem(BusinessType.btAddress) != null)
			infoView.getItem(BusinessType.btAddress).setInfoText(getIntent().getStringExtra("address"));
		if (infoView.getItem(BusinessType.btHeadPic) != null && getIntent().getStringExtra("headimage") != null){
			if (infoView.getItem(BusinessType.btHeadPic).getTempImageView() != null){
				infoView.getItem(BusinessType.btHeadPic).getTempImageView().setScaleType(ScaleType.FIT_XY);
				imageLoader.downLoadImage(getIntent().getStringExtra("headimage"), infoView.getItem(BusinessType.btHeadPic).getTempImageView());
			}
		}
	}
	protected void requestData() {
		String url = "";
		if (SelfInfo.userType == UserType.utUserViewAdviser)
			url = String.format(NetUrlMyInfo.INVESTERINFOURL, getIntent().getStringExtra("viewid"));
		else
			url = String.format(NetUrlMyInfo.INVESTERINFOURL, UserInfo.getInstance().getUserId());
		JsonRequest<InvestAdviserInfo> request = new JsonRequest<InvestAdviserInfo>(Method.GET, url,
				new RequestHandlerListener<InvestAdviserInfo>(getContext()) {

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
					public void onSuccess(String id, InvestAdviserInfo data) {
						// TODO Auto-generated method stub
						try{
							fillData(data);
						}
						catch(Exception e){
							
						}
						
					}
				}, InvestAdviserInfo.class);
			send(request);
	}
	private void fillData(InvestAdviserInfo info){
		Item item = info.getData();
		infoView.getItem(BusinessType.btNikeName).setInfoText(item.getUserName());
		imageLoader.downLoadImage(item.getHeadImage(), infoView.getItem(BusinessType.btHeadPic).getTempImageView());
		if (item.getSex() == 1)
			infoView.getItem(BusinessType.btSex).setInfoText("男");
		else {
			infoView.getItem(BusinessType.btSex).setInfoText("女");
		}
		if (item.getType() == 1)
			infoView.getItem(BusinessType.btCertification).setVisibility(View.GONE);
		infoView.getItem(BusinessType.btCertification).setInfoText(item.getCertificationNum());
		infoView.getItem(BusinessType.btAddress).setInfoText(item.getProvince());
		infoView.getItem(BusinessType.btOrgnization).setInfoText(item.getCompany());
		infoView.getItem(BusinessType.btPosition).setInfoText(item.getPosition());
		if (item.getExperienceScope() == 1)
			infoView.getItem(BusinessType.btWorkLimit).setInfoText("一年以下");
		else if (item.getExperienceScope() == 2)
			infoView.getItem(BusinessType.btWorkLimit).setInfoText("1~3年");
		else if (item.getExperienceScope() == 3)
			infoView.getItem(BusinessType.btWorkLimit).setInfoText("3~5年");
		else
			infoView.getItem(BusinessType.btWorkLimit).setInfoText("5年以上");
		infoView.getItem(BusinessType.btExpertArea).setInfoText(item.getInvestDirection().replace(",", " "));
		infoView.getItem(BusinessType.btSkill).setInfoText(item.getLabel().replace(",", " "));
		infoView.getItem(BusinessType.btBrief).setInfoText(item.getIntro());
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
	private void uploadHeadPic(){
		mIFileUploadPresenter.uploadHeadIcon(headPath);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onPhotoCropped(Uri uri) {
		headPath = mCropParams.uri.getPath();
		uploadHeadPic();
		infoView.getItem(BusinessType.btHeadPic).addRightImage(CropHelper.decodeUriAsBitmap(this, mCropParams.uri), 200, true);
//		infoView.setHeadPic(CropHelper.decodeUriAsBitmap(this, mCropParams.uri));
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
	
	IFileUploadPresenter mIFileUploadPresenter = new IFileUploadPresenter(this) {
		public void onSuccessed(String jsonData) {
			super.onSuccessed(jsonData);
			rigist.updateHead(headUrl, UserInfo.getInstance().getUserId());
//			rigist.updateHead(NetUrlLoginAndRegist.UPLOAD_PREFIX+headUrl, UserInfo.getInstance().getUserId());
		};
		public void onFailed() {
			Toast.makeText(MySelfInfoActivity.this, "上传头像失败", Toast.LENGTH_SHORT).show();
		};
	};
}
