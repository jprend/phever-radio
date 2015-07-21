package com.devstream.phever.activities;

//import com.devstream.phever.utilities.SplashAnimateThread;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class SplashActivity extends Activity {
	private ImageView splashRotate;
	private RotateAnimation r;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		splashRotate = (ImageView)findViewById(R.id.splash_img_rotate);
		r = new RotateAnimation(0.0f, 5400.0f, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
		r.setInterpolator(new LinearInterpolator());
		r.setDuration(1500);  //comment this line out to bypass splash screen for development purposes
		//r.setDuration(0);   //and uncomment this line in to avoid splash screen
		r.setRepeatCount(0);
		splashRotate.startAnimation(r);
		r.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// to do
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				Intent intent = new Intent(SplashActivity.this,
						HomeActivity.class);
				startActivity(intent);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// to do
			}
		});

		/*  //if using a thread but as not clashing with any ohter activity no need
		 * imgSplash = (ImageView) findViewById(R.id.img_splash);
		 * SplashAnimateThread rotator = new SplashAnimateThread(imgSplash);
		 * rotator.run();
		 */

	}
}
