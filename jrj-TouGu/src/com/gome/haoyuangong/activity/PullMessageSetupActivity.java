package com.gome.haoyuangong.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.layout.self.BarItem;
import com.gome.haoyuangong.layout.self.Function;
import com.gome.haoyuangong.layout.self.TurnOffItem;
import com.gome.haoyuangong.layout.self.data.XinGeGetMessageType;
import com.gome.haoyuangong.net.Request;
import com.gome.haoyuangong.net.RequestHandlerListener;
import com.gome.haoyuangong.net.Request.Method;
import com.gome.haoyuangong.net.result.XinGeBaseResult;
import com.gome.haoyuangong.net.url.XinGeURL;
import com.gome.haoyuangong.net.volley.JsonRequest;
/**
 * 消息推送设置
 * @author menghui
 *
 */
public class PullMessageSetupActivity extends ListViewActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setPullLoadEnable(false);
		setPullRefreshEnable(false);
		setDividerHeight(0);
		request();
		setTitle("推送设置");
		setDividerHeight(0);
	}

	private void setMessageType(int msgType,int Switch){
		Map<String, String> params = new HashMap<String, String>();
		params.put("msgType", String.valueOf(msgType));
		params.put("deviceToken", UserInfo.getInstance().getDeivceId());
		params.put("deviceType", "3");
		params.put("switch", String.valueOf(Switch));
		params.put("appid", "tougu");
		if (!UserInfo.getInstance().isLogin())
			params.put("user_type", "3");
		else if (UserInfo.getInstance().isTougu())
			params.put("user_type", "2");
		else
			params.put("user_type", "0");

		JsonRequest<XinGeBaseResult> request = new JsonRequest<XinGeBaseResult>(
				Method.POST, XinGeURL.SETMESSAGETYPE, params,
				new RequestHandlerListener<XinGeBaseResult>(getContext()) {

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
			public void onSuccess(String id, XinGeBaseResult data) {
				// TODO Auto-generated method stub
					if (data.getRet().equals("1")) {
						
					}
				}					

			}, XinGeBaseResult.class);
		send(request);
	}
	private void request(){
		Map<String, String> params = new HashMap<String, String>();
		params.put("deviceToken", UserInfo.getInstance().getDeivceId());
		params.put("deviceType", "3");
		params.put("appid", "tougu");
		if (!UserInfo.getInstance().isLogin())
			params.put("user_type", "3");
		else if (UserInfo.getInstance().isTougu())
			params.put("user_type", "1");
		else
			params.put("user_type", "2");

		JsonRequest<XinGeGetMessageType> request = new JsonRequest<XinGeGetMessageType>(
				Method.POST, XinGeURL.GETMESSAGETYPELIST, params,
				new RequestHandlerListener<XinGeGetMessageType>(getContext()) {

					@Override
					public void onStart(Request request) {
						super.onStart(request);
						 showDialog(request);
					}

					@Override
					public void onEnd(Request request) {
						super.onEnd(request);
						 hideDialog(request);
					}

			@Override
			public void onSuccess(String id, XinGeGetMessageType data) {
				// TODO Auto-generated method stub
					if (data == null || data.getItems() == null)
						return;
					if (data.getRet().equals("0")) {
						fillData(data);
						reFresh();
					}
				}					

			}, XinGeGetMessageType.class);
		send(request);
	}
	private void fillData(XinGeGetMessageType data) {
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Function.getFitPx(getContext(), 100));
		
		TextView tv = new TextView(this);
		tv.setText("消息订阅");
		tv.setGravity(Gravity.CENTER_VERTICAL);
		tv.setTextColor(getResources().getColor(R.color.font_727272));
		p.setMargins(Function.getFitPx(this, 40), 0, 0, 0);
		layout.addView(tv,p);

		p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Function.getFitPx(getContext(), 140));
		
		
		for(int i=0;i<data.getItems().size();i++){
			TurnOffItem item = createBarItem(data.getItems().get(i).getMsg());	
			item.setTag(Integer.parseInt(data.getItems().get(i).getType()));
			if (i<data.getItems().size()-1)
				item.setDrawBottomLine(true);
			if (data.getItems().get(i).getSwitchOpen().equals("0"))
				item.setClose(false);
			else
				item.setClose(true);
//			p.setMargins(0, 0, 0, 1);
			layout.addView(item,p);
		}
		addItem(layout);
	}
	
	private TurnOffItem createBarItem(String title){		
		final TurnOffItem item = new TurnOffItem(getContext());
		item.setRightArrowVisible(View.GONE);
		item.setTitleFontSize(50);
//		item.addRightImage(R.drawable.icon_check_checked,100,false);
		item.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					if (v instanceof TurnOffItem){
						setMessageType(Integer.parseInt(((TurnOffItem)v).getTag().toString()),((TurnOffItem)v).getClose()?0:1);
						((TurnOffItem)v).setClose(!((TurnOffItem)v).getClose());
						
				}
			}
		});
		item.setTitle(title);
		return item;
	}
}
