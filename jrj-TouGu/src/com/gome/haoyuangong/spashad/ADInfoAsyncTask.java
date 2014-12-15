/**
 * 
 */
package com.gome.haoyuangong.spashad;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.gome.haoyuangong.log.Logger;

import android.os.AsyncTask;
import android.util.Log;

/**
 *
 */
public class ADInfoAsyncTask extends AsyncTask<String, Integer, ADData> {

	private static final String TAG = ADInfoAsyncTask.class.getName();
	
	private static int CONN_TIMEOUT = 20 * 1000;

	private static int READ_TIMEOUT = 20 * 1000;

	private ADData currADData;

	public ADInfoAsyncTask(ADData currADData) {
		this.currADData = currADData;
	}

	@Override
	protected ADData doInBackground(String... params) {
		// TODO Auto-generated method stub
		if (params == null || params.length == 0) {
			return null;
		}
		String urlStr = params[0];
		Logger.error(TAG, "ad url ->"+urlStr);
		HttpURLConnection httpconn = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ADData aDData = null;
		try {
			URL url = new URL(urlStr);
			httpconn = (HttpURLConnection) url.openConnection();
			httpconn.setConnectTimeout(CONN_TIMEOUT);
			httpconn.setReadTimeout(READ_TIMEOUT);
			httpconn.connect();
			InputStream in = httpconn.getInputStream();
			byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = in.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			in.close();
			String result = baos.toString();
			if(result != null && result.trim().length() > 0){
				JSONTokener jsonParser = new JSONTokener(result);
				JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
				int retCode = getJsonIntValue(jsonObject, "retCode");
				if(retCode == 1){
					aDData = new ADData();
					aDData.setRetCode(getJsonIntValue(jsonObject, "retCode"));
					aDData.setImgUrl(getJsonStringValue(jsonObject, "imgUrl"));
					aDData.setImgVersion(getJsonIntValue(jsonObject, "imgVersion"));
					String pubdate = getJsonStringValue(jsonObject, "pubDate");
					if(ADUtils.isEmpty(pubdate)){
						aDData.setPubDate(0);
					}else{
						Date d = ADUtils.paserString(pubdate, "yyyyMMddHHmmss");
						aDData.setPubDate(ADUtils.paserDate(d));;
					}
					aDData.setStatus(getJsonIntValue(jsonObject, "status"));
					aDData.setClickType(getJsonIntValue(jsonObject, "clickType"));
					aDData.setClickUrl(getJsonStringValue(jsonObject, "clickUrl"));
					return aDData;
				}
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "url format error!!!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "url info read error!!!");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "json format error!!!");
		} finally {
			if (null != httpconn) {
				httpconn.disconnect();
			}
		}
		return null;
	}

	public Integer getJsonIntValue(JSONObject jsonObject, String fieldName) {
		try {
			return jsonObject.getInt(fieldName);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	public String getJsonStringValue(JSONObject jsonObject, String fieldName) {
		try {
			return jsonObject.getString(fieldName);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	@Override
	public void onPostExecute(ADData aDData) {
		if (null != aDData) {
			//删除旧图
			SplashADService.clear();
			// 下载图片
			ADDownloadAsyncTask aDDownloadAsyncTask = new ADDownloadAsyncTask(aDData);
			aDDownloadAsyncTask.execute("start");			
//			if (currADData != null && currADData.getImgVersion() == aDData.getImgVersion()
//					&& currADData.getStatus() == aDData.getStatus()) {
//				return;
//			} else {
//				//删除旧图
//				SplashADService.clear();
//				// 下载图片
//				ADDownloadAsyncTask aDDownloadAsyncTask = new ADDownloadAsyncTask(aDData);
//				aDDownloadAsyncTask.execute("start");
//			}
		}
	}
}
