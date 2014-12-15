package com.gome.haoyuangong.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class PhotoUtils {
	public static Bitmap getGivenSizeBitmap(String path,int width){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(path, options);
		if(bmp==null)return null;
		int h = options.outHeight * width / options.outWidth;
		options.outWidth = width;
		options.outHeight = h;
		
		options.inJustDecodeBounds = false;
		bmp = BitmapFactory.decodeFile(path, options);
		return bmp;
	}
	public static Bitmap getGivenSizeBitmap(Bitmap bmp,int width){
		if(bmp==null)return null;
		int w = bmp.getWidth();
		int h = bmp.getHeight();
		int th = h * width / w;
//		Bitmap bmp = BitmapFactory.decodeFile(path, options);
		return bmp;
	}
}
