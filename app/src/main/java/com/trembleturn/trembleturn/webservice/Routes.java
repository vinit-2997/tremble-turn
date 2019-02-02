package com.trembleturn.trembleturn.webservice;

import org.json.JSONObject;

public interface Routes {

    String API_URL = "https://maps.googleapis.com/maps/api/directions/json?";
    String API_KEY = "AIzaSyD61OtmkLZi8l_F3sVVe0R3DH5QfaZ3kbc";

    void makeStringGetRequest(String url);

    void makeJsonPostRequest(String url, JSONObject jsonRequest);

    void makeJsonPutRequest(String url, JSONObject jsonRequest);
}
