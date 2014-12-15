package com.gome.haoyuangong.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.R;

public class AboutActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		TextView tv = (TextView)findViewById(R.id.textView1);
		if (tv != null)
			tv.setText("爱投顾 v"+getVersion());
		setTitle("关于");
		final TextView phoneView = (TextView)findViewById(R.id.textView7);
		if (phoneView != null){
			phoneView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
		            //用intent启动拨打电话  
		            Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phoneView.getText().toString()));  
		            startActivity(intent); 
				}
			});
		}
	}
	private String getVersion(){
        PackageInfo pkg;
        try {
            pkg = getPackageManager().getPackageInfo(getApplication().getPackageName(), 0);
            String versionName = pkg.versionName; 
            return versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        } 
     }

}
