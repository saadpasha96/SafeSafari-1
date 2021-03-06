package pashainc.google.com.safesafari;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    DatabaseReference mDatabase;
    private FirebaseAuth mAuth;


    private ProgressBar pbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText) findViewById(R.id.name);
        //phone = (EditText) findViewById(R.id.phone);
        guard_name = (EditText) findViewById(R.id.guard_name);
        guard_phone = (EditText) findViewById(R.id.guard_phone);

        int maxLength = 12;
        int minLength = 11;
        guard_phone.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
        guard_phone.setFilters(new InputFilter[] {new InputFilter.LengthFilter(minLength)});
        register = (Button) findViewById(R.id.Register);

        pbar = (ProgressBar) findViewById(R.id.progressBar2);
        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "OnClick: attempting to register");
                pbar.setVisibility(view.VISIBLE);
                //check empty edit fields
                if (!isEmpty(name.getText().toString()) && !isEmpty(guard_name.getText().toString()) && !isEmpty(guard_phone.getText().toString())) {

//                    new OnSuccessListener() {
//                        @Override
//                        public void onSuccess(Object o) {
                            String user = mAuth.getCurrentUser().getUid();
                            DatabaseReference mDatabaseUID = mDatabase.child("User").child(user);
                            mDatabaseUID.child("Name").setValue(name.getText().toString());
                            mDatabaseUID.child("Guardian Name").setValue(guard_name.getText().toString());
                            mDatabaseUID.child("Guardian Phone").setValue(guard_phone.getText().toString());

                            Toast.makeText(Register.this, "Success!", Toast.LENGTH_SHORT).show();

                            Intent Register = new Intent(Register.this, CurrentLocation.class);
                            startActivity(Register);
                            finish();
//                        }
                    //};

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


