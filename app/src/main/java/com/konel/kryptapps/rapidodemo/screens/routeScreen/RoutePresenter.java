package com.konel.kryptapps.rapidodemo.screens.routeScreen;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Pair;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.konel.kryptapps.rapidodemo.R;
import com.konel.kryptapps.rapidodemo.model.directionsAPI.Bounds;
import com.konel.kryptapps.rapidodemo.model.directionsAPI.DirectionResponse;
import com.konel.kryptapps.rapidodemo.model.directionsAPI.Northeast;
import com.konel.kryptapps.rapidodemo.model.directionsAPI.Route;
import com.konel.kryptapps.rapidodemo.model.directionsAPI.Southwest;
import com.konel.kryptapps.rapidodemo.network.Repository;
import com.konel.kryptapps.rapidodemo.utils.CodeUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author : Adnaan 'Zohran' Ahmed <adnaanahmed@urbanclap.com>
 * @version : 1.0.0
 * @since : 11 Aug 2017 5:44 PM
 */


class RoutePresenter implements RouteContract.PresenterImpl {


    private RouteContract.ViewImpl view;
    private LatLng sourceLatLng;
    private LatLng destinationLatLng;
    private LatLng currentLatLng;
    private Callback<DirectionResponse> callBack;
    private DirectionResponse response = null;

    public RoutePresenter(@NonNull RouteContract.ViewImpl view,
                          @NonNull LatLng sourceLatLng,
                          @NonNull LatLng destinationLatLng,
                          @Nullable LatLng currentLatLng) {
        this.view = view;
        this.sourceLatLng = sourceLatLng;
        this.destinationLatLng = destinationLatLng;
        this.currentLatLng = currentLatLng;
        initCallback();
    }

    private void initCallback() {
        callBack = new Callback<DirectionResponse>() {
            @Override
            public void onResponse(@NonNull Call<DirectionResponse> call, @NonNull Response<DirectionResponse> response) {
                if (response.isSuccessful() && response.body() != null)
                    onPostRequest(response.body());
                else
                    view.showError("Problems during network call");
            }

            @Override
            public void onFailure(@NonNull Call<DirectionResponse> call, @NonNull Throwable t) {
                view.showError(t.getMessage());
            }
        };
    }

    private void onPostRequest(DirectionResponse response) {
        this.response = response;

        sendMarkers();

        List<Route> routes = response.routes;
        if (CodeUtil.isEmptyOrNull(routes)) {
            view.showError("No routes found");
            return;
        }

        sendBounds(routes.get(0).bounds);
        sendPolylines(routes);


    }

    private void sendPolylines(List<Route> routes) {
        ArrayList<PolylineOptions> polylineOptionsArrayList = new ArrayList<>();
        for (Route route : routes) {
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.clickable(true);
            polylineOptions.color(ContextCompat.getColor(view.getViewContext(), R.color.colorPrimaryLight));
            polylineOptions.width(CodeUtil.dpToPx(view.getViewContext(), 2));
            polylineOptions.addAll(PolyUtil.decode(route.overviewPolyline.points));
            if (polylineOptionsArrayList.size() == 0)
                polylineOptions.color(ContextCompat.getColor(view.getViewContext(), R.color.colorPrimary));

            polylineOptionsArrayList.add(polylineOptions);
        }
        view.showPolylines(polylineOptionsArrayList);
    }

    private void sendBounds(Bounds bounds) {

        Southwest southwest = bounds.southwest;
        Northeast northeast = bounds.northeast;

        LatLng southWest = new LatLng(southwest.lat, southwest.lng);
        LatLng northEast = new LatLng(northeast.lat, northeast.lng);

        LatLngBounds latLngBounds = new LatLngBounds(southWest, northEast);
        view.setMapBounds(latLngBounds);
    }

    private void sendMarkers() {
        ArrayList<Pair<String, LatLng>> markers = new ArrayList<>();
        markers.add(Pair.create("Source", sourceLatLng));
        markers.add(Pair.create("Destination", destinationLatLng));
        if (currentLatLng != null)
            markers.add(Pair.create("Current Location", currentLatLng));
        view.addMarkers(markers);
    }


    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void onMapReady() {
        //noinspection ConstantConditions
        Repository.getDirections(
                CodeUtil.LatLngToString(sourceLatLng),
                CodeUtil.LatLngToString(destinationLatLng),
                CodeUtil.LatLngToString(currentLatLng),
                callBack
        );
    }

    @Override
    public void onRouteSelected(Polyline polyline) {

    }
}
