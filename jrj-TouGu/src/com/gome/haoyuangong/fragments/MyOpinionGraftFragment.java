package com.gome.haoyuangong.fragments;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.activity.AttentionDetailActivity;
import com.gome.haoyuangong.layout.self.Function;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;

/**
 * 我的投资观点-草稿
 * @author Administrator
 *
 */
public class MyOpinionGraftFragment extends ListViewFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		addItems();
		reFresh();
	}
	
	/**
	 * 测试数据
	 */
	private void addItems(){
		for(int i=0;i<10;i++){
			OpinionItem opinionItem = new OpinionItem(getActivity());
			opinionItem.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent aIntent = new Intent(getActivity(), AttentionDetailActivity.class);
					startActivity(aIntent);
				}
			});
//			_items.add(opinionItem);
			addItem(opinionItem);
		}
	}
	
	private class OpinionItem extends LinearLayout {
		OpinionHead opinionHead;
		TextView titleView;
		TextView contentView;
		public OpinionItem(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			this.setOrientation(VERTICAL);
			this.setBackgroundColor(Color.WHITE);
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
			doLayout();
		}
		private void doLayout(){
			RelativeLayout rLayout = new RelativeLayout(getContext());
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			p.setMargins(Function.getFitPx(getContext(), 40), Function.getFitPx(getContext(), 40), 0, 0);
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
//			p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
//			p.setMargins(Function.getFitPx(getContext(), 160), Function.getFitPx(getContext(), 36), Function.getFitPx(getContext(), 40), 0);
//			layout.addView(titleView,p);
			p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			p.setMargins(Function.getFitPx(getContext(), 160), Function.getFitPx(getContext(), -16), Function.getFitPx(getContext(), 40), 0);
			layout.addView(contentView,p);
		}
		
	}
	private class OpinionHead extends LinearLayout {
		private ImageView headPic;		
		private TextView nameText;
		private TextView dateText;
		private int headHeight;
		public OpinionHead(Context context) {
			super(context);
			this.setBackgroundColor(Color.WHITE);
			this.setOrientation(HORIZONTAL);
			this.setGravity(Gravity.CENTER_VERTICAL);
			headHeight = Function.getFitPx(context, 70);
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
			headPic.setBackgroundResource(R.drawable.icon_ask);			
			nameText = new TextView(context);
			nameText.setText("倒N现，考验真正来临！");
			nameText.setTextSize(TypedValue.COMPLEX_UNIT_SP,Function.px2sp(getContext(), 40));
			nameText.setTextColor(context.getResources().getColor(R.color.font_4c86c6));
//			nameText.setPadding(0, Function.getFitPx(context, 20), 0, 0);
//			nameText.setGravity(Gravity.CENTER_VERTICAL);
			dateText = new TextView(context);
			dateText.setText("10-17 12:10");
			dateText.setGravity(Gravity.RIGHT);
			dateText.setTextSize(TypedValue.COMPLEX_UNIT_SP,Function.px2sp(getContext(), 36));
			dateText.setTextColor(context.getResources().getColor(R.color.font_b2b2b2));
		}
		
		private void doLayout(){
			this.removeAllViews();
			int picH = Function.getFitPx(getContext(), 70);
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, picH);
//			p.setMargins(Function.getFitPx(getContext(), 40), Function.getFitPx(getContext(), 40), 0, 0);
			LinearLayout pLayout = new LinearLayout(getContext());
			pLayout.setOrientation(HORIZONTAL);
			pLayout.setBackgroundColor(Color.WHITE);
			addView(pLayout,p);
			
			//头像
			p = new LinearLayout.LayoutParams(picH,LayoutParams.MATCH_PARENT);
			pLayout.addView(headPic,p);
			//标题
			p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
			p.setMargins(Function.getFitPx(getContext(), 20), 0, 0, 0);
			pLayout.addView(nameText,p);
			//日期
			p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			p.setMargins(0, 0, Function.getFitPx(getContext(), 40), 0);
			pLayout.addView(dateText,p);
			
		}
	}

}
