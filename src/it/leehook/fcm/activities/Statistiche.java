package it.leehook.fcm.activities;

import it.leehook.fcm.R;
import it.leehook.fcm.utils.Constants;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * @author l.angelini
 * 
 */
@SuppressLint("SetJavaScriptEnabled")
public class Statistiche extends Activity {

	WebView webview;
	private final static String URL_SCHEDINA = "/schedina.php";
	private final static String URL_SE_AVESSI_AVUTO = "/calendarioincrociato.htm";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.statistiche);
		final Statistiche activity = this;
		webview = (WebView) findViewById(R.id.stats);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				activity.setTitle(getString(R.string.loading));
				activity.setProgress(progress * 100);
				if (progress == 100) {
					activity.setTitle(R.string.stats);
				}
			}

		});
		webview.setWebViewClient(new WebViewClient() {
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(activity, "Oh no! " + description,
						Toast.LENGTH_SHORT).show();
			}
		});
		webview.setInitialScale(135);
		webview.getSettings().setSupportZoom(true);
		webview.loadUrl(PreferenceManager.getDefaultSharedPreferences(
				getBaseContext()).getString(Constants.PREF_HOST,
				Constants.DEFAULT_HOST)
				+ URL_SE_AVESSI_AVUTO);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.stats_menu, menu);
		return true;
	}

	/**
	 * Apre il menu
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.schedina:
			webview.clearCache(false);
			webview.getSettings().setBuiltInZoomControls(true);
			webview.loadUrl(PreferenceManager.getDefaultSharedPreferences(
					getBaseContext()).getString(Constants.PREF_HOST,
					Constants.DEFAULT_HOST)
					+ URL_SCHEDINA);
			return true;
		case R.id.seavessi:
			webview.clearCache(false);
			webview.getSettings().setBuiltInZoomControls(false);
			webview.loadUrl(PreferenceManager.getDefaultSharedPreferences(
					getBaseContext()).getString(Constants.PREF_HOST,
					Constants.DEFAULT_HOST)
					+ URL_SE_AVESSI_AVUTO);
			return true;
		default:
			return false;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			Statistiche.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
