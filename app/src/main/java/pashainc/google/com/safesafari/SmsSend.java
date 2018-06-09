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
	String VhlHeading , regnum , chassis, engine , make , model , color , OwnerHeading, Oname , Fname ,city ;

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
	}
//
//	public void alertSms(){
//		SharedPreferences spfread = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
//		userName = spfread.getString("UserName", null);
//		guardPhone = spfread.getString("GuardianPhone", null);
//		userPhone = spfread.getString("UserPhone", null);
//
//		SharedPreferences spfridedata = context.getSharedPreferences("LocationData", Context.MODE_PRIVATE);
//		currLocCoords = spfridedata.getString("LastLoc latlng", null);
//		currLocAddress = spfridedata.getString("LastLoc Address", null);
//		String alertmsg = "ALert! I am in danger. My current coordinates are: \n"+currLocCoords;
//		String alertmsg1 = "and my address is: \n"+currLocAddress;
//
//		Log.e("Alert MSG", alertmsg +"\n"+ alertmsg1);
//
//		SmsManager smsManager = SmsManager.getDefault();
//		smsManager.sendTextMessage(userPhone, guardPhone ,alertmsg, null, null);
//		smsManager.sendTextMessage(userPhone, guardPhone ,alertmsg1, null, null);
//	}

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

		SharedPreferences spfVhlData = context.getSharedPreferences("VehicleData", Context.MODE_PRIVATE);
		VhlHeading = spfVhlData.getString("VhlHeading", null);
		regnum = spfVhlData.getString("regnum", null);
		chassis = spfVhlData.getString("chassis", null);
		engine = spfVhlData.getString("engine", null);
		make = spfVhlData.getString("make", null);
		model = spfVhlData.getString("model", null);
		color = spfVhlData.getString("color", null);
		OwnerHeading = spfVhlData.getString("OwnerHeading", null);
		Oname = spfVhlData.getString("Oname", null);
		Fname = spfVhlData.getString("Fname", null);
		city = spfVhlData.getString("city", null);

		String text = "Hello I am travelling from "+startLocAddress+"with coordinates"+startLocCoords+"and my destination is "+destLocAddress+ "with coordinates"+destLocCoords;
		String msg = "Hello I am travelling from "+startLocAddress+"with coordinates"+startLocCoords;
		String msg1 = "and my destination is "+destLocAddress+ "with coordinates"+destLocCoords;
		String msg2 = "and Vehicle Details: "+"\n"+regnum + "\n"+make + "\n"+model + "\n"+color;
		String msg3 = "Owner Details: "+OwnerHeading + "\n"+Oname + "\n"+Fname + "\n"+city;

		Log.e("msg ","\n"+text);
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(userPhone, guardPhone ,  msg , null, null);
		smsManager.sendTextMessage(userPhone, guardPhone ,  msg1 , null, null);
		smsManager.sendTextMessage(userPhone, guardPhone ,  msg2 , null, null);
		smsManager.sendTextMessage(userPhone, guardPhone ,  msg3 , null, null);
		//smsManager.sendTextMessage(userPhone, guardPhone ,  "" , null, null);



//		smsManager.sendTextMessage(userPhone, guardPhone ,  msg , null, null);
//		smsManager.sendTextMessage(userPhone, guardPhone ,  msg , null, null);

		Toast.makeText(context, "Sms Sent to Guardian", Toast.LENGTH_SHORT).show();

	}

	public void alert(Context context){
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
	}

}
