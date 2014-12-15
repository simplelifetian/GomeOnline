
package com.gome.haoyuangong.update;

import java.io.Serializable;

/**
 * The information of the APK on the update server.<br>
 * 
 * @author <a href="mailto:zhengzhaomail@gmail.com">Zheng Zhao</a>
 */
public class UpdateInfo implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -8927188911079318146L;
    public boolean hasUpdate;
    public boolean force;
    
    public int updateType;
    public int downloadType;
    public int interval = -1;
    public int remind;
    public String updateUrl;
    public String versionName;
    public int versionCode;
    public String description;
    public String marketsIds;
    public String patchUrl;
    public String md5;
    public long sizeOriginal;
    public long sizePatch;
    /**
     * 升级版本的图文描述 zip文件
     */
    public String picTips;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("@UpdateInfo : [")
                .append("hasUpdate = ").append(hasUpdate)
                .append(", updateType = ").append(updateType)
                .append(", downloadType = ").append(downloadType)
                .append(", versionName = ").append(versionName)
                .append(", versionCode = ").append(versionCode)
                .append(", updateUrl = ").append(updateUrl)
                .append(", patchUrl = ").append(patchUrl)
                .append(", md5 = ").append(md5)
                .append(", sizeOriginal = ").append(sizeOriginal)
                .append(", sizePatch = ").append(sizePatch);
        sb.append("]");
        return sb.toString();
    }

}
