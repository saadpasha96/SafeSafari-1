package pashainc.google.com.safesafari;

import android.*;
import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.CursorIndexOutOfBoundsException;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.materialdrawer.Drawer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;
import static android.os.Build.VERSION_CODES.O;
import static cafe.adriel.androidaudiorecorder.model.AudioSource.MIC;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_BLUE;

public class CurrentLocation extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener,
		com.google.android.gms.location.LocationListener,
		RoutingListener {

	private String TAG = "Safe Safari";
	private GoogleMap mMap;
	private GoogleApiClient client;
	private LocationRequest locationRequest;
	private Marker mCurrLocationMarker;
	private Location lastlocation;
	private List<Polyline> polylines;
	private double total_distance;
	private LatLng dest_latlng;
	private Marker dest_marker;
	private Marker start_marker;
	private Button ridenow;

	vehicleData_POST vhlpost = new vehicleData_POST();

	private FirebaseAuth mAuth = FirebaseAuth.getInstance();
	private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

	String time;
	String user = mAuth.getCurrentUser().getUid();

	private static final int[] COLORS = new int[]{R.color.primary_dark_material_light};

	public static final int REQUEST_LOCATION_CODE = 99;

	static float checkDistance = 0;
	int count = 0;
	Boolean flag = true;
	Boolean notificationFlag = true;


	private String ridekey = "";

	public DatabaseReference mDatabaseUID = null;

	private Toolbar toolbar;

	public Geocoder geocoder;


	public LinearLayout placelayout;

	public Button ridetrackbtn;

	public SharedPreferences.Editor locationEditor;
	SharedPreferences sharedPreferencesforLocation;

//	@Override
//	protected void onResume() {
//		super.onResume();
//		Intent intent = getIntent();
//
//		if(intent.hasExtra("myKey")){
//			ridekey = getIntent().getStringExtra("myKey");
//			mDatabaseUID = mDatabase.child("rides").child(user).child(ridekey);
//			Toast.makeText(this, "Key in Current Loc is" + ridekey, Toast.LENGTH_SHORT).show();
//		}
//	}

	public String dest_coords;
	public String dest_address;
	public String current_coords;
	public String current_address;
	public String start_coords;
	public String start_address;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_current_location);

		toolbar = (Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		DrawerUtil.getDrawer(this, toolbar);

		SharedprefWrite spfwr = new SharedprefWrite(getApplicationContext());
		spfwr.getfirebasedata();



		/******Sharedref For User's Location*********/
		sharedPreferencesforLocation = getSharedPreferences("LocationData", MODE_PRIVATE);
		locationEditor = sharedPreferencesforLocation.edit();

		/******Sharedref For User's Location*********/

		ridenow = (Button) findViewById(R.id.ridebtn);

		ridenow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(CurrentLocation.this, vehicleData_GET.class);
				startActivity(intent);
			}
		});

		placelayout = (LinearLayout) findViewById(R.id.placelayout);

		ridetrackbtn = (Button) findViewById(R.id.startbtn);
		ridetrackbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mDatabaseUID.child("destAddress").setValue(dest_address);
				dest_coords = dest_latlng.toString();
				mDatabaseUID.child("destCoords").setValue(dest_coords);

				mDatabaseUID.child("startCoords").setValue(start_coords);
				mDatabaseUID.child("startAddress").setValue(start_address);

				locationEditor.putString("Destination Address", dest_address);
				locationEditor.putString("Destination Latlng", dest_coords);
				locationEditor.putString("Start Coords", start_coords);
				Boolean result = locationEditor.commit();
				Toast.makeText(CurrentLocation.this, "Done"+result.toString() , Toast.LENGTH_SHORT).show();

				SmsSend sms = new SmsSend();
				sms.send(getApplicationContext());

			}
		});

		Intent intent = getIntent();
		if(intent.hasExtra("myKey") && intent.hasExtra("showPlaceSearch") && intent.hasExtra("hideRideNowBtn")){
			placelayout.setVisibility(View.VISIBLE);
			ridekey = getIntent().getStringExtra("myKey");
			mDatabaseUID = mDatabase.child("rides").child(user).child(ridekey);
			ridenow.setVisibility(View.GONE);
			//Toast.makeText(this, "Key in Current Loc is" + ridekey, Toast.LENGTH_SHORT).show();
		}
