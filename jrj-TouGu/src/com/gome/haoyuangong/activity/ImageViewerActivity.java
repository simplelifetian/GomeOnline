/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.gome.haoyuangong.activity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.gome.haoyuangong.R;
import com.gome.haoyuangong.log.Logger;
import com.gome.haoyuangong.net.result.live.NewLiveCountResult;
import com.gome.haoyuangong.net.volley.ImageLoader;
import com.gome.haoyuangong.utils.JSONUtils;
import com.gome.haoyuangong.utils.StringUtils;
import com.gome.haoyuangong.views.photoview.PhotoView;
import com.gome.haoyuangong.views.photoview.PhotoViewAttacher;

public class ImageViewerActivity extends Activity {

    public static final String BUNDLE_PARAM_FILEPATH = "BUNDLE_PARAM_FILEPATH";

    private PhotoViewAttacher mAttacher;
    
    private PhotoView mImageView;
    
    private ImageLoader imageLoader;
    
    private RequestQueue mQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_photoview);
        
        String filePath = getIntent().getStringExtra(BUNDLE_PARAM_FILEPATH);
        
//        filePath="/storage/emulated/0/image_photo.jpg";
        
        if(filePath == null){
//        	Toast.makeText(this, "无效图片路径", Toast.LENGTH_SHORT).show();
        	finish();
        	return;
        }

        mImageView = (PhotoView) findViewById(R.id.iv_photo);
//        Drawable bitmap = getResources().getDrawable(R.drawable.ic_launcher);
//        mImageView.setImageDrawable(bitmap);

        mImageView.setScaleType(ScaleType.CENTER_INSIDE);
        // The MAGIC happens here!
        mAttacher = new PhotoViewAttacher(mImageView);
        mQueue = Volley.newRequestQueue(ImageViewerActivity.this);
        if(filePath.startsWith("http")){
        	downImage(filePath);
        }else{
        	Bitmap bitmap = getLocalImage(filePath);
        	if(bitmap != null){
        		mImageView.setImageBitmap(bitmap);
        	}
        }
    }
    
    private Bitmap getLocalImage(String imagePath){
    	try {
            FileInputStream fis = new FileInputStream(imagePath);
            return BitmapFactory.decodeStream(fis);      
         } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
       }
    }
    
    private void loadNetImage(String imageUrl,ImageView iamgeView){
    	imageLoader.downLoadImage(imageUrl, iamgeView,R.drawable.point_default_icon,R.drawable.point_default_icon);
    }
    
	private void downImage(String imagePath) {

		com.android.volley.toolbox.ImageRequest iamgeRequest = new com.android.volley.toolbox.ImageRequest(imagePath,  
                new Response.Listener<Bitmap>() {

					@Override
					public void onResponse(Bitmap response) {
						// TODO Auto-generated method stub
						if(response != null){
							mImageView.setImageBitmap(response);
						}
					}  
                   
                },0,0, null,new Response.ErrorListener() {  
                    @Override  
                    public void onErrorResponse(VolleyError error) {  
                    	Logger.error("TAG", error.getMessage(), error);  
                    }  
                });


		mQueue.add(iamgeRequest);

	}


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mAttacher != null){
        	mAttacher.cleanup();
        }
        
    }

}
