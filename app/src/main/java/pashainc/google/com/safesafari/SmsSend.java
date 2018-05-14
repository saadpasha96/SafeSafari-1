package pashainc.google.com.safesafari;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.util.Log;
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

	String startLocCoords;
	String destLocCoords;
	String currLocCoords;

	String startLocAddress;
	String destLocAddress;
	String currLocAddress;

	Context context;
	//SharedprefWrite spw = new SharedprefWrite(context);

	@Override
	public void onReceive(Context context, Intent intent) {

		SharedPreferences spfread = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
		userName = spfread.getString("UserName", null);
		guardPhone = spfread.getString("GuardianPhone", null);
		userPhone = spfread.getString("UserPhone", null);

		SharedPreferences spfridedata = context.getSharedPreferences("LocationData", Context.MODE_PRIVATE);
		currLocCoords = spfridedata.getString("LastLoc latlng", null);
		currLocAddress = spfridedata.getString("LastLoc Address", null);
		String alertmsg = "ALert! I am in danger. My current coordinates are: \n"+currLocCoords;
		String alertmsg1 = "and my address is: \n"+currLocAddress;

		Log.e("Alert MSG", alertmsg +"\n"+ alertmsg1);

		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(userPhone, guardPhone ,alertmsg, null, null);
		smsManager.sendTextMessage(userPhone, guardPhone ,alertmsg1, null, null);
		Toast.makeText(context, "Sms Sent", Toast.LENGTH_SHORT).show();

	}

	public void send(Context context){
		SharedPreferences spfread = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
		userName = spfread.getString("UserName", null);
		guardPhone = spfread.getString("GuardianPhone", null);
		userPhone = spfread.getString("UserPhone", null);

		SharedPreferences spfridedata = context.getSharedPreferences("LocationData", Context.MODE_PRIVATE);
		startLocCoords = spfridedata.getString("LastLoc latlng", null);
		destLocCoords = spfridedata.getString("Destination Latlng", null);
		startLocAddress = spfridedata.getString("LastLoc Address", null);
		destLocAddress = spfridedata.getString("Destination Address", null);

		String text = "Hello I am travelling from "+startLocAddress+"with coordinates"+startLocCoords+"and my destination is "+destLocAddress+ "with coordinates"+destLocCoords;
		String msg = "Hello I am travelling from "+startLocAddress+"with coordinates"+startLocCoords;
		String msg1 = "and my destination is "+destLocAddress+ "with coordinates"+destLocCoords;

		Log.e("msg ","\n"+text);
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(userPhone, guardPhone ,  msg , null, null);
		smsManager.sendTextMessage(userPhone, guardPhone ,  msg1 , null, null);
//		smsManager.sendTextMessage(userPhone, guardPhone ,  msg , null, null);
//		smsManager.sendTextMessage(userPhone, guardPhone ,  msg , null, null);

		Toast.makeText(context, "Sms Sent", Toast.LENGTH_SHORT).show();

	}


}
