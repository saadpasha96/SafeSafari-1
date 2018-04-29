package pashainc.google.com.safesafari;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by black on 3/29/2018.
 */

public class SmsSend extends BroadcastReceiver {

	String userName;
	Context context;
	//SharedprefWrite spw = new SharedprefWrite(context);

	@Override
	public void onReceive(Context context, Intent intent) {

		SharedPreferences spfread = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
		userName = spfread.getString("UserName", null);
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage("+923336118070", "+923336118070",userName, null, null);
		Toast.makeText(context, "Sms Sent", Toast.LENGTH_SHORT).show();

	}

	public void SharedPrefRead(){

	}

}
