package com.devstream.phever.activities;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TableRow;

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

/*core of class if needed
   static final String[] CONNECT_TEXT_LABELS = new String[]{"Visit our Facebook Link","Visit our Twitter Link",
                                                   "Visit our YouTube Link","Visit our House-Mixes Link",
                                                   "Visit our SoundCloud Link","Visit our MixCloud Link",
                                                   "DJ & music production academy"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new ConnectAdapter(this, CONNECT_TEXT_LABELS ));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        //get selected items
        //int selectedValue = (Integer)getListAdapter().getItem(position);
        switch (position) {
            case 0:
                String url1 =  "https://www.facebook.com/phevermusic";
                Intent intent1 = new Intent(Intent.ACTION_VIEW);
                intent1.setData(Uri.parse(url1));
                startActivity(intent1);
                break;
            case 1:
                String url2 =  "http://twitter.com/PHEVER_Events";
                Intent intent2 = new Intent(Intent.ACTION_VIEW);
                intent2.setData(Uri.parse(url2));
                startActivity(intent2);
                break;
            case 2:
                String url3 = "https://www.youtube.com/PHEVERIRL";
                Intent intent3 = new Intent(Intent.ACTION_VIEW);
                intent3.setData(Uri.parse(url3));
                startActivity(intent3);
                break;
            case 3:
                String url4 = "http://www.house-mixes.com/profile/phever-radio";
                Intent intent4 = new Intent(Intent.ACTION_VIEW);
                intent4.setData(Uri.parse(url4));
                startActivity(intent4);
                break;
            case 4:
                String url5 = "https://soundcloud.com/pheverie";
                Intent intent5 = new Intent(Intent.ACTION_VIEW);
                intent5.setData(Uri.parse(url5));
                startActivity(intent5);
                break;
            case 5:
                String url6 = "https://www.mixcloud.com/PHEVER_Radio";
                Intent intent6 = new Intent(Intent.ACTION_VIEW);
                intent6.setData(Uri.parse(url6));
                startActivity(intent6);
                break;
            case 6:
                String url7 = "http://www.facebook.com/PHEVERacademy";
                Intent intent7 = new Intent(Intent.ACTION_VIEW);
                intent7.setData(Uri.parse(url7));
                startActivity(intent7);
                break;
        }
    }
 */
