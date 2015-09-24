/**
 * 
 */
package it.leehook.fcm.activities;

import it.leehook.fcm.R;
import it.leehook.fcm.utils.Constants;
import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * @author l.angelini
 * 
 */
public class Guestbook extends Activity {

    WebView webview;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_PROGRESS);
	setContentView(R.layout.guestbook);
	final Guestbook activity = this;
	webview = (WebView) findViewById(R.id.guest);
	webview.getSettings().setJavaScriptEnabled(true);
	webview.setWebChromeClient(new WebChromeClient() {
	    public void onProgressChanged(WebView view, int progress) {
		activity.setTitle(getString(R.string.loading));
		activity.setProgress(progress * 100);
		if (progress == 100) {
		    activity.setTitle(R.string.guestbook);
		}
	    }

	});
	webview.setWebViewClient(new WebViewClient() {
	    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
	    }

	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
		view.loadUrl(url);
		return true;
	    }
	});
	webview.setInitialScale(77);
	webview.getSettings().setSupportZoom(true);
	webview.getSettings().setBuiltInZoomControls(true);
	webview.getSettings().setAppCacheEnabled(true);
	webview.getSettings().setAppCacheMaxSize(1024);
	webview.loadUrl(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(Constants.PREF_HOST,
		Constants.DEFAULT_HOST)
		+ "/guest/index.php");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
	    Guestbook.this.finish();
	    return true;
	}
	return super.onKeyDown(keyCode, event);
    }
}
