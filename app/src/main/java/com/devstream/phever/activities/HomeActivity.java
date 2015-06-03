package com.devstream.phever.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.devstream.phever.utilities.ColorTool;
import com.devstream.phever.utilities.SoundwaveAnimateThread;

import static android.media.AudioManager.*;

public class HomeActivity extends Activity implements View.OnClickListener,  View.OnTouchListener, OnAudioFocusChangeListener {
    private PopupMenu popupMenuColorSettings;
    private final static int ONE = 1;
    private final static int TWO = 2;
    private final static int THREE = 3;
    private final static int FOUR = 4;
    private final static int FIVE = 5;
    private final static int SIX = 6;
    private final static int SEVEN = 7;
    private final static int EIGHT = 8;
	private ImageView soundwaveAnimate, playRadio, pauseRadio, soundwaveRotate; //frame animate
    private SoundwaveAnimateThread swAnim;

	private Intent intent;

	ToggleButton playPauseButton;

	static Context context;
	boolean isPlaying;
	boolean radio = false;
	Intent streamService;
	SharedPreferences prefs;
	AudioManager audioManager;
	ComponentName RemoteControlReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		context = this;

        //set ontouch listener to the visible home image
		ImageView iv = (ImageView) findViewById(R.id.img_home);
		if (iv != null) {
			iv.setOnTouchListener(this);
		}

