package com.gome.haoyuangong.presenter;

import java.io.File;
import java.io.OptionalDataException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.gome.haoyuangong.BaseViewImpl;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.log.Logger;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.url.NetUrlLoginAndRegist;
import com.gome.haoyuangong.net.volley.FileDownloadRequest;
import com.gome.haoyuangong.net.volley.FileUploadRequest;
import com.gome.haoyuangong.utils.FileUtils;
import com.gome.haoyuangong.utils.StringUtils;

public class IFileDownloadPresenter extends IBasePresenter{
	public static final String VOICE_DONWLOAD_PATH=FileUtils.getSdcardPath();
	
	private Map<String, Request> requestMap = new HashMap<String, Request>();
	public IFileDownloadPresenter(BaseViewImpl vImpl) {
		super(vImpl);
	}
	
	public void downloadFile(final String url,String filePath){
		FileDownloadRequest request = new FileDownloadRequest(url,filePath, new RequestHandlerListener<String>(getContext()) {
			@Override
			public void onSuccess(String id, String data) {
				Logger.info("", data);
				onSuccessed(url,data);
				
			}
			@Override
			public void onFailure(String id, int code, String str, Object obj) {
				super.onFailure(id, code, str, obj);
				onFailed();
			}
			@Override
			public void onStart(Request request) {
				super.onStart(request);
//				showDialog(request);
				IFileDownloadPresenter.this.onStart(url);
			}
			@Override
			public void onEnd(Request request) {
				super.onEnd(request);
//				hideDialog(request);
				IFileDownloadPresenter.this.onEnd(url);
				requestMap.remove(url);
			}
		});
		requestMap.put(url, request);
		send(request);
	}
	public void downloadVoice(String url){
		downloadFile(url, FileUtils.getVoiceSdcardPath(url));
	}
	public void onSuccessed(String url,String path){
		
	}
	public void onFailed(){
		
	}
	public void onStart(String url){
		
	}
	public void onEnd(String url){
		
	}
	public void cancelDownload(String url){
		if(requestMap.containsKey(url)){
			Request request = requestMap.get(url);
			if(!request.isSuccess()){
				request.cancel();
			}
		}
	}

}
