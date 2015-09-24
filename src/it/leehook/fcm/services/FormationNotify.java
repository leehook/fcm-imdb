/**
 * 
 */
package it.leehook.fcm.services;

import it.leehook.fcm.R;
import it.leehook.fcm.activities.InviaFormazione;
import it.leehook.fcm.utils.DbUtils;
import it.leehook.fcm.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * Servizio di notifica per l'invio della formazione
 * 
 * @author l.angelini
 * 
 */
public class FormationNotify extends Service {

    private Timer timer = new Timer();
    private final IBinder mBinder = new MyBinder();
    private static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;

    @Override
    public IBinder onBind(Intent intent) {
	return mBinder;
    }

    @Override
    public void onCreate() {
	super.onCreate();
	// StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
	// .permitAll().build();
	// StrictMode.setThreadPolicy(policy);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
	startTimer();
	return super.onStartCommand(intent, flags, startId);
    }

    private void startTimer() {
	DbUtils.getVariabiliScadenza(FormationNotify.this);
	Date scadenza = Utils.getScadenza();
	GregorianCalendar cal = new GregorianCalendar();
	cal.setTime(scadenza);
	final String oraScadenza = cal.get(Calendar.HOUR_OF_DAY) + ":" + (cal.get(Calendar.MINUTE) == 0 ? "00" : cal.get(Calendar.MINUTE));
	cal.set(Calendar.HOUR_OF_DAY, scadenza.getHours() - 2);
	Date notificationTime = cal.getTime();
	if (new Date().before(scadenza)) {
	    timer.schedule(new TimerTask() {
		@Override
		public void run() {
		    showNotification(oraScadenza);
		}
	    }, notificationTime);
	    Log.i(getClass().getSimpleName(),
		    "Timer startato. Partira' il " + new SimpleDateFormat("dd/MM/yyyy' ore 'HH:mm").format(notificationTime));
	}
    }

    @Override
    public void onDestroy() {
	super.onDestroy();
	if (timer != null) {
	    timer.cancel();
	}
	Log.i(getClass().getSimpleName(), "Timer stopped.");

    }

    public class MyBinder extends Binder {
	FormationNotify getService() {
	    return FormationNotify.this;
	}
    }

    /**
     * Mostra la notifica nella status bar
     */
    private void showNotification(String oraScadenza) {
	mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	Notification notification = new Notification(R.drawable.app_icon, getString(R.string.notificationTitle), System.currentTimeMillis());
	PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, InviaFormazione.class), 0);
	notification.setLatestEventInfo(FormationNotify.this, getString(R.string.notificationMsg) + " " + oraScadenza,
		"Clicca per inviare la formazione", pendingIntent);
	notification.defaults |= Notification.DEFAULT_VIBRATE | Notification.FLAG_AUTO_CANCEL;
	mNotificationManager.notify(NOTIFICATION_ID, notification);
	timer.cancel();
    }
}
