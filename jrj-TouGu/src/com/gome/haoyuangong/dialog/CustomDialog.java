package com.gome.haoyuangong.dialog;


import com.gome.haoyuangong.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomDialog extends Dialog {  
		  
	    public CustomDialog(Context context) {  
	    	this(context, R.style.BaseDialog);
	    }  
	  
	    public CustomDialog(Context context, int theme) {  
	    	super(context, theme);
	    	initView(context);
	    }  
	    
	    private void initView(Context context){
	    	int w = getWinWidth(context);
	        WindowManager.LayoutParams params = this.getWindow().getAttributes();
	        params.width =w-(w>>3);
	        this.getWindow().setAttributes(params);
	    }
	    
	    public int getWinWidth(Context context){
	        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);  
	        return wm.getDefaultDisplay().getWidth();//
	      }
	      public int getWinHeight(Context context){
	        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);  
	        return wm.getDefaultDisplay().getHeight();//
	      }
	  
	    public static class Builder {  
	        private Context context;  
	        private String title;  
	        private String message;  
	        private String positiveButtonText;  
	        private String negativeButtonText;  
	        private View contentView;  
	        private DialogInterface.OnClickListener positiveButtonClickListener;  
	        private DialogInterface.OnClickListener negativeButtonClickListener;  
	  
	        public Builder(Context context) {  
	            this.context = context;  
	        }  
	  
	        public Builder setMessage(String message) {  
	            this.message = message;  
	            return this;  
	        }  
	  
	        /** 
	         * Set the Dialog message from resource 
	         *  
	         * @param title 
	         * @return 
	         */  
	        public Builder setMessage(int message) {  
	            this.message = (String) context.getText(message);  
	            return this;  
	        }  
	  
	        /** 
	         * Set the Dialog title from resource 
	         *  
	         * @param title 
	         * @return 
	         */  
	        public Builder setTitle(int title) {  
	            this.title = (String) context.getText(title);  
	            return this;  
	        }  
	  
	        /** 
	         * Set the Dialog title from String 
	         *  
	         * @param title 
	         * @return 
	         */  
	  
	        public Builder setTitle(String title) {  
	            this.title = title;  
	            return this;  
	        }  
	  
	        public Builder setContentView(View v) {  
	            this.contentView = v;  
	            return this;  
	        }  
	  
	        /** 
	         * Set the positive button resource and it's listener 
	         *  
	         * @param positiveButtonText 
	         * @return 
	         */  
	        public Builder setPositiveButton(int positiveButtonText,  
	                DialogInterface.OnClickListener listener) {  
	            this.positiveButtonText = (String) context  
	                    .getText(positiveButtonText);  
	            this.positiveButtonClickListener = listener;  
	            return this;  
	        }  
	  
	        public Builder setPositiveButton(String positiveButtonText,  
	                DialogInterface.OnClickListener listener) {  
	            this.positiveButtonText = positiveButtonText;  
	            this.positiveButtonClickListener = listener;  
	            return this;  
	        }  
	  
	        public Builder setNegativeButton(int negativeButtonText,  
	                DialogInterface.OnClickListener listener) {  
	            this.negativeButtonText = (String) context  
	                    .getText(negativeButtonText);  
	            this.negativeButtonClickListener = listener;  
	            return this;  
	        }  
	  
	        public Builder setNegativeButton(String negativeButtonText,  
	                DialogInterface.OnClickListener listener) {  
	            this.negativeButtonText = negativeButtonText;
	            this.negativeButtonClickListener = listener;  
	            return this;  
	        }  
	  
	        public CustomDialog create() {  
	            LayoutInflater inflater = (LayoutInflater) context  
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
	            // instantiate the dialog with the custom Theme  
	            final CustomDialog dialog = new CustomDialog(context);
	            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	            View layout = inflater.inflate(R.layout.dialog_normal_layout, null);  
	            dialog.addContentView(layout, new LayoutParams(  
	                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));  
	            // set the dialog title  
	            ((TextView) layout.findViewById(R.id.title)).setText(title);  
	            // set the confirm button  
	            if (positiveButtonText != null) {  
	                ((TextView) layout.findViewById(R.id.positiveButton))  
	                        .setText(positiveButtonText);  
	                if (positiveButtonClickListener != null) {  
	                    ((TextView) layout.findViewById(R.id.positiveButton))  
	                            .setOnClickListener(new View.OnClickListener() {  
	                                public void onClick(View v) {  
	                                    positiveButtonClickListener.onClick(dialog,  
	                                            DialogInterface.BUTTON_POSITIVE);  
	                                }  
	                            });  
	                }  
	            } else {  
	                // if no confirm button just set the visibility to GONE  
	                layout.findViewById(R.id.positiveButton).setVisibility(  
	                        View.GONE);  
	                layout.findViewById(R.id.dialog_divider).setVisibility(  
	                		View.GONE);  
	                layout.findViewById(R.id.negativeButton).setBackgroundResource(R.drawable.selector_bg_listitem_round_bottom);
	            }  
	            // set the cancel button  
	            if (negativeButtonText != null) {  
	                ((TextView) layout.findViewById(R.id.negativeButton))  
	                        .setText(negativeButtonText);  
	                if (negativeButtonClickListener != null) {  
	                    ((TextView) layout.findViewById(R.id.negativeButton))  
	                            .setOnClickListener(new View.OnClickListener() {  
	                                public void onClick(View v) {  
	                                    negativeButtonClickListener.onClick(dialog,  
	                                            DialogInterface.BUTTON_NEGATIVE);  
	                                }  
	                            });  
	                }  
	            } else {  
	                // if no confirm button just set the visibility to GONE  
	                layout.findViewById(R.id.negativeButton).setVisibility(  
	                        View.GONE);  
	                layout.findViewById(R.id.dialog_divider).setVisibility(  
	                		View.GONE);  
	                layout.findViewById(R.id.positiveButton).setBackgroundResource(R.drawable.selector_bg_listitem_round_bottom);
	            }  
	            if (title != null) {  
	                ((TextView) layout.findViewById(R.id.title)).setText(title);
	                }
	            // set the content message  
	            if (message != null) {  
	                ((TextView) layout.findViewById(R.id.message)).setText(message);                  
	            } else if (contentView != null) {  
	                // if no message set  
	                // add the contentView to the dialog body  
//	                ((LinearLayout) layout.findViewById(R.id.message))  
//	                        .removeAllViews();  
//	                ((LinearLayout) layout.findViewById(R.id.message)).addView(  
//	                        contentView, new LayoutParams(  
//	                                LayoutParams.WRAP_CONTENT,  
//	                                LayoutParams.WRAP_CONTENT));  
	            }  
//	            dialog.setContentView(layout);  
	            return dialog;  
	        }  
	  
	    }  
	}

