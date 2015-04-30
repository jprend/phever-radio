package com.devstream.phever.model;

import android.content.Context;

import java.util.ArrayList;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.devstream.phever.activities.R;


public class EventAdapter extends ArrayAdapter<Event> {

    Context mContext;
    int layoutResourceId;
    ArrayList<Event> eventList = new ArrayList<>();


    public Context getmContext() {
        return mContext;
    }


    public EventAdapter(Context context, int layout, ArrayList<Event> events) {
        super(context, layout, events);
        this.mContext = context;
        this.layoutResourceId = layout;
        this.eventList = events;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Event item;
        item = getItem(position);

        // Test to see if there is already a view, if not create one, else use what
        // already exists in convertView
        ViewHolder holder;
        String TAG = "Adapter";
        Log.d(TAG, "position= <" + position + ">" + item.getName());

        View v = convertView;
        if (v == null) {
            // if there is no existing view
            // use an Inflater to build the row layout and store in view

            LayoutInflater inflater = LayoutInflater.from(getmContext());
            v = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();

            //edate   name location    headline    headline_desc
            holder.edate = (TextView) v.findViewById(R.id.tvEdate);
            holder.name = (TextView) v.findViewById(R.id.tvName);
            holder.location = (TextView) v.findViewById(R.id.tvLocation);
            holder.headline = (TextView) v.findViewById(R.id.tvHeadline);
            holder.headlineDesc = (TextView) v.findViewById(R.id.tvHeadlineDesc);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }


        holder.edate.setText(eventList.get(position).getEdate());
        holder.name.setText(eventList.get(position).getName());
        holder.location.setText(eventList.get(position).getLocation());
        holder.headline.setText(eventList.get(position).getHeadline());
        holder.headlineDesc.setText(eventList.get(position).getHeadlineDesc());
        return v;
    }

    private static class ViewHolder {
        //edate   name location    headline    headline_desc
        // ImageView imageview;
        TextView edate;
        TextView name;
        TextView location;
        TextView headline;
        TextView headlineDesc;
    }
}