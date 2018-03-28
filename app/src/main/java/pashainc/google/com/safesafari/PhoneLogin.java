package pashainc.google.com.safesafari;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class PhoneLogin extends AppCompatActivity {

    String TAG = "PhoneAuth";

    private FirebaseAuth mAuth;
//    private ProgressDialog  pbar;
    private EditText userPhone, verfCode;
    private Button loginBtn, send_code;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth.AuthStateListener mAuthstateListener;
    public DatabaseReference mDatabase;

    SessionMgt session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonelogin);

        session = new SessionMgt(getApplicationContext());

        mAuth = FirebaseAuth.getInstance();

        userPhone = (EditText) findViewById(R.id.user_phone);
        send_code = (Button) findViewById(R.id.send_code);

        send_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phonenumber = userPhone.getText().toString();

                userPhone.setEnabled(false);
//                loginBtn.setEnabled(false);

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phonenumber,
                        60,
                        TimeUnit.SECONDS,
                        PhoneLogin.this,
                        mCallbacks
                );
            }
        });

        /*mAuthstateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mAuth.getCurrentUser() !=null){
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    mDatabase = FirebaseDatabase.getInstance().getReference();

                    final String key = mAuth.getCurrentUser().getUid();

                    DatabaseReference mDatabaseUID = mDatabase.child("User");

                    mDatabaseUID.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.hasChild(key)) {
                                // run some code
                                Intent currentLocation = new Intent(PhoneLogin.this, CurrentLocation.class);
                                startActivity(currentLocation);
                            }else{
                                Intent register = new Intent(PhoneLogin.this, Register.class);
                                startActivity(register);
                            }
                            finish();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
///////////////////////////////////////////////////////////////////////////////////////
                    Log.e("msg","kry: "+ key);

                }
                else {
                    Toast.makeText(PhoneLogin.this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
                }
            }
        };*/




        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Toast.makeText(PhoneLogin.this, "Verification Failed", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                Log.d(TAG, "onCodeSent:" + verificationId);

                mVerificationId = verificationId;
                mResendToken = token;

            }

        };

    }



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

//////////////////////////////////////////////////////////////////////////////////
                            mDatabase = FirebaseDatabase.getInstance().getReference();

                            final String key = mAuth.getCurrentUser().getUid();

                            DatabaseReference mDatabaseUID = mDatabase.child("User");

                           // mDatabaseUID.setValue(key);

                            mDatabaseUID.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    if (snapshot.hasChild(key)) {
                                        // run some code
                                        Intent currentLocation = new Intent(PhoneLogin.this, CurrentLocation.class);
                                        startActivity(currentLocation);
                                    }else{
                                        Intent register = new Intent(PhoneLogin.this, Register.class);
                                        startActivity(register);
                                    }
                                    finish();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
///////////////////////////////////////////////////////////////////////////////////////

                        } else {
                            // Sign in failed, display a message and update the UI
                            Toast.makeText(PhoneLogin.this, "Sign In Failed!", Toast.LENGTH_SHORT).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    @Override
    protected void onStart(){
        super.onStart();

        //mAuth.addAuthStateListener(mAuthstateListener);
    }


}
