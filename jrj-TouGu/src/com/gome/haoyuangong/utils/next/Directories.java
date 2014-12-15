
package com.gome.haoyuangong.utils.next;


import android.content.Context;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.gome.haoyuangong.MyApplication;

/**
 * For the path of common files. Should be init() at the beginning of the
 * application.<br>
 * 
 * @author <a href="mailto:zhengzhaomail@gmail.com">Zheng Zhao</a>
 */
public class Directories {

    private static String sApkPath;
    
    private static String sSelfPhotoPath;
    
    private static String sPhotoFileName;

    private static String sTempBackupPath;
    
    private static String sTempBackupTimeStampPath;
    
    private static final String PHOTO_DIR = Environment.getExternalStorageDirectory()
    + "/DCIM/Camera";
    
    /**
     * This method must be performed at the beginning of the application.
     * 
     * @param context the context of the application or activity
     */
    private Directories() {
    };
    
    public static void init(Context context) {
        sApkPath = getApkPath(context);
        sSelfPhotoPath = getSelfPhotoPath(context);
        sTempBackupPath = getTempBackupPath(context);
        makeProfilePhotoPath();
        File dir = new File(sTempBackupPath);
    	if (dir.isDirectory()) {
    		File[] timeStamps = dir.listFiles();
    		if (timeStamps != null) {
	    		for (int i = 0; i < timeStamps.length; i++) {
	    			sTempBackupTimeStampPath = timeStamps[i].getAbsolutePath() + "/";
	    		}
    		}
    	}
    }
    
    public static String getTempBackupPath(long thread) {
    	if (sTempBackupTimeStampPath == null) {
    		createNewTimeStamp();
    	}
    	return sTempBackupTimeStampPath + thread + ".tbak";
    }

    public static String getApkPath(String versionName) {
        return sApkPath + versionName + ".apk";
    }
	
//	private static String getTempBackupDir() {
//		return sTempBackupPath;
//	}

    public static String getTempBackupTimeStampPath() {
    	return sTempBackupTimeStampPath;
    }
    
    public static String getApkDir() {
        return sApkPath;
    }
    
    public static String getPhotoDir() {
        return PHOTO_DIR;
    }
    
    public static String getPhotoPath() {
        new File(PHOTO_DIR).mkdirs();
        return PHOTO_DIR + "/" + sPhotoFileName;
    }
    
    public static String createPhotoPath() {
        sPhotoFileName = getPhotoFileName();
        new File(PHOTO_DIR).mkdirs();
        return PHOTO_DIR + "/" + sPhotoFileName;
    }
    
    public static String getSelfPhotoPath() {
        if (sSelfPhotoPath == null || sSelfPhotoPath.length() == 0 || sSelfPhotoPath.indexOf("null") != -1) {
            sSelfPhotoPath = getSelfPhotoPath(MyApplication.get());
        }
        return sSelfPhotoPath;
    }

    private static String getApkPath(Context context) {
        new File(Environment.getExternalStorageDirectory() + "/jrj/tougu/download").mkdirs();
        return Environment.getExternalStorageDirectory() + "/jrj/tougu/download/";
    }
    
    private static String getTempBackupPath(Context context) {
		// TODO Auto-generated method stub
        new File(Environment.getExternalStorageDirectory() + "/jrj/tougu/download").mkdirs();
        return Environment.getExternalStorageDirectory() + "/jrj/tougu/download/";
	}
    
    private static void makeProfilePhotoPath() {
        new File(Environment.getExternalStorageDirectory() + "/jrj/tougu/profile_photo/").mkdirs();
    }
    
    public static String getProfilePhotoFile(Long contactId) {
        return Environment.getExternalStorageDirectory() + "/jrj/tougu/profile_photo/"+contactId+".png";
    }
    
    public static void createNewTimeStamp() {
    	clearTimeStamp();
        sTempBackupTimeStampPath = sTempBackupPath + "/" + System.currentTimeMillis() + "/";
    	new File(sTempBackupTimeStampPath).mkdirs();
    }
    
    public static void clearTimeStamp() {
    	File dir = new File(sTempBackupPath);
    	if (dir.isDirectory()) {
    		File[] files = dir.listFiles();
    		if (files != null) {
	    		for (int i = 0; i < files.length; i++) {
	    			deleteDir(files[i]);
	    		}
    		}
    	}
    }
    
    private static void deleteDir(File dir) {
    	if (dir.isDirectory()) {
    		File[] files = dir.listFiles();
    		if (files != null) {
	    		for (int i = 0; i < files.length; i++) {
	    			deleteDir(files[i]);
	    		}
    		}
    	}
		dir.delete();
    }
    
    public static void writeProfile(JSONObject profile) {
		File proFile = new File(Directories.getTempBackupTimeStampPath() + "profile");
		FileOutputStream fos = null;
		try {
			proFile.createNewFile();
			fos = new FileOutputStream(proFile);
			byte[] json = profile.toString().getBytes("utf-8");
			fos.write(json);
			fos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    }
    
    public static JSONObject readProfile() {
    	JSONObject json = null;
		File proFile = new File(Directories.getTempBackupTimeStampPath() + "profile");
		if (proFile.exists()) {
			BufferedInputStream bis = null;
			ByteArrayOutputStream byteos = null;
			try {
				bis = new BufferedInputStream(new FileInputStream(proFile));
				byteos = new ByteArrayOutputStream();
				
				byte[] buf = new byte[1024];
				int len = 0;
				while ((len = bis.read(buf)) > 0) {
				    byteos.write(buf, 0, len);
				}
				byte[] content=byteos.toByteArray();
                String s = new String(content, "utf-8");
                json = new JSONObject(s);
                buf = null;
                content = null;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					if (bis != null) {
						bis.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					if (byteos != null) {
						byteos.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return json;
    }

    private static String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }
    
    private static String getSelfPhotoPath(Context context) {
        return context.getFilesDir() + "/self_portrait.jpg";
    }
}
