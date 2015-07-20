package com.devstream.phever.activities;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;


import com.devstream.phever.model.Slot;
import com.devstream.phever.model.SlotAdapter;
import com.devstream.phever.model.SlotSingleton;
import com.devstream.phever.utilities.GeneralAlertDialog;

public class DjScheduleActivity extends Activity implements GeneralAlertDialog.NoticeDialogListener {
    final String rosterUrl = "http://phever.ie/db/slots.php";
    final int NUMDAYS = 7;
    String[] dayNames = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    ArrayList<ArrayList<Slot>> weekRoster = new ArrayList<ArrayList<Slot>>();
    SlotAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dj_schedule);
        Intent intent = getIntent();
        int index = intent.getExtras().getInt("day");
        //Create new instances of arraylists for each day -very important
        for (int i = 0; i < NUMDAYS; i++) {
            weekRoster.add(i, new ArrayList<Slot>());
        }

        TextView daySelected = (TextView) findViewById(R.id.tvDay);
        daySelected.setText(dayNames[index]);

        SlotSingleton instance = SlotSingleton.getInstance();
        if (instance.getUpdated()) {
            weekRoster = instance.getWeekRoster();
        }
        else {
            new getSlots().execute(rosterUrl);
        }

        ListView listview = (ListView)findViewById(R.id.list);
        ArrayList<Slot> dayRoster = weekRoster.get(index);
        //adapter = new SlotAdapter(getApplicationContext(),R.layout.row_slot,dayRoster);
        adapter = new SlotAdapter(getApplicationContext(),dayRoster);
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

    class getSlots extends AsyncTask<String, Void, Boolean > {
        ProgressDialog dialog;
        int connectStatus = 0;// a 0 indicates no internet connect - a 1 indicates no server connect
        //start a progress dialog advises user that something is happening
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(DjScheduleActivity.this);
            dialog.setMessage("Loading ... please wait!");
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
                //Log.d("NETWORK_INFO", String.valueOf(networkinfo.isConnected()));
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
				/*	number	:	2
					sday	:	Monday
					stime	:	12-2pm,Lunchtimes
					genre	:	All Things House
					dj_name	:	House Hatcher
					show_title	:	House Hatcher's Grooves
					dj_image	:	null
					modified	:	2015-04-20 14:29:47 */
                    int dayIndex;
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject object = jarray.getJSONObject(i);
                        Slot anySlot = new Slot();
                        anySlot.setShowNumber(Integer.valueOf(object.optString("number")));
                        anySlot.setSday(object.optString("sday"));
                        anySlot.setStime(object.optString("stime"));
                        anySlot.setGenre(object.optString("genre"));
                        anySlot.setDjName(object.optString("dj_name"));
                        anySlot.setShowTitle(object.optString("show_title"));
                        anySlot.setDjImage(object.optString("dj_image"));

                        dayIndex=getDayIndex(anySlot.getSday());
                        weekRoster.get(dayIndex).add(anySlot);

                    }
                    //Sort each day using the show number
                    for (int i = 0; i < NUMDAYS; i++)
                        Collections.sort(weekRoster.get(i), new Comparator<Slot>() {
                            @Override
                            public int compare(Slot Slot1, Slot Slot2) {
                                return Slot1.getShowNumber() - Slot2.getShowNumber(); // Ascending
                            }
                        });

                    SlotSingleton.getInstance().updateLocal(DjScheduleActivity.this);
                    SlotSingleton.getInstance().setWeekRoster(weekRoster);
                    return true; //connection made
                }

            }catch(Exception e){ //if connection to server or file cannot be made
                connectStatus = 1; //server connect issue
                return false;
            }
            }//close if
            connectStatus = 0; //internet connect issue
            return false;
        }//close method doinbackground

        protected void onPostExecute(Boolean result) {
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

        private int getDayIndex(String sday) {
            int j = 0;
            for (int i = 0; i < dayNames.length; i++) {
                if (dayNames[i].equals(sday)) j = i;
            }
            return j;
        }
		/*
		public static <T> int indexOf(T needle, T[] haystack)
		{
			for (int i=0; i<haystack.length; i++)
			{
				if (haystack[i] != null && haystack[i].equals(needle)
						|| needle == null && haystack[i] == null) return i;
			}

			return -1;
		}
		*/
    }//close class get slots

}//close class DJScheduleActivity

