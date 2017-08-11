package com.konel.kryptapps.rapidodemo.model.navigationModels;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

/**
 * @author : Adnaan 'Zohran' Ahmed <adnaanahmed@urbanclap.com>
 * @version : 1.0.0
 * @since : 11 Aug 2017 6:56 PM
 */


public class RouteActivityModel implements Parcelable {

    public static final Parcelable.Creator<RouteActivityModel> CREATOR = new Parcelable.Creator<RouteActivityModel>() {
        @Override
        public RouteActivityModel createFromParcel(Parcel source) {
            return new RouteActivityModel(source);
        }

        @Override
        public RouteActivityModel[] newArray(int size) {
            return new RouteActivityModel[size];
        }
    };
    private LatLng sourceLatLng;
    private LatLng destinationLatLng;
    private LatLng currentLatLng;

    public RouteActivityModel(@NonNull LatLng sourceLatLng, @NonNull LatLng destinationLatLng, @Nullable LatLng currentLatLng) {
        this.sourceLatLng = sourceLatLng;
        this.destinationLatLng = destinationLatLng;
        this.currentLatLng = currentLatLng;
    }

    private RouteActivityModel(Parcel in) {
        this.sourceLatLng = in.readParcelable(LatLng.class.getClassLoader());
        this.destinationLatLng = in.readParcelable(LatLng.class.getClassLoader());
        this.currentLatLng = in.readParcelable(LatLng.class.getClassLoader());
    }

    public LatLng getSourceLatLng() {
        return sourceLatLng;
    }

    public LatLng getDestinationLatLng() {
        return destinationLatLng;
    }

    public LatLng getCurrentLatLng() {
        return currentLatLng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.sourceLatLng, flags);
        dest.writeParcelable(this.destinationLatLng, flags);
        dest.writeParcelable(this.currentLatLng, flags);
    }
}
