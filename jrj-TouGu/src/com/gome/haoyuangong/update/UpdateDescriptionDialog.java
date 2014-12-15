
package com.gome.haoyuangong.update;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gome.haoyuangong.MyApplication;
import com.gome.haoyuangong.R;

public class UpdateDescriptionDialog extends Activity implements OnClickListener {

    private static final String TAG = UpdateDescriptionDialog.class.getName();

    public static final String KEY_UPDATE_INFO = "update_info";
    private UpdateInfo mUpdateInfo;

    private TextView title;
    private TextView updateContent;
    private TextView cancel;
    private TextView confirm;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.startActivity(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
//        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_update_layout);
        
        

        mUpdateInfo = (UpdateInfo) getIntent().getSerializableExtra(KEY_UPDATE_INFO);
        if (mUpdateInfo == null) {
            finish();
            return;
        }
        initView();

    }

    private void initView() {
    	
    	cancel = (TextView)findViewById(R.id.negativeButton);
    	cancel.setOnClickListener(this);
    	confirm = (TextView)findViewById(R.id.positiveButton);
    	confirm.setOnClickListener(this);

        title = (TextView)findViewById(R.id.title);
        updateContent = (TextView) findViewById(R.id.message);
        updateContent.setText(mUpdateInfo.description);
        
        if(mUpdateInfo.force){
        	
        	cancel.setText("退出");
        	confirm.setText("确定");
        }else{
        	cancel.setText("稍后再说");
        	confirm.setText("立即升级");
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.destroyActivity(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.positiveButton:
                onOk();
                finish();
                break;
            case R.id.negativeButton:
                onCancel();
                break;
            default:
                break;
        }
    }

    private void onCancel() {
        UpdateManager.getInstance().onCancelClick(mUpdateInfo);
        finish();
    }

    private void onOk() {
        UpdateManager.getInstance().onOkClick(mUpdateInfo);
        finish();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onCancel();
        }
        return super.onKeyUp(keyCode, event);
    }

}
