package com.devstream.phever.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;

import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.devstream.phever.utilities.ColorTool;
import com.devstream.phever.utilities.GeneralAlertDialog;
import com.devstream.phever.utilities.SoundwaveAnimateThread;
import com.devstream.phever.utilities.SplashAnimateThread;

import java.net.URL;
import java.net.URLConnection;

import static android.media.AudioManager.*;


public class HomeActivity extends Activity implements View.OnClickListener,  OnTouchListener, GeneralAlertDialog.NoticeDialogListener, OnAudioFocusChangeListener {
    private final static String pheverRadioUrlconnect =  "http://89.101.1.140:8003/"; //url to connect to radio streaming server
    private final static String pheverTvUrlConnect = "http://livestream.com/accounts/10782842/TV"; //url to connect phever tv
    private final static String pheverEmailUrlConnect = "https://docs.google.com/forms/d/1op3yEBANTh7_QDTMDW-bygsiLwH1uQgsSAJiffznssU/viewform"; //url to connect phever google email database
    private final static String pheverWebsiteUrlConnect = "http://phever.ie"; // url to connect to phever website
    private final static String PHEVER_URLS = "com.devstream.phever.activities.phever_urls";
    private final static String PHEVER_RADIO_URL = "com.devstream.phever.activities.phever_radio_url";
    private final static String HOME_BACKGROUND_COLOR = "com.devstream.phever.activities.homeBackgroundColor"; //shared prefs file for home layount background color
    private final static String BACKGROUND_COLOR = "com.devstream.phever.activities.homeBackgroundColor.backgroundColor"; //key for background color
    private PopupMenu popupMenuColorSettings;
    private final static int ONE = 1;
    private final static int TWO = 2;
    private final static int THREE = 3;
    private final static int FOUR = 4;
    private final static int FIVE = 5;
    private final static int SIX = 6;
    private final static int SEVEN = 7;
    private final static int EIGHT = 8;
    private final static int NINE = 9;
    private final static int TEN = 10;
    private final static int ELEVEN = 11;
    private final static int TWELVE = 12;
    private final static int THIRTEEN = 13;
    private final static int FOURTEEN = 14;
    private final static int FIFTEEN = 15;
    private final static int SIXTEEN = 16;
    private final static int SEVENTEEN = 17;
    private String url;
	private ImageView soundwaveAnimate, playRadio, pauseRadio, soundwaveRotate;
    private SoundwaveAnimateThread swAnim;

	private Intent intent;

	ToggleButton playPauseButton;

	static Context context;
	boolean isPlaying;//the boolean which indicates radio playing or not which gets saved in shared preferences - its set in the service
	boolean radio = false;
	Intent streamService;
	SharedPreferences prefs;  //create instance of shared preference for boolean isPlaying which is set by service
    SharedPreferences.Editor edit; //set preferences key/value for  boolean isPlaying
    SharedPreferences urlPrefs; // create instance of shared preferences for radio url so can be accessed by service
    SharedPreferences.Editor editUrlPrefs; // set preferences key/value for radio url  which ca be accessed by service



	AudioManager audioManager;
	ComponentName RemoteControlReceiver;
    Drawable    soundwaveImage;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		context = this;

        streamService = new Intent(this, StreamService.class);

        //sets the  user chosen home layout background color in shared preferences
        int defaultColor = getResources().getColor(R.color.color_white);//very first time default is white
        prefs = getSharedPreferences(HOME_BACKGROUND_COLOR, MODE_PRIVATE ); // get the required shared prefs file
        int background = prefs.getInt(BACKGROUND_COLOR, defaultColor); // if nothing in prefs then set to default
        //check if background is a color or an image and set the chosen color or image
        if((background == getResources().getColor(R.color.color_white)) || (background == getResources().getColor(R.color.color_black)) ||
                (background == getResources().getColor(R.color.color_green)) || (background == getResources().getColor(R.color.color_blue)) ||
                (background == getResources().getColor(R.color.color_blue)) ||(background == getResources().getColor(R.color.color_red)) ||
                (background == getResources().getColor(R.color.color_pink)) || (background == getResources().getColor(R.color.color_purple)) ||
                (background == getResources().getColor(R.color.color_yellow))) {
            findViewById(R.id.main_layout).setBackgroundColor(background); // set color
        } else {
            findViewById(R.id.main_layout).setBackgroundResource(background); //set image
        }

        playPauseButton = (ToggleButton) findViewById(R.id.playPauseButton);

        urlPrefs = getSharedPreferences(PHEVER_URLS, Context.MODE_PRIVATE); //pass the radio streaming url to the service
        editUrlPrefs = urlPrefs.edit();
        editUrlPrefs.putString(PHEVER_RADIO_URL, pheverRadioUrlconnect);
        editUrlPrefs.commit();

