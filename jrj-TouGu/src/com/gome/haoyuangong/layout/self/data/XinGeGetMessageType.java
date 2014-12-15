package com.gome.haoyuangong.layout.self.data;

import java.util.ArrayList;

import com.gome.haoyuangong.net.result.XinGeBaseResult;

public class XinGeGetMessageType extends XinGeBaseResult {

	ArrayList<MessageTypeItem> items;
	
	public ArrayList<MessageTypeItem> getItems() {
		return items;
	}

	public void setItems(ArrayList<MessageTypeItem> items) {
		this.items = items;
	}

	public class MessageTypeItem{
		String msg;
		String switchOpen;
		String type;
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
		public String getSwitchOpen() {
			return switchOpen;
		}
		public void setSwitchOpen(String switchOpen) {
			this.switchOpen = switchOpen;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		
	}
}
