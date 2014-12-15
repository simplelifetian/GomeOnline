package com.gome.haoyuangong.net.volley;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.Volley;
import com.gome.haoyuangong.MyApplication;
import com.gome.haoyuangong.R;
import com.gome.haoyuangong.SetupInfo;
import com.gome.haoyuangong.log.Logger;
import com.gome.haoyuangong.utils.FileCache;

public class ImageLoader {
	public static boolean isOk = true;
	private com.android.volley.toolbox.ImageLoader mImageLoader;
	private Context ctx;
	private Map<ImageView, ImageListener> mImageMaps = new HashMap<ImageView, ImageListener>();
	private FileCache fileCache;
	public ImageLoader(Context context) {
		RequestQueue mQueue = Volley.newRequestQueue(context);
		this.ctx = context;
		mImageLoader = new com.android.volley.toolbox.ImageLoader(mQueue, new BitmapCache());
		fileCache = new FileCache(ctx, SetupInfo.SDCARD_CACHE_IMAGE_FOLDER);
	}

	public void downLoadImage(String url, ImageView imgView) {
		// TODO
		downLoadImage(url, imgView, R.drawable.icon_head_default, R.drawable.icon_head_default);
	}

	public void downLoadImage(String url, final ImageView imgView, final int defaultImageResId, final int errorImageResId) {
//		if (!isOk) {
//			if (imgView != null) {
//				imgView.setImageResource(R.drawable.icon_head_default);
//			}
//			return;
//		}
		if (url == null) {
			imgView.setImageResource(R.drawable.icon_head_default);
			return;
		}
		// ImageListener listener =
		// com.android.volley.toolbox.ImageLoader.getImageListener(imgView,
		// defaultImageResId, errorImageResId);

		ImageListener listener = new MyImageListener(imgView,defaultImageResId,errorImageResId) ;
		ImageListener lastL = mImageMaps.get(imgView);
		if(lastL!=null){
			((MyImageListener)lastL).isCancel = true;
			mImageMaps.remove(lastL);
		}
		mImageMaps.put(imgView, listener);
		mImageLoader.get(url, listener);
	}
	
	
	
	private class MyImageListener implements ImageListener{
		public boolean isCancel=false;
		private ImageView imgView;
		private int defaultImageResId;
		private int errorImageResId;
		public MyImageListener(ImageView imgView, int defaultImageResId, int errorImageResId){
			this.imgView = imgView;
			this.defaultImageResId = defaultImageResId;
			this.errorImageResId = errorImageResId;
		}
		@Override
		public void onErrorResponse(VolleyError error) {
			if(isCancel)return;
			if (errorImageResId != 0) {
				imgView.setImageResource(errorImageResId);
			}
			mImageMaps.remove(this);
		}

		@Override
		public void onResponse(ImageContainer response, boolean isImmediate) {
			if(isCancel)return;
			if (response.getBitmap() != null) {
				imgView.setImageBitmap(response.getBitmap());
			} else if (defaultImageResId != 0) {
				imgView.setImageResource(defaultImageResId);
			}
			mImageMaps.remove(this);
		}
	}

	public class BitmapCache implements ImageCache {
		private LruCache<String, Bitmap> mCache;

		public BitmapCache() {
			if (ctx instanceof Activity) {
				mCache = ((MyApplication) ((Activity) ctx).getApplication()).getImageCache();
			}
			if (mCache == null) {
				int maxSize = 10 * 1024 * 1024;
				mCache = new LruCache<String, Bitmap>(maxSize) {
					@Override
					protected int sizeOf(String key, Bitmap value) {
						return value.getRowBytes() * value.getHeight();
					}

				};
			}
		}

		@Override
		public Bitmap getBitmap(String url) {
			Bitmap b = mCache.get(url);
			Logger.info("getBitmap", b+"");
			if(b==null&&fileCache!=null){
				Logger.info("getBitmapFromFileCache", url);
				b = fileCache.getBitmapFromFileCache(url);
				if(b!=null){
					mCache.put(url, b);
					mCache.get(url);
				}
			}
			
			return b;
		}

		@Override
		public void putBitmap(String url, Bitmap bitmap) {
			if(mCache.get(url)==null){
				if(fileCache!=null){
					fileCache.addBitmapToFileCache(url, bitmap);
				}
				mCache.put(url, bitmap);
			}
		}

	}
}
