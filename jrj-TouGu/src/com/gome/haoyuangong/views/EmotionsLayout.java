package com.gome.haoyuangong.views;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.gome.haoyuangong.utils.HtmlUtils;

public class EmotionsLayout extends FrameLayout {

	private ViewPager viewPager;
	private EmotionAdapter myEmotionAdapter;

	private static final int EMO_ICON_SIZE = 40;
	private static final int COLUMN_NUM = 7;
	private static final int ROW_NUM = 3;
	private static final int TOTAL_PAGE_COUNT = COLUMN_NUM * ROW_NUM;
	private static final int TOTAL_ICONS = 64;
	
	private OnItemClickListener listener;

	public EmotionsLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public EmotionsLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public EmotionsLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	private void initView(Context context) {

		viewPager = new WrapContentHeightViewPager(context);
		addView(viewPager);
		myEmotionAdapter = new EmotionAdapter(context);
		viewPager.setAdapter(myEmotionAdapter);
	}

	private class EmotionAdapter extends PagerAdapter {

		private SparseArray<View> viewArray = new SparseArray<View>();
		private Context context;

		public EmotionAdapter(Context context) {
			this.context = context;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			View v = viewArray.get(position);
			if (v == null) {
				GridView gridView = new GridView(context);
				ViewGroup.LayoutParams vl = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.WRAP_CONTENT);
				gridView.setLayoutParams(vl);
				gridView.setColumnWidth(GridView.AUTO_FIT);
				gridView.setNumColumns(COLUMN_NUM);
				final List<String> iconData = new ArrayList<String>();
				int start = position * TOTAL_PAGE_COUNT;
				int end = (position + 1) * TOTAL_PAGE_COUNT;
				end = end > TOTAL_ICONS ? TOTAL_ICONS : end;
				for (int i = start; i < end; i++) {
					iconData.add("emote/" + i + ".gif");
				}
				gridView.setAdapter(new GridViewAdapter(context, iconData));
				gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						// TODO Auto-generated method stub
						if(listener != null){
							String name = iconData.get(position);
							listener.onEmotionClick(name);
						}
					}
				});
				v = gridView;
				viewArray.put(position, v);
			}
			container.addView(v);
			return v;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return TOTAL_ICONS % TOTAL_PAGE_COUNT == 0 ? (TOTAL_ICONS / TOTAL_PAGE_COUNT) : (TOTAL_ICONS
					/ TOTAL_PAGE_COUNT + 1);
		}

		@Override
		public void destroyItem(ViewGroup view, int position, Object object) {
			view.removeView(viewArray.get(position));
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

	}
	
	public void setOnItemClickListener(OnItemClickListener listener){
		this.listener = listener;
	}
	
	public static interface OnItemClickListener{
		public void onEmotionClick(String emoName);
	}

	private class GridViewAdapter extends BaseAdapter {

		private Context context;
		private List<String> iconData = new ArrayList<String>();

		public GridViewAdapter(Context context, List<String> list) {
			this.context = context;
			iconData.addAll(list);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return iconData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return iconData.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder vHolder;
			if (convertView == null) {
				MyImageView tv = new MyImageView(context);
				tv.setScaleType(ScaleType.CENTER);
				// AbsListView.LayoutParams vl = new
				// AbsListView.LayoutParams(EMO_ICON_SIZE,EMO_ICON_SIZE);
				// tv.setLayoutParams(vl);
				tv.setPadding(0, EMO_ICON_SIZE / 2, 0, EMO_ICON_SIZE / 2);
				convertView = tv;
				vHolder = new ViewHolder();
				vHolder.icon = tv;
				convertView.setTag(vHolder);
			} else {
				vHolder = (ViewHolder) convertView.getTag();
			}

			String icon = iconData.get(position);
			vHolder.icon.setImageResource(HtmlUtils.getEmotionResId(icon).getResId());
			return convertView;
		}

		class ViewHolder {
			ImageView icon;
		}

	}

	private class MyImageView extends ImageView {

		public MyImageView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		public MyImageView(Context context, AttributeSet attrs) {
			super(context, attrs);
			// TODO Auto-generated constructor stub
		}

		public MyImageView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			int size = getMeasuredWidth() > getMeasuredHeight() ? getMeasuredWidth() : getMeasuredHeight();
			super.onMeasure(MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY),
					MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY));
		}

	}

	private class WrapContentHeightViewPager extends ViewPager {

		public WrapContentHeightViewPager(Context context) {
			super(context);
		}

		public WrapContentHeightViewPager(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

			int height = 0;
			// 下面遍历所有child的高度
			for (int i = 0; i < getChildCount(); i++) {
				View child = getChildAt(i);
				child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
				int h = child.getMeasuredHeight();
				if (h > height) // 采用最大的view的高度。
					height = h;
			}

			heightMeasureSpec = MeasureSpec.makeMeasureSpec(height * ROW_NUM, MeasureSpec.EXACTLY);

			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}
}
