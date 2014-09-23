package it.leehook.fcm.activities;

import it.leehook.fcm.R;
import it.leehook.fcm.adapter.DBAdapter;
import it.leehook.fcm.adapter.PartiteAdapter;
import it.leehook.fcm.bean.Giocatore;
import it.leehook.fcm.bean.Partita;
import it.leehook.fcm.utils.Constants;
import it.leehook.fcm.utils.DbUtils;
import it.leehook.fcm.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Attivita' relativa ala visualizzazione dei risultati
 * 
 * @author l.angelini
 */
public class Calendario extends ListActivity {

	private SharedPreferences sPrefs;
	private PartiteAdapter adap;
	private List<Partita> partite;
	private Spinner spinner;
	private DBAdapter adapter;
	private Integer prossimaGiornata;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sPrefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		setContentView(R.layout.calendario);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		adapter = new DBAdapter(getApplicationContext());
		adapter.open();
		spinner = (Spinner) findViewById(R.id.spinnerGiornata);
		ArrayAdapter<Integer> spinnerArrayAdapter = new ArrayAdapter<Integer>(
				this, android.R.layout.simple_spinner_item, getGiornate());
		spinnerArrayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(spinnerArrayAdapter);
		/*
		 * Setto come valore di default dello spinner di ricerca giornata
		 * l'ultima giocata, a meno che oggi non sia il giorno della partita. In
		 * questo caso setto il valore della prossima giornata
		 */
		prossimaGiornata = adapter.getProssimaGiornata();
		int defaultValue = prossimaGiornata
				- Integer.valueOf(sPrefs.getString(Constants.PREF_PRIMA, "1"))
				- 1;
		Date lastMatchDate = DbUtils.getLastMatchDate(Calendario.this,
				(prossimaGiornata - 1));
		GregorianCalendar cal2 = new GregorianCalendar(Locale.ENGLISH);
		cal2.setTime(lastMatchDate);
		cal2.set(
				Calendar.DAY_OF_MONTH,
				cal2.get(Calendar.DAY_OF_MONTH)
						+ (cal2.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
								|| cal2.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY ? 2
								: 3));
		Date check = cal2.getTime();
		if (new Date().after(check)) {
			defaultValue += 1;
		}
		spinner.setSelection(defaultValue);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onNothingSelected(AdapterView<?> parent) {
			}

			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				Utils.setGiornata(Integer.valueOf(parent.getItemAtPosition(pos)
						.toString()));
				partite = getPartite();
				adap = new PartiteAdapter(Calendario.this, partite);
				setListAdapter(adap);
			}
		});
		TextView title = (TextView) findViewById(R.id.titleRes);
		title.setTypeface(Typeface.createFromAsset(getAssets(),
				"fonts/romance.ttf"));
		title.setText(getString(R.string.resGiornata));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (adapter != null) {
			adapter.close();
		}
	}

	/**
	 * Restituisce la lista delle partita giocate nella giornata X
	 * 
	 * @return
	 */
	private List<Partita> getPartite() {
		List<Partita> list = new ArrayList<Partita>();
		Cursor curs = adapter.getData(Constants.TABLE_MATCH, Constants.NUMERO
				+ "=" + Utils.getGiornata() + " AND " + Constants.ID_CAMPIONATO
				+ "=" + adapter.getIdCampionato(), null);
		startManagingCursor(curs);
		Partita partita = null;
		while (curs.moveToNext()) {
			partita = new Partita();
			// Se non e' stata giocata la giornata, stampo le partite e la
			// formazione data
			if (curs.getInt(7) == 0) {
				partita.setSquadraCasa(curs.getString(1));
				partita.setFormazioneCasa(getTabellino(curs.getInt(8), false,
						true));
				partita.setPanchinaCasa(getTabellino(curs.getInt(8), false,
						false));
				partita.setSquadraTrasferta(curs.getString(4));
				partita.setFormazioneTrasferta(getTabellino(curs.getInt(9),
						false, true));
				partita.setPanchinaTrasferta(getTabellino(curs.getInt(9),
						false, false));
				list.add(partita);
			} else {
				// Altrimenti stampo il tabellino
				partita.setSquadraCasa(curs.getString(1));
				partita.setGolCasa(curs.getString(2) + " (" + curs.getString(3)
						+ ") ");
				partita.setSquadraTrasferta(curs.getString(4));
				partita.setGolTrasferta(curs.getString(5) + " ("
						+ curs.getString(6) + ") ");
				partita.setFormazioneCasa(getTabellino(curs.getInt(8), true,
						false));
				partita.setFormazioneTrasferta(getTabellino(curs.getInt(9),
						true, false));
				partita.setModificatoreCasa(curs.getString(10));
				partita.setModificatoreTrasferta(curs.getString(11));
				list.add(partita);
			}
		}
		curs.close();
		return list;
	}

	/**
	 * Popola il tabellino della giornata
	 * 
	 * @param idSquadra
	 * @return
	 */
	private List<Giocatore> getTabellino(Integer idSquadra, boolean giocata,
			boolean isTitolare) {
		List<Giocatore> giocatori = new ArrayList<Giocatore>();
		Cursor curs = adapter.getData(Constants.TABLE_TAB,
				Constants.NUMERO_GIORNATA + "=" + Utils.getGiornata() + " AND "
						+ Constants.ID_SQUADRA + "=" + idSquadra,
				Constants.TITOLARE);
		startManagingCursor(curs);
		while (curs.moveToNext()) {
			// Caso tabellino
			if (giocata) {
				// Inserisco solo i giocatori titolari
				if (curs.getInt(4) == 1) {
					Giocatore gioc = new Giocatore();
					String nomeCompleto = adapter.getNomeByCode(
							Constants.TABLE_PLAYER, curs.getString(2));
					if (!StringUtils.isBlank(nomeCompleto)) {
						String[] nomeDiviso = nomeCompleto.trim().split("\\s");
						String nomeStampato = "";
						for (String s : nomeDiviso) {
							if (StringUtils.isAllUpperCase(s.replace("'", ""))) {
								nomeStampato += " " + s;
							} else {
								nomeStampato += " " + s.substring(0, 1) + ".";
							}
						}
						gioc.setCognome(nomeStampato);
						gioc.setRuolo(adapter.getRoleByCode(curs.getString(2)));
						gioc.setVoto(curs.getString(3));
						giocatori.add(gioc);
					} else {
						gioc.setCognome("----------------------");
						gioc.setRuolo(0);
						gioc.setVoto("-");
						giocatori.add(gioc);
					}
				}
			} else {
				// Caso prossima giornata
				if ((isTitolare && curs.getInt(4) == 0)
						|| (!isTitolare && curs.getInt(4) > 0)) {
					Giocatore gioc = new Giocatore();
					String nomeCompleto = adapter.getNomeByCode(
							Constants.TABLE_PLAYER, curs.getString(2));
					if (!StringUtils.isEmpty(nomeCompleto)) {
						String[] nomeDiviso = nomeCompleto.trim().split("\\s");
						String nomeStampato = "";
						for (String s : nomeDiviso) {
							if (StringUtils.isAllUpperCase(s.replace("'", ""))) {
								nomeStampato += " " + s;
							} else {
								nomeStampato += " " + s.substring(0, 1) + ".";
							}
						}
						gioc.setCognome(nomeStampato);
						gioc.setRuolo(adapter.getRoleByCode(curs.getString(2)));
						giocatori.add(gioc);
					} else {
						gioc.setCognome("----------------------");
						gioc.setRuolo(0);
						giocatori.add(gioc);
					}
				}
			}
		}
		curs.close();
		return giocatori;
	}

	/**
	 * Popola lo spinner relativo alla giornata da selezionare
	 * 
	 * @return
	 */
	private List<Integer> getGiornate() {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = Integer.valueOf(sPrefs
				.getString(Constants.PREF_PRIMA, "1")); i <= Integer
				.valueOf(sPrefs.getString(Constants.PREF_ULTIMA, "38")); i++) {
			list.add(i);
		}
		return list;
	}
}
