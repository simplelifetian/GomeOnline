/**
 * 
 */
package com.gome.haoyuangong.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.bean.Stock;
import com.gome.haoyuangong.dialog.BottomDialog;
import com.gome.haoyuangong.dialog.CommenBottomDialog;
import com.gome.haoyuangong.dialog.BottomDialog.OnConfirmListener;
import com.gome.haoyuangong.log.Logger;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.tougu.PostOpinionResultBean;
import com.gome.haoyuangong.net.url.NetUrlLoginAndRegist;
import com.gome.haoyuangong.net.url.NetUrlTougu;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.presenter.IFileUploadPresenter;
import com.gome.haoyuangong.utils.FileUtils;
import com.gome.haoyuangong.utils.ImageUtils;
import com.gome.haoyuangong.utils.StringUtils;
import com.gome.haoyuangong.views.KeyboardLayout;
import com.gome.haoyuangong.views.KeyboardLayout.onKybdsChangeListener;
import com.google.gson.Gson;

/**
 * 
 */
@SuppressLint("NewApi")
public class WriteOpinionActivity extends BaseActivity{
	
	private static final String TAG = WriteOpinionActivity.class.getName();
	public static final int STOCK_REQUEST_CODE = 1001;
	public static final int IMAGE_REQUEST_CODE = 1002;
	public static final int TAKE_PICTURE = 1003;
	public static final int RESULT_LOAD_IMAGE = 1004;
	public static final int REQUEST_CATEGORY_CODE = 1006;
	
	private static final int MAX_PIC_SIZE = 1024 * 1024;
	
	public static final int WRITE_SUCCESS = 1005;
	
	private static final String[] agu = {"1","A股"};
	private static final String[] ganggu = {"2","港股"};
	private static final String[] meigu = {"3","美股"};
	private static final String[] jijin = {"4","基金"};
	private static final String[] guijinshu = {"5","贵金属"};
	

	private LinearLayout categorySelector;
	private LinearLayout categoryList;

	private TextView currSpinnerItem;
	private String[] currentItem = agu;

	private EditText opTitle;
	private EditText opContent;

	private LinearLayout spinnerViewer;
//	private TextView currSpinnerPointer;

	private ViewFlipper mViewFirstFlipper;
	private PopupWindow mPopButtonWin;
	private GridView mPopWinGridView;
	private View popMasker;
	
//	private KeyboardLayout mainLayout;
	private LinearLayout writeOpBottom;
	
	private ImageView imageSrc;
	private ImageView stockSrc;
	
	private CheckBox isPrivate;
	
	private ScrollView scrollView;
	
	private LinearLayout loSelectedImage;
	private ImageView[] selectedImages = new ImageView[3];
	private String[] selectImagePath = new String[3];
	
	private final ImageView[] selectDel = new ImageView[3];
	private final RelativeLayout[] selectedLayout = new RelativeLayout[3];
	
	private String prefix = "def_";
	
//	private LinearLayout guideLayout;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_opinion_layout);
		setTitle("写观点");
		titleRight2.setText("提交");

		writeOpBottom = (LinearLayout)findViewById(R.id.write_opinion_bottom);
//		mainLayout = (KeyboardLayout)findViewById(R.id.main_lo);
//		mainLayout.setOnkbdStateListener(new onKybdsChangeListener() {
//            
//            public void onKeyBoardStateChange(int state) {
//                    switch (state) {
//                    case KeyboardLayout.KEYBOARD_STATE_HIDE:
////                    		changeMaskViewLayout();
//                    		uiHandler.sendEmptyMessage(1);
////                    		Toast.makeText(WriteOpinionActivity.this, "键盘隐藏", Toast.LENGTH_SHORT).show();
//                    		
//                            break;
//                    case KeyboardLayout.KEYBOARD_STATE_SHOW:
////                    		changeMaskViewLayout();
//                    		uiHandler.sendEmptyMessage(2);
////                    		Toast.makeText(WriteOpinionActivity.this, "键盘显示", Toast.LENGTH_SHORT).show();
//                    		
//                            break;
//                    }
//            }
//		});
		
