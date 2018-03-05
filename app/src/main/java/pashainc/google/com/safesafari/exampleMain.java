package pashainc.google.com.safesafari;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class exampleMain extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_main);

        mAuth = FirebaseAuth.getInstance();
    }


    @Override
    public void onStart(){
        super.onStart();
        if (mAuth.getCurrentUser() == null ){
            Intent intent = new Intent(exampleMain.this, PhoneLogin.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "All DONE!", Toast.LENGTH_SHORT).show();
        }


    }
}
