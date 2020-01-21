package com.myidea.sih.reportpothole;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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
import com.myidea.sih.reportpothole.database.Fire;
import com.myidea.sih.reportpothole.user.UserHistoryData;
import com.myidea.sih.reportpothole.util.UserComplaintToShow;

public class UserSeeFullHistory extends FragmentActivity implements OnMapReadyCallback {

    private ImageView user_fullhistory_image;
    private TextView user_fullhistory_name_text_view;
    private TextView user_fullhistory_number_text_view;
    private TextView user_fullhistory_comment_text_view;
    private TextView user_fullhistory_status_text_view;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_see_full_history);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setTitle("My History");

        user_fullhistory_image = findViewById(R.id.user_fullhistory_image);
        user_fullhistory_name_text_view = findViewById(R.id.user_fullhistory_name_text_view);
        user_fullhistory_number_text_view = findViewById(R.id.user_fullhistory_number_text_view);
        user_fullhistory_comment_text_view = findViewById(R.id.user_fullhistory_comment_text_view);
        user_fullhistory_status_text_view = findViewById(R.id.user_fullhistory_status_text_view);

        user_fullhistory_name_text_view.setText(UserHistoryData.selectedToSee.complainerName);
        user_fullhistory_comment_text_view.setText(UserHistoryData.selectedToSee.comment);
        user_fullhistory_number_text_view.setText(UserHistoryData.selectedToSee.mobileNumber);
        user_fullhistory_status_text_view.setText(user_fullhistory_status_text_view.getText().toString() + UserHistoryData.selectedToSee.status);

        StorageReference storageReference = Fire.mainStorageReference.child(UserHistoryData.selectedToSee.imageUniqueID);
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext())
                        .load(uri)
                        .into(user_fullhistory_image);
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
        LatLng markedLocation = new LatLng(UserHistoryData.selectedToSee.lat, UserHistoryData.selectedToSee.lng);
        mMap.addMarker(new MarkerOptions().position(markedLocation).title("Pothole"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markedLocation, 18f));
    }
}
