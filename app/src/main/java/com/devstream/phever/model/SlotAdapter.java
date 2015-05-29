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
    Context context;
    int layoutResourceId;
    ArrayList<Slot> Schedule;
    final String IMAGE = "logo_small.jpg";
    final String IMAGEPATH = "http://phever.ie/images/";
    ViewHolder holder;

    @Override
    public Context getContext() {
        return context;
    }

    private static class ViewHolder {
        ImageView imageview;
        TextView time;
        TextView genre;
        TextView djName;
        TextView showTitle;
        Bitmap djBmp;
    }

    public SlotAdapter(Context context, int layoutResourceId, ArrayList<Slot> slots) {
        super(context, layoutResourceId, slots);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.Schedule = slots;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Slot item;
        item = getItem(position);

        // Test to see if there is already a view, if not create one, else use what
        // already exists in convertView
        //ViewHolder holder;
        String TAG = "Adapter";
        Log.d(TAG, "position= <" + position + ">" + item.getDjImage());

        View v = convertView;
        if (v == null) {
            // if there is no existing view
            // use an Inflater to build the row layout and store in view
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
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
        String djLogo = Schedule.get(position).getDjImage();
        if (djLogo.equals(null) || djLogo.isEmpty() || djLogo.equals("null")) {
            djLogo = IMAGE;
        }


        Log.d(TAG, "djLogo = <" + djLogo + ">");
        new DownloadImageTask(holder.imageview).execute(djLogo);

        holder.time.setText(Schedule.get(position).getStime());
        holder.genre.setText(Schedule.get(position).getGenre());
        holder.djName.setText(Schedule.get(position).getDjName());
        holder.showTitle.setText(Schedule.get(position).getShowTitle());

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
            Bitmap bmp = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bmp = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bmp;
        }

        protected void onPostExecute(Bitmap result) {
            holder.djBmp = result;
           /* bmImage.setAdjustViewBounds(true);
            bmImage.setMaxHeight(bmImage.getHeight());
            bmImage.setMaxWidth(bmImage.getWidth());
            bmImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE); */
            bmImage.setImageBitmap(result);
        }

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