package com.gome.haoyuangong.xg;

import org.json.JSONException;
import org.json.JSONObject;

import com.gome.haoyuangong.UserInfo;
import com.gome.haoyuangong.activity.AskDetailActivity;
import com.gome.haoyuangong.activity.MessageListActivity;
import com.gome.haoyuangong.activity.TouGuMessageListActivity;
import com.gome.haoyuangong.fragments.MessageListFragment;
import com.google.gson.JsonObject;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
/**
 * 处理信鸽的消息
 * @author menghui
 *
 */
public class XgMessageDeal {
	public static void DealMessage(Context context,String custContent){
		try {
			if (TextUtils.isEmpty(custContent))
				return;
			JSONObject jsonObject = new JSONObject(custContent);
			int msgType = jsonObject.getInt("msgType");
			switch (msgType) {
			case MessageType.MESSAGE_STOCK_ALERT:
				doStockAlert(context, jsonObject);
				break;
			case MessageType.MESSAGE_ANWSER:
			case MessageType.MESSAGE_ASK:
				doAnswer(context, jsonObject);
				break;
			case MessageType.MESSAGE_ADVISER_GROUPMESSAGE:
				doGroupMessage(context);
				break;
			case MessageType.MESSAGE_SYSTEM:
				Intent intent = new Intent(context, MessageListActivity.class);
				intent.putExtra(MessageListActivity.BUNDLE_PARAM_TITLE, "系统通知");
				intent.putExtra(MessageListActivity.BUNDLE_PARAM_USERID, UserInfo.getInstance().getUserId());
				intent.putExtra(MessageListActivity.BUNDLE_PARAM_MTYPE, MessageListFragment.MSG_TYPE_SYSTEM);
				context.startActivity(intent);
				break;
			default:
				break;
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void doStockAlert(Context context,JSONObject jsonObject){
		try {
			String code = jsonObject.getString("stockCode");
//			String market = jsonObject.getString("stockMarket").equals("沪市")?"cn.sh":"cn.sz";
			String market = jsonObject.getString("stockMarket");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	private static void doAnswer(Context context,JSONObject jsonObject){
//		try {
//			Intent aIntent = new Intent(context, com.jrj.tougu.activity.AskDetailActivity_.class);
//			aIntent.putExtra(AskDetailActivity.BUNDLE_Id,jsonObject.getInt("dataId"));
//			context.startActivity(aIntent);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}	
	}
	private static void doGroupMessage(Context context){
		Intent intent = new Intent(context, TouGuMessageListActivity.class);
		intent.putExtra(MessageListActivity.BUNDLE_PARAM_USERID, UserInfo.getInstance().getUserId());
		context.startActivity(intent);
	}
}