        //do a rotation of home image then set ontouch listener to the visible home image
		ImageView iv = (ImageView) findViewById(R.id.img_home);
        SplashAnimateThread rotator = new SplashAnimateThread(iv);
        rotator.run();
		if (iv != null) {
			iv.setOnTouchListener(this);
		}

        //set up the sound wave animation
        soundwaveRotate  = (ImageView) findViewById(R.id.soundwave_img_animate); // soundwave image
        soundwaveImage = soundwaveRotate.getDrawable();
        swAnim = new SoundwaveAnimateThread(soundwaveRotate);  //soundwave thread class

        //footer change home view background color
        findViewById(R.id.change_background_color).setOnClickListener(this);

        //footer phever link
        TextView phever_link = (TextView)findViewById(R.id.phever_weblink);
        SpannableString content = new SpannableString(getResources().getString(R.string.footer_text));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        phever_link.setText(content);
        phever_link.setOnClickListener(this);

        //footer database link
        ImageView subscribe = (ImageView)findViewById(R.id.mail_list_link);
        subscribe.setOnClickListener(this);
		
		/*
		//frame animate 
		soundwaveAnimate = (ImageView)findViewById(R.id.imageAnimation);
		// Setting animation_list.xml as the background of the image view
		soundwaveAnimate.setBackgroundResource(R.drawable.frame_animation_soundwave);
		SoundwaveAnimateThread frameAnim = new SoundwaveAnimateThread(soundwaveAnimate);
		frameAnim.run();
		*/
		
	}//close method onCreate

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.change_background_color:
                popupMenuColorSettings = new PopupMenu(HomeActivity.this, findViewById(R.id.change_background_color));
                popupMenuColorSettings.getMenu().add(Menu.NONE, ONE, Menu.NONE, "Black");
                popupMenuColorSettings.getMenu().add(Menu.NONE, TWO, Menu.NONE, "White");
                popupMenuColorSettings.getMenu().add(Menu.NONE, THREE, Menu.NONE, "Green");
                popupMenuColorSettings.getMenu().add(Menu.NONE, FOUR , Menu.NONE, "Blue");
                popupMenuColorSettings.getMenu().add(Menu.NONE, FIVE, Menu.NONE, "Red");
                popupMenuColorSettings.getMenu().add(Menu.NONE, SIX, Menu.NONE, "Pink");
                popupMenuColorSettings.getMenu().add(Menu.NONE, SEVEN, Menu.NONE, "Purple");
                popupMenuColorSettings.getMenu().add(Menu.NONE, EIGHT, Menu.NONE, "Yellow");
                popupMenuColorSettings.getMenu().add(Menu.NONE, NINE, Menu.NONE, "Image 1");
                popupMenuColorSettings.getMenu().add(Menu.NONE, TEN, Menu.NONE, "Image 2");
                popupMenuColorSettings.getMenu().add(Menu.NONE, ELEVEN, Menu.NONE, "Image 3");
                popupMenuColorSettings.getMenu().add(Menu.NONE, TWELVE, Menu.NONE, "Image 4");
                popupMenuColorSettings.getMenu().add(Menu.NONE, THIRTEEN, Menu.NONE, "Image 5");
                popupMenuColorSettings.getMenu().add(Menu.NONE, FOURTEEN, Menu.NONE, "Image 6");
                popupMenuColorSettings.getMenu().add(Menu.NONE, FIFTEEN, Menu.NONE, "Image 7");
                popupMenuColorSettings.getMenu().add(Menu.NONE, SIXTEEN, Menu.NONE, "Image 8");
                popupMenuColorSettings.getMenu().add(Menu.NONE, SEVENTEEN, Menu.NONE, "Image 9");
                popupMenuColorSettings.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
                      @Override
                      public boolean onMenuItemClick(MenuItem item) {
                          switch (item.getItemId()) {
                              case ONE:
                                  findViewById(R.id.main_layout).setBackgroundColor(getResources().getColor(R.color.color_black));
                                  //store chosen color in shared prefs
                                  prefs = getSharedPreferences(HOME_BACKGROUND_COLOR, MODE_PRIVATE );
                                  edit = prefs.edit();
                                  edit.putInt(BACKGROUND_COLOR,getResources().getColor(R.color.color_black));
                                  edit.commit();
                                  break;
                              case TWO:
                                  findViewById(R.id.main_layout).setBackgroundColor(getResources().getColor(R.color.color_white));
                                  //store chosen color in shared prefs
                                  prefs = getSharedPreferences(HOME_BACKGROUND_COLOR, MODE_PRIVATE );
                                  edit = prefs.edit();
                                  edit.putInt(BACKGROUND_COLOR,getResources().getColor(R.color.color_white));
                                  edit.commit();
                                  break;
                              case THREE:
                                  findViewById(R.id.main_layout).setBackgroundColor(getResources().getColor(R.color.color_green));
                                  //store chosen color in shared prefs
                                  prefs = getSharedPreferences(HOME_BACKGROUND_COLOR, MODE_PRIVATE );
                                  edit = prefs.edit();
                                  edit.putInt(BACKGROUND_COLOR,getResources().getColor(R.color.color_green));
                                  edit.commit();
                                  break;
                              case FOUR:
                                  findViewById(R.id.main_layout).setBackgroundColor(getResources().getColor(R.color.color_blue));
                                  //store chosen color in shared prefs
                                  prefs = getSharedPreferences(HOME_BACKGROUND_COLOR, MODE_PRIVATE );
                                  edit = prefs.edit();
                                  edit.putInt(BACKGROUND_COLOR,getResources().getColor(R.color.color_blue));
                                  edit.commit();
                                  break;
                              case FIVE:
                                  findViewById(R.id.main_layout).setBackgroundColor(getResources().getColor(R.color.color_red));
                                  //store chosen color in shared prefs
                                  prefs = getSharedPreferences(HOME_BACKGROUND_COLOR, MODE_PRIVATE );
                                  edit = prefs.edit();
                                  edit.putInt(BACKGROUND_COLOR,getResources().getColor(R.color.color_red));
                                  edit.commit();
                                  break;
                              case SIX:
                                  findViewById(R.id.main_layout).setBackgroundColor(getResources().getColor(R.color.color_pink));
                                  //store chosen color in shared prefs
                                  prefs = getSharedPreferences(HOME_BACKGROUND_COLOR, MODE_PRIVATE );
                                  edit = prefs.edit();
                                  edit.putInt(BACKGROUND_COLOR,getResources().getColor(R.color.color_pink));
                                  edit.commit();
                                  break;
                              case SEVEN:
                                  findViewById(R.id.main_layout).setBackgroundColor(getResources().getColor(R.color.color_purple));
                                  //store chosen color in shared prefs
                                  prefs = getSharedPreferences(HOME_BACKGROUND_COLOR, MODE_PRIVATE );
                                  edit = prefs.edit();
                                  edit.putInt(BACKGROUND_COLOR,getResources().getColor(R.color.color_purple));
                                  edit.commit();
                                  break;
                              case EIGHT:
                                  findViewById(R.id.main_layout).setBackgroundColor(getResources().getColor(R.color.color_yellow));
                                  //store chosen color in shared prefs
                                  prefs = getSharedPreferences(HOME_BACKGROUND_COLOR, MODE_PRIVATE );
                                  edit = prefs.edit();
                                  edit.putInt(BACKGROUND_COLOR,getResources().getColor(R.color.color_yellow));
                                  edit.commit();
                                  break;
                              case NINE:
                                  findViewById(R.id.main_layout).setBackgroundResource(R.drawable.bg_pattern1);
                                  //store chosen image in shared prefs
                                  prefs = getSharedPreferences(HOME_BACKGROUND_COLOR, MODE_PRIVATE );
                                  edit = prefs.edit();
                                  edit.putInt(BACKGROUND_COLOR,R.drawable.bg_pattern1);
                                  edit.commit();
                                  break;
                              case TEN:
                                  findViewById(R.id.main_layout).setBackgroundResource(R.drawable.bg_pattern2);
                                  //store chosen image in shared prefs
                                  prefs = getSharedPreferences(HOME_BACKGROUND_COLOR, MODE_PRIVATE );
                                  edit = prefs.edit();
                                  edit.putInt(BACKGROUND_COLOR,R.drawable.bg_pattern2);
                                  edit.commit();
                                  break;
                              case ELEVEN:
                                  findViewById(R.id.main_layout).setBackgroundResource(R.drawable.bg_pattern3);
                                  //store chosen image in shared prefs
                                  prefs = getSharedPreferences(HOME_BACKGROUND_COLOR, MODE_PRIVATE );
                                  edit = prefs.edit();
                                  edit.putInt(BACKGROUND_COLOR,R.drawable.bg_pattern3);
                                  edit.commit();
                                  break;
                              case TWELVE :
                                  findViewById(R.id.main_layout).setBackgroundResource(R.drawable.bg_pattern4);
                                  //store chosen image in shared prefs
                                  prefs = getSharedPreferences(HOME_BACKGROUND_COLOR, MODE_PRIVATE );
                                  edit = prefs.edit();
                                  edit.putInt(BACKGROUND_COLOR,R.drawable.bg_pattern4);
                                  edit.commit();
                                  break;
                              case THIRTEEN :
                                  findViewById(R.id.main_layout).setBackgroundResource(R.drawable.bg_pattern5);
                                  //store chosen image in shared prefs
                                  prefs = getSharedPreferences(HOME_BACKGROUND_COLOR, MODE_PRIVATE );
                                  edit = prefs.edit();
                                  edit.putInt(BACKGROUND_COLOR,R.drawable.bg_pattern5);
                                  edit.commit();
                                  break;
                              case FOURTEEN:
                                  findViewById(R.id.main_layout).setBackgroundResource(R.drawable.bg_pattern6);
                                  //store chosen image in shared prefs
                                  prefs = getSharedPreferences(HOME_BACKGROUND_COLOR, MODE_PRIVATE );
                                  edit = prefs.edit();
                                  edit.putInt(BACKGROUND_COLOR,R.drawable.bg_pattern6);
                                  edit.commit();
                                  break;
                              case FIFTEEN:
                                  findViewById(R.id.main_layout).setBackgroundResource(R.drawable.bg_pattern7);
                                  //store chosen image in shared prefs
                                  prefs = getSharedPreferences(HOME_BACKGROUND_COLOR, MODE_PRIVATE );
                                  edit = prefs.edit();
                                  edit.putInt(BACKGROUND_COLOR,R.drawable.bg_pattern7);
                                  edit.commit();
                                  break;
                              case SIXTEEN:
                                  findViewById(R.id.main_layout).setBackgroundResource(R.drawable.bg_pattern8);
                                  //store chosen image in shared prefs
                                  prefs = getSharedPreferences(HOME_BACKGROUND_COLOR, MODE_PRIVATE );
                                  edit = prefs.edit();
                                  edit.putInt(BACKGROUND_COLOR,R.drawable.bg_pattern8);
                                  edit.commit();
                                  break;
                              case SEVENTEEN:
                                  findViewById(R.id.main_layout).setBackgroundResource(R.drawable.bg_pattern9);
                                  //store chosen image in shared prefs
                                  prefs = getSharedPreferences(HOME_BACKGROUND_COLOR, MODE_PRIVATE );
                                  edit = prefs.edit();
                                  edit.putInt(BACKGROUND_COLOR,R.drawable.bg_pattern9);
                                  edit.commit();
                                  break;
                          }//close switch
                        return false;
                      }
                });
                popupMenuColorSettings.show();
                break;
            case R.id.phever_weblink:
                GeneralAlertDialog myAlert1 = GeneralAlertDialog.newInstance("Advise of Internet Connect", "This Action tries to connect to the internet", true, true, 1);
                myAlert1.show(getFragmentManager(), "phever_link"); // the tab name is for referencing this instance if required
                break;
            case R.id.mail_list_link:
                GeneralAlertDialog myAlert2 = GeneralAlertDialog.newInstance("Advise of Internet Connect", "This Action tries to connect to the internet", true, true, 2);
                myAlert2.show(getFragmentManager(), "email_link"); // the tab name is for referencing this instance if required
                break;
        }//close switch

    }// close onclick method



	// Respond to the user touching the screen
	@Override
	public boolean onTouch(View v, MotionEvent ev) {
		final int action = ev.getAction();
		final int evX = (int) ev.getX();
		final int evY = (int) ev.getY();

		// If we cannot find the imageView, return.
		ImageView imageView = (ImageView) v.findViewById(R.id.img_home);
		if (imageView == null)
			return false;

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			// not used
			break;

		case MotionEvent.ACTION_UP:
			// On the UP of press we do the action.
			// The hidden image (img_mask) has different coloured hotspots on
			// it.
			// The colors are red, green, indigo, yellow, white, orange, magents
			// Use img_mask to determine which region the user touched.
			int touchColor = getHotspotColor(R.id.img_mask, evX, evY);

			// Compare the touchColor to the expected values. do action
			// depending on what color was touched.
			// Note that we use the ColorTool object to test whether the
			// observed color is close enough to the real color to
			// count as a match. We do this because colors on the screen do not
			// match the map exactly because of scaling and
			// varying pixel density.
			// by trial and error do not use black 0,0,0 as is the same as
			// transparent ie. zero colour.
			ColorTool ct = new ColorTool(); // instantiate class colortool and
											// use its method closematch
			int tolerance = 25;

			// process selected options from user
			if (ct.closeMatch(Color.rgb(255, 238, 56), touchColor, tolerance)) {
				// RADIO toast("Radio (yellow)");
                GeneralAlertDialog myAlert5 = GeneralAlertDialog.newInstance("Advise of Internet Connect", "This action makes use of the internet", true, true, 5);
                myAlert5.show(getFragmentManager(), "radio_link"); // the tab name is for referencing this instance if required
				//listenToRadio();
			} else if (ct.closeMatch(Color.rgb(67, 255, 61), touchColor,
					tolerance)) {
				// CONNNECT toast("Contacts (Green)");
                GeneralAlertDialog myAlert4 = GeneralAlertDialog.newInstance("Advise of Internet Connect", "Each of the list items on next screen connect to the internet", true, true, 4);
                myAlert4.show(getFragmentManager(), "connect_action"); // the tab name is for referencing this instance if required

			} else if (ct.closeMatch(Color.rgb(255, 71, 239), touchColor,
					tolerance)) {
				// EVENTS toast("Events (Magenta)");
                GeneralAlertDialog myAlert7 = GeneralAlertDialog.newInstance("Advise of Internet Connect", "This action makes use of the internet", true, true, 7);
                myAlert7.show(getFragmentManager(), "events_action"); // the tab name is for referencing this instance if required
				//intent = new Intent(this, EventsActivity.class);
				//startActivity(intent);
			} else if (ct.closeMatch(Color.rgb(255, 48, 86), touchColor,
					tolerance)) {
				// DJ SCHEDULE toast("Dj Schedule (Red)");
                GeneralAlertDialog myAlert6 = GeneralAlertDialog.newInstance("Advise of Internet Connect", "Each of the list items on the popup menu connect to the internet", false, true, 6);
                myAlert6.show(getFragmentManager(), "djschedule_action"); // the tab name is for referencing this instance if required
                showPopup(v);
			} else if (ct.closeMatch(Color.rgb(176, 58, 255), touchColor,
					tolerance)) {
				// TV toast("TV (indigo)");
                GeneralAlertDialog myAlert3 = GeneralAlertDialog.newInstance("Advise of Internet Connect", "This Action tries to connect to the internet", true, true, 3);
                myAlert3.show(getFragmentManager(), "tv_link"); // the tab name is for referencing this instance if required

			} else if (ct.closeMatch(Color.rgb(255, 255, 255), touchColor,
					tolerance)) {
				//Radio
				//toast("V- (White)");
				if (radio) {
				    // Its visible
					audioManager.adjustStreamVolume(STREAM_MUSIC,
							ADJUST_LOWER, FLAG_SHOW_UI);
				} else {
				    // Either gone or invisible
				}

		
			} else if (ct.closeMatch(Color.rgb(255, 196, 58), touchColor,
					tolerance)) {
				//toast("V+ (orange)");

				if (radio) {
				    // Its visible
					audioManager.adjustStreamVolume(STREAM_MUSIC,
							ADJUST_RAISE, FLAG_SHOW_UI);
				} else {
				    // Either gone or invisible
				}
			}
		default:
			// not used
			break;
		} // end switch

		return true;
	}// close method onTouch

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.day_menu, popup.getMenu());
        //popup.setOnMenuItemClickListener(this); //note major prob with 'this' since added extra popup menus
        // Handle item selection on the dj days popup
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int day = 0;
                switch (item.getItemId()) {
                    case R.id.monday:
                        day = 0;
                        break;
                    case R.id.tuesday:
                        day = 1;
                        break;
                    case R.id.wednesday:
                        day = 2;
                        break;
                    case R.id.thursday:
                        day = 3;
                        break;
                    case R.id.friday:
                        day = 4;
                        break;
                    case R.id.saturday:
                        day = 5;
                        break;
                    case R.id.sunday:
                        day = 6;
                        break;
                }//close switch

                Intent intent = new Intent(HomeActivity.this, DjScheduleActivity.class);
                intent.putExtra("day", day);
                startActivity(intent);
                return true;
            }
        });

        popup.show();
    }
    /* api level 14            Inflating the Popup using xml file
		popup.getMenuInflater().inflate(R.menu.day_menu,popup.getMenu());            */
	
	
	// get the x y co-ordinates of finger press on actual image
	public int getHotspotColor(int hotspotId, int x, int y) {
		ImageView img = (ImageView) findViewById(hotspotId);

		if (img == null) {
			Log.d("ImageAreasActivity", "Hot spot image not found");
			return 0;
		} else {
			img.setDrawingCacheEnabled(true);
			Bitmap hotspots = Bitmap.createBitmap(img.getDrawingCache());
			if (hotspots == null) {
				Log.d("ImageAreasActivity", "Hot spot bitmap was not created");
				return 0;
			} else {
				img.setDrawingCacheEnabled(false);
				return hotspots.getPixel(x, y);
			}
		}
	}// close method gethotspotcolour

	public void toast(String msg) {
		//jp this.getApplicationContext()  ???
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	} // end toast

	public void listenToRadio() {
		radio = true;
		isPlaying = false;
        //swAnim.run(); 			//start soundwave animation
        /*
		playPauseButton = (ToggleButton) findViewById(R.id.playPauseButton);
        playPauseButton.setVisibility(View.VISIBLE);


		playPauseButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Perform action on clicks
				if (playPauseButton.isChecked()) { // Checked - Pause icon visible
                    //pause();
                    stopService(streamService);
                    setVolumeControlStream(USE_DEFAULT_STREAM_TYPE);
                    swAnim.stop();
                    soundwaveRotate.setImageDrawable(soundwaveImage);

				} else { // Unchecked - Play icon visible
                    ///start();
                    startService(streamService);
                    swAnim.run();
                }
			}
		});
        */

		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		getPrefs();
		// control media volume
		this.setVolumeControlStream(STREAM_MUSIC);
        //final Intent streamService = new Intent(StreamService.class.getName());//this three lines replace next single commented out line
        //streamService.putExtra("URL", url);
        //this.startService(streamService);
		streamService = new Intent(HomeActivity.this, StreamService.class);
        streamService.putExtra("URL",  url);

		audioManager =  (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		// start the radio - thats why we are here

		//******************************************************************
		OnAudioFocusChangeListener afChangeListener = new OnAudioFocusChangeListener() {
            @Override
			public void onAudioFocusChange(int focusChange) {
				if (focusChange == AUDIOFOCUS_LOSS_TRANSIENT) {
                    // Pause playback
                    stopService(streamService);
                    setVolumeControlStream(USE_DEFAULT_STREAM_TYPE);
                    swAnim.stop();
                    soundwaveRotate.setImageDrawable(soundwaveImage);

				} else if (focusChange == AUDIOFOCUS_GAIN) {
					// Resume playback
                    if (!isPlaying) startService(streamService);
				} else if  (focusChange == AUDIOFOCUS_LOSS) {
                        //AudioManager.unregisterMediaButtonEventReceiver(RemoteControlReceiver);
                        //AudioManager.abandonAudioFocus(afChangeListener);
                        // Stop playback
                        stopService(streamService);
                        setVolumeControlStream(USE_DEFAULT_STREAM_TYPE);
                        swAnim.stop();
                        soundwaveRotate.setImageDrawable(soundwaveImage);


                } else if (focusChange == AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        // Lower the volume jp
                    audioManager.adjustStreamVolume(STREAM_MUSIC,ADJUST_LOWER, FLAG_SHOW_UI);

                } else if (focusChange == AUDIOFOCUS_GAIN) {
                        // Raise it back to normal jp
					audioManager.adjustStreamVolume(STREAM_MUSIC,ADJUST_RAISE, FLAG_SHOW_UI);
                }
			}
		};
		// Request audio focus for playback
		int result = audioManager.requestAudioFocus(afChangeListener,STREAM_MUSIC,AUDIOFOCUS_GAIN);

		if (result == AUDIOFOCUS_REQUEST_GRANTED) {
			// Start listening for button presses
			//AudioManager.registerMediaButtonEventReceiver(RemoteControlReceiver);
			//AudioManager.setMediaButtonReceiver(RemoteControlReceiver);
			//startService(streamService);
			// Start playback.
		}

		//******************************************************************
		//startService(streamService);

	} //close method listenToRadio

    /*
    @Override
    public void onAudioFocusChange(int focusChange) {
        if (focusChange == AUDIOFOCUS_LOSS_TRANSIENT) {
            // Pause playback
            stopService(streamService);
            setVolumeControlStream(USE_DEFAULT_STREAM_TYPE);
            swAnim.stop();
            soundwaveRotate.setImageDrawable(soundwaveImage);

        } else if (focusChange == AUDIOFOCUS_GAIN) {
            // Resume playback
            if (!isPlaying) startService(streamService);
        } else if  (focusChange == AUDIOFOCUS_LOSS) {
            //AudioManager.unregisterMediaButtonEventReceiver(RemoteControlReceiver);
            //AudioManager.abandonAudioFocus(afChangeListener);
            // Stop playback
            stopService(streamService);
            setVolumeControlStream(USE_DEFAULT_STREAM_TYPE);
            swAnim.stop();
            soundwaveRotate.setImageDrawable(soundwaveImage);


        } else if (focusChange == AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
            // Lower the volume jp
            audioManager.adjustStreamVolume(STREAM_MUSIC,ADJUST_LOWER, FLAG_SHOW_UI);

        } else if (focusChange == AUDIOFOCUS_GAIN) {
            // Raise it back to normal jp
            audioManager.adjustStreamVolume(STREAM_MUSIC,ADJUST_RAISE, FLAG_SHOW_UI);
        }
    }
    */



    //note the boolean isPlaying get set to true in the service start method and set to false in the service destroy method
    //this method gets the stored share preferences boolean which indicates radio playing = true or radio not playing = false
    //to uese this method the shared preferences class and the editor reader have been instantiated above
	public void getPrefs() {
		isPlaying = prefs.getBoolean("isPlaying", false); // get status of radio on or off
		if (isPlaying){ //if on
           //stopService(streamService);//stop the service which in turn stops the radio which runs in the service
           //setVolumeControlStream(USE_DEFAULT_STREAM_TYPE); // free up focus to other resource
            Toast.makeText(HomeActivity.this, "isPlaying = " + isPlaying, Toast.LENGTH_LONG).show();
		}
	}


    //used to check if a service class is running an instance returns true if yes and false if not
    //note is more accurate than using preferences since onDestroy does not get called in every situation eg. suddenly turn off phone
    //isServiceRunning(myService.class); // this is how you would call on the check service class running method -note you pass the class name not the instance
    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }



    @Override
    public void onDialogPositiveClick(DialogFragment dialog, int ok_option) {
        View v;
        Boolean radioPlaying = false;
        // User touched the dialog's positive button
        switch(ok_option){
            case 0:
               // Toast.makeText(HomeActivity.this, "User touched ok and option is " + ok_option, Toast.LENGTH_LONG).show();
                break;
            case 1:
                //Toast.makeText(HomeActivity.this, "User touched ok and option is " + ok_option, Toast.LENGTH_LONG).show();
                //phever.ie weblink
                url = pheverWebsiteUrlConnect;
                new HandleUrlConnect().execute(url);//calls asyncTask class to try connect to internet
                break;
            case 2:
                //Toast.makeText(HomeActivity.this, "User touched ok and option is " + ok_option, Toast.LENGTH_LONG).show();
                //email database
                url = pheverEmailUrlConnect;
                new HandleUrlConnect().execute(url);//calls asyncTask class to try connect to internet
                break;
            case 3:
                //Toast.makeText(HomeActivity.this, "User touched ok and option is " + ok_option, Toast.LENGTH_LONG).show();
                //tv
                //check if streamService is running ie. returns true if is and false if not
                radioPlaying = isServiceRunning(StreamService.class);
                //if service running then radio is running so turn it off
                if(radioPlaying){
                    //Toast.makeText(HomeActivity.this, "service status is " + radioPlaying, Toast.LENGTH_SHORT).show();
                    //streamService = new Intent(HomeActivity.this, StreamService.class);
                    stopService(streamService);//stop the service which in turn stops the radio which runs in the service
                    //setVolumeControlStream(USE_DEFAULT_STREAM_TYPE); // free up focus to other resource
                    playPauseButton.setVisibility(View.INVISIBLE);
                    swAnim.stop();
                    soundwaveRotate.setVisibility(View.INVISIBLE);
                    soundwaveRotate.setImageDrawable(soundwaveImage);
                   // audioManager.abandonAudioFocus(this);
                }
                url = pheverTvUrlConnect;
                new HandleUrlConnect().execute(url);//calls asyncTask class to try connect to internet
                break;
            case 4:
                //connect
                //check if streamService is running ie. returns true if is and false if not
                radioPlaying = isServiceRunning(StreamService.class);
                //if service running then radio is running so turn it off
                if(radioPlaying){
                    //Toast.makeText(HomeActivity.this, "service status is " + radioPlaying, Toast.LENGTH_SHORT).show();
                    //streamService = new Intent(HomeActivity.this, StreamService.class);
                    stopService(streamService);//stop the service which in turn stops the radio which runs in the service
                    //setVolumeControlStream(USE_DEFAULT_STREAM_TYPE); // free up focus to other resource
                    playPauseButton.setVisibility(View.INVISIBLE);
                    swAnim.stop();
                    soundwaveRotate.setVisibility(View.INVISIBLE);
                    soundwaveRotate.setImageDrawable(soundwaveImage);
                    //audioManager.abandonAudioFocus(this);
                }
                //Toast.makeText(HomeActivity.this, "User touched ok and option is " + ok_option, Toast.LENGTH_LONG).show();
                Intent intent4 = new Intent(HomeActivity.this, ConnectActivity.class);
                startActivity(intent4);
                break;
            case 5:
                //Toast.makeText(HomeActivity.this, "User touched ok and option is " + ok_option, Toast.LENGTH_LONG).show();
                //radio
                radioPlaying = isServiceRunning(StreamService.class);
                if(radioPlaying){
                    //streamService = new Intent(HomeActivity.this, StreamService.class);
                    playPauseButton.setChecked(false);
                    playPauseButton.setVisibility(View.VISIBLE);
                    soundwaveRotate.setVisibility(View.VISIBLE);
                    swAnim.run();
                } else {
                    url = pheverRadioUrlconnect;
                    new HandleUrlConnect().execute(url);//calls asyncTask class to try connect to internet
                }
            break;
            case 6:
                //Toast.makeText(HomeActivity.this, "User touched ok and option is " + ok_option, Toast.LENGTH_LONG).show();
                //Dj Schedule
                //showPopup(v); //cannot get this to work due to the needed param View v - this cannot be passed in through alert dialog
                break;
            case 7:
                //Toast.makeText(HomeActivity.this, "User touched ok and option is " + ok_option, Toast.LENGTH_LONG).show();
                //Events
                intent = new Intent(this, EventsActivity.class);
                startActivity(intent);
                break;

        }//close switch

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog, int cancel_option) {
        // User touched the dialog's negative  button
       // Toast.makeText(HomeActivity.this, "User touched cancel and option is  " + cancel_option, Toast.LENGTH_LONG).show();
    }

    //this method listens for any notivications sent by other apps which make request on audio focus
    //it receives the notificaitons and processes them based on their value
    //it is the method from the interface  onAudioFocusChange implemented by HomeActivity
    @Override
    public void onAudioFocusChange(int focusChange) {
        if(focusChange == AUDIOFOCUS_LOSS_TRANSIENT) {  //some app seeks temporary audio focus
            //stop service
            stopService(streamService);
            //setVolumeControlStream(USE_DEFAULT_STREAM_TYPE);
            swAnim.stop();
            soundwaveRotate.setVisibility(View.INVISIBLE);
            soundwaveRotate.setImageDrawable(soundwaveImage);
            playPauseButton.setChecked(true);
            audioManager.abandonAudioFocus(this);
        } else if(focusChange == AUDIOFOCUS_GAIN) {     //audio focus given back to this app
            //start service
            if (!isPlaying){
                startService(streamService);
                soundwaveRotate.setVisibility(View.VISIBLE);
                swAnim.run();
                playPauseButton.setChecked(false);
            }
        } else if(focusChange == AUDIOFOCUS_LOSS) {    //this app is giving up focus
            // Stop service
            stopService(streamService);
            setVolumeControlStream(USE_DEFAULT_STREAM_TYPE);
            swAnim.stop();
            soundwaveRotate.setVisibility(View.INVISIBLE);
            soundwaveRotate.setImageDrawable(soundwaveImage);
            playPauseButton.setChecked(true);
            audioManager.abandonAudioFocus(this);
        }
    }

    //async task to check internet and server connectivity and advise user if no connect
    //NOTE THIS ASYNC TASK CLASS IS NOT SUITABLE FOR RETURNING DATA - IS ONLY FOR CONNECT TO A URL OUTSIDE THE APP
    private class HandleUrlConnect extends AsyncTask<String, Void, Boolean> {
        ProgressDialog progressUrlConnect;
        AlertDialog alert;
        int connectStatus = 0;// a 0 indicates no internet connect - a 1 indicates no server connect
        //start a progress dialog advises user that something is happening
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressUrlConnect = ProgressDialog.show(HomeActivity.this, "Connecting to Internet", "Please Wait ...", true);
        }//close method onpreexecute

        @Override
        protected Boolean doInBackground(String... urls) {
            //first check there is a connection to intenet (is turned on and in range)
            ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkinfo = connMgr.getActiveNetworkInfo();
            if(networkinfo != null && networkinfo.isConnected()){ //yes internet turned on and in range
                Log.d("NETWORK_INFO", String.valueOf(networkinfo.isConnected()));
                try{
                    //second check connection to server
                    URL myUrl = new URL(urls[0]);//paras [0] is the url passed into the async task
                    Log.d("url used is: ", myUrl.toString());
                    URLConnection connection = myUrl.openConnection();
                    connection.setConnectTimeout(500 * 8); //4 seconds
                    connection.connect();
                    return true; //connect made
                }catch(Exception e){ //if connection to server or file cannot be made
                    connectStatus = 1; //server connect issue
                    return false;
                }
            }//close if
            connectStatus = 0; //internet connect issue
            return false;
        }//close method doinbackground

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            progressUrlConnect.dismiss();//close progress dialog if still open
            if(result){ //if connection to url is successfull
                if(url.equalsIgnoreCase(pheverRadioUrlconnect)){
                    playPauseButton.setChecked(true);
                    playPauseButton.setVisibility(View.VISIBLE);
                } else {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url)); //url here it the url passed in at the asynctask call
                    startActivity(i);
                }
            }else {  //if connecction not successfull
                if(connectStatus == 0){ //no internet connection (is turned off or out of range)
                    GeneralAlertDialog myAlert = GeneralAlertDialog.newInstance("Cannot Connect to Intenet", "Please check internet turned on or in range", false, true, 0);
                    myAlert.show(getFragmentManager(), "no internet connect");
                }else { //yes internet but no connect to server cannot get web file or data
                    GeneralAlertDialog myAlert = GeneralAlertDialog.newInstance("Cannot Connect to Server", "Server may be down try again later", false, true, 0);
                    myAlert.show(getFragmentManager(), "no server connect");
                }//close second if
            }//close first if

        }//close method onpost

    }//close class handleurlconnect  (async task)


        public void handleToggleButton(View v) {
            // Perform action on clicks
            if (playPauseButton.isChecked()) { // Checked - Pause icon visible
                //pause();
                stopService(streamService);
                //setVolumeControlStream(USE_DEFAULT_STREAM_TYPE);
                swAnim.stop();
                soundwaveRotate.setVisibility(View.INVISIBLE);
                soundwaveRotate.setImageDrawable(soundwaveImage);
               // audioManager.abandonAudioFocus(this);

            } else { // Unchecked - Play icon visible
                //register audio manager and request audio focus ie. use of device speakers
                audioManager =  (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                int result = audioManager.requestAudioFocus(HomeActivity.this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
                if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){  //if granted audio focus start service
                    startService(streamService);
                    soundwaveRotate.setVisibility(View.VISIBLE);
                    swAnim.run();
                }else {  //if not granted audio focus advise user to try again later
                    Toast.makeText(HomeActivity.this, "Other app has Audio Speakers - Try again later", Toast.LENGTH_LONG).show();
                }

            }
        }

}// close class homeactivity

