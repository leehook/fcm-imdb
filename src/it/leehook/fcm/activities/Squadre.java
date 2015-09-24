package it.leehook.fcm.activities;

import it.leehook.fcm.R;
import it.leehook.fcm.adapter.DBAdapter;
import it.leehook.fcm.adapter.SquadreAdapter;
import it.leehook.fcm.bean.Giocatore;
import it.leehook.fcm.bean.Squadra;
import it.leehook.fcm.utils.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * @author l.angelini
 */
public class Squadre extends ListActivity {

    private SquadreAdapter adap;
    private DBAdapter dbAdapter;
    private static List<Squadra> squadre;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.squadre);
	dbAdapter = new DBAdapter(getApplicationContext());
	dbAdapter.open();
	squadre = getSquadre();
	adap = new SquadreAdapter(this, squadre);
	setListAdapter(adap);
	if (squadre.isEmpty()) {
	    Toast.makeText(Squadre.this, "Aggiorna i dati CAZZO!", Toast.LENGTH_LONG).show();
	    Squadre.this.finish();
	}
    }

    @Override
    protected void onDestroy() {
	super.onDestroy();
	if (dbAdapter != null) {
	    dbAdapter.close();
	}
    }

    /**
     * Recupera dal server le informazioni delle squadre
     * 
     * @return
     */
    private List<Squadra> getSquadre() {
	List<Squadra> list = new ArrayList<Squadra>();
	Cursor curs = dbAdapter.getData(Constants.TABLE_SQ, null, Constants.NOME);
	startManagingCursor(curs);
	Squadra squadra = null;
	while (curs.moveToNext()) {
	    squadra = new Squadra();
	    squadra.setNomeSquadra(curs.getString(1));
	    squadra.setNomeAllenatore(curs.getString(2));
	    squadra.setCreditiResidui(curs.getString(4));
	    squadra.setMailAllenatore(curs.getString(3));
	    squadra.setIdSquadra(curs.getInt(0));
	    InputStream is = null;
	    try {
		is = new FileInputStream(Constants.IMG_PATH + Constants.ALL_FOLDER + File.separator
			+ squadra.getNomeSquadra().toLowerCase().replaceAll("\\s+", "") + ".png");
		squadra.setFotoAllenatore(Bitmap.createScaledBitmap(BitmapFactory.decodeStream(is), 40, 40, true));
	    } catch (Exception e) {
		try {
		    is = getAssets().open(
			    Constants.IMG_FOLDER + File.separator + Constants.DEFAULTS_FOLDER + File.separator + Constants.NO_CAPO);
		    squadra.setFotoAllenatore(Bitmap.createScaledBitmap(BitmapFactory.decodeStream(is), 40, 40, true));
		} catch (IOException e1) {
		    Log.e("FotoAllenatore", "Can't find all_photo: " + e.getMessage());
		}
	    }
	    try {
		is = new FileInputStream(Constants.IMG_PATH + Constants.LOGHI_FOLDER + File.separator
			+ squadra.getNomeSquadra().toLowerCase().replaceAll("\\s+", "") + ".gif");
		squadra.setLogoSquadra(Bitmap.createScaledBitmap(BitmapFactory.decodeStream(is), 40, 40, true));
	    } catch (Exception e) {
		try {
		    is = getAssets().open(
			    Constants.IMG_FOLDER + File.separator + Constants.DEFAULTS_FOLDER + File.separator + Constants.NO_LOGO);
		    squadra.setLogoSquadra(Bitmap.createScaledBitmap(BitmapFactory.decodeStream(is), 40, 40, true));
		} catch (IOException e1) {
		    Log.e("LogoSquadra", "Can't find logo_photo: " + e.getMessage());
		}
	    }
	    squadra.setRosa(getRosa(squadra.getIdSquadra()));
	    list.add(squadra);
	}
	curs.close();
	return list;
    }

    /**
     * Recupera i dati relativi alla rosa della squadra interessata
     * 
     * @param squadra
     * @return
     */
    private List<Giocatore> getRosa(Integer idSquadra) {
	List<Giocatore> rosa = new ArrayList<Giocatore>();
	Cursor curs = dbAdapter.getData(Constants.TABLE_PLAYER, Constants.ID_SQUADRA + "=" + idSquadra, Constants.RUOLO);
	startManagingCursor(curs);
	while (curs.moveToNext()) {
	    Giocatore giocatore = new Giocatore();
	    giocatore.setRuolo(Integer.valueOf(curs.getString(2)));
	    giocatore.setCognome(curs.getString(1));
	    giocatore.setSquadraAppartenenza(dbAdapter.getNomeByCode(Constants.TABLE_TEAM, curs.getString(5)));
	    giocatore.setPrezzo(curs.getString(3));
	    giocatore.setFantaSquadra(String.valueOf(idSquadra));
	    rosa.add(giocatore);
	}
	curs.close();
	return rosa;
    }
}
