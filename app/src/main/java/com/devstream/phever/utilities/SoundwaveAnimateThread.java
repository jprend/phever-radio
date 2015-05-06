package com.devstream.phever.utilities;

import android.animation.ObjectAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

public class SoundwaveAnimateThread implements Runnable {
	private ImageView soundwaveAnimate;

    //constructor
	public SoundwaveAnimateThread(ImageView soundwaveAnimate) {
		this.soundwaveAnimate = soundwaveAnimate;
	}

	@Override
	public void run() {
            ObjectAnimator animation = ObjectAnimator.ofFloat(soundwaveAnimate, "rotationX", 0.0f, 360f);
            animation.setDuration(3600);
            animation.setRepeatCount(ObjectAnimator.INFINITE);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            animation.start();
	}

}//close class soundwaveframeanimationthread




