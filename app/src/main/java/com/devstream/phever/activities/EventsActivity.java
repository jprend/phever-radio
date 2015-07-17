package com.devstream.phever.activities;

import java.io.IOException;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.devstream.phever.model.Event;
import com.devstream.phever.model.EventAdapter;
import com.devstream.phever.model.EventSingleton;
import com.devstream.phever.utilities.GeneralAlertDialog;

public class EventsActivity extends Activity implements GeneralAlertDialog.NoticeDialogListener {
    final String eventsUrl = "http://phever.ie/db/events.php";
    ArrayList<Event> eventList;
    EventAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        eventList = new ArrayList<>();


        EventSingleton instance = EventSingleton.getInstance();
        if (instance.getUpdated()) {
            eventList = instance.getEvents();
        } else {
            new getEvents().execute(eventsUrl);
        }

        //new getEvents().execute(eventsUrl);

        //Log.d("jp01", "eventList size 1 =" + eventList.size());

        ListView listview = (ListView) findViewById(R.id.list);
        adapter = new EventAdapter(getApplicationContext(), eventList);
        //adapter = new EventAdapter(this, R.layout.row_event, eventList);
        //Log.d("jp01", "eventList size 2 =" + eventList.size());

        listview.setAdapter(adapter);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, int ok_option) {
        switch(ok_option){
            case 0:
                finish();
                break;
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog, int cancel_option) {

    }

    class getEvents extends AsyncTask<String, Void, Boolean> {
        ProgressDialog dialog;
        int connectStatus = 0;// a 0 indicates no internet connect - a 1 indicates no server connect
        //start a progress dialog advises user that something is happening

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(EventsActivity.this);
            dialog.setMessage("Loading... please wait!");
            dialog.setTitle("Connecting to server");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            //first check there is a connection to intenet (is turned on and in range)
            ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkinfo = connMgr.getActiveNetworkInfo();
            if(networkinfo != null && networkinfo.isConnected()){ //yes internet turned on and in range
                Log.d("NETWORK_INFO", String.valueOf(networkinfo.isConnected()));
            try {
                HttpGet httppost = new HttpGet(urls[0]);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);
                StatusLine stat = response.getStatusLine();
                int status = stat.getStatusCode();
                //Log.d("jp01", "status =" + status);

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);
                    //Log.d("jp01", "data =" + data);

                    JSONArray jarray = new JSONArray(data);
                    /*
					    private String  edate;  edate   name location    headline    headline_desc
                        private String  etime;
                        private String  name;
                        private String  location;
                        private String  headline;
                        private String  headlineDesc;
                        private String  price;
                        private String  purchase;
                        private Text supportActs;
                        private Text terms; */
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject object = jarray.getJSONObject(i);
                        Event anyEvent = new Event();
                        String edate = object.optString("edate");
                        //SimpleDateFormat sdf = new SimpleDateFormat("d'%s' MMM, yyyy");
                        //String myDate = String.format(sdf.format(date), Util.dateSuffix(date));
                        anyEvent.setEdate(edate);
                        anyEvent.setName(object.optString("name"));
                        anyEvent.setLocation(object.optString("location"));
                        anyEvent.setHeadline(object.optString("headline"));
                        anyEvent.setHeadlineDesc(object.optString("headline_desc"));
                        anyEvent.setPrice(object.optString("price"));
                        anyEvent.setPurchase(object.optString("purchase"));
                        anyEvent.setSupportActs(object.optString("support_acts"));
                        anyEvent.setTerms(object.optString("terms"));
                        anyEvent.setHeadlineDesc(object.optString("image_url"));
                        eventList.add(anyEvent);
                        //Log.d("jp01", i + "event name ="  + anyEvent.getName());

                    }
                    //Sorting
                    Collections.sort(eventList, new Comparator<Event>() {
                        @Override
                        public int compare(Event Event1, Event Event2) {
                            //Ascending
                            return Event1.getEdate().compareTo(Event2.getEdate());
                        }
                    });

                    EventSingleton.getInstance().updateLocal(EventsActivity.this);
                    EventSingleton.getInstance().setEvents(eventList);
                    return true;
                }
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
            //Log.d("jp01", "eventList size 4 =" + eventList.size());
            dialog.dismiss();//dialog.cancel();
            adapter.notifyDataSetChanged();

            if(!result) { //no connection
                if(connectStatus == 0){ //no internet connection (is turned off or out of range)
                    GeneralAlertDialog myAlert = GeneralAlertDialog.newInstance("Cannot Connect to Intenet", "Check internet turned on / in range", false, true, 0);
                    myAlert.show(getFragmentManager(), "no internet connect");
                }else { //yes internet but no connect to server cannot get web file or data
                    GeneralAlertDialog myAlert = GeneralAlertDialog.newInstance("Cannot Connect to Server", "Server may be down try again later", false, true, 0);
                    myAlert.show(getFragmentManager(), "no server connect");
                }//close second if
            }//close first if

        }//close method onPostExecute

    }//close class getEvntsh adync task

}//close class EventsActivity
