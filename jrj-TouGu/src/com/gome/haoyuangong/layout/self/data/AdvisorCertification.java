package com.gome.haoyuangong.layout.self.data;

import com.gome.haoyuangong.net.result.TouguBaseResult;

public class AdvisorCertification extends TouguBaseResult {
	Data data;
	
	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public class Data{
		int status;
		int write_num;
		String error_message;
		public int getStatus() {
			return status;
		}
		public void setStatus(int status) {
			this.status = status;
		}
		public int getWrite_num() {
			return write_num;
		}
		public void setWrite_num(int write_num) {
			this.write_num = write_num;
		}
		public String getError_message() {
			return error_message;
		}
		public void setError_message(String error_message) {
			this.error_message = error_message;
		}
		
	}
}
