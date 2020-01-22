package com.myidea.sih.reportpothole;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.myidea.sih.reportpothole.complaint.UserComplaint;
import com.myidea.sih.reportpothole.data_helper.DataFramework;
import com.myidea.sih.reportpothole.database.Fire;
import com.myidea.sih.reportpothole.status.StatusUpdater;
import com.myidea.sih.reportpothole.util.AgencyName;
import com.myidea.sih.reportpothole.util.UserComplaintToShow;

public class CivilSeeFullComplaint extends FragmentActivity implements OnMapReadyCallback {

    private ImageView civil_fullcomplaint_image_view;
    private TextView civil_fullcomplaint_name_text_view;
    private TextView civil_fullcomplaint_number_text_view;
    private TextView civil_fullcomplaint_comment_text_view;
    private Button civil_fullcomplaint_mark_complete;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_civil_see_full_complaint);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setTitle("Full Complaint");

        if (UserComplaintToShow.toShow.status.equals("PROBLEM FORWARDED TO CIVIL AGENCY"))
            StatusUpdater.updateStatus("VERIFIED");

        civil_fullcomplaint_name_text_view = findViewById(R.id.civil_fullcomplaint_name_text_view);
        civil_fullcomplaint_number_text_view = findViewById(R.id.civil_fullcomplaint_number_text_view);
        civil_fullcomplaint_comment_text_view = findViewById(R.id.civil_fullcomplaint_comment_text_view);
        civil_fullcomplaint_image_view = findViewById(R.id.civil_fullcomplaint_image_view);
        civil_fullcomplaint_mark_complete = findViewById(R.id.civil_fullcomplaint_mark_complete);

        civil_fullcomplaint_name_text_view.setText(UserComplaintToShow.toShow.complainerName);
        civil_fullcomplaint_number_text_view.setText(UserComplaintToShow.toShow.mobileNumber);
        civil_fullcomplaint_comment_text_view.setText(UserComplaintToShow.toShow.comment);

        StorageReference storageReference = Fire.mainStorageReference.child(UserComplaintToShow.toShow.imageUniqueID);
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext())
                        .load(uri)
                        .into(civil_fullcomplaint_image_view);
            }
        });

        if (UserComplaintToShow.toShow.status.equals("PROBLEM SOLVED")) {
            civil_fullcomplaint_mark_complete.setBackgroundResource(R.drawable.disabled_round);
            civil_fullcomplaint_mark_complete.setEnabled(false);
        } else {
            civil_fullcomplaint_mark_complete.setBackgroundResource(R.drawable.round_button);
            civil_fullcomplaint_mark_complete.setEnabled(true);
        }

        civil_fullcomplaint_mark_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                civil_fullcomplaint_mark_complete.setBackgroundResource(R.drawable.disabled_round);
                civil_fullcomplaint_mark_complete.setEnabled(false);
                StatusUpdater.updateStatus("PROBLEM SOLVED");
                startActivity(new Intent(CivilSeeFullComplaint.this, SuccessActivityCivil.class));
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
        LatLng potholeLocation = new LatLng(UserComplaintToShow.toShow.lat, UserComplaintToShow.toShow.lng);
        mMap.addMarker(new MarkerOptions().position(potholeLocation).title("Pothole"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(potholeLocation, 18f));
    }

    @Override
    public void onBackPressed() {
        finish();
        DataFramework.mainForCivilInside(AgencyName.agencyName, getApplicationContext());
    }
}