//		Intent intent = getIntent();
//		if(intent != null) {
//			ridekey = intent.getStringExtra("mykey");
//			Toast.makeText(this, "koi chakkar hy " + intent.getStringExtra("myKey"), Toast.LENGTH_SHORT).show();
//		}
//		else {
//			Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
//		}

//		/***********Shared Pref*********/
//		SharedPreferences sharedPref = this.getSharedPreferences("KEYS",MODE_PRIVATE);
//		//Toast.makeText(this, "Key is " + sharedPref.getString("RIDE_KEY", null),  Toast.LENGTH_SHORT).show();
//
//		// ridekey =  sharedPref.getString("RIDE_KEY", null);
//		/***********Shared Pref*********/


		geocoder = new Geocoder(this, Locale.getDefault());

		Long tsLong = System.currentTimeMillis()/1000;
		time = tsLong.toString() ;



		polylines = new ArrayList<>();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			checklocationPermission();
		}

		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

//		final MarkerOptions markerOptions = new MarkerOptions();

		/**********************Places Search*********************/

		final PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
				getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

		autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
			@Override
			public void onPlaceSelected(Place place) {

				AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
						.setTypeFilter(place.TYPE_COUNTRY)
						.setCountry("PK")
						.build();
				autocompleteFragment.setFilter(autocompleteFilter);

				//autocompleteFragment.setBoundsBias(new LatLngBounds(southwestLatLng, northeastLatLng));


				if (dest_marker != null) {
					dest_marker.remove();
				}

				Toast.makeText(CurrentLocation.this, "Your Address is" + place.getAddress(), Toast.LENGTH_SHORT).show();

				dest_latlng = place.getLatLng();

				List<Address> destAddresses;
				try {
					destAddresses = geocoder.getFromLocation(dest_latlng.latitude, dest_latlng.longitude, 1);
					dest_address = destAddresses.get(0).getAddressLine(0);

					//Toast.makeText(CurrentLocation.this, ""+result, Toast.LENGTH_SHORT).show();
				}
				catch (IOException e) {
					e.printStackTrace();
				}


				//mDatabaseUID.child("destAddress").setValue(place.getAddress());
//				mDatabaseUID.child("destCoords").setValue(dest_latlng.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
//					@Override
//					public void onSuccess(Void Void) {
//						Toast.makeText(CurrentLocation.this, "Dest LOC Saved!", Toast.LENGTH_SHORT).show();
//					}
//				});


				DrawRoute(dest_latlng);
				dest_marker = mMap.addMarker(new MarkerOptions().position(dest_latlng).title("Your Destination")
						.icon(BitmapDescriptorFactory.defaultMarker(HUE_BLUE)));
				//Camera Properties
				mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dest_latlng, 15));

			}



			@Override
			public void onError(Status status) {
				// TODO: Handle the error.
			}
		});

		/**********************Places Search Ends*********************/
	}



	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case REQUEST_LOCATION_CODE:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					//permission granted
					if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
						if (client == null) {
							buildGoogleApiClient();
						}
						mMap.setMyLocationEnabled(true);
					}
					//permission not granted
					else {
						Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
					}
				}
		}
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;

		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

			buildGoogleApiClient();

			mMap.setMyLocationEnabled(true);

		}

	}

	private void DrawRoute(LatLng dest_latlng) {
		Routing routing = new Routing.Builder()
				.travelMode(Routing.TravelMode.DRIVING)
				.withListener(this)
				.alternativeRoutes(false)
				.waypoints(new LatLng(lastlocation.getLatitude(), lastlocation.getLongitude()), dest_latlng)
				.build();
		routing.execute();

	}

	protected synchronized void buildGoogleApiClient() {
		client = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.build();
		client.connect();

	}

	@Override
	public void onConnected(@Nullable Bundle bundle) {
		locationRequest = new LocationRequest();
		locationRequest.setInterval(1000);
		locationRequest.setFastestInterval(1000);
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == (PackageManager.PERMISSION_GRANTED)) {
			LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
		}

	}


	public boolean checklocationPermission() {
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
			} else {
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
			}
			return false;

		} else {
			return false;
		}
	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

	}


	@Override
	public void onLocationChanged(final Location location) {
		lastlocation = location;

		if (mCurrLocationMarker != null) {
			mCurrLocationMarker.remove();
		}

		//Getting Start Location Coordinates
		final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());


		//Setting Up the the Marker
		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(latLng);
		markerOptions.title("Current Location");
		markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
		markerOptions.draggable(false);

		//Updating Maker
		mCurrLocationMarker = mMap.addMarker(markerOptions);


		//Camera Properties
		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
		Log.d("LOC", "Changed");




	/******************************Ride Tracking**********************************************************/