		spinnerViewer = (LinearLayout) findViewById(R.id.select_category_lo);
		spinnerViewer.setOnClickListener(this);
		currSpinnerItem = (TextView) findViewById(R.id.category_name);
		currSpinnerItem.setText(currentItem[1]);
		popMasker = findViewById(R.id.mask_view);
		opTitle = (EditText) findViewById(R.id.op_title);
		opContent = (EditText) findViewById(R.id.op_content);
		if(!StringUtils.isEmpty(UserInfo.getInstance().getUserId())){
			prefix = UserInfo.getInstance().getUserId()+"_";
		}
		SharedPreferences shared = getContext().getSharedPreferences(prefix+"write_opinion_draft", MODE_PRIVATE);
		if(shared != null){
			String opTitleStr = shared.getString("write_opinion_draft_title",null);
			String opContentStr = shared.getString("write_opinion_draft_content",null);
			String typeIndex = shared.getString("write_opinion_draft_type_index",null);
			String typeName = shared.getString("write_opinion_draft_type_name",null);
			if(!StringUtils.isEmpty(opTitleStr)){
				opTitle.setText(opTitleStr);
			}
			if(!StringUtils.isEmpty(opContentStr)){
				opContent.setText(opContentStr);
			}
			
			if(!StringUtils.isEmpty(typeIndex) && !StringUtils.isEmpty(typeName)){
				currSpinnerItem.setText(typeName);
				currentItem = new String[]{typeIndex,typeName};
			}
			
		}
		
		imageSrc = (ImageView)findViewById(R.id.image_src);
		imageSrc.setOnClickListener(this);
		stockSrc = (ImageView)findViewById(R.id.stock_src);
		stockSrc.setOnClickListener(this);
		
		isPrivate = (CheckBox)findViewById(R.id.is_private);
		
		scrollView = (ScrollView)findViewById(R.id.scrollView1);
		loSelectedImage = (LinearLayout)findViewById(R.id.lo_selected_image);
		ViewGroup.LayoutParams vg = loSelectedImage.getLayoutParams();
		vg.height = getScreenW() / 3;
		loSelectedImage.setLayoutParams(vg);
		
		selectedImages[0] = (ImageView)findViewById(R.id.selected_1);
		selectedImages[1] = (ImageView)findViewById(R.id.selected_2);
		selectedImages[2] = (ImageView)findViewById(R.id.selected_3);
		
