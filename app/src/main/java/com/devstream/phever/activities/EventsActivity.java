package com.devstream.phever.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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



    ArrayList<Event> eventsList = new ArrayList<Event>();


    EventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_row);
        Intent intent = getIntent();



        EventSingleton instance = EventSingleton.getInstance();
        if (instance.getUpdated()) {
            eventsList = instance.getEvents();
        } else {
            new getEvents().execute(eventsUrl);
            instance.updateLocal(this);
            instance.setEvents(eventsList);
        }

        ListView listview = (ListView) findViewById(R.id.list);

        adapter = new EventAdapter(getApplicationContext(), R.layout.row, eventsList);
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
                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

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
                        private Text terms;
[{"id":"1","edate":"2015-07-01","etime":"20:00:00","name":"ganstagrass","location":"the pond",
"headline":"dave alvin","headline_desc":"bla bla bla","price":"too dear","purchase":"purchase ",
"support_acts":"a\r\nb\r\nc\r\nd\r\ne\r\nf\r\ng\r\nh\r\ni\r\nj\r\nk",
"terms":"t\r\ne\r\nr\r\nm\r\ns\r\n&\r\nc\r\no\r\nn\r\nd\r\ns","modified":"2015-04-26 22:35:33"}]					 */
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject object = jarray.getJSONObject(i);
                        Event anyEvent = new Event();
                        anyEvent.setEdate(object.optString("edate"));
                        anyEvent.setName(object.optString("name"));
                        anyEvent.setLocation(object.optString("location"));
                        anyEvent.setHeadline(object.optString("headline"));
                        anyEvent.setHeadlineDesc(object.optString("headline_desc"));

                        //Sorting
                        /*
                        Collections.sort(events.get(dayIndex), new Comparator<Event>() {

                            @Override
                            public int compare(Event Event1, Event Event2) {
                                return Event1.getShowNumber() - Event2.getShowNumber(); // Ascending
                            }
                        });
                        */
                    }
                    //Log.d("jp01", "week roster=" + events );
                    return true;
                }

            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            //dialog.cancel();
            dialog.dismiss();
            adapter.notifyDataSetChanged();
            if (result == false)
                Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();

        }

    }

}
