package com.trembleturn.trembleturn.webservice;

import org.json.JSONObject;

public interface Routes {

    String API_URL = "https://maps.googleapis.com/maps/api/directions/json?";

    void makeStringGetRequest(String url, boolean showDialog);

    void makeJsonPostRequest(String url, JSONObject jsonRequest, boolean showDialog);

    void makeJsonPutRequest(String url, JSONObject jsonRequest, boolean showDialog);
}
