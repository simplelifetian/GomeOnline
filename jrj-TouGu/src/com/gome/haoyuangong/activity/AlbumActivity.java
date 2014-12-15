package com.gome.haoyuangong.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Thumbnails;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gome.haoyuangong.log.Logger;
import com.gome.haoyuangong.net.result.tougu.OpinionListBean;
import com.gome.haoyuangong.R;
//import android.util.Log;

public class AlbumActivity extends BaseActivity {

	private static final String TAG = AlbumActivity.class.getName();
	private static final int COLUMN_NUM = 4;
	public static final String SELECTED_PHOTOS = "selected_photos";
	public static final int RESPONSE_CODE = 1012;
	
	private ListView albumListView;
	private ListView photoListView;
	private LinearLayout loBottom;
	
	private List<AlbumData> albumList = new ArrayList<AlbumData>();
	private List<PhotoBean[]> photoList = new ArrayList<PhotoBean[]>();
	private AlbumListAdapter albumListAdapter;
	private PhotoListAdapter photoListAdapter;
	
	private final ImageView[] select = new ImageView[3];
	private final ImageView[] selectDel = new ImageView[3];
	private final PhotoBean[] selectedPhotoBean = new PhotoBean[3];
	private final RelativeLayout[] selectedLayout = new RelativeLayout[3];
	private Button buttonSelect;
	
	private String currAlbumName;
	
	private static final String[] STORE_ALBUM = {  
		Images.Media._ID,
		Images.Media.DATA, 
		Images.Media.BUCKET_ID,
		Images.Media.BUCKET_DISPLAY_NAME, 
		"COUNT(1) AS count"};
	
