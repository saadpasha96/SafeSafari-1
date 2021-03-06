package pashainc.google.com.safesafari;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by black on 4/18/2018.
 */

public class SharedprefWrite {

	public DatabaseReference query;
	public String user_name ;
	public String guard_name ;
	public String guard_phone ;
	public String user_phone;
	public SharedPreferences.Editor editor;
	public Context context;
	FirebaseAuth mAuth;

	public SharedprefWrite(Context context) {
		this.context = context;
	}

	public void getfirebasedata() {
		mAuth = FirebaseAuth.getInstance();
		String userid = mAuth.getCurrentUser().getUid();
		user_phone = mAuth.getCurrentUser().getPhoneNumber();

		query = FirebaseDatabase.getInstance().getReference().child("User").child(userid);


		/*********Reading User Static Data from Firebase***********/
		query.addValueEventListener(new ValueEventListener() {

			public void onDataChange(DataSnapshot dataSnapshot) {
				user_name = dataSnapshot.child("Name").getValue().toString();
				guard_name = dataSnapshot.child("Guardian Name").getValue().toString();
				guard_phone = dataSnapshot.child("Guardian Phone").getValue().toString();

				SharedPreferences sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
				editor = sharedPreferences.edit();
				editor.putString("UserName", user_name);
				editor.putString("GuardianName", guard_name);
				editor.putString("GuardianPhone", guard_phone);
				editor.putString("UserPhone", user_phone);
				boolean result = editor.commit();
				String r = String.valueOf(result);
//				Toast.makeText(context, "User Data done"+result, Toast.LENGTH_SHORT).show();
				Log.e("SPF", r);

			}
			public void onCancelled(DatabaseError databaseError) {

			}
		});
		/*********Reading User Static Data from Firebase***********/

	}

//	public void storeinsharedpref(){
//		sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
//		editor = sharedPreferences.edit();
//		editor.putString("UserName", user_name);
//		editor.putString("GuardianName", guard_name);
//		editor.putString("GuardianPhone", guard_phone);
//		editor.commit();
//	}

}
