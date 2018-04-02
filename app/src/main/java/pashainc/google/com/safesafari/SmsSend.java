package pashainc.google.com.safesafari;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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

	DatabaseReference mDatabase;
	private FirebaseAuth mAuth;
	String userName;
	@Override
	public void onReceive(Context context, Intent intent) {

//		mAuth = FirebaseAuth.getInstance();
//		mDatabase = FirebaseDatabase.getInstance().getReference();
//		String user = mAuth.getCurrentUser().getUid();
//
//		final DatabaseReference query = mDatabase.child(user);

		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage("+923336118070", "+923336118070", "User in Danger", null, null);
		Toast.makeText(context, "Sms Sent", Toast.LENGTH_SHORT).show();

//		query.addValueEventListener(new ValueEventListener() {
//			@Override
//			public void onDataChange(DataSnapshot dataSnapshot) {
//				userName = dataSnapshot.getValue().toString();
//			}
//
//			@Override
//			public void onCancelled(DatabaseError databaseError) {
//
//			}
//		});
	}
}
