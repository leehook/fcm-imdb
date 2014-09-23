package it.leehook.fcm.activities;

import com.bugsense.trace.BugSenseHandler;

import it.leehook.fcm.R;
import it.leehook.fcm.buttons.CalendarioBtn;
import it.leehook.fcm.buttons.ClassificaBtn;
import it.leehook.fcm.buttons.InviaFormBtn;
import it.leehook.fcm.buttons.SquadraBtn;
import it.leehook.fcm.utils.Constants;
import it.leehook.fcm.utils.Utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

public class Lega extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lega);
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
		ClassificaBtn btnClassifica = (ClassificaBtn) findViewById(R.id.classificaBtn);
		btnClassifica.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startClassifica();
			}
		});
		SquadraBtn btnRose = (SquadraBtn) findViewById(R.id.squadreBtn);
		btnRose.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startRose();
			}
		});
		InviaFormBtn labelInvia = (InviaFormBtn) findViewById(R.id.inviaFormBtn);
		labelInvia.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startInviaFormazione();
			}
		});
		CalendarioBtn labelCalendario = (CalendarioBtn) findViewById(R.id.calendarBtn);
		SharedPreferences sPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		if (sPrefs.getBoolean(Constants.PREF_COPPA, true)) {
			labelCalendario.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					AlertDialog.Builder builder = new AlertDialog.Builder(v
							.getContext());
					builder.setMessage(getString(R.string.calType));
					builder.setPositiveButton(getString(R.string.campionato),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									startCampionato();
								}
							}).setNegativeButton(getString(R.string.coppa),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									startCoppa();
								}
							});
					AlertDialog alert = builder.create();
					alert.show();
				}
			});
		} else {
			labelCalendario.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					startCampionato();
				}
			});
		}
	}

	/**
	 * Mostra la pagina di invio della formazione
	 */
	private void startInviaFormazione() {
		if (Utils.checkConn(getApplicationContext())) {
			Intent intent = new Intent(this, InviaFormazione.class);
			startActivity(intent);
		} else {
			Toast.makeText(this, getString(R.string.noconnection),
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Mostra la pagina della classifica
	 */
	private void startClassifica() {
		Intent intent = new Intent(this, Classifica.class);
		startActivity(intent);
	}

	/**
	 * Mostra la pagina delle rose
	 */
	private void startRose() {
		Intent intent = new Intent(this, Squadre.class);
		startActivity(intent);
	}

	/**
	 * Mostra il calendario del campionato
	 */
	private void startCampionato() {
		Intent intent = new Intent(this, Calendario.class);
		startActivity(intent);
	}

	/**
	 * Mostra il calendario della coppa
	 */
	private void startCoppa() {
		Intent intent = new Intent(this, Coppa.class);
		startActivity(intent);
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
		builder.setPositiveButton(getString(R.string.yes),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						BugSenseHandler.closeSession(Lega.this);
						Lega.this.finish();
					}
				}).setNegativeButton(getString(R.string.no),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

}
