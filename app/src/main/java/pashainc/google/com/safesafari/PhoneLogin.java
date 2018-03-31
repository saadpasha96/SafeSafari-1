package pashainc.google.com.safesafari;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
    public DatabaseReference mDatabase;

    String phonenumber;

    private ProgressBar pbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonelogin);

        mAuth = FirebaseAuth.getInstance();

        userPhone = (EditText) findViewById(R.id.user_phone);
        send_code = (Button) findViewById(R.id.send_code);
        pbar = (ProgressBar) findViewById(R.id.progressBar3);


        int maxLength = 12;
        int minLength = 11;
        userPhone.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
        userPhone.setFilters(new InputFilter[] {new InputFilter.LengthFilter(minLength)});


        send_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                phonenumber = userPhone.getText().toString();

                pbar.setVisibility(view.VISIBLE);
                userPhone.setEnabled(false);
                send_code.setEnabled(false);

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phonenumber,
                        60,
                        TimeUnit.SECONDS,
                        PhoneLogin.this,
                        mCallbacks
                );
            }
        });


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Toast.makeText(PhoneLogin.this, "Verification Failed, Please Enter Your Number Again!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                Log.d(TAG, "onCodeSent:" + verificationId);

                mVerificationId = verificationId;
                mResendToken = token;


            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                resendVerificationCode(phonenumber, mResendToken);
            }
        };

    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
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


}
