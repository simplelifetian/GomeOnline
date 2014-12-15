package com.gome.haoyuangong.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsMessage;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gome.haoyuangong.log.Logger;
import com.gome.haoyuangong.presenter.IRegistPresenter;
import com.gome.haoyuangong.utils.StringUtils;
import com.gome.haoyuangong.R;

@EActivity(R.layout.regist_2)
public class Regist2Activity extends BaseActivity {
	public static final String BUNDLE_PHONE="phone";
	public static final int VCODE_TIME=60;
	private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
	public static final String JRJ_VCODE_PHONE = "10690158";
	public static final String JRJ_VCODE_TEMPLATE = "【金融界】(\\d+)";
	@ViewById(R.id.regist2_text_tel_num)
	protected TextView mTvPhoneNum;
	@ViewById(R.id.check_num)
	protected EditText mEditText;
	@ViewById(R.id.get_vcode)
	protected TextView mGetVCodeBtn;
	@Extra(BUNDLE_PHONE)
	String phoneNum;
	private SMSReceiver1 mSMSReceiver;
	private TimerTask task;
	private Timer timer = new Timer();  
	private int time = 10;
	
	IRegistPresenter check = new IRegistPresenter(this) {
		public void onVerifyCode (boolean isOk) {
//			if (isOk) {
//				Intent i = new Intent(Regist2Activity.this, com.jrj.tougu.activity.Regist3Activity_.class);
//				i.putExtra(Regist2Activity.BUNDLE_PHONE,phoneNum);
//				startActivity(i);
//				finish();
//			}else{
//				mGetVCodeBtn.setEnabled(true);
//				mGetVCodeBtn.setText("获取验证码");
//				task.cancel();
//			}
		}
		public void onGetCodeFailure() {
			mGetVCodeBtn.setEnabled(true);
			mGetVCodeBtn.setText("重新获取");
			task.cancel();
		};
	};
	protected void onResume() {
		super.onResume();
//		if(mSMSReceiver==null){
//			mSMSReceiver = new SMSReceiver1();
//		}
//		IntentFilter filter = new IntentFilter(ACTION);
//		filter.setPriority(10000);
//		registerReceiver(mSMSReceiver, filter);
		
		
//		ContentObserver co = new SmsReceiver(new Handler(), 
//				Regist2Activity.this); 
//		this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, co); 
	};
	@Override
	protected void onStop() {
		super.onStop();
//		if(mSMSReceiver!=null){
//			unregisterReceiver(mSMSReceiver);
//		}
	}
	@AfterViews
	protected void init() {
		setTitle(R.string.regist);
		if(phoneNum!=null){
			mTvPhoneNum.setText(phoneNum);
		}else{
			Logger.error("regist2Activity", "手机号不能为空");
			finish();
		}
		getVCodeClicked();
	}

	@Click
	void submitClicked() {
		String vcode = mEditText.getText().toString();
		check.verifyCode(vcode, phoneNum);
	}
	
