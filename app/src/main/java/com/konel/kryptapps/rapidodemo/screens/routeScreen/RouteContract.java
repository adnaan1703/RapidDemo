package com.konel.kryptapps.rapidodemo.screens.routeScreen;

import android.util.Pair;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.konel.kryptapps.rapidodemo.base.BasePresenterImpl;
import com.konel.kryptapps.rapidodemo.base.BaseViewImpl;
import com.konel.kryptapps.rapidodemo.model.directionsAPI.Step;

import java.util.ArrayList;

/**
 * @author : Adnaan 'Zohran' Ahmed <adnaanahmed@urbanclap.com>
 * @version : 1.0.0
 * @since : 11 Aug 2017 5:02 PM
 */


public class RouteContract {


    public interface PresenterImpl extends BasePresenterImpl {
        void onMapReady();

        void onRouteSelected(Polyline polyline);

    }

    interface ViewImpl extends BaseViewImpl {

        void showPolylines(ArrayList<PolylineOptions> polylineList);

        void addMarkers(ArrayList<Pair<String, LatLng>> markers);

        void setMapBounds(LatLngBounds bounds);

        void showRoutesBottomSheet(ArrayList<Step> steps);
    }

}
