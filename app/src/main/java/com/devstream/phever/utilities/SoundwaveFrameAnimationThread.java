package com.devstream.phever.utilities;

import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;

public class SoundwaveFrameAnimationThread implements Runnable {
	private AnimationDrawable frameAnimation;  //for soundwave sound animation
	private ImageView soundwaveAnimate;
		
	public SoundwaveFrameAnimationThread(ImageView soundwaveAnimate) {
		this.soundwaveAnimate = soundwaveAnimate;
	}

	@Override
	public void run() {
		// Type casting the Animation drawable
		frameAnimation = (AnimationDrawable) soundwaveAnimate.getBackground();		
		frameAnimation.start();				
	}

}//close class soundwaveframeanimationthread

/* transfer this code to splash screen
case R.id.btn_rotate_image:
ImageView image = (ImageView) findViewById(R.id.imageView1);
RotateThread rotator = new RotateThread(image);
rotator.run();
 */
