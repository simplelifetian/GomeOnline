package com.gome.haoyuangong.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import org.androidannotations.annotations.rest.Post;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IInterface;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;

import com.gome.haoyuangong.layout.self.Function;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.TouguBaseResult;
import com.gome.haoyuangong.net.url.NetUrlMyInfo;
import com.gome.haoyuangong.net.url.XinGeURL;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.update.IAsyncUpdate.OnUpdateListener;
import com.gome.haoyuangong.views.KeyboardLayout;
import com.gome.haoyuangong.views.KeyboardLayout.onKybdsChangeListener;
import com.gome.haoyuangong.R;

public class AdviserAccreditateSkillActivity extends BaseActivity {
	Intent intent;
	int itemWidth;
	List<String> items_one,item_two;
	LinearLayout inputLayout;
	KeyboardLayout keyLayout;
	List<SkillItem> selectedItems_one,selectedItems_two;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		intent = getIntent();
		itemWidth = getScreenW()/3-Function.getFitPx(this, 60);
		keyLayout = new KeyboardLayout(this);	
		keyLayout.setOnkbdStateListener(new onKybdsChangeListener() {
            
            public void onKeyBoardStateChange(int state) {
                    switch (state) {
                    case KeyboardLayout.KEYBOARD_STATE_HIDE:
                    		
                    	inputLayout.setVisibility(View.GONE);
                        break;
                    case KeyboardLayout.KEYBOARD_STATE_SHOW:

                    		
                            break;
                    }
            }
		});
		items_one = new ArrayList<String>();
		items_one.clear();
		items_one.add("A股");
		items_one.add("美股");
		items_one.add("港股");
		items_one.add("基金");
		items_one.add("贵金属");
		items_one.add("其他领域");
		item_two = new ArrayList<String>();
		item_two.add("技术面");
		item_two.add("基本面");
		item_two.add("长线交易");
		item_two.add("短线交易");
		item_two.add("波段操作");
		item_two.add("个股分析");
		item_two.add("基金理财");
		item_two.add("信托");
		selectedItems_one = new ArrayList<SkillItem>();
		selectedItems_two = new ArrayList<SkillItem>();
		content.removeAllViews();
		content.addView(getView());
		inputLayout.setVisibility(View.GONE);
		setTitle("投资顾问认证");
	}

	private KeyboardLayout getView(){
		keyLayout.removeAllViews();
		inputLayout = new LinearLayout(this);
		inputLayout.setGravity(Gravity.CENTER_VERTICAL);
		inputLayout.setOrientation(LinearLayout.HORIZONTAL);
		inputLayout.setBackgroundColor(Color.WHITE);
		
		RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		ScrollView scrollView = new ScrollView(this);
		keyLayout.addView(scrollView,rp);
		
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		scrollView.addView(layout,p);
		
		TextView tvTextView = createTextView("擅长投资品种，请选择1-3项",40);
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Function.getFitPx(this, 60));
		p.setMargins(Function.getFitPx(this, 20), Function.getFitPx(this, 20), Function.getFitPx(this, 20), Function.getFitPx(this, 20));
		layout.addView(tvTextView,p);		
		
		
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		layout.addView(getView_Skill(items_one,1));
		
		tvTextView = createTextView("能力标签 ，请选择1-8项",40);		
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Function.getFitPx(this, 60));
		p.setMargins(Function.getFitPx(this, 20), Function.getFitPx(this, 20), Function.getFitPx(this, 20), Function.getFitPx(this, 20));
		layout.addView(tvTextView,p);		
		
		
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		layout.addView(getView_Skill(item_two,2));
		
		tvTextView = createTextView("简介",40);
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Function.getFitPx(this, 60));
		p.setMargins(Function.getFitPx(this, 20), Function.getFitPx(this, 20), Function.getFitPx(this, 20), Function.getFitPx(this, 20));
		layout.addView(tvTextView,p);
		
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);		
		LinearLayout textLayout = new LinearLayout(this);
		textLayout.setOrientation(LinearLayout.VERTICAL);
		textLayout.setBackgroundColor(Color.WHITE);		
		layout.addView(textLayout,p);
		
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		final EditText editText = new EditText(this);
		editText.setPadding(Function.getFitPx(this, 20), 0, Function.getFitPx(this, 20), 0);
		editText.setHintTextColor(getResources().getColor(R.color.font_cccccc));
		editText.setTextColor(getResources().getColor(R.color.font_cccccc));
		editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(this, 40));
		editText.setBackgroundColor(Color.WHITE);
		editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});
		editText.setGravity(Gravity.TOP);
