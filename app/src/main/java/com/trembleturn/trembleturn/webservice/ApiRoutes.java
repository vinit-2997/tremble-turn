package com.trembleturn.trembleturn.webservice;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

public abstract class ApiRoutes {

    public static final String API_URL = "https://maps.googleapis.com/maps/api/directions/json?";
    public static final String API_KEY = "AIzaSyD61OtmkLZi8l_F3sVVe0R3DH5QfaZ3kbc";

    public static final int RC_A2B_STEPS = 1;

    public static String getA2BRequestUrl(LatLng source, LatLng dest) {

       return ApiRoutes.API_URL +
                "origin=" + String.valueOf(source.latitude) + "," + String.valueOf(source.longitude) + "&" +
                "destination=" + String.valueOf(dest.latitude) + "," + String.valueOf(dest.longitude) + "&" +
                "key=" + ApiRoutes.API_KEY;
    }

    abstract void makeStringGetRequest(String url);

    abstract void makeJsonPostRequest(String url, JSONObject jsonRequest);

    abstract void makeJsonPutRequest(String url, JSONObject jsonRequest);
}
