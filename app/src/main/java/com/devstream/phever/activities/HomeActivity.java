package com.devstream.phever.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.devstream.phever.utilities.ColorTool;
import com.devstream.phever.utilities.SoundwaveAnimateThread;

public class HomeActivity extends Activity implements View.OnClickListener,  View.OnTouchListener, AudioManager.OnAudioFocusChangeListener {
    private PopupMenu popupMenuColorSettings;
    private final static int ONE = 1;
    private final static int TWO = 2;
    private final static int THREE = 3;
	private ImageView soundwaveAnimate, playRadio, pauseRadio, soundwaveRotate; //frame animate
    private SoundwaveAnimateThread swAnim;

	private Intent intent;
	
	Button startButton, stopButton;
	Button btnPlayPause;
	static Context context;
	boolean isPlaying;
	boolean radio = false;
	Intent streamService;
	SharedPreferences prefs;
	AudioManager audioManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		context = this;

        //set ontouch listener to the visible home image
		ImageView iv = (ImageView) findViewById(R.id.img_home);
		if (iv != null) {
			iv.setOnTouchListener((View.OnTouchListener) this);
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


/*
	@SuppressLint("SetJavaScriptEnabled")
	public boolean onMenuItemClick(MenuItem item) {
		// Handle item selection on the dj days popup
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
        default:
                return super.onOptionsItemSelected(item);
        }//close switch

        Intent intent = new Intent(this, DjScheduleActivity.class);
        intent.putExtra("day", day);
        startActivity(intent);
        return true;
	}//close onMenuItemClick (for popups)
*/
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
                //start sound wave animation when radio initially turned on
				//intent = new Intent(this, RadioActivity.class);
				//startActivity(intent);
			} else if (ct.closeMatch(Color.rgb(67, 255, 61), touchColor,
					tolerance)) {
				// CONNNECT toast("Contacts (Green)");
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
				intent = new Intent(this, TvActivity.class);
				startActivity(intent);
			} else if (ct.closeMatch(Color.rgb(255, 255, 255), touchColor,
					tolerance)) {
				//Radio
				//toast("V- (White)");
				if (radio) {
				    // Its visible
					audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
							AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);							
				} else {
				    // Either gone or invisible
				}

		
			} else if (ct.closeMatch(Color.rgb(255, 196, 58), touchColor,
					tolerance)) {
				//toast("V+ (orange)");

				if (radio) {
				    // Its visible
					audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
							AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);							
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
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	} // end toast

	public void listenToRadio() {
		radio = true;
		isPlaying = false;
        swAnim.run(); //start soundwave animation
		//startButton = (Button) findViewById(R.id.startButton);
		//startButton.setVisibility(View.VISIBLE);
		//stopButton = (Button) findViewById(R.id.stopButton);
		//stopButton.setVisibility(View.VISIBLE);

        playRadio = (ImageView)findViewById(R.id.img_radio_play);
        pauseRadio = (ImageView)findViewById(R.id.img_radio_pause);
		playRadio.setVisibility(View.VISIBLE);
		pauseRadio.setVisibility(View.VISIBLE);

		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		getPrefs();
		// control media volume
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		streamService = new Intent(HomeActivity.this, StreamService.class);
		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		// start the radio - thats why we are here
		startService(streamService);
		playRadio.setEnabled(false);   //startButton.setEnabled(false);
		
		playRadio.setOnClickListener(new View.OnClickListener() { //to return to buttons just replace with startButton
            @Override
            public void onClick(View v) {
                startService(streamService);
                playRadio.setEnabled(false); //startButton.setEnabled(false);
                //swAnim.run();  //not working
            }
        });
		
		pauseRadio.setOnClickListener(new View.OnClickListener() { //to return to buttons just replace with stopButton
            @Override
            public void onClick(View v) {
                // Abandon audio focus when playback complete
                //audioManager.abandonAudioFocus(HomeActivity.this);
                stopService(streamService);
                playRadio.setEnabled(true); //startButton.setEnabled(true);
                setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);

                //swAnim.
                //would be nice to stop animate when radio stopped not known yet how to do this

            }
        });
	}

	public void getPrefs() {
		isPlaying = prefs.getBoolean("isPlaying", false);
		if (isPlaying)
			playRadio.setEnabled(false);	 //startButton.setEnabled(false);
	}

    @Override
    public void onAudioFocusChange(int focusChange) {

    }
}// close class homeactivity

