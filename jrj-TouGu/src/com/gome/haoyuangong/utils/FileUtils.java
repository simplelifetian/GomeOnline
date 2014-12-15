package com.gome.haoyuangong.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.gome.haoyuangong.SetupInfo;

import android.os.Environment;

public class FileUtils {
	public static boolean isSdCardMounted() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	public static byte[] getBytesFromFile(String filePath) {
		return getBytesFromFile(new File(filePath));
	}

	/**
	 * 文件转化为字节数组
	 */
	public static byte[] getBytesFromFile(File f) {
		if (f == null) {
			return null;
		}
		try {
			FileInputStream stream = new FileInputStream(f);
			ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = stream.read(b)) != -1) {
				out.write(b, 0, n);
			}
			stream.close();
			out.close();
			return out.toByteArray();
		} catch (IOException e) {
		}
		return null;
	}

	public static String getSdcardPath() {
		String path = null;
		if (isSdCardMounted()) {
			path = Environment.getExternalStorageDirectory().toString();
		}
		return path;
	}

	public static String getVoiceSdcardPath(String url) {
		String path = getSdcardPath();
		if (path != null) {
			if (!StringUtils.isEmpty(url)) {
				url = url.substring(url.lastIndexOf("/"), url.length());
				path = path + SetupInfo.SDCARD_VOICE_FOLDER + url;
			}
		}
		return path;
	}
}
