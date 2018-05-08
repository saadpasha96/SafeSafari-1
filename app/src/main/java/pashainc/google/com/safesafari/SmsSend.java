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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by black on 3/29/2018.
 */

public class SmsSend extends BroadcastReceiver {

	String userName;
	String guardPhone;
	String userPhone;
	Context context;
	//SharedprefWrite spw = new SharedprefWrite(context);

	@Override
	public void onReceive(Context context, Intent intent) {

		SharedPreferences spfread = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
		userName = spfread.getString("UserName", null);
		guardPhone = spfread.getString("GuardianPhone", null);
		userPhone = spfread.getString("UserPhone", null);

		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(userPhone, guardPhone ,"ALERT,\nYour family memeber:"+userName+ "travelling from" , null, null);
		Toast.makeText(context, "Sms Sent", Toast.LENGTH_SHORT).show();

	}

	public void send(Context context){
		SharedPreferences spfread = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
		userName = spfread.getString("UserName", null);
		guardPhone = spfread.getString("GuardianPhone", null);
		userPhone = spfread.getString("UserPhone", null);

		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(userPhone, guardPhone ,"ALERT" , null, null);
		Toast.makeText(context, "Sms Sent", Toast.LENGTH_SHORT).show();

	}


}
