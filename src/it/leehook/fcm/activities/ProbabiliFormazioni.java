package it.leehook.fcm.activities;

import it.leehook.fcm.R;
import it.leehook.fcm.utils.Constants;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
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
public class ProbabiliFormazioni extends Activity {

	WebView webview;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.probabili);
		final ProbabiliFormazioni activity = this;
		webview = (WebView) findViewById(R.id.prob);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				activity.setTitle(getString(R.string.loading));
				activity.setProgress(progress * 100);
				if (progress == 100) {
					activity.setTitle(R.string.probabili);
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
		webview.setInitialScale(60);
		webview.getSettings().setSupportZoom(true);
		webview.getSettings().setBuiltInZoomControls(true);
		webview.getSettings().setUserAgentString("Desktop");
		
		SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		String url = sPrefs.getString(Constants.PREF_MEDIASET, Constants.DEFAULT_MEDIASET);
		webview.loadUrl(url);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.prob_menu, menu);
		return true;
	}

	/**
	 * Apre il menu
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());		
		switch (item.getItemId()) {
		case R.id.gazzetta:
			webview.loadUrl(sPrefs.getString(Constants.PREF_GAZZETTA, Constants.DEFAULT_GAZZETTA));
			return true;
		case R.id.fantagazzetta:
			webview.loadUrl(sPrefs.getString(Constants.PREF_FANTAGAZZETTA, Constants.DEFAULT_FANTAGAZZETTA));
			return true;
		case R.id.mediaset:
			webview.loadUrl(sPrefs.getString(Constants.PREF_MEDIASET, Constants.DEFAULT_MEDIASET));
			return true;
		default:
			return false;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			ProbabiliFormazioni.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
