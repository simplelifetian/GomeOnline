package com.gome.haoyuangong.update;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

import com.gome.haoyuangong.LogUpdate;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.delegate.Helper;
import com.gome.haoyuangong.net.NetManager;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.utils.next.Directories;
import com.gome.haoyuangong.utils.next.FileUtil;
import com.gome.haoyuangong.utils.next.FileUtils;
import com.gome.haoyuangong.utils.next.LogUtil;
import com.gome.haoyuangong.utils.next.MD5;

/**
 * 异步获取升级信息及异步下载apk包
 * 
 * @author
 */
public class AsyncUpdateOld implements IAsyncUpdate,
		UpdateLogic.DownloadProgressListener {
	private final static String TAG = "AsyncUpdateOld";

	private OnUpdateListener mOnUpdateListener;
	private UpdateLogic mUpdateLogic;
	private DownloadTask mDownloadTask;

	private Context mContext;

	private Handler mHandler;

	private Object mPicTipsSyncObj = new Object();

	public AsyncUpdateOld(Context context) {
		mContext = context;
		mUpdateLogic = new UpdateLogic();
		mUpdateLogic.setOnDownloadProgressListener(this);
	}

	@Override
	public void checkUpdate(final Context context) {
		if (LogUtil.DDBG) {
			LogUtil.d(TAG, "checkUpdate begin");
		}
		boolean needHash = true;
		SharedPreferences sp = Helper.getUISharedPreferences(context);
		String apkHashcode = sp.getString(
				UpdateManager.KEY_APK_FILE_HASHCODE_MD5, "");
		String apkHashVersionCode = sp.getString(
				UpdateManager.KEY_APK_FILE_HASHCODE_VERSION_CODE, "0");
		String apkPath = sp.getString(UpdateManager.KEY_APK_FILE_PATH, "");
		if (apkHashVersionCode == AppInfo.getVersionName(context) && !TextUtils.isEmpty(apkHashcode)) {
			if (!TextUtils.isEmpty(apkPath)&& FileUtil.checkFileExistWithFullPath(context, apkPath)) {
				needHash = false;
			}
		}
		if (needHash && Constants.INCREMENTAL_UPDATE_SUPPORT) {
			if (mHandler == null) {
				mHandler = new Handler(context.getMainLooper()) {
					@Override
					public void handleMessage(Message msg) {
						// 更新数据update数据
						getUpdateInfo();
					}
				};
			}
			new Thread() {
				@Override
				public void run() {
					PackageManager manager = context.getPackageManager();
					try {
						PackageInfo pkgInfo = manager.getPackageInfo(
								context.getPackageName(), 0);
						String versionName = pkgInfo.versionName;
						String oldVersionPath;
						// if(FileUtil.checkFileExistWithFullPath(context,
						// Directories.getApkPath(versionName))){
						// oldVersionPath = Directories.getApkPath(versionName);
						// }else{
						oldVersionPath = pkgInfo.applicationInfo.publicSourceDir;
						// }
						File file = new File(oldVersionPath);
						String md5HashCode = "";
						if (file.canRead()) {
							md5HashCode = MD5.encrypt(file);
						} else if (FileUtil.checkFileExistWithFullPath(context,
								Directories.getApkPath(versionName))) {
							oldVersionPath = Directories
									.getApkPath(versionName);
							file = new File(oldVersionPath);
							if (file.canRead()) {
								md5HashCode = MD5.encrypt(file);
							}
						}
						String versionCode = pkgInfo.versionName;
						if (!TextUtils.isEmpty(md5HashCode)) {
							SharedPreferences sp = Helper
									.getUISharedPreferences(context);
							Editor editor = sp.edit();
							editor.putString(
									UpdateManager.KEY_APK_FILE_HASHCODE_MD5,
									md5HashCode);
							editor.putString(
									UpdateManager.KEY_APK_FILE_HASHCODE_VERSION_CODE,
									versionCode);
							editor.putString(UpdateManager.KEY_APK_FILE_PATH,
									file.getAbsolutePath());
							editor.commit();
						}
					} catch (NameNotFoundException e) {
						e.printStackTrace();
					}
					mHandler.sendEmptyMessage(0);
				}
			}.start();
		} else {
			// 更新数据update数据
			getUpdateInfo();
		}
	}

	@Override
	public void downloadApk(UpdateInfo info) {
		if (LogUtil.DDBG) {
			LogUtil.d(TAG, "downloadApk info: " + info);
		}
		if (mDownloadTask != null
				&& mDownloadTask.getStatus() != AsyncTask.Status.FINISHED) {
			mDownloadTask.cancel(true);
		}
		mDownloadTask = new DownloadTask();
		mDownloadTask.execute(info);
	}

	private class DownloadTask extends AsyncTask<UpdateInfo, Void, Integer> {
		private UpdateInfo updateInfo;

		@Override
		protected Integer doInBackground(UpdateInfo... params) {
			updateInfo = params[0];
			File tmpFile = mUpdateLogic.downloadApk(mContext, updateInfo);
			if (tmpFile == null) {
				return NETWORK_ERROR;
			}

			// picTips文件不要删除
			String picTipsFileName = null;
			if (!TextUtils.isEmpty(updateInfo.picTips)
					&& updateInfo.picTips.contains(".zip")) {
				picTipsFileName = FileUtils
						.getFileNameForUrl(updateInfo.picTips);
			}
			// 如果下载成功，则清除之前的更新包
			File apkDir = new File(Directories.getApkDir());
			if (apkDir.exists()) {
				File[] apks = apkDir.listFiles();
				if (apks != null) {
					for (int i = 0; i < apks.length; i++) {
						if (apks[i].getName().equals(tmpFile.getName())) {
							apks[i].renameTo(new File(Directories
									.getApkPath(updateInfo.versionName)));
							continue;
						} else {
							if (picTipsFileName != null
									&& apks[i].getName().startsWith(
											picTipsFileName)) {
								continue;
							}
							try {
								if (apks[i].exists()) {
									apks[i].delete();
								}
							} catch (SecurityException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}

			// 下载包完成后，下载版本描述图片
			if (picTipsFileName != null) {
				synchronized (mPicTipsSyncObj) {
					final File zipFile = new File(Directories.getApkDir(),
							picTipsFileName);
					if (!zipFile.exists()) {
						FileUtils.downloadToExternal(mContext,
								updateInfo.picTips, Directories.getApkDir());
					}
				}
			}

			return SUCCESSED;
		}

		@Override
		protected void onPostExecute(Integer rst) {
			if (LogUtil.DDBG) {
				LogUtil.d(TAG, "Download finish: " + rst);
			}
			File file = new File(Directories.getApkPath(updateInfo.versionName));
			if (mOnUpdateListener != null) {
				mOnUpdateListener.onDownloadFinish(rst, file, updateInfo);
			}
		}
	}

	@Override
	public void setOnUpdateListener(OnUpdateListener l) {
		mOnUpdateListener = l;
	}

	@Override
	public void onProgress(int ratio) {
		if (mOnUpdateListener != null) {
			mOnUpdateListener.onDownloadProgress(ratio);
		}
	}

	// private HttpCallback<UpdateReqMessage, UpdateRespMessage> updateCallback
	// = new HttpCallback<UpdateReqMessage, UpdateRespMessage>() {
	//
	// @Override
	// public void onExceptionCaught(Exception e, String message) {
	// if (LogUtil.WDBG) {
	// LogUtil.w(TAG, "update on message server: got exception");
	// }
	// e.printStackTrace();
	// if (mOnUpdateListener != null) {
	// mOnUpdateListener.onCheck(NETWORK_ERROR, new UpdateInfo());
	// }
	// }
	//
	// @Override
	// public void onResponse(HttpRequest<UpdateReqMessage> req,
	// HttpResponse<UpdateRespMessage> resp) {
	// UpdateRespMessage respMessage = resp.getRespMessage();
	//
	// if (LogUtil.DDBG) {
	// LogUtil.d(TAG, "resp.result======" + resp.getResult());
	// }
	// if (LogUtil.DDBG) {
	// LogUtil.d(TAG, "respmessage======" + respMessage);
	// }
	// if (resp.getResult() == HttpResponse.RESPONSE_RESULT_OK) {
	// if (respMessage != null) {
	// if (LogUtil.DDBG) {
	// LogUtil.d(TAG, "respmessage resultcode=" + respMessage.getResultCode());
	// }
	// UpdateInfo info = respMessage.getUpdateInfo();
	// if (LogUtil.DDBG) {
	// LogUtil.d(TAG, "info is null:" + (info == null)
	// + "/mOnUpdateListner is null:" + (mOnUpdateListener == null));
	// }
	// if (info != null) {
	// if (mOnUpdateListener != null) {
	// mOnUpdateListener.onCheck(SUCCESSED, info);
	// return;
	// }
	// }
	// }
	// }
	// if (mOnUpdateListener != null) {
	// mOnUpdateListener.onCheck(NETWORK_ERROR, new UpdateInfo());
	// }
	// }
	// };

	private void getUpdateInfo() {
//		AppInfo appInfo = new AppInfo(mContext);
//		UserInfo info = UserInfo.getInstance();
		// String url = "";
//		String url = "http://sjcms.jrj.com.cn/api/app?channelId=inhouse&versionId=0.0.1&deviceId=debug-uuid-C2C129FA-E858-41A6-8623-2235E858FED5&os=android-7.1&uid=141112010044393758&productId=1020021";
		
		String url = LogUpdate.getPostUrl(mContext, "http://sjcms.jrj.com.cn/api/app");
		// String url = "http://172.16.20.78/index.php/api/app?channelId=%s&"
		// + "versionId=%s&deviceId=%s&os=%s"
		// + "&uid=%s&productId=jrj320";
		// url=String.format(
		// url, "self",appInfo.version.name,
		// appInfo.imei, appInfo.deviceName+"-"+appInfo.osVersion,
		// info.getUserId());
		Log.e("test", url);
		JsonRequest<UpdateInfoResult> request = new JsonRequest<UpdateInfoResult>(
				Method.GET, url, new RequestHandlerListener<UpdateInfoResult>(
						mContext) {

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
					public void onFailure(String id, int code, String str,
							Object obj) {
						// TODO Auto-generated method stub
						super.onFailure(id, code, str, obj);
						UpdateInfo info = new UpdateInfo();
						info.hasUpdate = false;
						if (mOnUpdateListener != null) {
							mOnUpdateListener.onCheck(NETWORK_ERROR, info);
						}
					}

					@Override
					public void onSuccess(String id, UpdateInfoResult data) {

						UpdateInfo info = new UpdateInfo();
						info.patchUrl = data.getData().getDiffPackageUrl();
						info.updateUrl = data.getData().getPackageUrl();
						info.updateType = data.getData().getUpdateType();

						if ((TextUtils.isEmpty(info.updateUrl) && TextUtils
								.isEmpty(info.patchUrl))
								|| info.updateType == IAsyncUpdate.NO_UPDATE) {
							info.hasUpdate = false;
						} else {
							info.hasUpdate = true;
							info.force = (info.updateType == IAsyncUpdate.FORCE_UPDATE) ? true
									: false;
							info.versionName = data.getData().getVersionId();
							info.description = data.getData().getDescription();
							info.versionCode = 1;
//							String versionCode = "100";
//							if (versionCode != null) {
//								info.versionCode = Integer
//										.parseInt(versionCode);
//							}
							info.marketsIds = "self";
							info.downloadType = IAsyncUpdate.NORMAL_DOWNLOAD;
							// info.interval =;
							info.remind = 1;
							info.md5 = data.getData().getPackageMd5();
							info.sizeOriginal = data.getData().getPackageSize();
							info.sizePatch = data.getData().getDiffPackageSize();
							info.picTips = "";
						}
						if (mOnUpdateListener != null) {
							mOnUpdateListener.onCheck(SUCCESSED, info);
							return;
						}

					}
				}, UpdateInfoResult.class);

		NetManager mNetManager = new NetManager(mContext);

		mNetManager.send(request);

	}

	/**
	 * 异步下载版本描述图片
	 * 
	 * @param info
	 */
	@Override
	public void downloadPicTips(final UpdateInfo info) {
		if (info == null || TextUtils.isEmpty(info.picTips)
				|| !info.picTips.contains(".zip")) {
			return;
		}
		final File zipFile = new File(Directories.getApkDir(),
				FileUtils.getFileNameForUrl(info.picTips));
		if (zipFile.exists()) {
			return;
		}
		new Thread() {

			@Override
			public void run() {
				synchronized (mPicTipsSyncObj) {
					if (!zipFile.exists()) {
						FileUtils.downloadToExternal(mContext, info.picTips,
								Directories.getApkDir());
					}
				}
			}

		}.start();
	}
}
