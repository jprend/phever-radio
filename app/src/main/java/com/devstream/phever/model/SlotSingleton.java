package com.devstream.phever.model;

import java.util.ArrayList;

import android.content.Context;

public class SlotSingleton {

	private boolean updated;

	private ArrayList<ArrayList<Slot>> weekRoster = new ArrayList<ArrayList<Slot>>();
    
	private static SlotSingleton instance = null;
 
	protected SlotSingleton() {
	}
 
	// Lazy Initialization (If required then only)
	public static synchronized SlotSingleton getInstance() {
		if (instance == null) {
			// Thread Safe. Might be costly operation in some case
					instance = new SlotSingleton();
		}
		return instance;
	}
	public boolean getUpdated(){
		return updated;
	}
	
	public void updateLocal(Context context){
		updated = true;
	}

    public ArrayList<ArrayList<Slot>> getWeekRoster() {
		return weekRoster;
	}

	public void setWeekRoster(ArrayList<ArrayList<Slot>> weekRoster) {
		this.weekRoster = weekRoster;
	}

}