package com.devstream.phever.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.devstream.phever.activities.ConnectActivity;
import com.devstream.phever.activities.DjScheduleActivity;
import com.devstream.phever.activities.HomeActivity;
import com.devstream.phever.activities.R;//makes this utility class visible in all activity classes

import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Chris on 19/06/2015.
 */
//http://developer.android.com/guide/topics/ui/dialogs.html
/* this dialog class is called as in these two lines:-

  GeneralAlertDialog myAlert = GeneralAlertDialog.newInstance("any title", "any message", true, true, 0);
  myAlert.show(getFragmentManager(), "any ref tag name eg. Tag0"); // the tab name is for referencing this instance if required

  parameters are in order:-
  string title = any text
  string  message = any text
  boolean negative button (cancel) = true for show - false for not show
  boolean positive button (ok) = true for show - false for not show
  int switch block in positive button = can be any int  of choice (here 0 is default just closes dialog)
 */
public class GeneralAlertDialog extends DialogFragment {
    private String title;
    private String message;
    private boolean cancel;
    private boolean ok;
    private int action;
    private String url; //general string for any url needed in the async task

    //note cannot use a custom constructor with any fragment so need this static method to pass data into the dialog
    public static GeneralAlertDialog newInstance(String title, String message, boolean cancel, boolean ok, int action) {
        GeneralAlertDialog dialog = new GeneralAlertDialog();
        dialog.title = title;
        dialog.message = message;
        dialog.cancel = cancel;
        dialog.ok = ok;
        dialog.action = action;

        return dialog;
    }

    /* any activity (the hosting activity) that creates an instance of this dialog fragment must also
    * implement this interface in order to receive event callbacks from the positive, negative or neutral buttons.
    * INTERFACE
    */
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, int ok_option);
        public void onDialogNegativeClick(DialogFragment dialog, int cancel_option);
    }

    // INSTANCE OF INTERFACE
    NoticeDialogListener mListener;

    // INSTANTIATE THE INTERFACE Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    //ALSO CHECKS IF THE HOSTING ACTIVITY HAS IMPLEMENTED THE INTERFACE
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        setRetainInstance(true); //very important for the use of the static method newInstance
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);//will not close dialog if  clicking anywhere around it (true does close it)
        builder.setTitle(title);

        if((message != null) && (!message.isEmpty())){
            builder.setMessage(message);
        } else {
            builder.setItems(R.array.days_array, new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //int day = which;
                    //Toast.makeText(getActivity(), "selected item is " + which, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity(), DjScheduleActivity.class);
                    intent.putExtra("day", which);
                    startActivity(intent);
                }
            });
        }

         if(cancel) {
            builder.setNegativeButton(R.string.text_negativebutton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Send the negative button event back to the host activity
                    mListener.onDialogNegativeClick(GeneralAlertDialog.this, 0);
                }
            });
        }

        if(ok) {
            builder.setPositiveButton(R.string.text_positivebutton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                       // Send the positive button event back to the host activity
                        mListener.onDialogPositiveClick(GeneralAlertDialog.this, action);
                }
            });
        }

        Dialog dialog = builder.create(); // must go here after setup
        return dialog;
    }

}//close class GeneralAlertDialog
