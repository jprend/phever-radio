package com.devstream.phever.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuInflater;
import android.util.Log;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.Toast;

import com.devstream.phever.utilities.ColorTool;
import com.devstream.phever.utilities.SoundwaveFrameAnimationThread;


public class HomeActivity extends Activity implements View.OnTouchListener,
													  android.widget.PopupMenu.OnMenuItemClickListener {
	private ImageView soundwaveAnimate; //frame animate

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
		ImageView iv = (ImageView) findViewById(R.id.img_home);

		if (iv != null) {
			iv.setOnTouchListener(this);
		}
		
		/*
		//frame animate 
		soundwaveAnimate = (ImageView)findViewById(R.id.imageAnimation);
		// Setting animation_list.xml as the background of the image view
		soundwaveAnimate.setBackgroundResource(R.drawable.frame_animation_soundwave);
		SoundwaveFrameAnimationThread frameAnim = new SoundwaveFrameAnimationThread(soundwaveAnimate);
		frameAnim.run();
		*/
		
	}

	@SuppressLint("SetJavaScriptEnabled")
	public boolean onMenuItemClick(MenuItem item) {
		// Toast.makeText( HomeActivity.this,"You Clicked : " +
		// item.getTitle(),Toast.LENGTH_SHORT).show();
		// Handle item selection
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
		}

		Intent intent = new Intent(this, DjScheduleActivity.class);
		intent.putExtra("day", day);
		startActivity(intent);
		return true;
	}

	// Respond to the user touching the screen
	@SuppressLint("ClickableViewAccessibility")
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
				//intent = new Intent(this, RadioActivity.class);
				//startActivity(intent);
			} else if (ct.closeMatch(Color.rgb(67, 255, 61), touchColor,
					tolerance)) {
				// CONTACTS toast("Contacts (Green)");
				intent = new Intent(this, ContactsActivity.class);
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
		popup.setOnMenuItemClickListener(this);
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
		startButton = (Button) findViewById(R.id.startButton);
		startButton.setVisibility(View.VISIBLE);
		stopButton = (Button) findViewById(R.id.stopButton);
		stopButton.setVisibility(View.VISIBLE);
		
	
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		getPrefs();
		// control media volume
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		streamService = new Intent(HomeActivity.this, StreamService.class);
		audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		// start the radio - thats why we are here
		startService(streamService);
		startButton.setEnabled(false);
		
		startButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startService(streamService);
				startButton.setEnabled(false);
			}
		});
		
		stopButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Abandon audio focus when playback complete    
				//audioManager.abandonAudioFocus(afChangeListener);
				stopService(streamService);
				startButton.setEnabled(true);
				setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);				
			}
		});
	}

	public void getPrefs() {
		isPlaying = prefs.getBoolean("isPlaying", false);
		if (isPlaying)
			startButton.setEnabled(false);	
	}

}// close class homeactivity

