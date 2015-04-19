package com.devstream.phever.activities;

//import com.devstream.phever.utilities.RotateThread;
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
		//r.setDuration(2000);
		r.setDuration(0);
		r.setRepeatCount(0);
		splashRotate.startAnimation(r);
		r.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				Intent intent = new Intent(SplashActivity.this,
						HomeActivity.class);
				startActivity(intent);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO
			}
		});

		/*
		 * imgSplash = (ImageView) findViewById(R.id.img_splash); RotateThread
		 * rotator = new RotateThread(imgSplash); rotator.run();
		 */

	}
}
