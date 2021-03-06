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
public class VotiUfficiosi extends Activity {

    WebView webview;
    final VotiUfficiosi activity = this;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	StrictMode.setThreadPolicy(policy);
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_PROGRESS);
	setContentView(R.layout.voti_ufficiosi);
	webview = (WebView) findViewById(R.id.voti);
	webview.getSettings().setUserAgentString("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0");
	webview.getSettings().setJavaScriptEnabled(true);
	webview.setWebChromeClient(new WebChromeClient() {
	    public void onProgressChanged(WebView view, int progress) {
		activity.setTitle(getString(R.string.loading));
		activity.setProgress(progress * 100);
		if (progress == 100) {
		    activity.setTitle(R.string.voti);
		}
	    }

	});
	webview.setWebViewClient(new WebViewClient() {
	    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
	    }

	});
	webview.getSettings().setSupportZoom(true);
	webview.getSettings().setBuiltInZoomControls(true);
	SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
	String url = sPrefs.getString(Constants.PREF_VOTI_UFF, Constants.DEFAULT_VOTI);
	webview.loadUrl(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.layout.voti_menu, menu);
	return true;
    }

    /**
     * Apre il menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
	switch (item.getItemId()) {
	case R.id.voti_ufficiosi:
	    webview.loadUrl(sPrefs.getString(Constants.PREF_VOTI_UFF, Constants.DEFAULT_VOTI));
	    webview.setWebChromeClient(new WebChromeClient() {
		public void onProgressChanged(WebView view, int progress) {
		    activity.setTitle(getString(R.string.loading));
		    activity.setProgress(progress * 100);
		    if (progress == 100) {
			activity.setTitle(R.string.voti);
		    }
		}

	    });
	    return true;
	case R.id.assist:
	    webview.loadUrl(sPrefs.getString(Constants.PREF_ASSIST, Constants.DEFAULT_ASSIST));
	    webview.setWebChromeClient(new WebChromeClient() {
		public void onProgressChanged(WebView view, int progress) {
		    activity.setTitle(getString(R.string.loading));
		    activity.setProgress(progress * 100);
		    if (progress == 100) {
			activity.setTitle(R.string.assist);
		    }
		}

	    });
	    return true;
	case R.id.voti_live:
	    webview.loadUrl(sPrefs.getString(Constants.PREF_LIVE, Constants.DEFAULT_LIVE));
	    webview.setWebChromeClient(new WebChromeClient() {
		public void onProgressChanged(WebView view, int progress) {
		    activity.setTitle(getString(R.string.loading));
		    activity.setProgress(progress * 100);
		    if (progress == 100) {
			activity.setTitle(R.string.live);
		    }
		}

	    });
	    return true;
	default:
	    return false;
	}
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    VotiUfficiosi.this.finish();
	    return true;
	}
	return super.onKeyDown(keyCode, event);
    }
}
