package com.gome.haoyuangong.net.volley;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.gome.haoyuangong.log.Logger;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.utils.StringUtils;

public class FileUploadRequest extends Request<String> implements Runnable {

	private String mFilePath;
	private String paramF="file";
	public FileUploadRequest(int method, String url, RequestHandlerListener listener) {
		super(method, url, listener);
	}

	public FileUploadRequest(int method, String url, String filePath, Map<String, String> params, RequestHandlerListener listener) {
		super(method, url, params, listener);
		this.mFilePath = filePath;
	}
	public FileUploadRequest(int method, String url, String filePath, Map<String, String> params, String paramF, RequestHandlerListener listener) {
		super(method, url, params, listener);
		this.mFilePath = filePath;
		this.paramF = paramF;
	}

	public FileUploadRequest(String url, String filePath, RequestHandlerListener listener) {
		super(Method.GET, url, listener);
		this.mFilePath = filePath;
	}
	public FileUploadRequest(String url, String filePath, String paramF,RequestHandlerListener listener) {
		super(Method.GET, url, listener);
		this.mFilePath = filePath;
		this.paramF = paramF;
	}

	protected void init() {

	}

	public void downloadFile(final String urlStr, String filePath) {
		boolean isRequestOk = false;
		HttpClient httpclient = new DefaultHttpClient();
		// 设置通信协议版本
		httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

		// File path= Environment.getExternalStorageDirectory(); //取得SD卡的路径

		// String pathToOurFile = path.getPath()+File.separator+"ak.txt";
		// //uploadfile
		// String urlServer = "http://192.168.1.88/test/upload.php";

		HttpPost httppost = new HttpPost(urlStr);

		MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE); // 文件传输
		if(params!=null){
			for(String key: params.keySet()){
				try {
					mpEntity.addPart(key,new StringBody(params.get(key)));
				} catch (UnsupportedEncodingException e) {
				}
			}
		}
		if (!StringUtils.isEmpty(filePath)) {
			File file = new File(filePath);
			ContentBody cbFile = new FileBody(file);
			mpEntity.addPart(paramF, cbFile); // <input type="file"
																						// name="userfile"
		}
		httppost.setEntity(mpEntity);
		String content = "";
		HttpResponse response;
		try {
			response = httpclient.execute(httppost);

			int httpStatusCode;
			httpStatusCode = response.getStatusLine().getStatusCode();
			HttpEntity resEntity = response.getEntity();
			if (resEntity != null) {
				if (httpStatusCode == HttpStatus.SC_OK) {
					content = EntityUtils.toString(resEntity, HTTP.UTF_8);
					isRequestOk = true;
				} else {
					Logger.info("httpStatusCode", httpStatusCode+"");
				}
			}
			if (resEntity != null) {
				resEntity.consumeContent();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (isRequestOk) {
				onSuccess(content,content);
			} else {
				onFailure(-1, "上传失败");
			}
			onEnd();
		}
		httpclient.getConnectionManager().shutdown();
	}

	public void cancel() {
		super.cancel();
	}

	@Override
	public Object getTargetRequest() {
		return null;
	}

	@Override
	public void setTag(Object obj) {
	}

	@Override
	public void run() {
		downloadFile(url, mFilePath);
	}

}
