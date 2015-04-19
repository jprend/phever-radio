package com.devstream.phever.model;

import android.util.Log;

public class Slot {
	private String  time;
	private String  showName;
	private String  dj;
	private String  djDesc;
	private String  imageName;

	public Slot ()
	{}
	
	public Slot(String time, String inline) {
		super();
		this.time = time;
		this.imageName = "logo_small.jpg";
		String[] lines = inline.split("\n");
		Log.d("jp06", lines.toString());
		for( int i =0; i<lines.length; i++) {
			switch (i) {
			case 0:	
					if (lines[0].length() > 3 ){
						this.showName = lines[0].substring(3);   //cut off the "[00 ]Monday"
					}
					else {
						this.showName="";
					}
            		break;
			case 1: this.dj = lines[1];
    				break;
			case 2:	this.djDesc = lines[2];
    				break;
			case 3: this.imageName = lines[3];
    				break;
			}
		}
	}
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getShowName() {
		return showName;
	}
	public void setShowName(String showName) {
		this.showName = showName;
	}
	public String getDj() {
		return dj;
	}
	public void setDj(String dj) {
		this.dj = dj;
	}
	public String getDjDesc() {
		return djDesc;
	}
	public void setDjDesc(String djDesc) {
		this.djDesc = djDesc;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	@Override
	public String toString() {
		return "Slot [time=" + time + ", showName=" + showName + ", dj=" + dj
				+ ", djDesc=" + djDesc + ", imageName=" + imageName + "]";
	}


}


