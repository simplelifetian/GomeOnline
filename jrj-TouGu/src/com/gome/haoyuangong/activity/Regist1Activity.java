package com.gome.haoyuangong.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.toolbox.HurlStack.UrlRewriter;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.url.NetUrlLoginAndRegist;
import com.gome.haoyuangong.net.volley.JsonRequest;
import com.gome.haoyuangong.presenter.IRegistPresenter;
import com.gome.haoyuangong.R;

@EActivity(R.layout.regist_1)
public class Regist1Activity extends BaseActivity {
	@ViewById(R.id.agreement_text)
	protected TextView mTvAgreement;
	@ViewById(R.id.agreement_check)
	protected CheckBox mCheckAgreement;
	@ViewById(R.id.phone_no)
	EditText mEdit;
	@ViewById(R.id.submit)
	View mSubmit;

	@AfterViews
	protected void init() {
		setTitle(R.string.regist);
		mTvAgreement.setMovementMethod(LinkMovementMethod.getInstance());

		String orginStr = getResources().getString(R.string.agreement);

		SpannableString str = new SpannableString(orginStr);
		str.setSpan(new ClickableSpan() {

			@Override
			public void onClick(View widget) {
				WebViewActivity.gotoWebViewActivity(Regist1Activity.this, "用户协议", NetUrlLoginAndRegist.USER_AGREEMENT);
			}

			@Override
			public void updateDrawState(TextPaint ds) {
				ds.setColor(getResources().getColor(R.color.font_4c86c6));
				ds.setUnderlineText(true); // 下划线
			}
		}, 7, orginStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mTvAgreement.setText(str);
		mTvAgreement.setMovementMethod(LinkMovementMethod.getInstance());

//		mCheckAgreement.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				mSubmit.setEnabled(isChecked);
//			}
//		});
	}

	IRegistPresenter check = new IRegistPresenter(this) {
		@Override
		public void onCheckPhone(boolean isOk) {
			if (isOk) {
//				Intent i = new Intent(Regist1Activity.this, Regist2Activity_.class);
//				i.putExtra(Regist2Activity.BUNDLE_PHONE, phone);
//				startActivity(i);
			}
		}
	};
	private String phone;

	@Click
	void submitClicked() {
		if(!mCheckAgreement.isChecked()){
			showToast("您还没有同意用户协议");
			return;
		}
		phone = mEdit.getText().toString();
		check.checkPhoneNum(phone);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
	}

}
