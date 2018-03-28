package pashainc.google.com.safesafari;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreen extends AppCompatActivity {


	String TAG = "PhoneAuth";

	private FirebaseAuth mAuth;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		mAuth = FirebaseAuth.getInstance();


		if(mAuth.getCurrentUser()!=null){
			Toast.makeText(getApplicationContext(),"Exist",Toast.LENGTH_LONG).show();
			Intent intent = new Intent(SplashScreen.this,CurrentLocation.class);
			startActivity(intent);
			//Intent
		}else{
			Toast.makeText(getApplicationContext(),"Not Exist",Toast.LENGTH_LONG).show();
			Intent intent = new Intent(SplashScreen.this,PhoneLogin.class);
			startActivity(intent);

		}


//		mAuthstateListener = new FirebaseAuth.AuthStateListener() {
//			@Override
//			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//				if (mAuth.getCurrentUser() !=null){
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//					mDatabase = FirebaseDatabase.getInstance().getReference();
//
//					final String key = mAuth.getCurrentUser().getUid();
//
//					DatabaseReference mDatabaseUID = mDatabase.child("User");
//
//					mDatabaseUID.addListenerForSingleValueEvent(new ValueEventListener() {
//						@Override
//						public void onDataChange(DataSnapshot snapshot) {
//							if (snapshot.hasChild(key)) {
//								// run some code
//								Intent currentLocation = new Intent(SplashScreen.this, CurrentLocation.class);
//								startActivity(currentLocation);
//							}else{
//								Intent register = new Intent(SplashScreen.this, Register.class);
//								startActivity(register);
//							}
//							finish();
//						}
//
//						@Override
//						public void onCancelled(DatabaseError databaseError) {
//
//						}
//					});
/////////////////////////////////////////////////////////////////////////////////////////
//					Log.e("msg","kry: "+ key);
//
//				}
//				else {
//					Toast.makeText(SplashScreen.this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
//				}
//			}
//		};
//
//		mAuth.addAuthStateListener(mAuthstateListener);
//
//
	}
}