	private static final String[] STORE_IMAGES = { 
		MediaStore.Images.Media._ID,
		MediaStore.Images.Media.DATA,
		MediaStore.Images.Media.DISPLAY_NAME};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.album_layout);
		setTitle("选择图片");
		loBottom = (LinearLayout)findViewById(R.id.lo_bottom);
		ViewGroup.LayoutParams vg0 = loBottom.getLayoutParams();
		vg0.height = super.getScreenW() / 4;
		loBottom.setLayoutParams(vg0);
		
		albumListView = (ListView) findViewById(R.id.album_list);
		ViewGroup.LayoutParams vg = albumListView.getLayoutParams();
		vg.width = super.getScreenW() / 4;
		albumListView.setLayoutParams(vg);
		
		photoListView = (ListView) findViewById(R.id.photo_list);
		
		View bottom1 = new View(this);
		AbsListView.LayoutParams vgl1 = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,super.getScreenW() / 4);
		bottom1.setLayoutParams(vgl1);
		albumListView.addFooterView(bottom1,null,false);
		View bottom2 = new View(this);
		AbsListView.LayoutParams vgl2 = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,super.getScreenW() / 4);
		bottom2.setLayoutParams(vgl2);
		photoListView.addFooterView(bottom2,null,false);
		
		albumListAdapter = new AlbumListAdapter(this);
		albumListView.setAdapter(albumListAdapter);
		albumListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				
				if (id >= 0 && id < albumList.size()) {
					AlbumData adata = albumList.get((int)id);
					getAllPhotoByAlbum(adata.getBucketId(),adata.getAlbumName());
					photoListView.smoothScrollToPosition(0);
				}
			}
		});
		
		photoListAdapter = new PhotoListAdapter(this);
		photoListView.setAdapter(photoListAdapter);
		
		select[0] = (ImageView)findViewById(R.id.selected_1);
		select[1] = (ImageView)findViewById(R.id.selected_2);
		select[2] = (ImageView)findViewById(R.id.selected_3);
		
		selectDel[0] = (ImageView)findViewById(R.id.image_del_1);
		selectDel[1] = (ImageView)findViewById(R.id.image_del_2);
		selectDel[2] = (ImageView)findViewById(R.id.image_del_3);
		
		selectedLayout[0] = (RelativeLayout)findViewById(R.id.lo_select_1);
		selectedLayout[1] = (RelativeLayout)findViewById(R.id.lo_select_2);
		selectedLayout[2] = (RelativeLayout)findViewById(R.id.lo_select_3);
		
		for(int i = 0 ; i < select.length ; i++){
			selectedLayout[i].setTag(i);
			selectedLayout[i].setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int index = (Integer)v.getTag();
					setSelectImage(index,null);
				}
			});
		}
		
		buttonSelect = (Button)findViewById(R.id.button_select);
		buttonSelect.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				List<String> result = new ArrayList<String>();
				for(int i = 0 ; i < selectedPhotoBean.length ; i++){
					if(selectedPhotoBean[i] != null){
						result.add(selectedPhotoBean[i].getPhotoPath());
					}
				}
				intent.putExtra(SELECTED_PHOTOS, result.toArray(new String[0]));
				setResult(RESPONSE_CODE, intent);
				finish();
				
			}
		});
		
		
		
		getAllAlbum();

	}
	
	public void setSelectImage(int index,PhotoBean bean){
		if(bean == null){
			select[index].setImageBitmap(null);
			selectDel[index].setVisibility(View.GONE);
			PhotoBean willDel = selectedPhotoBean[index];
			if(willDel != null && willDel.getAlbumName().equals(currAlbumName)){
				label:for(PhotoBean[] p : photoList){
					for(PhotoBean b : p){
						if(b.getPhotoPath().equals(willDel.getPhotoPath())){
							b.setSelected(false);
							photoListAdapter.notifyDataSetChanged();
							break label;
						}
					}
				}
			}
		}else{
			select[index].setImageBitmap(bean.getImage());
			selectDel[index].setVisibility(View.VISIBLE);
		}
		selectedPhotoBean[index] = bean;
		
		int selectCount = 0;
		for(int i = 0 ; i < selectedPhotoBean.length ; i++){
			if(selectedPhotoBean[i] != null){
				selectCount++;
			}
		}
		buttonSelect.setText("确定\n("+selectCount+"/3)");
	}

	public void getAllAlbum() {
		albumList.clear();
		ContentResolver cr = getContentResolver();
		String selection = "0==0) GROUP BY (" + Images.Media.BUCKET_ID;
		String sortOrder = Images.Media.DATE_MODIFIED;
		Cursor cur = null;
		
		try{
			cur = cr.query(Images.Media.EXTERNAL_CONTENT_URI, STORE_ALBUM, selection, null, sortOrder);
			if (cur.moveToFirst()) {

				int id_column = cur.getColumnIndex(Images.Media._ID);
				int image_id_column = cur.getColumnIndex(Images.Media.DATA);
				int bucket_id_column = cur.getColumnIndex(Images.Media.BUCKET_ID);
				int bucket_name_column = cur.getColumnIndex(Images.Media.BUCKET_DISPLAY_NAME);
				int count_column = cur.getColumnIndex("count");

				do {
					// Get the field values
					int id = cur.getInt(id_column);
					String image_path = cur.getString(image_id_column);
					int bucket_id = cur.getInt(bucket_id_column);
					String bucket_name = cur.getString(bucket_name_column);
					int count = cur.getInt(count_column);
					// Do something with the values.
//					Log.e(TAG, id + "," + image_path + "," + bucket_id + "," + bucket_name + "," + count);
					AlbumData albumData = new AlbumData();
					albumData.setImageId(id);
					albumData.setThumbnailPath(image_path);
					albumData.setAlbumName(bucket_name);
					albumData.setCount(count);
					albumData.setBucketId(bucket_id);
					albumList.add(albumData);
				} while (cur.moveToNext());
			}
		}finally{
			cur.close();
		}
		
		albumListAdapter.notifyDataSetChanged();
		if(albumList.size() > 0){
			getAllPhotoByAlbum(albumList.get(0).getBucketId(),albumList.get(0).getAlbumName());
		}
		
	}
	
	public void getAllPhotoByAlbum(int bucketId,String bucketName) {
		photoList.clear();
		ContentResolver cr = getContentResolver();
		String selection = Images.Media.BUCKET_ID+"="+bucketId;
		String sortOrder = Images.Media.DATE_MODIFIED;
		Cursor cur = null;
		try{
			cur = cr.query(Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES, selection, null, sortOrder);
			if (cur.moveToFirst()) {
				int id_column = cur.getColumnIndex(MediaStore.Images.Media._ID);
				int image_data_column = cur.getColumnIndex(MediaStore.Images.Media.DATA);
				int image_name_column = cur.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);

				do {
					// Get the field values
					int id = cur.getInt(id_column);
					String image_path = cur.getString(image_data_column);
					String image_name = cur.getString(image_name_column);
					// Do something with the values.
					Logger.error(TAG, id + "," + image_path + "," + image_name);
					PhotoBean photoBean = new PhotoBean();
					photoBean.setImageId(id);
					photoBean.setPhotoPath(image_path);
					photoBean.setAlbumName(bucketName);
					for(PhotoBean s : selectedPhotoBean){
						if(s != null && s.getPhotoPath().equals(image_path)){
							photoBean.setSelected(true);
						}
					}
					if(photoList.isEmpty()){
						photoList.add(new PhotoBean[]{photoBean});
					}else{
						PhotoBean[] beans = photoList.get(photoList.size() - 1);
						if(beans.length >= 3){
							photoList.add(new PhotoBean[]{photoBean});
						}else{
							int len = beans.length;
							PhotoBean[] tmp = new PhotoBean[len + 1];
							System.arraycopy(beans, 0, tmp, 0, len);
							tmp[len] = photoBean;
							photoList.set(photoList.size() - 1, tmp);
						}
					}
					
				} while (cur.moveToNext());
			}
		}finally{
			cur.close();
		}
		currAlbumName = bucketName;
		photoListAdapter.notifyDataSetChanged();
	}

	class AlbumListAdapter extends BaseAdapter {
		
		private LayoutInflater mInflater;
		
		public AlbumListAdapter(Context context){
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return albumList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return albumList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder;
			if(convertView == null){
				convertView = mInflater.inflate(R.layout.album_item_layout, parent, false);
				viewHolder = new ViewHolder();
				viewHolder.albumItem = (RelativeLayout)convertView.findViewById(R.id.album_item);
				ViewGroup.LayoutParams vg = viewHolder.albumItem.getLayoutParams();
				vg.width = getScreenW() / 4;
				vg.height = getScreenW() / 4;
				viewHolder.albumItem.setLayoutParams(vg);
				viewHolder.imageThumb = (ImageView)convertView.findViewById(R.id.image_thumb);
				viewHolder.photoNum = (TextView)convertView.findViewById(R.id.photo_num);
				viewHolder.albumName = (TextView)convertView.findViewById(R.id.album_name);
				convertView.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder)convertView.getTag();
			}
			
			AlbumData albumData = albumList.get(position);
			if(albumData.getThumbnail() == null){
//				String imagePath = albumData.getThumbnailPath();
				Bitmap thumbtail = getThumbnail(albumData.getImageId(),albumData.getThumbnailPath());
				albumData.setThumbnail(thumbtail);
			}
			
			viewHolder.imageThumb.setImageBitmap(albumData.getThumbnail());
//			String thumbPath = getThumbPath(albumData.getImageId());
//			if(!StringUtils.isEmpty(thumbPath)){
//				try {
//					InputStream is = new FileInputStream(thumbPath);
//					Bitmap bmp = BitmapFactory.decodeStream(is);
//					viewHolder.imageThumb.setImageBitmap(bmp);
//				} catch (FileNotFoundException e) {
//					// TODO Auto-generated catch block
////					e.printStackTrace();
//					Logger.error(TAG, "get image error --> "+thumbPath);
//				}  
//			}
            
			viewHolder.photoNum.setText("("+albumData.getCount()+")");
			viewHolder.albumName.setText(albumData.getAlbumName());
			
			return convertView;
		}
		
		class ViewHolder{
			ImageView imageThumb;
			TextView photoNum;
			TextView albumName;
			RelativeLayout albumItem;
		}

	}
	
	class PhotoListAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
		
		public PhotoListAdapter(Context context){
			mInflater = LayoutInflater.from(context);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return photoList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return photoList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder;
			if(convertView == null){
				convertView = mInflater.inflate(R.layout.photo_item_layout, parent, false);
				viewHolder = new ViewHolder();
				viewHolder.photoItem = (LinearLayout)convertView.findViewById(R.id.photo_item);
				ViewGroup.LayoutParams vg = viewHolder.photoItem.getLayoutParams();
				vg.height = getScreenW() / 4;
				viewHolder.photoItem.setLayoutParams(vg);
				viewHolder.images[0] = (ImageView)convertView.findViewById(R.id.image_1);
				viewHolder.images[1] = (ImageView)convertView.findViewById(R.id.image_2);
				viewHolder.images[2] = (ImageView)convertView.findViewById(R.id.image_3);
				
				viewHolder.masker[0] = (RelativeLayout)convertView.findViewById(R.id.image_marsker_1);
				viewHolder.masker[1] = (RelativeLayout)convertView.findViewById(R.id.image_marsker_2);
				viewHolder.masker[2] = (RelativeLayout)convertView.findViewById(R.id.image_marsker_3);
				convertView.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder)convertView.getTag();
			}
			PhotoBean[] photos = photoList.get(position);
