package com.gome.haoyuangong.activity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.views.photoview.PhotoView;
import com.gome.haoyuangong.views.photoview.PhotoViewAttacher;

public class PhotoViewActivity extends Activity {
	
	private static final String BUNDLE_TYPE_IMAGE_SRC = "IMAGE_SRC";
	private static final String BUNDLE_TYPE_IMAGE_TYPE = "IMAGE_TYPE";
	
	public static final int LOCAL_URI = 1;
	public static final int NET_URI = 2;
	public static final int DRAWABLE_ID = 3;

	private PhotoViewAttacher mAttacher;

	private String imgSrc;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_photoview);
		
		PhotoView mImageView = (PhotoView) findViewById(R.id.iv_photo);
		mAttacher = new PhotoViewAttacher(mImageView);

		Drawable bitmap = getResources().getDrawable(R.drawable.feature_guide_3);
		mImageView.setImageDrawable(bitmap);
		
//		String imageSrc = getIntent().getStringExtra("BUNDLE_TYPE_IMAGE_SRC");
//		int imageType = getIntent().getIntExtra(BUNDLE_TYPE_IMAGE_TYPE, -1);
//		if(imageType == -1){
//			Toast.makeText(this, "无效图片源", Toast.LENGTH_SHORT).show();
//			finish();
//		}
		
//		switch(imageType){
//			case DRAWABLE_ID:
//				
//				Drawable bitmap = getResources().getDrawable(R.drawable.ic_launcher);
//				mImageView.setImageDrawable(bitmap);
//				break;
//			case NET_URI:
//				break;
//			case LOCAL_URI:
//				break;
//		}
	}
	
	

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Need to call clean-up
		if(mAttacher != null){
			mAttacher.cleanup();
		}
	}

}
