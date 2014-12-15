
package com.gome.haoyuangong.update;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.gome.haoyuangong.update.SmartDownloader.OnDownloadListener;
import com.gome.haoyuangong.utils.next.Directories;
import com.gome.haoyuangong.utils.next.LogUtil;
import com.gome.haoyuangong.utils.next.MD5;
import com.gome.haoyuangong.utils.next.PatchUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * 升级文件的下载（支持断点下载）
 * 完整apk的下载、增量文件的下载后与现装apk合并获取新的apk。
 * 
 * @author 
 */
public class UpdateLogic {
    private final static String TAG = "UpdateLogic";

    private final static int DOWNLOAD_BUFFER_SIZE = 65535;
    private final static int MAX_PROGRESS = 100;

    private final static int TIMEOUT_MS = 40000;

    private DownloadProgressListener mDownloadProgressListener;

    public File downloadApk(Context context, UpdateInfo info) {
        if (LogUtil.DDBG) {
            LogUtil.d(TAG, "url: " + info.updateUrl);
            LogUtil.d(TAG, "patchurl: " + info.patchUrl);
        }
        String tempFileName = Directories.getApkPath(info.versionName) + ".temp";
        String patchFileName = Directories.getApkPath(info.versionName) + ".patch";
        // 增量文件地址存在，就先下载
        if (!TextUtils.isEmpty(info.patchUrl) && !TextUtils.isEmpty(info.md5)) {
            if (LogUtil.DDBG) {
                LogUtil.d(TAG, "start download patch file");
            }

            File patchFile = smartDownloadFile(info.patchUrl, patchFileName, info.md5,
                    info.sizePatch, info);
            if (LogUtil.DDBG) {
                LogUtil.d(TAG, "finish download patch file");
            }
            if (patchFile != null) {
                try {
                    SharedPreferences sp = Helper.getUISharedPreferences(context);
                    String oldVersionPath = sp.getString(UpdateManager.KEY_APK_FILE_PATH, "");
                    if (!TextUtils.isEmpty(oldVersionPath)) {
                        File oldFile = new File(oldVersionPath);
                        if (LogUtil.DDBG) {
                            LogUtil.d(TAG, "start apply patch file");
                        }
                        if (oldFile.exists() && oldFile.canRead()) {
                            // 合并增量文件和现装包得到新的apk包
                            int ret = PatchUtil.applyPatch(oldVersionPath, tempFileName,
                                    patchFileName);
                            if (ret == 0) {
                                if (LogUtil.DDBG) {
                                    LogUtil.d(TAG, "finish apply patch file");
                                }
                                File tempFile = new File(tempFileName);
                                if (tempFile.exists()) {
                                    String md5Code = MD5.encrypt(tempFile);
                                    if (LogUtil.DDBG) {
                                        LogUtil.d(TAG, "patched file md5 = " + md5Code);
                                    }
                                    if (!TextUtils.isEmpty(info.md5) && md5Code.equals(info.md5)) {
                                        return tempFile;
                                    }
                                }
                            } else {
                                //统计失败
                            }
                        }
                    }
                } finally {
                    if (patchFile.exists()) {
                        patchFile.delete();
                    }
                }

            } else {
                // 增量下载失败则返回
                return null;
            }
        }
        if (LogUtil.DDBG) {
            LogUtil.d(TAG, "patch file not exist or download failed");
        }

        // 完整apk下载
        File entireTmpFile = smartDownloadFile(info.updateUrl, tempFileName, info.md5,
                info.sizeOriginal, info);
        if (entireTmpFile != null && entireTmpFile.canRead()) {
            if (!TextUtils.isEmpty(info.md5)) {
                String md5Code = MD5.encrypt(entireTmpFile);
                if (LogUtil.DDBG) {
                    LogUtil.d(TAG, "tmp file md5 = " + md5Code);
                    LogUtil.d(TAG, "info md5 = " + info.md5);
                }
                if (!TextUtils.isEmpty(info.md5) && md5Code.equals(info.md5)) {
                    return entireTmpFile;
                } else {
                    if (LogUtil.DDBG) {
                        LogUtil.d(TAG, "md5 check failed");
                    }
                    // md5校验失败时，将key update versioncode清除掉，这样在再次升级时，重新从服务器获取升级信息
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                    sp.edit().remove(UpdateManager.KEY_UPDATE_VERSIONCODE).commit();
                    deleteTempAppFile(info.versionName);
                    //统计失败
                    return null;
                }
            }
        }

        return entireTmpFile;
    }

