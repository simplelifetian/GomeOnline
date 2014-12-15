package com.gome.haoyuangong.update;

import com.gome.haoyuangong.net.result.BaseResultWeb;

public class UpdateInfoResult extends BaseResultWeb {

	private UpdateInfo data = new UpdateInfo();
	
	public UpdateInfo getData() {
		return data;
	}

	public void setData(UpdateInfo data) {
		this.data = data;
	}

	public static class UpdateInfo{
		private int updateType;
		private String versionId;
		private String description;
		private String packageUrl;
		private long packageSize;
		private String packageMd5;
		private String diffPackageUrl;
		private long diffPackageSize;
		private String diffPackageMd5;
		private int downloadType;

		public int getUpdateType() {
			return updateType;
		}

		public void setUpdateType(int updateType) {
			this.updateType = updateType;
		}

		public String getVersionId() {
			return versionId;
		}

		public void setVersionId(String versionId) {
			this.versionId = versionId;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getPackageUrl() {
			return packageUrl;
		}

		public void setPackageUrl(String packageUrl) {
			this.packageUrl = packageUrl;
		}

		public long getPackageSize() {
			return packageSize;
		}

		public void setPackageSize(long packageSize) {
			this.packageSize = packageSize;
		}

		public String getPackageMd5() {
			return packageMd5;
		}

		public void setPackageMd5(String packageMd5) {
			this.packageMd5 = packageMd5;
		}

		public String getDiffPackageUrl() {
			return diffPackageUrl;
		}

		public void setDiffPackageUrl(String diffPackageUrl) {
			this.diffPackageUrl = diffPackageUrl;
		}

		public long getDiffPackageSize() {
			return diffPackageSize;
		}

		public void setDiffPackageSize(long diffPackageSize) {
			this.diffPackageSize = diffPackageSize;
		}

		public String getDiffPackageMd5() {
			return diffPackageMd5;
		}

		public void setDiffPackageMd5(String diffPackageMd5) {
			this.diffPackageMd5 = diffPackageMd5;
		}

		public int getDownloadType() {
			return downloadType;
		}

		public void setDownloadType(int downloadType) {
			this.downloadType = downloadType;
		}
	}

}
