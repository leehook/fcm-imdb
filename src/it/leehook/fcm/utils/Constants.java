package it.leehook.fcm.utils;

import android.os.Environment;

/**
 * @author l.angelini
 * 
 */
public class Constants {

	/*
	 * FCMActivity
	 */
	public static final String LEGA = "Lega";
	public static final String EXTRA = "Utils";

	/*
	 * DB Utils
	 */
	public static final String TABLE_PREFS = "prefs";
	public static final String TABLE_PLAYER = "player";
	public static final String TABLE_TEAM = "serieA";
	public static final String TABLE_MATCH = "giornata";
	public static final String TABLE_SQ = "squadra";
	public static final String TABLE_TAB = "tabellino";
	public static final String CODICE = "codice";
	public static final String NOME = "nome";
	public static final String NUMERO = "numero";
	public static final String GIOCATA = "giocata";
	public static final String SQ_CASA = "squadra_casa";
	public static final String GOL_CASA = "gol_casa";
	public static final String PNT_CASA = "punti_casa";
	public static final String SQ_TRASF = "squadra_trasferta";
	public static final String MOD_CASA = "mod_casa";
	public static final String MOD_TRASF = "mod_trasf";
	public static final String GOL_TRASF = "gol_trasferta";
	public static final String PNT_TRASF = "punti_trasferta";
	public static final String ID_SQUADRA = "id_squadra";
	public static final String ID_SQUADRA_CASA = "id_squadra_casa";
	public static final String ID_SQUADRA_TRASF = "id_squadra_trasferta";
	public static final String PRESIDENTE = "presidente";
	public static final String MAIL = "mail";
	public static final String CREDITI = "crediti";
	public static final String PUNTI = "punti";
	public static final String TOT_PUNTI = "tot_punti";
	public static final String RUOLO = "ruolo";
	public static final String PREZZO = "prezzo";
	public static final String SQUADRA_DI_A = "squadra_di_a";
	public static final String NUMERO_GIORNATA = "num_giornata";
	public static final String MODIFICATORE = "modificatore";
	public static final String VOTO = "voto";
	public static final String VALORE = "valore";
	public static final String TITOLARE = "titolare";
	public static final String VINTE = "vinte";
	public static final String PAREGGIATE = "pareggiate";
	public static final String PERSE = "perse";
	public static final String GOL_FATTI = "gol_fatti";
	public static final String GOL_SUBITI = "gol_subiti";
	public static final String PROSSIMA_GIORNATA = "prossima_giornata";
	public static final String FIRST_RUN = "first_run";
	public static final String ID_CAMPIONATO = "id_lega";
	public static final String HOST = "host";
	public static final String DEFAULT_HOST = "http://imalatidelbari.netsons.org";
	public static final String DEFAULT_GAZZETTA = "http://www.gazzetta.it/Calcio/prob_form/";
	public static final String DEFAULT_FANTAGAZZETTA = "http://m.fantagazzetta.com/probabili-formazioni.vbhtml";
	public static final String DEFAULT_MEDIASET = "http://www.sportmediaset.mediaset.it/calcio/calcio/2014/articoli/1045240/le-probabili-formazioni-di-serie-a.shtml";

	/*
	 * Preferences
	 */
	public static final String PREF_HOST = "hostEdit";
	public static final String PREF_LINGUA = "languagePref";
	public static final String PREF_PRIMA = "firstMatchPref";
	public static final String PREF_ULTIMA = "lastMatchPref";
	public static final String PREF_INVIO = "invioEdit";
	public static final String PREF_TEAM = "teamPref";
	public static final String PREF_NOTIFICHE = "notifiche";
	public static final String PREF_PROBABILI = "probCheck";
	public static final String PREF_COPPA = "cupCheck";
	public static final String PREF_GUEST = "guestCheck";
	public static final String PREF_NUM_SQ = "numSqPref";
	public static final String PREF_VOTI = "votiCheck";
	public static final String IMG_PATH = Environment
			.getExternalStorageDirectory().getPath() + "/IMDB/img/";
	public static final String DEFAULTS_FOLDER = "defaults";
	public static final String NO_LOGO = "nologo.png";
	public static final String NO_CAPO = "nocapo.png";
	public static final String PREF_ROSE = "updRose";
	public static final String PREF_SERIEA = "updSerieA";
	public static final String PREF_TABELLINI = "updTabellini";
	public static final String PREF_CLEAR_DB = "clearDb";
	public static final String PREF_GAZZETTA = "gazzettaEdit";
	public static final String PREF_FANTAGAZZETTA = "fantagazzettaEdit";
	public static final String PREF_MEDIASET = "mediasetEdit";

	/*
	 * JS Utils
	 */
	public static final String HTTP = "http://";
	public static final String JS_FOLDER = "js";
	public static final String IMG_FOLDER = "img";
	public static final String LOGHI_FOLDER = "loghi";
	public static final String ALL_FOLDER = "allenatori";
	public static final String SQ_PREFIX = "xa";
	public static final String PL_PREFIX = "xg";
	public static final String NON_GIOCATA = "NG";
	public static final String TERMINE_GG = "TermineInviog";
	public static final String TERMINE_MM = "TermineInviom";
	public static final String TERMINE_AA = "TermineInvioa";
	public static final String TERMINE_MIN = "TermineInviomm";
	public static final String TERMINE_HH = "TermineInviohh";
	public static final String DATA_GIORNATA = "dataGiornata[";

	public static final String SERIE_A_FILE = "fcmSerieADati.js";
	public static final String CALENDARIO_FILE = "fcmCalendarioDati.js";
	public static final String CLASSIFICA_FILE = "fcmClassificaDati.js";
	public static final String SQUADRE_FILE = "fcmFantasquadreDati.js";
	public static final String RISULTATI_FILE = "fcmRisultatiDati";
	public static final String FORMAZIONI_FILE = "fcmFormazioniDati";
	public static final String COMPETIZIONI_FILE = "fcmCompetizioniDati.js";
	public static final String VARIABILI_FILE = "fcmVariabili.js";
	public static final String DATA_A_FILE = "DataA.js";

	/*
	 * Coppa
	 */
	public static final String CUP_QUARTI_A = "Quarti Andata";
	public static final String CUP_QUARTI_R = "Quarti Ritorno";
	public static final String CUP_SEMI_A = "Semifinali Andata";
	public static final String CUP_SEMI_R = "Semifinali Ritorno";
	public static final String CUP_FINALE = "Finale";

}
