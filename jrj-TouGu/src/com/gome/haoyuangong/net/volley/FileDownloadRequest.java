package com.gome.haoyuangong.net.volley;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import com.gome.haoyuangong.log.Logger;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;

public class FileDownloadRequest extends Request<String> implements Runnable{
	
	private String mFilePath;
	private boolean isCancel=false;
	public FileDownloadRequest(int method, String url, RequestHandlerListener listener) {
		super(method, url, listener);
	}

	public FileDownloadRequest(int method, String url, Map<String, String> params, RequestHandlerListener listener) {
		super(method, url, params, listener);
	}

	public FileDownloadRequest( String url, String filePath, RequestHandlerListener listener){
		super(Method.GET, url, listener);
		this.mFilePath = filePath;
	}

	protected void init() {
		
	}
	

	public void downloadFile(final String urlStr, String filePath) {
		OutputStream output = null;
		boolean isDownLoadOk =false;
		try {
			/*
			 * 通过URL取得HttpURLConnection 要网络连接成功，需在AndroidMainfest.xml中进行权限配置
			 * <uses-permission android:name="android.permission.INTERNET" />
			 */
			Logger.info("downloadfile", urlStr);
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 取得inputStream，并将流中的信息写入SDCard
			/*
			 * 写前准备 1.在AndroidMainfest.xml中进行权限配置 <uses-permission
			 * android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
			 * 取得写入SDCard的权限 2.取得SDCard的路径： Environment.getExternalStorageDirectory()
			 * 3.检查要保存的文件上是否已经存在 4.不存在，新建文件夹，新建文件 5.将input流中的信息写入SDCard 6.关闭流
			 */
			String pathName = filePath;// 文件存储路径

			File file = new File(pathName);
			int allSize = conn.getContentLength();
			if(allSize<=0){
//				onFailure(0, "文件下载异常");
//				onEnd();
//				return;
			}
			InputStream input = conn.getInputStream();
			if (file.exists()) {
				file.delete();

			} else {
				int last = filePath.lastIndexOf("/");
				if (last > 0) {
					String dir = filePath.substring(0, last);
					new File(dir).mkdirs();
				}
			}
			file.createNewFile();// 新建文件
			output = new FileOutputStream(file);
			// 读取大文件
			byte[] buffer = new byte[4 * 1024];
			int size=0;
			int addSize=0;
			while ((size=input.read(buffer))!= -1&&!isCancel) {
				output.write(buffer,0,size);
				addSize+=size;
				onProgress(urlStr, addSize, allSize);
			}
			if(isCancel){
				if (file.exists()) {
					file.delete();
				}
				onEnd();
				return;
			}
			isDownLoadOk = true;
			output.flush();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(isDownLoadOk){
					onSuccess(filePath,filePath);
				}else{
					onFailure(-1, "文件下载失败");
				}
				onEnd();
				if(output!=null){
					output.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	
	public void cancel() {
		isCancel = true;
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
