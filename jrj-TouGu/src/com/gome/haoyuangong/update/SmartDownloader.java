
package com.gome.haoyuangong.update;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.gome.haoyuangong.MyApplication;
import com.gome.haoyuangong.utils.next.DeviceUtil;
import com.gome.haoyuangong.utils.next.Utility;

public class SmartDownloader {
    private static final String TAG = "SmartDownloader";

    private final static int DOWNLOAD_BUFFER_SIZE = 65535;
    private final static int TIMEOUT_MS = 30000;

    private final static int RETRY_MAX_COUNT = 2;

    /**
     * 升级下载文件进度 key: prefix_key ＋ url, value: flag + "|" + filePath + "|" + size
     * + | + costTime + "|" + startTime + "|" + networkType + "|" + desc;
     */
    private final static String KEY_PREFIX_SMART_DOWNLOADED_ = "smart_downloaded_";

    private Context mContext;
    private SharedPreferences mPref;

    public SmartDownloader() {
        mContext = MyApplication.get();
        mPref = mContext.getSharedPreferences("smart_download", Context.MODE_PRIVATE);
    }

    /**
     * 获取已下载数据： 已下载的大小、时间、描述
     * 
     * @param url
     * @param filePath
     * @param flag
     * @return
     */
    public DownloadedData getDownloadedData(String url, String filePath, String flag) {
        DownloadedData data = new DownloadedData();
        String key = KEY_PREFIX_SMART_DOWNLOADED_ + url;
        String downloadStr = mPref.getString(key, "");
        if (downloadStr == null || downloadStr.length() == 0) {
            return data;
        }
        String startStr = flag + "|" + filePath + "|";
        if (downloadStr.startsWith(startStr)) {
            String[] dataStr = downloadStr.substring(startStr.length()).split("\\|");
            if (dataStr.length != 5) {
                return data;
            }
            try {
                data.size = Long.parseLong(dataStr[0]);
                data.costTime = Long.parseLong(dataStr[1]);
                data.startTime = Long.parseLong(dataStr[2]);
                data.net = dataStr[3];
                data.desc = dataStr[4];
                return data;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    /**
     * 保存已下载数据： 已下载的大小、时间、描述
     * @param url
     * @param filePath
     * @param flag
     * @param data
     */
    public void saveDownloadedSize(String url, String filePath, String flag, DownloadedData data) {
        String key = KEY_PREFIX_SMART_DOWNLOADED_ + url;
        StringBuilder sb = new StringBuilder();
        sb.append(flag).append('|').append(filePath).append('|').append(data.size).append('|')
                .append(data.costTime).append('|').append(data.startTime).append('|')
                .append(data.net).append('|').append(data.desc);

        mPref.edit().putString(key, sb.toString()).commit();
    }

    public void deleteDownloadedSize(String url, String filePath) {
        String key = KEY_PREFIX_SMART_DOWNLOADED_ + url;
        mPref.edit().remove(key).commit();
    }

    /**
     * 断点下载文件，失败重试两次
     * @param downloadUrl
     * @param filePath
     * @param flag
     * @param fileLength
     * @param listener
     * @return
     */
    public boolean smartDownloadFile(String downloadUrl, String filePath, String flag,
            long fileLength, OnDownloadListener listener) {
        boolean ret = doDownloadFile(downloadUrl, filePath, flag, fileLength, listener);
//        if (LogUtil.DDBG) {
//            LogUtil.d(TAG, "smartDownloadFile first doDownloadFile ret = " + ret);
//        }
        if (ret) {
            return true;
        }
        // 失败重试
        int retry = 0;
        do {
            // 等候最多1分钟，每隔5s检测网络，有网络结束等待，就开始重试请求，
            int d = 0;
            do {
                d++;
                try {
                    Thread.sleep(5000l);
//                    if (LogUtil.DDBG) {
//                        LogUtil.d(TAG, "sleep......... " + d * 5 + "s");
//                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (listener != null && listener.isOnlyWifi()) {
                    if (Utility.isWifi(mContext)) {
//                        if (LogUtil.DDBG) {
//                            LogUtil.d(TAG, "smartDownloadFile isWifi is true");
//                        }
                        break;
                    }
                } else {
                    if (Utility.isNetworkAvailable(mContext)) {
//                        if (LogUtil.DDBG) {
//                            LogUtil.d(TAG, "smartDownloadFile isNetworkAvailable is true");
//                        }
                        break;
                    }
                }
            } while (d < 12);

            retry++;
//            if (LogUtil.DDBG) {
//                LogUtil.d(TAG, "smartDownloadFile retry = " + retry);
//            }
            // 网络条件不满足不会去请求下载
            if (d < 12) {
                ret = doDownloadFile(downloadUrl, filePath, flag, fileLength, listener);
//                if (LogUtil.DDBG) {
//                    LogUtil.d(TAG, "smartDownloadFile retry ret = " + ret);
//                }
                if (ret) {
                    return true;
                }
            }
        } while (retry < RETRY_MAX_COUNT);

        return false;
    }

    /**
     * 单线程断点下载
     * @param downUrl
     * @param filePath
     * @param flag
     * @param fileLength
     * @param listener
     * @return
     */
    private boolean doDownloadFile(String downUrl, String filePath, String flag, long fileLength,
            OnDownloadListener listener) {
        checkSaveFileExists(downUrl, filePath);
        // 之前已下载的信息
        final DownloadedData downloadData = getDownloadedData(downUrl, filePath, flag);
        if (downloadData.isDownloading()) {
            // 说明之前是意外中止
        }
//        if (LogUtil.DDBG) {
//            LogUtil.d(TAG, "doDownloadFile downloadSize = " + downloadData.size + ", fileLength = "
//                    + fileLength);
//        }
        if (downloadData.size == 0) {
            // 已下载的进度为0时，若文件存在则删除
            new File(filePath).deleteOnExit();
        } else {
            if (downloadData.size >= fileLength) {
                return true;
            }
        }
        
        final long startPos = downloadData.size;
        RandomAccessFile raFile = null;
        InputStream is = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(downUrl).openConnection();
            conn.setConnectTimeout(TIMEOUT_MS);
            conn.setReadTimeout(TIMEOUT_MS);
            conn.setRequestProperty("Range", "bytes=" + startPos + "-");
            conn.setRequestProperty("User-Agent-YN", DeviceUtil.getDeviceInfoForUserAgentYN());

            downloadData.startTime = System.currentTimeMillis();
            downloadData.net = Utility.getNetworkName(mContext);
            is = conn.getInputStream();

            raFile = new RandomAccessFile(new File(filePath), "rwd");
            raFile.seek(startPos);
            byte[] buffer = new byte[DOWNLOAD_BUFFER_SIZE];
            int count = -1;
            int i = 0;
            while ((count = is.read(buffer)) > -1) {
                raFile.write(buffer, 0, count);
                downloadData.size += count;
                i++;
                if (i == 50) {
                    i = 0;
                    // 保存已下载的信息
                    downloadData.setDownloading();
                    saveDownloadedSize(downUrl, filePath, flag, downloadData);
//                    if (LogUtil.DDBG) {
//                        LogUtil.d(TAG, "doDownloadFile saveDownloadedSize i = " + i
//                                + ", downloadSize = " + downloadData.size);
//                    }
                }
                if (listener != null) {
                    listener.onProgress((int) (downloadData.size * 100 / fileLength));
                }
            }
            conn.disconnect();
            boolean isSuccess = downloadData.size >= fileLength;

            // 保存已下载的信息
            downloadData.setDownloadFinished(isSuccess);
            saveDownloadedSize(downUrl, filePath, flag, downloadData);
//            if (LogUtil.DDBG) {
//                LogUtil.d(TAG, "doDownloadFile saveDownloadedSize isSuccess ＝ " + isSuccess
//                        + ", downloadSize = "
//                        + downloadData.size);
//            }
            return isSuccess;
        } catch (MalformedURLException e) {
            e.printStackTrace();
//            if (LogUtil.DDBG) {
//                LogUtil.d(TAG, "doDownloadFile MalformedURLException: " + e.getMessage());
//            }
            // 保存已下载的信息
            downloadData.setException(e.getMessage());
            saveDownloadedSize(downUrl, filePath, flag, downloadData);
//            if (LogUtil.DDBG) {
//                LogUtil.d(TAG, "doDownloadFile saveDownloadedSize downloadSize = "
//                        + downloadData.size);
//            }
        } catch (IOException e) {
            e.printStackTrace();
//            if (LogUtil.DDBG) {
//                LogUtil.d(TAG, "doDownloadFile IOException: " + e.getMessage());
//            }
            // 保存已下载的信息
            downloadData.setException(e.getMessage());
            saveDownloadedSize(downUrl, filePath, flag, downloadData);
//            if (LogUtil.DDBG) {
//                LogUtil.d(TAG, "doDownloadFile saveDownloadedSize downloadSize = "
//                        + downloadData.size);
//            }
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (raFile != null) {
                try {
                    raFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    public boolean checkSaveFileExists(String downloadUrl, String filePath) {
        File saveFile = new File(filePath);
        if (!saveFile.exists()
                && Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            deleteDownloadedSize(downloadUrl, filePath);
            return false;
        }
        return true;
    }

    public static class DownloadedData {
        private static final String STATUS_DOWNLOAD_ING = "1";
        private static final String STATUS_DOWNLOAD_FINISHED_SUCCESS = "2";
        private static final String STATUS_DOWNLOAD_FINISHED_FAIL = "3";
        private static final String STATUS_DOWNLOAD_EXCEPTION = "4";

        /**
         * 总的已下载大小
         */
        long size;
        /**
         * 总的花费时间
         */
        long costTime;
        /**
         * 开始请求的时间
         */
        long startTime;
        /**
         * 请求时网络
         */
        String net;

        /**
         * 状态说明
         */
        String desc;

        public boolean isDownloading() {
            return STATUS_DOWNLOAD_ING.equals(desc);
        }

        public void setDownloading() {
            desc = STATUS_DOWNLOAD_ING;
            costTime += System.currentTimeMillis() - startTime;
        }

        public void setDownloadFinished(boolean isSuccess) {
            if (isSuccess) {
                desc = STATUS_DOWNLOAD_FINISHED_SUCCESS;
            } else {
                desc = STATUS_DOWNLOAD_FINISHED_FAIL;
            }
            costTime += System.currentTimeMillis() - startTime;
        }

        public void setException(String exceptionMsg) {
            desc = STATUS_DOWNLOAD_EXCEPTION + ':' + exceptionMsg;
            costTime += System.currentTimeMillis() - startTime;
        }

        /**
         * 打点日志的value: 开始请求的时间 ＋ @@ ＋ 请求时的网络 ＋ @@ ＋ 下载已花费的时间 ＋ @@ ＋
         * 已下载的大小 ＋ @@ ＋ 状态说明
         * 
         * @return
         */
        public String toValues() {
            StringBuilder sb = new StringBuilder();
            sb.append(startTime).append("@@").append(net)
                    .append("@@").append(costTime)
                    .append("@@").append(size)
                    .append("@@").append(desc);
            return sb.toString();
        }
    }

    public interface OnDownloadListener {
        void onProgress(int progress);
        boolean isOnlyWifi();
    }
}
