package com.gome.haoyuangong.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.fragments.BaseFragment;
import com.gome.haoyuangong.fragments.Invest_CurrentPosition_Fragment;
import com.gome.haoyuangong.fragments.Invest_NewOperation_Fragment;
import com.gome.haoyuangong.fragments.Invest_OrperateLog_Fragment;
import com.gome.haoyuangong.layout.self.Function;
import com.gome.haoyuangong.layout.self.data.InvestGroupList;
import com.gome.haoyuangong.layout.self.data.InvestGroupPosition;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.url.NetUrlMyInfo;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.views.MyViewPageIndicator;
/**
 * 投资组合详情页
 * @author Administrator
 *
 */
public class InvestGroupDetailActivity extends BaseActivity {
	ListView mListView1;
//	MyAdapter myAdapter;
	RelativeLayout mHead;
	LinearLayout main;
	private ViewPager mViewPager;
	private PagerAdapter mPagerAdapter;
//	private String[] tableBtnStrs = { "当前持仓", "最新操作","操盘日志" };
	private String[] tableBtnStrs = { "当前持仓"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invest_group_item_detail);
		RelativeLayout view = (RelativeLayout)findViewById(R.id.igid_head);
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(InvestGroupDetailActivity.this, InvestGroupOverViewActivity.class);
				startActivity(intent);
			}
		});
		mViewPager = (ViewPager)findViewById(R.id.viewpager);
		mPagerAdapter = new MyFragmentAdapter(getSupportFragmentManager());
		 mViewPager.setAdapter(mPagerAdapter);
		 MyViewPageIndicator indicator = (MyViewPageIndicator)findViewById(R.id.indicator);
		 
	     indicator.setViewPager(mViewPager,tableBtnStrs);
	     indicator.setVisibility(View.GONE);
		
//		setContentView(R.layout.invest_stock);
//		mHead = (RelativeLayout) findViewById(R.id.head);
//		mHead.setFocusable(true);
//		mHead.setClickable(true);
//		mHead.findViewById(R.id.leftArrow).setVisibility(View.VISIBLE);
//		mHead.findViewById(R.id.rightArrow).setVisibility(View.VISIBLE);
//		mHead.setBackgroundColor(Color.parseColor("#b2d235"));
//		mHead.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
//
//		mListView1 = (ListView) findViewById(R.id.listView1);
//		mListView1.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
//
//		myAdapter = new MyAdapter(this, R.layout.item_invest_stock);
//		mListView1.setAdapter(myAdapter);
		
		setTitle("组合详情");
	}

	private TextView createHead(String text){
		TextView tv = new TextView(this);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(this, 40));		
		return tv;
	}
	
	class ListViewAndHeadViewTouchLinstener implements View.OnTouchListener {

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			//当在列头 和 listView控件上touch时，将这个touch的事件分发给 ScrollView
			HorizontalScrollView headSrcrollView = (HorizontalScrollView) mHead
					.findViewById(R.id.horizontalScrollView1);
			headSrcrollView.onTouchEvent(arg1);
			return false;
		}
	}
		
	
public class MyFragmentAdapter extends FragmentStatePagerAdapter {
		
		private SparseArray<BaseFragment> mapFragment = new SparseArray<BaseFragment>();
		
		public MyFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			
			BaseFragment f = mapFragment.get(position);
			if(f == null){
				
				CharSequence title = getPageTitle(position);
				if(position == 0){
					Invest_CurrentPosition_Fragment _f = new Invest_CurrentPosition_Fragment();
					f = _f;					
				}
				else if (position == 1){
					Invest_NewOperation_Fragment _f = new Invest_NewOperation_Fragment();
					f = _f;
				}
				else{
					Invest_OrperateLog_Fragment _f = new Invest_OrperateLog_Fragment();
					f = _f;
				}
				
				mapFragment.put(position, f);
			}
			return f;
		}

		@Override
	    public CharSequence getPageTitle(int position) {
	      return tableBtnStrs[position % tableBtnStrs.length];
	    }
		
		@Override
		public int getCount() {
			return tableBtnStrs.length;
		}
	}
	

//	public class MyAdapter extends BaseAdapter {
//		public List<ViewHolder> mHolderList = new ArrayList<ViewHolder>();
//
//		int id_row_layout;
//		LayoutInflater mInflater;
//
//		public MyAdapter(Context context, int id_row_layout) {
//			super();
//			this.id_row_layout = id_row_layout;
//			mInflater = LayoutInflater.from(context);
//
//		}
//
//		@Override
//		public int getCount() {
//			// TODO Auto-generated method stub
//			return 250;
//		}
//
//		@Override
//		public Object getItem(int arg0) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public long getItemId(int arg0) {
//			// TODO Auto-generated method stub
//			return 0;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parentView) {
//			ViewHolder holder = null;
//			if (convertView == null) {
//				synchronized (InvestGroupDetailActivity.this) {
//					convertView = mInflater.inflate(id_row_layout, null);
//					holder = new ViewHolder();
//
//					HorizonScrollView scrollView1 = (HorizonScrollView) convertView
//							.findViewById(R.id.horizontalScrollView1);
//
//					holder.scrollView = scrollView1;
//					holder.txt1 = (TextView) convertView
//							.findViewById(R.id.textView1);
//					holder.txt2 = (TextView) convertView
//							.findViewById(R.id.igid_date);
//					holder.txt3 = (TextView) convertView
//							.findViewById(R.id.textView3);
//					holder.txt4 = (TextView) convertView
//							.findViewById(R.id.textView4);
//					holder.txt5 = (TextView) convertView
//							.findViewById(R.id.textView5);
//
//					HorizonScrollView headSrcrollView = (HorizonScrollView) mHead
//							.findViewById(R.id.horizontalScrollView1);
//					headSrcrollView
//							.AddOnScrollChangedListener(new OnScrollChangedListenerImp(
//									scrollView1));
//
//					convertView.setTag(holder);
//					mHolderList.add(holder);
//				}
//			} else {
//				holder = (ViewHolder) convertView.getTag();
//			}
//			holder.txt1.setText(position + "" + 1);
//			holder.txt2.setText(position + "" + 2);
//			holder.txt3.setText(position + "" + 3);
//			holder.txt4.setText(position + "" + 4);
//			holder.txt5.setText(position + "" + 5);
//
//			return convertView;
//		}
//
//		class OnScrollChangedListenerImp implements OnScrollChangedListener {
//			HorizonScrollView mScrollViewArg;
//
//			public OnScrollChangedListenerImp(HorizonScrollView scrollViewar) {
//				mScrollViewArg = scrollViewar;
//			}
//
//			@Override
//			public void onScrollChanged(int l, int t, int oldl, int oldt) {
//				mScrollViewArg.smoothScrollTo(l, t);
//			}
//		};
//
//		class ViewHolder {
//			TextView txt1;
//			TextView txt2;
//			TextView txt3;
//			TextView txt4;
//			TextView txt5;
//			HorizontalScrollView scrollView;
//		}
//	}// end class my

}
