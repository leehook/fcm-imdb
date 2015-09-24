package it.leehook.fcm.activities;

import it.leehook.fcm.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

public class Splash extends Activity {

    private boolean active = true;

    private int splashTime = 1200;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.splash);

	// thread for displaying the SplashScreen
	Thread splashTread = new Thread() {
	    @Override
	    public void run() {
		try {
		    int waited = 0;
		    while (active && (waited < splashTime)) {
			sleep(100);
			if (active) {
			    waited += 200;
			}
		    }
		} catch (InterruptedException e) {
		    // do nothing
		} finally {
		    finish();
		    startActivity(new Intent(Splash.this, FCMActivity.class));
		}
	    }
	};
	splashTread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
	if (event.getAction() == MotionEvent.ACTION_DOWN) {
	    active = false;
	}
	return true;
    }
}
