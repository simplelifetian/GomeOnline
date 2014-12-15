package com.gome.haoyuangong.net.result.tougu;

import com.gome.haoyuangong.net.result.BaseResultWeb;

public class CommentActionResult extends BaseResultWeb {

	private String commentId;
	private String message;
	private String appId;
	private String appItemId;
	private int returnCode;

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppItemId() {
		return appItemId;
	}

	public void setAppItemId(String appItemId) {
		this.appItemId = appItemId;
	}

	public int getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}
	
}
