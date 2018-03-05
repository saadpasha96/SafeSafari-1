package pashainc.google.com.safesafari;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ExampleActivity extends AppCompatActivity {

    Button btnReg,btnCode;
    EditText etPhone,etCode;

    private FirebaseAuth mAuth;

    String code,phoneNumber,mVerificationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);

        etPhone = (EditText) findViewById(R.id.Phone);
        etCode = (EditText) findViewById(R.id.Code);
        btnCode = (Button) findViewById(R.id.btnCode);
        btnReg = (Button) findViewById(R.id.btnReg);
        mAuth = FirebaseAuth.getInstance();


        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendCode();



            }
        });


        btnCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                code = etCode.getText().toString();
                signInWithCredential(PhoneAuthProvider.getCredential(mVerificationId,code));
            }
        });
    }



    public void signInWithCredential(PhoneAuthCredential phoneAuthCredential){
        OnCompleteListener<AuthResult> ol = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();

                }
                if(!task.isSuccessful()){

                    Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_LONG).show();

                }
            }
        };


        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(ol);
    }

    public void sendCode(){


        phoneNumber = "03336118070";




        PhoneAuthProvider.OnVerificationStateChangedCallbacks ov = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                signInWithCredential(phoneAuthCredential);
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                mVerificationId = s;
                Toast.makeText(getApplicationContext(),"Sent",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {


            }
        };

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,60, TimeUnit.SECONDS,this,ov);

    }
}