//		editText.setPadding(0, 0, 0, 0);
		StringBuilder sb = new StringBuilder();
		sb.append("需要填写如下信息，以便加快审核的进程，填写的信息将会在您的个人主页醒目位置显示：\n");
		sb.append("1、投顾人员可提供从业背景、学历等信息\n");
		sb.append("2、投顾人员可完善个人的荣誉记录，如新财富最佳分析师\n");
		sb.append("3、新浪投顾大赛、中国基金业金牛奖等各项认识度较高的赛事荣誉\n");
		sb.append("4、投顾人员可完善个人的成功服务案例、投资案例等");
		editText.setHint(sb.toString());
		editText.setText(InvestmentAdvisorCertificationActivity.ParamsMap.get("intro"));
		textLayout.addView(editText,p);
		
//		tvTextView = createTextView("需要填写如下信息，以便加快审核的进程，填写的信息将会在您的个人主页醒目位置显示：",30);
//		tvTextView.setTextColor(getResources().getColor(R.color.font_cccccc));
//		tvTextView.setBackgroundColor(Color.WHITE);
//		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//		tvTextView.setPadding(Function.getFitPx(this, 40), Function.getFitPx(this, 42), Function.getFitPx(this, 40), 0);
//		textLayout.addView(tvTextView,p);
//		tvTextView = createTextView("1、投顾人员可提供从业背景、学历等信息",30);
//		tvTextView.setTextColor(getResources().getColor(R.color.font_cccccc));
//		tvTextView.setBackgroundColor(Color.WHITE);
//		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//		tvTextView.setPadding(Function.getFitPx(this, 40), 0, Function.getFitPx(this, 40), 0);
//		textLayout.addView(tvTextView,p);
//		tvTextView = createTextView("2、投顾人员可可完善个人的荣誉记录，如新财富最佳分析师",30);
//		tvTextView.setTextColor(getResources().getColor(R.color.font_cccccc));
//		tvTextView.setBackgroundColor(Color.WHITE);
//		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//		tvTextView.setPadding(Function.getFitPx(this, 40), 0, Function.getFitPx(this, 40), 0);
//		textLayout.addView(tvTextView,p);
//		tvTextView = createTextView("3、新浪投顾大赛、中国基金业金牛奖，等各项认识度较高的赛事荣誉",30);
//		tvTextView.setTextColor(getResources().getColor(R.color.font_cccccc));
//		tvTextView.setBackgroundColor(Color.WHITE);
//		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//		tvTextView.setPadding(Function.getFitPx(this, 40), 0, Function.getFitPx(this, 40), 0);
//		textLayout.addView(tvTextView,p);
//		tvTextView = createTextView("4、投顾人员可可完善个人的成功服务案例、投资案例等",30);
//		tvTextView.setTextColor(getResources().getColor(R.color.font_cccccc));
//		tvTextView.setBackgroundColor(Color.WHITE);
//		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//		tvTextView.setPadding(Function.getFitPx(this, 40), 0, Function.getFitPx(this, 40), Function.getFitPx(this, 42));
//		textLayout.addView(tvTextView,p);
		
		LinearLayout checkLayout = new LinearLayout(this);
		checkLayout.setOrientation(LinearLayout.HORIZONTAL);
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);		
		layout.addView(checkLayout,p);
		
		p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		p.setMargins(Function.getFitPx(this, 40), Function.getFitPx(this, 42), 0, Function.getFitPx(this, 120));
		
		final TextView finishView = createTextView("完成",50);
		final CheckBox checkBox = new CheckBox(this);
		checkBox.setChecked(true);
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (!isChecked)
					finishView.setTextColor(getContext().getResources().getColor(R.color.font_595959));
				else
					finishView.setTextColor(getContext().getResources().getColor(R.color.font_de3031));
			}
		});
		
		checkBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(this, 36));
		checkBox.setTextColor(getResources().getColor(R.color.font_4c87c6));
		checkBox.setButtonDrawable(R.drawable.checkbox_bg);
		checkBox.setText("  ");
		checkLayout.addView(checkBox,p);
		p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		p.setMargins(0, Function.getFitPx(this, 42), 0, Function.getFitPx(this, 120));
		TextView checkView = new TextView(this);
		checkView.setTextColor(getResources().getColor(R.color.font_727272));
		checkView.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(this, 36));
		SpannableStringBuilder builder = new SpannableStringBuilder("我已经同意证券通投资顾问平台的相关协议");
		builder.setSpan(new ClickableSpan() {
			
			@Override
			public void onClick(View widget) {
				// TODO Auto-generated method stub
				WebViewActivity.gotoWebViewActivity(getContext(), "证券通投资协议", "http://itougu.jrj.com.cn/site/adviser_agreement.html");
//				startActivity(new Intent(AdviserAccreditateSkillActivity.this,ProtocolActivity.class));
			}
			public void updateDrawState(TextPaint ds) {
				ds.setColor(getResources().getColor(R.color.font_4c87c6));
		        ds.setUnderlineText(true);
			}
		}, 5, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		checkView.setText(builder);
		checkView.setMovementMethod(LinkMovementMethod.getInstance());
		checkLayout.addView(checkView,p);
		
		textLayout = new LinearLayout(this);
//		textLayout.setBackgroundColor(Color.GRAY);
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		p.setMargins(Function.getFitPx(this, 40), 0, Function.getFitPx(this, 40), Function.getFitPx(this, 40));
		layout.addView(textLayout,p);
		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		finishView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!checkBox.isChecked())
					return;
				if (selectedItems_one.size() == 0){
					Toast.makeText(getContext(), "请选择擅长投资品种", Toast.LENGTH_SHORT).show();
					return;
				}
				if (selectedItems_two.size() == 0){
					Toast.makeText(getContext(), "请选择能力标签", Toast.LENGTH_SHORT).show();
					return;
				}
				if (TextUtils.isEmpty(editText.getText())){
					Toast.makeText(getContext(), "请请填写简介", Toast.LENGTH_SHORT).show();
					return;
				}
				InvestmentAdvisorCertificationActivity.ParamsMap.put("investDirection", getSelContactString(selectedItems_one));
				InvestmentAdvisorCertificationActivity.ParamsMap.put("label", getSelContactString(selectedItems_two));
				InvestmentAdvisorCertificationActivity.ParamsMap.put("intro", editText.getText().toString());
				post();
			}
		});
		finishView.setBackgroundColor(Color.WHITE);
		finishView.setBackgroundResource(R.drawable.selector_login_button);
		int pad=getResources().getDimensionPixelSize(R.dimen.base_btn_padding);		
		finishView.setPadding(pad, pad, pad, pad);
		finishView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,19);
		finishView.setGravity(Gravity.CENTER);
		finishView.setTextColor(getContext().getResources().getColor(R.color.font_de3031));
		p.setMargins(1, 1, 1, 1);
		textLayout.addView(finishView,p);
		
		final EditText edit = new EditText(this);
		edit.setBackgroundResource(R.drawable.roundcorner_edit_background);
