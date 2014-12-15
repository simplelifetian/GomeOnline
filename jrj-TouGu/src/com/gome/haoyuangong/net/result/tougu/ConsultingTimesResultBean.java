package com.gome.haoyuangong.net.result.tougu;

import com.gome.haoyuangong.net.result.TouguBaseResult;

public class ConsultingTimesResultBean extends TouguBaseResult {

	private Data data = new Data();
	
	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public static class Data{
		
		private int times;

		public int getTimes() {
			return times;
		}

		public void setTimes(int times) {
			this.times = times;
		}

	}
}
