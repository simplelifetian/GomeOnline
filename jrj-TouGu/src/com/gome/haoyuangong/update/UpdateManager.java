
package com.gome.haoyuangong.update;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.gome.haoyuangong.MyApplication;
import com.gome.haoyuangong.R;
import com.gome.haoyuangong.delegate.Helper;
import com.gome.haoyuangong.utils.StringUtils;
import com.gome.haoyuangong.utils.next.Directories;
import com.gome.haoyuangong.utils.next.LogUtil;
import com.gome.haoyuangong.utils.next.PreferenceKeys;
import com.gome.haoyuangong.utils.next.TimeUtil;
import com.gome.haoyuangong.utils.next.Utility;


/**
 * 升级管理类 分自动检测和手动检测
 */
public class UpdateManager implements IAsyncUpdate.OnUpdateListener {
    private final static String TAG = "UpdateManager";

    public static final long DAY = 24 * 60 * 60 * 1000;
    public static final long DEFAULT_UPDATE_INTERVAL = 2;
    public static final int DEFAULT_UPDATE_REMIND_COUNT = 4;

    private final static int REFRESH_PERIOD_MS = 2000;
    private static final long UPDATE_CHECK_INTERVAL = 60 * 60 * 1000;// 24小时
//    private static final long UPDATE_CHECK_INTERVAL = 0;// 24小时
    
    private static final long UPDATE_MARKET_INTERVAL = 30 * DAY;

    private static final int AUTO_SHOW_MAX_COUNT = 3;

    /**
     * the local update infomation
     */
    public static final String KEY_UPDATE_VERSIONCODE = "update_versioncode";
    public static final String KEY_UPDATE_VERSIONNAME = "update_versionname";
    private static final String KEY_UPDATE_DOWNLOADURI = "update_downloaduir";
    private static final String KEY_UPDATE_DESCRIPTION = "update_description";
    private static final String KEY_UPDATE_TYPE = "update_type";
    private static final String KEY_UPDATE_FORCE = "update_force";
    private static final String KEY_UPDATE_MARKETS_ID = "update_markets_id";
    private static final String KEY_UPDATE_DOWNLOAD_TYPE = "update_download_type";
    private static final String KEY_UPDATE_INTERVAL = "update_interval";
    private static final String KEY_UPDATE_REMIND = "update_remind";
    private static final String KEY_UPDATE_INCREMENT_PATCH_URL = "update_patch_url";
    private static final String KEY_UPDATE_APK_MD5 = "update_apk_md5";
    private static final String KEY_UPDATE_SIZE_ORIGINAL = "update_full_size";
    private static final String KEY_UPDATE_SIZE_PATCH = "update_incremental_size";
    /**
     * 升级版本的图文描述 zip文件
     */
    private static final String KEY_UPDATE_PIC_TIPS = "update_pic_tips";

    // 上次调用市场的时间
    // luojianding 2011-12-16
    public static final String KEY_UPDATE_CALL_MARKET = "update_call_market";
    public static final String KEY_UPDATE_NOTIFICATION_AFTER_MARKET = "update_notification_after_market";
    public static final String KEY_UPDATE_CALL_MARKET_SUCCESS = "update_call_market_success";

    // 是否设置过尝鲜版选项
    // luojianding 2012-04-10
    public static final String KEY_IS_SET_UPDATE_BETA_VERSION = "is_set_update_beta_version";

    public static final String KEY_APK_FILE_HASHCODE_MD5 = "apk_file_hash_code_md5";
    public static final String KEY_APK_FILE_PATH = "apk_file_path";
    public static final String KEY_APK_FILE_HASHCODE_VERSION_CODE = "apk_file_hash_code_version_code";

    private static final String KEY_APK_REMIND_COUNT = "apk_remind_count";// 提醒安装的次数
    private static final String KEY_APK_REMIND_LAST_TIME = "apk_remind_last_time";// 提醒安装的最后时间

    private static final String KEY_APK_REMIND_D_COUNT = "apk_remind_d_count";// 提醒下载的次数
    private static final String KEY_APK_REMIND_D_LAST_TIME = "apk_remind_d_last_time";// 提醒下载的最后时间
    
    private static final String KEY_APK_UPDATE_IGNORE = "apk_update_ignore_version";

    /**
     * 小秘书已提醒的版本号
     */
    private static final String KEY_UPDATE_YNCM_LAST_VERSIONCODE = "update_yncm_last_versioncode";

    public static final String ACTION_UPDATE_CHECK_RESULT = "action_update_check_result";

