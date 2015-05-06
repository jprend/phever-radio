package com.devstream.phever.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class TvActivity extends Activity {

    String vidAddress = "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri uri  = Uri.parse("http://livestream.com/accounts/10782842/TV");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }//close method oncreate

}//close class tvactivity
