package com.devstream.phever.activities;

import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


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
import android.app.ProgressDialog;

import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.devstream.phever.model.Event;
import com.devstream.phever.model.EventAdapter;
import com.devstream.phever.model.EventSingleton;

public class EventsActivity extends Activity {
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
            instance.updateLocal(this);
            instance.setEvents(eventList);
        }

        //new getEvents().execute(eventsUrl);

        //Log.d("jp01", "eventList size 1 =" + eventList.size());

        ListView listview = (ListView) findViewById(R.id.list);
        adapter = new EventAdapter(getApplicationContext(), R.layout.row_event, eventList);
        //adapter = new EventAdapter(this, R.layout.row_event, eventList);
        Log.d("jp01", "eventList size 2 =" + eventList.size());

        listview.setAdapter(adapter);
    }

    class getEvents extends AsyncTask<String, Void, Boolean> {
        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(EventsActivity.this);
            dialog.setMessage("Loading, please wait");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                HttpGet httppost = new HttpGet(urls[0]);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);
                StatusLine stat = response.getStatusLine();
                int status = stat.getStatusCode();
                Log.d("jp01", "status =" + status);

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);
                    Log.d("jp01", "data =" + data);

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
                        anyEvent.setEdate(dateConvert(edate));
                        anyEvent.setName(object.optString("name"));
                        anyEvent.setLocation(object.optString("location"));
                        anyEvent.setHeadline(object.optString("headline"));
                        anyEvent.setHeadlineDesc(object.optString("headline_desc"));
                        eventList.add(anyEvent);
                        Log.d("jp01", i + "event name ="  + anyEvent.getName());
                        //Sorting
                        Collections.sort(eventList, new Comparator<Event>() {
                            @Override
                            public int compare(Event Event1, Event Event2) {
                                //Ascending
                                return Event1.getEdate().compareTo(Event2.getEdate());
                            }
                        });
                    }
                    return true;
                }
            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            //dialog.cancel();
            Log.d("jp01", "eventList size 4 =" + eventList.size());
            dialog.dismiss();
            adapter.notifyDataSetChanged();
            if (!result)
                Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();
        }

    }



    public String dateConvert(String D){

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = null;
        try {
            date = format1.parse(D);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        String dateString = format2.format(date);
        dateString = dateString.replace("-", " ");
        System.out.println(dateString);
        return ((dateString));
    }
}
