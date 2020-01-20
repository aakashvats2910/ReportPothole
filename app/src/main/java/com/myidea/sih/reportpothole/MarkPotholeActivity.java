package com.myidea.sih.reportpothole;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.myidea.sih.reportpothole.media_capture.NewLocationCapture;
import com.myidea.sih.reportpothole.util.SavedLatLng;

public class MarkPotholeActivity extends FragmentActivity implements OnMapReadyCallback {

    private Button mark_pothole_button;

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
                // To start new activity.
                startActivity(new Intent(MarkPotholeActivity.this, SendPotholeActivity.class));
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

}
