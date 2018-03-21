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
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;
import static android.os.Build.VERSION_CODES.O;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_BLUE;

public class CurrentLocation extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener,
		com.google.android.gms.location.LocationListener,
		RoutingListener {

	private String TAG = "Safe Safari";
	private GoogleMap mMap;
	private GoogleApiClient client;
	GoogleApiClient aclient;
	private LocationRequest locationRequest;
	private FusedLocationProviderClient fusedLocationProviderClient;
	private Marker mCurrLocationMarker;
	private Location lastlocation;
	private List<Polyline> polylines;
	private double total_distance;
	private LatLng dest_latlng;
	private Marker dest_marker;
	private Marker start_marker;
	private Button ridenow;
	private FirebaseAuth mAuth = FirebaseAuth.getInstance();
	private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

	String time;
	String user = mAuth.getCurrentUser().getUid();

	private static final int[] COLORS = new int[]{R.color.primary_dark_material_light};

	public static final int REQUEST_LOCATION_CODE = 99;

	static float checkDistance = 0;
	int count = 0;
	Boolean flag = true;
	final DatabaseReference mDatabaseUID = mDatabase.child("User").child(user).child("rides").push();




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_current_location);


		 Long tsLong = System.currentTimeMillis()/1000;
		 time = tsLong.toString() ;


		ridenow = (Button) findViewById(R.id.ridebtn);

		ridenow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(CurrentLocation.this, vehicleData_GET.class);
				startActivity(intent);
			}
		});

		polylines = new ArrayList<>();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			checklocationPermission();
		}

		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

		final MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.draggable(true);

		//**********************Places Search*********************

		PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
				getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

		autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
			@Override
			public void onPlaceSelected(Place place) {
				Toast.makeText(CurrentLocation.this, "Your Address is" + place.getAddress(), Toast.LENGTH_SHORT).show();
				dest_latlng = place.getLatLng();


				////***********GEOFIRING*************************
//				mDatabaseUID.child("dest").setValue(dest_latlng.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
//					@Override
//					public void onSuccess(Void aVoid) {
//						Toast.makeText(CurrentLocation.this, "DEST LOC OK!", Toast.LENGTH_SHORT).show();
//					}
//				});
				////***********GEOFIRING*************************


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

//	protected synchronized void abuildGoogleApiClient() {
//
//		aclient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
//				.addOnConnectionFailedListener(this)
//				.addApi(LocationServices.API)
//				.build();
//		aclient.connect();
//
//		//Toast.makeText(this, "ACLIENT DONE", Toast.LENGTH_SHORT).show();
//	}

	@Override
	public void onConnected(@Nullable Bundle bundle) {
		locationRequest = new LocationRequest();
		locationRequest.setInterval(5000);
		locationRequest.setFastestInterval(5000);
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == (PackageManager.PERMISSION_GRANTED)) {
			LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
		}


//		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == (PackageManager.PERMISSION_GRANTED)) {
//			LocationServices.FusedLocationApi.requestLocationUpdates(aclient, locationRequest, this);
//
//		}

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
		LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());


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


		if (dest_latlng != null) {
			float[] result = new float[1];

			Location.distanceBetween(lastlocation.getLatitude(), lastlocation.getLongitude(), dest_latlng.latitude, dest_latlng.longitude, result);

			float curr_distance = Float.parseFloat(String.format("%.1f", result[0]));
			Toast.makeText(this, "Current Distance: " + curr_distance, Toast.LENGTH_SHORT).show();

			if (flag) {
				checkDistance = curr_distance;
				Toast.makeText(this, "Check Distance is: " + checkDistance, Toast.LENGTH_SHORT).show();

				//Storing Start Position
				if (client != null) {
					mDatabaseUID.child("start").setValue(latLng.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
						@Override
						public void onSuccess(Void Void) {
							Toast.makeText(CurrentLocation.this, "Start LOC OK!", Toast.LENGTH_SHORT).show();
						}
					});
				}

				flag = false;
			} else {
				if (checkDistance < curr_distance) {
					count++;
					Toast.makeText(this, "Count is: " + count, Toast.LENGTH_SHORT).show();
					if (count >= 5) {
						Notification();
						//Toast.makeText(this, "Alarm", Toast.LENGTH_SHORT).show();
					}
				} else {
					count = 0;
					checkDistance = curr_distance;
				}
			}
		}


//		if (client !=null) {
//			start_marker = mMap.addMarker(new MarkerOptions().position(latLng));
//			mDatabaseUID.child("start").setValue(latLng.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
//				@Override
//				public void onSuccess(Void aVoid) {
//					Toast.makeText(CurrentLocation.this, "Start LOC OK!", Toast.LENGTH_SHORT).show();
//				}
//			});
//			start_marker.remove();
//			Toast.makeText(this, "Marker Added", Toast.LENGTH_SHORT).show();
//			LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
//		}
//
//	}
	}
	private void Notification(){

		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(this, "notify_001");

		NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
		bigText.setBigContentTitle("ALERT!!");
		bigText.setSummaryText("Press Snooze Button to ENSURE your Safety");

		Intent snoozeIntent = new Intent(this, CurrentLocation.class);
		//snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, 1);
		PendingIntent snoozePendingIntent =
				PendingIntent.getBroadcast(this, 1, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		mBuilder.setSmallIcon(R.drawable.icon);
		mBuilder.setContentTitle("Alert Notification");
		mBuilder.setContentText("Press Snooze");
		mBuilder.setCategory(Notification.CATEGORY_ALARM);
		mBuilder.setPriority(Notification.PRIORITY_MAX);
		mBuilder.setStyle(bigText);
		mBuilder.setVisibility(2);
		mBuilder.addAction(R.drawable.phone_icon, "Snooze", snoozePendingIntent);

		//Playing Audio
		int resID = R.raw.alert;
		MediaPlayer alert = MediaPlayer.create(this, resID);
		alert.start();


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


		Toast.makeText(this, "Route Distance(in KM) is" + total_distance / 1000, Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onRoutingCancelled() {

	}

}
