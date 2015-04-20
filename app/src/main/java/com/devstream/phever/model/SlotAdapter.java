package com.devstream.phever.model;

import java.io.InputStream;
import java.util.ArrayList;

import com.devstream.phever.activities.R;

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

public class SlotAdapter extends ArrayAdapter<Slot> {
	ArrayList<Slot> Schedule;
	LayoutInflater vi;
	int Resource;
	ViewHolder holder;

	public SlotAdapter(Context context, int resource, ArrayList<Slot> objects) {
		super(context, resource, objects);
		vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Resource = resource;
		Schedule = objects;
	}
 
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// convert view = design
		View v = convertView;
		if (v == null) {
			holder = new ViewHolder();
			v = vi.inflate(Resource, null);
			holder.imageview = (ImageView) v.findViewById(R.id.ivImage);
			holder.time = (TextView) v.findViewById(R.id.tvTime);
			holder.genre = (TextView) v.findViewById(R.id.tvGenre);
			holder.djName = (TextView) v.findViewById(R.id.tvDjName);
			holder.showTitle = (TextView) v.findViewById(R.id.tvShowTitle);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		//holder.imageview.setImageResource(R.drawable.ic_launcher);
		new DownloadImageTask(holder.imageview).execute(Schedule.get(position).getDjImage());
		holder.time.setText(Schedule.get(position).getStime());
		holder.genre.setText(Schedule.get(position).getGenre());
		holder.djName.setText(Schedule.get(position).getDjName());
		holder.showTitle.setText(Schedule.get(position).getShowTitle());


		return v;

	}

	static class ViewHolder {
		public ImageView imageview;
		public TextView time;
		public TextView genre;
		public TextView djName;
		public TextView showTitle;


	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;

		}

		protected Bitmap doInBackground(String... urls) {

			String urldisplay = "http://phever.ie/images/" + urls[0];
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
			//------------------------------------------------- 		
			bmImage.setAdjustViewBounds(true);
			bmImage.setMaxHeight(bmImage.getHeight());
			bmImage.setMaxWidth(bmImage.getWidth());
			//bmImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			//-------------------------------------------------
			
			bmImage.setImageBitmap(result);
		}

	}
}