		selectDel[0] = (ImageView)findViewById(R.id.image_del_1);
		selectDel[1] = (ImageView)findViewById(R.id.image_del_2);
		selectDel[2] = (ImageView)findViewById(R.id.image_del_3);
		for(int i = 0 ; i < selectedImages.length ; i++){
			selectDel[i].setTag(i);
			selectDel[i].setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int index = (Integer)v.getTag();
					delSelectImage(index);
				}
			});
		}
		
		selectedLayout[0] = (RelativeLayout)findViewById(R.id.lo_select_1);
		selectedLayout[1] = (RelativeLayout)findViewById(R.id.lo_select_2);
		selectedLayout[2] = (RelativeLayout)findViewById(R.id.lo_select_3);
		for(int i = 0 ; i < selectedImages.length ; i++){
			selectedLayout[i].setTag(i);
			selectedLayout[i].setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int index = (Integer)v.getTag();
					String filePath = selectImagePath[index];
					Intent intent = new Intent(WriteOpinionActivity.this,ImageViewerActivity.class);
					intent.putExtra(ImageViewerActivity.BUNDLE_PARAM_FILEPATH, filePath);
					startActivity(intent);
				}
			});
		}
		overridePendingTransition(R.anim.dialog_enter, R.anim.activity_default);
	}
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.activity_default, R.anim.dialog_exit);
	}
	
	public void delSelectImage(int index){
		selectedImages[index].setImageBitmap(null);
		selectDel[index].setVisibility(View.GONE);
		selectImagePath[index] = null;
		
	}

	@Override
	public void onClick(View v) {
//		super.onClick(v);
		switch (v.getId()) {
		case R.id.title_left1:
			if(checkNeedSave()){
				
			}else{
				finish();
			}
			break;
		case R.id.select_category_lo:
//			writeOpBottom.setVisibility(View.GONE);
//			hideSoftInput();
//			uiHandler.sendEmptyMessage(3);
//			showPopWindow();
			Intent intent = new Intent(WriteOpinionActivity.this,OptionCategoryListActivity.class);
			if(currentItem != null){
				intent.putExtra(OptionCategoryListActivity.BUNDLE_PARAM_CURRINDEX, currentItem[0]);
			}
			startActivityForResult(intent, REQUEST_CATEGORY_CODE);
			break;
		case R.id.image_src:
			headClicked();
			break;
		case R.id.stock_src:
			stockSrc.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					
				}
			});
			break;
			
		case R.id.title_right2:
			//判断用户是否登录
			if(currentItem == null){
				Toast.makeText(WriteOpinionActivity.this, "请选择观点分类", Toast.LENGTH_SHORT).show();
				return;
			}
			String titleStr = opTitle.getText().toString();
			if(StringUtils.isEmpty(titleStr) || titleStr.length() < 3){
				Toast.makeText(WriteOpinionActivity.this, "标题字数要在3~50字之间", Toast.LENGTH_SHORT).show();
				return;
			}
			String contentStr = opContent.getText().toString();
			if(StringUtils.isEmpty(contentStr)||contentStr.length() < 50){
				Toast.makeText(WriteOpinionActivity.this, "观点字数要在50~500字之间", Toast.LENGTH_SHORT).show();
				return;
			}
			
			String needUpLoad = null;
			
			for(String ss : selectImagePath){
				if(ss != null){
					needUpLoad = ss;
					break;
				}
			}
			
			if(StringUtils.isEmpty(needUpLoad)){
				postOpinoin(null);
			}else{
				
				File f = new File(needUpLoad);
				if(!f.exists())return;
				long fileSize = f.length();
				Logger.error(TAG, "old pic size : "+fileSize);
				if(fileSize > MAX_PIC_SIZE){
					Bitmap oldPic = ImageUtils.compressImageFromFile(needUpLoad);
					File cacheFile = getContext().getExternalCacheDir();
					File tempImageFile = new File(cacheFile,"opinion_image.jpg");
					FileOutputStream fos = null;
					boolean compSuccess = false;
					try {
						fos = new FileOutputStream(tempImageFile);
						oldPic.compress(CompressFormat.JPEG, 100, fos);
						compSuccess = true;
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						Logger.error(TAG, "图片压缩失败",e);
					}finally{
						try {
							fos.close();
							oldPic.recycle();
						} catch (IOException e) {
							// TODO Auto-generated catch block
						}
					}
					if(compSuccess){
						needUpLoad = tempImageFile.getAbsolutePath();
						f = new File(needUpLoad);
						fileSize = f.length();
					}
				}
				
				Logger.error(TAG, " new pic : "+needUpLoad);
				Logger.error(TAG, " new pic size : "+fileSize);
				Map<String, String> params = new HashMap<String, String>();
				params.put("channel", "test");
				params.put("backJson","1");
				params.put("sizeMax", fileSize+"");
//				params.put("isNeedGenScales", "isNeedGenScales");
//				params.put("imageFormats", "jpg,220,220,_220_220");
				
				fileUploade.uploadFile(NetUrlLoginAndRegist.UPLOAD_TOUGU_HEAD_URL, needUpLoad, params,"发布中...");
			}
			
			break;
		}
	}
	
	private IFileUploadPresenter fileUploade = new IFileUploadPresenter(this){
		
		public void onSuccessed(String jsonData){
			Logger.error(TAG, jsonData);
			Gson gson = new Gson();
			ImageUpLoadResult r = gson.fromJson(jsonData, ImageUpLoadResult.class);
			if(r != null && r.getFlag() == 1){
				postOpinoin(r.getFilename());
			}else{
				Toast.makeText(WriteOpinionActivity.this, "发表观点失败，请稍候重试", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	class ImageUpLoadResult{
		private int flag;
		private String filename;
		public int getFlag() {
			return flag;
		}
		public void setFlag(int flag) {
			this.flag = flag;
		}
		public String getFilename() {
			return filename;
		}
		public void setFilename(String filename) {
			this.filename = filename;
		}
		
	}
	
	
	private void headClicked() {
		BottomDialog dialog = new BottomDialog(this);
		dialog.setText1(R.string.phoneSelect1);
		dialog.setText2(R.string.phoneSelect2);
		dialog.setOnConfirmListener(new OnConfirmListener() {
			@Override
			public void onConfirm(int index) {
				if (index == 0) {
					getImageFromCamera();
				} else if (index == 1) {
					getImageFromAlbum();
				}
			}
		});
		dialog.show();
	}
	
	public void getImageFromAlbum() {
//		Intent intent = new Intent(WriteOpinionActivity.this, AlbumActivity.class);
//		startActivityForResult(intent, IMAGE_REQUEST_CODE);
		Intent intent = new Intent();  
		intent.addCategory(Intent.CATEGORY_OPENABLE);  
		intent.setType("image/*");  
		//根据版本号不同使用不同的Action  
		if (Build.VERSION.SDK_INT <19) {  
		    intent.setAction(Intent.ACTION_GET_CONTENT);  
		}else {  
		    intent.setAction(Intent.ACTION_OPEN_DOCUMENT);  
		}  
		startActivityForResult(intent, RESULT_LOAD_IMAGE);

	}

	public void getImageFromCamera() {
		if (FileUtils.isSdCardMounted()) {
		    Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		    File file = new File(Environment.getExternalStorageDirectory()+"/jrj/tougu/.images");
	    	if(!file.exists()){
	    		file.mkdirs();
	    	}
		    Uri imageUri = Uri.fromFile(new File(file,"image_photo.jpg"));  
		    openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);  
		    startActivityForResult(openCameraIntent, TAKE_PICTURE);  
		} else {
			showToast(R.string.warn_no_sdcard);
		}
	}
	
//	public Handler uiHandler = new Handler(){
//		
//		@Override
//        public void handleMessage(Message msg) {
//			switch(msg.what){
//			case 1:
//				writeOpBottom.setVisibility(View.GONE);
//				break;
//			case 2:
//				writeOpBottom.setVisibility(View.VISIBLE);
//				break;
//			case 3:
//				showPopWindow();
//				break;
//			}
//		}
//	};

	private void showPopWindow() {

		if (mPopButtonWin == null) {
			mViewFirstFlipper = new ViewFlipper(this);
			LayoutInflater layoutInflater = LayoutInflater.from(this);
			view = layoutInflater.inflate(R.layout.fragment_popmenu_layout_new, null, false);
			mPopWinGridView = (GridView) view.findViewById(R.id.moreGridView);
			mPopWinGridView.setNumColumns(1);
			mPopWinGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
			mPopWinGridView.setGravity(Gravity.CENTER_VERTICAL);
			mPopWinGridView.setAdapter(new SpinnerAdapter(this));
			mPopWinGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
			mViewFirstFlipper.addView(view);
			mViewFirstFlipper.setFlipInterval(6000);
			// 创建Popup
			mPopButtonWin = new PopupWindow(mViewFirstFlipper, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,
					true);
			mPopButtonWin.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//
			mPopButtonWin.setFocusable(true);//

			mPopButtonWin.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss() {

//					currSpinnerPointer.setEnabled(true);
					dismissA.reset();
					popMasker.startAnimation(dismissA);
					postHandler.postDelayed(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							postHandler.sendEmptyMessage(0);
						}

					}, 400);
				}
			});
			mPopWinGridView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					mPopButtonWin.dismiss();
					// topOnClickAction(arg2);
					currSpinnerItem.setText(spinnerItems.get(arg2)[1]);
					currentItem = spinnerItems.get(arg2);
				}
			});
			mPopButtonWin.update();

			// mPopButtonWin.setAnimationStyle(R.style.popwin_anim_style);
			dismissA = AnimationUtils.loadAnimation(this, R.anim.popwindow_masker_dismiss);
			showA = AnimationUtils.loadAnimation(this, R.anim.popwindow_masker_show);
			showWin = AnimationUtils.loadAnimation(this, R.anim.popwindow_show);

		}
		if (mPopButtonWin.isShowing()) {
			mPopButtonWin.dismiss();
		} else {
			mPopButtonWin.showAsDropDown(spinnerViewer);
			showWin.reset();
			view.startAnimation(showWin);
			if (popMasker.getVisibility() == View.GONE) {
//				popMasker.setVisibility(View.VISIBLE);
				postHandler.sendEmptyMessage(1);
			}
			showA.reset();
			popMasker.startAnimation(showA);
			postHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					postHandler.sendEmptyMessage(1);
				}

			}, 400);