    private IAsyncUpdate mAsyncUpdate;

    private RemoteViews mRemoteViews;
    private PendingIntent mPendingIntent;
    private NotificationManager mManager;
    private Notification mNotification;

    private TimerTask mTimerTask;
    private Timer mTimer;
    private int mProgress;

    /**
     * 显示首页前显示了如引导界面时，如果有升级信息就延迟展示提示
     */
    private boolean mIsDelay;
    private UpdateInfo mDelayInfo;
    private Object mDelaySyncObj = new Object();

    private Context mContext;
    private SharedPreferences mSp;

    private boolean mIsAutoCheck;
    private boolean mIsChecking;
    private boolean mIsDownloading;
    private boolean mIsAutoDownload;
    private int mDownloadVersionCode;

    private BroadcastReceiver mFailReceiver;

    private static UpdateManager mInstance;

    public static UpdateManager getInstance() {
        if (mInstance == null) {
            mInstance = new UpdateManager();
        }
        return mInstance;
    }

    @SuppressWarnings("deprecation")
    private UpdateManager() {
        mContext = MyApplication.get();
        mSp = PreferenceManager.getDefaultSharedPreferences(mContext);
        mAsyncUpdate = new AsyncUpdateOld(mContext);
        mAsyncUpdate.setOnUpdateListener(this);
    }

    /**
     * 手动检测更新
     */
    public void update() {
//        int versioncode = mSp.getInt(KEY_UPDATE_VERSIONCODE, 0);
        if (needUpdateSaved()) {
            UpdateInfo info = getLocalUpdateInfo();
            showUpdateDescription(info, false);
        } else {
            Toast.makeText(mContext, "等待中...",Toast.LENGTH_SHORT).show();
            checkUpdate(false);
        }
    }

    /**
     * 自动检测更新
     */
    public void autoUpdate() {
    	
    	Log.e(TAG, "启动自动升级检查....");
//        int versioncode = mSp.getInt(KEY_UPDATE_VERSIONCODE, 0);
        if (needUpdateSaved()) {
            UpdateInfo info = getLocalUpdateInfo();
            if (info != null) {
                if (info.force) {
                    showUpdateDescription(info, false);
                    return;
                }
                // 若是静默下载未完成，检测更新并继续下载。
//                File file = new File(Directories.getApkPath(info.versionName));
//                if (!file.exists()) {
//                    if (LogUtil.DDBG) {
//                        LogUtil.d(TAG, "autoUpdate info file no exists. downloadtype = "
//                                + info.downloadType);
//                    }
//                    // 网络条件满足的情况才去继续下载的处理
//                    switch (info.downloadType) {
//                        case IAsyncUpdate.SILENCE_DOWNLOAD:
//                            if (!isOnlyWifiOpen() || Utility.isWifi(mContext)) {
//                                checkUpdate(true);
//                                mSp.edit()
//                                        .putLong(PreferenceKeys.KEY_LAST_UPDATE_CHECK_TIME,
//                                                System.currentTimeMillis()).commit();
//                                return;
//                            }
//                            break;
//                        case IAsyncUpdate.SILENCE_DOWNLOAD_WIFI:
//                            if (Utility.isWifi(mContext)) {
//                                checkUpdate(true);
//                                mSp.edit()
//                                        .putLong(PreferenceKeys.KEY_LAST_UPDATE_CHECK_TIME,
//                                                System.currentTimeMillis()).commit();
//                                return;
//                            }
//                            break;
//                    }
//                }

            }
        }
        // 隔断一段时间获取升级信息。
        if (System.currentTimeMillis()
                - mSp.getLong(PreferenceKeys.KEY_LAST_UPDATE_CHECK_TIME, 0) > UPDATE_CHECK_INTERVAL) {
            checkUpdate(true);
//            mSp.edit()
//                    .putLong(PreferenceKeys.KEY_LAST_UPDATE_CHECK_TIME,
//                            System.currentTimeMillis()).commit();
        }
    }