//	ridetrackbtn.setOnClickListener(new View.OnClickListener() {
//		@Override
//		public void onClick(View view) {

			if (dest_latlng != null) {
				float[] result = new float[1];

				Location.distanceBetween(lastlocation.getLatitude(), lastlocation.getLongitude(), dest_latlng.latitude, dest_latlng.longitude, result);

				mDatabaseUID.child("lastlocCoords").setValue(latLng.toString());

				/*****Use these Coords for sending SMS both for Alert & Start Ride******/
				locationEditor.putString("LastLoc latlng", latLng.toString());
				locationEditor.commit();
				//Log.e("LastLoc Coords", latLng.toString() );


				List<Address> currentAddresses;
				try {
					currentAddresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
					String startAddress = currentAddresses.get(0).getAddressLine(0);
					mDatabaseUID.child("currentAddress").setValue(startAddress);

					/*****Use this Address for sending SMS both for Alert & Start Ride******/
					locationEditor.putString("LastLoc Address", startAddress);
					locationEditor.commit();

				}
				catch (IOException e) {
					e.printStackTrace();
				}


				float curr_distance = Float.parseFloat(String.format("%.1f", result[0]));
				//Toast.makeText(this, "Current Distance: " + curr_distance, Toast.LENGTH_SHORT).show();

				if (flag) {
					checkDistance = curr_distance;

					/******************Storing Start Position to Firebase*********************/
					if (client != null) {

						List<Address> startAddresses;
						try {
							startAddresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
							start_address = startAddresses.get(0).getAddressLine(0);
//							mDatabaseUID.child("startAddress").setValue(start_address);

						}
						catch (IOException e) {
							e.printStackTrace();
						}
//
						start_coords = latLng.toString();
//						mDatabaseUID.child("startCoords").setValue(start_coords);
					}
					/******************Storing Start Position to Firebase*********************/


					flag = false;
				}
				else {
					if (checkDistance < curr_distance) {
						count++;
						Toast.makeText(CurrentLocation.this, "Count is: " + count, Toast.LENGTH_SHORT).show();

						if (count >= 3) {
							if (notificationFlag) {
//								SharedPreferences spfread = getSharedPreferences("notificationFlagRead", MODE_PRIVATE);
//								notificationFlag = spfread.getBoolean("flag",false );
								Notification();
								notificationFlag = false;

							}
						}
					}
//				if (checkDistance-curr_distance <= 20){
//					Toast.makeText(this, "Destination Reached", Toast.LENGTH_SHORT).show();
//				}
					else {
						count = 0;
						checkDistance = curr_distance;
					}
				}
			}
			/******************************Ride Tracking**********************************************************/
//		}
//	});


	}
	private void Notification(){

		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(this, "notify_001");

		NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
		bigText.setBigContentTitle("ALERT!!");
		bigText.setSummaryText("Press Snooze Button to ENSURE your Safety");

		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		// Vibrate for 500 milliseconds
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			v.vibrate(VibrationEffect.createOneShot(10000, VibrationEffect.DEFAULT_AMPLITUDE));
		}else{
			//deprecated in API 26
			v.vibrate(500);
		}


		Intent snoozeIntent = new Intent(this, AlertReceiver.class);
		snoozeIntent.putExtra("Stop", 1);

		//snoozeIntent.putExtra(EXTRA_NOTIFICATION_IwD, 1);
		PendingIntent snoozePendingIntent =
				PendingIntent.getBroadcast(this, 1, snoozeIntent, PendingIntent.FLAG_CANCEL_CURRENT);


		Intent sendalert = new Intent(this, SmsSend.class);
		sendalert.putExtra("Send",2);
		PendingIntent alertPendingIntent =
				PendingIntent.getBroadcast(this, 1, sendalert, PendingIntent.FLAG_CANCEL_CURRENT);

