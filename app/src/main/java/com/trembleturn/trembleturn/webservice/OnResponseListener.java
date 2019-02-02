package com.trembleturn.trembleturn.webservice;


import org.json.JSONObject;

/**
 * Created by GladiatoR on 25/11/15.
 */
public interface OnResponseListener {
    void onSuccess(int requestCode, JSONObject response);

    void onError(int requestCode, ErrorType errorType, JSONObject response);

}
