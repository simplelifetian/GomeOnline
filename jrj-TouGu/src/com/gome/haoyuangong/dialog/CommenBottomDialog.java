package com.gome.haoyuangong.dialog;

import com.gome.haoyuangong.R;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;


public class CommenBottomDialog extends Dialog implements android.view.View.OnClickListener {

	private Context context;

	private TextView title;
	private TextView cancel;
	
	private LinearLayout itemContainer;

	public CommenBottomDialog(Context context) {
		this(context, R.style.BaseDialog);
	}

	private CommenBottomDialog(Context context, int style) {
		super(context, style);
		this.context = context;
		initView();
	}

	private void initView() {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v;
		v = inflater.inflate(R.layout.dialog_common_bottom, null);
		title = (TextView)v.findViewById(R.id.title);
		cancel = (TextView) v.findViewById(R.id.cancel);
		setContentView(v);
		int w = getWinWidth(context);
		int h = getWinHeight(context);
		WindowManager.LayoutParams params = this.getWindow().getAttributes();
		params.width = w - (w >> 3);
		params.y = h;
		this.getWindow().setWindowAnimations(R.style.Dialog_anim);
		this.getWindow().setAttributes(params);
		cancel.setOnClickListener(this);
		itemContainer = (LinearLayout)v.findViewById(R.id.items_layout);
		this.setCanceledOnTouchOutside(false);
	}
	
	private TextView getItemView(String itemText,int color){
		TextView textView  = new TextView(getContext());
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		textView.setLayoutParams(params);
		textView.setBackgroundResource(R.drawable.selector_bg_noedge);
		textView.setText(itemText);
		textView.setPadding(0, dipToPixels(13), 0, dipToPixels(13));
		textView.setGravity(Gravity.CENTER);
		if(color > 0){
			textView.setTextColor(context.getResources().getColor(color));
		}else{
			textView.setTextColor(context.getResources().getColor(R.color.font_blue_light));
		}
		textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
		return textView;
	}
	
	public void setDialogTitle(String titleStr){
		title.setText(titleStr);
	}
	
	public void addActionItem(String text,int color,View.OnClickListener listener){
		View view = new View(context);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,dipToPixels(1));
		view.setLayoutParams(params);
		view.setBackgroundColor(context.getResources().getColor(R.color.divider));
		itemContainer.addView(view);
		View item = getItemView(text,color);
		itemContainer.addView(item);
		item.setOnClickListener(listener);
		int count = itemContainer.getChildCount();
		if(count > 0){
			itemContainer.getChildAt(count-1).setBackgroundResource(R.drawable.selector_bg_listitem_round_bottom);
		}
		if(count > 3){
			itemContainer.getChildAt(count-3).setBackgroundResource(R.drawable.selector_bg_noedge);
		}
		
	}
	
	public void setTitleVisibable(int visibility){
		title.setVisibility(visibility);
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.cancel) {
			this.dismiss();
		}
	}

	public int getWinWidth(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getWidth();//
	}

	public int getWinHeight(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getHeight();//
	}

	@Override
	public void show() {

		super.show();
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
	}
	
	private int dipToPixels(int dip) {
		Resources r = context.getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
		return (int) px;
	}

}
