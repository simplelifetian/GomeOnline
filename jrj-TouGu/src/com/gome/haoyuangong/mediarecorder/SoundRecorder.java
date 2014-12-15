/*
 * Copyright (c) 2010-2011, The MiCode Open Source Community (www.micode.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gome.haoyuangong.mediarecorder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import com.gome.haoyuangong.R;
import com.gome.haoyuangong.log.Logger;

public class SoundRecorder implements Button.OnClickListener, Recorder.OnStateChangedListener {
	private static final String TAG = "SoundRecorder";

	private static final String RECORDER_STATE_KEY = "recorder_state";

	private static final String SAMPLE_INTERRUPTED_KEY = "sample_interrupted";

	private static final String MAX_FILE_SIZE_KEY = "max_file_size";

	private static final String AUDIO_3GPP = "audio/3gpp";

	private static final String AUDIO_AMR = "audio/amr";

	private static final String AUDIO_ANY = "audio/*";

	private static final String ANY_ANY = "*/*";

	private static final String FILE_EXTENSION_AMR = ".amr";

	private static final String FILE_EXTENSION_3GPP = ".3gpp";

	public static final int BITRATE_AMR = 2 * 1024 * 8; // bits/sec

	public static final int BITRATE_3GPP = 20 * 1024 * 8; // bits/sec
	
	public static final long MAX_SOUND_SIZE = 120000;//最长时间120000毫秒

//	private static final int SEEK_BAR_MAX = 10000;
	


	private String mRequestedType = AUDIO_ANY;

	private boolean mCanRequestChanged = false;

	private Recorder mRecorder;

	private RecorderReceiver mReceiver;

	private boolean mSampleInterrupted = false;

	private boolean mShowFinishButton = false;

	private String mErrorUiMessage = null; // Some error messages are displayed
																				 // in the UI, not a dialog. This
																				 // happens when a recording
																				 // is interrupted for some reason.

	private long mMaxFileSize = -1; // can be specified in the intent

	private RemainingTimeCalculator mRemainingTimeCalculator;
	private SoundPool mSoundPool;


	private HashSet<String> mSavedRecord;

	private long mLastClickTime;

	private final Handler mHandler = new Handler();

	private Runnable mUpdateTimer = new Runnable() {
		public void run() {
			if (!mStopUiUpdate) {
				updateTimerView();
			}
		}
	};

	private Runnable mUpdateSeekBar = new Runnable() {
		@Override
		public void run() {
			if (!mStopUiUpdate) {
				updateSeekBar();
			}
		}
	};

	private Runnable mUpdateVUMetur = new Runnable() {
		@Override
		public void run() {
			if (!mStopUiUpdate) {
				updateVUMeterView();
			}
		}
	};

	public interface OnRecorderListener{
		public void onIdle();
		public void onStartRecording();
		public void onUpdateVUMetur(int cur,int max);
		public void onUpdateTime(int second);
		public void onFinish();
		public void onPlaying();
		public void onPlayProgress(int progress);
		public void onError(String error);
	}
	
	private ImageButton mNewButton;

	private ImageButton mFinishButton;

	private ImageButton mRecordButton;

	private ImageButton mStopButton;

	private ImageButton mPlayButton;

	private ImageButton mPauseButton;

	private ImageButton mDeleteButton;

	private LinearLayout mTimerLayout;

	private LinearLayout mVUMeterLayout;

	private LinearLayout mSeekBarLayout;

	private TextView mStartTime;

	private TextView mTotalTime;

	private SeekBar mPlaySeekBar;

	private BroadcastReceiver mSDCardMountEventReceiver = null;


	private boolean mStopUiUpdate;

	private Context ctx;
	
	private String fileName;
	private long maxTimeLenth = MAX_SOUND_SIZE+500;//最大文件长度
	private OnRecorderListener mOnRecorderListener;

	public OnRecorderListener getmOnRecorderListener() {
		return mOnRecorderListener;
	}

	public void setmOnRecorderListener(OnRecorderListener mOnRecorderListener) {
		this.mOnRecorderListener = mOnRecorderListener;
	}

	public SoundRecorder(Context ctx, Bundle icycle, Intent i) {
		this.ctx = ctx;
		initInternalState(i);

		mRecorder = new Recorder(ctx);
		mRecorder.setOnStateChangedListener(this);
		mReceiver = new RecorderReceiver();
		mRemainingTimeCalculator = new RemainingTimeCalculator();
		mSavedRecord = new HashSet<String>();

		initResourceRefs();
		fileName = "temp"+System.currentTimeMillis();
		registerExternalStorageListener();
		if (icycle != null) {
			Bundle recorderState = icycle.getBundle(RECORDER_STATE_KEY);
			if (recorderState != null) {
				mRecorder.restoreState(recorderState);
				mSampleInterrupted = recorderState.getBoolean(SAMPLE_INTERRUPTED_KEY, false);
				mMaxFileSize = recorderState.getLong(MAX_FILE_SIZE_KEY, -1);
			}
		}

		((Activity)ctx).setVolumeControlStream(AudioManager.STREAM_MUSIC);

		if (mShowFinishButton) {
			// reset state if it is a recording request
			mRecorder.reset();
		}
	}

