package com.devstream.phever.utilities;

import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

//private class to rotate the image on a separate thread
	public class SplashAnimateThread implements Runnable {
		private ImageView imageToRotate;
        RotateAnimation r;

        //constructor - pass in imageview which has image to rotate
		public SplashAnimateThread(ImageView image) {
			this.imageToRotate = image;
		}

		@Override
		public void run() {
            r = new RotateAnimation(0.0f, 5400.0f, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
            r.setInterpolator(new LinearInterpolator());
            r.setDuration(1500);
            r.setRepeatCount(0);
			try {
				this.imageToRotate.startAnimation(r);
				} catch (Exception e) {
					e.printStackTrace();
				}
		}//close method run
	}//close class splashanimatethread
	