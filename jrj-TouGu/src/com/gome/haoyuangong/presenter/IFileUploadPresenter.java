package com.gome.haoyuangong.presenter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Handler;

import com.gome.haoyuangong.BaseViewImpl;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.log.Logger;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.url.AskUrl;
import com.gome.haoyuangong.net.url.NetUrlLoginAndRegist;
import com.gome.haoyuangong.net.volley.FileUploadRequest;
import com.gome.haoyuangong.utils.ImageUtils;
import com.gome.haoyuangong.utils.StringUtils;

public class IFileUploadPresenter extends IBasePresenter {
	private static final int MAX_PIC_SIZE = 1024 * 1024;
	public IFileUploadPresenter(BaseViewImpl vImpl) {
		super(vImpl);
	}

	public void uploadFile(String url, String filePath, Map<String, String> params) {
		uploadFile(url, filePath, "file", params,null);
	}
	
	public void uploadFile(String url, String filePath, Map<String, String> params,String msg) {
		uploadFile(url, filePath, "file", params,msg);
	}
	
	public void uploadFile(String url, String filePath, String paramF, Map<String, String> params){
		uploadFile(url, filePath, paramF, params,null);
	}

	public void uploadFile(String url, String filePath, String paramF, Map<String, String> params,final String msg) {
		FileUploadRequest request = new FileUploadRequest(Method.POST, url, filePath, params, paramF, new RequestHandlerListener<String>(getContext()) {
			@Override
			public void onSuccess(String id, String data) {
				Logger.info("", data);
				onSuccessed(data);

			}

			@Override
			public void onFailure(String id, int code, String str, Object obj) {
				super.onFailure(id, code, str, obj);
				onFailed();
			}

			@Override
			public void onStart(Request request) {
				super.onStart(request);
				if(StringUtils.isBlank(msg)){
					showDialog(request,"图片上传中...");
				}else{
					showDialog(request,msg);
				}
			}

			@Override
			public void onEnd(Request request) {
				super.onEnd(request);
				hideDialog(request);
			}
		});
		send(request);
	}

	public void uploadHeadIcon(String file) {
//		UserInfo userInfo = UserInfo.getInstance();
//		if(StringUtils.isEmpty(userInfo.getUserId())||StringUtils.isEmpty(userInfo.getUserName())){
//			 return ;
//		}
		File f = new File(file);
		if (!f.exists())
			return;
		long fileSize = f.length();
		Map<String, String> params = new HashMap<String, String>();
		params.put("channel", "hp");
		params.put("backJson", "1");
		params.put("sizeMax", fileSize + "");
		if(UserInfo.getInstance().isLogin()){
		params.put("creator", UserInfo.getInstance().getUserId());
		params.put("userId", UserInfo.getInstance().getUserId());
		params.put("userName", UserInfo.getInstance().getUserName());
		}
		uploadFile(NetUrlLoginAndRegist.UPLOAD_URL, file, params);
	}

	public void uploadAccrediationHeadImage(String file) {
		UserInfo userInfo = UserInfo.getInstance();
		if (StringUtils.isEmpty(userInfo.getUserId()) || StringUtils.isEmpty(userInfo.getUserName())) {
			return;
		}
		File f = new File(file);
		if (!f.exists())
			return;
		long fileSize = f.length();
		Map<String, String> params = new HashMap<String, String>();
		params.put("channel", "thead_attach");
		params.put("backJson", "1");
		params.put("sizeMax", fileSize + "");
		params.put("userId", userInfo.getUserId());
		params.put("userName", userInfo.getUserName());
		uploadFile(NetUrlLoginAndRegist.UPLOAD_TOUGU_HEAD_URL, file, params);
	}

