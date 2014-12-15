package com.gome.haoyuangong.utils.next;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtils {
    private static final String TAG = "FileUtils";
    
    private static final String TMP_SUFFIX = ".tmp";

    
    public static boolean downloadToExternal(Context context, String dataUrl, String outPath) {
        return downloadToExternal(context, dataUrl, getFileNameForUrl(dataUrl), outPath);
    }
    
    public static boolean downloadToExternal(Context context, String dataUrl,
            String fileName, String outPath) {
        File tmpFile = new File(outPath, fileName + TMP_SUFFIX);
        tmpFile.deleteOnExit();
        if (doDownload(context, dataUrl, tmpFile)) {
            boolean ret = tmpFile.renameTo(new File(outPath, fileName));
            if (LogUtil.DDBG) {
                LogUtil.d(TAG, "downloadData renameTo ret = " + ret);
            }
            return true;
        }
        return false;
    }


    /**
     * 下载zip文件直接解压
     * @param context
     * @param dataUrl
     * @return
     */
    public static boolean downloadAndUnzip(Context context, String dataUrl, String outPath) {
        InputStream is = null;

        try {
        	URL url = new URL(dataUrl);
//            URL url = new URL(urlAddParams(context, dataUrl));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.addRequestProperty("User-Agent-YN",
                    DeviceUtil.getDeviceInfoForUserAgentYN()); 
            is = conn.getInputStream();

            String fileName = getFileNameForUrl(dataUrl);
            String zipFileName;
            int index = fileName.indexOf(".");
            if (index != -1) {
                zipFileName = fileName.substring(0, index);
            } else {
                zipFileName = fileName;
            }
            return unZip(is, outPath, zipFileName);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static boolean doDownload(Context context, String imageUrl, File outFile) {
        URL url = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
        	url = new URL(imageUrl);
//            url = new URL(urlAddParams(context, imageUrl));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.addRequestProperty("User-Agent-YN",
                    DeviceUtil.getDeviceInfoForUserAgentYN()); 
            is = conn.getInputStream();
            fos = new FileOutputStream(outFile);
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = is.read(buf)) > 0) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
    
    public static boolean unZip(File file, String outPath, String zipFileName) {
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
            return unZip(is, outPath, zipFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 解压zip压缩文件到指定目录
     * 
     * @param is
     * @param outPath
     * @param zipFileName
     * @throws Exception
     */
    public static boolean unZip(InputStream is, String outPath, String zipFileName) {
        ZipInputStream inZip = null;
        try {
            inZip = new ZipInputStream(is);
            ZipEntry zipEntry;
            String szName;
            while ((zipEntry = inZip.getNextEntry()) != null) {
                szName = zipEntry.getName();
                if (zipFileName != null && szName.startsWith(zipFileName + "/")) {
                    szName = szName.substring(zipFileName.length() + 1);
                }
                if ("".equals(szName)) {
                    continue;
                }

                if (zipEntry.isDirectory()) {
                    if (szName.endsWith("/")) {
                        szName = szName.substring(0, szName.length() - 1);
                    }
                    File folder = new File(outPath + File.separator + szName);
                    folder.mkdirs();
                } else {

                    File file = new File(outPath + File.separator + szName);
                    file.createNewFile();

                    FileOutputStream out = null;
                    try {
                        out = new java.io.FileOutputStream(file);
                        int len;
                        byte[] buffer = new byte[1024];
                        while ((len = inZip.read(buffer)) != -1) {
                            out.write(buffer, 0, len);
                        }
                        out.flush();
                    } finally {
                        if (out != null) {
                            try {
                                out.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            if (inZip != null) {
                try {
                    inZip.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
    
    /**
     * 解压zip文件到内部文件系统
     * @param context
     * @param file
     * @param fileName
     * @return
     */
    public static boolean unzipToInternalFile(Context context, File file, String fileName, String md5) {
        String fileMd5 = MD5.encrypt(file);
        // md5校验
        if (md5 != null && (fileMd5 == null || !fileMd5.equals(md5))) {
            file.delete();
            return false;
        }
        String zipFileName;
        int index = fileName.indexOf(".");
        if (index != -1) {
            zipFileName = fileName.substring(0, index);
        } else {
            zipFileName = fileName;
        }
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
            return unZip(is, context.getFilesDir().getAbsolutePath(), zipFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    

    /**
     * 资源url加参数
     * @param context
     * @param url
     * @return
     */
//    public static String urlAddParams(Context context, String url) {
//        int index = url.lastIndexOf("/");
//        if (index > 0) {
//            String selfPhone = YouNiInfo.getSelfMobileNumber();
//            if (!TextUtils.isEmpty(selfPhone)) {
//                selfPhone = HttpUtils.encrypt(selfPhone);
//            }
//            TelephonyManager tm = (TelephonyManager) context
//                    .getSystemService(Context.TELEPHONY_SERVICE);
//            String imei = tm == null ? "" : tm.getDeviceId();
//            if (!TextUtils.isEmpty(imei)) {
//                imei = HttpUtils.encrypt(imei);
//            }
//            String params = "p=" + selfPhone + "&i=" + imei;
//            index = url.indexOf("?", index + 1);
//            url = url + (index > 0 ? '&' : '?') + params;
//        }
//        return url;
//    }
    
    
    public static String getFileNameForUrl(String url) {
        int index = url.lastIndexOf("/");
        if (index != -1) {
            int end = url.lastIndexOf("?", index);
            if (end != -1) {
                return url.substring(index + 1, end);
            }
            return url.substring(index + 1);
        }
        return url;
    }
}
