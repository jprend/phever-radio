package com.devstream.phever.model;

import android.graphics.Bitmap;
import android.util.Log;

public class Slot {
	private int		showNumber;
	private String  sday;
	private String  stime;
	private String  genre;
	private String  djName;
	private String  showTitle;
	private String  djImage;
	private Bitmap  djBmp;


	public String getSday() {
		return sday;
	}

	public void setSday(String sday) {
		this.sday = sday;
	}

	public String getDjName() {
		return djName;
	}

	public void setDjName(String djName) {
		this.djName = djName;
	}

	public int getShowNumber() {
		return showNumber;
	}

	public void setShowNumber(int showNumber) {
		this.showNumber = showNumber;
	}

	public String getStime() {
		return stime;
	}

	public void setStime(String stime) {
		this.stime = stime;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getShowTitle() {
		return showTitle;
	}

	public void setShowTitle(String showTitle) {
		this.showTitle = showTitle;
	}

	public String getDjImage() {
		return djImage;
	}

	public void setDjImage(String djImage) {
		this.djImage = djImage;
	}

	public Bitmap getDjBmp() {return djBmp;}

	public void setDjBmp(Bitmap djBmp) {this.djBmp = djBmp;	}


}


