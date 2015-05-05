package com.devstream.phever.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TableRow;

public class ConnectActivity extends Activity implements View.OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connect);

        TableRow row1 = (TableRow)findViewById(R.id.row1);
        row1.setOnClickListener(this);
        TableRow row2 = (TableRow)findViewById(R.id.row2);
        row2.setOnClickListener(this);
        TableRow row3 = (TableRow)findViewById(R.id.row3);
        row3.setOnClickListener(this);
        TableRow row4 = (TableRow)findViewById(R.id.row4);
        row4.setOnClickListener(this);
        TableRow row5 = (TableRow)findViewById(R.id.row5);
        row5.setOnClickListener(this);
        TableRow row6 = (TableRow)findViewById(R.id.row6);
        row6.setOnClickListener(this);
	}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.row1:
                String url1 =  "https://www.facebook.com/phevermusic";
                Intent intent1 = new Intent(Intent.ACTION_VIEW);
                intent1.setData(Uri.parse(url1));
                startActivity(intent1);
                break;
            case R.id.row2:
                String url2 =  "http://twitter.com/PHEVER_Events";
                Intent intent2 = new Intent(Intent.ACTION_VIEW);
                intent2.setData(Uri.parse(url2));
                startActivity(intent2);
                break;
            case R.id.row3:
                String url3 = "https://www.youtube.com/PHEVERIRL";
                Intent intent3 = new Intent(Intent.ACTION_VIEW);
                intent3.setData(Uri.parse(url3));
                startActivity(intent3);
                break;
            case R.id.row4:
                String url4 = "http://www.house-mixes.com/profile/phever-radio";
                Intent intent4 = new Intent(Intent.ACTION_VIEW);
                intent4.setData(Uri.parse(url4));
                startActivity(intent4);
                break;
            case R.id.row5:
                String url5 = "https://soundcloud.com/pheverie";
                Intent intent5 = new Intent(Intent.ACTION_VIEW);
                intent5.setData(Uri.parse(url5));
                startActivity(intent5);
                break;
            case R.id.row6:
                String url6 = "https://www.mixcloud.com/PHEVER_Radio";
                Intent intent6 = new Intent(Intent.ACTION_VIEW);
                intent6.setData(Uri.parse(url6));
                startActivity(intent6);
                break;
        }
    }

}//close class SocialLinks
