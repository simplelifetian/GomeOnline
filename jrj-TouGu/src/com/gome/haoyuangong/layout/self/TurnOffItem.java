package com.gome.haoyuangong.layout.self;


import com.gome.haoyuangong.R;

import android.content.Context;
import android.view.View;

public class TurnOffItem extends BarItem {

	private boolean close = false;
	public TurnOffItem(Context context) {		
		super(context);		
		// TODO Auto-generated constructor stub
		update();
	}
	public boolean getClose() {
		return close;
	}
	public void setClose(boolean close) {
		this.close = close;	
		update();
	}
	private void update(){
		if (close)
			addRightImage(R.drawable.icon_check_normal,120,true);
		else
			addRightImage(R.drawable.icon_check_checked,120,true);
	}

}
