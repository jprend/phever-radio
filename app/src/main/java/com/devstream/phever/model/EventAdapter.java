package com.devstream.phever.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.devstream.phever.activities.R;

import java.io.InputStream;
import java.util.ArrayList;


public class EventAdapter extends ArrayAdapter<Event> {
	Context context;
	int layoutResourceId;
	ArrayList<Event> EventList;
	final String IMAGE = "logo_small.jpg";
	final String IMAGEPATH = "http://phever.ie/images/";
	@Override
	public Context getContext() {
		return context;
	}

	private static class ViewHolder {
		//edate   name location    headline    headline_desc
		 ImageView imageview;
		 TextView edate;
		 TextView name;
		 TextView location;
		 TextView headline;
		 TextView headlineDesc;
	}
	public EventAdapter(Context context, int layoutResourceId, ArrayList<Event> events) {
		super(context, layoutResourceId, events);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.EventList = events;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Event item;
		item = getItem(position);

		// Test to see if there is already a view, if not create one, else use what
		// already exists in convertView
		ViewHolder holder = null;
		String TAG =  "Adapter";
		Log.d(TAG, "position= <" + position + ">" + item.getName());

		View v = convertView;
		if (v == null) {
			// if there is no existing view
			// use an Inflater to build the row layout and store in view
			LayoutInflater inflater =LayoutInflater.from(getContext());
			v = inflater.inflate(layoutResourceId, parent, false);
			holder = new ViewHolder();
			holder.imageview = (ImageView) v.findViewById(R.id.ivImage);
			//edate   name location    headline    headline_desc
			holder.edate = (TextView) v.findViewById(R.id.tvEdate);
			holder.name = (TextView) v.findViewById(R.id.tvName);
			holder.location = (TextView) v.findViewById(R.id.tvLocation);
			holder.headline = (TextView) v.findViewById(R.id.tvHeadline);
			holder.headlineDesc = (TextView) v.findViewById(R.id.tvHeadlineDesc);
			v.setTag(holder);
		}
		else {
			holder = (ViewHolder) v.getTag();
		}



		holder.edate.setText(EventList.get(position).getEdate());
		holder.name.setText(EventList.get(position).getName());
		holder.location.setText(EventList.get(position).getLocation());
		holder.headline.setText(EventList.get(position).getHeadline());
		holder.headlineDesc.setText(EventList.get(position).getHeadlineDesc());
		return v;
	}



	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;

		}

		protected Bitmap doInBackground(String... urls) {

			String urldisplay = IMAGEPATH + urls[0];
			//String urldisplay = "http://phever.ie/images/ken_logo.jpg";
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {


			bmImage.setAdjustViewBounds(true);
			bmImage.setMaxHeight(bmImage.getHeight());
			bmImage.setMaxWidth(bmImage.getWidth());
			bmImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

			
			bmImage.setImageBitmap(result);
		}

	}
}