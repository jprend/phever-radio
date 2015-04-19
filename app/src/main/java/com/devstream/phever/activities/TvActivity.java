package com.devstream.phever.activities;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TvActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tv);
		
		WebView webViewTv = (WebView) findViewById(R.id.webViewTv);
		WebSettings webViewSettings = webViewTv.getSettings();
		webViewTv.setWebViewClient(new WebViewClient());
		webViewSettings.setJavaScriptEnabled(true);
		webViewSettings.setBuiltInZoomControls(true);
		
		//loadUrl
		String data = "http://new.livestream.com/accounts/10782842/events/3605123/videos/82404157/player?autoPlay=false&height=360&mute=false&width=640";
		webViewTv.loadUrl(data);
		
		/*
		//loadData
		String data = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">";
		data += "<html><head><META http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"></head><body>";
		data += "<div style=\"border:0px solid rgb(201,0,1);overflow:hidden;margin:15px auto;width:650px;height:1800\">";
	    data += "<iframe src=\"http://new.livestream.com/accounts/10782842/events/3605123/videos/82404157/player?autoPlay=false&height=360&mute=false&width=640\" width=\"640\" height=\"360\" frameborder=\"0\" scrolling=\"no\"></iframe>";
		data += "</div></body></html>";
		// args: data, mimeType, encoding
		 * webViewTv.loadData(data, "text/html", "UTF-8");	
		 */
		
		

	}
}