//		edit.setBackgroundColor(Color.WHITE);
		edit.setFocusable(true);
		edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
		edit.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(this, 46));
		p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT,1);			
		p.setMargins(Function.getFitPx(this, 40), 10, 10, 10);
		inputLayout.addView(edit,p);
		
		TextView tv = new TextView(this);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(this, 46));
		tv.setGravity(Gravity.CENTER);
		tv.setText("发送");
		tv.setTextColor(getResources().getColor(R.color.font_4c87c6));
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(edit.getText().toString()))
					return;
				selectedItems_one.clear();
				selectedItems_two.clear();
				item_two.add(item_two.size()-1, edit.getText().toString());
				content.removeAllViews();
				content.addView(getView());
//				inputLayout.setVisibility(View.GONE);
				hideSoftInput();
			}
		});
		p = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);			
		p.setMargins(Function.getFitPx(this, 40), 0, Function.getFitPx(this, 40), 0);
		inputLayout.addView(tv,p);
		rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, Function.getFitPx(this, 140));
		rp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		keyLayout.addView(inputLayout,rp);	
		
		return keyLayout;
	}
	private void post() {
		JsonRequest<TouguBaseResult> request = new JsonRequest<TouguBaseResult>(Method.POST, NetUrlMyInfo.APPLYADVISERINFO, 
				InvestmentAdvisorCertificationActivity.ParamsMap, new RequestHandlerListener<TouguBaseResult>(getContext()) {

			@Override
			public void onStart(Request request) {
				super.onStart(request);
				// showDialog(request);
			}

			@Override
			public void onEnd(Request request) {
				super.onEnd(request);
				// hideDialog(request);
			}
			@Override
					public void onFailure(String id, int code, String str,
							Object obj) {
						// TODO Auto-generated method stub
						super.onFailure(id, code, str, obj);
					}

			@Override
			public void onSuccess(String id, TouguBaseResult data) {
				// TODO Auto-generated method stub
				// Toast.makeText(ReplyActivity.this, "赞成功", Toast.LENGTH_SHORT).show();
				if (data.getRetCode() == 0) {
					Toast.makeText(AdviserAccreditateSkillActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					intent.setClass(AdviserAccreditateSkillActivity.this, AdviserAccreditationActivity.class);
					AdviserAccreditationActivity.TIPTYPE = 1;
					startActivity(intent);
//					finish();
				}
				else
					Toast.makeText(AdviserAccreditateSkillActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
			}
		}, TouguBaseResult.class);

		send(request);
	}
	private LinearLayout getView_Skill(List<String> values,int itemType){	
		List<SkillItem> selectedItems = new ArrayList<AdviserAccreditateSkillActivity.SkillItem>();
		List<String> itemsList = new ArrayList<String>();
		if (itemType == 1){
			selectedItems = selectedItems_one;
			if (InvestmentAdvisorCertificationActivity.ParamsMap.get("investDirection") != null)
				itemsList.addAll(Arrays.asList(InvestmentAdvisorCertificationActivity.ParamsMap.get("investDirection").split(",")));
		}
		else{
			selectedItems = selectedItems_two;
			if (InvestmentAdvisorCertificationActivity.ParamsMap.get("label") != null)
				itemsList.addAll(Arrays.asList(InvestmentAdvisorCertificationActivity.ParamsMap.get("label").split(",")));
		}
		LinearLayout layout = new LinearLayout(this);
		layout.setBackgroundColor(Color.WHITE);
		
		layout.setOrientation(LinearLayout.VERTICAL);
		for(int i=0;i<values.size();i+=3){
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Function.getFitPx(this, 120));	
			if (values.size() - i <= 3)
				p.setMargins(Function.getFitPx(this, 40), Function.getFitPx(this, 40), Function.getFitPx(this, 40), Function.getFitPx(this, 40));
			else {
				p.setMargins(Function.getFitPx(this, 40), Function.getFitPx(this, 40), Function.getFitPx(this, 40), 0);
			}
			LinearLayout topLayout = new LinearLayout(this);	
			topLayout.setOrientation(LinearLayout.HORIZONTAL);
			layout.addView(topLayout,p);
			
			p = new LinearLayout.LayoutParams(itemWidth, Function.getFitPx(this, 120));
			SkillItem item = new SkillItem(this);		
			item.itemType = itemType;			
			item.setContent(values.get(i));
			if (itemsList.indexOf(values.get(i))>=0){
				item.setSelected(true);
				selectedItems.add(item);
			}
			p.setMargins(0, 0, Function.getFitPx(this, 40), 0);
			topLayout.addView(item,p);
			
			LinearLayout itemLayout = new LinearLayout(this);
			
			if (i+1 <= values.size() - 1){
				p = new LinearLayout.LayoutParams(itemWidth, Function.getFitPx(this, 120));
				item = new SkillItem(this);
				item.itemType = itemType;
				item.setContent(values.get(i+1));
				if (itemsList.indexOf(values.get(i+1))>=0){
					item.setSelected(true);
					selectedItems.add(item);
				}				
				p.setMargins(0, 0, Function.getFitPx(this, 40), 0);
				topLayout.addView(item,p);
			}
			
			if (i+2 <= values.size() - 1){
				p = new LinearLayout.LayoutParams(itemWidth, Function.getFitPx(this, 120));
				item = new SkillItem(this);
				item.itemType = itemType;
				item.setContent(values.get(i+2));
				if (itemsList.indexOf(values.get(i+2))>=0){
					item.setSelected(true);
					selectedItems.add(item);
				}				
				p.setMargins(0, 0, Function.getFitPx(this, 40), 0);
				topLayout.addView(item,p);
			}
		}		
		
		return layout;
	}
	private String getSelContactString(List<SkillItem> list){
		StringBuilder sb = new StringBuilder();
		for(SkillItem item:list){
			if (sb.length() > 0)
				sb.append(",");
			sb.append(item.getContent());
			
		}
		return sb.toString();
	}
	private TextView createTextView(String text,int fontsize){
		TextView tvTextView = new TextView(this);
		tvTextView.setText(text);
		tvTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(this, fontsize));
//		tvTextView.setBackgroundColor(Color.WHITE);
		tvTextView.setGravity(Gravity.CENTER_VERTICAL);
		tvTextView.setTextColor(getResources().getColor(R.color.font_727272));
		tvTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,Function.px2sp(this, 40));
		return tvTextView;
	}
	private class SkillItem extends LinearLayout{
		private TextView contentTextView;
		private boolean selected;
		private int itemType;
		public SkillItem(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			this.setBackgroundColor(getResources().getColor(R.color.font_cccccc));
			contentTextView = new TextView(context);
			contentTextView.setBackgroundColor(Color.WHITE);
			doLayout();
			this.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					List<SkillItem> selectedItems;
					int count = 3;
					if (itemType == 1)
						selectedItems = selectedItems_one;
					else {
						selectedItems = selectedItems_two;
						count = 8;
					}
					if (contentTextView.getText().toString().equals("+")){
						inputLayout.setVisibility(VISIBLE);
						inputLayout.getChildAt(0).setFocusable(true);
						inputLayout.getChildAt(0).requestFocus();
						showSoftInput(inputLayout.getChildAt(0));
						return;
					}
					inputLayout.setVisibility(View.GONE);				
					setSelected(!selected);
					if (selected){
						if (selectedItems.size() == count){
							setSelected(!selected);
							Toast.makeText(getContext(), String.format("最多只能选择%d项", count), Toast.LENGTH_SHORT).show();
							return;
						}
						selectedItems.add(SkillItem.this);
					}
					else
						selectedItems.remove(SkillItem.this);
				}
			});
		}
		private void doLayout(){
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(itemWidth, Function.getFitPx(getContext(), 120));
			LinearLayout itemLayout = new LinearLayout(getContext());
			p.setMargins(0, 0, Function.getFitPx(getContext(), 60), 0);
			itemLayout.setBackgroundColor(getResources().getColor(R.color.font_cccccc));
			addView(itemLayout,p);
			
			p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			p.setMargins(1, 1, 1, 1);
			itemLayout.addView(contentTextView,p);
		}
		public String getContent(){
			return contentTextView.getText().toString();
		}
		public boolean isSelected() {
			return selected;
		}
		public void setSelected(boolean selected) {
			this.selected = selected;
			if (this.selected){
				contentTextView.setBackgroundColor(getResources().getColor(R.color.font_4c87c6));
				contentTextView.setTextColor(Color.WHITE);
			}
			else{
				contentTextView.setBackgroundColor(Color.WHITE);
				contentTextView.setTextColor(getResources().getColor(R.color.font_595959));
			}
		}
		protected void setContent(String text){
			contentTextView.setText(text);
			
			if (text.equals("+")){
				contentTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(getContext(), 90));
				contentTextView.setTextColor(getResources().getColor(R.color.font_cccccc));
			}
			else{
				contentTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, Function.px2sp(getContext(), 50));
				contentTextView.setTextColor(getResources().getColor(R.color.font_595959));
			}
			
			contentTextView.setGravity(Gravity.CENTER);
//			if (text.equals(skillName)){
//				contentTextView.setBackgroundColor(getResources().getColor(R.color.font_4c87c6));
//				contentTextView.setTextColor(Color.WHITE);
//			}
		}
		
	}

}
