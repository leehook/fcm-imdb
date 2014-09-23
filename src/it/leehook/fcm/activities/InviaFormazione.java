package it.leehook.fcm.activities;

import it.leehook.fcm.R;
import it.leehook.fcm.adapter.DBAdapter;
import it.leehook.fcm.utils.Constants;

import java.io.File;

import org.apache.commons.lang.StringUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * 
 * Attivita relativa all'invio della formazione
 * 
 * @author l.angelini
 * 
 */
@SuppressLint("SetJavaScriptEnabled")
public class InviaFormazione extends Activity {

	WebView webview;
	DBAdapter adapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		super.onCreate(savedInstanceState);
		adapter = new DBAdapter(this);
		adapter.open();
		requestWindowFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.inviaform);
		final Activity activity = this;
		webview = (WebView) findViewById(R.id.invioForm);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				activity.setTitle(getString(R.string.loading));
				activity.setProgress(progress * 100);
				if (progress == 100) {
					activity.setTitle(R.string.inviaform);
				}
			}

			@Override
			public boolean onCreateWindow(WebView view, boolean dialog,
					boolean userGesture, Message resultMsg) {
				WebView childView = new WebView(InviaFormazione.this);
				WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
				childView.getSettings()
						.setJavaScriptCanOpenWindowsAutomatically(true);
				childView.getSettings().setJavaScriptEnabled(true);
				childView.setWebChromeClient(this);
				transport.setWebView(childView);
				resultMsg.sendToTarget();
				return true;
			}

			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				new AlertDialog.Builder(view.getContext())
						.setMessage(message)
						.setCancelable(true)
						.setPositiveButton(getString(android.R.string.ok),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								}).show();
				result.confirm();
				return true;

			}
		});
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(activity, "Oh no! " + description,
						Toast.LENGTH_SHORT).show();
			}
		});
		webview.setInitialScale(75);
		webview.getSettings().setSupportZoom(true);
		webview.getSettings().setBuiltInZoomControls(true);
		webview.getSettings().setSupportMultipleWindows(true);
		webview.getSettings().setAppCacheEnabled(true);
		SharedPreferences sPrefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		String url = sPrefs.getString(Constants.PREF_HOST,
				Constants.DEFAULT_HOST)
				+ File.separator
				+ sPrefs.getString(Constants.PREF_INVIO, "");
		String nomeSquadra = sPrefs.getString(Constants.PREF_TEAM, "");
		if (!StringUtils.isBlank(nomeSquadra)) {
			url += "?Fsq=" + adapter.getIdSquadraByName(nomeSquadra);
		}
		adapter.close();
		webview.loadUrl(url);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.browser, menu);
		MenuItem item = menu.findItem(R.id.back);
		item.setEnabled(webview.canGoBack());
		MenuItem item2 = menu.findItem(R.id.forward);
		item2.setEnabled(webview.canGoForward());
		return true;
	}

	/**
	 * Apre il menu
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.back:
			if (webview.canGoBack()) {
				webview.goBack();
			}
			return true;
		case R.id.refresh:
			webview.clearCache(false);
			webview.reload();
			return true;
		case R.id.forward:
			if (webview.canGoForward()) {
				webview.goForward();
			}
			return true;
		default:
			return false;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			InviaFormazione.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
