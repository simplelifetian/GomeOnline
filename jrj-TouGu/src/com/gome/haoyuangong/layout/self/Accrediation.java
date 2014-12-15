package com.gome.haoyuangong.layout.self;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.activity.BaseActivity;
import com.gome.haoyuangong.activity.LoginActivity;
import com.gome.haoyuangong.activity.ProtocolActivity;
import com.gome.haoyuangong.activity.WebViewActivity;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.BaseSignResult;
import com.gome.haoyuangong.net.result.TouguBaseResult;
import com.gome.haoyuangong.net.url.NetUrlMyInfo;
import com.gome.haoyuangong.net.url.XinGeURL;
import com.gome.haoyuangong.net.volley.JsonRequest;

public class Accrediation {
	public static String USERNAME;
	private static Context _context;	
	private static String _adviserId;
	private static String _userId;
	private static CancelListener cancelListener;
	private static OkListener okListener;
	private static IAccrediatedListener accrediatedListener;
	private static ChangeType changeType;
	static {
		cancelListener = new CancelListener();
		okListener = new OkListener();
	}

	/**
	 * 验证
	 * 
	 * @param context
	 *            上下文
	 * @param accrediationType
	 *            验证类型
	 * @param signFlag 当accrediationType=atSign时有效
	 *            1:签约，0:解除签约
	 * @param adviserId
	 *            投顾id
	 * @param userId
	 *            用户id
	 * @param accrediatedListener
	 *            回调
	 */
	public static void Accrediated(Context context,
			AccrediationType accrediationType, int signFlag, String adviserId,
			String userId, IAccrediatedListener accrediatedListener) {
		_context = context;
		_adviserId = adviserId;
		_userId = userId;
		Accrediation.accrediatedListener = accrediatedListener;
		if (accrediationType == AccrediationType.atSign){
			if (signFlag == 0) {
				changeType = ChangeType.ctUnBindSign;
				showDialog(context, R.layout.dialog_unbindsign);				
			}
			else
				doVerify(context, signFlag, accrediatedListener);
		}
		else if (accrediationType == AccrediationType.atInvestGroupDetail){
			
		}
	}

	private static void doSign(final Context context, int signFlag,
			final IAccrediatedListener accrediatedListener) {
		String url = String.format(NetUrlMyInfo.SIGNURL, _adviserId, _userId,
				signFlag);
		JsonRequest<BaseSignResult> request = new JsonRequest<BaseSignResult>(
				Method.GET, url, new RequestHandlerListener<BaseSignResult>(
						context) {
					@Override
							public void onFailure(String id, int code,
									String str, Object obj) {
								// TODO Auto-generated method stub
								super.onFailure(id, code, str, obj);
								accrediatedListener.OnAccrediated(-1);
							}
					@Override
					public void onSuccess(String id, BaseSignResult data) {
						accrediatedListener.OnAccrediated(data.getRetCode());
						if (data == null)
							return;
						switch (data.getRetCode()) {
						case 1:
						case 2:
						case 3:
						case 4:
							changeType = ChangeType.ctNone;
							accrediatedListener.OnAccrediated(data.getRetCode());
							break;
						case -8:
							changeType = ChangeType.ctNotEnoughSevenDays;
							doNotEnoughSevenDays(context);
							break;
						default:
							Function.showToask(_context, data.getMsg());
							break;
						}
					}
				}, BaseSignResult.class);
		if (context instanceof BaseActivity)
			((BaseActivity) context).send(request);
	}
	private static void doVerify(final Context context, int signFlag,
			final IAccrediatedListener accrediatedListener) {
		String url = String.format(NetUrlMyInfo.SIGN_VERIFY_URL, _adviserId, _userId);
		JsonRequest<BaseSignResult> request = new JsonRequest<BaseSignResult>(
				Method.GET, url, new RequestHandlerListener<BaseSignResult>(
						context) {
					@Override
							public void onFailure(String id, int code,
									String str, Object obj) {
								// TODO Auto-generated method stub
								super.onFailure(id, code, str, obj);
								accrediatedListener.OnAccrediated(-1);
							}
					@Override
					public void onSuccess(String id, BaseSignResult data) {
						accrediatedListener.OnAccrediated(data.getRetCode());
						if (data == null)
							return;
						switch (data.getRetCode()) {
						case -3:
							changeType = ChangeType.ctAlreadySign;
							doAlreadySign(context);
							break;
						case -5:
						case -4:
						case -2:
							changeType = ChangeType.ctUnZQT;
							doUnZQT(context);
							break;
						case -6:
							changeType = ChangeType.ctVerifyingZQT;
							doZQTVerifying(context);
							break;
						case -7:
							changeType = ChangeType.ctUnBindZQT;
							doUnBindZQT(context);
							break;
						case -8:
							changeType = ChangeType.ctNotEnoughSevenDays;
							doNotEnoughSevenDays(context);
							break;
						case 0:
							changeType = ChangeType.ctCanSign;
							showDialog(context, R.layout.dialog_unsign);
							break;
						default:
							Function.showToask(_context, data.getMsg());
							break;
						}
					}
				}, BaseSignResult.class);
		if (context instanceof BaseActivity)
			((BaseActivity) context).send(request);
	}

