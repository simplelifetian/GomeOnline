package com.gome.haoyuangong.activity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;

import com.gome.haoyuangong.R;

import android.app.Dialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Thumbnails;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


/**
 * Android实现获取本机中所有图片
 * 
 * @Description: Android实现获取本机中所有图片
 * 
 * @FileName: MyDevicePhotoActivity.java
 * 
 * @Package com.device.photo
 * 
 * @Author Hanyonglu
 * 
 * @Date 2012-5-10 下午04:43:55
 * 
 * @Version V1.0
 */
public class PhotoSeletorActivity extends FragmentActivity implements LoaderCallbacks<Cursor> {

	private static final String TAG = PhotoSeletorActivity.class.getName();

	private Bitmap bitmap = null;
	private byte[] mContent = null;

	private ListView listView = null;
	private SimpleCursorAdapter simpleCursorAdapter = null;

	private static final String[] STORE_IMAGES = { MediaStore.Images.Media.DISPLAY_NAME,
			MediaStore.Images.Media.LATITUDE, MediaStore.Images.Media.LONGITUDE, MediaStore.Images.Media._ID };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_selector_layout);

		listView = (ListView) findViewById(android.R.id.list);
		simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.photo_simple_list_item, null, STORE_IMAGES,
				new int[] { R.id.item_title, R.id.item_value }, 0);

		simpleCursorAdapter.setViewBinder(new ImageLocationBinder());
		listView.setAdapter(simpleCursorAdapter);
		// 注意此处是getSupportLoaderManager()，而不是getLoaderManager()方法。
		getSupportLoaderManager().initLoader(0, null, this);

		// 单击显示图片
		listView.setOnItemClickListener(new ShowItemImageOnClickListener());

//		getAllAlbum();
		
		getThumbnails();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		// 为了查看信息，需要用到CursorLoader。
		CursorLoader cursorLoader = new CursorLoader(this, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES,
				null, null, null);
		return cursorLoader;
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		simpleCursorAdapter.swapCursor(null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		// TODO Auto-generated method stub
		// 使用swapCursor()方法，以使旧的游标不被关闭．
		simpleCursorAdapter.swapCursor(cursor);
	}

	// 将图片的位置绑定到视图
	private class ImageLocationBinder implements ViewBinder {
		@Override
		public boolean setViewValue(View view, Cursor cursor, int arg2) {
			// TODO Auto-generated method stub
			if (arg2 == 1) {
				// 图片经度和纬度
				double latitude = cursor.getDouble(arg2);
				double longitude = cursor.getDouble(arg2 + 1);

				if (latitude == 0.0 && longitude == 0.0) {
					((TextView) view).setText("位置：未知");
				} else {
					((TextView) view).setText("位置：" + latitude + ", " + longitude);
				}

				// 需要注意：在使用ViewBinder绑定数据时，必须返回真；否则，SimpleCursorAdapter将会用自己的方式绑定数据。
				return true;
			} else {
				return false;
			}
		}
	}

	// 单击项显示图片事件监听器
	private class ShowItemImageOnClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			final Dialog dialog = new Dialog(PhotoSeletorActivity.this);
			// 以对话框形式显示图片
			dialog.setContentView(R.layout.photo_image_show);
			dialog.setTitle("图片显示");

			ImageView ivImageShow = (ImageView) dialog.findViewById(R.id.ivImageShow);
			Button btnClose = (Button) dialog.findViewById(R.id.btnClose);

			btnClose.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();

					// 释放资源
					if (bitmap != null) {
						bitmap.recycle();
					}
				}
			});

			Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon().appendPath(Long.toString(id)).build();
			Log.e("TAG", "" + uri.toString());
			ContentResolver resolver = getContentResolver();

			// 从Uri中读取图片资源
			try {
				mContent = readInputStream(resolver.openInputStream(Uri.parse(uri.toString())));
				bitmap = getBitmapFromBytes(mContent, null);
				ivImageShow.setImageBitmap(bitmap);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			dialog.show();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (bitmap != null) {
			bitmap.recycle();
		}
	}

	public byte[] readInputStream(InputStream inStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();

		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}

		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();

		return data;
	}

	/**
	 * Byte to bitmap
	 * 
	 * @param bytes
	 * @param opts
	 * @return
	 */
	public Bitmap getBitmapFromBytes(byte[] bytes, BitmapFactory.Options opts) {
		if (bytes != null) {
			if (opts != null) {
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
			} else {
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			}
		}

		return null;
	}

	public void getAllAlbum() {
		ContentResolver cr = getContentResolver();
		String[] columns = { Images.Media._ID, Images.Media.DATA, Images.Media.BUCKET_ID,
				Images.Media.BUCKET_DISPLAY_NAME, "COUNT(1) AS count" };
		String selection = "0==0) GROUP BY (" + Images.Media.BUCKET_ID;
		String sortOrder = Images.Media.DATE_MODIFIED;
		Cursor cur = cr.query(Images.Media.EXTERNAL_CONTENT_URI, columns, selection, null, sortOrder);
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
				Log.e(TAG, id + "," + image_path + "," + bucket_id + "," + bucket_name + "," + count);
			} while (cur.moveToNext());
		}
	}

	public void getThumbnails() {
		ContentResolver cr = getContentResolver();
		String[] projection = { Thumbnails._ID, Thumbnails.IMAGE_ID, Thumbnails.DATA };
		Cursor cur = cr.query(Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);

		if (cur.moveToFirst()) {
			int _id;
			int image_id;
			String image_path;
			int _idColumn = cur.getColumnIndex(Thumbnails._ID);
			int image_idColumn = cur.getColumnIndex(Thumbnails.IMAGE_ID);
			int dataColumn = cur.getColumnIndex(Thumbnails.DATA);

			do {
				// Get the field values
				_id = cur.getInt(_idColumn);
				image_id = cur.getInt(image_idColumn);
				image_path = cur.getString(dataColumn);

				// Do something with the values.
				Log.e(TAG, _id + " image_id:" + image_id + " path:" + image_path + "---");

			} while (cur.moveToNext());

		}
	}
}