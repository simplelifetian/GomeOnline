package com.jrj.sharesdk;

public interface CallbackListener {
	public abstract void onSuccess();
	public abstract void onFailure();
	public abstract void onCancel();
}
