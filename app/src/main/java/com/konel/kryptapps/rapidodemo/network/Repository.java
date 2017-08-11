package com.konel.kryptapps.rapidodemo.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.konel.kryptapps.rapidodemo.model.directionsAPI.DirectionResponse;
import com.konel.kryptapps.rapidodemo.utils.AppConstantsUtil;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * @author : Adnaan 'Zohran' Ahmed <adnaanahmed@urbanclap.com>
 * @version : 1.0.0
 * @since : 10 Aug 2017 6:35 PM
 */


public class Repository {

    public static void getDirections(@NonNull String origin,
                                     @NonNull String destination,
                                     @Nullable String wayPoint,
                                     @NonNull Callback<DirectionResponse> callback) {

        DirectionApiClient client = GoogleServiceGenerator.createService(DirectionApiClient.class);
        Call<DirectionResponse> call = client.getDirections(
                origin,
                destination,
                wayPoint,
                true,
                AppConstantsUtil.GOOGLE_DIRECTIONS_API_KEY
        );
        call.enqueue(callback);
    }

}