        //set up the sound wave animation
        soundwaveRotate  = (ImageView) findViewById(R.id.soundwave_img_animate); // soundwave image
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
                popupMenuColorSettings.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
                      @Override
                      public boolean onMenuItemClick(MenuItem item) {
                          switch (item.getItemId()) {
                              case ONE:
                                  findViewById(R.id.main_layout).setBackgroundColor(getResources().getColor(R.color.color_black));
                                  break;
                              case TWO:
                                  findViewById(R.id.main_layout).setBackgroundColor(getResources().getColor(R.color.color_white));
                                  break;
                              case THREE:
                                  findViewById(R.id.main_layout).setBackgroundColor(getResources().getColor(R.color.color_green));
                                  break;
                              case FOUR:
                                  findViewById(R.id.main_layout).setBackgroundColor(getResources().getColor(R.color.color_blue));
                                  break;
                              case FIVE:
                                  findViewById(R.id.main_layout).setBackgroundColor(getResources().getColor(R.color.color_red));
                                  break;
                              case SIX:
                                  findViewById(R.id.main_layout).setBackgroundColor(getResources().getColor(R.color.color_pink));
                                  break;
                              case SEVEN:
                                  findViewById(R.id.main_layout).setBackgroundColor(getResources().getColor(R.color.color_purple));
                                  break;
                              case EIGHT:
                                  findViewById(R.id.main_layout).setBackgroundColor(getResources().getColor(R.color.color_yellow));
                                  break;
                          }//close switch
                        return false;
                      }
                });
                popupMenuColorSettings.show();
                break;
            case R.id.phever_weblink:
                String url1 =  "http://phever.ie";
                Intent intent1 = new Intent(Intent.ACTION_VIEW);
                intent1.setData(Uri.parse(url1));
                startActivity(intent1);
                break;
            case R.id.mail_list_link:
                String url2 = "https://docs.google.com/forms/d/1op3yEBANTh7_QDTMDW-bygsiLwH1uQgsSAJiffznssU/viewform";
                Intent intent2 = new Intent(Intent.ACTION_VIEW);
                intent2.setData(Uri.parse(url2));
                startActivity(intent2);
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
				listenToRadio();
			} else if (ct.closeMatch(Color.rgb(67, 255, 61), touchColor,
					tolerance)) {
				// CONNNECT toast("Contacts (Green)");
                /*
                //check if streamService is running ie. returns true if is and false if not
                //Boolean radioPlaying = isServiceRunning(StreamService.class);
                //if service running then radio is running so turn it off
               // if(radioPlaying){
                    //Toast.makeText(HomeActivity.this, "service status is " + radioPlaying, Toast.LENGTH_SHORT).show();
                    //listenToRadio();
                    //stopService(streamService);//stop the service which in turn stops the radio which runs in the service
                    //setVolumeControlStream(USE_DEFAULT_STREAM_TYPE); // free up focus to other resource
                    //playPauseButton = (ToggleButton) findViewById(R.id.playPauseButton);
                   // playPauseButton.setVisibility(View.INVISIBLE);
               // }
                */
				intent = new Intent(this, ConnectActivity.class);
				startActivity(intent);
			} else if (ct.closeMatch(Color.rgb(255, 71, 239), touchColor,
					tolerance)) {
				// EVENTS toast("Events (Magenta)");
				intent = new Intent(this, EventsActivity.class);
				startActivity(intent);
			} else if (ct.closeMatch(Color.rgb(255, 48, 86), touchColor,
					tolerance)) {
				// DJ SCHEDULE toast("Dj Schedule (Red)");				
				showPopup(v);
			} else if (ct.closeMatch(Color.rgb(176, 58, 255), touchColor,
					tolerance)) {
				// TV toast("TV (indigo)");
                Uri uri  = Uri.parse("http://livestream.com/accounts/10782842/TV");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
				//intent = new Intent(this, TvActivity.class);
				//startActivity(intent);
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
        swAnim.run(); 			//start soundwave animation

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
				} else { // Unchecked - Play icon visible
                    ///start();
                    startService(streamService);
                }
			}
		});


		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		getPrefs();
		// control media volume
		this.setVolumeControlStream(STREAM_MUSIC);
		streamService = new Intent(HomeActivity.this, StreamService.class);

		audioManager =  (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		// start the radio - thats why we are here

		//******************************************************************
		final OnAudioFocusChangeListener afChangeListener = new OnAudioFocusChangeListener() {
			public void onAudioFocusChange(int focusChange) {
				if (focusChange == AUDIOFOCUS_LOSS_TRANSIENT) {
					// Pause playback
				} else if (focusChange == AUDIOFOCUS_GAIN) {
					// Resume playback
				} else if (focusChange == AUDIOFOCUS_LOSS) {
					//AudioManager.unregisterMediaButtonEventReceiver(RemoteControlReceiver);
					//AudioManager.abandonAudioFocus(afChangeListener);
					// Stop playback

				} else if (focusChange == AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
					// Lower the volume
				} else if (focusChange == AUDIOFOCUS_GAIN) {
					// Raise it back to normal
				}
			}
		};
		// Request audio focus for playback
		int result = audioManager.requestAudioFocus(afChangeListener,
				// Use the music stream.
				STREAM_MUSIC,
				// Request permanent focus.
				AUDIOFOCUS_GAIN);

		if (result == AUDIOFOCUS_REQUEST_GRANTED) {
			// Start listening for button presses
			//AudioManager.registerMediaButtonEventReceiver(RemoteControlReceiver);
			//AudioManager.setMediaButtonReceiver(RemoteControlReceiver);
			startService(streamService);
			// Start playback.
		}

		//******************************************************************
		//startService(streamService);

	}

    //note the boolean isPlaying get set to true in the service start method and set to false in the service destroy method
	public void getPrefs() {
		isPlaying = prefs.getBoolean("isPlaying", false); // get status of radio on or off
		if (isPlaying){ //if on
           //stopService(streamService);//stop the service which in turn stops the radio which runs in the service
           //setVolumeControlStream(USE_DEFAULT_STREAM_TYPE); // free up focus to other resource
           // Toast.makeText(HomeActivity.this, "isPlaying = " + isPlaying, Toast.LENGTH_LONG).show();
		}
	}

    @Override
    public void onAudioFocusChange(int focusChange) {

    }

    //used to check if a service class is running an instance returns true if yes and false if not
    //note is more accurate than using preferences since onDestroy does not get called in every situation eg. suddenly turn off phone
    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    //isServiceRunning(myService.class); // this is how you would call on the check service class running method -note you pass the class name not the instance

}// close class homeactivity