    /**
     * 断点下载文件
     * @param sUrl
     * @param filePath
     * @param flag
     * @param fileLength
     * @return
     */
    public File smartDownloadFile(String sUrl, String filePath, String flag, long fileLength, final UpdateInfo info) {
        SmartDownloader downloader = new SmartDownloader();
        boolean ret = downloader.smartDownloadFile(sUrl, filePath, flag, fileLength,
                new OnDownloadListener() {

                    @Override
                    public void onProgress(int progress) {
                        if (mDownloadProgressListener != null) {
                            mDownloadProgressListener.onProgress(progress);
                        }
                    }

                    @Override
                    public boolean isOnlyWifi() {
                        return UpdateManager.getInstance().isOnlyWifiDownload(info);
                    }
                });
        if (ret) {
            return new File(filePath);
        }
        return null;
    }


	/**
     * 直接下载文件
     * @param sUrl
     * @param filePath
     * @return
     * @throws NetworkException
     */
    public File downloadFile(String sUrl, String filePath) throws NetworkException {
        File fileOut;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            URL url;
            url = new URL(sUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(TIMEOUT_MS);
            con.setReadTimeout(TIMEOUT_MS);
            if (HttpURLConnection.HTTP_OK == con.getResponseCode()) {

                int fileLength = con.getContentLength();
                if (fileLength <= 0) {
                    throw new NetworkException();
                }

                int downloadCount = 0;

                is = con.getInputStream();
                fileOut = new File(filePath);
                if (!fileOut.exists()) {
                    fileOut.createNewFile();
                }
                fos = new FileOutputStream(fileOut);
                byte[] bytes = new byte[DOWNLOAD_BUFFER_SIZE];
                int cnt;
                while ((cnt = is.read(bytes)) != -1) {
                    fos.write(bytes, 0, cnt);
                    downloadCount = downloadCount + cnt;
                    if (mDownloadProgressListener != null) {
                        mDownloadProgressListener.onProgress(downloadCount * MAX_PROGRESS
                                / fileLength);
                    }
                    if (LogUtil.DDBG) {
                        LogUtil.d(TAG, "downloaded apk size:" + downloadCount);
                    }
                }
                con.disconnect();

                if (fileLength == 0 || downloadCount != fileLength) {
                    throw new IOException();
                }

            } else {
                throw new NetworkException();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new NetworkException();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            throw new NetworkException();
        } catch (Exception e) {
            if (LogUtil.DDBG) {
                LogUtil.d(TAG, "update catch a new exception");
            }
            e.printStackTrace();
            throw new NetworkException();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return fileOut;
    }

    public void deleteTempAppFile(String versionName) {
        String apkPath = Directories.getApkPath(versionName);
        File file = new File(apkPath + ".temp");
        try {
            if (file.exists()) {
                file.delete();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public AppInfo getTempAppInfo(Context context, String versionName) {
        AppInfo appInfo = null;
        String apkPath = Directories.getApkPath(versionName);
        PackageManager pm = context.getPackageManager();
        if (pm == null) {
            return null;
        }
        PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            appInfo = new AppInfo(info);
        }
        return appInfo;
    }

    public void setOnDownloadProgressListener(DownloadProgressListener l) {
        mDownloadProgressListener = l;
    }

    public interface DownloadProgressListener {
        public void onProgress(int ratio); // the max ratio is 100 as default
    }
}
