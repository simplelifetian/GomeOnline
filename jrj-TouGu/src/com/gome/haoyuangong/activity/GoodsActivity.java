package com.gome.haoyuangong.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.fragments.GoodsISellFragment;
import com.gome.haoyuangong.net.Request;

@SuppressLint("NewApi")
public class GoodsActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods);
		Button btnISell = (Button) findViewById(R.id.goods_i_sell);
		Button btnHotRec = (Button) findViewById(R.id.goods_hot_rec);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.goods_i_sell:
			GoodsISellFragment fragment1 = new GoodsISellFragment();
			FragmentManager fragManager = getFragmentManager();
			FragmentTransaction transaction = fragManager.beginTransaction();
			transaction.replace(R.id.goods_isell_fragment, fragment1);
			transaction.commit();
			break;		
			default:
				break;
		}
	}
	
}