    private void checkUpdate(boolean isAuto) {
        if (isAuto) {
            // 自动检测，若正在检测，或 正在手动下载中，则返回
            if (mIsChecking || (mIsDownloading && !mIsAutoDownload)) {
                return;
            }
            mIsAutoCheck = true;
        } else {
            if (mIsChecking) {
                if (mIsAutoCheck) {
                    mIsAutoCheck = false;
                } else {
                    Toast.makeText(mContext, "更新中...",
                            Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // 手动下载中，提示，并返回
            if (mIsDownloading && !mIsAutoDownload) {
                Toast.makeText(mContext, "下载中...",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            mIsAutoCheck = false;
        }
        setDelay(false);

        mIsChecking = true;
        mAsyncUpdate.checkUpdate(mContext);
    }

    /**
     * 展示升级描述
     */
    public void showUpdateDescription(final UpdateInfo info, boolean isAuto) {
        synchronized (mDelaySyncObj) {
            if (mIsDelay) {
                mDelayInfo = info;
                return;
            }
        }
        final Context context = mContext;
//        if ((AppInfo.getVersionCode(context)).compareTo(info.versionName)>0) {
        if(!needUpdateSaved()){
            // 已是最新版本
            return;
        }
        if (isAuto) {
            String lastTimeKey;
            String countKey;
            // 提醒区分出下载或安装的情况
            File file = new File(Directories.getApkPath(info.versionName));
            if (file.exists()) {
                lastTimeKey = KEY_APK_REMIND_LAST_TIME;
                countKey = KEY_APK_REMIND_COUNT;
            } else {
                lastTimeKey = KEY_APK_REMIND_D_LAST_TIME;
                countKey = KEY_APK_REMIND_D_COUNT;
            }
            // 最后提醒的时间是今天，或已提醒了最大次数，就不提醒了
            if (TimeUtil.isToday(mSp.getLong(lastTimeKey, 0))) {
                if (LogUtil.DDBG) {
                    LogUtil.d(TAG, "showUpdateDescription isAuto. no show. isToady");
                }
                return;
            }
            int count = mSp.getInt(countKey, 0);
            if (count >= AUTO_SHOW_MAX_COUNT) {
                if (LogUtil.DDBG) {
                    LogUtil.d(TAG, "showUpdateDescription isAuto. no show. max count");
                }
                return;
            }
            Editor editor = mSp.edit();
            editor.putLong(lastTimeKey, System.currentTimeMillis());
            editor.putInt(countKey, count + 1);
            editor.commit();
            
            //是否已忽略版本
            String ignoreVersion = mSp.getString(KEY_APK_UPDATE_IGNORE, "");
            if(ignoreVersion.equals(info.versionName+"_"+info.versionCode)){
            	Log.e(TAG, "忽略版本...");
            	return;
            }
        }
        
        Log.e(TAG, "open update dialog...");
        Intent intent = new Intent(context, UpdateDescriptionDialog.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(UpdateDescriptionDialog.KEY_UPDATE_INFO, info);
        context.startActivity(intent);
//        showUpdateDialog(info);
    }
    
    
    private void showUpdateDialog(final UpdateInfo info){
    	
    	String ok = null;
    	String cancel = null;
    	if(info.force){
        	cancel = "退出";
        	ok = "确定";
        }else{
        	cancel = "稍后再说";
        	ok = "立即升级";
        }
    	
    	final Context context = mContext;
		AlertDialog.Builder builder = new AlertDialog.Builder(context)
				.setMessage(info.description)
				.setPositiveButton(ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {
								UpdateManager.getInstance().onOkClick(info);
								dialog.dismiss();
							}
						});
		builder.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {
								UpdateManager.getInstance().onCancelClick(info);
								dialog.dismiss();
							}
						});
		builder.setTitle("发现新版本");
		AlertDialog dialog = builder.create();
		dialog.setCancelable(false);
		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialog.show();
		Button yes = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
		yes.setTextSize(18);
		Button no = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
		no.setTextSize(18);
    }
    
    public void showDialogDelay() {
        if (LogUtil.DDBG) {
            LogUtil.d(TAG, "showDialogDelay:[" + mIsDelay + "]");
        }
        synchronized (mDelaySyncObj) {
            if (mIsDelay && mDelayInfo != null) {
                showUpdateDescription(mDelayInfo, !mDelayInfo.force);
                mDelayInfo = null;
                mIsDelay = false;
            }
        }
    }

    public void setDelay(boolean delay) {
        synchronized (mDelaySyncObj) {
            this.mIsDelay = delay;
        }
    }

    @Override
    public void onCheck(int rst, UpdateInfo info) {
        if (LogUtil.DDBG) {
            LogUtil.d(TAG, "rst: " + rst + "   info: " + ((info == null) ? "null" : info));
        }
        if(!info.force){
			mSp.edit()
            .putLong(PreferenceKeys.KEY_LAST_UPDATE_CHECK_TIME,
                    System.currentTimeMillis()).commit();
		}
        mIsChecking = false;
        int versioncode = mSp.getInt(KEY_UPDATE_VERSIONCODE, 0);
        if (LogUtil.DDBG) {
            LogUtil.d(TAG, "temp versionCode is " + versioncode);
            LogUtil.d(TAG, "app versionCode is " + AppInfo.getVersionCode(mContext));
        }
        if (rst == IAsyncUpdate.SUCCESSED && info != null) {
            updateLocalUpdateInfo(info);// 保存升级信息

            LocalBroadcastManager.getInstance(mContext).sendBroadcast(
                    new Intent(ACTION_UPDATE_CHECK_RESULT));
            if (info.hasUpdate) {
                if (info.versionCode > versioncode) {
                    if (LogUtil.DDBG) {
                        LogUtil.d(TAG, "download type is " + info.downloadType);
                    }
                    // 有新升级信息，清空提醒记录
                    mSp.edit().remove(KEY_APK_REMIND_COUNT).remove(KEY_APK_REMIND_LAST_TIME)
                            .remove(KEY_APK_REMIND_D_COUNT).remove(KEY_APK_REMIND_D_LAST_TIME)
                            .commit();
                }
                // 手动检测或要求强制升级，则直接提示
//                if (!mIsAutoCheck || info.force) {
                    showUpdateDescription(info, false);
                    return;
//                }
                // 若文件已下载好，则根据策略提示；否则，若静默下载条件满足，则去下载，否则根据策略提示
//                File file = new File(Directories.getApkPath(info.versionName));
//                if (file.exists()) {
//                    showUpdateDescription(info, true);
//                } else {
//                    switch (info.downloadType) {
//                        case IAsyncUpdate.NORMAL_DOWNLOAD:
//                            normalDownload(info);
//                            break;
//                        case IAsyncUpdate.SILENCE_DOWNLOAD:
//                            silenceDownloadAny(info);
//                            break;
//                        case IAsyncUpdate.SILENCE_DOWNLOAD_WIFI:
//                            silenceDownloadWifi(info);
//                            break;
//                    }
//                }
//                return;
            }
            // 不需要升级，若是手动检测的，则提示
            if (!mIsAutoCheck) {
                Toast.makeText(mContext, "已经最新",
                        Toast.LENGTH_SHORT).show();
                clearNotification();
            }
            return;
        }

        // 获取升级信息失败，若是手动检测，则提示
        if (!mIsAutoCheck) {
            Toast.makeText(mContext, "网络异常",
                    Toast.LENGTH_SHORT).show();
            clearNotification();
            return;
        }
        // 使用已有升级信息，做提示或静默下载处理
        info = getLocalUpdateInfo();
        if (needUpdate(info.versionCode,info.versionName)) {
            File file = new File(Directories.getApkPath(info.versionName));
            if (file.exists()) {
                showUpdateDescription(info, true);
            } else {
                switch (info.downloadType) {
                    case IAsyncUpdate.NORMAL_DOWNLOAD:
                        normalDownload(info);
                        break;
                    case IAsyncUpdate.SILENCE_DOWNLOAD:
                        silenceDownloadAny(info);
                        break;
                    case IAsyncUpdate.SILENCE_DOWNLOAD_WIFI:
                        silenceDownloadWifi(info);
                        break;
                }
            }
        }

    }

    private void updateLocalUpdateInfo(UpdateInfo info) {
        Editor editor = mSp.edit();
        editor.putInt(KEY_UPDATE_VERSIONCODE, info.versionCode);
        editor.putString(KEY_UPDATE_VERSIONNAME, info.versionName);
        editor.putString(KEY_UPDATE_DOWNLOADURI, info.updateUrl);
        editor.putString(KEY_UPDATE_DESCRIPTION, info.description);
        editor.putInt(KEY_UPDATE_TYPE, info.updateType);
        editor.putBoolean(KEY_UPDATE_FORCE, info.force);
        editor.putInt(KEY_UPDATE_DOWNLOAD_TYPE, info.downloadType);
        editor.putString(KEY_UPDATE_INCREMENT_PATCH_URL, info.patchUrl);
        editor.putString(KEY_UPDATE_APK_MD5, info.md5);
        editor.putLong(KEY_UPDATE_SIZE_ORIGINAL, info.sizeOriginal);
        editor.putLong(KEY_UPDATE_SIZE_PATCH, info.sizePatch);
        if (TextUtils.isEmpty(info.marketsIds)) {
            editor.remove(KEY_UPDATE_MARKETS_ID);
        } else {
            editor.putString(KEY_UPDATE_MARKETS_ID,
                    info.marketsIds);
        }
        if (info.interval > -1) {
            editor.putInt(KEY_UPDATE_INTERVAL, info.interval);
        } else {
            editor.remove(KEY_UPDATE_INTERVAL);
        }

        if (info.remind > 0) {
            editor.putInt(KEY_UPDATE_REMIND, info.remind);
        } else {
            editor.remove(KEY_UPDATE_REMIND);
        }

        editor.putString(KEY_UPDATE_PIC_TIPS, info.picTips);

        editor.commit();
    }

    public UpdateInfo getLocalUpdateInfo() {
        SharedPreferences sp = mSp;
        UpdateInfo infom = new UpdateInfo();
        infom.versionCode = sp.getInt(KEY_UPDATE_VERSIONCODE, 0);
//        infom.hasUpdate = infom.versionName.compareTo(AppInfo.getVersionCode(mContext))>0;
        infom.hasUpdate = needUpdateSaved();
        infom.updateUrl = sp.getString(KEY_UPDATE_DOWNLOADURI, "");
        infom.description = sp.getString(KEY_UPDATE_DESCRIPTION, "");
        infom.updateType = sp.getInt(KEY_UPDATE_TYPE, 0);
        infom.force = sp.getBoolean(KEY_UPDATE_FORCE, false);

        infom.versionName = sp.getString(KEY_UPDATE_VERSIONNAME, "");
        infom.marketsIds = sp.getString(KEY_UPDATE_MARKETS_ID, "");
        infom.downloadType = sp.getInt(KEY_UPDATE_DOWNLOAD_TYPE,
                IAsyncUpdate.NORMAL_DOWNLOAD);
        infom.interval = sp.getInt(KEY_UPDATE_INTERVAL, -1);
        infom.remind = sp.getInt(KEY_UPDATE_REMIND, 0);
        infom.patchUrl = sp.getString(KEY_UPDATE_INCREMENT_PATCH_URL, "");
        infom.md5 = sp.getString(KEY_UPDATE_APK_MD5, "");
        infom.sizeOriginal = sp.getLong(KEY_UPDATE_SIZE_ORIGINAL, 0);
        infom.sizePatch = sp.getLong(KEY_UPDATE_SIZE_PATCH, 0);
        infom.picTips = sp.getString(KEY_UPDATE_PIC_TIPS, "");

        return infom;
    }

    @Override
    public void onDownloadFinish(int rst, File file, UpdateInfo info) {
        mIsDownloading = false;
        if (LogUtil.DDBG) {
            LogUtil.d(TAG, "onDownloadFinish: rst = " + rst);
        }
        if (rst == IAsyncUpdate.SUCCESSED) {
            if (LogUtil.DDBG) {
                LogUtil.d(TAG, "onDownloadFinish: downloadType = " + info.downloadType);
            }
            // 下载成功，若是手动下载，则去安装；否则按策略提示并show通知
            if (mIsAutoDownload) {
                showUpdateDescription(info, true);
                setupInstallNotification(file);
            } else {
                installAPK(file);
                clearNotification();
                if (info.force) {
                    if (LogUtil.DDBG) {
                        LogUtil.d(TAG, "SYSTEM WILL EXIT!!!!!!!!!!!!!!!!!");
                    }
                    if (Helper.isYouniVisibility()) {
                        Helper.clearYouniActivity();
                    }
                }
            }
        } else {
            // 下载失败，若是手动下载的，则通知失败
            if (!mIsAutoDownload) {
                setupFailNotification();
            }
        }
    }

    @Override
    public void onDownloadProgress(int progress) {
    	Log.e(TAG, "downloading ..."+progress);
        mProgress = progress;
    }

    private void installAPK(File file) {
        final Context context = mContext;
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void normalDownload(final UpdateInfo info) {
        if (info.updateType == IAsyncUpdate.POPUP_UPDATE) {
            showUpdateDescription(info, true);
        }
    }

    private void silenceDownload(UpdateInfo info) {
        // 正在下载当前版本，则返回
        if (mIsDownloading && mDownloadVersionCode == info.versionCode) {
            return;
        }
        String apkPath = Directories.getApkPath(info.versionName);
        File apkFile = new File(apkPath);
        if (LogUtil.DDBG) {
            LogUtil.d(TAG, "silenceDownload:" + apkFile.exists());
        }
        if (apkFile.exists()) {
            showUpdateDescription(info, true);
        } else {
            mIsDownloading = true;
            mDownloadVersionCode = info.versionCode;
            mIsAutoDownload = true;
            mAsyncUpdate.setOnUpdateListener(UpdateManager.this);
            mAsyncUpdate.downloadApk(info);
        }
    }

    private void silenceDownloadWifi(UpdateInfo info) {
        if (Utility.isWifi(mContext)) {
            silenceDownload(info);
        } else {
            normalDownload(info);
        }
    }

    private void silenceDownloadAny(UpdateInfo info) {
        if (isOnlyWifiOpen()) {
            silenceDownloadWifi(info);
        } else {
            silenceDownload(info);
        }
    }

    private boolean isOnlyWifiOpen() {
        SharedPreferences sp = Helper.getUISharedPreferences(mContext);
        // 仅wifi静默下载更新，未打开则直接静默下载
//        if (sp.getInt(PreferenceKeys.KEY_UPDATE_SILENCE_WIFI_DOWNLOAD_SWITCH2,
//                SettingsConstants.OFF) == SettingsConstants.ON) {
//            return true;
//        }
        return false;
    }

    /**
     * 是否只是wifi下可下载
     * 
     * @param info
     * @return
     */
    public boolean isOnlyWifiDownload(UpdateInfo info) {
        if (!mIsAutoDownload) {
            return false;
        }
        switch (info.downloadType) {
            case IAsyncUpdate.SILENCE_DOWNLOAD:
                if (isOnlyWifiOpen()) {
                    return true;
                }
                break;
            case IAsyncUpdate.SILENCE_DOWNLOAD_WIFI:
                return true;
        }
        return false;
    }

    /**
     * 提示框点击取消
     * 
     * @param info
     */
    public void onCancelClick(final UpdateInfo info) {
        if (!TextUtils.isEmpty(info.picTips) && info.picTips.contains(".zip")) {
            mAsyncUpdate.downloadPicTips(info);
        }

        if (info.force) {
            if (LogUtil.DDBG) {
                LogUtil.d(TAG, "SYSTEM WILL EXIT!!!!!!!!!!!!!!!!!");
            }
//            if (Helper.isYouniVisibility()) {
//                Helper.clearYouniActivity();
//            }
            
//            if(YouniDelegate.isRunning() && YouniActivity.sInstance != null){
//            	if(YouniActivity.sInstance.get() != null){
//            		YouniActivity.sInstance.get().finish();
//            	}
//            }
            
            if (MyApplication.isTouguVisibility()) {
            	MyApplication.clearTouguActivity();
            }
            
            if(MyApplication.get() != null){
//            	MyApplication.get().finishAllTradeActivity();
            }
            
            return;
        }
        //保存忽略版本
        mSp.edit().putString(KEY_APK_UPDATE_IGNORE, info.versionName+"_"+info.versionCode).commit();
        // 点击取消后，插入小秘书提醒升级的消息，同一版本一次
//        int yncmLastCode = mSp.getInt(KEY_UPDATE_YNCM_LAST_VERSIONCODE, 0);
//        if (yncmLastCode < info.versionCode) {
//            new Thread() {
//                public void run() {
//                    UpdateYNCM updateYNCM = new UpdateYNCM(info, mContext);
//                    MessageCenter.insertMessage(mContext, updateYNCM);
//                }
//            }.start();
//            mSp.edit().putInt(KEY_UPDATE_YNCM_LAST_VERSIONCODE, info.versionCode).commit();
//        }
    }

    /**
     * 提示框点击ok（下载或安装）
     * 
     * @param info
     */
    public void onOkClick(UpdateInfo info) {
        if (!needUpdate(info.versionCode,info.versionName)) {
            // 已经是最新版本了
            return;
        }
        File file = new File(Directories.getApkPath(info.versionName));
        if (file.exists()) {
            final Intent intent = new Intent(
                    Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);

            if (info.force) {
                if (LogUtil.DDBG) {
                    LogUtil.d(TAG, "SYSTEM WILL EXIT!!!!!!!!!!!!!!!!!");
                }
//                if (Helper.isYouniVisibility()) {
//                    Helper.clearYouniActivity();
//                }
                
                if (MyApplication.isTouguVisibility()) {
                	MyApplication.clearTouguActivity();
                }
                
                if(MyApplication.get() != null){
//                	MyApplication.get().finishAllTradeActivity();
                }
            }
        } else {
//            if (System.currentTimeMillis()
//                    - mSp.getLong(KEY_UPDATE_CALL_MARKET, 0) > UPDATE_MARKET_INTERVAL
//                    || mSp.getBoolean(
//                            KEY_UPDATE_CALL_MARKET_SUCCESS,
//                            false)) {
//                switch (marketInstalled(info.marketsIds)) {
//                    case 1:
//                    case 2:
//                    case 3:
//                    case 4:
//                    case 5:
//                    case 6:
//                    case 7:
//                    case 8:
//                    case 9:
//                        Intent intent = new Intent(Intent.ACTION_VIEW,
//                                Uri.parse("market://details?id=com.snda.youni"));
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        mContext.startActivity(intent);
//                        mSp.edit()
//                                .putLong(KEY_UPDATE_CALL_MARKET,
//                                        System.currentTimeMillis()).commit();
//                        mSp.edit()
//                                .putBoolean(
//                                        KEY_UPDATE_NOTIFICATION_AFTER_MARKET,
//                                        true).commit();
//                        return;
//                }
//            }
            // 正在下载该版本，若是之前非自动下载的则toast提示下载中，否则表识切为手动下载
            if (mIsDownloading && info.versionCode == mDownloadVersionCode) {
                if (mIsAutoDownload) {
                    mIsAutoDownload = false;
                    setupNotification();
                } else {
                    Toast.makeText(mContext, "更新正在进行中。。。", Toast.LENGTH_SHORT).show();
                }
                
                if (info.force) {
                    if (LogUtil.DDBG) {
                        LogUtil.d(TAG, "SYSTEM WILL EXIT!!!!!!!!!!!!!!!!!");
                    }
                    
                    if (MyApplication.isTouguVisibility()) {
                    	MyApplication.clearTouguActivity();
                    }
                    
                    if(MyApplication.get() != null){
//                    	MyApplication.get().finishAllTradeActivity();
                    }
                }
                
                return;
            }
            mIsDownloading = true;
            mIsAutoDownload = false;
            mDownloadVersionCode = info.versionCode;
            mAsyncUpdate.setOnUpdateListener(UpdateManager.this);
            mAsyncUpdate.downloadApk(info);
            setupNotification();
            
            if (info.force) {
                if (LogUtil.DDBG) {
                    LogUtil.d(TAG, "SYSTEM WILL EXIT!!!!!!!!!!!!!!!!!");
                }
                
                if (MyApplication.isTouguVisibility()) {
                	MyApplication.clearTouguActivity();
                }
                
                if(MyApplication.get() != null){
//                	MyApplication.get().finishAllTradeActivity();
                }
                
                return;
            }
        }
    }
    
    private int marketInstalled(String marketsId) {
        if (TextUtils.isEmpty(marketsId)) {
            return 0;
        }
        HashMap<String, String> marketMap = new HashMap<String, String>();
        marketMap.put("com.android.vending", "1");
        marketMap.put("com.nd.assistance", "2");
        marketMap.put("com.hiapk.marketpho", "3");
        marketMap.put("com.yingyonghui.market", "4");
        marketMap.put("com.nduoa.nmarket", "5");
        marketMap.put("cn.goapk.market", "6");
        marketMap.put("com.baidu.appsearch", "7");
        marketMap.put("com.mappn.gfan", "8");
        marketMap.put("com.wandoujia.phoenix2", "9");
        marketMap.put("com.qihoo.appstore", "10");

        String[] ids = marketsId.split(",");
        ArrayList<String> installedMarket = new ArrayList<String>();
        List<PackageInfo> packageInfos = mContext.getPackageManager().getInstalledPackages(
                PackageManager.GET_SIGNATURES);
        for (PackageInfo PI : packageInfos) {
            if (marketMap.containsKey(PI.packageName)) {
                installedMarket.add(marketMap.get(PI.packageName));
            }
        }

        for (int i = 0; i < ids.length; i++) {
            if (installedMarket.contains(ids[i])) {
                return Integer.valueOf(ids[i]);
            }
        }
        return 0;
    }

    private void setupNotification() {
        final Context context = mContext;
        mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        clearNotification();
        mRemoteViews = new RemoteViews(context.getPackageName(),R.layout.update_remote);
        Intent intent = new Intent(context, Notification.class);
        mPendingIntent = PendingIntent.getService(context, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        mNotification = new Notification();
        mNotification.icon = android.R.drawable.stat_sys_download;
        mRemoteViews.setImageViewResource(R.id.update_image,R.drawable.ic_launcher);
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;

        if (mTimer != null) {
            mTimer.cancel();
            // mTimer.purge();
        }
        mTimer = new Timer();
        mTimerTask = new NotificationTimerTask();
        mTimer.schedule(mTimerTask, 0, REFRESH_PERIOD_MS);
    }

    @SuppressWarnings("deprecation")
    private void setupInstallNotification(File file) {
        final Context context = mContext;
        mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        clearNotification();

        mNotification = new Notification();
        mNotification.icon = R.drawable.ic_launcher;
        mNotification.tickerText = "安装";
        mNotification.when = System.currentTimeMillis();
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;

        final Intent notificationIntent = new Intent(Intent.ACTION_VIEW);
        notificationIntent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mNotification.setLatestEventInfo(context,
                "更新安装提示标题",
               "更新安装提示内容", contentIntent);

        mManager.notify(Helper.NOTIFY_ID_UPDATE, mNotification);
    }

    @SuppressWarnings("deprecation")
    private void setupFailNotification() {
        final Context context = mContext;
        String failAction = "action_youni_update_download_fail";
        if (mFailReceiver == null) {
            mFailReceiver = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {
                    onOkClick(getLocalUpdateInfo());
                    if (mFailReceiver != null) {
                        mContext.unregisterReceiver(mFailReceiver);
                        mFailReceiver = null;
                    }
                }
            };
            IntentFilter filter = new IntentFilter(failAction);
            mContext.registerReceiver(mFailReceiver, filter);
        }

        mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        clearNotification();

        mNotification = new Notification();
        mNotification.icon = R.drawable.ic_launcher;
        mNotification.tickerText = "更新失败";
        mNotification.when = System.currentTimeMillis();
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;

        final Intent notificationIntent = new Intent(failAction);
        PendingIntent contentIntent = PendingIntent.getBroadcast(context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mNotification.setLatestEventInfo(context,
                "爱投顾",
                "更新失败", contentIntent);

        mManager.notify(Helper.NOTIFY_ID_UPDATE, mNotification);

//        SoundPlayerImpl.playRaw(context, R.raw.webyouni_online);
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    private void updateNotification(int progress) {
        mRemoteViews.setProgressBar(R.id.down_progress, 100, progress, false);
        mRemoteViews.setTextViewText(R.id.update_tv, progress + "%");
        mNotification.contentView = mRemoteViews;
        mNotification.contentIntent = mPendingIntent;
        mManager.notify(Helper.NOTIFY_ID_UPDATE, mNotification);
    }

    private void clearNotification() {
        if (LogUtil.DDBG) {
            LogUtil.d(TAG, "clearNotification");
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            if (LogUtil.DDBG) {
                LogUtil.d(TAG, "canceled timer task");
            }
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            if (LogUtil.DDBG) {
                LogUtil.d(TAG, "canceled and purged timer");
            }
        }
        if (mManager != null) {
            mManager.cancel(Helper.NOTIFY_ID_UPDATE);
            if (LogUtil.DDBG) {
                LogUtil.d(TAG, "canceled notification manager");
            }
        }
    }

    private class NotificationTimerTask extends TimerTask {

        @Override
        public void run() {
            updateNotification(mProgress);
            if (LogUtil.DDBG) {
                LogUtil.d(TAG, "update: " + String.valueOf(mProgress));
            }
        }

    }

    public boolean needUpdateSaved() {
        int savedVersionCode = mSp.getInt(KEY_UPDATE_VERSIONCODE, 0);
        String savedVersionName = mSp.getString(KEY_UPDATE_VERSIONNAME, "");
        return needUpdate(savedVersionCode,savedVersionName);
    }
    
    public boolean needUpdate(int versionCode,String versionName) {
        
        int appVersionCode = AppInfo.getVersionCode(mContext);
        String appVersionName = AppInfo.getVersionName(mContext);
        
        if(appVersionCode < versionCode ){
        	return true;
        }
        if(compareVersionName(appVersionName,versionName) == -1){
        	return true;
        }
        
        return false;
    }

    /**
     * 
     * @param name1
     * @param name2
     * @return
     */
    public static int compareVersionName(String name1,String name2){
    	if(StringUtils.isEmpty(name1) || StringUtils.isEmpty(name2)){
    		return 0;
    	}
    	
    	String[] vstr1 = name1.split("\\.");
    	String[] vstr2 = name2.split("\\.");
    	if(vstr1.length != vstr2.length){
    		return 0;
    	}
    	
    	for(int i = 0 ; i < vstr1.length ; i++){
    		int v1 = Integer.parseInt(vstr1[i]);
    		int v2 = Integer.parseInt(vstr2[i]);
    		if(v1 > v2){
    			return 1;
    		}else if( v1 < v2){
    			return -1;
    		}
    	}
    	return 0;
    }
}
