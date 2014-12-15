package com.gome.haoyuangong.dialog;


import com.gome.haoyuangong.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;



public class BottomShareDialog extends Dialog implements android.view.View.OnClickListener{
  
  private Context context;
  
  private OnConfirmListener listener;
  public BottomShareDialog(Context context) {
    this(context,R.style.BaseDialog);
  }
  private BottomShareDialog(Context context,int style){
    super(context,style);
    this.context = context;
    initView();
  }
  private void initView(){
    LayoutInflater  inflater=LayoutInflater.from(context);
    View v;
    v = inflater.inflate(R.layout.share_dialog_bottom,null);
    
//    div01= (TextView) v.findViewById(R.id.select1);
//    div02= (TextView) v.findViewById(R.id.select2);
    TextView cancel= (TextView) v.findViewById(R.id.cancel);
    setContentView(v);
    int w = getWinWidth(context);
    int h = getWinHeight(context);
    WindowManager.LayoutParams params = this.getWindow().getAttributes();
    params.width =w-(w>>3);
//    params.x = w>>1;
    params.y = h;
//    this.getWindow().setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);
//    params.gravity = Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM;
    this.getWindow().setWindowAnimations(R.style.Dialog_anim);
    this.getWindow().setAttributes(params);
//    div01.setOnClickListener(this);
//    div02.setOnClickListener(this);
    cancel.setOnClickListener(this);
    
    LinearLayout select1 = (LinearLayout)findViewById(R.id.select1);
    LinearLayout select2 = (LinearLayout)findViewById(R.id.select2);
    LinearLayout select3 = (LinearLayout)findViewById(R.id.select3);
    select1.setOnClickListener(this);
    select2.setOnClickListener(this);
    select3.setOnClickListener(this);
    
    this.setCanceledOnTouchOutside(false);
  }
  @Override
  public void onClick(View v) {
    int id = v.getId();
		if (id == R.id.select1) {
			if(listener!=null){
        listener.onConfirm(0);
      }
			this.dismiss();
		} else if (id == R.id.select2) {
			if(listener!=null){
    		listener.onConfirm(1);
    	}
			this.dismiss();
		}else if (id == R.id.select3) {
			if(listener!=null){
	    		listener.onConfirm(2);
	    	}
			this.dismiss();
		} else if (id == R.id.cancel) {
			this.dismiss();
		}
  }
  public void setOnConfirmListener(OnConfirmListener listener){
    this.listener = listener;
  }
  
  public interface OnConfirmListener{
    public void onConfirm(int index);
  }
  public int getWinWidth(Context context){
    WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);  
    return wm.getDefaultDisplay().getWidth();//
  }
  public int getWinHeight(Context context){
    WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);  
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
  
//  public void setText1(String str){
//  	div01.setText(str);
//  }
//  public void setText1(int id){
//  	div01.setText(id);
//  }
//  public void setText2(String str){
//  	div02.setText(str);
//  }
//  public void setText2(int id){
//  	div02.setText(id);
//  }
  
}
