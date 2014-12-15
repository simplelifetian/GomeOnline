package com.gome.haoyuangong.net.result.live;

import java.util.ArrayList;
import java.util.List;

import android.text.SpannableStringBuilder;

import com.gome.haoyuangong.net.result.TouguBaseResult;

public class LiveOpinionBean extends TouguBaseResult {

	
	private List<LiveOpinionItem> data = new ArrayList<LiveOpinionItem>();
	
	public List<LiveOpinionItem> getData() {
		return data;
	}

	public void setData(List<LiveOpinionItem> data) {
		this.data = data;
	}

	public static class LiveOpinionItem extends LinkPicBean{
		
		private int idx;
		private String id;
		private String from_id;
		private String content;
		private String time;
		private String timeStamp;
		private String answer_image;
		
		private SpannableStringBuilder mContentSSB;
		
		public int getIdx() {
			return idx;
		}
		public void setIdx(int idx) {
			this.idx = idx;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getFrom_id() {
			return from_id;
		}
		public void setFrom_id(String from_id) {
			this.from_id = from_id;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
		
		public String getTimeStamp() {
			return timeStamp;
		}
		public void setTimeStamp(String timeStamp) {
			this.timeStamp = timeStamp;
		}
		public String getAnswer_image() {
			return answer_image;
		}
		public void setAnswer_image(String answer_image) {
			this.answer_image = answer_image;
		}
		public SpannableStringBuilder getmContentSSB() {
			return mContentSSB;
		}
		public void setmContentSSB(SpannableStringBuilder mContentSSB) {
			this.mContentSSB = mContentSSB;
		}
		
	}
	
}
