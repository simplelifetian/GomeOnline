package com.gome.haoyuangong.layout.self;

import java.util.ArrayList;
import java.util.List;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.layout.self.SelfView.BusinessType;
import com.gome.haoyuangong.views.xlistview.XListView;
import com.gome.haoyuangong.views.xlistview.XListView.IXListViewListener;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

public class InvestOpinion extends LinearLayout {
	private String invester;
	LinearLayout mainLayout;
	private XListView myListView;
	private Adapter myAdapter;
	private List<Object> items;
	public InvestOpinion(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		items = new ArrayList<Object>();
		addItems();
		myListView = new XListView(context);		
		mainLayout = new LinearLayout(getContext());
		mainLayout.setOrientation(VERTICAL);
		doLayout();
		myAdapter = new Adapter(context);
		myListView.setAdapter(myAdapter);	
	}
	private void addItems(){
		for(int i=0;i<10;i++){
			OpinionItem opinionItem = new OpinionItem(getContext());
			opinionItem.setInvester("李家智");
			items.add(opinionItem);
		}
	}
	private void doLayout(){
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);	
		addView(mainLayout,p);
		mainLayout.addView(myListView,p);
	}
	
	public String getInvester() {
		return invester;
	}
	public void setInvester(String invester) {
		this.invester = invester;
	}
	private class OpinionItem extends LinearLayout {
		OpinionHead opinionHead;
		TextView titleView;
		TextView contentView;
		private ImageView contentPic;
		private ImageView leftCornerPic;
		public OpinionItem(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			this.setOrientation(VERTICAL);
			this.setBackgroundColor(context.getResources().getColor(R.color.background_ECECEC));
			titleView = new TextView(context);
			titleView.setGravity(Gravity.CENTER_VERTICAL);
			titleView.setText("倒N现，考验真正来临！");
			titleView.setTextColor(context.getResources().getColor(R.color.font_595959));
			titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP,Function.px2sp(context, 50));
			contentView = new TextView(context);
			contentView.setGravity(Gravity.CENTER_VERTICAL);
			contentView.setText("周一大盘低开高走，大幅调整，三连阳反弹成果被吞噬一空，勉强收复的5日线、10日线和2300点关口再次失守！");
			contentView.setTextColor(context.getResources().getColor(R.color.font_727272));
			contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP,Function.px2sp(context, 50));
			contentView.setMaxLines(3);
			contentView.setEllipsize(TruncateAt.END);
			contentPic = new ImageView(context);
			contentPic.setScaleType(ScaleType.FIT_START);
			contentPic.setBackgroundResource(R.drawable.icon111);
			leftCornerPic = new ImageView(context);
			leftCornerPic.setScaleType(ScaleType.FIT_START);
			leftCornerPic.setBackgroundResource(R.drawable.opinion_mi);
			doLayout();
		}
		private void doLayout(){
			RelativeLayout rLayout = new RelativeLayout(getContext());
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			p.setMargins(0, 0, 0, Function.getFitPx(getContext(), 40));
			addView(rLayout,p);
			
			
			RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			LinearLayout layout = new LinearLayout(getContext());
			layout.setBackgroundColor(Color.WHITE);
			layout.setOrientation(VERTICAL);
//			p.setMargins(0, 0, 0, Function.getFitPx(getContext(), 40));
			rLayout.addView(layout,rp);
			opinionHead = new OpinionHead(getContext());	
			p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,opinionHead.getItemHeight());
			layout.addView(opinionHead,p);
			p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			p.setMargins(Function.getFitPx(getContext(), 160), Function.getFitPx(getContext(), 36), Function.getFitPx(getContext(), 40), 0);
			layout.addView(titleView,p);
			p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			p.setMargins(Function.getFitPx(getContext(), 160), Function.getFitPx(getContext(), 36), Function.getFitPx(getContext(), 40), 0);
			layout.addView(contentView,p);
			//内容图
			int picH = Function.getFitPx(getContext(), 220);
			p = new LinearLayout.LayoutParams(picH,picH);
			p.setMargins(Function.getFitPx(getContext(), 160), Function.getFitPx(getContext(), 36), 
					Function.getFitPx(getContext(), 40), Function.getFitPx(getContext(), 20));
			layout.addView(contentPic,p);
			//左下角图片
			picH = Function.getFitPx(getContext(), 114);
			rp = new RelativeLayout.LayoutParams(picH,picH);
			rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			rp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			rLayout.addView(leftCornerPic,rp);
		}
		public void setInvester(String invester) {
			opinionHead.setInvesterName(invester);
		}
		
	}
	private class OpinionHead extends RelativeLayout {
		private ImageView headPic;		
		private TextView nameText;
		private TextView identityText;
		private TextView companyText;
		private TextView dateText;
		private int headHeight;
		public OpinionHead(Context context) {
			super(context);
			this.setBackgroundColor(Color.WHITE);
			headHeight = Function.getFitPx(context, 140);
			initComponent(context);
			doLayout();
			// TODO Auto-generated constructor stub
		}
		
		private int getItemHeight(){
			return headHeight;
		}
		
		private void initComponent(Context context){
			headPic = new ImageView(context);
			headPic.setScaleType(ScaleType.FIT_START);
			headPic.setBackgroundResource(R.drawable.icon111);			
			nameText = new TextView(context);
			nameText.setTextSize(TypedValue.COMPLEX_UNIT_SP,Function.px2sp(getContext(), 40));
			nameText.setTextColor(context.getResources().getColor(R.color.font_4c86c6));
//			nameText.setPadding(0, Function.getFitPx(context, 20), 0, 0);
//			nameText.setGravity(Gravity.CENTER_VERTICAL);
			identityText = new TextView(context);
			identityText.setText("投资顾问");
			identityText.setTextSize(TypedValue.COMPLEX_UNIT_SP,Function.px2sp(getContext(), 36));
			identityText.setTextColor(context.getResources().getColor(R.color.font_727272));
			identityText.setGravity(Gravity.CENTER_VERTICAL|Gravity.BOTTOM);
			companyText = new TextView(context);
			companyText.setText("国信证券");
			companyText.setGravity(Gravity.CENTER_VERTICAL|Gravity.BOTTOM);
			companyText.setTextSize(TypedValue.COMPLEX_UNIT_SP,Function.px2sp(getContext(), 36));
			companyText.setTextColor(context.getResources().getColor(R.color.font_727272));
			dateText = new TextView(context);
			dateText.setText("10-17 12:10");
			dateText.setGravity(Gravity.BOTTOM|Gravity.RIGHT);
			dateText.setTextSize(TypedValue.COMPLEX_UNIT_SP,Function.px2sp(getContext(), 36));
			dateText.setTextColor(context.getResources().getColor(R.color.font_b2b2b2));
		}
		
		private void doLayout(){
			this.removeAllViews();
			LinearLayout.LayoutParams lp = null;
			RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			p.setMargins(Function.getFitPx(getContext(), 40), Function.getFitPx(getContext(), 40), 0, 0);
			RelativeLayout pLayout = new RelativeLayout(getContext());
			pLayout.setBackgroundColor(getContext().getResources().getColor(R.color.divider));
			addView(pLayout,p);
			
			p = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			RelativeLayout layout = new RelativeLayout(getContext());
			layout.setBackgroundColor(Color.WHITE);
			pLayout.addView(layout,p);

			p = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			p.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//			p.addRule(RelativeLayout.LEFT_OF,divideLayout.getId());
			RelativeLayout leftLayout = new RelativeLayout(getContext());
			layout.addView(leftLayout,p);
			
			//头像
			LinearLayout headLayout = new LinearLayout(getContext());
			headLayout.setGravity(Gravity.CENTER);
			headLayout.setId(1003);
			int picH = Function.getFitPx(getContext(), 100);
			p = new RelativeLayout.LayoutParams(picH,LayoutParams.MATCH_PARENT);
			p.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			leftLayout.addView(headLayout,p);
			lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,picH);
			headLayout.addView(headPic,lp);
			//姓名
			p = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, headHeight/2);
			p.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			p.addRule(RelativeLayout.RIGHT_OF,headLayout.getId());
			p.setMargins(Function.getFitPx(getContext(), 20), 0, 0, 0);
			LinearLayout nameLayout = new LinearLayout(getContext());
			nameLayout.setId(1004);
			leftLayout.addView(nameLayout,p);
			lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			nameLayout.addView(nameText,lp);
			//日期
			p = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, headHeight/2);
			p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			p.addRule(RelativeLayout.RIGHT_OF,nameLayout.getId());
			p.setMargins(Function.getFitPx(getContext(), 20), 0, Function.getFitPx(getContext(), 40), 0);
			LinearLayout dateLayout = new LinearLayout(getContext());
			dateLayout.setGravity(Gravity.RIGHT);
			leftLayout.addView(dateLayout,p);
			lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			dateLayout.addView(dateText,lp);
			//身份及公司
			p = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, headHeight/2);
			p.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			p.addRule(RelativeLayout.RIGHT_OF,headLayout.getId());
			p.setMargins(Function.getFitPx(getContext(), 20), 0, 0, 0);
			LinearLayout identityLayout = new LinearLayout(getContext());
			identityLayout.setOrientation(HORIZONTAL);
			leftLayout.addView(identityLayout,p);
			lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
			identityLayout.addView(identityText,lp);
			lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
			lp.setMargins(Function.getFitPx(getContext(), 20), 0, 0, 0);
			identityLayout.addView(companyText,lp);			
			
		}
		public void setInvesterName(String name){
			nameText.setText(name);
		}
	}
	private class OpinionFoot extends LinearLayout {

		public OpinionFoot(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			this.setOrientation(HORIZONTAL);
			this.setBackgroundColor(context.getResources().getColor(R.color.divider));
			doLayout();
		}
		private void doLayout(){
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,Function.getFitPx(getContext(), 150),1);
			OpinionFootItem item = new OpinionFootItem(getContext());
			item.setTitle("分享");
			item.setImageID(R.drawable.android_icon_share);
			p.setMargins(0, 0, 1, 0);
			addView(item,p);
			
			item = new OpinionFootItem(getContext());
			item.setTitle("1022");
			item.setImageID(R.drawable.icon_comment);
			p.setMargins(0, 0, 1, 0);
			addView(item,p);
			
			item = new OpinionFootItem(getContext());
			item.setTitle("2300");
			item.setImageID(R.drawable.icon_zan);
			addView(item,p);
		}
	}
	private class OpinionFootItem extends LinearLayout {
		ImageView image;
		TextView titleText;
		public OpinionFootItem(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			this.setOrientation(HORIZONTAL);
			this.setGravity(Gravity.CENTER);
			this.setBackgroundColor(Color.WHITE);
			image = new ImageView(context);
			image.setScaleType(ScaleType.CENTER_INSIDE);
			titleText = new TextView(context);
			titleText.setTextColor(context.getResources().getColor(R.color.font_4c86c6));
			titleText.setTextSize(TypedValue.COMPLEX_UNIT_SP,Function.px2sp(context, 50));
			titleText.setGravity(Gravity.CENTER_VERTICAL);
			doLayout();
		}
		private void doLayout(){
			int picH = Function.getFitPx(getContext(), 44);
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(picH, picH);
			addView(image,p);
			p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
			addView(titleText,p);
		}
		public void setImageID(int resid){
			image.setImageDrawable(getContext().getResources().getDrawable(resid));
		}
		public void setTitle(String title){
			titleText.setText(title);
		}
	}
	
	
	class Adapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public Adapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return items.get(position);
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
			if (convertView == null) {
				convertView = new OpinionItem(getContext());
				((OpinionItem)convertView).setInvester(invester);
				viewHolder = new ViewHolder();
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			return convertView;
		}

		class ViewHolder {
			ImageView headImage;
			TextView nameText;
			TextView identityText;
			TextView companyText;
			TextView signedText;
		}
	}


}
