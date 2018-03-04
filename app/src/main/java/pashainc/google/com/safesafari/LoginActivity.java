package pashainc.google.com.safesafari;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "Login Activity";

    //Firebase
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    private EditText u_email, u_pw;
    private Button login_btn;
    private ProgressBar progressBar;
    private DatabaseReference mDatabase;
    private TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setupFirebaseAuth();

        u_email = (EditText) findViewById(R.id.u_email_in);
        u_pw = (EditText) findViewById(R.id.u_pw_in);

        login_btn = (Button) findViewById(R.id.login_btn);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        //Login Button Action
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = u_email.getText().toString();
                String pw = u_pw.getText().toString();

                FirebaseAuth.getInstance().signInWithEmailAndPassword(email,pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                            Toast.makeText(LoginActivity.this, "Voila!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(LoginActivity.this, CurrentLocation.class);
                            startActivity(intent);


                    }
                });
            }
        });


        register = (TextView) findViewById(R.id.register_user);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent to Register
                Intent intent = new Intent(LoginActivity.this, Register.class);
                startActivity(intent);

            }
        });
    }


    private void setupFirebaseAuth() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged: signed_in: " + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged: signed_out: ");
                }
            }
        };


    }


    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthStateListener != null){
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener);
        }
    }
    protected void onStart(){
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener);
    }
}