//	@Override
//	protected void onNewIntent(Intent intent) {
//		super.onNewIntent(intent);
//
//		boolean preShowFinishButton = mShowFinishButton;
//		initInternalState(intent);
//
//		if (mShowFinishButton || preShowFinishButton != mShowFinishButton) {
//			// reset state if it is a recording request or state is changed
//			mRecorder.reset();
//			resetFileNameEditText();
//		}
//	}

	private void initInternalState(Intent i) {
		mRequestedType = AUDIO_ANY;
		mShowFinishButton = false;
		if (i != null) {
			String s = i.getType();
			if (AUDIO_AMR.equals(s) || AUDIO_3GPP.equals(s) || AUDIO_ANY.equals(s) || ANY_ANY.equals(s)) {
				mRequestedType = s;
				mShowFinishButton = true;
			} else if (s != null) {
				// we only support amr and 3gpp formats right now
//				setResult(RESULT_CANCELED);
//				finish();
				return;
			}

			final String EXTRA_MAX_BYTES = android.provider.MediaStore.Audio.Media.EXTRA_MAX_BYTES;
			mMaxFileSize = i.getLongExtra(EXTRA_MAX_BYTES, -1);
		}
		mRequestedType = AUDIO_AMR;
//		if (AUDIO_ANY.equals(mRequestedType)) {
//			mRequestedType = SoundRecorderPreferenceActivity.getRecordType(this);
//		} else if (ANY_ANY.equals(mRequestedType)) {
//			mRequestedType = AUDIO_3GPP;
//		}
	}

	public void onConfigurationChanged(Configuration newConfig) {
		initResourceRefs();
		updateUi(false);
	}

	protected void onSaveInstanceState(Bundle outState) {

		if (mRecorder.sampleLength() == 0)
			return;

		Bundle recorderState = new Bundle();

		if (mRecorder.state() != Recorder.RECORDING_STATE) {
			mRecorder.saveState(recorderState);
		}
		recorderState.putBoolean(SAMPLE_INTERRUPTED_KEY, mSampleInterrupted);
		recorderState.putLong(MAX_FILE_SIZE_KEY, mMaxFileSize);

		outState.putBundle(RECORDER_STATE_KEY, recorderState);
	}

	/*
	 * Whenever the UI is re-created (due f.ex. to orientation change) we have to
	 * reinitialize references to the views.
	 */
	private void initResourceRefs() {
		mLastClickTime = 0;
	}


	private void stopRecordPlayingAnimation() {
	}




	/*
	 * Make sure we're not recording music playing in the background, ask the
	 * MediaPlaybackService to pause playback.
	 */
	private void stopAudioPlayback() {
		// Shamelessly copied from MediaPlaybackService.java, which
		// should be public, but isn't.
		Intent i = new Intent("com.android.music.musicservicecommand");
		i.putExtra("command", "pause");

		ctx.sendBroadcast(i);
	}

	/*
	 * Handle the buttons.
	 */
	public void onClick(View button) {
//		if (System.currentTimeMillis() - mLastClickTime < 300) {
//			// in order to avoid user click bottom too quickly
//			return;
//		}
//
//		if (!button.isEnabled())
//			return;
//
//		if (button.getId() == mLastButtonId && button.getId() != R.id.newButton) {
//			// as the recorder state is async with the UI
//			// we need to avoid launching the duplicated action
//			return;
//		}
//
//		if (button.getId() == R.id.stopButton && System.currentTimeMillis() - mLastClickTime < 1500) {
//			// it seems that the media recorder is not robust enough
//			// sometime it crashes when stop recording right after starting
//			return;
//		}
//
//		mLastClickTime = System.currentTimeMillis();
//		mLastButtonId = button.getId();
//
//		switch (button.getId()) {
//		case R.id.newButton:
//			mFileNameEditText.clearFocus();
//			saveSample();
//			mRecorder.reset();
//			resetFileNameEditText();
//			break;
//		case R.id.recordButton:
//			showOverwriteConfirmDialogIfConflicts();
//			break;
//		case R.id.stopButton:
//			mRecorder.stop();
//			break;
//		case R.id.playButton:
//			mRecorder.startPlayback(mRecorder.playProgress());
//			break;
//		case R.id.pauseButton:
//			mRecorder.pausePlayback();
//			break;
//		case R.id.finishButton:
//			mRecorder.stop();
//			saveSample();
//			finish();
//			break;
//		case R.id.deleteButton:
//			showDeleteConfirmDialog();
//			break;
//		}
	}
	public void playRecording(){
		if(mRecorder.sampleLength()>=0){
			mRecorder.startPlayback(mRecorder.playProgress());
		}
	}
	public void startRecording() {
		mRemainingTimeCalculator.reset();
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			mSampleInterrupted = true;
			mErrorUiMessage = ctx.getResources().getString(R.string.insert_sd_card);
			updateUi(false);
		} else if (!mRemainingTimeCalculator.diskSpaceAvailable()) {
			mSampleInterrupted = true;
			mErrorUiMessage = ctx.getResources().getString(R.string.storage_is_full);
			updateUi(false);
		} else {
			stopAudioPlayback();
			boolean isHighQuality = false;
			if (AUDIO_AMR.equals(mRequestedType)) {
				mRemainingTimeCalculator.setBitRate(BITRATE_AMR);
				int outputfileformat = isHighQuality ? MediaRecorder.OutputFormat.AMR_WB : MediaRecorder.OutputFormat.AMR_NB;
				mRecorder.startRecording(outputfileformat, fileName, FILE_EXTENSION_AMR, isHighQuality, mMaxFileSize);
			} else if (AUDIO_3GPP.equals(mRequestedType)) {
				// HACKME: for HD2, there is an issue with high quality 3gpp
				// use low quality instead
				if (Build.MODEL.equals("HTC HD2")) {
					isHighQuality = false;
				}

				mRemainingTimeCalculator.setBitRate(BITRATE_3GPP);
				mRecorder.startRecording(MediaRecorder.OutputFormat.THREE_GPP, fileName, FILE_EXTENSION_3GPP, isHighQuality, mMaxFileSize);
			} else {
				throw new IllegalArgumentException("Invalid output file type requested");
			}
			if (mMaxFileSize != -1) {
				mRemainingTimeCalculator.setFileSizeLimit(mRecorder.sampleFile(), mMaxFileSize);
			}
		}
	}
	
	public void stopRecord(){
		if(mRecorder.state()==Recorder.RECORDING_STATE){
			mRecorder.stop();
		}else{
			RecorderService.stopRecording(ctx);
		}
	}
	public void stopPlaying(){
		if(mRecorder.state()==Recorder.PLAYING_STATE){
			mRecorder.stop();
		}
	}

	public void deleteRecord(){
		if(mRecorder.state()==Recorder.IDLE_STATE&&mRecorder.sampleLength()>0){
			mRecorder.delete();
			mOnRecorderListener.onIdle();
		}
	}
	public int getTimeLength(){
		if(mRecorder!=null&&mRecorder.state()==Recorder.IDLE_STATE){
			return mRecorder.sampleLength();
		}
		return 0;
	}
	public long getTimeLengthMiss(){
		if(mRecorder!=null&&mRecorder.state()==Recorder.IDLE_STATE){
			return mRecorder.sampleLengthMill();
		}
		return 0;
	}
	/*
	 * Handle the "back" hardware key.
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			switch (mRecorder.state()) {
			case Recorder.IDLE_STATE:
			case Recorder.PLAYING_PAUSED_STATE:
				if (mRecorder.sampleLength() > 0)
					saveSample();
//				finish();
				break;
			case Recorder.PLAYING_STATE:
				mRecorder.stop();
				saveSample();
				break;
			case Recorder.RECORDING_STATE:
				if (mShowFinishButton) {
					mRecorder.clear();
				} else {
//					finish();
				}
				break;
			}
			return true;
		} else {
			return ((Activity)ctx).onKeyDown(keyCode, event);///
		}
	}
	
	
	public void onResume() {
		String type = null;
//		if (mCanRequestChanged && !TextUtils.equals(type, mRequestedType)) {
//			saveSample();
//			mRecorder.reset();
//			mRequestedType = type;
//		}
		mCanRequestChanged = false;

		if (!mRecorder.syncStateWithService()) {
			mRecorder.reset();
		}

		if (mRecorder.state() == Recorder.RECORDING_STATE) {
			String preExtension = AUDIO_AMR.equals(mRequestedType) ? FILE_EXTENSION_AMR : FILE_EXTENSION_3GPP;
			if (!mRecorder.sampleFile().getName().endsWith(preExtension)) {
				// the extension is changed need to stop current recording
				mRecorder.reset();
			} else {
				// restore state
				if (!mShowFinishButton) {
//					String fileName = mRecorder.sampleFile().getName().replace(preExtension, "");
//					mFileNameEditText.setText(fileName);
				}

				if (AUDIO_AMR.equals(mRequestedType)) {
					mRemainingTimeCalculator.setBitRate(BITRATE_AMR);
				} else if (AUDIO_3GPP.equals(mRequestedType)) {
					mRemainingTimeCalculator.setBitRate(BITRATE_3GPP);
				}
			}
		} else {
			File file = mRecorder.sampleFile();
			if (file != null && !file.exists()) {
				mRecorder.reset();
			}
		}

		IntentFilter filter = new IntentFilter();
		filter.addAction(RecorderService.RECORDER_SERVICE_BROADCAST_NAME);
		ctx.registerReceiver(mReceiver, filter);

		mStopUiUpdate = false;
//		updateUi(true);

		if (RecorderService.isRecording()) {
			Intent intent = new Intent(ctx, RecorderService.class);
			intent.putExtra(RecorderService.ACTION_NAME, RecorderService.ACTION_DISABLE_MONITOR_REMAIN_TIME);
			ctx.startService(intent);
		}
	}

	public void onPause() {
		if (mRecorder.state() == Recorder.RECORDING_STATE) {
			stopRecord();
		}else if(mRecorder.state()==Recorder.PLAYING_STATE){
			stopPlaying();
		}

		if (mReceiver != null) {
			ctx.unregisterReceiver(mReceiver);
		}

		mCanRequestChanged = true;
		mStopUiUpdate = true;

		if (RecorderService.isRecording()) {
			Intent intent = new Intent(ctx, RecorderService.class);
			intent.putExtra(RecorderService.ACTION_NAME, RecorderService.ACTION_ENABLE_MONITOR_REMAIN_TIME);
			ctx.startService(intent);
		}

	}

	
	/*
	 * If we have just recorded a sample, this adds it to the media data base and
	 * sets the result to the sample's URI.
	 */
	private void saveSample() {
		if (mRecorder.sampleLength() == 0)
			return;
		if (!mSavedRecord.contains(mRecorder.sampleFile().getAbsolutePath())) {
			Uri uri = null;
			try {
				uri = this.addToMediaDB(mRecorder.sampleFile());
			} catch (UnsupportedOperationException ex) { // Database
				// manipulation
				// failure
				return;
			}
			if (uri == null) {
				return;
			}
			mSavedRecord.add(mRecorder.sampleFile().getAbsolutePath());
//			setResult(RESULT_OK, new Intent().setData(uri));
		}
	}

	private void showDeleteConfirmDialog() {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ctx);
		dialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
