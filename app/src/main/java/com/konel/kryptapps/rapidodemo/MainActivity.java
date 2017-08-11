package com.konel.kryptapps.rapidodemo;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.konel.kryptapps.rapidodemo.base.RapidoBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends RapidoBaseActivity {

    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE_SOURCE = 0x1;
    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE_DESTINATION = 0x2;

    @BindView(R.id.tvSource)
    TextView tvSourceAddress;
    @BindView(R.id.tvDestination)
    TextView tvDestinationAddress;
    @BindView(R.id.tvCurrent)
    TextView tvCurrentAddress;

    private Place sourcePlace = null;
    private Place destinationPlace = null;
    private Location currentLocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnSource)
    public void fetchSource() {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_SOURCE);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    @OnClick(R.id.btnDestination)
    public void fetchDestination() {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_DESTINATION);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    @OnClick(R.id.btnCurrent)
    public void detectLocation() {
        fetchLocation();
    }

    @Override
    public void locationChanged(Location location) {
        super.locationChanged(location);
        tvCurrentAddress.setText(location.getProvider());
        currentLocation = location;
    }

    @OnClick(R.id.btnCTA)
    public void openNextPage() {
        if (sourcePlace != null && destinationPlace != null && currentLocation != null) {
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("SOURCE_LATITUDE", sourcePlace.getLatLng().latitude);
            intent.putExtra("SOURCE_LONGITUDE", sourcePlace.getLatLng().longitude);
            intent.putExtra("DESTINATION_LATITUDE", destinationPlace.getLatLng().latitude);
            intent.putExtra("DESTINATION_LONGITUDE", destinationPlace.getLatLng().longitude);
            intent.putExtra("CURRENT_LATITUDE", currentLocation.getLatitude());
            intent.putExtra("CURRENT_LONGITUDE", currentLocation.getLongitude());

            startActivity(intent);
        } else {
            Toast.makeText(this, "places not selected..", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_SOURCE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                sourcePlace = place;
                tvSourceAddress.setText(place.getAddress());
                Log.i(this.getClass().getSimpleName(), "\nPlace: " + place.getName()
                        + "\nplaceId : " + place.getId()
                        + "\naddress : " + place.getAddress()
                        + "\nLatLng : " + place.getLatLng().toString());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.i(this.getClass().getSimpleName(), status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                Log.d(this.getClass().getSimpleName(), "user cancelled");
            }
        } else if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_DESTINATION) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                destinationPlace = place;
                tvDestinationAddress.setText(place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.i(this.getClass().getSimpleName(), status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                Log.d(this.getClass().getSimpleName(), "user cancelled");
            }
        }
    }
}
