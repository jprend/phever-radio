package com.devstream.phever.utilities;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

public class SoundwaveAnimateThread implements Runnable {
	ImageView soundwaveAnimate;
	ObjectAnimator animation ;
    //constructor
	public SoundwaveAnimateThread(ImageView soundwaveAnimate) {
		this.soundwaveAnimate = soundwaveAnimate;
        this.animation = ObjectAnimator.ofFloat(soundwaveAnimate, "rotationX", 0.0f, 360f);
	}

	@Override
	public void run() {
        ObjectAnimator objectAnimator = animation.setDuration(3600);
        animation.setRepeatCount(ObjectAnimator.INFINITE);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.start();
	}
    //@TargetApi(Build.VERSION_CODES.KITKAT)
    public void stop() {
        animation.cancel();   // .pause level19 required
    }

}//close class soundwaveframeanimationthread




