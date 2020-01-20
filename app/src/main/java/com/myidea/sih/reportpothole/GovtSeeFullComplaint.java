package com.myidea.sih.reportpothole;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.myidea.sih.reportpothole.complaint.UserComplaint;
import com.myidea.sih.reportpothole.util.UserComplaintToShow;

import org.w3c.dom.Text;

public class GovtSeeFullComplaint extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private ImageView fullcomplaint_image_view;
    private TextView fullcomplaint_name_text_view;
    private TextView fullcomplaint_number_text_view;
    private TextView fullcomplaint_comment_text_view;
    private Button fullcomplaint_choose_agency_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_govt_see_full_complaint);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fullcomplaint_image_view = findViewById(R.id.fullcomplaint_image_view);
        fullcomplaint_name_text_view = findViewById(R.id.fullcomplaint_name_text_view);
        fullcomplaint_number_text_view = findViewById(R.id.fullcomplaint_number_text_view);
        fullcomplaint_comment_text_view = findViewById(R.id.fullcomplaint_comment_text_view);
        fullcomplaint_choose_agency_button = findViewById(R.id.fullcomplaint_choose_agency_button);

        //todo image left to add in the image view.

        fullcomplaint_name_text_view.setText(UserComplaintToShow.toShow.complainerName);
        fullcomplaint_number_text_view.setText(UserComplaintToShow.toShow.mobileNumber);
        fullcomplaint_comment_text_view.setText(UserComplaintToShow.toShow.comment);

        fullcomplaint_choose_agency_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GovtSeeFullComplaint.this, GovtSelectAgencyActivity.class));
            }
        });

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

        // Add a marker in Sydney and move the camera

        LatLng markedLocatoin = new LatLng(UserComplaintToShow.toShow.lat, UserComplaintToShow.toShow.lng);
        mMap.addMarker(new MarkerOptions().position(markedLocatoin).title("Pothole"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markedLocatoin, 18f));
    }
}
