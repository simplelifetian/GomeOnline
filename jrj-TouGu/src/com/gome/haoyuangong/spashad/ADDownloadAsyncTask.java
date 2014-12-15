/**
 * 
 */
package com.gome.haoyuangong.spashad;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

/**
 *
 */
public class ADDownloadAsyncTask extends AsyncTask<String, Integer, Bitmap> {

	private static final String TAG = ADDownloadAsyncTask.class.getName();

	private static int CONN_TIMEOUT = 20 * 1000;

	private static int READ_TIMEOUT = 20 * 1000;

	private static final long MIN_SDCARD_SIZE = 1024 * 1024;

	private static final String AD_PATH = "jrj/ylb/splash";

	private ADData adData;

	public ADDownloadAsyncTask(ADData adData) {
		this.adData = adData;
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		// TODO Auto-generated method stub
		if (params == null || params.length == 0) {
			return null;
		}
		File sdRoot = getSDPath();
		if (sdRoot == null) {
			return null;
		}
		File imgdir = new File(sdRoot, AD_PATH);
		if (!imgdir.exists()) {
			imgdir.mkdirs();
		}
		File imgFile = new File(imgdir, adData.getImageFileName());
		File infoFile = new File(imgdir, adData.getInfoFileName());
		
		if(adData.getStatus() != 0){
			DataOutputStream dos = null;
			try{
				dos = new DataOutputStream(new FileOutputStream(infoFile));
				adData.writeObject(dos);
			}catch(Exception e){
				Log.d(TAG, infoFile+" save error!!!");
			}finally{
				if(dos != null){
					try {
						dos.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Log.d(TAG, infoFile+" close error!!!");
					}
				}
			}
			return null;
		}
		
		String imgurl = adData.getImgUrl();
		HttpURLConnection httpconn = null;
		try {
			httpconn = (HttpURLConnection) new URL(imgurl).openConnection();
			httpconn.setConnectTimeout(CONN_TIMEOUT);
			httpconn.setReadTimeout(READ_TIMEOUT);
			httpconn.connect();
			InputStream in = httpconn.getInputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(in);
			in.close();
			// 保存图片
			bitmap.compress(CompressFormat.PNG, 10, new FileOutputStream(imgFile));
			// 保存图片信息
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(infoFile));
			adData.writeObject(dos);
			dos.close();
			return bitmap;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "url format error!!!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "url info read error!!!");

		} finally {
			if (httpconn != null) {
				httpconn.disconnect();
			}
		}
		return null;
	}

	private File getSDPath() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File sdRoot = Environment.getExternalStorageDirectory();
			StatFs statFs = new StatFs(sdRoot.getPath());
			long availableSpare = statFs.getBlockSize() * ((long) statFs.getAvailableBlocks() - 4);
			if (availableSpare > MIN_SDCARD_SIZE) {
				return sdRoot;
			}
		}
		return null;
	}

}
