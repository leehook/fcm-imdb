package it.leehook.fcm.activities;

import it.leehook.fcm.R;
import it.leehook.fcm.adapter.ClassificaAdapter;
import it.leehook.fcm.adapter.DBAdapter;
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
 * 
 * Attivita' relativa alla classifica
 * 
 * @author l.angelini
 * 
 */
public class Classifica extends ListActivity {

    private ClassificaAdapter adap;
    private DBAdapter dbAdapter;
    private static List<Squadra> squadre;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.classifica);
	dbAdapter = new DBAdapter(getApplicationContext());
	dbAdapter.open();
	squadre = getSquadre();
	adap = new ClassificaAdapter(this, squadre);
	setListAdapter(adap);
	if (squadre.isEmpty()) {
	    Toast.makeText(Classifica.this, "Aggiorna i dati CAZZO!", Toast.LENGTH_LONG).show();
	    Classifica.this.finish();
	}
    }

    @Override
    public void onDestroy() {
	super.onDestroy();
	if (dbAdapter != null) {
	    dbAdapter.close();
	}
    }

    /**
     * Restituisce la lista delle partita giocate nella giornata X
     * 
     * @return
     */
    private List<Squadra> getSquadre() {
	List<Squadra> list = new ArrayList<Squadra>();
	Cursor curs = dbAdapter.getData(Constants.TABLE_SQ, null, Constants.PUNTI + " DESC, " + Constants.TOT_PUNTI + " DESC");
	startManagingCursor(curs);
	Squadra squadra = null;
	while (curs.moveToNext()) {
	    squadra = new Squadra();
	    squadra.setPunti(curs.getString(5));
	    squadra.setNomeAllenatore(curs.getString(2));
	    squadra.setNomeSquadra(curs.getString(1));
	    squadra.setTotPuntiFatti(curs.getString(7));
	    squadra.setPartiteVinte(curs.getString(8));
	    squadra.setPartitePareggiate(curs.getString(9));
	    squadra.setPartitePerse(curs.getString(10));
	    squadra.setGolFatti(curs.getString(11));
	    squadra.setGolSubiti(curs.getString(12));
	    InputStream is = null;
	    try {
		is = new FileInputStream(Constants.IMG_PATH + Constants.LOGHI_FOLDER + File.separator
			+ squadra.getNomeSquadra().toLowerCase().replaceAll("\\s+", "") + ".gif");
		squadra.setLogoSquadra(Bitmap.createScaledBitmap(BitmapFactory.decodeStream(is), 35, 35, true));
	    } catch (IOException e) {
		try {
		    is = getAssets().open(
			    Constants.IMG_FOLDER + File.separator + Constants.DEFAULTS_FOLDER + File.separator + Constants.NO_LOGO);
		    squadra.setLogoSquadra(Bitmap.createScaledBitmap(BitmapFactory.decodeStream(is), 40, 40, true));
		} catch (IOException e1) {
		    Log.e("LogoSquadra", "Can't find logo_photo: " + e.getMessage());
		}
	    }
	    list.add(squadra);
	}
	curs.close();
	return list;
    }
}
