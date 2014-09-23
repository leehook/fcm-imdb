package it.leehook.fcm.impl;

import it.leehook.fcm.activities.FCMActivity;
import it.leehook.fcm.utils.Constants;
import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Activity di gestione delle gesture di {@link FCMActivity}
 * 
 * @author l.angelini
 * 
 */
public class ActivitySwipeDetector implements View.OnTouchListener {

	static final String logTag = "ActivitySwipeDetector";
	private Activity activity;
	static final int MIN_DISTANCE = 50;
	private float downX, upX;

	/**
	 * Costruttore. Inizializza l'activity
	 * 
	 * @param activity
	 */
	public ActivitySwipeDetector(Activity activity) {
		this.activity = activity;
	}

	/**
	 * Gestisce la gesture da destra a sinistra
	 */
	public void onRightToLeftSwipe() {
		((FCMActivity) activity).switchTab(Constants.EXTRA);
	}

	/**
	 * Gestisce la gesture da sinistra a destra
	 */
	public void onLeftToRightSwipe() {
		((FCMActivity) activity).switchTab(Constants.LEGA);
	}

	/**
	 * Gestisce il tocco sullo schermo
	 */
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			downX = event.getX();
			return true;
		}
		case MotionEvent.ACTION_UP: {
			upX = event.getX();

			float deltaX = downX - upX;

			// swipe horizontal?
			if (Math.abs(deltaX) > MIN_DISTANCE) {
				// left or right
				if (deltaX < 0) {
					this.onLeftToRightSwipe();
					return true;
				}
				if (deltaX > 0) {
					this.onRightToLeftSwipe();
					return true;
				}
			} else {
				Log.i(logTag, "Swipe was only " + Math.abs(deltaX)
						+ " long, need at least " + MIN_DISTANCE);
				return false; // We don't consume the event
			}

			return true;
		}
		}
		return false;
	}

}
