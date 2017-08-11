package com.konel.kryptapps.rapidodemo.utils;

import android.Manifest;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.konel.kryptapps.rapidodemo.base.RapidoLocationActivity;


/**
 * @author : Adnaan 'Zohran' Ahmed <adnaanahmed@urbanclap.com>
 * @version : 1.0.0
 * @since : 10 Aug 2017 9:58 PM
 */


public class LocationUtil implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int DEFAULT_INTERVAL = 10000;

    private RapidoLocationActivity activity;
    private GoogleApiClient locationClient;
    private Boolean mRequestingLocationUpdates;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mLastKnownLocation = null;
    private Location mCurrentLocation = null;
    private LocationPublishHandler publishHandler;

    public LocationUtil(String name, RapidoLocationActivity activity) {
        this.activity = activity;
        init();
    }

    private void init() {
        mRequestingLocationUpdates = false;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        mSettingsClient = LocationServices.getSettingsClient(activity);
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
    }

    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mCurrentLocation = locationResult.getLastLocation();
                publishLocation();
            }
        };
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(DEFAULT_INTERVAL);
        mLocationRequest.setFastestInterval(DEFAULT_INTERVAL >> 1);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    public void startLocationDetection() {
        publishHandler = new LocationPublishHandler(activity);
        initLocationClient();
        connect();
    }

    private void initLocationClient() {
        locationClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void connect() {
        if (null != locationClient && (!locationClient.isConnected() || !locationClient.isConnecting())) {
            locationClient.connect();
        }
    }

    private void disconnect() {
        if (null != locationClient &&
                (locationClient.isConnected() || locationClient.isConnecting())) {
            locationClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (activity.hasAllPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)) {
            //noinspection MissingPermission
            mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(locationClient);
            publishLocation(false);
            startLocationUpdates();
        } else {
            activity.askForAllPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (!connectionResult.hasResolution()) {
            activity.locationError(connectionResult.getErrorMessage());
        } else {
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(activity, new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

                        mRequestingLocationUpdates = true;
                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());
                    }
                })
                .addOnFailureListener(activity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(activity, RapidoLocationActivity.REQUEST_CODE_FOR_LOCATION);
                                } catch (IntentSender.SendIntentException sie) {
                                    publishLocation();
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show();
                                mRequestingLocationUpdates = false;
                                publishLocation();
                        }
                    }
                });
    }

    public void publishLocation(boolean kill) {
        Location sendingLocation;

        if (mCurrentLocation != null)
            sendingLocation = mCurrentLocation;
        else if (mLastKnownLocation != null)
            sendingLocation = mLastKnownLocation;
        else sendingLocation = null;

        if (sendingLocation != null) {
            publishHandler.publishLocationChange(sendingLocation);
        } else {
            publishHandler.publishLocationError();
        }
        if (kill)
            stopLocationUpdates();
    }

    public void publishLocation() {
        publishLocation(true);
    }

    public void stopLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            return;
        }
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mRequestingLocationUpdates = false;
                    }
                });
        disconnect();
        locationClient = null;
        publishHandler = null;
    }

    private static class LocationPublishHandler extends Handler {
        private static final int LOCATION_CHANGED = 0;
        private static final int LOCATION_ERROR = 1;
        private final RapidoLocationActivity target;

        LocationPublishHandler(RapidoLocationActivity target) {
            super(Looper.getMainLooper());
            this.target = target;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOCATION_ERROR: {
                    target.locationError();
                    break;
                }
                default:
                case LOCATION_CHANGED: {
                    if (msg.obj instanceof Location) {
                        Location location = (Location) msg.obj;
                        target.locationChanged(location);
                    }
                    break;
                }
            }
        }

        void publishLocationChange(Location location) {
            sendMessage(obtainMessage(LOCATION_CHANGED, location));
        }

        void publishLocationError() {
            sendEmptyMessage(LOCATION_ERROR);
        }
    }
}
