package com.konel.kryptapps.rapidodemo.screens.routeScreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.konel.kryptapps.rapidodemo.R;
import com.konel.kryptapps.rapidodemo.base.RapidoBaseActivity;
import com.konel.kryptapps.rapidodemo.model.navigationModels.RouteActivityModel;
import com.konel.kryptapps.rapidodemo.utils.CodeUtil;
import com.konel.kryptapps.rapidodemo.utils.NavigationUtil;

import java.util.ArrayList;

public class RouteActivity extends RapidoBaseActivity implements RouteContract.ViewImpl, OnMapReadyCallback {

    private RoutePresenter presenter;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        try {
            handleIntent();
        } catch (Exception e) {
            finish();
            e.printStackTrace();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.routeMap);
        mapFragment.getMapAsync(this);
    }

    private void handleIntent() throws Exception {
        Intent intent = getIntent();
        if (!intent.hasExtra(NavigationUtil.NAVIGATION_DATA_KEY))
            throw new Exception("Screen should be started with its model");

        RouteActivityModel model = intent.getParcelableExtra(NavigationUtil.NAVIGATION_DATA_KEY);
        presenter = new RoutePresenter(this, model.getSourceLatLng(), model.getDestinationLatLng(), model.getCurrentLatLng());
    }

    @Override
    public Context getViewContext() {
        return getBaseContext();
    }

    @Override
    public void showError(String message) {
        showToast(message);
    }

    @Override
    public void showPolylines(ArrayList<PolylineOptions> polylineList) {
        for (PolylineOptions polylineOptions : polylineList) {
            googleMap.addPolyline(polylineOptions);
        }
    }

    @Override
    public void addMarkers(ArrayList<Pair<String, LatLng>> markers) {
        for (Pair<String, LatLng> marker : markers) {
            googleMap.addMarker(new MarkerOptions().position(marker.second).title(marker.first));
        }
    }

    @Override
    public void setMapBounds(LatLngBounds bounds) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, CodeUtil.dpToPx(this, 48)));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        presenter.onMapReady();
    }
}
