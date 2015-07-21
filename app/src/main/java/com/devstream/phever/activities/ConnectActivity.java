package com.devstream.phever.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import com.devstream.phever.model.ConnectAdapter;

public class ConnectActivity extends Activity {
   private ListView connectLv;
   private Context context = this;
   public static final String[] CONNECT_LABELS = {"Visit our Facebook Link","Visit our Twitter Link",
                                                   "Visit our YouTube Link","Visit our House-Mixes Link",
                                                   "Visit our SoundCloud Link","Visit our MixCloud Link",
                                                   "DJ & music production academy"};
   public static final int[] CONNECT_IMAGES = {R.drawable.facebook_icon, R.drawable.twitter_icon,
                                                R.drawable.utube_icon, R.drawable.housemixes_icon,
                                                R.drawable.soundcloud_icon, R.drawable.mixcloud_icon,
                                                R.drawable.djacademy_icon };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        connectLv=(ListView) findViewById(R.id.connect_listview);
        connectLv.setAdapter(new ConnectAdapter(this, CONNECT_LABELS, CONNECT_IMAGES));

    }

}//close class connectactivity


