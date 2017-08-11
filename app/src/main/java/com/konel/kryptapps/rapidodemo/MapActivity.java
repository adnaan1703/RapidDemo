package com.konel.kryptapps.rapidodemo;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.konel.kryptapps.rapidodemo.base.RapidoBaseActivity;
import com.konel.kryptapps.rapidodemo.model.directionsAPI.DirectionResponse;
import com.konel.kryptapps.rapidodemo.network.DirectionApiClient;
import com.konel.kryptapps.rapidodemo.network.GoogleServiceGenerator;
import com.konel.kryptapps.rapidodemo.utils.AppConstantsUtil;
import com.konel.kryptapps.rapidodemo.utils.CodeUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends RapidoBaseActivity implements OnMapReadyCallback {

    private SupportMapFragment mapFragment;
    private LatLng sourceLatLng;
    private LatLng destinationLatLng;
    private LatLng currentLatLng;
    private Call<DirectionResponse> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        handleIntent();
        mapFragment.getMapAsync(this);
    }

    private void handleIntent() {
        Intent intent = getIntent();
        double sourceLatitude = intent.getDoubleExtra("SOURCE_LATITUDE", 0.0);
        double sourceLongitude = intent.getDoubleExtra("SOURCE_LONGITUDE", 0.0);
        double destinationLatitude = intent.getDoubleExtra("DESTINATION_LATITUDE", 0.0);
        double destinationLongitude = intent.getDoubleExtra("DESTINATION_LONGITUDE", 0.0);
        double currentLatitude = intent.getDoubleExtra("CURRENT_LATITUDE", 0.0);
        double currentLongitude = intent.getDoubleExtra("CURRENT_LONGITUDE", 0.0);

        sourceLatLng = new LatLng(sourceLatitude, sourceLongitude);
        destinationLatLng = new LatLng(destinationLatitude, destinationLongitude);
        currentLatLng = new LatLng(currentLatitude, currentLongitude);

        DirectionApiClient client = GoogleServiceGenerator.createService(DirectionApiClient.class);
        call = client.getDirections(
                CodeUtil.LatLngToString(sourceLatLng),
                CodeUtil.LatLngToString(destinationLatLng),
                CodeUtil.LatLngToString(currentLatLng),
                true,
                AppConstantsUtil.GOOGLE_DIRECTIONS_API_KEY
        );
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
//        LatLng home = new LatLng(28.4773518, 77.0727241);
//        googleMap.addMarker(new MarkerOptions().position(home)
//                .title("Marker in Home"));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, 15));

//        Polyline polyline = googleMap.addPolyline(new PolylineOptions()
//                .addAll(PolyUtil.decode(getString(R.string.polyline))));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(new LatLng(28.476775, 77.0559064),
//                new LatLng(28.4988619, 77.0772925)), 200));

        call.enqueue(new Callback<DirectionResponse>() {
            @Override
            public void onResponse(Call<DirectionResponse> call, Response<DirectionResponse> response) {

                googleMap.addMarker(new MarkerOptions().position(sourceLatLng).title("Source"));
                googleMap.addMarker(new MarkerOptions().position(destinationLatLng).title("Destination"));
                googleMap.addMarker(new MarkerOptions().position(currentLatLng).title("Your Location"));

                googleMap.addPolyline(new PolylineOptions()
                        .addAll(PolyUtil.decode(response.body().routes.get(0).overviewPolyline.points)));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(
                        new LatLngBounds(
                                new LatLng(response.body().routes.get(0).bounds.southwest.lat, response.body().routes.get(0).bounds.southwest.lng),
                                new LatLng(response.body().routes.get(0).bounds.northeast.lat, response.body().routes.get(0).bounds.northeast.lng)
                        ), CodeUtil.dpToPx(MapActivity.this, 32)
                ));
            }

            @Override
            public void onFailure(Call<DirectionResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
}
