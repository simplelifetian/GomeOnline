
package com.gome.haoyuangong.update;

import java.io.File;


import android.content.Context;

public interface IAsyncUpdate {

    public final static int SUCCESSED = 0;
    public final static int NETWORK_ERROR = 1;
    public final static int FILE_ERROR = 2;
    public final static int STILL_RUNNING = 3;

    public final static int FORCE_UPDATE = 2;
    public final static int NORMAL_UPDATE = 1;
    public final static int NO_UPDATE = 0;
    public final static int POPUP_UPDATE = 4;
    
    public final static int NORMAL_DOWNLOAD = 0;
    public final static int SILENCE_DOWNLOAD = 1;
    public final static int SILENCE_DOWNLOAD_WIFI = 2;
    
    public void checkUpdate(Context context);
    
    public void downloadApk(UpdateInfo info);

    public void setOnUpdateListener(OnUpdateListener l);
    
    public void downloadPicTips(final UpdateInfo info);

    public interface OnUpdateListener {
        void onCheck(int rst, UpdateInfo info);

        void onDownloadFinish(int rst, File file, UpdateInfo info);

        void onDownloadProgress(int progress);
    }
}
