package com.gome.haoyuangong.activity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.gome.haoyuangong.SetupData;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.crop.CropHelper;
import com.gome.haoyuangong.dialog.CustomDialog;
import com.gome.haoyuangong.dialog.CustomDialog.Builder;
import com.gome.haoyuangong.layout.self.BarItem;
import com.gome.haoyuangong.layout.self.Function;
import com.gome.haoyuangong.layout.self.SetupView;
import com.gome.haoyuangong.layout.self.TurnOffItem;
import com.gome.haoyuangong.layout.self.SetupView.BusinessType;
import com.gome.haoyuangong.layout.self.SetupView.IItemClicked;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.XinGeBaseResult;
import com.gome.haoyuangong.net.url.XinGeURL;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.update.UpdateManager;

public class SetupActivity extends ListViewActivity {	
	SetupView infoView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);				
		setPullRefreshEnable(false);
		setPullLoadEnable(false);
		setDividerHeight(0);
		infoView = new SetupView(this);
		if (SetupData.getInstance() != null){
			if (infoView.getItem(BusinessType.btWifi) != null)
				((TurnOffItem)infoView.getItem(BusinessType.btWifi)).setClose(!SetupData.getInstance().getOnlyWifiDown());
		}
		addItems();
//		infoView.getItem(BusinessType.btVersion).setInfoText(getVersion());	
		if (SetupData.getInstance().getFreshFrequency() == 0)
			infoView.getItem(BusinessType.btFreshRate).setInfoText("手动刷新");
		else
			infoView.getItem(BusinessType.btFreshRate).setInfoText(String.valueOf(SetupData.getInstance().getFreshFrequency())+"秒");
		reFresh();
		setTitle("设置");		
	}
	private void saveSetupData(){
		if (SetupData.getInstance() == null)
			return;
		Function.serialize(SetupData.getInstance(), SetupData.SetupFilePath);
	}
	private void addItems(){
		
		infoView.setItemClicked(new IItemClicked() {
			
			@Override
			public void OnItemClicked(BusinessType businessType, BarItem item) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				if (item != null)
					intent.putExtra("name", item.getInfoText());
				if (item instanceof TurnOffItem){
					SetupData.getInstance().setOnlyWifiDown(((TurnOffItem) item).getClose());
					((TurnOffItem) item).setClose(!((TurnOffItem) item).getClose());					
					saveSetupData();
				}
				switch (businessType){
					case btPullMessage:						
						intent.setClass(SetupActivity.this, PullMessageSetupActivity.class);
						break;
					case btThirdPlatform:						
						intent.setClass(SetupActivity.this, BindThirdPlatformActivity.class);
						break;
					case btPhone:						
						intent.setClass(SetupActivity.this, ChangePhoneActivity.class);
						break;	
					case btFreshRate:						
						break;	
					case btFeedBack:						
						intent.setClass(SetupActivity.this, FeedBackActivity.class);
						break;
					case btAbout:
						intent.setClass(SetupActivity.this, AboutActivity.class);
						break;
					case btLogout:
						logOut();
						return;
					case btVersion:
						UpdateManager.getInstance().update();
						return;
					default:
						return;
//						break;
				}
				startActivityForResult(intent, businessType.ordinal());	
			}
		});		
		addItem(infoView);
	}
	private void unRegistDevice() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("deviceToken", UserInfo.getInstance().getDeivceId());
		params.put("deviceType", "3");
		params.put("user_id", "");
		params.put("appid", "tougu");
		params.put("user_type", "3");

		JsonRequest<XinGeBaseResult> request = new JsonRequest<XinGeBaseResult>(
				Method.POST, XinGeURL.REGISTDEVICE, params,
				new RequestHandlerListener<XinGeBaseResult>(getContext()) {

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
					public void onFailure(String id, int code, String str, Object obj) {
					}

					@Override
					public void onSuccess(String id, XinGeBaseResult data) {
						// TODO Auto-generated method stub
					}
				}, XinGeBaseResult.class);
		send(request);

	}
	private void logOut() {
		Dialog dialog;
		CustomDialog.Builder builder = new Builder(this);
		builder.setTitle("提示");
		builder.setMessage("是否退出登录？");
		builder.setPositiveButton("确定", new Dialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				UserInfo.getInstance().clearUserInfo(SetupActivity.this);
				Intent intent = new Intent();
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setClass(SetupActivity.this, MainActivity.class);
				startActivity(intent);
				unRegistDevice();
			}
		});
		builder.setNegativeButton("取消", new Dialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog = builder.create();
		dialog.show();	
		
	}
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
    super.onActivityResult(requestCode, resultCode, data);      
		 if (data == null)
			return;
		 
		 BusinessType businessType = BusinessType.values()[requestCode];
		 infoView.getItem(businessType).setInfoText(data.getStringExtra("returnvalue"));	
		 if (businessType == BusinessType.btFreshRate){
			 SetupData.getInstance().setFreshFrequency(Integer.parseInt(data.getStringExtra("time")));
			 saveSetupData();
		 }
    }
	private String getVersion(){
        PackageInfo pkg;
        try {
            pkg = getPackageManager().getPackageInfo(getApplication().getPackageName(), 0);
            String versionName = pkg.versionName; 
            return versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        } 
     }

}
