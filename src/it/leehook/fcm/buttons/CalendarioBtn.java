/**
 * 
 */
package it.leehook.fcm.buttons;

import it.leehook.fcm.utils.Utils;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageButton;

/**
 * @author l.angelini
 * 
 */
public class CalendarioBtn extends ImageButton {

    public CalendarioBtn(Context context) {
	super(context);
	setButtonSrc(context);
    }

    public CalendarioBtn(Context context, AttributeSet attrs) {
	super(context, attrs);
	setButtonSrc(context);
    }

    private void setButtonSrc(Context context) {
	Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
	int displayWidth = display.getWidth();
	int displayHeight = display.getHeight();
	try {
	    InputStream inputStream = this.getResources().getAssets().open("img/buttons/calendario_" + Utils.getLocale(context) + ".png");
	    setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeStream(inputStream), displayWidth / 2, displayHeight / 3, true));
	} catch (IOException e) {
	    Log.e("CalendarioBtn.setImage", e.getMessage(), e);
	}
    }
}
