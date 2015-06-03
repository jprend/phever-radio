package com.devstream.phever.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.devstream.phever.activities.ConnectActivity;
import com.devstream.phever.activities.R;

/**
 * Created by Chris on 31/05/2015.
 */
public class ConnectAdapter extends BaseAdapter {
    private final Context context;
    private final String[] connectLabelText;
    private final int [] connectImageId;
    private static LayoutInflater inflater = null;

    public ConnectAdapter (ConnectActivity connectactivity, String[] labels, int[] images) {
        //super(context, R.layout.activity_connect, labels, images);
        this.context = connectactivity;
        this.connectLabelText = labels;
        this.connectImageId = images;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return connectLabelText.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.activity_connectlistview, null);
        holder.tv=(TextView) rowView.findViewById(R.id.connect_label);
        holder.img=(ImageView) rowView.findViewById(R.id.connect_logo);
        holder.tv.setText(connectLabelText[position]);
        holder.img.setImageResource(connectImageId[position]);
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "You Clicked " + connectLabelText[position], Toast.LENGTH_LONG).show();
                switch (position) {
                    case 0:
                        String url1 =  "https://www.facebook.com/phevermusic";
                        Intent intent1 = new Intent(Intent.ACTION_VIEW);
                        intent1.setData(Uri.parse(url1));
                        context.startActivity(intent1);
                        break;
                    case 1:
                        String url2 =  "http://twitter.com/PHEVER_Events";
                        Intent intent2 = new Intent(Intent.ACTION_VIEW);
                        intent2.setData(Uri.parse(url2));
                        context.startActivity(intent2);
                        break;
                    case 2:
                        String url3 = "https://www.youtube.com/PHEVERIRL";
                        Intent intent3 = new Intent(Intent.ACTION_VIEW);
                        intent3.setData(Uri.parse(url3));
                        context.startActivity(intent3);
                        break;
                    case 3:
                        String url4 = "http://www.house-mixes.com/profile/phever-radio";
                        Intent intent4 = new Intent(Intent.ACTION_VIEW);
                        intent4.setData(Uri.parse(url4));
                        context.startActivity(intent4);
                        break;
                    case 4:
                        String url5 = "https://soundcloud.com/pheverie";
                        Intent intent5 = new Intent(Intent.ACTION_VIEW);
                        intent5.setData(Uri.parse(url5));
                        context.startActivity(intent5);
                        break;
                    case 5:
                        String url6 = "https://www.mixcloud.com/PHEVER_Radio";
                        Intent intent6 = new Intent(Intent.ACTION_VIEW);
                        intent6.setData(Uri.parse(url6));
                        context.startActivity(intent6);
                        break;
                    case 6:
                        String url7 = "http://www.facebook.com/PHEVERacademy";
                        Intent intent7 = new Intent(Intent.ACTION_VIEW);
                        intent7.setData(Uri.parse(url7));
                        context.startActivity(intent7);
                        break;
                }//close switch

            }
        });


        return rowView;
    }

}//close class connect adapter

/*
{"Visit our Facebook Link","Visit our Twitter Link","Visit our YouTube Link","Visit our House-Mixes Link",
  "Visit our SoundCloud Link","Visit our MixCloud Link","DJ & music production academy"};

  {R.drawable.facebook_icon, R.drawable.twitter_icon, R.drawable.utube_icon, R.drawable.housemixes_icon,
   R.drawable.soundcloud_icon, R.drawable.mixcloud_icon, R.drawable.djacademy_icon };
*/

/*cire if class with list adapter
public class ConnectAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] connectLabelText;

    public ConnectAdapter (Context context, String[] textValues) {
        super(context, R.layout.activity_connect, textValues);
        this.context = context;
        this.connectLabelText = textValues;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.activity_connect, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
        textView.setText(connectLabelText[position]);

        // Change icon based on name
        String s = connectLabelText[position];

       // System.out.println(s);

        if (s.equals("Visit our Facebook Link")) {
            imageView.setImageResource(R.drawable.facebook_icon);
        } else if (s.equals("Visit our Twitter Link")) {
            imageView.setImageResource(R.drawable.twitter_icon);
        } else if (s.equals("Visit our YouTube Link")) {
            imageView.setImageResource(R.drawable.utube_icon);
        } else if (s.equals("Visit our House-Mixes Link")) {
            imageView.setImageResource(R.drawable.housemixes_icon);
        } else if (s.equals("Visit our SoundCloud Link")) {
            imageView.setImageResource(R.drawable.soundcloud_icon);
        } else if (s.equals("Visit our MixCloud Link")) {
            imageView.setImageResource(R.drawable.mixcloud_icon);
        } else {
            imageView.setImageResource(R.drawable.djacademy_icon);
        }

        return rowView;
    }


 */