package com.devstream.phever.activities;

import java.io.IOException;
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
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.devstream.phever.model.Slot;
import com.devstream.phever.model.SlotAdapter;
import com.devstream.phever.model.SlotSingleton;
//NOTE WHOLE CLASS INNARDS COMMENTED OUT TILL LOST CODE RECOVERED - gets around constant errors - john will work on this
public class DjScheduleActivity extends Activity {
<<<<<<< HEAD
    /*
	final String rosterUrl = "http://members.upc.ie/john.prendergast4/schedule.json";
	int size = 7;
	

   ArrayList<Slot> dayRoster = new ArrayList<Slot>();
   ArrayList<ArrayList<Slot>> weekRoster = new ArrayList<ArrayList<Slot>>();

  
=======
	final String rosterUrl = "http://phever.ie/db/slots.php";
	final int NUMDAYS = 7;
	final int NUMSLOTS = 12;
	String[] dayNames = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
	ArrayList<ArrayList<Slot>> weekRoster = new ArrayList<ArrayList<Slot>>();
>>>>>>> b0a909131f21bfd291bb37bf0615bc3da11c58f5
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


		SlotSingleton instance = SlotSingleton.getInstance();
		if (instance.getUpdated()) {
			weekRoster = instance.getWeekRoster();
		}
		else {
			new getSlots().execute(rosterUrl);
			instance.updateLocal(this);
			instance.setWeekRoster(weekRoster);
		}

		ListView listview = (ListView)findViewById(R.id.list);
		ArrayList<Slot> dayRoster = weekRoster.get(index);
		adapter = new SlotAdapter(getApplicationContext(),R.layout.row,dayRoster);
		listview.setAdapter(adapter);
<<<<<<< HEAD

=======
>>>>>>> b0a909131f21bfd291bb37bf0615bc3da11c58f5
	}

	class getSlots extends AsyncTask<String, Void, Boolean > {
		ProgressDialog dialog;		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(DjScheduleActivity.this);
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

<<<<<<< HEAD
					    //   jp j p    ---------------------------------------  weekRoster.add(i, new ArrayList<Slot>()); 
						Iterator<String> keys = object.keys();
						//Log.d("jp01", "obj=" + object);
						//Log.d("jp01", "keys=" + keys);

						while(keys.hasNext()) {
							String timeSlot = (String)keys.next();
							String show = object.getString(timeSlot);						
							//Slot anySlot = new Slot(timeSlot, show);
                            Slot anySlot = new Slot(show);

   							//Log.d("jp01", anySlot.toString());
							//dayRoster.add(anySlot);
							//dayRoster = weekRoster.get(i);
						    weekRoster.get(i).add(anySlot); 			    
						}
						//Sorting
						 Collections.sort(weekRoster.get(i), new Comparator<Slot>() {
						         @Override
						         public int compare(Slot  Slot1, Slot  Slot2)
						         {
						             return  Slot1.getShowName().compareTo(Slot2.getShowName());
						         }
						     });
							Log.d("jp02", "weekRoster2=" + weekRoster.get(i) );
							for (ArrayList<Slot> innerList : weekRoster) {
							    for (Slot strng : innerList) {
							        // do stuff with string
							    	Log.d("jp03", "weekRoster3============================" + strng.toString() );
							    }
							}
=======
>>>>>>> b0a909131f21bfd291bb37bf0615bc3da11c58f5
					}
					//Sort each day using the show number
					for (int i = 0; i < NUMDAYS; i++)
						Collections.sort(weekRoster.get(i), new Comparator<Slot>() {
							@Override
							public int compare(Slot Slot1, Slot Slot2) {
								return Slot1.getShowNumber() - Slot2.getShowNumber(); // Ascending
							}
						});

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
			if(!result)
				Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();
		}
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
	}
    */
}

		/*
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				// TODO Auto-generated method stub
				//jp   Toast.makeText(getApplicationContext(), dayRoster.get(position).getShowName(), Toast.LENGTH_LONG).show();
			}
		});
		*/
