package com.konel.kryptapps.rapidodemo.base;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.konel.kryptapps.rapidodemo.utils.LocationUtil;

/**
 * @author : Adnaan 'Zohran' Ahmed <adnaanahmed@urbanclap.com>
 * @version : 1.0.0
 * @since : 11 Aug 2017 12:34 AM
 */


public abstract class RapidoLocationActivity extends RapidoPermissionActivity {

    public static final int REQUEST_CODE_FOR_LOCATION = 0x2;

    private LocationUtil locationUtil;
    private Location currentLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationUtil = new LocationUtil(this);
    }

    @Override
    public void onPermissionGranted() {
        if (locationUtil != null)
            locationUtil.startLocationDetection();
    }

    protected void fetchLocation() {
        if (locationUtil != null)
            locationUtil.startLocationDetection();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_FOR_LOCATION) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    locationUtil.startLocationDetection();
                    break;
                case Activity.RESULT_CANCELED:
                    if (locationUtil != null)
                        locationUtil.publishLocation();
                    break;
            }
        }
    }

    public void locationError() {
        locationError("Couldn't fetch the location");
    }

    public void locationError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void locationChanged(Location location) {
        currentLocation = location;
    }
}
