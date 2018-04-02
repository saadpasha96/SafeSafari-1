package pashainc.google.com.safesafari;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.CursorIndexOutOfBoundsException;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;


public class vehicleData_GET extends AppCompatActivity {

    String server_url = "http://www.mtmis.excise-punjab.gov.pk";

//     static DatabaseReference mDatabaseUID;
//    int counter;

    /****************************Firebase Setting********************************/

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String user = mAuth.getCurrentUser().getUid();

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();



    public final DatabaseReference mDatabaseUID = mDatabase.child("rides").child(user).push();
    public final String key = mDatabaseUID.getKey();


    EditText Number;
    TextView vhldata;
    ImageButton getDetails;
    String number;
    String vehicleData;
    Toolbar toolbar;
    Button savetofb;
    ProgressBar pBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_data__get);

//        mDatabaseUID = mDatabase.child("rides").child(user).child("ride no "+counter);
//        mDatabase.child("row 1").setValue("1");
//        mDatabase.child("row 2").setValue("1");
//        mDatabase.child("row 3").setValue("1");


//        Toast.makeText(this, "key is "+key, Toast.LENGTH_SHORT).show();

        /***********Shared Pref*********/
//        if (alpha) {
//            final SharedPreferences sharedPref = this.getSharedPreferences("KEYS", MODE_PRIVATE);
//            final SharedPreferences.Editor editor = sharedPref.edit();
//            editor.putString("RIDE_KEY", key);
//            editor.apply();
//            alpha = false;
//        }
        /***********Shared Pref*********/

        savetofb = (Button) findViewById(R.id.save);
        vehicleData = "";

        pBar = (ProgressBar) findViewById(R.id.progressingB);


        Number = (EditText) findViewById(R.id.vhlno);

        getDetails = (ImageButton) findViewById(R.id.send);

        vhldata = (TextView) findViewById(R.id.vhldata);

        getDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                number = Number.getText().toString();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(Number.getWindowToken(), 0);
                pBar.setVisibility(View.VISIBLE);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
//                                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                                Log.i("VOLLEY", response);

                                Document doc = Jsoup.parse(response);
                                Elements trs = doc.select("table tr");

                                vehicleData = "";
                                for (Element tr : trs){
                                    Elements tds = tr.getElementsByTag("td");
                                    if( tds.toString() != "" && tds.toString() != null && !tds.toString().isEmpty())
                                        vehicleData +=  vehicleData_GET.html2text(tds.toString()) + "\n";
                                }

                                vhldata.setText(vehicleData);
                                vhldata.setVisibility(View.VISIBLE);
                                pBar.setVisibility(View.GONE);

             /**************************Saving Vehicle Data to Firebase*****************************************/

                                savetofb.setVisibility(View.VISIBLE);
                                savetofb.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mDatabaseUID.child("vhldata").setValue(vhldata.getText().toString());
                                        Toast.makeText(vehicleData_GET.this, "Successfully saved!", Toast.LENGTH_SHORT).show();
                                        startActivity(CurrentLocation.getIntent(view.getContext()).putExtra("myKey", key).putExtra("showPlaceSearch", "1"));
                                        finish();
                                    }

                                });
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
//                                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                            }
                        }) {
                    // SLM-11-4455
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("vhlno", number);
                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type", "application/x-www-form-urlencoded");
                        return params;
                    }

                };


                MySingleton.getmInstance(vehicleData_GET.this).addToRequestque(stringRequest);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        startActivity(CurrentLocation.getIntent(this).putExtra("myKey", key));
        finish();
//        Toast.makeText(this, "Test Backpress", Toast.LENGTH_SHORT).show();
    }


    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }
}
