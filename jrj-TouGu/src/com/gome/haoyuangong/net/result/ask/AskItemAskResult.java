package com.gome.haoyuangong.net.result.ask;

import java.util.List;

import android.text.SpannableString;

import com.gome.haoyuangong.net.result.BaseResultWeb;
import com.gome.haoyuangong.net.result.ask.AskItemAnswerResult.AskItemAnswer;

public class AskItemAskResult extends BaseResultWeb {
	private DataList data;

	public DataList getData() {
		return data;
	}

	public void setData(DataList data) {
		this.data = data;
	}

	public class DataList {
		private List<AskItemAsk> list;

		public List<AskItemAsk> getList() {
			return list;
		}

		public void setList(List<AskItemAsk> list) {
			this.list = list;
		}

	}

	public class AskItemAsk {
		private int askId;
		private String content;
		private String auserId;
		private String ausername;
		private int status;
		private long ctime;
		private int type;
		private String headImages;
		private int isMyFuns;
		private int askUserType;// 提问人类别 0普通用户 1投顾

		transient private SpannableString contentSpanStr;
		
		public SpannableString getContentSpanStr() {
			return contentSpanStr;
		}
		public void setContentSpanStr(SpannableString contentSpanStr) {
			this.contentSpanStr = contentSpanStr;
		}
		public int getAskUserType() {
			return askUserType;
		}

		public void setAskUserType(int askUserType) {
			this.askUserType = askUserType;
		}

		public int getAskId() {
			return askId;
		}

		public void setAskId(int askId) {
			this.askId = askId;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getAuserId() {
			return auserId;
		}

		public void setAuserId(String auserId) {
			this.auserId = auserId;
		}

		public String getAusername() {
			return ausername;
		}

		public void setAusername(String ausername) {
			this.ausername = ausername;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public long getCtime() {
			return ctime;
		}

		public void setCtime(long ctime) {
			this.ctime = ctime;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public String getHeadImages() {
			return headImages;
		}

		public void setHeadImages(String headImages) {
			this.headImages = headImages;
		}

		public int getIsMyFuns() {
			return isMyFuns;
		}

		public void setIsMyFuns(int isMyFuns) {
			this.isMyFuns = isMyFuns;
		}

	}
}
