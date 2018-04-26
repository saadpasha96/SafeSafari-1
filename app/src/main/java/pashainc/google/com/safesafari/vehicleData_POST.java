package pashainc.google.com.safesafari;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class vehicleData_POST extends AppCompatActivity {


    //TextView data;
    private DatabaseReference mDatabase;
    private Button btn;
    private Toolbar toolbar;
    private SharedPreferences pref;
    private FirebaseAuth mAuth;
    SharedPrefRead spr = new SharedPrefRead();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_data__post);

//        data = (TextView) findViewById(R.id.detailsEditText);

//        Intent intent = getIntent();
//        String value = intent.getStringExtra("details");
//
//        data.setText(value);

//        Toast.makeText(this, "Data: " + value, Toast.LENGTH_SHORT).show();

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerUtil.getDrawer(this, toolbar);
//        mAuth = FirebaseAuth.getInstance();
//        String userid = mAuth.getCurrentUser().getUid();
//
//
//        mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(userid);

        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            result();
            }

        });
    }

    public void result(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userid = mAuth.getCurrentUser().getUid();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(userid);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //String uname =  dataSnapshot.child("Name").getValue().toString();
//                        SharedPrefRead spr = new SharedPrefRead();
                        spr.setName(dataSnapshot.getValue(SharedPrefRead.class).getName());
//                        String post = dataSnapshot.getValue().toString();
                //Toast.makeText(vehicleData_POST.this, "User Name is" + spr.getName(), Toast.LENGTH_SHORT).show();
                Log.e("User ", spr.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
