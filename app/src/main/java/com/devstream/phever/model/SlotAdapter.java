package com.devstream.phever.model;


import java.util.ArrayList;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.devstream.phever.activities.R;
import com.devstream.phever.utilities.ImageLoader;

public class SlotAdapter extends BaseAdapter {

    private  Context context;
    private ArrayList<Slot> schedule;


    final String defaultIMAGE = "logo_small.jpg";
    final String IMAGEPATH = "http://www.phever.ie/images/";

    public ImageLoader imageLoader;


    // the context is needed to inflate views in getView()
    public SlotAdapter(Context context, ArrayList<Slot> inSchedule) {
        this.context = context;
        this.schedule = inSchedule;
        this.imageLoader = new ImageLoader(context);
    }

    public void updateSchedule(ArrayList<Slot> inSchedule) {
        this.schedule = inSchedule;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return schedule.size();
    }

    // getItem(int) in Adapter returns Object but we can override
    // it to Slot thanks to Java return type covariance
    @Override
    public Slot getItem(int position) {
        return schedule.get(position);
    }

    // getItemId() is often useless, I think this should be the default
    // implementation in BaseAdapter
    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder {
        public final ImageView djImageView;
        public final String djImageUrl;
        public final TextView time;
        public final TextView genre;
        public final TextView djName;
        public final TextView showTitle;

        public ViewHolder(	  ImageView djImageView,
                              String djImageUrl,
                              TextView time,
                              TextView genre,
                              TextView djName,
                              TextView showTitle) {
            this.djImageView = djImageView;
            this.djImageUrl = djImageUrl;
            this.time = time;
            this.genre = genre;
            this.djName = djName;
            this.showTitle = showTitle;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView djImageView;
        String djImageUrl = "";
        TextView time;
        TextView genre;
        TextView djName;
        TextView showTitle;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_slot, parent, false);

            djImageView = (ImageView) convertView.findViewById(R.id.ivImage);
            time = (TextView) convertView.findViewById(R.id.tvTime);
            genre = (TextView) convertView.findViewById(R.id.tvGenre);
            djName = (TextView) convertView.findViewById(R.id.tvDjName);
            showTitle = (TextView) convertView.findViewById(R.id.tvShowTitle);
            convertView.setTag(new ViewHolder( djImageView,	djImageUrl, time, genre, djName, showTitle));

        } else {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            djImageView = viewHolder.djImageView;
            djImageUrl = viewHolder.djImageUrl;
            //Log.i("Image","Loader " + djImageUrl);



            time = viewHolder.time;
            genre = viewHolder.genre;
            djName = viewHolder.djName;
            showTitle = viewHolder.showTitle;
        }
        Slot tSlot = getItem(position);

        String imageFileName = tSlot.getDjImage();
        //Log.i("Image","Blank " + imageFileName);
        if (imageFileName.equals("") || imageFileName.equals(null) || imageFileName.equals("null")  )   {
            imageFileName = defaultIMAGE;
        }

        djImageUrl = IMAGEPATH + imageFileName;
        //Log.i("Image","Blank " + djImageUrl);

        imageLoader.DisplayImage(djImageUrl, djImageView);

        //djImageView.setImageResource(int id);
        time.setText(tSlot.getStime());
        genre.setText(tSlot.getGenre());
        djName.setText(tSlot.getDjName());
        showTitle.setText(tSlot.getShowTitle());

        return convertView;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}