package pashainc.google.com.safesafari;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by black on 3/29/2018.
 */

public class SmsSend extends BroadcastReceiver {

	DatabaseReference mDatabase;
	private FirebaseAuth mAuth;

	@Override
	public void onReceive(Context context, Intent intent) {

		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage("+923022352114", "+923336118070", "Hii", null, null);
		Toast.makeText(context, "Sms Sent", Toast.LENGTH_SHORT).show();

	}
}
