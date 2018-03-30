package pashainc.google.com.safesafari;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;


public class vehicleData_GET extends AppCompatActivity {

    String server_url = "http://www.mtmis.excise-punjab.gov.pk";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


    EditText Number;
    TextView vhldata;
    ImageButton getDetails;
    String number;
    String vehicleData;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_data__get);

//        toolbar = (Toolbar)findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        DrawerUtil.getDrawer(this, toolbar);


        vehicleData = "";

        Number = (EditText) findViewById(R.id.vhlno);

        getDetails = (ImageButton) findViewById(R.id.send);

        vhldata = (TextView) findViewById(R.id.vhldata);

        getDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number = Number.getText().toString();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(Number.getWindowToken(), 0);

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

//                                Toast.makeText(MainActivity.this, vehicleData
//                                        , Toast.LENGTH_SHORT).show();

//                                Intent myIntent = new Intent(vehicleData_GET.this, vehicleData_POST.class);
//                                myIntent.putExtra("details", vehicleData ); //Optional parameters
//                                startActivity(myIntent);


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

    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }
}
