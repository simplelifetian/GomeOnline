/**
 * 
 */
package com.gome.haoyuangong.spashad;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * 
 */
public class ADData {

	private int retCode;
	private String imgUrl;
	private int imgVersion;
	private int status;
	private long pubDate;
	private int clickType;
	private String clickUrl;
	private String locaPath;

	public int getRetCode() {
		return retCode;
	}

	public void setRetCode(int retCode) {
		this.retCode = retCode;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public int getImgVersion() {
		return imgVersion;
	}

	public void setImgVersion(int imgVersion) {
		this.imgVersion = imgVersion;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getClickType() {
		return clickType;
	}

	public void setClickType(int clickType) {
		this.clickType = clickType;
	}

	public String getClickUrl() {
		return clickUrl;
	}

	public void setClickUrl(String clickUrl) {
		this.clickUrl = clickUrl;
	}
	
	public String getLocaPath() {
		return locaPath;
	}

	public void setLocaPath(String locaPath) {
		this.locaPath = locaPath;
	}

	public long getPubDate() {
		return pubDate;
	}

	public void setPubDate(long pubDate) {
		this.pubDate = pubDate;
	}

	public void writeObject(DataOutputStream out) throws IOException {
		out.writeInt(retCode);
		out.writeUTF(imgUrl);
		out.writeInt(imgVersion);
		out.writeLong(pubDate);
		out.writeInt(status);
		out.writeInt(clickType);
		if(clickUrl != null){
			out.writeUTF(clickUrl);
		}else{
			out.writeUTF("null");
		}
	}

	public void readObject(DataInputStream in) throws IOException {
		retCode = in.readInt();
		imgUrl = in.readUTF();
		imgVersion = in.readInt();
		pubDate = in.readLong();
		status = in.readInt();
		clickType = in.readInt();
		clickUrl = in.readUTF();
	}

	public String getImageFileName() {
		return "splash_" + imgVersion + "_" + status + "_.pic";
	}

	public String getInfoFileName() {
		return "splash_" + imgVersion + "_" + status + "_.info";
	}
}
