package com.devstream.phever.utilities;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devstream.phever.activities.R;

/**
 * Created by Chris on 17/09/2015.
 */
public class CustomToast {

    //constructor
    public CustomToast(Context context, String message){
        Toast custom_toast = new Toast(context);
        LinearLayout toastLayout =  new LinearLayout(context);
        toastLayout.setOrientation(LinearLayout.HORIZONTAL);
        //toastLayout.setBackgroundColor(Color.BLUE);  //can change to setredource of shape
        toastLayout.setBackgroundResource(R.drawable.custom_toast_background);
        toastLayout.setPadding(15, 40, 15, 40);
        ImageView toastImage = new ImageView(context);
        TextView toastText = new TextView(context);
        toastText.setTextColor(Color.rgb(33,205,207));
        toastText.setTextSize(16);
        toastText.setText(message);
        //toastImage.setImageResource(R.drawable.ic_launcher);
        custom_toast.setGravity(Gravity.CENTER, 0, 0);
        toastLayout.addView(toastText);
        toastLayout.addView(toastImage);
        custom_toast.setView(toastLayout);
        custom_toast.setDuration(Toast.LENGTH_SHORT);
        custom_toast.show();
    }

}//close class CustomToast
