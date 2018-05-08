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
    SharedPrefRead spr;

    String vhdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_data__post);
        spr = new SharedPrefRead(getApplicationContext());

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
//        mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(userid);

        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            result();
            SharedPreferences spr = getSharedPreferences("Vehicle Data", MODE_PRIVATE);
            vhdata = spr.getString("vehicle data", null);
            Log.e("VHL data", "a" +vhdata);
            }

        });
    }

    public void result(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userid = mAuth.getCurrentUser().getUid();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(userid).child("Name");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //String uname =  dataSnapshot.child("Name").getValue().toString();
//                        SharedPrefRead spr = new SharedPrefRead();
//                spr.getName();

                //spr.getName();
                spr.setName(dataSnapshot.getValue().toString());


                Log.e("User ", "ds"+spr.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