	@Click(R.id.get_vcode)
	void getVCodeClicked() {
		check.getVCode(phoneNum);
		mGetVCodeBtn.setEnabled(false);
		time = VCODE_TIME;
		task = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() { // UI thread
					@Override
					public void run() {
						if (time <= 0) {
							mGetVCodeBtn.setEnabled(true);
							mGetVCodeBtn.setText("重新获取");
							task.cancel();
						} else {
							mGetVCodeBtn.setText(time+"s");
						}
						time--;
					}
				});
			}
		};
		timer.schedule(task, 0, 1000);
	}
	private class SMSReceiver1 extends BroadcastReceiver {
		private String TAG = "smsreceiveandmask";

		@Override
		public void onReceive(Context context, Intent intent) {
			// 第一步、获取短信的内容和发件人
			Logger.info(TAG, "get sms");
			StringBuilder body = new StringBuilder();// 短信内容
			StringBuilder number = new StringBuilder();// 短信发件人
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				Object[] _pdus = (Object[]) bundle.get("pdus");
				SmsMessage[] message = new SmsMessage[_pdus.length];
				for (int i = 0; i < _pdus.length; i++) {
					message[i] = SmsMessage.createFromPdu((byte[]) _pdus[i]);
				}
				for (SmsMessage currentMessage : message) {
					body.append(currentMessage.getDisplayMessageBody());
					number.append(currentMessage.getDisplayOriginatingAddress());
				}
				String smsBody = body.toString();
				String smsNumber = number.toString();
				if (smsNumber.contains("+86")) {
					smsNumber = smsNumber.substring(3);
				}
				if (smsNumber.equals(JRJ_VCODE_PHONE)) {
					Pattern p = Pattern.compile(JRJ_VCODE_TEMPLATE);
					Matcher m = p.matcher(smsBody);
					if (m.find()) {
						String code = m.group(1);
						if (!StringUtils.isBlank(code)) {
							mEditText.setText(code);
						}
					}
				}
			}
		}
	}
	
	public class SmsReceiver extends ContentObserver {
    /**
     * Activity对象
     */ 
    private Activity activity; 
    private List<SmsInfo> infos; 
 
    public SmsReceiver(Handler handler, Activity activity) { 
        super(handler); 
        this.activity = activity; 
    } 
 
    @Override 
    public void onChange(boolean selfChange) { 
        Uri uri = Uri.parse("content://sms/inbox");// 设置一个uri来查看各种类别短信内容 
        SmsContent smscontent = new SmsContent(activity, uri); 
        infos = smscontent.getSmsInfo(); 
        System.out.println(infos.get(1).getSmsbody()); 
        super.onChange(selfChange); 
    } 
} 

	public class SmsInfo { 
    /**
     * 短信内容
     */ 
    private String smsbody; 
    /**
     * 发送短信的电话号码
     */ 
    private String phoneNumber; 
    /**
     * 发送短信的日期和时间
     */ 
    private String date; 
    /**
     * 发送短信人的姓名
     */ 
    private String name; 
    /**
     * 短信类型1是接收到的，2是已发出
     */ 
    private String type; 
 
    public String getSmsbody() { 
        return smsbody; 
    } 
 
    public void setSmsbody(String smsbody) { 
        this.smsbody = smsbody; 
    } 
 
    public String getPhoneNumber() { 
        return phoneNumber; 
    } 
 
    public void setPhoneNumber(String phoneNumber) { 
        this.phoneNumber = phoneNumber; 
    } 
 
    public String getDate() { 
        return date; 
    } 
 
    public void setDate(String date) { 
        this.date = date; 
    } 
 
    public String getName() { 
        return name; 
    } 
 
    public void setName(String name) { 
        this.name = name; 
    } 
 
    public String getType() { 
        return type; 
    } 
 
    public void setType(String type) { 
        this.type = type; 
    } 
} 
	public class SmsContent { 
    private Activity activity;//这里有个activity对象，不知道为啥以前好像不要，现在就要了。自己试试吧。 
    private Uri uri; 
    List<SmsInfo> infos; 
 
    public SmsContent(Activity activity, Uri uri) { 
        infos = new ArrayList<SmsInfo>(); 
        this.activity = activity; 
        this.uri = uri; 
    } 
 
    /**
     * Role:获取短信的各种信息 <BR>
     * Date:2012-3-19 <BR>
     * 
     * @author CODYY)peijiangping
     */ 
    public List<SmsInfo> getSmsInfo() { 
        String[] projection = new String[] { "_id", "address", "person", 
                "body", "date", "type" }; 
        Cursor cusor = activity.managedQuery(uri, projection, null, null, 
                "date desc"); 
        int nameColumn = cusor.getColumnIndex("person"); 
        int phoneNumberColumn = cusor.getColumnIndex("address"); 
        int smsbodyColumn = cusor.getColumnIndex("body"); 
        int dateColumn = cusor.getColumnIndex("date"); 
        int typeColumn = cusor.getColumnIndex("type"); 
        if (cusor != null) { 
            while (cusor.moveToNext()) { 
                SmsInfo smsinfo = new SmsInfo(); 
                smsinfo.setName(cusor.getString(nameColumn)); 
                smsinfo.setDate(cusor.getString(dateColumn)); 
                smsinfo.setPhoneNumber(cusor.getString(phoneNumberColumn)); 
                smsinfo.setSmsbody(cusor.getString(smsbodyColumn)); 
                smsinfo.setType(cusor.getString(typeColumn)); 
                infos.add(smsinfo); 
            } 
            cusor.close(); 
        } 
        return infos; 
    } 
} 
        
}
