/**
 * 
 */
package it.leehook.fcm.activities;

import it.leehook.fcm.R;
import it.leehook.fcm.adapter.DBAdapter;
import it.leehook.fcm.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.DisplayMetrics;

/**
 * @author l.angelini
 * 
 */
public class Preferences extends PreferenceActivity {

    private DBAdapter dbAdapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	addPreferencesFromResource(R.layout.preferences);
	dbAdapter = new DBAdapter(getApplicationContext());
	dbAdapter.open();
	ListPreference squadre = (ListPreference) findPreference(Constants.PREF_TEAM);
	List<String> teams = dbAdapter.getSquadre();
	squadre.setEntries(teams.toArray(new CharSequence[teams.size()]));
	squadre.setEntryValues(teams.toArray(new CharSequence[teams.size()]));
	ListPreference primaGiornata = (ListPreference) findPreference(Constants.PREF_PRIMA);
	ListPreference ultimaGiornata = (ListPreference) findPreference(Constants.PREF_ULTIMA);
	List<String> giornate = getGiornate();
	primaGiornata.setEntries(giornate.toArray(new CharSequence[giornate.size()]));
	primaGiornata.setEntryValues(giornate.toArray(new CharSequence[giornate.size()]));
	ultimaGiornata.setEntries(giornate.toArray(new CharSequence[giornate.size()]));
	ultimaGiornata.setEntryValues(giornate.toArray(new CharSequence[giornate.size()]));
	ListPreference lingua = (ListPreference) findPreference(Constants.PREF_LINGUA);
	CharSequence[] keys = new CharSequence[2];
	keys[0] = getString(R.string.italian);
	keys[1] = getString(R.string.english);
	CharSequence[] vals = new CharSequence[2];
	vals[0] = "it";
	vals[1] = "en";
	lingua.setEntries(keys);
	lingua.setEntryValues(vals);
	lingua.setDefaultValue(vals[0]);
	lingua.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

	    public boolean onPreferenceChange(Preference preference, Object newValue) {
		Resources res = getApplicationContext().getResources();
		DisplayMetrics dm = res.getDisplayMetrics();
		android.content.res.Configuration conf = res.getConfiguration();
		conf.locale = new Locale((String) newValue);
		res.updateConfiguration(conf, dm);
		return true;
	    }
	});
	final CheckBoxPreference clear = (CheckBoxPreference) findPreference(Constants.PREF_CLEAR_DB);
	clear.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

	    @Override
	    public boolean onPreferenceChange(Preference preference, Object newValue) {
		Boolean val = (Boolean) newValue;
		if (val) {
		    AlertDialog.Builder builder = new AlertDialog.Builder(preference.getContext());
		    builder.setMessage(getString(R.string.clearConfirm));
		    builder.setCancelable(false);
		    builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
			    clear.setChecked(true);
			    dialog.cancel();
			}
		    }).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
			    clear.setChecked(false);
			    dialog.cancel();
			}
		    });
		    AlertDialog alert = builder.create();
		    alert.show();
		}
		return true;
	    }
	});
    }

    /**
     * Valorizza le giornate
     * 
     * @return
     */
    private List<String> getGiornate() {
	List<String> list = new ArrayList<String>();
	for (int i = 1; i < 39; i++) {
	    list.add("" + i);
	}
	return list;
    }

    @Override
    protected void onDestroy() {
	if (dbAdapter != null) {
	    dbAdapter.close();
	}
	super.onDestroy();
    }
}
