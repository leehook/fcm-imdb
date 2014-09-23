package it.leehook.fcm.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

/**
 * @author l.angelini
 * 
 */
public class Utils {

	private static Integer giornata;

	private static String turnoCoppa;

	private static Map<String, String> mappaSquadre;

	private static Date scadenza;

	/**
	 * @return the giornata
	 */
	public static Integer getGiornata() {
		return giornata != null ? giornata : 3;
	}

	/**
	 * @param giornata
	 *            the giornata to set
	 */
	public static void setGiornata(Integer giornata) {
		Utils.giornata = giornata;
	}

	/**
	 * @return the mappaSquadre
	 */
	public static Map<String, String> getMappaSquadre() {
		if (mappaSquadre != null) {
			return mappaSquadre;
		} else {
			return new HashMap<String, String>();
		}
	}

	/**
	 * @param mappaSquadre
	 *            the mappaSquadre to set
	 */
	public static void setMappaSquadre(Map<String, String> mappaSquadre) {
		Utils.mappaSquadre = mappaSquadre;
	}

	/**
	 * @return the scadenza
	 */
	public static Date getScadenza() {
		return scadenza;
	}

	/**
	 * @return the turnoCoppa
	 */
	public static String getTurnoCoppa() {
		return turnoCoppa;
	}

	/**
	 * @param turnoCoppa
	 *            the turnoCoppa to set
	 */
	public static void setTurnoCoppa(String turnoCoppa) {
		Utils.turnoCoppa = turnoCoppa;
	}

	/**
	 * @param scadenza
	 *            the scadenza to set
	 */
	public static void setScadenza(Date scadenza) {
		Utils.scadenza = scadenza;
	}

	/**
	 * Trasforma il BufferedReader in stringa
	 * 
	 * @param br
	 * @return
	 * @throws IOException
	 */
	public static String getStringFromStringBuffer(BufferedReader br)
			throws IOException {
		String line = null;
		StringBuffer theText = new StringBuffer();
		while ((line = br.readLine()) != null) {
			theText.append(line + "\n");
		}
		return theText.toString();
	}

	/**
	 * Verifica che la connessione sia attiva
	 * 
	 * @param ctx
	 * @return
	 */
	public static boolean checkConn(Context ctx) {
		ConnectivityManager conMgr = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conMgr != null) {
			NetworkInfo i = conMgr.getActiveNetworkInfo();
			if (i != null) {
				if (!i.isConnected() || !i.isAvailable()) {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	/**
	 * Restituisce la lingua scelta dall'utente o in alternativa quella di
	 * sistema
	 * 
	 * @param ctx
	 * @return
	 */
	public static String getLocale(Context ctx) {
		SharedPreferences sPrefs = PreferenceManager
				.getDefaultSharedPreferences(ctx);
		return StringUtils.isBlank(sPrefs.getString(Constants.PREF_LINGUA, "")) ? Locale
				.getDefault().getDisplayName().toLowerCase().substring(0, 2)
				: sPrefs.getString(Constants.PREF_LINGUA, "");
	}
}
