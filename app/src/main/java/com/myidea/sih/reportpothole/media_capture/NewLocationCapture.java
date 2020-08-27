package com.myidea.sih.reportpothole.media_capture;

// 1. Before using it change the package name
// 2. add this dependency
// // Google Location
//    implementation 'com.google.android.gms:play-services-location:17.0.0'

import android.content.Context;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.myidea.sih.reportpothole.MarkPotholeActivity;

public class NewLocationCapture {

    private static double latitude;
    private static double longitude;

    private static FusedLocationProviderClient fusedLocationProviderClient;
    private static LocationRequest locationRequest;
    private static LocationCallback locationCallback;

    NewLocationCapture() {}

    public static void setLatitude(double latitude) {
        NewLocationCapture.latitude = latitude;
    }

    public static void setLongitude(double longitude) {
        NewLocationCapture.longitude = longitude;
    }

    public static double getLatitude() {
        return latitude;
    }

    public static double getLongitude() {
        return longitude;
    }

    // Building location request.
    private static void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(0);
        locationRequest.setFastestInterval(0);
        locationRequest.setSmallestDisplacement(0);
    }

    // Building the LocationCallback.
    private static void buildLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location: locationResult.getLocations()) {
                    MarkPotholeActivity.updateLocation(location.getLatitude(), location.getLongitude());
                    Log.d("()() LOCATION", " " + location.getLatitude() + "||" + location.getLongitude());
                }
            }
        };
    }

    // If we have to get location then this method is must to be called.
    public static void mandatoryForGettingLocation(Context context) {
        buildLocationRequest();
        buildLocationCallback();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        startLocationService();
    }

    public static void startLocationService() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    public static void stopLocationService() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

}
