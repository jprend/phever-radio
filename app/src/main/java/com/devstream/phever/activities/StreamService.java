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
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class StreamService extends Service {
    private final static String PHEVER_URLS = "phever_urls";
    private final static String PHEVER_RADIO_URL = "phever_radio_url";
    private static final String TAG = "StreamService";
    private String url = "";
    MediaPlayer mp;
    //boolean isPlaying;
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
        // Init the SharedPreferences and Editor

        Context context = getApplicationContext();
        //prefs = context.getSharedPreferences("radioPrefs", MODE_MULTI_PROCESS);
        //editor = prefs.edit();



        // Set up the buffering notification
        notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

        String notifTitle = context.getResources().getString(R.string.service_stream);
        String notifMessage = context.getResources().getString(R.string.buffering);

        n = new Notification();
        n.icon = R.drawable.ic_launcher;
        n.tickerText = "Buffering";
        n.when = System.currentTimeMillis();

        Intent nIntent = new Intent(context, HomeActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, nIntent, 0);

        n.setLatestEventInfo(context, notifTitle, notifMessage, pIntent);

        startForeground(notifId, n);

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
            Log.e(TAG, "SecurityException");
        } catch (IllegalStateException e) {
            Log.e(TAG, "IllegalStateException");
        } catch (IOException e) {
            Log.e(TAG, "IOException");
        }
    }//close method onCreate


    @SuppressWarnings("deprecation")
    @Override
    public void onStart(Intent intent, int startId) {

        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer player){
                mp.start();
            }
        });
        // Set the isPlaying preference to true
        //editor.putBoolean("isPlaying", true);
        //editor.commit();

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
        // Change 5315 to some other number
        //notificationManager.notify(notifId, n);
        startForeground(notifId, n);
    }//close method onStart

    @Override
    public void onDestroy() {

        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer player) {
                mp.stop();
            }
        });
        mp.release();
        mp = null;

        //editor.putBoolean("isPlaying", false);
        //editor.commit();
        notificationManager.cancel(notifId);
        stopForeground(true);
    }

}//close class StreamService

