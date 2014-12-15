package com.gome.haoyuangong.net.result.tougu;

import com.gome.haoyuangong.net.result.BaseResultWeb;

public class OptionLimitResult extends BaseResultWeb {

	private Data data = new Data();
	
	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public static class Data{
		private boolean ifPraise;
		private boolean ifSign;
		private long likeTotal;
		public boolean isIfPraise() {
			return ifPraise;
		}
		public void setIfPraise(boolean ifPraise) {
			this.ifPraise = ifPraise;
		}
		public boolean isIfSign() {
			return ifSign;
		}
		public void setIfSign(boolean ifSign) {
			this.ifSign = ifSign;
		}
		public long getLikeTotal() {
			return likeTotal;
		}
		public void setLikeTotal(long likeTotal) {
			this.likeTotal = likeTotal;
		}
	}
}
