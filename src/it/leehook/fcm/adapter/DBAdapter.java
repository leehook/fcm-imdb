package it.leehook.fcm.adapter;

import it.leehook.fcm.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 
 * Adapter di interazione con il db
 * 
 * @author l.angelini
 * 
 */
public class DBAdapter {

	private static final String TAG = "DBAdapter";
	private static final String DATABASE_NAME = "imdb";
	private static final int DATABASE_VERSION = 2;

	private static final String CREATE_PLAYER = "create table if not exists player (codice text primary key, nome text not null, ruolo text, prezzo integer, id_squadra integer, squadra_di_a text);";
	private static final String DROP_PLAYER = "delete from player";

	private static final String CREATE_SERIEA = "create table if not exists serieA (codice text primary key, "
			+ "nome text not null);";
	private static final String DROP_SERIEA = "delete from serieA";

	private static final String CREATE_MATCH = "create table if not exists giornata (numero text not null, squadra_casa text, gol_casa text, punti_casa text, squadra_trasferta text, gol_trasferta text, punti_trasferta text, giocata integer not null, id_squadra_casa integer, id_squadra_trasferta integer, mod_casa text, mod_trasf text, id_lega integer);";
	private static final String DROP_MATCH = "delete from giornata";

	private static final String CREATE_TEAM = "create table if not exists squadra (id_squadra integer primary key, nome text, presidente text, mail text, crediti integer, punti integer, id_lega integer, tot_punti text, vinte integer, pareggiate integer, perse integer, gol_fatti integer, gol_subiti integer);";
	private static final String DROP_TEAM = "delete from squadra";

	private static final String CREATE_TAB = "create table if not exists tabellino (num_giornata integer not null, id_squadra integer not null, codice text, voto text, titolare integer, id_lega integer)";
	private static final String DROP_TAB = "delete from tabellino";

	private static final String CREATE_PREFS = "create table if not exists prefs (nome text, valore text)";
	private static final String DROP_PREFS = "delete from prefs";