//		Intent actintent = new Intent(this, CurrentLocation.class);
//		sendalert.putExtra("Send",2);
//		PendingIntent pactintent =
//				PendingIntent.getActivity(this, 1, actintent, PendingIntent.FLAG_CANCEL_CURRENT);




		Toast.makeText(this, "Build OK", Toast.LENGTH_SHORT).show();


		mBuilder.setSmallIcon(R.drawable.ic_warning_black_24dp);
		mBuilder.setTicker("ALERT ALERT");
		mBuilder.setContentTitle("Alert Notification");
		mBuilder.setContentText("Press Snooze");
		mBuilder.setCategory(android.app.Notification.CATEGORY_ALARM);
		mBuilder.setPriority(android.app.Notification.PRIORITY_MAX);
		mBuilder.setStyle(bigText);
		mBuilder.setVisibility(2);
		mBuilder.setTimeoutAfter(45000);

		//mBuilder.setContentIntent(pactintent);
		mBuilder.addAction(R.drawable.ic_warning_black_24dp, "Snooze", snoozePendingIntent).setAutoCancel(true);
		mBuilder.addAction(R.drawable.ic_send_black_24dp, "Send Alert", alertPendingIntent).setAutoCancel(true);


		MediaP m = new MediaP(getApplicationContext());
		m.mp();

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Recorder rec = new Recorder(getApplicationContext());
				try {
					rec.record();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		},45000);

		Handler handler1 = new Handler();
		handler1.postDelayed(new Runnable() {
			@Override
			public void run() {
				SmsSend notifSms = new SmsSend();
				notifSms.alertSms();
			}
		},15000);

		NotificationManager mNotificationManager =
				(NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);


		if (Build.VERSION.SDK_INT >= O) {
			NotificationChannel channel = new NotificationChannel("notify_001",
					"Channel human readable title",
					NotificationManager.IMPORTANCE_DEFAULT);
			mNotificationManager.createNotificationChannel(channel);
		}

		mNotificationManager.notify(0, mBuilder.build());


	}


	//Routing Listeners
	@Override
	public void onRoutingFailure(RouteException e)
	{
		if (e != null)
		{
			Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
		}
		else
		{
			Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onRoutingStart() {

	}

	@Override
	public void onRoutingSuccess(ArrayList<Route> routes, int shortestrouteindex) {

		if (polylines.size() > 0)
		{
			for (Polyline poly : polylines)
			{
				poly.remove();
			}
		}

		polylines = new ArrayList<>();
		//add route(s) to the map.
		for (int i = 0; i < routes.size(); i++) {

			//In case of more than 5 alternative routes
			int colorIndex = i % COLORS.length;

			PolylineOptions polyOptions = new PolylineOptions();
			polyOptions.color(getResources().getColor(COLORS[colorIndex]));
			polyOptions.width(15 + i * 3);
			polyOptions.addAll(routes.get(i).getPoints());
			Polyline polyline = mMap.addPolyline(polyOptions);
			polylines.add(polyline);

			//Toast.makeText(getApplicationContext(),"Route "+ (i+1) +": distance - "+ routes.get(i).getDistanceValue()+": duration - "+ routes.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();

		}
		total_distance = routes.get(0).getDistanceValue();

		Toast.makeText(this, "Route Distance(in KM) is" + total_distance, Toast.LENGTH_SHORT).show();

		mDatabaseUID.child("totaldist").setValue(total_distance);

		String tdistance = String.valueOf(total_distance);
		locationEditor.putString("totalDistance", tdistance);
		locationEditor.commit();

		ridetrackbtn.setVisibility(View.VISIBLE);
	}

	@Override
	public void onRoutingCancelled() {

	}




//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_POWER) {
//			// Do something here...
//			Toast.makeText(this, "hii", Toast.LENGTH_SHORT).show();
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}


	public static Intent getIntent(Context context){
		return new Intent(context, CurrentLocation.class);
	}

}

