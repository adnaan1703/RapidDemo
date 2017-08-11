package com.konel.kryptapps.rapidodemo.network;

import com.konel.kryptapps.rapidodemo.model.directionsAPI.DirectionResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author : Adnaan 'Zohran' Ahmed <adnaanahmed@urbanclap.com>
 * @version : 1.0.0
 * @since : 10 Aug 2017 5:57 PM
 */


interface DirectionApiClient {
    @GET("/maps/api/directions/json")
    Call<DirectionResponse> getDirections(
            @Query("origin") String origin,
            @Query("destination") String destination,
            @Query("waypoints") String wayPoint,
            @Query("alternatives") boolean alternatives,
            @Query("key") String apiKey
    );
}
