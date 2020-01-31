package com.myidea.sih.reportpothole;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.myidea.sih.reportpothole.database.Fire;
import com.myidea.sih.reportpothole.media_capture.NewLocationCapture;
import com.myidea.sih.reportpothole.user.UserDetails;
import com.myidea.sih.reportpothole.user_persistance.UserPersistance;
import com.myidea.sih.reportpothole.util.MeterDistanceGiver;
import com.myidea.sih.reportpothole.util.SavedLatLng;

public class MarkPotholeActivity extends FragmentActivity implements OnMapReadyCallback {

    private Button mark_pothole_button;
    private Button signout_pothole_button;
    private Button user_history_button;

    private boolean globalPlaceExist;

    public static GoogleMap mMapStatic;

    private GoogleMap mMap;
    private static Marker markerStatic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_pothole);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        checkLocation(getApplicationContext());

        UserDetails.userID = UserPersistance.getDefaults("userid",getApplicationContext());
        UserDetails.mobileNumber = UserPersistance.getDefaults("usermobile", getApplicationContext());
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Getting the map as static object.
        mMapStatic = mMap;

        // Start getting the location and updating the location.
        NewLocationCapture.mandatoryForGettingLocation(this);

        LatLng panaji = new LatLng(15.4897858,73.8237997);
        markerStatic = mMapStatic.addMarker(new MarkerOptions().position(panaji).title("My Location"));

        mMapStatic.moveCamera(CameraUpdateFactory.newLatLngZoom(panaji, 18f));

        //Initializing mark_pothole_button.
        mark_pothole_button = findViewById(R.id.mark_pothole_button);
        mark_pothole_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To save the location when the pothole is marked.
                SavedLatLng.lat = markerStatic.getPosition().latitude;
                SavedLatLng.lng = markerStatic.getPosition().longitude;
                // Check the problems marked are near that or not.
                // To start new activity.
                if (checkLocation(getApplicationContext()))
                    checkSamePlaceOrNot();
                else Toast.makeText(getApplicationContext(), "Location not enabled!", Toast.LENGTH_LONG).show();
            }
        });

        // For signing out
        signout_pothole_button = findViewById(R.id.signout_pothole_button);
        signout_pothole_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserPersistance.setDefaults("userid", "", getApplicationContext());
                startActivity(new Intent(MarkPotholeActivity.this, MainActivity.class));
            }
        });

        // For seeing history
        user_history_button = findViewById(R.id.user_history_button);
        user_history_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MarkPotholeActivity.this, UserHistoryActivity.class));
            }
        });

    }

    // To update the location whenever it changes.
    // The method is called inside the NewLocationCapture class.
    public static void updateLocation(double lat, double lng) {
        if (mMapStatic != null) {
            markerStatic.remove();
            LatLng currentLocation = new LatLng(lat, lng);
            markerStatic = mMapStatic.addMarker(new MarkerOptions().position(currentLocation).title("My Location"));
            mMapStatic.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 18f));
        }
    }

    public boolean checkLocation(Context context) {
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if (gps_enabled || network_enabled)
            return true;
        else return false;

    }

    public void checkSamePlaceOrNot() {
        Fire.complaintListDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                boolean placeExist = false;
                Toast.makeText(MarkPotholeActivity.this, "Checking in database! Need a moment please!", Toast.LENGTH_SHORT).show();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    double uploaded_lat = snapshot.child("lat").getValue(Double.class);
                    double uploaded_lng = snapshot.child("lng").getValue(Double.class);
                    System.out.println("()()()() RAN RAN");
                    if (MeterDistanceGiver.distance((float)uploaded_lat, (float)uploaded_lng, (float)SavedLatLng.lat, (float)SavedLatLng.lng) < 10f) {
                        placeExist = true;
                        break;
                    }
                }
                if (!placeExist) {
                    System.out.println("()()()() STARTED");
                    startActivity(new Intent(MarkPotholeActivity.this, SendPotholeActivity.class));
                } else {
                    Toast.makeText(MarkPotholeActivity.this, "Location Already Marked!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void distanceHelper(boolean placeExist) {
        globalPlaceExist = placeExist;
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
