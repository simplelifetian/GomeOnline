package com.gome.haoyuangong.mediarecorder;

import java.io.File;
import java.io.IOException;

import com.gome.haoyuangong.log.Logger;
import com.gome.haoyuangong.utils.StringUtils;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;

public class SoundPlayer  implements OnCompletionListener, OnErrorListener{
	MediaPlayer mPlayer;
	public void startPlay(String path){
		if(StringUtils.isEmpty(path)){
			Logger.error("SoundPlayer", "file path is null");
			return;
		}
		File f = new File(path);
		if(!f.exists()) return;
		mPlayer = new MediaPlayer();
		try {
			mPlayer.setDataSource(path);
			mPlayer.setOnCompletionListener(this);
			mPlayer.setOnErrorListener(this);
			mPlayer.prepare();
			mPlayer.seekTo(0);
			mPlayer.start();
			if(listener!=null){
				listener.onPaly();
			}
		} catch (IllegalArgumentException e) {
			mPlayer = null;
			if(listener!=null){
				listener.onError();
			}
			return;
		} catch (IOException e) {
			mPlayer = null;
			if(listener!=null){
				listener.onError();
			}
			return;
		}
	}
	public void stopPlay(){
		if(mPlayer!=null){
			if(mPlayer.isPlaying()){
				mPlayer.stop();
			}
		}
		if(listener!=null){
			listener.onFinish();
		}
	}
	@Override
	public void onCompletion(MediaPlayer mp) {
		if(listener!=null){
			listener.onFinish();
		}
	}
	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		if(listener!=null){
			listener.onError();
		}
		return false;
	}
	private OnSoundPlayListner listener;
	
	public OnSoundPlayListner getListener() {
		return listener;
	}
	public void setListener(OnSoundPlayListner listener) {
		this.listener = listener;
	}

	
	public interface OnSoundPlayListner{
		public void onPaly();
		public void onFinish();
		public void onError();
	}
}
