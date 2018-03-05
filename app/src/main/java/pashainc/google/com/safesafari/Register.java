package pashainc.google.com.safesafari;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class Register extends AppCompatActivity {

    private static final String TAG = "Register Activity";
    private EditText name, email, password, phone, guard_name, guard_phone;
    private Button register;
    private ProgressBar pbar;
    DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    String code,phoneNumber,mVerificationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText) findViewById(R.id.name);
        //phone = (EditText) findViewById(R.id.phone);
        guard_name = (EditText) findViewById(R.id.guard_name);
        guard_phone = (EditText) findViewById(R.id.guard_phone);
        register = (Button) findViewById(R.id.Register);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "OnClick: attempting to register");
                //check empty edit fields
                if (!isEmpty(name.getText().toString()) && !isEmpty(guard_name.getText().toString()) && !isEmpty(guard_phone.getText().toString())) {

                    String user = mAuth.getCurrentUser().getUid();
                    DatabaseReference mDatabaseUID = mDatabase.child("User").child(user);
                    mDatabaseUID.child("Name").setValue(name.getText().toString());
                    mDatabaseUID.child("Guardian Name").setValue(guard_name.getText().toString());
                    mDatabaseUID.child("Guardian Phone").setValue(guard_phone.getText().toString());

                    Toast.makeText(Register.this, "Success!", Toast.LENGTH_SHORT).show();

//                    //NewUserReg(email.getText().toString(), password.getText().toString());
//                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
//                            .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    if (task.isSuccessful()) {
//                                        // Sign in success, update UI with the signed-in user's information
//                                        Log.d(TAG, "createUserWithEmail:success");
//
//                                    }
//                                    else {
//                                        // If sign in fails, display a message to the user.
//                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                                        Toast.makeText(Register.this, "Authentication failed.",
//                                                Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//
                } else {
                    Toast.makeText(Register.this, "You must fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
//    private void NewUserReg(final String email, String password){
//        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                Log.d(TAG, "on Complete: On Complete "+ task.isSuccessful());
//
//                if (task.isSuccessful()){
//                    Log.d(TAG," On Complete: Auth State: " + FirebaseAuth.getInstance().getCurrentUser().getUid());
//                    //Toast.makeText(Register.this, "TestTest", Toast.LENGTH_SHORT).show();
////                    FirebaseAuth.getInstance().signOut();
//
//                    String uid  = task.getResult().getUser().getUid();
//                    Toast.makeText(Register.this, uid, Toast.LENGTH_SHORT).show();
//
//                   /* DatabaseReference mDatabaseUid = mDatabase.child("User").child(uid);
//
//                    mDatabaseUid.child("Name").setValue(name);
//                    mDatabaseUid.child("Phone").setValue(phone);
////                    mDatabaseUid.child("Email").setValue(email);
//
//                    mDatabaseUid.child("Guardian Name").child("Name").setValue(guard_name);
//                    mDatabaseUid.child("FamilyContact").child("Contact").setValue(guard_phone).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Toast.makeText(getApplicationContext(),"DATA UPLOADED",Toast.LENGTH_SHORT).show();
//                        }
//                    }); */
//                }
//
//                else {
//                    Toast.makeText(Register.this, "Unable To Register", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });


    private Boolean isEmpty(String string) {
        return string.equals("");
    }

}


