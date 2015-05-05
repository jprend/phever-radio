package com.devstream.phever.model;

import android.content.Context;

import java.util.ArrayList;

public class EventSingleton {

	private boolean updated;

	private ArrayList<Event> events = new ArrayList<Event>();

	private static EventSingleton instance = null;

	protected EventSingleton() {
	}
 
	// Lazy Initialization (If required then only)
	public static synchronized EventSingleton getInstance() {
		if (instance == null) {
			// Thread Safe. Might be costly operation in some case
					instance = new EventSingleton();
		}
		return instance;
	}
	public boolean getUpdated(){
		return updated;
	}
	
	public void updateLocal(Context context){
		updated = true;
	}

    public ArrayList<Event> getEvents() {
		return events;
	}

	public void setEvents(ArrayList<Event> events) {
		this.events = events;
	}

}