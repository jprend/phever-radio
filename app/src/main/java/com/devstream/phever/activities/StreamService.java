package com.devstream.phever.activities;

import java.io.IOException;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class StreamService extends Service {
    private final static String PHEVER_URLS = "com.devstream.phever.activities.phever_urls";
    private final static String PHEVER_RADIO_URL = "com.devstream.phever.activities.phever_radio_url";
	private static final String TAG = "StreamService";
    private String url = "";
	MediaPlayer mp;
	boolean isPlaying;
	SharedPreferences prefs;
	SharedPreferences.Editor editor;
    SharedPreferences urlPrefs;
    SharedPreferences.Editor editUrlPrefs;
	Notification n;
	NotificationManager notificationManager;
	// Change this int to some number specifically for this app
	int notifId = 5315;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		super.onCreate();
		//Log.d(TAG, "onCreate");

		// Init the SharedPreferences and Editor
		prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		editor = prefs.edit();

		// Set up the buffering notification
		notificationManager = (NotificationManager) getApplicationContext()
				.getSystemService(NOTIFICATION_SERVICE);
		Context context = getApplicationContext();

		String notifTitle = context.getResources().getString(R.string.service_stream);
		String notifMessage = context.getResources().getString(R.string.buffering);

		n = new Notification();
		n.icon = R.drawable.ic_launcher;
		n.tickerText = "Buffering";
		n.when = System.currentTimeMillis();

		Intent nIntent = new Intent(context, HomeActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(context, 0, nIntent, 0);

		n.setLatestEventInfo(context, notifTitle, notifMessage, pIntent);

		// notificationManager.notify(notifId, n);
		// http://developer.android.com/guide/topics/media/mediaplayer.html
		startForeground(notifId, n);

		// It's very important that you put the IP/URL of your ShoutCast stream here
		// Otherwise you'll get Webcom Radio
		//String url = "http://176.31.115.196:8214/";

        urlPrefs = getSharedPreferences(PHEVER_URLS, Context.MODE_PRIVATE);
		url = urlPrefs.getString(PHEVER_RADIO_URL, null); //"http://89.101.1.140:8003/";
		mp = new MediaPlayer();
		mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			mp.setDataSource(url);
			mp.prepare();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			//Log.e(TAG, "SecurityException");
			e.printStackTrace();
		} catch (IllegalStateException e) {
			//Log.e(TAG, "IllegalStateException");
			e.printStackTrace();
		} catch (IOException e) {
			//Log.e(TAG, "IOException");
			e.printStackTrace();
		}
	}


	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		//Log.d(TAG, "onStart");
		//mp.start();
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer player){
                mp.start();  //or mp.stop();
            }
        });
		// Set the isPlaying preference to true
		editor.putBoolean("isPlaying", true);
		editor.commit();

		Context context = getApplicationContext();
		String notifTitle = context.getResources().getString(R.string.service_stream);
		String notifMessage = context.getResources().getString(R.string.now_playing);

		n.icon = R.drawable.ic_launcher;
		n.tickerText = notifMessage;
		n.flags = Notification.FLAG_NO_CLEAR;
		n.when = System.currentTimeMillis();

		Intent nIntent = new Intent(context, HomeActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(context, 0, nIntent, 0);

		n.setLatestEventInfo(context, notifTitle, notifMessage, pIntent);
		// Change 5315 to some nother number
		//notificationManager.notify(notifId, n);
		startForeground(notifId, n);
	}

    @Override
	public void onDestroy() {
		//Log.d(TAG, "onDestroy");
		//mp.stop();
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer player){
                mp.stop();
            }
        });
		mp.release();
		mp = null;
		editor.putBoolean("isPlaying", false);
		editor.commit();
		notificationManager.cancel(notifId);
		stopForeground(true);
	}

}

/*
might need this to solve the error media player start called in state 0
and
media player stop called in state 0
from http://stackoverflow.com/questions/23197807/error-mediaplayer-start-called-in-state-0-error-38-0

  mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
    public void onPrepared(MediaPlayer player){
         mp.start();  //or mp.stop();
    }
 });

 */