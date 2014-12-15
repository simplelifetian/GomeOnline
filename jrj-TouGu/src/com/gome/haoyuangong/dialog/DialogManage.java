package com.gome.haoyuangong.dialog;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.dialog.CustomDialog.Builder;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.TextView;

public class DialogManage {
	public static void showSingleButtonDialog(Context context,String content,String buttonText){
		Dialog dialog;
		CustomDialog.Builder builder = new Builder(context);
		builder.setMessage(content);
		builder.setNegativeButton(buttonText, new Dialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});		
		dialog = builder.create();
		TextView tView = (TextView)dialog.findViewById(R.id.negativeButton);
		if (tView != null){
			tView.setTextColor(context.getResources().getColor(R.color.font_4c87c6));
		}
		dialog.show();	
	}
	public static void showTwoButtonDialog(Context context,String content,String btnText1,
			String btnText2,OnClickListener listener2){
		Dialog dialog;
		CustomDialog.Builder builder = new Builder(context);
		builder.setMessage(content);
		builder.setNegativeButton(btnText1, new Dialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});		
		builder.setPositiveButton(btnText2, listener2);	
		dialog = builder.create();
		TextView tView = (TextView)dialog.findViewById(R.id.negativeButton);
		if (tView != null){
			tView.setTextColor(context.getResources().getColor(R.color.font_4c87c6));
		}
		tView = (TextView)dialog.findViewById(R.id.positiveButton);
		if (tView != null){
			tView.setTextColor(context.getResources().getColor(R.color.font_4c87c6));
		}
		dialog.show();	
	}
}