	private final Context context;

	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public DBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_PREFS);
			db.execSQL(CREATE_SERIEA);
			db.execSQL(CREATE_PLAYER);
			db.execSQL(CREATE_MATCH);
			db.execSQL(CREATE_TAB);
			db.execSQL(CREATE_TEAM);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			onCreate(db);
		}
	}

	/**
	 * Apre il DB
	 * 
	 * @return
	 * @throws SQLException
	 */
	public DBAdapter open() throws SQLException {
		if (db == null) {
			db = DBHelper.getWritableDatabase();
		}
		return this;
	}

	/**
	 * Chiude il DB
	 */
	public void close() {
		DBHelper.close();
	}

	/**
	 * Cancella le righe dalla tabella specificata in base alla query fornita in
	 * input
	 * 
	 * @param tableName
	 * @param whereClause
	 * @return
	 */
	public boolean deleteData(String tableName, String whereClause) {
		return db.delete(tableName, whereClause, null) > 0;
	}

	/**
	 * Inserisce una riga nella tabella desiderata
	 * 
	 * @param tableName
	 * @param values
	 */
	public void insertData(String tableName, Map<String, String> values) {
		ContentValues initialValues = new ContentValues();
		for (String key : values.keySet()) {
			initialValues.put(key, values.get(key));
		}
		try {
			db.insertOrThrow(tableName, null, initialValues);
		} catch (SQLException e) {
			Log.e(this.getClass().getCanonicalName(), e.getMessage(), e);
		}
	}

	/**
	 * Esegue una query sulla tabella specificata in base ai criteri della
	 * whereClause
	 * 
	 * @param tableName
	 * @param whereClause
	 * @return
	 */
	public Cursor getData(String tableName, String whereClause, String order) {
		return db.query(tableName, null, whereClause, null, null, null, order);
	}

	/**
	 * Recupera il nome del giocatore/squadra in base al codice
	 * 
	 * @param codice
	 * @return
	 */
	public String getNomeByCode(String tableName, String codice) {
		String nome = "";
		Cursor curs = db.query(tableName, new String[] { Constants.NOME },
				Constants.CODICE + "='" + codice + "'", null, null, null, null);
		while (curs.moveToNext()) {
			nome = curs.getString(0);
		}
		curs.close();
		return nome;
	}

	/**
	 * Recupera il ruolo del giocatore
	 * 
	 * @param codice
	 * @return
	 */
	public Integer getRoleByCode(String codice) {
		Integer ruolo = 0;
		Cursor curs = db.query(Constants.TABLE_PLAYER,
				new String[] { Constants.RUOLO }, Constants.CODICE + "='"
						+ codice + "'", null, null, null, null);
		while (curs.moveToNext()) {
			ruolo = curs.getInt(0);
		}
		curs.close();
		return ruolo;
	}

	/**
	 * Verifica se una giornata e' stata giocata o meno
	 * 
	 * @param giornata
	 * @param sqCasa
	 * @param sqTr
	 * @param codiceCompetizione
	 * @return 0 = giornata presente su db ma NG <br>
	 *         1 = giornata presente e valorizzata su db <br>
	 *         2= giornata non presente su db
	 */
	public Integer isMatchPlayed(String giornata, String sqCasa, String sqTr,
			String codiceCompetizione) {
		Cursor curs = db.query(Constants.TABLE_MATCH,
				new String[] { Constants.GIOCATA }, Constants.NUMERO + "='"
						+ giornata + "' AND " + Constants.SQ_CASA + "='"
						+ sqCasa + "' AND " + Constants.SQ_TRASF + "='" + sqTr
						+ "' AND " + Constants.ID_CAMPIONATO + "='"
						+ codiceCompetizione + "'", null, null, null, null);
		while (curs.moveToNext()) {
			if (curs.getString(0).equals(0) || curs.getInt(0) == 0) {
				curs.close();
				return 0;
			} else {
				curs.close();
				return 1;
			}
		}
		curs.close();
		return 2;
	}

	/**
	 * Aggiorna i dati di una tabella
	 * 
	 * @param tableName
	 * @param newColumnValues
	 * @param whereClause
	 * @return
	 */
	public boolean updateData(String tableName,
			Map<String, String> newColumnValues, String whereClause) {
		ContentValues args = new ContentValues();
		for (String key : newColumnValues.keySet()) {
			args.put(key, newColumnValues.get(key));
		}
		return db.update(tableName, args, whereClause, null) > 0;
	}

	/**
	 * Pulisce il Db
	 */
	public void clearDb() {
		db.execSQL(DROP_SERIEA);
		db.execSQL(DROP_PLAYER);
		db.execSQL(DROP_MATCH);
		db.execSQL(DROP_TAB);
		db.execSQL(DROP_TEAM);
		db.execSQL(DROP_PREFS);
	}

	/**
	 * Verifica se le tabelle della serie A sono gia' state popolate o meno
	 */
	public boolean isSerieAFull() {
		Cursor c = getData(Constants.TABLE_PLAYER, null, null);
		if (c.getCount() > 0) {
			c.close();
			return true;
		} else {
			c.close();
			return false;
		}
	}

	/**
	 * Verifica se le tabelle delle squadre sono state gia' popolate o meno
	 */
	public boolean isSquadreFull() {
		Cursor c = getData(Constants.TABLE_SQ, null, null);
		if (c.getCount() > 0) {
			c.close();
			return true;
		} else {
			c.close();
			return false;
		}
	}

	/**
	 * Verifica se i tabellini di una specifica giornata sono stati gia' salvati
	 * o meno
	 * 
	 * @param numGiornata
	 * @return
	 */
	public boolean isTabSaved(Integer numGiornata) {
		Cursor c = getData(Constants.TABLE_TAB, Constants.NUMERO_GIORNATA + "="
				+ numGiornata, null);
		if (c.getCount() > 0) {
			c.close();
			return true;
		} else {
			c.close();
			return false;
		}
	}

	/**
	 * Recupera il numero della prossima giornata
	 * 
	 * @return
	 */
	public Integer getProssimaGiornata() {
		Integer giornata = 1;
		Cursor c = getData(Constants.TABLE_PREFS, Constants.NOME + "='"
				+ Constants.PROSSIMA_GIORNATA + "'", null);
		while (c.moveToNext()) {
			giornata = c.getInt(1);
		}
		c.close();
		return giornata;
	}

	/**
	 * Restituisce il codice della squadra in base al suo nome
	 * 
	 * @param nomeSquadra
	 * @return
	 */
	public Integer getIdSquadraByName(String nomeSquadra) {
		Integer idSq = 1;
		Cursor c = getData(
				Constants.TABLE_SQ,
				Constants.NOME
						+ " LIKE '%"
						+ (nomeSquadra.length() > 4 ? StringUtils
								.capitalize(nomeSquadra) : nomeSquadra
								.toUpperCase()) + "%'", null);
		while (c.moveToNext()) {
			idSq = c.getInt(0);
		}
		c.close();
		return idSq;
	}

	/**
	 * Restituisce il codice del campionato
	 * 
	 * @return
	 */
	public Integer getIdCampionato() {
		Integer idLega = 1;
		Cursor c = db.query(Constants.TABLE_SQ,
				new String[] { Constants.ID_CAMPIONATO }, null, null, null,
				null, null);
		if (c.moveToNext()) {
			idLega = c.getInt(0);
		}
		c.close();
		return idLega;
	}

	/**
	 * Verifica se ho già inserito 25 giocatori per squadra
	 * 
	 * @param giornata
	 * @param idSquadra
	 * @return
	 */
	public boolean isTeamComplete(Integer giornata, Integer idSquadra) {
		Cursor c = getData(Constants.TABLE_TAB, Constants.NUMERO_GIORNATA + "="
				+ giornata + " AND " + Constants.ID_SQUADRA + "=" + idSquadra,
				null);
		if (c.getCount() == 25) {
			c.close();
			return true;
		} else {
			c.close();
			return false;
		}
	}

	/**
	 * Restituisce la lista delle fantasquadre
	 * 
	 * @return
	 */
	public List<String> getSquadre() {
		List<String> squadre = new ArrayList<String>();
		Cursor c = db.query(Constants.TABLE_SQ,
				new String[] { Constants.NOME }, null, null, null, null,
				Constants.NOME);
		while (c.moveToNext()) {
			squadre.add(c.getString(0));
		}
		c.close();
		return squadre;
	}

	/**
	 * Controlla il risultato dell'ultima giornata della squadra indicata
	 * 
	 * @param giornata
	 * @param nomeSquadra
	 * @return
	 */
	public String getLastResult(Integer giornata, String nomeSquadra) {
		if (!StringUtils.isBlank(nomeSquadra)) {
			Cursor curs = getData(Constants.TABLE_MATCH, Constants.NUMERO + "="
					+ giornata + " AND " + Constants.ID_CAMPIONATO + "="
					+ getIdCampionato() + " AND (" + Constants.SQ_CASA + "='"
					+ nomeSquadra + "' OR " + Constants.SQ_TRASF + "='"
					+ nomeSquadra + "')", null);
			String result = "";
			while (curs.moveToNext()) {
				Integer golCasa = Integer.valueOf(curs.getString(2));
				Integer golTr = Integer.valueOf(curs.getString(5));
				if (StringUtils
						.equalsIgnoreCase(nomeSquadra, curs.getString(1))) {
					if (golCasa > golTr) {
						result = "Complimenti! " + nomeSquadra + " ha vinto "
								+ golCasa + " a " + golTr + " nella "
								+ giornata + "a giornata contro "
								+ curs.getString(4) + "!";
					} else if (golCasa < golTr) {
						result = "Peccato! " + nomeSquadra + " ha perso "
								+ golTr + " a " + golCasa + " nella "
								+ giornata + "a giornata contro "
								+ curs.getString(4);
					} else {
						result = nomeSquadra + " ha pareggiato " + golCasa
								+ " a " + golTr + " nella " + giornata
								+ "a giornata contro " + curs.getString(4);
					}
				} else if (StringUtils.equalsIgnoreCase(nomeSquadra,
						curs.getString(4))) {
					if (golCasa < golTr) {
						result = "Complimenti! " + nomeSquadra + " ha vinto "
								+ golTr + " a " + golCasa + " nella "
								+ giornata + "a giornata contro "
								+ curs.getString(1) + "!";
					} else if (golCasa > golTr) {
						result = "Peccato! " + nomeSquadra + " ha perso "
								+ golCasa + " a " + golTr + " nella "
								+ giornata + "a giornata contro "
								+ curs.getString(1);
					} else {
						result = nomeSquadra + " ha pareggiato " + golCasa
								+ " a " + golTr + " nella " + giornata
								+ "a giornata contro " + curs.getString(1);
					}
				}
			}
			curs.close();
			return result;
		} else {
			return "Ricordati di settare la tua squadre nelle preferenze";
		}
	}
}
