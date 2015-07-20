package com.devstream.phever.model;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.devstream.phever.activities.ConnectActivity;
import com.devstream.phever.activities.R;
import com.devstream.phever.utilities.GeneralAlertDialog;

import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Chris on 31/05/2015.
 */
public class ConnectAdapter extends BaseAdapter implements GeneralAlertDialog.NoticeDialogListener {
    private final static String pheverFacebookUrl = "https://www.facebook.com/phevermusic"; // phever facebook link
    private final static String pheverTwitterUrl = "https://twitter.com/search?q=PHEVER_Events"; //phever twitter link
    private final static String pheverYoutubeUrl = "https://www.youtube.com/PHEVERIRL";  // phever youtube link
    private final static String housemixesUrl = "http://www.house-mixes.com/profile/phever-radio"; //phever house mixes link
    private final static String soundCloudUrl =  "https://soundcloud.com/pheverie"; //phever soundcloud link
    private final static String mixCloudUrl = "https://www.mixcloud.com/PHEVER_Radio"; // phever mixcloud link
    private final static String pheverAcademyUrl = "http://www.facebook.com/PHEVERacademy"; //phever dj music academy link

    private String url;
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
    public void onDialogPositiveClick(DialogFragment dialog, int ok_option) {
        //user pressed ok button
        //to do stuff if required
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog, int cancel_option) {
        //user pressed cancel button
        //to to stuff if required
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }


    @Override
    public int getCount() {
        return connectLabelText.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
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
                        url = pheverFacebookUrl;
                        new HandleUrlConnect_ConnectAdapter().execute(url);//calls asyncTask class to try connect to i.execute(url);//calls asyncTask class to try connect to internet
                        break;
                    case 1:
                        url =  pheverTwitterUrl;
                        new HandleUrlConnect_ConnectAdapter().execute(url);//calls asyncTask class to try connect to i.execute(url);//calls asyncTask class to try connect to internet
                        break;
                    case 2:
                        url = pheverYoutubeUrl;
                        new HandleUrlConnect_ConnectAdapter().execute(url);//calls asyncTask class to try connect to i.execute(url);//calls asyncTask class to try connect to internet
                        break;
                    case 3:
                        url = housemixesUrl;
                        new HandleUrlConnect_ConnectAdapter().execute(url);//calls asyncTask class to try connect to i.execute(url);//calls asyncTask class to try connect to internet
                        break;
                    case 4:
                        url = soundCloudUrl;
                        new HandleUrlConnect_ConnectAdapter().execute(url);//calls asyncTask class to try connect to i
                        break;
                    case 5:
                        url = mixCloudUrl;
                        new HandleUrlConnect_ConnectAdapter().execute(url);//calls asyncTask class to try connect to internet
                        break;
                    case 6:
                        url = pheverAcademyUrl;
                        new HandleUrlConnect_ConnectAdapter().execute(url);//calls asyncTask class to try connect to internet
                        break;
                }//close switch

            }
        });

        return rowView;
    }

    //async task to check internet and server connectivity and advise user if no connect
    //NOTE THIS ASYNC TASK CLASS IS NOT SUITABLE FOR RETURNING DATA - IS ONLY FOR CONNECT TO A URL OUTSIDE THE APP
    private class HandleUrlConnect_ConnectAdapter extends AsyncTask<String, Void, Boolean> {
        ProgressDialog progressUrlConnect;
        String alertMessage;
        int connectStatus = 0;// a 0 indicates no internet connect - a 1 indicates no server connect
        //start a progress dialog advises user that something is happening
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressUrlConnect = ProgressDialog.show(context, "Connecting to Internet", "Please Wait ...", true);
        }//close method onpreexecute

        @Override
        protected Boolean doInBackground(String... urls) {
            //first check there is a connection to intenet (is turned on and in range)
            ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkinfo = connMgr.getActiveNetworkInfo();
            if(networkinfo != null && networkinfo.isConnected()){ //yes internet turned on and in range
                //Log.d("NETWORK_INFO", String.valueOf(networkinfo.isConnected()));
                try{
                    //second check connection to server
                    URL myUrl = new URL(urls[0]);//paras [0] is the url passed into the async task
                    //Log.d("url used is: ", myUrl.toString());
                    URLConnection connection = myUrl.openConnection();
                    connection.setConnectTimeout(500 * 8); //4 seconds
                    connection.connect();
                    return true; //connect made
                }catch(Exception e){ //if connection to server or file cannot be made
                    connectStatus = 1; //server connect issue
                    return false;
                }
            }//close if
            connectStatus = 0; //internet connect issue
            return false;
        }//close method doinbackground

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            progressUrlConnect.dismiss();//close progress dialog if still open
            if(result){ //if connection to url is successfull
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url)); //url here it the url passed in at the asynctask call
                context.startActivity(i);
            }else {  //if connecction not successfull
                if(connectStatus == 0){ //no internet connection (is turned off or out of range)
                   alertMessage = "No internet connect - check is turned on or out of range";
                }else { //yes internet but no connect to server cannot get web file or data
                   alertMessage = "No server connect - may be down try again later";
                }//close second if
                final AlertDialog.Builder statusAlert = new AlertDialog.Builder(context);
                statusAlert.setCancelable(false);
                statusAlert.setTitle("Connect Result");
                statusAlert.setMessage(alertMessage);
                statusAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //when clicked just dismiss dialog
                    }
                });
                AlertDialog alert = statusAlert.create();
                alert.show();

            }//close first if

        }//close method onpost

    }//close class handleurlconnect  (async task)

}//close class connect adapter

