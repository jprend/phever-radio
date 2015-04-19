package com.devstream.phever.activities;

import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class RadioActivity extends Activity {
	
	Button startButton, stopButton;
	static Context context;
	boolean isPlaying;
	Intent streamService;
	SharedPreferences prefs;
	AudioManager audioManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		startButton = (Button) findViewById(R.id.startButton);
		startButton.setVisibility(View.VISIBLE);
		stopButton = (Button) findViewById(R.id.stopButton);
		stopButton.setVisibility(View.VISIBLE);
		context = this;

		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		//this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		/*   -- set max volume
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
		    audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
		    AudioManager.FLAG_SHOW_UI);
		*/

		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		getPrefs();
		streamService = new Intent(RadioActivity.this, StreamService.class);		
		
		startButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startService(streamService);
				startButton.setEnabled(false);
			}
		});
		
		stopButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				stopService(streamService);
				startButton.setEnabled(true);
			}
		});
	}
		
	public void getPrefs() {
			isPlaying = prefs.getBoolean("isPlaying", false);
			if (isPlaying) startButton.setEnabled(false);
	}
	//http://stackoverflow.com/questions/4593552/how-do-you-get-set-media-volume-not-ringtone-volume-in-android
	// more on raising a bar
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    switch (keyCode) {
	    case KeyEvent.KEYCODE_VOLUME_UP:
	        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
	                AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
	        return true;
	    case KeyEvent.KEYCODE_VOLUME_DOWN:
	        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
	                AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
	        return true;
	    default:
	        return false;
	    }
	}
	
}
