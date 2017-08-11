package com.konel.kryptapps.rapidodemo.screens.queryScreen;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.konel.kryptapps.rapidodemo.R;
import com.konel.kryptapps.rapidodemo.base.RapidoBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QueryActivity extends RapidoBaseActivity implements QueryContract.ViewImpl {

    @BindView(R.id.tvSourceName)
    TextView tvSourceName;

    @BindView(R.id.tvSourceAddress)
    TextView tvSourceAddress;

    @BindView(R.id.tvDestinationName)
    TextView tvDestinationName;

    @BindView(R.id.tvDestinationAddress)
    TextView tvDestinationAddress;

    @BindView(R.id.tvCurrentLocationLabel)
    TextView tvCurrentLocationLabel;

    private QueryPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        ButterKnife.bind(this);
        tvCurrentLocationLabel.setVisibility(View.GONE);
        presenter = new QueryPresenter(this);
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
    public void showSourceLocation(@NonNull String name, @NonNull String address) {
        tvSourceName.setText(name);
        tvSourceAddress.setText(address);
    }

    @Override
    public void showDestinationLocation(@NonNull String name, @NonNull String address) {
        tvDestinationName.setText(name);
        tvDestinationAddress.setText(address);
    }

    @OnClick(R.id.btnSourceDetection)
    public void onSourceDetectionClicked() {
        presenter.getSourceLocation(this);
    }

    @OnClick(R.id.btnDestinationDetection)
    public void onDestinationDetectionClicked() {
        presenter.getDestinationLocation(this);
    }

    @OnClick(R.id.query_screen_cta)
    public void onCTAClicked() {
        presenter.fetchRoutes();
    }

    @OnClick(R.id.btnDetectLocation)
    public void onDetectionButtonClicked() {
        fetchLocation();
    }

    @Override
    public void locationChanged(Location location) {
        super.locationChanged(location);
        tvCurrentLocationLabel.setVisibility(View.VISIBLE);
        presenter.onCurrentLocationDetected(new LatLng(location.getLatitude(), location.getLongitude()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Place place = null;
        if (resultCode == RESULT_OK) {
            place = PlaceAutocomplete.getPlace(this, data);
        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            showToast(PlaceAutocomplete.getStatus(this, data).getStatusMessage());
            return;
        } else if (requestCode == RESULT_CANCELED) {
            showToast("interaction cancelled");
            return;
        }

        if (place != null) {
            if (requestCode == QueryPresenter.REQUEST_CODE_SOURCE)
                presenter.onSourceLocationReceived(place);
            else if (requestCode == QueryPresenter.REQUEST_CODE_DESTINATION)
                presenter.onDestinationLocationReceived(place);
        }
    }
}