//			Logger.error(TAG, "size is : "+photos.length);
			if(photos != null){
				for(int i = 0 ; i < 3 ; i++){
					if(photos.length > i){
						final PhotoBean bean = photos[i];
						if(bean.getImage() == null){
//							String imagePath = albumData.getThumbnailPath();
							Bitmap thumbtail = getThumbnail(bean.getImageId(),bean.getPhotoPath());
							bean.setImage(thumbtail);
						}
						final ImageView currView = viewHolder.images[i];
						final View maskerView = viewHolder.masker[i];
						currView.setImageBitmap(bean.getImage());
						currView.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								boolean selected = false;
								for(int i = 0 ; i< selectedPhotoBean.length;i++){
									if(selectedPhotoBean[i] == null){
										setSelectImage(i,bean);
										selected = true;
										bean.setSelected(true);
										photoListAdapter.notifyDataSetChanged();
										break;
									}
								}
								if(!selected){
									Toast.makeText(AlbumActivity.this, "最多选3个", Toast.LENGTH_SHORT).show();
								}
							}
						});
						
						if(bean.isSelected){
							viewHolder.images[i].setEnabled(false);
							((View)viewHolder.images[i].getParent()).setBackgroundColor(getResources().getColor(R.color.font_4c87c6));
							viewHolder.masker[i].setVisibility(View.VISIBLE);
						}else{
							viewHolder.images[i].setEnabled(true);
							((View)viewHolder.images[i].getParent()).setBackgroundColor(0);
							viewHolder.masker[i].setVisibility(View.GONE);
						}
					}else{
						viewHolder.images[i].setImageBitmap(null);
						viewHolder.images[i].setOnClickListener(null);
					}
					
				}
			}
			
			return convertView;
		}

		class ViewHolder{
			ImageView images[] = new ImageView[3];
			LinearLayout photoItem;
			RelativeLayout masker[] = new RelativeLayout[3];
		}
	}

	class AlbumData {

		private int imageId;
		private String albumName;
		private String thumbnailPath;
		private int count;
		private int bucketId;
		private Bitmap thumbnail;
		
		public int getImageId() {
			return imageId;
		}

		public void setImageId(int imageId) {
			this.imageId = imageId;
		}

		public String getAlbumName() {
			return albumName;
		}

		public void setAlbumName(String albumName) {
			this.albumName = albumName;
		}

		public String getThumbnailPath() {
			return thumbnailPath;
		}

		public void setThumbnailPath(String thumbnailPath) {
			this.thumbnailPath = thumbnailPath;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}
		
		public int getBucketId() {
			return bucketId;
		}

		public void setBucketId(int bucketId) {
			this.bucketId = bucketId;
		}

		public Bitmap getThumbnail() {
			return thumbnail;
		}

		public void setThumbnail(Bitmap thumbnail) {
			this.thumbnail = thumbnail;
		}
		
	}
	
	class PhotoBean{
		
		private String photoPath;
		private int imageId;
		private Bitmap image;
		private boolean isSelected = false;
		private String albumName;

		public String getPhotoPath() {
			return photoPath;
		}

		public void setPhotoPath(String photoPath) {
			this.photoPath = photoPath;
		}

		public int getImageId() {
			return imageId;
		}

		public void setImageId(int imageId) {
			this.imageId = imageId;
		}

		public Bitmap getImage() {
			return image;
		}

		public void setImage(Bitmap image) {
			this.image = image;
		}

		public boolean isSelected() {
			return isSelected;
		}

		public void setSelected(boolean isSelected) {
			this.isSelected = isSelected;
		}

		public String getAlbumName() {
			return albumName;
		}

		public void setAlbumName(String albumName) {
			this.albumName = albumName;
		}
		
	}
	
	private Bitmap getThumbnail(int imageId,String imagePath){
		ContentResolver cr = getContentResolver();
		Cursor cursor = null;
		String thumbPath = null;
		try{
			String[] projection = { Thumbnails._ID, Thumbnails.IMAGE_ID,Thumbnails.DATA };
	        cursor = cr.query(Thumbnails.EXTERNAL_CONTENT_URI, projection,Thumbnails.IMAGE_ID + "=" + imageId, null, null);
	        if (cursor != null && cursor.moveToFirst()) {
	        	thumbPath = cursor.getString(cursor.getColumnIndex(Thumbnails.DATA));
	        }
		}finally{
			if(cursor!= null){
				cursor.close();
			}
		}
        
		Bitmap result = null;
		if(thumbPath != null){
			Bitmap temp = BitmapFactory.decodeFile(thumbPath);
			result = temp;
			if(temp != null){
				int width = temp.getWidth();
				int height = temp.getHeight();
				if(width > height){
					int edge = (width - height)/2;
					result = Bitmap.createBitmap(temp, edge, 0, height, height);
				}else if(width < height){
					int edge = (height - width)/2;
					result = Bitmap.createBitmap(temp, 0, edge, width, width);
				}
			}
		}else{
			result = BitmapFactory.decodeFile(imagePath);
		}
		
//		Logger.error(TAG, "w : "+result.getWidth() + "  h:"+result.getHeight());
        return result;
	}
	
}