	public void uploadAccrediationIdentityImage(String file) {
		UserInfo userInfo = UserInfo.getInstance();
		if (StringUtils.isEmpty(userInfo.getUserId()) || StringUtils.isEmpty(userInfo.getUserName())) {
			return;
		}
		File f = new File(file);
		if (!f.exists())
			return;
		long fileSize = f.length();
		Map<String, String> params = new HashMap<String, String>();
		params.put("channel", "tcard_attach");
		params.put("backJson", "1");
		params.put("sizeMax", fileSize + "");
		params.put("userId", userInfo.getUserId());
		params.put("userName", userInfo.getUserName());
		uploadFile(NetUrlLoginAndRegist.UPLOAD_IDENTITY_URL, file, params);
	}

	public void uploadAnswerMedia(int askId, String file, int length) {
		UserInfo userInfo = UserInfo.getInstance();
		if (StringUtils.isEmpty(userInfo.getUserId()) || StringUtils.isEmpty(userInfo.getUserName())) {
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("source ", "爱投顾手机客户端");
		params.put("qid", askId + "");
		params.put("uid", userInfo.getUserId());
		params.put("voiceLength", String.valueOf(length));
		params.put("type", "1");
		uploadFile(AskUrl.ANSWER_MEDIA, file, "f", params);
	}

	public void uploadReAnswerMedia(int reAskId, String file, int length) {
		UserInfo userInfo = UserInfo.getInstance();
		if (StringUtils.isEmpty(userInfo.getUserId()) || StringUtils.isEmpty(userInfo.getUserName())) {
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("source ", "爱投顾手机客户端");
		params.put("qid", reAskId + "");
		params.put("uid", userInfo.getUserId());
		params.put("voiceLength", String.valueOf(length));
		params.put("type", "2");
		uploadFile(AskUrl.ANSWER_MEDIA, file, "f", params);
	}
	/**
	 * 上传回答图片
	 * @param file
	 */
	public void uploadImageNormal(String file) {
		File f = new File(file);
		if (!f.exists())
			return;
		long fileSize = f.length();
		final Map<String, String> params = new HashMap<String, String>();
		params.put("channel", "tg");
		params.put("backJson", "1");
		params.put("sizeMax", fileSize + "");
		picPath = file;
		new Thread(){
			public void run() {
				handler.post(new Runnable() {
					@Override
					public void run() {
						showDialog(null, "处理图片中...");
					}
				});
				picPath = limitPicSize(picPath);
				handler.post(new Runnable() {
					@Override
					public void run() {
						hideDialog(null);
					}
				});
				handler.post(new Runnable() {
					@Override
					public void run() {
						uploadFile(NetUrlLoginAndRegist.UPLOAD_TOUGU_HEAD_URL, picPath, params,"上传图片中...");
					}
				});
			};
		}.start();
	}
	Handler handler = new Handler();
	private String picPath;
	public String headUrl;

	public void onSuccessed(String jsonData) {
		try {
			JSONObject object = new JSONObject(jsonData);
			int flag = object.optInt("flag");
			if (flag == 1) {
				headUrl = object.optString("filename");
				// UserInfo user = UserInfo.getInstance();
				// user.setHeadPath(filePath);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onFailed() {

	}
	private String limitPicSize(String needUpLoad){
		File f = new File(needUpLoad);
		if (!f.exists())
			return needUpLoad;
		long fileSize = f.length();
		if (fileSize > MAX_PIC_SIZE) {
			Bitmap oldPic = ImageUtils.compressImageFromFile(needUpLoad);
			File cacheFile = getContext().getExternalCacheDir();
			File tempImageFile = new File(cacheFile, "opinion_image.jpg");
			FileOutputStream fos = null;
			boolean compSuccess = false;
			try {
				fos = new FileOutputStream(tempImageFile);
				oldPic.compress(CompressFormat.JPEG, 100, fos);
				compSuccess = true;
			} catch (FileNotFoundException e) {
			} finally {
				try {
					fos.close();
					oldPic.recycle();
				} catch (IOException e) {
					// TODO Auto-generated catch block
				}
			}
			if (compSuccess) {
				needUpLoad = tempImageFile.getAbsolutePath();
				f = new File(needUpLoad);
				fileSize = f.length();
			}
		}
		return needUpLoad;
	}

}
