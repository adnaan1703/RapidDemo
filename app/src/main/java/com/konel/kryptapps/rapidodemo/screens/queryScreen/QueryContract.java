package com.konel.kryptapps.rapidodemo.screens.queryScreen;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.konel.kryptapps.rapidodemo.base.BasePresenterImpl;
import com.konel.kryptapps.rapidodemo.base.BaseViewImpl;

/**
 * @author : Adnaan 'Zohran' Ahmed <adnaanahmed@urbanclap.com>
 * @version : 1.0.0
 * @since : 11 Aug 2017 1:00 PM
 */


interface QueryContract {

    interface PresenterImpl extends BasePresenterImpl {
        void getSourceLocation(@NonNull AppCompatActivity activity);

        void onSourceLocationReceived(@NonNull Place place);

        void getDestinationLocation(@NonNull AppCompatActivity activity);

        void onDestinationLocationReceived(@NonNull Place place);

        void onCurrentLocationDetected(@NonNull LatLng currentLatLng);

        void fetchRoutes();
    }

    interface ViewImpl extends BaseViewImpl {
        void showSourceLocation(@NonNull String name, @NonNull String address);

        void showDestinationLocation(@NonNull String name, @NonNull String address);
    }
}