//		dialogBuilder.setTitle(R.string.delete_dialog_title);
		dialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mRecorder.delete();
			}
		});
		dialogBuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		dialogBuilder.show();
	}

	private void showOverwriteConfirmDialogIfConflicts() {
//		String fileName = mFileNameEditText.getText().toString() + (AUDIO_AMR.equals(mRequestedType) ? FILE_EXTENSION_AMR : FILE_EXTENSION_3GPP);
//
//		if (mRecorder.isRecordExisted(fileName) && !mShowFinishButton) {
//			// file already existed and it's not a recording request from other
//			// app
//			AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//			dialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
//			dialogBuilder.setTitle(getString(R.string.overwrite_dialog_title, fileName));
//			dialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					startRecording();
//				}
//			});
//			dialogBuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					mLastButtonId = 0;
//				}
//			});
//			dialogBuilder.show();
//		} else {
//			startRecording();
//		}
	}

	/*
	 * Called on destroy to unregister the SD card mount event receiver.
	 */
	public void onDestroy() {
		if (mSDCardMountEventReceiver != null) {
			ctx.unregisterReceiver(mSDCardMountEventReceiver);
			mSDCardMountEventReceiver = null;
		}
		
	}

	/*
	 * Registers an intent to listen for
	 * ACTION_MEDIA_EJECT/ACTION_MEDIA_UNMOUNTED/ACTION_MEDIA_MOUNTED
	 * notifications.
	 */
	private void registerExternalStorageListener() {
		if (mSDCardMountEventReceiver == null) {
			mSDCardMountEventReceiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					mSampleInterrupted = false;
					mRecorder.reset();
					updateUi(false);
				}
			};
			IntentFilter iFilter = new IntentFilter();
			iFilter.addAction(Intent.ACTION_MEDIA_EJECT);
			iFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
			iFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
			iFilter.addDataScheme("file");
			ctx.registerReceiver(mSDCardMountEventReceiver, iFilter);
		}
	}

	/*
	 * A simple utility to do a query into the databases.
	 */
	private Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		try {
			ContentResolver resolver = ctx.getContentResolver();
			if (resolver == null) {
				return null;
			}
			return resolver.query(uri, projection, selection, selectionArgs, sortOrder);
		} catch (UnsupportedOperationException ex) {
			return null;
		}
	}

	/*
	 * Add the given audioId to the playlist with the given playlistId; and
	 * maintain the play_order in the playlist.
	 */
	private void addToPlaylist(ContentResolver resolver, int audioId, long playlistId) {
		String[] cols = new String[] { "count(*)" };
		Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId);
		Cursor cur = resolver.query(uri, cols, null, null, null);
		cur.moveToFirst();
		final int base = cur.getInt(0);
		cur.close();
		ContentValues values = new ContentValues();
		values.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, Integer.valueOf(base + audioId));
		values.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, audioId);
		resolver.insert(uri, values);
	}



	/*
	 * Adds file and returns content uri.
	 */
	private Uri addToMediaDB(File file) {
		Resources res = ctx.getResources();
		ContentValues cv = new ContentValues();
		long current = System.currentTimeMillis();
		long modDate = file.lastModified();
		Date date = new Date(current);
		SimpleDateFormat formatter = new SimpleDateFormat("");
		String title = formatter.format(date);
		long sampleLengthMillis = mRecorder.sampleLength() * 1000L;

		// Lets label the recorded audio file as NON-MUSIC so that the file
		// won't be displayed automatically, except for in the playlist.
		cv.put(MediaStore.Audio.Media.IS_MUSIC, "0");

		cv.put(MediaStore.Audio.Media.TITLE, title);
		cv.put(MediaStore.Audio.Media.DATA, file.getAbsolutePath());
		cv.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
		cv.put(MediaStore.Audio.Media.DATE_MODIFIED, (int) (modDate / 1000));
		cv.put(MediaStore.Audio.Media.DURATION, sampleLengthMillis);
		cv.put(MediaStore.Audio.Media.MIME_TYPE, mRequestedType);
//		cv.put(MediaStore.Audio.Media.ARTIST, res.getString(R.string.audio_db_artist_name));
//		cv.put(MediaStore.Audio.Media.ALBUM, res.getString(R.string.audio_db_album_name));
		Log.d(TAG, "Inserting audio record: " + cv.toString());
		ContentResolver resolver = ctx.getContentResolver();
		Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		Log.d(TAG, "ContentURI: " + base);
		Uri result = resolver.insert(base, cv);
		if (result == null) {
			return null;
		}

//		if (getPlaylistId(res) == -1) {
//			createPlaylist(res, resolver);
//		}
		int audioId = Integer.valueOf(result.getLastPathSegment());
//		addToPlaylist(resolver, audioId, getPlaylistId(res));

		// Notify those applications such as Music listening to the
		// scanner events that a recorded audio file just created.
//		sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, result));
		return result;
	}


	/**
	 * Update the big MM:SS timer. If we are in playback, also update the progress
	 * bar.
	 */
	private void updateTimerView() {
		int state = mRecorder.state();

		boolean ongoing = state == Recorder.RECORDING_STATE || state == Recorder.PLAYING_STATE;
		if(!ongoing){
//			mOnRecorderListener.onUpdateTime(mRecorder.sampleLength());
			return;
		}
		long time =  mRecorder.playProgressP();
		if(time>=maxTimeLenth){
			mRecorder.stop();
			time = maxTimeLenth;
		}
		mOnRecorderListener.onUpdateTime((int)( time/1000));
		if (state == Recorder.RECORDING_STATE) {
			updateTimeRemaining();
		}
		if (ongoing) {
			mHandler.postDelayed(mUpdateTimer, 100);
		}
	}


	private void updateSeekBar() {
		if (mRecorder.state() == Recorder.PLAYING_STATE) {
			mOnRecorderListener.onPlayProgress((int) (mRecorder.playProgress()*100)+1);
			mHandler.postDelayed(mUpdateSeekBar, 10);
		}
	}
	
	/*
	 * Called when we're in recording state. Find out how much longer we can go on
	 * recording. If it's under 5 minutes, we display a count-down in the UI. If
	 * we've run out of time, stop the recording.
	 */
	private void updateTimeRemaining() {
		long t = mRemainingTimeCalculator.timeRemaining();

		if (t <= 0) {
			mSampleInterrupted = true;

			int limit = mRemainingTimeCalculator.currentLowerLimit();
			switch (limit) {
			case RemainingTimeCalculator.DISK_SPACE_LIMIT:
				mErrorUiMessage = ctx.getResources().getString(R.string.storage_is_full);
				break;
			case RemainingTimeCalculator.FILE_SIZE_LIMIT:
//				mErrorUiMessage = ctx.getResources().getString(R.string.max_length_reached);
				break;
			default:
				mErrorUiMessage = null;
				break;
			}

			mRecorder.stop();
			return;
		}
	}
	
	public void setVUMMax(int max){
		maxVUMSize = max;
	}
	private int maxVUMSize =7;
	private void updateVUMeterView(){
		if(mRecorder.state() == Recorder.RECORDING_STATE){
			int vuSize = maxVUMSize * mRecorder.getMaxAmplitude() / 32768;
			vuSize+=1;
			if (vuSize >= maxVUMSize) {
				vuSize = maxVUMSize;
			}
			mOnRecorderListener.onUpdateVUMetur(vuSize, maxVUMSize);
			mHandler.postDelayed(mUpdateVUMetur, 100);
		}
	}


	/**
	 * Shows/hides the appropriate child views for the new state.
	 */
	private void updateUi(boolean skipRewindAnimation) {
		switch (mRecorder.state()) {
		case Recorder.IDLE_STATE:
//			mLastButtonId = 0;
			if (mRecorder.sampleLength()>0) {
				mOnRecorderListener.onFinish();
			}else{
				mOnRecorderListener.onIdle();
			}
			Logger.info("updateUi", "IDLE_STATE");
		case Recorder.PLAYING_PAUSED_STATE:
			if (mRecorder.sampleLength() == 0) {
			} else {
			}
//			mFileNameEditText.setEnabled(true);
//			mFileNameEditText.clearFocus();

			if (mRecorder.sampleLength() > 0) {
				if (mRecorder.state() == Recorder.PLAYING_PAUSED_STATE) {
//					stopAnimation();
//					if (SoundRecorderPreferenceActivity.isEnabledSoundEffect(this)) {
//						mSoundPool.play(mPauseSound, 1.0f, 1.0f, 0, 0, 1);
//					}
				} else {
//					mPlaySeekBar.setProgress(0);
					if (!skipRewindAnimation) {
						stopRecordPlayingAnimation();
					} else {
//						stopAnimation();
					}
				}
			} else {
//				stopAnimation();
			}

			// we allow only one toast at one time
			if (mSampleInterrupted && mOnRecorderListener == null) {
				mOnRecorderListener.onError(mErrorUiMessage);
			}

			break;
		case Recorder.RECORDING_STATE:
			mOnRecorderListener.onStartRecording();
			break;
		case Recorder.PLAYING_STATE:
			mOnRecorderListener.onPlaying();
			break;
		}

		updateTimerView();
		updateSeekBar();
		updateVUMeterView();

	}

	/*
	 * Called when Recorder changed it's state.
	 */
	public void onStateChanged(int state) {
		if (state == Recorder.PLAYING_STATE || state == Recorder.RECORDING_STATE) {
			mSampleInterrupted = false;
			mErrorUiMessage = null;
		}
		updateUi(false);
	}

	/*
	 * Called when MediaPlayer encounters an error.
	 */
	public void onError(int error) {
		Resources res = ctx.getResources();

		String message = null;
		switch (error) {
		case Recorder.STORAGE_ACCESS_ERROR:
//			message = res.getString(R.string.error_sdcard_access);
			break;
		case Recorder.IN_CALL_RECORD_ERROR:
			// TODO: update error message to reflect that the recording
			// could not be
			// performed during a call.
		case Recorder.INTERNAL_ERROR:
//			message = res.getString(R.string.error_app_internal);
			break;
		}
		if (message != null) {
		}
	}
	
	public String getRecordFilePath(){
		if(mRecorder.sampleFile()!=null){
			return mRecorder.sampleFile().getAbsolutePath();
		}
		return null;
	}
	

	private class RecorderReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.hasExtra(RecorderService.RECORDER_SERVICE_BROADCAST_STATE)) {
				boolean isRecording = intent.getBooleanExtra(RecorderService.RECORDER_SERVICE_BROADCAST_STATE, false);
				mRecorder.setState(isRecording ? Recorder.RECORDING_STATE : Recorder.IDLE_STATE);
			} else if (intent.hasExtra(RecorderService.RECORDER_SERVICE_BROADCAST_ERROR)) {
				int error = intent.getIntExtra(RecorderService.RECORDER_SERVICE_BROADCAST_ERROR, 0);
				mRecorder.setError(error);
			}
		}
	}
}