//			currSpinnerPointer.setEnabled(false);
		}
	}

	private Handler postHandler = new Handler() {
		@Override
		public void handleMessage(Message m) {
			if (m.what == 1) {
				changeMaskViewLayout();
				popMasker.setVisibility(View.VISIBLE);
			} else {
				popMasker.setVisibility(View.GONE);
			}
		}
	};
	
	View view;
	Animation showA;
	Animation dismissA;
	Animation showWin;

	private List<String[]> spinnerItems = Arrays.asList(agu, ganggu, meigu, jijin, guijinshu);

	class SpinnerAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		private SpinnerAdapter(Context cxt) {
			mInflater = LayoutInflater.from(cxt);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return spinnerItems.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return spinnerItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.spinner_item, null);
				viewHolder = new ViewHolder();
				viewHolder.itemName = (TextView) convertView.findViewById(R.id.item_name);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.itemName.setText(spinnerItems.get(position)[1]);

			return convertView;
		}

		class ViewHolder {
			TextView itemName;
		}
	}
	
	private void changeMaskViewLayout(){
		ViewGroup.LayoutParams lp = popMasker.getLayoutParams();
		lp.height = scrollView.getHeight() +scrollView.getScrollY() - 1;
		Log.e("ddddd", "y:"+scrollView.getScrollY()+" x:"+scrollView.getScrollX()+"  h:"+scrollView.getHeight());
		popMasker.setLayoutParams(lp);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (STOCK_REQUEST_CODE == requestCode) {
			if (data != null) {
				
				View v = getCurrentFocus();
				if(v instanceof EditText){
					
				}
			}
			return;
		}else if(IMAGE_REQUEST_CODE == requestCode ){
			if (data != null) {
				String[] photos = data.getStringArrayExtra(AlbumActivity.SELECTED_PHOTOS);
				if(photos != null){
					for(int i = 0 ; i < selectedImages.length ; i++){
						selectedImages[i].setImageBitmap(null);
						selectDel[i].setVisibility(View.GONE);
						selectImagePath[i] = null;
					}
					
					int i = 0;
					for(String s : photos){
						selectedImages[i].setImageBitmap(ImageUtils.createTumnbtail(s, 128, 128));
						selectImagePath[i] = s;
						selectDel[i].setVisibility(View.VISIBLE);
						i++;
					}
					if(loSelectedImage.getVisibility() == View.GONE){
						loSelectedImage.setVisibility(View.VISIBLE);
					}
				}
			}
			return;
		}else if(TAKE_PICTURE == requestCode &&resultCode == RESULT_OK ){
				String[] photos = new String[1];
				photos[0] = Environment.getExternalStorageDirectory()+"/jrj/tougu/.images/image_photo.jpg";
				if(photos != null){
					for(int i = 0 ; i < selectedImages.length ; i++){
						selectedImages[i].setImageBitmap(null);
						selectImagePath[i] = null;
						selectDel[i].setVisibility(View.GONE);
					}
					
					int i = 0;
					for(String s : photos){
						selectedImages[i].setImageBitmap(ImageUtils.createTumnbtail(s, 128, 128));
						selectImagePath[i] = s;
						selectDel[i].setVisibility(View.VISIBLE);
						i++;
					}
					if(loSelectedImage.getVisibility() == View.GONE){
						loSelectedImage.setVisibility(View.VISIBLE);
					}
				}
				return;
		}else if(RESULT_LOAD_IMAGE == requestCode &&resultCode == RESULT_OK ){
			if (null!=data) {  
                Uri contentUri = data.getData();  
                String img_path = getPath(getContext(),contentUri);
                if(!StringUtils.isEmpty(img_path)){
                	String[] photos = {img_path};
                	if(photos != null){
    					for(int i = 0 ; i < selectedImages.length ; i++){
    						selectedImages[i].setImageBitmap(null);
    						selectImagePath[i] = null;
    						selectDel[i].setVisibility(View.GONE);
    					}
    					
    					int i = 0;
    					for(String s : photos){
    						selectedImages[i].setImageBitmap(ImageUtils.createTumnbtail(s, 128, 128));
    						selectImagePath[i] = s;
    						selectDel[i].setVisibility(View.VISIBLE);
    						i++;
    					}
    					if(loSelectedImage.getVisibility() == View.GONE){
    						loSelectedImage.setVisibility(View.VISIBLE);
    					}
    				}
                }
                  
            }
			return;
		}else if(REQUEST_CATEGORY_CODE == requestCode && resultCode == OptionCategoryListActivity.SELECT_RESPONSE_CODE){
			if(data != null){
				String index = data.getStringExtra("category_index");
				String name = data.getStringExtra("category_name");
				if(StringUtils.isEmpty(index) || StringUtils.isEmpty(index)){
					
				}else{
					currentItem = new String[]{index,name};
					currSpinnerItem.setText(name);
				}
				
			}
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
		
	}
	
	private void postOpinoin(String urlPath) {
		
		if(currentItem == null){
			Toast.makeText(WriteOpinionActivity.this, "请选择观点分类", Toast.LENGTH_SHORT).show();
			return;
		}
		String titleStr = opTitle.getText().toString();
		if(StringUtils.isEmpty(titleStr) || titleStr.length() < 3){
			Toast.makeText(WriteOpinionActivity.this, "请输入观点标题(3-20字)", Toast.LENGTH_SHORT).show();
			return;
		}
		String contentStr = opContent.getText().toString();
		if(StringUtils.isEmpty(contentStr)){
			Toast.makeText(WriteOpinionActivity.this, "请输入观点内容", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(StringUtils.isEmpty(urlPath)){
			
		}else{
			urlPath = NetUrlLoginAndRegist.UPLOAD_PREFIX + urlPath;
			contentStr += "<img src=\""+urlPath+"\" />";
		}

		Map<String,String> params = new HashMap<String,String>();
		params.put("title", titleStr);
		params.put("content", contentStr);
		params.put("userid", UserInfo.getInstance().getUserId());
		params.put("username", UserInfo.getInstance().getUserName());
		params.put("type", currentItem[0]);
		params.put("limits", isPrivate.isChecked()?"2":"1");
		params.put("status", "2");
		params.put("label", "股票");
		
		
		JsonRequest<PostOpinionResultBean> request = new JsonRequest<PostOpinionResultBean>(Method.POST, NetUrlTougu.OPINION_POST,params,
				new RequestHandlerListener<PostOpinionResultBean>(getContext()) {

					@Override
					public void onStart(Request request) {
						super.onStart(request);
						 showDialog(request,"发布中...");
					}

					@Override
					public void onEnd(Request request) {
						super.onEnd(request);
						 hideDialog(request);
					}

					@Override
					public void onSuccess(String id, PostOpinionResultBean data) {
						// TODO Auto-generated method stub
//						Toast.makeText(ReplyActivity.this, "赞成功", Toast.LENGTH_SHORT).show();
						if(data.getRetCode() == 0){
							Toast.makeText(WriteOpinionActivity.this, "观点发布成功", Toast.LENGTH_SHORT).show();
							SharedPreferences.Editor editor = getContext().getSharedPreferences("write_opinion_draft", MODE_PRIVATE).edit();
							editor.clear();
							editor.commit();
							setResult(WRITE_SUCCESS);
							finish();
						}
					}
				}, PostOpinionResultBean.class);

		send(request);

	}
	
	
	public static String getPath(final Context context, final Uri uri) {  
		  
	    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;  
	  
	    // DocumentProvider  
	    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {  
	        // ExternalStorageProvider  
	        if (isExternalStorageDocument(uri)) {  
	            final String docId = DocumentsContract.getDocumentId(uri);  
	            final String[] split = docId.split(":");  
	            final String type = split[0];  
	  
	            if ("primary".equalsIgnoreCase(type)) {  
	                return Environment.getExternalStorageDirectory() + "/" + split[1];  
	            }  
	  
	            // TODO handle non-primary volumes  
	        }  
	        // DownloadsProvider  
	        else if (isDownloadsDocument(uri)) {  
	  
	            final String id = DocumentsContract.getDocumentId(uri);  
	            final Uri contentUri = ContentUris.withAppendedId(  
	                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));  
	  
	            return getDataColumn(context, contentUri, null, null);  
	        }  
	        // MediaProvider  
	        else if (isMediaDocument(uri)) {  
	            final String docId = DocumentsContract.getDocumentId(uri);  
	            final String[] split = docId.split(":");  
	            final String type = split[0];  
	  
	            Uri contentUri = null;  
	            if ("image".equals(type)) {  
	                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;  
	            } else if ("video".equals(type)) {  
	                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;  
	            } else if ("audio".equals(type)) {  
	                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;  
	            }  
	  
	            final String selection = "_id=?";  
	            final String[] selectionArgs = new String[] {  
	                    split[1]  
	            };  
	  
	            return getDataColumn(context, contentUri, selection, selectionArgs);  
	        }  
	    }  
	    // MediaStore (and general)  
	    else if ("content".equalsIgnoreCase(uri.getScheme())) {  
	  
	        // Return the remote address  
	        if (isGooglePhotosUri(uri))  
	            return uri.getLastPathSegment();  
	  
	        return getDataColumn(context, uri, null, null);  
	    }  
	    // File  
	    else if ("file".equalsIgnoreCase(uri.getScheme())) {  
	        return uri.getPath();  
	    }  
	  
	    return null;  
	}  
	  
	/** 
	 * Get the value of the data column for this Uri. This is useful for 
	 * MediaStore Uris, and other file-based ContentProviders. 
	 * 
	 * @param context The context. 
	 * @param uri The Uri to query. 
	 * @param selection (Optional) Filter used in the query. 
	 * @param selectionArgs (Optional) Selection arguments used in the query. 
	 * @return The value of the _data column, which is typically a file path. 
	 */  
	public static String getDataColumn(Context context, Uri uri, String selection,  
	        String[] selectionArgs) {  
	  
	    Cursor cursor = null;  
	    final String column = "_data";  
	    final String[] projection = {  
	            column  
	    };  
	  
	    try {  
	        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,  
	                null);  
	        if (cursor != null && cursor.moveToFirst()) {  
	            final int index = cursor.getColumnIndexOrThrow(column);  
	            return cursor.getString(index);  
	        }  
	    } finally {  
	        if (cursor != null)  
	            cursor.close();  
	    }  
	    return null;  
	}  
	  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is ExternalStorageProvider. 
	 */  
	public static boolean isExternalStorageDocument(Uri uri) {  
	    return "com.android.externalstorage.documents".equals(uri.getAuthority());  
	}  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is DownloadsProvider. 
	 */  
	public static boolean isDownloadsDocument(Uri uri) {  
	    return "com.android.providers.downloads.documents".equals(uri.getAuthority());  
	}  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is MediaProvider. 
	 */  
	public static boolean isMediaDocument(Uri uri) {  
	    return "com.android.providers.media.documents".equals(uri.getAuthority());  
	}  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is Google Photos. 
	 */  
	public static boolean isGooglePhotosUri(Uri uri) {  
	    return "com.google.android.apps.photos.content".equals(uri.getAuthority());  
	} 
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_DOWN)) {
			if(checkNeedSave()){
				
			}else{
				finish();
			}
			return true;
		}
		return false;
	}
	
	private boolean checkNeedSave(){
		final String opTitleStr = opTitle.getText().toString();
		final String opContentStr = opContent.getText().toString();
		if(!StringUtils.isEmpty(opTitleStr) || !StringUtils.isEmpty(opContentStr)){
			
			final CommenBottomDialog dialog = new CommenBottomDialog(this);
			dialog.setDialogTitle("保存草稿吗？");
			dialog.addActionItem("是",R.color.font_c30114, new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					SharedPreferences.Editor editor = getContext().getSharedPreferences(prefix+"write_opinion_draft", MODE_PRIVATE).edit();
					if(currentItem != null){
						editor.putString("write_opinion_draft_type_index", currentItem[0]);
						editor.putString("write_opinion_draft_type_name", currentItem[1]);
					}
					if(!StringUtils.isEmpty(opTitleStr)){
						editor.putString("write_opinion_draft_title", opTitleStr);
					}
					if(!StringUtils.isEmpty(opContentStr)){
						editor.putString("write_opinion_draft_content", opContentStr);
					}
					editor.commit();
					dialog.dismiss();
					finish();
				}
			});
			
			dialog.addActionItem("否",0, new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					SharedPreferences.Editor editor = getContext().getSharedPreferences(prefix+"write_opinion_draft", MODE_PRIVATE).edit();
					editor.clear();
					editor.commit();
					dialog.dismiss();
					finish();
				}
			});
			dialog.show();
			return true;
		}else{
			SharedPreferences.Editor editor = getContext().getSharedPreferences(prefix+"write_opinion_draft", MODE_PRIVATE).edit();
			editor.clear();
			editor.commit();
			return false;
		}
	}
	
}
