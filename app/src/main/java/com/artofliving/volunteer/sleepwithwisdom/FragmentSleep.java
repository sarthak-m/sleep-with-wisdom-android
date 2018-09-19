package com.artofliving.volunteer.sleepwithwisdom;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

@SuppressLint("InflateParams")
public class FragmentSleep extends Fragment {

	private Tracker mTracker;

	private ImageView playImg;
	// AlertDialog alertDialog;
	float brightness;

	// handle calls in between
	TelephonyManager telephonyManager;
	PhoneCallListener phoneListener;

	TextView tvLabel;
	String DIRECTORY_PATH = Environment.getExternalStorageDirectory().getPath()
			+ "/Sleep";

	private Chronometer focus;
	private long timeWhenStopped = 0;

	AssetFileDescriptor assetFileDescriptor = null;
	private Boolean file_loaded = null;
	private MediaPlayer mp = new MediaPlayer();
	String[] audioFiles;

	private LinearLayout beginSleep;
	private RelativeLayout showTips;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_sleep, container, false);

		// GET RESOURCES AFTER onCreate()
		audioFiles = getActivity().getResources().getStringArray(
				R.array.audio_files_mp3);

		showTips = (RelativeLayout) view
				.findViewById(R.id.rlTipsForBetterSleep);
		beginSleep = (LinearLayout) view.findViewById(R.id.llSleepBegin);
		playImg = (ImageView) view.findViewById(R.id.frag_sleep_play_pause);
		tvLabel = (TextView) view.findViewById(R.id.textViewLabel);
		focus = (Chronometer) view.findViewById(R.id.testChronometer);

		// pick a random file from audio folder
		openFile(audioFiles[randomInt(0, 1)]);

		playImg.setTag(R.drawable.img_btn_play);
		playImg.setImageResource(R.drawable.img_btn_play);

		focus.setBase(SystemClock.uptimeMillis());

		brightness = getActivity().getWindow().getAttributes().screenBrightness;
		brightness = Settings.System.getInt(getActivity().getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS, -1);

		phoneListener = new PhoneCallListener();
		telephonyManager = (TelephonyManager) getActivity().getSystemService(
				Context.TELEPHONY_SERVICE);
		telephonyManager.listen(phoneListener,
				PhoneStateListener.LISTEN_CALL_STATE);

		beginSleep.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				if (file_loaded) {
					if (playImg.getTag().equals(R.drawable.img_btn_play)) {
						focus.setBase(SystemClock.elapsedRealtime()
								+ timeWhenStopped);
						focus.start();
						mp.start();
						tvLabel.setText("Pause");
						playImg.setTag(R.drawable.img_btn_pause);
						playImg.setImageResource(R.drawable.img_btn_pause);
//						dimBrightness();

						// Build and send an Event.
						mTracker.send(new HitBuilders.EventBuilder()
								.setAction("Media started")
								.setLabel("Going to sleep").build());

					} else if (playImg.getTag()
							.equals(R.drawable.img_btn_pause)) {
						timeWhenStopped = focus.getBase()
								- SystemClock.elapsedRealtime();
						focus.stop();
						mp.pause();
						if (timeWhenStopped != 0) {
							tvLabel.setText("Resume");
							playImg.setTag(R.drawable.img_btn_play);
							playImg.setImageResource(R.drawable.img_btn_play);
//							revertBrightness();
						}
					}
				} else {
					Toast.makeText(getActivity(), "Error loading file.",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		showTips.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), TipsFragmentActivity.class);
				startActivityForResult(i, 1);
			}

		});

		mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

			@SuppressLint("InflateParams")
			@Override
			public void onCompletion(MediaPlayer mp1) {

				mTracker = ((SleepWithWisdom) getActivity().getApplication())
						.getDefaultTracker();
				mTracker.setScreenName("Media Complete");
				mTracker.send(new HitBuilders.ScreenViewBuilder().build());

				tvLabel.setText("Begin");

				// RESET BEIGHTNESS TO ORIGINAL
				android.provider.Settings.System.putInt(getActivity()
						.getContentResolver(),
						android.provider.Settings.System.SCREEN_BRIGHTNESS,
						(int) brightness);

				// RESET THE MEDIA PLAYER, OPEN AND LOAD THE FILE AGAIN
				mp.reset();

				timeWhenStopped = 0;
				playImg.setTag(R.drawable.img_btn_play);
				playImg.setImageResource(R.drawable.img_btn_play);

				openFile(audioFiles[randomInt(0, 1)]);

				/*
				 * AlertDialog.Builder dialog = new AlertDialog.Builder(
				 * getActivity());
				 * 
				 * LayoutInflater inflater = getActivity().getLayoutInflater();
				 * 
				 * View dialogView; dialogView =
				 * inflater.inflate(R.layout.dialog_media_complete, null);
				 * dialog.setView(dialogView); dialog.setOnCancelListener(new
				 * OnCancelListener() {
				 * 
				 * @Override public void onCancel(DialogInterface dialog) { }
				 * });
				 * 
				 * alertDialog = dialog.create();
				 * alertDialog.setCanceledOnTouchOutside(true);
				 * alertDialog.setCancelable(true);
				 * alertDialog.setOnCancelListener(new OnCancelListener() {
				 * 
				 * @Override public void onCancel(DialogInterface dialog) {
				 * openFile(audioFiles[randomInt(0, 1)]); } });
				 * 
				 * alertDialog.show();
				 * 
				 * Button thank_you, still_awake; thank_you = (Button)
				 * dialogView .findViewById(R.id.bMediaCompleteThankYou);
				 * thank_you.setOnClickListener(FragmentSleep.this); still_awake
				 * = (Button) dialogView
				 * .findViewById(R.id.bMediaCompleteStillAwake);
				 * still_awake.setOnClickListener(FragmentSleep.this);
				 */
			}
		});

		mTracker = ((SleepWithWisdom) getActivity().getApplication())
				.getDefaultTracker();
		mTracker.setScreenName("Sleep Fragment");
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());

		return view;
	}

	public static int randomInt(int min, int max) {

		// NOTE: Usually this should be a field rather than a method
		// variable so that it is not re-seeded every call.
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

	protected void revertBrightness() {
		android.provider.Settings.System.putInt(getActivity()
				.getContentResolver(),
				android.provider.Settings.System.SCREEN_BRIGHTNESS,
				(int) brightness);
	}

	protected void dimBrightness() {
		android.provider.Settings.System.putInt(getActivity()
				.getContentResolver(),
				android.provider.Settings.System.SCREEN_BRIGHTNESS, 0);

	}

	/*
	 * private void goToTips() {
	 * 
	 * Intent i = new Intent(getActivity(), TipsFragmentActivity.class);
	 * startActivityForResult(i, 1); }
	 */

	private void openFile(String file_name) {
		file_loaded = loadFileToMediaPlayer(file_name);
	}

	// check if file exists
	@SuppressWarnings("finally")
	private boolean loadFileToMediaPlayer(String file_name) {

		if (new File(DIRECTORY_PATH + "/" + file_name).exists()) {
			try {
				mp.setDataSource(DIRECTORY_PATH + "/" + file_name);
				mp.prepare();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				return true;
			}

		}

		return false;

	}

	@Override
	public void onDestroyView() {
		mp.pause();
		mp.release();
//		revertBrightness();
		if (telephonyManager != null) {
			telephonyManager.listen(phoneListener,
					PhoneStateListener.LISTEN_NONE);
		}
		super.onDestroyView();

	}

	/*
	 * @Override public void onClick(View v) { switch (v.getId()) {
	 * 
	 * case R.id.bMediaCompleteStillAwake: alertDialog.cancel(); goToTips();
	 * break;
	 * 
	 * case R.id.bMediaCompleteThankYou: openFile(audioFiles[randomInt(0, 1)]);
	 * alertDialog.cancel();
	 * 
	 * break; } }
	 */

	private class PhoneCallListener extends PhoneStateListener {

		private boolean isPhoneCalling = false;

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {

			if (TelephonyManager.CALL_STATE_RINGING == state) {
				// ringing number : incomingNumber

				if (mp.isPlaying())
					timeWhenStopped = focus.getBase()
							- SystemClock.elapsedRealtime();
				focus.stop();
				mp.pause();
				playImg.setTag(R.drawable.img_btn_play);
				playImg.setImageResource(R.drawable.img_btn_play);
//				revertBrightness();
			}

			if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
				// active

				if (mp.isPlaying())
					timeWhenStopped = focus.getBase()
							- SystemClock.elapsedRealtime();
				focus.stop();
				mp.pause();
				playImg.setTag(R.drawable.img_btn_play);
				playImg.setImageResource(R.drawable.img_btn_play);
//				revertBrightness();

				isPhoneCalling = true;
			}

			if (TelephonyManager.CALL_STATE_IDLE == state) {
				// now idle

				// run when class initial and phone call ended, need detect flag
				// from CALL_STATE_OFFHOOK
				if (isPhoneCalling) {

					isPhoneCalling = false;
				}

			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		// Set title
		if (getActivity().getActionBar() != null) {
			getActivity().getActionBar().setTitle(HomeActivity.TITLE_SLEEP);
		}
	}

}