	/**
	 * 证券通未开户
	 */
	private static void doUnZQT(Context context) {
		showDialog(context, R.layout.dialog_unzqt);
	}

	/**
	 * 未绑定证券通
	 */
	private static void doUnBindZQT(Context context) {
		showDialog(context, R.layout.dialog_unbindzqt);
	}

	/**
	 * 证券通审核中
	 */
	private static void doZQTVerifying(Context context) {
		showDialog(context, R.layout.dialog_zqtverifing);
	}

	/**
	 * 未满七天
	 */
	private static void doNotEnoughSevenDays(Context context) {
		showDialog(context, R.layout.dialog_alreadysign_lower_seven);
	}

	/**
	 * 已签约其他投顾
	 */
	private static void doAlreadySign(Context context) {
		showDialog(context, R.layout.dialog_alreadysign);
	}

	private static void showDialog(final Context context, int resId) {
		final Dialog dialog = new Dialog(context);
		CancelListener.dialog = dialog;
		OkListener.dialog = dialog;
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(resId);
		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.width = (int) (((BaseActivity) context).getScreenW() * 0.85);
		dialogWindow.setAttributes(lp);
		TextView tvLink = (TextView) dialog.findViewById(R.id.linktext);
		if (tvLink != null){
			tvLink.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					WebViewActivity.gotoWebViewActivity(_context, "证券通投资协议", "http://itougu.jrj.com.cn/site/adviser_agreement.html");
//					_context.startActivity(new Intent(_context,ProtocolActivity.class));
				}
			});			
		}
		final TextView tvOk = (TextView) dialog.findViewById(R.id.unsign_btnok);
		final CheckBox checkBox = (CheckBox) dialog
				.findViewById(R.id.checkBox1);
		if (checkBox != null) {
			checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					// TODO Auto-generated method stub
					if (!isChecked) {
						tvOk.setTextColor(context.getResources().getColor(
								R.color.font_595959));
						tvOk.setEnabled(false);
					} else {
						tvOk.setTextColor(context.getResources().getColor(
								R.color.font_4c87c6));
						tvOk.setEnabled(true);
					}
				}
			});
		}

		tvOk.setOnClickListener(okListener);
		TextView tvCancel = (TextView) dialog
				.findViewById(R.id.unsign_btncancel);
		tvCancel.setOnClickListener(cancelListener);
		setContentText(dialog);
		dialog.show();
	}

	private static void setContentText(Dialog dialog) {
		TextView content = (TextView) dialog.findViewById(R.id.content1);
		if (content == null) {
			return;
		}
		if (changeType == ChangeType.ctUnBindSign) {
			String str = "您确认要与投顾"+USERNAME+"解约吗？解除签约后，您将无法享受该投顾的私人服务！";
			SpannableStringBuilder builder = new SpannableStringBuilder(str);
			ForegroundColorSpan span = new ForegroundColorSpan(dialog.getContext().getResources().getColor(R.color.font_4c87c6));
			builder.setSpan(span, 7, 7+USERNAME.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			content.setText(builder);
		}
	}

	public enum AccrediationType {
		/**
		 * 签约时
		 */
		atSign,
		/**
		 * 查看投资符合详情时
		 */
		atInvestGroupDetail
	}

	public static interface IAccrediatedListener {
		public void OnAccrediated(int returnCode);
	}

	private static class CancelListener implements OnClickListener {
		static Dialog dialog;

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (dialog != null)
				dialog.dismiss();
		}
	}

	private static class OkListener implements OnClickListener {
		static Dialog dialog;

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (changeType) {
			case ctUnZQT: {
				
			}
				break;
			case ctUnBindZQT: {
				
				break;
			}
			case ctVerifyingZQT: {
				
			}
			break;
			case ctUnBindSign:
				doSign(_context, 0, accrediatedListener);
				break;
			case ctCanSign:
				doSign(_context, 1, accrediatedListener);
				break;
			default:
				break;
			}
			if (dialog != null)
				dialog.dismiss();
		}
	}

	public enum ChangeType {
		ctNone, ctUnZQT, ctUnBindZQT, ctVerifyingZQT, ctAlreadySign, ctNotEnoughSevenDays, ctUnBindSign,
		ctCanSign
	}
}
