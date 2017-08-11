package com.konel.kryptapps.rapidodemo.screens.queryScreen;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.konel.kryptapps.rapidodemo.utils.NavigationUtil;

/**
 * @author : Adnaan 'Zohran' Ahmed <adnaanahmed@urbanclap.com>
 * @version : 1.0.0
 * @since : 11 Aug 2017 2:52 PM
 */


class QueryPresenter implements QueryContract.PresenterImpl {

    static final int REQUEST_CODE_SOURCE = 0x0;
    static final int REQUEST_CODE_DESTINATION = 0x1;

    private QueryContract.ViewImpl view;
    private Place sourcePlace;
    private Place destinationPlace;
    private LatLng currentLatLng;

    QueryPresenter(@NonNull QueryContract.ViewImpl view) {
        this.view = view;
    }

    @Override
    public void start() {
        // nothing to do here...
    }

    @Override
    public void stop() {
        // nothing to do here...
    }

    @Override
    public void getSourceLocation(@NonNull AppCompatActivity activity) {
        NavigationUtil.startAutoCompleteLocationActivityForResult(activity, REQUEST_CODE_SOURCE);
    }

    @Override
    public void onSourceLocationReceived(@NonNull Place place) {
        this.sourcePlace = place;
        view.showSourceLocation(place.getName().toString(), place.getAddress().toString());
    }

    @Override
    public void getDestinationLocation(@NonNull AppCompatActivity activity) {
        NavigationUtil.startAutoCompleteLocationActivityForResult(activity, REQUEST_CODE_DESTINATION);
    }

    @Override
    public void onDestinationLocationReceived(@NonNull Place place) {
        this.destinationPlace = place;
        view.showDestinationLocation(place.getName().toString(), place.getAddress().toString());
    }

    @Override
    public void onCurrentLocationDetected(@NonNull LatLng currentLatLng) {
        this.currentLatLng = currentLatLng;
    }

    @Override
    public void fetchRoutes() {
        if (sourcePlace != null && destinationPlace != null) {
            if (currentLatLng == null)
                view.showError("Proceeding without current Location");
            NavigationUtil.startRouteActivity(view.getViewContext(),
                    sourcePlace.getLatLng(), destinationPlace.getLatLng(), currentLatLng);
        } else
            view.showError("Incomplete Information");
    }
}
