package pashainc.google.com.safesafari;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.text.TextUtils.isEmpty;

public class ProfileUpdate extends AppCompatActivity {

	private static final String TAG = "Register Activity";
	private EditText updatename, updateguard_name, updateguard_phone;
	private Button update;
	DatabaseReference mDatabase;
	private FirebaseAuth mAuth;
	private ProgressBar pbar;
	private android.support.v7.widget.Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_update);

		toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		DrawerUtil.getDrawer(this, toolbar);

		updatename = (EditText) findViewById(R.id.updatename);
		//phone = (EditText) findViewById(R.id.phone);
		updateguard_name = (EditText) findViewById(R.id.updateguard_name);
		updateguard_phone = (EditText) findViewById(R.id.updateguard_phone);

		int maxLength = 12;
		int minLength = 11;
		updateguard_phone.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
		updateguard_phone.setFilters(new InputFilter[] {new InputFilter.LengthFilter(minLength)});
		update = (Button) findViewById(R.id.Update);

		pbar = (ProgressBar) findViewById(R.id.progressBar2);
		mAuth = FirebaseAuth.getInstance();

		mDatabase = FirebaseDatabase.getInstance().getReference();

		update.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.d(TAG, "OnClick: attempting to register");
				pbar.setVisibility(view.VISIBLE);
				//check empty edit fields
				if (!isEmpty(updatename.getText().toString()) || !isEmpty(updateguard_name.getText().toString()) || !isEmpty(updateguard_phone.getText().toString())) {

					String user = mAuth.getCurrentUser().getUid();
					DatabaseReference mDatabaseUID = mDatabase.child("User").child(user);
					mDatabaseUID.child("Name").setValue(updatename.getText().toString());
					mDatabaseUID.child("Guardian Name").setValue(updateguard_name.getText().toString());
					mDatabaseUID.child("Guardian Phone").setValue(updateguard_phone.getText().toString());

					Toast.makeText(ProfileUpdate.this, "Profile Update Successfully!", Toast.LENGTH_SHORT).show();
					Intent Home = new Intent(ProfileUpdate.this, CurrentLocation.class);
					startActivity(Home);
					finish();
					pbar.setVisibility(View.GONE);
				}
				else {
					Toast.makeText(ProfileUpdate.this, "You must fill all the fields", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}
