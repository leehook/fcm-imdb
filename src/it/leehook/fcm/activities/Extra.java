package it.leehook.fcm.activities;

import it.leehook.fcm.R;
import it.leehook.fcm.buttons.GuestbookBtn;
import it.leehook.fcm.buttons.ProbabiliBtn;
import it.leehook.fcm.buttons.StatisticheBtn;
import it.leehook.fcm.buttons.VotiUfficiosiBtn;
import it.leehook.fcm.utils.Utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;

public class Extra extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.extra);
	setButtons();
    }

    @Override
    protected void onResume() {
	setButtons();
	super.onResume();
    }

    /**
     * Setta le immagini dei bottoni
     */
    private void setButtons() {
	StatisticheBtn labelStats = (StatisticheBtn) findViewById(R.id.statsBtn);
	labelStats.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		startStatistiche();
	    }
	});
	GuestbookBtn labelGuest = (GuestbookBtn) findViewById(R.id.guestbookBtn);
	labelGuest.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		startGuestbook();
	    }
	});
	ProbabiliBtn labelProb = (ProbabiliBtn) findViewById(R.id.probabiliBtn);
	labelProb.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		startProbabili();
	    }
	});

	VotiUfficiosiBtn labelVoti = (VotiUfficiosiBtn) findViewById(R.id.votiBtn);
	labelVoti.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
		startVoti();
	    }
	});
    }

    /**
     * Mostra le statistiche (Schedina/Se Avessi Avuto)
     */
    private void startStatistiche() {
	if (Utils.checkConn(getApplicationContext())) {
	    Intent intent = new Intent(this, Statistiche.class);
	    startActivity(intent);
	} else {
	    Toast.makeText(this, getString(R.string.noconnection), Toast.LENGTH_SHORT).show();
	}
    }

    /**
     * Mostra la pagina del guestbook
     */
    private void startGuestbook() {
	if (Utils.checkConn(getApplicationContext())) {
	    Intent intent = new Intent(this, Guestbook.class);
	    startActivity(intent);
	} else {
	    Toast.makeText(this, getString(R.string.noconnection), Toast.LENGTH_SHORT).show();
	}
    }

    /**
     * Mostra la pagina delle probabili formazioni
     */
    private void startProbabili() {
	if (Utils.checkConn(getApplicationContext())) {
	    Intent intent = new Intent(this, ProbabiliFormazioni.class);
	    startActivity(intent);
	} else {
	    Toast.makeText(this, getString(R.string.noconnection), Toast.LENGTH_SHORT).show();
	}
    }

    /**
     * Mostra la pagina dei voti ufficiosi
     */
    private void startVoti() {
	if (Utils.checkConn(getApplicationContext())) {
	    Intent intent = new Intent(this, VotiUfficiosi.class);
	    startActivity(intent);
	} else {
	    Toast.makeText(this, getString(R.string.noconnection), Toast.LENGTH_SHORT).show();
	}
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
		BugSenseHandler.closeSession(Extra.this);
		Extra.this.finish();
	    }
	}).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog, int id) {
		dialog.cancel();
	    }
	});
	AlertDialog alert = builder.create();
	alert.show();
    }

}
