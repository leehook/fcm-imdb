package it.leehook.fcm.utils;

import it.leehook.fcm.adapter.DBAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class DbUtils {

    private static DBAdapter dbAdapter;

    private static String host;

    private static SharedPreferences sPrefs;

    /**
     * Popola la classifica
     */
    public static void populateClassifica(Context ctx) throws Exception {
	dbAdapter = new DBAdapter(ctx);
	dbAdapter.open();
	URL url = null;
	String classificaUrl = host + File.separator + Constants.JS_FOLDER + File.separator + Constants.CLASSIFICA_FILE;
	Integer idCampionato = dbAdapter.getIdCampionato();
	try {
	    // Recupero il js relativo alla classifica dal sito
	    url = new URL(classificaUrl);
	    InputStream inputStream = (InputStream) url.getContent();
	    BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
	    String strLine;
	    while ((strLine = in.readLine()) != null) {
		if (strLine.contains("]=new C(")) {
		    // Spezzetto la riga di una squadra in modo da ottenere le
		    // informazioni che mi interessano
		    String[] singolaRiga = strLine.split(",");
		    if (Integer.valueOf(singolaRiga[2]) == idCampionato) {
			Integer idSquadra = Integer.valueOf(singolaRiga[0].substring(singolaRiga[0].length() - 1));
			Map<String, String> values = new HashMap<String, String>();
			values.put(Constants.PUNTI, singolaRiga[6]);
			values.put(Constants.TOT_PUNTI, singolaRiga[29]);
			values.put(Constants.VINTE,
				"" + Integer.valueOf(Integer.valueOf(singolaRiga[7]) + Integer.valueOf(singolaRiga[10])));
			values.put(Constants.PAREGGIATE,
				"" + Integer.valueOf(Integer.valueOf(singolaRiga[8]) + Integer.valueOf(singolaRiga[11])));
			values.put(Constants.PERSE,
				"" + Integer.valueOf(Integer.valueOf(singolaRiga[9]) + Integer.valueOf(singolaRiga[12])));
			values.put(Constants.GOL_FATTI,
				"" + Integer.valueOf(Integer.valueOf(singolaRiga[13]) + Integer.valueOf(singolaRiga[15])));
			values.put(Constants.GOL_SUBITI,
				"" + Integer.valueOf(Integer.valueOf(singolaRiga[14]) + Integer.valueOf(singolaRiga[16])));
			dbAdapter.updateData(Constants.TABLE_SQ, values, Constants.ID_SQUADRA + "=" + idSquadra);
		    }
		}
	    }
	} catch (MalformedURLException e) {
	    throw e;
	} catch (IOException e) {
	    throw e;
	} finally {
	    dbAdapter.close();
	}
    }

    /**
     * Popola la mappa delle squadre e i risultati delle giornata giocate
     */
    public static void populateGiornate(Context ctx) throws Exception {
	dbAdapter = new DBAdapter(ctx);
	dbAdapter.open();
	String risultatiUrl = host + File.separator + Constants.JS_FOLDER + File.separator + Constants.CALENDARIO_FILE;
	Integer idCampionato = dbAdapter.getIdCampionato();
	URL url = null;
	try {
	    // Recupero il js relativo al calendario dal sito
	    url = new URL(risultatiUrl);
	    InputStream inputStream = (InputStream) url.getContent();
	    BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
	    String strLine;
	    Integer startLine = null;
	    Integer numeroSquadre = dbAdapter.getSquadre().size();
	    Boolean mapFull = Utils.getMappaSquadre().size() == numeroSquadre ? true : false;
	    boolean prossimaSettata = false;
	    while ((strLine = in.readLine()) != null) {
		if (!mapFull && strLine.startsWith("var")) {
		    if (startLine != null) {
			// Popolo la mappa con i nomi delle fantasquadre
			Map<String, String> squadre = new HashMap<String, String>();
			for (int i = startLine.intValue(); i < startLine + numeroSquadre.intValue(); i++) {
			    String codiceSq = strLine.substring(strLine.indexOf('x'), strLine.indexOf('='));
			    String nomeSq = strLine.substring(strLine.indexOf('"') + 1, strLine.length() - 1);
			    squadre.put(codiceSq, nomeSq);
			    strLine = in.readLine();
			}
			Utils.setMappaSquadre(squadre);
			mapFull = true;
		    } else {
			if (strLine.contains("Campionato")) {
			    startLine = Integer.valueOf(strLine.substring(6, 7));
			}
		    }
		} else if (strLine.startsWith("a[")) {
		    Map<String, String> values = new HashMap<String, String>();
		    String[] singolaRiga = strLine.split(",");
		    // Righe relative alla giornata selezionata
		    Boolean giornataGiocata = Integer.valueOf(singolaRiga[3]) == 1 ? true : false;
		    Integer giocataDb = dbAdapter.isMatchPlayed(singolaRiga[10], Utils.getMappaSquadre().get(singolaRiga[15]), Utils
			    .getMappaSquadre().get(singolaRiga[16]), singolaRiga[6]);
		    values.put(Constants.NUMERO, singolaRiga[10]);
		    values.put(Constants.GIOCATA, singolaRiga[3]);
		    if (!StringUtils.equals(singolaRiga[6], String.valueOf(dbAdapter.getIdCampionato()))) {
			Map<String, String> mapCoppa = new HashMap<String, String>();
			mapCoppa.put(Constants.GIORNATA, singolaRiga[10]);
			dbAdapter.updateData(Constants.TABLE_CUP, mapCoppa, Constants.CODICE + "='" + singolaRiga[9] + "'");
		    }
		    if (!prossimaSettata && singolaRiga[3].equals("0") && Integer.valueOf(singolaRiga[6]) == idCampionato) {
			Map<String, String> pref = new HashMap<String, String>();
			pref.put(Constants.VALORE, singolaRiga[10]);
			dbAdapter.updateData(Constants.TABLE_PREFS, pref, Constants.NOME + "='" + Constants.PROSSIMA_GIORNATA + "'");
			prossimaSettata = true;
		    }
		    switch (giocataDb.intValue()) {
		    case 0:
			if (giornataGiocata) {
			    values.put(Constants.ID_CAMPIONATO, singolaRiga[6]);
			    values.put(Constants.SQ_CASA, Utils.getMappaSquadre().get(singolaRiga[15]));
			    values.put(Constants.GOL_CASA, singolaRiga[17]);
			    values.put(Constants.PNT_CASA, singolaRiga[21]);
			    values.put(Constants.SQ_TRASF, Utils.getMappaSquadre().get(singolaRiga[16]));
			    values.put(Constants.GOL_TRASF, singolaRiga[18]);
			    values.put(Constants.PNT_TRASF, singolaRiga[22]);
			    values.put(Constants.ID_SQUADRA_CASA, singolaRiga[11]);
			    values.put(Constants.ID_SQUADRA_TRASF, singolaRiga[12]);
			    values.put(Constants.MOD_CASA, new BigDecimal(singolaRiga[21]).subtract(new BigDecimal(singolaRiga[19]))
				    .toBigInteger().toString());
			    values.put(Constants.MOD_TRASF, new BigDecimal(singolaRiga[22]).subtract(new BigDecimal(singolaRiga[20]))
				    .toBigInteger().toString());
			    dbAdapter.updateData(Constants.TABLE_MATCH, values, Constants.NUMERO + "=" + singolaRiga[10] + " AND "
				    + Constants.ID_SQUADRA_CASA + "=" + singolaRiga[11] + " AND " + Constants.ID_SQUADRA_TRASF + "="
				    + singolaRiga[12] + " AND " + Constants.ID_CAMPIONATO + "=" + singolaRiga[6]);
			}
			break;
		    case 1:
			break;
		    case 2:
			values.put(Constants.ID_CAMPIONATO, singolaRiga[6]);
			values.put(Constants.SQ_CASA, Utils.getMappaSquadre().get(singolaRiga[15]));
			values.put(Constants.SQ_TRASF, Utils.getMappaSquadre().get(singolaRiga[16]));
			values.put(Constants.ID_SQUADRA_CASA, singolaRiga[11]);
			values.put(Constants.ID_SQUADRA_TRASF, singolaRiga[12]);
			if (giornataGiocata) {
			    values.put(Constants.GOL_CASA, singolaRiga[17]);
			    values.put(Constants.PNT_CASA, singolaRiga[21]);
			    values.put(Constants.GOL_TRASF, singolaRiga[18]);
			    values.put(Constants.PNT_TRASF, singolaRiga[22]);
			    values.put(Constants.MOD_CASA, new BigDecimal(singolaRiga[21]).subtract(new BigDecimal(singolaRiga[19]))
				    .toBigInteger().toString());
			    values.put(Constants.MOD_TRASF, new BigDecimal(singolaRiga[22]).subtract(new BigDecimal(singolaRiga[20]))
				    .toBigInteger().toString());
			}
			dbAdapter.insertData(Constants.TABLE_MATCH, values);
			break;
		    default:
			break;
		    }
		} else {
		    continue;
		}
	    }
	} catch (MalformedURLException e) {
	    throw e;
	} catch (IOException e) {
	    throw e;
	} finally {
	    dbAdapter.close();
	}
    }

    /**
     * Popola le formazioni schierate per l'ultima giornata
     */
    public static void populateProssimeFormazioni(Context ctx) throws Exception {
	dbAdapter = new DBAdapter(ctx);
	dbAdapter.open();
	URL url = null;
	String formPrefix = host + File.separator + Constants.JS_FOLDER + File.separator + Constants.FORMAZIONI_FILE;
	Map<String, String> values = new HashMap<String, String>();
	Integer prossimaGiornata = dbAdapter.getProssimaGiornata();
	Integer idCampionato = dbAdapter.getIdCampionato();
	dbAdapter.deleteData(Constants.TABLE_TAB, Constants.NUMERO_GIORNATA + "=" + prossimaGiornata);
	try {
	    url = new URL(formPrefix.concat(prossimaGiornata + ".js"));
	    InputStream inputStream = (InputStream) url.getContent();
	    BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
	    String sb = Utils.getStringFromStringBuffer(in);
	    for (String strLine : sb.split("\n")) {
		if (strLine.contains("]=new Z(")) {
		    values.clear();
		    String[] singolaRiga = strLine.split(",");
		    if (!dbAdapter.isTeamComplete(prossimaGiornata, Integer.valueOf(singolaRiga[1]))) {
			values.put(Constants.NUMERO_GIORNATA, "" + prossimaGiornata);
			values.put(Constants.ID_CAMPIONATO, "" + idCampionato);
			values.put(Constants.ID_SQUADRA, singolaRiga[1]);
			values.put(Constants.CODICE, singolaRiga[3]);
			values.put(Constants.TITOLARE, singolaRiga[6]);
			dbAdapter.insertData(Constants.TABLE_TAB, values);
		    }
		}
	    }
	} catch (MalformedURLException e) {
	    throw e;
	} catch (IOException e) {
	    throw e;
	} finally {
	    dbAdapter.close();
	}
    }

    /**
     * Popola le tabelle iniziali del Db
     */
    public static void populateSerieA(Context ctx) throws Exception {
	dbAdapter = new DBAdapter(ctx);
	dbAdapter.open();
	// Riscrivo le preferenze
	dbAdapter.deleteData(Constants.TABLE_PREFS, Constants.NOME + "='" + Constants.PROSSIMA_GIORNATA + "'");
	Map<String, String> prefs = new HashMap<String, String>();
	prefs.put(Constants.NOME, Constants.PROSSIMA_GIORNATA);
	prefs.put(Constants.VALORE, "" + 1);
	dbAdapter.insertData(Constants.TABLE_PREFS, prefs);
	host = getSprefs(ctx).getString(Constants.PREF_HOST, Constants.DEFAULT_HOST);
	if (!host.startsWith(Constants.HTTP)) {
	    host = Constants.HTTP.concat(host);
	}
	try {
	    // Se e' selezionato il tasto di aggiornamento serie A, cancello
	    // i giocatori presenti per poterli ricaricare
	    if (getSprefs(ctx).getBoolean(Constants.PREF_SERIEA, false)) {
		dbAdapter.deleteData(Constants.TABLE_PLAYER, null);
	    }
	    if (!dbAdapter.isSerieAFull()) {
		String datiUrl = host + File.separator + Constants.JS_FOLDER + File.separator + Constants.SERIE_A_FILE;
		URL url = null;
		url = new URL(datiUrl);
		InputStream inputStream = (InputStream) url.getContent();
		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
		String strLine;
		while ((strLine = in.readLine()) != null) {
		    String[] splitStr = strLine.split("=");
		    if (strLine.startsWith(Constants.PL_PREFIX)) {
			// Popolo la tabella dei giocatori
			Map<String, String> values = new HashMap<String, String>();
			values.put(Constants.CODICE, splitStr[0]);
			values.put(Constants.NOME, splitStr[1].replace('"', ' '));
			dbAdapter.insertData(Constants.TABLE_PLAYER, values);
		    } else if (strLine.startsWith(Constants.SQ_PREFIX)) {
			// Popolo la tabella delle squadre
			Map<String, String> values = new HashMap<String, String>();
			values.put(Constants.CODICE, splitStr[0]);
			values.put(Constants.NOME, splitStr[1].replace('"', ' '));
			dbAdapter.insertData(Constants.TABLE_TEAM, values);
		    }
		}
	    }
	    // Carico la tabella dei turni di coppa
	    if (!dbAdapter.isCoppaFull()) {
		String compUrl = host + File.separator + Constants.JS_FOLDER + File.separator + Constants.CALENDARIO_FILE;
		URL url = new URL(compUrl);
		InputStream inputStream = (InputStream) url.getContent();
		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
		String strLine;
		while ((strLine = in.readLine()) != null) {
		    if (StringUtils.startsWith(strLine, Constants.COMP_PREFIX)) {
			String[] splitStr = strLine.split("=");
			Map<String, String> values = new HashMap<String, String>();
			values.put(Constants.CODICE, StringUtils.replace(splitStr[0], "var ", ""));
			values.put(Constants.NOME, StringUtils.substring(splitStr[1], 1, StringUtils.length(splitStr[1]) - 1));
			dbAdapter.insertData(Constants.TABLE_CUP, values);
		    }
		}
	    }
	} catch (MalformedURLException e) {
	    throw e;
	} catch (IOException e) {
	    throw e;
	} finally {
	    dbAdapter.close();
	}
    }

    /**
     * Popola le squadre
     */
    public static void populateSquadre(Context ctx) throws Exception {
	dbAdapter = new DBAdapter(ctx);
	dbAdapter.open();
	try {
	    // Se e' selezionato il tasto di aggiornamento rose, cancello le
	    // squadre per poterle ricaricare
	    if (getSprefs(ctx).getBoolean(Constants.PREF_ROSE, false)) {
		dbAdapter.deleteData(Constants.TABLE_SQ, null);
	    }
	    if (!dbAdapter.isSquadreFull()) {
		URL url = null;
		String roseUrl = host + File.separator + Constants.JS_FOLDER + File.separator + Constants.SQUADRE_FILE;
		url = new URL(roseUrl);
		InputStream inputStream = (InputStream) url.getContent();
		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
		String sb = Utils.getStringFromStringBuffer(in);
		for (String strLine : sb.split("\n")) {
		    if (strLine.contains("]=new F(")) {
			String[] singolaRiga = strLine.split(",");
			Map<String, String> values = new HashMap<String, String>();
			values.put(Constants.ID_SQUADRA, singolaRiga[0].substring(singolaRiga[0].length() - 1));
			values.put(Constants.NOME, singolaRiga[1].replace('"', ' '));
			values.put(Constants.PRESIDENTE, singolaRiga[2].replace('"', ' '));
			values.put(Constants.MAIL, singolaRiga[6].replace('"', ' '));
			values.put(Constants.CREDITI, singolaRiga[8].substring(0, singolaRiga[8].length() - 1));
			dbAdapter.insertData(Constants.TABLE_SQ, values);
		    } else if (strLine.contains("]=new R(")) {
			String[] singolaRiga = strLine.split(",");
			// Aggiungo solo i giocatori non svincolati
			// (singolaRiga[5]
			// = 0)
			if (Integer.valueOf(singolaRiga[5]) == 0) {
			    Map<String, String> values = new HashMap<String, String>();
			    values.put(Constants.RUOLO, singolaRiga[1]);
			    values.put(Constants.PREZZO, singolaRiga[7]);
			    values.put(Constants.ID_SQUADRA, singolaRiga[0].substring(singolaRiga[0].length() - 1));
			    values.put(Constants.SQUADRA_DI_A, singolaRiga[3]);
			    dbAdapter.updateData(Constants.TABLE_PLAYER, values, Constants.CODICE + "='" + singolaRiga[2] + "'");
			}
		    }
		}
	    }
	} catch (MalformedURLException e) {
	    throw e;
	} catch (IOException e) {
	    throw e;
	} finally {
	    dbAdapter.close();
	}
    }

    /**
     * Popola la tabella delle competizioni
     * 
     * @param ctx
     * @throws Exception
     */
    public static void populateCompetizioni(Context ctx) throws Exception {
	dbAdapter = new DBAdapter(ctx);
	dbAdapter.open();
	try {
	    URL url = null;
	    // Inserisco il codice delle competizioni
	    String competizioniUrl = host + File.separator + Constants.JS_FOLDER + File.separator + Constants.COMPETIZIONI_FILE;
	    url = new URL(competizioniUrl);
	    InputStream inputStream = (InputStream) url.getContent();
	    BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
	    String sb = Utils.getStringFromStringBuffer(in);
	    Integer idCampionato = 0;
	    for (String strLine : sb.split("\n")) {
		if (StringUtils.contains(strLine, "Campionato")) {
		    String rigaCampionato = strLine.split(",")[0];
		    idCampionato = Integer.valueOf(rigaCampionato.substring(rigaCampionato.lastIndexOf("(") + 1));
		    Map<String, String> values = new HashMap<String, String>();
		    values.put(Constants.ID_CAMPIONATO, "" + idCampionato);
		    dbAdapter.updateData(Constants.TABLE_SQ, values, null);
		}
	    }
	} catch (MalformedURLException e) {
	    throw e;
	} catch (IOException e) {
	    throw e;
	} finally {
	    dbAdapter.close();
	}
    }

    /**
     * Popola i tabellini delle giornate giocate
     */
    public static void populateTabellini(Context ctx) throws Exception {
	dbAdapter = new DBAdapter(ctx);
	dbAdapter.open();
	URL url = null;
	String tabPrefix = host + File.separator + Constants.JS_FOLDER + File.separator + Constants.RISULTATI_FILE;
	Map<String, String> values = new HashMap<String, String>();
	// Determino la prossima giornata
	Integer prossimaGiornata = dbAdapter.getProssimaGiornata();
	// Se e' selezionato il tasto di aggiornamento tabellini, cancello i
	// tabellini per poterli ricaricare
	if (getSprefs(ctx).getBoolean(Constants.PREF_TABELLINI, false)) {
	    dbAdapter.deleteData(Constants.TABLE_TAB, null);
	}
	try {
	    for (int i = Integer.valueOf(getSprefs(ctx).getString(Constants.PREF_PRIMA, "1")); i < prossimaGiornata; i++) {
		// Per ogni giornata, se il tabellino non esiste, lo creo
		if (!dbAdapter.isTabSaved(i)) {
		    values.clear();
		    values.put(Constants.NUMERO_GIORNATA, "" + i);
		    url = new URL(tabPrefix.concat(i + ".js"));
		    InputStream inputStream = (InputStream) url.getContent();
		    BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
		    String sb = Utils.getStringFromStringBuffer(in);
		    List<String> squadreInserite = new ArrayList<String>();
		    for (String strLine : sb.split("\n")) {
			if (strLine.contains("]=new T(")) {
			    String[] singolaRiga = strLine.split(",");
			    if (!squadreInserite.contains(singolaRiga[1])) {
				values.put(Constants.ID_SQUADRA, singolaRiga[1]);
				String[] players = singolaRiga[3].substring(1, singolaRiga[3].length() - 1).split("%");
				String[] voti = singolaRiga[8].substring(1, singolaRiga[8].length() - 1).split("%");
				for (int j = 0; j < players.length; j++) {
				    Integer tit = 0;
				    values.put(Constants.CODICE, Constants.PL_PREFIX + players[j]);
				    values.put(Constants.VOTO, voti[j]);
				    if (j < 11) {
					tit = 1;
				    }
				    values.put(Constants.TITOLARE, "" + tit);
				    dbAdapter.insertData(Constants.TABLE_TAB, values);
				}
				squadreInserite.add(singolaRiga[1]);
			    }
			}
		    }
		} else if (i == (prossimaGiornata - 1)) {
		    // Se invece esiste e mi trovo nella giornata in corso, lo
		    // aggiorno sostituendo le formazioni inserite con il
		    // tabellino
		    url = new URL(tabPrefix.concat(i + ".js"));
		    InputStream inputStream = (InputStream) url.getContent();
		    BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
		    String sb = Utils.getStringFromStringBuffer(in);
		    for (String strLine : sb.split("\n")) {
			if (strLine.contains("]=new T(")) {
			    String[] singolaRiga = strLine.split(",");
			    dbAdapter.deleteData(Constants.TABLE_TAB, Constants.NUMERO_GIORNATA + "=" + i + " AND " + Constants.ID_SQUADRA
				    + "=" + singolaRiga[1]);
			    String[] players = singolaRiga[3].substring(1, singolaRiga[3].length() - 1).split("%");
			    String[] voti = singolaRiga[8].substring(1, singolaRiga[8].length() - 1).split("%");
			    for (int j = 0; j < players.length; j++) {
				values.clear();
				Integer tit = 0;
				values.put(Constants.VOTO, voti[j]);
				if (j < 11) {
				    tit = 1;
				}
				values.put(Constants.TITOLARE, "" + tit);
				values.put(Constants.NUMERO_GIORNATA, "" + i);
				values.put(Constants.ID_SQUADRA, singolaRiga[1]);
				values.put(Constants.CODICE, Constants.PL_PREFIX + players[j]);
				dbAdapter.insertData(Constants.TABLE_TAB, values);
			    }
			}
		    }
		}
	    }
	} catch (MalformedURLException e) {
	    throw e;
	} catch (IOException e) {
	    throw e;
	} finally {
	    dbAdapter.close();
	}
    }

    /**
     * Scarica le foto degli allenatori e i loghi delle squadre
     * 
     * @param ctx
     */
    public static void populateImages(Context ctx) throws Exception {
	downloadImages(ctx, Constants.ALL_FOLDER);
	downloadImages(ctx, Constants.LOGHI_FOLDER);
    }

    /**
     * Esegue il download delle foto
     * 
     * @param dbAdapter
     * @param tipoFoto
     */
    private static void downloadImages(Context ctx, String tipoFoto) throws Exception {
	URL url = null;
	File imgPath = new File(Constants.IMG_PATH + tipoFoto);
	int size = 0;
	if (imgPath.exists()) {
	    size = imgPath.listFiles().length;
	} else {
	    imgPath.mkdirs();
	}
	if (size > 0) {
	    return;
	} else {
	    dbAdapter = new DBAdapter(ctx);
	    dbAdapter.open();
	    List<String> squadre = dbAdapter.getSquadre();
	    for (String s : squadre) {
		String nomeFile = s.toLowerCase().replaceAll("\\s+", "")
			+ (StringUtils.equals(tipoFoto, Constants.ALL_FOLDER) ? ".png" : ".gif");
		URI uri = new URI("http", host.replace(Constants.HTTP, ""), File.separator + Constants.IMG_FOLDER + File.separator
			+ tipoFoto + File.separator + s.substring(1, s.length() - 1)
			+ (StringUtils.equals(tipoFoto, Constants.ALL_FOLDER) ? ".png" : ".gif"), null);
		url = uri.toURL();
		InputStream inputStream = (InputStream) url.getContent();
		FileOutputStream fos = new FileOutputStream(Constants.IMG_PATH + tipoFoto + File.separator + nomeFile);
		byte[] buffer = new byte[4096];
		int len;
		while ((len = inputStream.read(buffer)) > 0) {
		    fos.write(buffer, 0, len);
		}
	    }
	    dbAdapter.close();
	}
    }

    /**
     * Valorizza la data di scadenza per l'invio della formazione
     */
    public static void getVariabiliScadenza(Context ctx) {
	String host = getSprefs(ctx).getString(Constants.PREF_HOST, Constants.DEFAULT_HOST);
	if (!host.startsWith(Constants.HTTP)) {
	    host = Constants.HTTP.concat(host);
	}
	int hour = 0;
	int minutes = 0;
	int days = 0;
	int month = 0;
	int year = 0;
	try {
	    URL url = new URL(host + File.separator + Constants.JS_FOLDER + File.separator + Constants.VARIABILI_FILE);
	    InputStream inputStream = (InputStream) url.getContent();
	    BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
	    String strLine;
	    while ((strLine = in.readLine()) != null) {
		String[] splitStr = strLine.split("\"");
		if (strLine.contains(Constants.TERMINE_AA)) {
		    year = Integer.valueOf(splitStr[1]);
		} else if (strLine.contains(Constants.TERMINE_MIN)) {
		    minutes = Integer.valueOf(splitStr[1]);
		} else if (strLine.contains(Constants.TERMINE_MM)) {
		    month = Integer.valueOf(splitStr[1]);
		} else if (strLine.contains(Constants.TERMINE_GG)) {
		    days = Integer.valueOf(splitStr[1]);
		} else if (strLine.contains(Constants.TERMINE_HH)) {
		    hour = Integer.valueOf(splitStr[1]);
		}
	    }
	    GregorianCalendar cal = new GregorianCalendar(year, month - 1, days, hour, minutes);
	    Utils.setScadenza(cal.getTime());
	} catch (Exception e) {
	    Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
	    Log.e(ctx.getClass().getSimpleName(), e.getMessage(), e);
	}
    }

    /**
     * Restituisce la data dell'ultima giornata giocata
     * 
     * @param giornata
     * @return
     */
    public static Date getLastMatchDate(Context ctx, Integer giornata) {
	String host = getSprefs(ctx).getString(Constants.PREF_HOST, Constants.DEFAULT_HOST);
	if (!host.startsWith(Constants.HTTP)) {
	    host = Constants.HTTP.concat(host);
	}
	Date data = new Date(1900, 1, 1);
	try {
	    URL url = new URL(host + File.separator + Constants.JS_FOLDER + File.separator + Constants.DATA_A_FILE);
	    InputStream inputStream = (InputStream) url.getContent();
	    BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
	    String strLine;
	    String dataStr = "";
	    while ((strLine = in.readLine()) != null) {
		String[] splitStr = strLine.split("\"");
		if (strLine.startsWith(Constants.DATA_GIORNATA + giornata)) {
		    dataStr = splitStr[1];
		    break;
		}
	    }
	    if (!StringUtils.isBlank(dataStr)) {
		data = new SimpleDateFormat("MMMM' 'dd' 'yyyy", Locale.ENGLISH).parse(dataStr);
	    }
	} catch (Exception e) {
	    Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
	    Log.e(ctx.getClass().getSimpleName(), e.getMessage(), e);
	}
	return data;
    }

    /**
     * Ritorna le sharedPreferences
     * 
     * @param ctx
     * @return
     */
    private static SharedPreferences getSprefs(Context ctx) {
	if (sPrefs == null) {
	    sPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
	}
	return sPrefs;
    }

    /**
     * Svuota il db
     */
    public static void clearDb(Context ctx) throws Exception {
	dbAdapter = new DBAdapter(ctx);
	dbAdapter.open();
	dbAdapter.clearDb();
	dbAdapter.close();
    }
}
