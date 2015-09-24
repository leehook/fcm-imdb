package it.leehook.fcm.activities;

import it.leehook.fcm.R;
import it.leehook.fcm.adapter.DBAdapter;
import it.leehook.fcm.adapter.PartiteAdapter;
import it.leehook.fcm.bean.Giocatore;
import it.leehook.fcm.bean.Partita;
import it.leehook.fcm.utils.Constants;
import it.leehook.fcm.utils.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import android.app.ListActivity;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
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
public class Coppa extends ListActivity {

    private PartiteAdapter adap;
    private List<Partita> partite;
    private Spinner spinner;
    private DBAdapter adapter;
    private Map<String, String> mappaTurni;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.calendario);
	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	StrictMode.setThreadPolicy(policy);
	adapter = new DBAdapter(getApplicationContext());
	adapter.open();
	mappaTurni = getTurni();
	Integer prossimaGiornata = adapter.getProssimaGiornata();
	spinner = (Spinner) findViewById(R.id.spinnerGiornata);
	Collection<String> coll = mappaTurni.keySet();
	List<String> spinnerArray = new ArrayList<String>();
	spinnerArray.addAll(coll);
	ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
	spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spinner.setAdapter(spinnerArrayAdapter);
	spinner.setSelection(getSpinnerSelection(prossimaGiornata));
	spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

	    public void onNothingSelected(AdapterView<?> parent) {
	    }

	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		Utils.setTurnoCoppa(parent.getItemAtPosition(pos).toString());
		partite = getPartite();
		adap = new PartiteAdapter(Coppa.this, partite);
		setListAdapter(adap);
	    }
	});
	TextView title = (TextView) findViewById(R.id.titleRes);
	title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/romance.ttf"));
	title.setText(getString(R.string.cupGiornata));
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
	Cursor curs = adapter.getData(Constants.TABLE_MATCH, Constants.NUMERO + "=" + getGiornataPerTurno(Utils.getTurnoCoppa()) + " AND "
		+ Constants.ID_CAMPIONATO + "!=" + adapter.getIdCampionato(), null);
	startManagingCursor(curs);
	Partita partita = null;
	while (curs.moveToNext()) {
	    partita = new Partita();
	    // Se non e' stata giocata la giornata, stampo le partite e la
	    // formazione data
	    if (curs.getInt(7) == 0) {
		partita.setSquadraCasa(curs.getString(1));
		partita.setFormazioneCasa(getTabellino(curs.getInt(8), false, true));
		partita.setPanchinaCasa(getTabellino(curs.getInt(8), false, false));
		partita.setSquadraTrasferta(curs.getString(4));
		partita.setFormazioneTrasferta(getTabellino(curs.getInt(9), false, true));
		partita.setPanchinaTrasferta(getTabellino(curs.getInt(9), false, false));
		list.add(partita);
	    } else {
		// Altrimenti stampo il tabellino
		partita.setSquadraCasa(curs.getString(1));
		partita.setGolCasa(curs.getString(2) + " (" + curs.getString(3) + ") ");
		partita.setSquadraTrasferta(curs.getString(4));
		partita.setGolTrasferta(curs.getString(5) + " (" + curs.getString(6) + ") ");
		partita.setFormazioneCasa(getTabellino(curs.getInt(8), true, false));
		partita.setFormazioneTrasferta(getTabellino(curs.getInt(9), true, false));
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
    private List<Giocatore> getTabellino(Integer idSquadra, boolean giocata, boolean isTitolare) {
	List<Giocatore> giocatori = new ArrayList<Giocatore>();
	Cursor curs = adapter.getData(Constants.TABLE_TAB, Constants.NUMERO_GIORNATA + "=" + getGiornataPerTurno(Utils.getTurnoCoppa())
		+ " AND " + Constants.ID_SQUADRA + "=" + idSquadra, Constants.TITOLARE);
	startManagingCursor(curs);
	while (curs.moveToNext()) {
	    // Caso tabellino
	    if (giocata) {
		// Inserisco solo i giocatori titolari
		if (curs.getInt(4) == 1) {
		    Giocatore gioc = new Giocatore();
		    String nomeCompleto = adapter.getNomeByCode(Constants.TABLE_PLAYER, curs.getString(2));
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
		if ((isTitolare && curs.getInt(4) == 0) || (!isTitolare && curs.getInt(4) > 0)) {
		    Giocatore gioc = new Giocatore();
		    String nomeCompleto = adapter.getNomeByCode(Constants.TABLE_PLAYER, curs.getString(2));
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
     * Popola lo spinner relativo al turno di coppa da selezionare
     * 
     * @return
     */
    private Map<String, String> getTurni() {
	Map<String, String> list = new HashMap<String, String>();
	Cursor curs = adapter.getData(Constants.TABLE_CUP, Constants.GIORNATA + " IS NOT NULL", Constants.CODICE);
	startManagingCursor(curs);
	while (curs.moveToNext()) {
	    list.put(curs.getString(1), curs.getString(2));
	}
	curs.close();
	return sortByComparator(list);
    }

    private static Map<String, String> sortByComparator(Map<String, String> unsortMap) {

	// Convert Map to List
	List<Map.Entry<String, String>> list = new LinkedList<Map.Entry<String, String>>(unsortMap.entrySet());

	// Sort list with comparator, to compare the Map values
	Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
	    public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
		return (o1.getValue()).compareTo(o2.getValue());
	    }
	});

	// Convert sorted map back to a Map
	Map<String, String> sortedMap = new LinkedHashMap<String, String>();
	for (Iterator<Map.Entry<String, String>> it = list.iterator(); it.hasNext();) {
	    Map.Entry<String, String> entry = it.next();
	    sortedMap.put(entry.getKey(), entry.getValue());
	}
	return sortedMap;
    }

    /**
     * Ritorna il numero di giornata a cui corrisponde il turno di coppa selezionato
     * 
     * @param turno
     * @return
     */
    private Integer getGiornataPerTurno(String turno) {
	String numeroGiornata = mappaTurni.get(turno);
	return Integer.valueOf(numeroGiornata);
    }

    /**
     * Ritorna il prossimo turno di coppa come default dello spinner
     * 
     * @param prossimaGiornata
     * @return
     */
    private Integer getSpinnerSelection(Integer prossimaGiornata) {
	Integer counter = 0;
	for (String s : mappaTurni.values()) {
	    Integer numero = Integer.valueOf(s);
	    if (prossimaGiornata > numero) {
		counter++;
		continue;
	    } else {
		break;
	    }
	}
	return counter;
    }
}
