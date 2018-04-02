package pashainc.google.com.safesafari;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

/**
 * Created by black on 3/28/2018.
 */

public class AlertReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {


		MediaP m = new MediaP(context);

		if (intent.hasExtra("Stop")) {

			if (m.mp != null && m.mp.isPlaying()) {
				m.mp.pause();
				Toast.makeText(context, "CALLING", Toast.LENGTH_SHORT).show();
				Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
				context.sendBroadcast(it);
			} else {
				Toast.makeText(context, "Not playing", Toast.LENGTH_SHORT).show();
			}
		}
		if (intent.hasExtra("Send")){
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage("+923336118070", "+923336118070", "Hii", null, null);
			Toast.makeText(context, "Sms Sent", Toast.LENGTH_SHORT).show();
		}
		else {
			Toast.makeText(context, "NIL", Toast.LENGTH_SHORT).show();
		}
	}
}