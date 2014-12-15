package com.gome.haoyuangong.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class TagTextView extends TextView {

	public TagTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	private Object tag;
	public Object getTag() {
		return tag;
	}
	public void setTag(Object tag) {
		this.tag = tag;
	}

}
