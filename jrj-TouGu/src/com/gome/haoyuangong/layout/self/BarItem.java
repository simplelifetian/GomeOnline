package com.gome.haoyuangong.layout.self;


import com.gome.haoyuangong.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BarItem extends LinearLayout {
	private ImageView imageView;
	private ImageView arrowView;
	protected ImageView _tempImage;
	private TextView titleText;
	private TextView stateText;
	private TextView infoText;
	private LinearLayout linearLayout;
	private LinearLayout arrowLayout;
	private LinearLayout rightImageLayout;
	private int itemHeight;
	private boolean drawBottomLine = false;
	private Object tag;
	private boolean editable=false;
	public BarItem(Context context) {
		super(context);
		init();
		// TODO Auto-generated constructor stub
		
	}
	public BarItem(Context context,boolean editable){	
		super(context);
		this.editable = editable;
		init();
	}
	private void init(){
//		this.setBackgroundColor(Color.WHITE);
		this.setBackgroundResource(R.drawable.selector_item_btn);
		if (getContext().getResources().getDisplayMetrics().density < 3)
			itemHeight = Function.getFitPx(getContext(), 140);
		else
			itemHeight = Function.getFitPx(getContext(), 120);
		linearLayout = new LinearLayout(getContext());
		imageView = new ImageView(getContext());
//		imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.icon_v));
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE); 
        
        if (editable){        	
        	titleText = new EditText(getContext());
        	titleText.setPadding(0, 0, 0, 0);
            titleText.setBackgroundColor(Color.WHITE);
        }
        else
        	titleText = new TextView(getContext());
        titleText.setHintTextColor(getResources().getColor(R.color.font_cccccc));
        titleText.setTextColor(getContext().getResources().getColor(R.color.font_727272));
	    titleText.setTextSize(TypedValue.COMPLEX_UNIT_DIP,Function.px2sp(getContext(), 45));
	    titleText.setGravity(Gravity.CENTER_VERTICAL);
        
        stateText = new TextView(getContext());
        stateText.setPadding(Function.getFitPx(getContext(), 10), 0, 0, 0);
        stateText.setTextColor(getContext().getResources().getColor(R.color.font_727272));
        stateText.setTextSize(TypedValue.COMPLEX_UNIT_DIP,Function.px2sp(getContext(), 45));
        stateText.setGravity(Gravity.CENTER_VERTICAL);
        
        infoText = new TextView(getContext());
        infoText.setMaxLines(1);
        infoText.setEllipsize(TruncateAt.END);
        infoText.setMaxEms(15);
        infoText.setTextColor(getContext().getResources().getColor(R.color.font_595959));
        infoText.setTextSize(TypedValue.COMPLEX_UNIT_DIP,Function.px2sp(getContext(), 45));
        infoText.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
        infoText.setPadding(0, 0, 5, 0);
        
        arrowLayout = new LinearLayout(getContext());
//        arrowLayout.setBackgroundColor(Color.WHITE);
        
        rightImageLayout = new LinearLayout(getContext());
//        rightImageLayout.setBackgroundColor(Color.WHITE);
        
        arrowView = new ImageView(getContext());
//        arrowView.setBackgroundColor(Color.WHITE);
        arrowView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        arrowView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.right_arrow));
        
		doLayout();
		imageView.setVisibility(GONE);
	}
	private void doLayout(){
		LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1);
        p.setMargins(Function.getFitPx(getContext(),40),0,0,1);
        addView(linearLayout,p);

        p = new LayoutParams(Function.getFitPx(getContext(), 60), LayoutParams.MATCH_PARENT);
        p.setMargins(0,0,Function.getFitPx(getContext(), 10),1);
        imageView.setLayoutParams(p);
        linearLayout.addView(imageView, p);
        
        p = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        p.setMargins(0,0,0,1);
        linearLayout.addView(titleText, p);
        p = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        p.setMargins(0,0,0,1);        
        linearLayout.addView(stateText, p);
        addInfoView();
        addRightImageLayout();
        addRightFlag();
	}
	protected void addInfoView(){
        LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        p.setMargins(0,0,0,1);
        infoText.setLayoutParams(p);
        linearLayout.addView(infoText, p);
    }
	
	protected void addRightFlag(){
        LayoutParams p = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        p.setMargins(0,0,Function.getFitPx(getContext(), 40),2);
        addView(arrowLayout,p);

        p = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        LinearLayout tlayout = new LinearLayout(getContext());
        tlayout.setGravity(Gravity.CENTER);
        arrowLayout.addView(tlayout,p);

        p = new LayoutParams(LayoutParams.MATCH_PARENT, Function.getFitPx(getContext(),40));
//        ImageView image = new ImageView(getContext());
//        image.setBackgroundColor(Color.WHITE);
//        image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//        image.setImageDrawable(getContext().getResources().getDrawable(R.drawable.right_arrow));
        tlayout.addView(arrowView, p);
    }
	
	protected void addRightImageLayout(){
		LayoutParams p = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        p.setMargins(0,0,Function.getFitPx(getContext(),10),2);
        addView(rightImageLayout,p);
	}
	
	public int getItemHeight(){
		return itemHeight;
	}
	public void setRightArrowVisible(int visible){
		arrowLayout.setVisibility(visible);
	}
	public ImageView getRightImageView(){
		return arrowView;
	}
	public void setTitleFontColor(int color){
		titleText.setTextColor(color);
	}
	public void setInfoFontColor(int color){
		infoText.setTextColor(color);
	}
	public void setInfoBackColor(int color){
		infoText.setBackgroundColor(color);
	}
	public void setDrawBottomLine(boolean value){
		if (value != drawBottomLine){
			drawBottomLine = value;
			invalidate();
		}
	}
	public void setHeadImage(int resid){
//		imageView.getLayoutParams().width = width;
		imageView.setImageDrawable(getContext().getResources().getDrawable(resid));
		setHeadImageVisible(true);
	}
	public void setHeadImage(int resid,int width){
		setHeadImage(resid);
		imageView.getLayoutParams().width = Function.getFitPx(getContext(), width);
	}
	public void setHeadImageVisible(boolean visible){
		if (!visible)
			imageView.setVisibility(GONE);
		else
			imageView.setVisibility(VISIBLE);
	}
	
	public Object getTag() {
		return tag;
	}
	public void setTag(Object tag) {
		this.tag = tag;
	}
	public void setTitle(String value){
		titleText.setText(value);
    }
	public void setTitleHint(String value){
		titleText.setHint(value);
	}
	public String getTitle(){
		return titleText.getText().toString();
    }
	public void setTitleFontSize(int size){
		titleText.setTextSize(TypedValue.COMPLEX_UNIT_SP,Function.px2sp(getContext(), size));		
	}
	public void setInfoFontSize(int size){
		infoText.setTextSize(TypedValue.COMPLEX_UNIT_SP,Function.px2sp(getContext(), size));
	}
    public void setInfoText(String value){
    	infoText.setText(value);
    }
    public void setInfoText(SpannableStringBuilder value){
    	infoText.setText(value);
    }
    public void setInfoTextWidth(int width){
    	infoText.getLayoutParams().width = Function.getFitPx(getContext(), width);
    }
    public String getInfoText(){
    	return infoText.getText().toString();
    }
    public void setStateText(String value){
    	stateText.setText(value);
    }
    public void setStateFontColor(int color){
    	stateText.setTextColor(color);
    }
    public String getStateText(){
    	return stateText.getText().toString();
    }
    public void setHeadViewClick(OnClickListener listener){
    	imageView.setOnClickListener(listener);
    }
    
    public void addRightImage(int resid,int width,boolean clear,OnClickListener clicklistner){
    	addRightImage(resid,width,clear);
    	if (_tempImage != null){
    		_tempImage.setOnClickListener(clicklistner);
    	}
    }
    public void addRightImage(int resid,int width,boolean clear){
    	Bitmap bitmap = null;
    	if (resid != -1){
    		if (resid > 0)
    			bitmap = ((BitmapDrawable)getContext().getResources().getDrawable(resid)).getBitmap();
			addRightImage(bitmap,width,clear);
    	}
    	else
    		addRightImage(null,width,clear);
    }
    public void addRightImage(Bitmap bitmap,int width,boolean clear){
    	if (_tempImage != null)
    		_tempImage = null;
    	if (clear)
    		rightImageLayout.removeAllViews();
    	LayoutParams p = new LayoutParams(Function.getFitPx(getContext(), width), LayoutParams.MATCH_PARENT);
    	ImageView image = new ImageView(getContext());
    	image.setScaleType(ScaleType.CENTER_INSIDE);
    	if (bitmap != null)
    		image.setImageBitmap(bitmap);
    	p.setMargins(Function.getFitPx(getContext(),10), 0, 0, 0);
    	rightImageLayout.addView(image,p);
    	_tempImage = image;
    }
    public ImageView getTempImageView() {
		return _tempImage;
	}
    
    protected void onDraw(Canvas canvas){
    	if (!drawBottomLine)
    		return;
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        paint.setColor(getContext().getResources().getColor(R.color.list_divider_color));
        canvas.drawLine(getLeft()+Function.getFitPx(getContext(), 40),getHeight()-1,getRight(),getHeight()-1,paint);
    }    
}

