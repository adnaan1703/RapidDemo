package com.konel.kryptapps.rapidodemo.screens.routeScreen;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.konel.kryptapps.rapidodemo.R;
import com.konel.kryptapps.rapidodemo.base.RapidoBaseActivity;
import com.konel.kryptapps.rapidodemo.model.directionsAPI.Step;
import com.konel.kryptapps.rapidodemo.model.navigationModels.RouteActivityModel;
import com.konel.kryptapps.rapidodemo.screens.routeScreen.bottomSheet.BottomSheetRouteAdapter;
import com.konel.kryptapps.rapidodemo.utils.CodeUtil;
import com.konel.kryptapps.rapidodemo.utils.NavigationUtil;

import java.util.ArrayList;

public class RouteActivity extends RapidoBaseActivity implements RouteContract.ViewImpl, OnMapReadyCallback, GoogleMap.OnPolylineClickListener {

    private RoutePresenter presenter;
    private GoogleMap googleMap;

    private RecyclerView routeDirectionList;
    private BottomSheetBehavior bottomSheetBehavior;
    private Polyline previousSelectedPolyline = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        init();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.routeMap);
        mapFragment.getMapAsync(this);
    }

    private void init() {
        try {
            handleIntent();
        } catch (Exception e) {
            finish();
            e.printStackTrace();
        }

        if (!hasAllPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION))
            askForAllPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);

        FrameLayout bottomSheet = (FrameLayout) findViewById(R.id.bottomSheetRouteDirectionContainer);
        routeDirectionList = (RecyclerView) findViewById(R.id.rvBottomSheetRouteDirection);

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        routeDirectionList.setLayoutManager(new LinearLayoutManager(this));
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
        int counter = 0;
        for (PolylineOptions polylineOptions : polylineList) {
            Polyline polyline = googleMap.addPolyline(polylineOptions);
            if (counter == 0)
                previousSelectedPolyline = polyline;
            polyline.setTag(counter++);
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
    public void showRoutesBottomSheet(ArrayList<Step> steps) {
        BottomSheetRouteAdapter adapter = new BottomSheetRouteAdapter(steps);
        routeDirectionList.setAdapter(adapter);
        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED)
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setOnPolylineClickListener(this);
        if (hasAllPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            fetchLocation();
            //noinspection MissingPermission
            this.googleMap.setMyLocationEnabled(true);
        }
        presenter.onMapReady();
    }

    @Override
    public void onPolylineClick(Polyline polyline) {
        if (previousSelectedPolyline != null)
            previousSelectedPolyline.setColor(ContextCompat.getColor(this, R.color.colorPrimaryLight));
        previousSelectedPolyline = polyline;
        polyline.setColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        presenter.onRouteSelected(polyline);
    }
}
