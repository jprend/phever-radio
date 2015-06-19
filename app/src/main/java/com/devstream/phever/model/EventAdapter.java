package com.devstream.phever.model;

import android.content.Context;

import java.util.ArrayList;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.devstream.phever.activities.R;
import com.devstream.phever.utilities.ImageLoader;


public class EventAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Event> eventList;


    final String defaultIMAGE = "logo_small.jpg";
    final String IMAGEPATH = "http://www.phever.ie/images/";

    public ImageLoader imageLoader;


    // the context is needed to inflate views in getView()

    public EventAdapter(Context context, ArrayList<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
        this.imageLoader = new ImageLoader(context);
    }

    public void updateeventList(ArrayList<Event> eventList) {
        this.eventList = eventList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    // getItem(int) in Adapter returns Object but we can override
    // it to Event thanks to Java return type covariance
    @Override
    public Event getItem(int position) {
        return eventList.get(position);
    }

    // getItemId() is often useless, I think this should be the default
    // implementation in BaseAdapter
    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder {
        public final ImageView eventImageView;
        public final String eventImageUrl;

        public final TextView edate;
        public final TextView name;
        public final TextView location;
        public final TextView headline;
        public final TextView headlineDesc;
        public final TextView price;
        public final TextView purchase;
        public final TextView supportActs;
        public final TextView terms;


        public ViewHolder(ImageView eventImageView,
                          String eventImageUrl,
                          TextView edate,
                          TextView name,
                          TextView location,
                          TextView headline,
                          TextView headlineDesc,
                          TextView price,
                          TextView purchase,
                          TextView supportActs,
                          TextView terms


        ) {
            this.eventImageView = eventImageView;
            this.eventImageUrl = eventImageUrl;

            this.edate = edate;
            this.name = name;
            this.location = location;
            this.headline = headline;
            this.headlineDesc = headlineDesc;
            this.price = price;
            this.purchase = purchase;
            this.supportActs = supportActs;
            this.terms = terms;

        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView eventImageView;
        String eventImageUrl = "";


        TextView edate;
        TextView name;
        TextView location;
        TextView headline;
        TextView headlineDesc;
        TextView price;
        TextView purchase;
        TextView supportActs;
        TextView terms;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_event, parent, false);

            eventImageView = (ImageView) convertView.findViewById(R.id.ivImage);

            edate = (TextView) convertView.findViewById(R.id.tvEdate);
            name = (TextView) convertView.findViewById(R.id.tvName);
            location = (TextView) convertView.findViewById(R.id.tvLocation);
            headline = (TextView) convertView.findViewById(R.id.tvHeadline);
            headlineDesc = (TextView) convertView.findViewById(R.id.tvHeadlineDesc);

            price = (TextView) convertView.findViewById(R.id.tvPrice);
            purchase = (TextView) convertView.findViewById(R.id.tvPurchase);
            supportActs = (TextView) convertView.findViewById(R.id.tvSupportActs);
            terms = (TextView) convertView.findViewById(R.id.tvTerms);
            convertView.setTag(new ViewHolder(eventImageView, eventImageUrl, edate, name, location, headline, headlineDesc,
                    price, purchase, supportActs, terms));

        } else {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            eventImageView = viewHolder.eventImageView;
            eventImageUrl = viewHolder.eventImageUrl;
            Log.i("jp01", "s1 " + eventImageUrl);

            edate = viewHolder.edate;
            name = viewHolder.name;
            location = viewHolder.location;
            headline = viewHolder.headline;
            headlineDesc = viewHolder.headlineDesc;
            price = viewHolder.price;
            purchase = viewHolder.purchase;
            supportActs = viewHolder.supportActs;
            terms = viewHolder.terms;
        }
        Event tEvent = getItem(position);

        String imageFileName = tEvent.getImageUrl();
        Log.i("jp01", "s2 " + imageFileName);
        if (imageFileName == null) {
            imageFileName = defaultIMAGE;
        }
        if (imageFileName.equals("") || imageFileName.equals(null) || imageFileName.equals("null")) {
            imageFileName = defaultIMAGE;
        }

        eventImageUrl = IMAGEPATH + imageFileName;
        Log.i("jp01", "s3 " + eventImageUrl);

        imageLoader.DisplayImage(eventImageUrl, eventImageView);

        edate.setText(tEvent.getEdate());
        name.setText(tEvent.getName());
        location.setText(tEvent.getLocation());
        headline.setText(tEvent.getHeadline());
        headlineDesc.setText(tEvent.getHeadlineDesc());

        price.setText(tEvent.getPrice());
        purchase.setText(tEvent.getPurchase());

        String value = tEvent.getSupportActs().toString();
        supportActs.setText(value);

        value = tEvent.getTerms().toString();
        terms.setText(value);
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