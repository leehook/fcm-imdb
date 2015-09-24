package it.leehook.fcm.activities;

import it.leehook.fcm.R;
import it.leehook.fcm.adapter.DBAdapter;
import it.leehook.fcm.impl.ActivitySwipeDetector;
import it.leehook.fcm.utils.Constants;
import it.leehook.fcm.utils.DbUtils;
import it.leehook.fcm.utils.Utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;

/**
 * Activity principale di FCM
 * 
 * @author l.angelini
 * 
 */
public class FCMActivity extends TabActivity {

    private ProgressDialog progress = null;
    private ProgressThread progressThread;
    private DBAdapter adapter;
    private final static int SERIE_A = 1;
    private final static int SQUADRE = 2;
    private final static int COMPETIZIONI = 3;
    private final static int TABELLINI = 4;
    private final static int CLASSIFICA = 5;
    private final static int GIORNATE = 6;
    private final static int FOTO = 7;
    private final static int FINISHING = 8;
    private String error = "";
    private TabHost tabHost;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	ActivitySwipeDetector activitySwipeDetector = new ActivitySwipeDetector(this);
	BugSenseHandler.initAndStartSession(FCMActivity.this, "31c9136b");
	setContentView(R.layout.main_tab);
	tabHost = getTabHost();
	TabSpec lega = tabHost.newTabSpec(Constants.LEGA);
	lega.setIndicator(Constants.LEGA.toUpperCase());
	Intent legaIntent = new Intent(this, Lega.class);
	lega.setContent(legaIntent);
	TabSpec extra = tabHost.newTabSpec(Constants.EXTRA);
	extra.setIndicator(Constants.EXTRA.toUpperCase());
	Intent extraIntent = new Intent(this, Extra.class);
	extra.setContent(extraIntent);
	tabHost.addTab(lega);
	tabHost.addTab(extra);
	for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
	    tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 110;
	}
	setTabColor(tabHost);
	tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
	    @Override
	    public void onTabChanged(String tabId) {
		setTabColor(tabHost);
	    }
	});
	tabHost.setOnTouchListener(activitySwipeDetector);
	// Intent notify = new Intent(FCMActivity.this, FormationNotify.class);
	// startService(notify);
	checkResultsAndInvioFormazione();
    }

    /**
     * Verifica il termine per l'invio della formazione e, se mancano meno di due ore, mostra un messaggio di warning
     */
    private void checkResultsAndInvioFormazione() {
	if (!Utils.checkConn(getApplicationContext())) {
	    runOnUiThread(new Runnable() {
		public void run() {
		    Toast.makeText(FCMActivity.this, getString(R.string.noconnection), Toast.LENGTH_SHORT).show();
		}
	    });
	} else {
	    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
	    SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
	    Boolean notifiche = sPrefs.getBoolean(Constants.PREF_NOTIFICHE, true);
	    if (notifiche) {
		DbUtils.getVariabiliScadenza(FCMActivity.this);
		adapter = new DBAdapter(getApplicationContext());
		adapter.open();
		Date scadenza = Utils.getScadenza();
		if (scadenza != null) {
		    Integer prossimaGiornata = adapter.getProssimaGiornata();
		    String nomeSquadra = sPrefs.getString(Constants.PREF_TEAM, "");
		    GregorianCalendar cal = new GregorianCalendar();
		    cal.setTime(scadenza);
		    final String oraScadenza = cal.get(Calendar.HOUR_OF_DAY) + ":"
			    + (cal.get(Calendar.MINUTE) == 0 ? "00" : cal.get(Calendar.MINUTE));
		    cal.set(Calendar.HOUR_OF_DAY, scadenza.getHours() - 5);
		    Date notificationDate = cal.getTime();
		    Date now = new Date();
		    if (now.before(scadenza) && now.after(notificationDate)) {
			if (!adapter.isTeamComplete(prossimaGiornata, adapter.getIdSquadraByName(nomeSquadra))) {
			    Toast.makeText(this, "Attenzione! Alle ore " + oraScadenza + " scade il termine per l'invio della formazione!",
				    Toast.LENGTH_LONG).show();
			}
		    } else {
			Date lastMatchDate = DbUtils.getLastMatchDate(FCMActivity.this, (prossimaGiornata - 1));
			GregorianCalendar cal2 = new GregorianCalendar(Locale.ENGLISH);
			cal2.setTime(lastMatchDate);
			cal2.set(Calendar.DAY_OF_MONTH, cal2.get(Calendar.DAY_OF_MONTH)
				+ (cal2.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
					|| cal2.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY ? 2 : 3));
			Date check = cal2.getTime();
			if (DateUtils.isSameDay(check, now) && now.getHours() > 13) {
			    Toast.makeText(FCMActivity.this, adapter.getLastResult((prossimaGiornata - 1), StringUtils.trim(nomeSquadra)),
				    Toast.LENGTH_LONG).show();
			}
		    }
		}
		if (adapter != null) {
		    adapter.close();
		}
	    }
	}
    }

    /**
     * Setta lo sfondo dei tab
     * 
     * @param tabhost
     */
    public static void setTabColor(TabHost tabhost) {
	for (int i = 0; i < tabhost.getTabWidget().getChildCount(); i++) {
	    tabhost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#000000")); // unselected
	}
	tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundColor(Color.parseColor("#FE2E2E")); // selected
    }

    @Override
    public void onBackPressed() {
	exit();
    }

    /**
     * Chiede conferma per l'uscita dall'app
     */
    private void exit() {
	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	builder.setMessage(getString(R.string.confirmMsg));
	builder.setCancelable(false);
	builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog, int id) {
		BugSenseHandler.closeSession(FCMActivity.this);
		FCMActivity.this.finish();
	    }
	}).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog, int id) {
		dialog.cancel();
	    }
	});
	AlertDialog alert = builder.create();
	alert.show();
    }

    /**
     * Crea il menu delle opzioni che appare alla pressione del tasto Menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.layout.opt_menu, menu);
	return true;
    }

    /**
     * Apre il menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.prefs:
	    startPreferences();
	    return true;
	case R.id.update:
	    startUpdateData();
	    return true;
	case R.id.exit:
	    exit();
	    return true;
	default:
	    return false;
	}
    }

    @Override
    protected Dialog onCreateDialog(int id) {
	switch (id) {
	case 0:
	    progress = new ProgressDialog(FCMActivity.this);
	    progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	    progress.setMessage(getProgressMessage(SERIE_A));
	    progress.setCancelable(false);
	    return progress;
	default:
	    return null;
	}
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
	switch (id) {
	case 0:
	    progress.setProgress(0);
	    progressThread = new ProgressThread(handler);
	    progressThread.start();
	}
    }

    final Handler handler = new Handler() {
	public void handleMessage(Message msg) {
	    int total = msg.arg1;
	    int messaggio = msg.arg2;
	    progress.setProgress(total);
	    progress.setMessage(getProgressMessage(messaggio));
	    if (total >= 100) {
		dismissDialog(0);
		Toast.makeText(FCMActivity.this, getString(R.string.updateSuccessful), Toast.LENGTH_SHORT).show();
	    }
	}
    };

    /**
     * Effettua i calcoli della percentuale
     * 
     */
    private class ProgressThread extends Thread {
	Handler mHandler;

	ProgressThread(Handler h) {
	    mHandler = h;
	}

	public void run() {
	    try {
		// Se e' vero, svuoto il db per poi ricaricarlo
		if (PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(Constants.PREF_CLEAR_DB, false)) {
		    DbUtils.clearDb(FCMActivity.this);
		}
		// Squadre e giocatori di serie A
		DbUtils.populateSerieA(FCMActivity.this);
		Message msg = mHandler.obtainMessage();
		msg.arg1 = 20;
		msg.arg2 = SQUADRE;
		mHandler.sendMessage(msg);
		// Squadre del fantacalcio
		DbUtils.populateSquadre(FCMActivity.this);
		msg = mHandler.obtainMessage();
		msg.arg1 = 35;
		msg.arg2 = GIORNATE;
		mHandler.sendMessage(msg);
		// Competizioni
		DbUtils.populateCompetizioni(FCMActivity.this);
		msg = mHandler.obtainMessage();
		msg.arg1 = 45;
		msg.arg2 = COMPETIZIONI;
		mHandler.sendMessage(msg);
		// Giornate del fantacalcio
		DbUtils.populateGiornate(FCMActivity.this);
		msg = mHandler.obtainMessage();
		msg.arg1 = 60;
		msg.arg2 = TABELLINI;
		mHandler.sendMessage(msg);
		// Tabellini del fantacalcio
		DbUtils.populateTabellini(FCMActivity.this);
		DbUtils.populateProssimeFormazioni(FCMActivity.this);
		msg = mHandler.obtainMessage();
		msg.arg1 = 75;
		msg.arg2 = FOTO;
		mHandler.sendMessage(msg);
		// Foto
		DbUtils.populateImages(FCMActivity.this);
		msg = mHandler.obtainMessage();
		msg.arg1 = 90;
		msg.arg2 = CLASSIFICA;
		mHandler.sendMessage(msg);
		// Classifica del fantacalcio
		DbUtils.populateClassifica(FCMActivity.this);
		msg = mHandler.obtainMessage();
		msg.arg1 = 100;
		msg.arg2 = FINISHING;
		mHandler.sendMessage(msg);
		resetUpdatePreferences();
	    } catch (Exception e) {
		error = e.getMessage();
		Log.e("UpdateData", e.getMessage(), e);
		runOnUiThread(new Runnable() {
		    public void run() {
			dismissDialog(0);
			Toast.makeText(FCMActivity.this, getString(R.string.error) + getString(R.string.checkhost) + error,
				Toast.LENGTH_LONG).show();
		    }
		});
	    }
	}
    }

    /**
     * Mostra le preferenze
     */
    private void startPreferences() {
	Intent intent = new Intent(this, Preferences.class);
	startActivity(intent);
    }

    /**
     * Aggiorna i dati
     */
    private void startUpdateData() {
	if (Utils.checkConn(getApplicationContext())) {
	    showDialog(0);
	} else {
	    runOnUiThread(new Runnable() {
		public void run() {
		    Toast.makeText(FCMActivity.this, getString(R.string.noconnection), Toast.LENGTH_SHORT).show();
		}
	    });
	}
    }

    /**
     * Imposta il messaggio della progresBar dell'aggiornamento dati
     * 
     * @param msgId
     * @return
     */
    private String getProgressMessage(int msgId) {
	String msg = "";
	switch (msgId) {
	case SERIE_A:
	    msg = getString(R.string.updatingSerieA);
	    break;
	case COMPETIZIONI:
	    msg = getString(R.string.updatingCompetitions);
	    break;
	case SQUADRE:
	    msg = getString(R.string.updatingSquadre);
	    break;
	case GIORNATE:
	    msg = getString(R.string.updatingGiornate);
	    break;
	case TABELLINI:
	    msg = getString(R.string.updatingTabellini);
	    break;
	case CLASSIFICA:
	    msg = getString(R.string.updatingClassifica);
	    break;
	case FOTO:
	    msg = getString(R.string.updatingFoto);
	    break;
	case FINISHING:
	    msg = getString(R.string.updating);
	    break;
	default:
	    break;
	}
	return msg;
    }

    /**
     * Cambia il tab selezionato
     * 
     * @param tabTag
     */
    public void switchTab(String tabTag) {
	tabHost.setCurrentTabByTag(tabTag);
    }

    /**
     * Resetta le selezioni di aggiornamento dei dati
     */
    private void resetUpdatePreferences() {
	SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
	SharedPreferences.Editor editor = sPrefs.edit();
	@SuppressWarnings("unchecked")
	Map<String, Boolean> categories = (Map<String, Boolean>) sPrefs.getAll();
	for (String s : categories.keySet()) {
	    if (StringUtils.equals(s, Constants.PREF_ROSE)) {
		editor.putBoolean(Constants.PREF_ROSE, false);
	    }
	    if (StringUtils.equals(s, Constants.PREF_SERIEA)) {
		editor.putBoolean(Constants.PREF_SERIEA, false);
	    }
	    if (StringUtils.equals(s, Constants.PREF_TABELLINI)) {
		editor.putBoolean(Constants.PREF_TABELLINI, false);
	    }
	    if (StringUtils.equals(s, Constants.PREF_CLEAR_DB)) {
		editor.putBoolean(Constants.PREF_CLEAR_DB, false);
	    }
	}
	editor.commit();
    }
}