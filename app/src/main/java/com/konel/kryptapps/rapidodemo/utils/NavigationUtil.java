package com.konel.kryptapps.rapidodemo.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.konel.kryptapps.rapidodemo.model.navigationModels.RouteActivityModel;
import com.konel.kryptapps.rapidodemo.screens.routeScreen.RouteActivity;

/**
 * @author : Adnaan 'Zohran' Ahmed <adnaanahmed@urbanclap.com>
 * @version : 1.0.0
 * @since : 11 Aug 2017 2:52 PM
 */


public class NavigationUtil {

    public static final String NAVIGATION_DATA_KEY = "com.konel.kryptapps.rapidodemo.utils.NavigationUtil.data_key";

    public static void startAutoCompleteLocationActivityForResult(@NonNull AppCompatActivity activity, int requestCode) {
        try {
            Intent intent =
                    new PlaceAutocomplete
                            .IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(activity);
            activity.startActivityForResult(intent, requestCode);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            Log.d(NavigationUtil.class.getSimpleName(), e.getMessage());
        }
    }

    public static void startRouteActivity(@NonNull Context context,
                                          @NonNull LatLng source,
                                          @NonNull LatLng destination,
                                          @Nullable LatLng current) {

        Intent intent = new Intent(context, RouteActivity.class);
        RouteActivityModel model = new RouteActivityModel(source, destination, current);
        intent.putExtra(NAVIGATION_DATA_KEY, model);
        context.startActivity(intent);
    }
}
