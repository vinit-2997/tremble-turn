package com.trembleturn.trembleturn.webservice;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.trembleturn.trembleturn.MyApplication;
import com.trembleturn.trembleturn.helpers.Utilities;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ApiRouter extends ApiRoutes {

    public static final String TAG = ApiRouter.class.getSimpleName();
    Context context;
    OnResponseListener callBack;
    private String headerKey;
    private int requestCode;
    private String requestTag;
    private String tag_json_obj = "jobj_req";
    private int socketTimeout = 10000;

    Response.Listener successStringListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String s) {
            try {
                System.out.println("-------------------------successStringListener-------------------------------");
                System.out.println("---------" + s);
                System.out.println("----------------------------successStringListener----------------------------");

                JSONObject response = new JSONObject(s);
                callBack.onSuccess(requestCode, response);
            } catch (Exception e) {
                callBack.onSuccess(requestCode, new JSONObject());
                e.printStackTrace();
            }
        }
    };
    Response.Listener successJsonListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject s) {
            try {
                System.out.println("--------------------------successJsonListener------------------------------");
                System.out.println("---------" + s);
                System.out.println("--------------------------successJsonListener------------------------------");
                JSONObject response = s;
                callBack.onSuccess(requestCode, response);
            } catch (Exception e) {
                e.printStackTrace();
                callBack.onSuccess(requestCode, new JSONObject());
            }
        }
    };
    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(final VolleyError volleyError) {
            System.out.println("------------------------errorListener--------------------------------");
            String err = null;
            String jsonError;
            JSONObject response = new JSONObject();
            NetworkResponse networkResponse = volleyError.networkResponse;
            if (networkResponse != null && networkResponse.data != null) {
                jsonError = new String(networkResponse.data);
                try {
                    response = new JSONObject(jsonError);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "---jsonError--" + jsonError);
            }

            try {
                if (volleyError != null) {
                    if (volleyError.getCause() != null && volleyError.getCause().getMessage().equalsIgnoreCase("End of input at character 0 of ")) {
                        callBack.onSuccess(requestCode, new JSONObject());
                        return;
                    } else {
                        try {
                            if (volleyError.networkResponse.statusCode == 500) {
                                callBack.onError(requestCode, ErrorType.ERROR500, response);
                                return;
                            } else if (volleyError.networkResponse.statusCode == 400) {
                                callBack.onError(requestCode, ErrorType.ERROR400, response);
                                return;
                            } else {
                                err = new String(volleyError.networkResponse.data);
                            }
                        } catch (Exception e) {

                        }
                        if (err != null) {
                            Toast.makeText(context, err, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            callBack.onError(requestCode, ErrorType.ERROR, response);
            System.out.println("------------------------errorListener--------------------------------");
        }
    };


    public ApiRouter(Context context, OnResponseListener callBack, int requestCode, String requestTag) {
        this.context = context;
        this.callBack = callBack;
        this.requestCode = requestCode;
//        headerKey = PrefSetup.getInstance().getHeaderKey();
        this.requestTag = requestTag;
    }

    private void showNoInternetConnection() {
        Toast.makeText(context, "No internet access", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void makeJsonPostRequest(String url, JSONObject jsonRequest) {
        System.out.println("--------------------------------------------------------");
        System.out.println("------" + "url=  " + url + " --------");
        System.out.println("------" + "jsonRequest= " + jsonRequest + " --------");
        System.out.println("---------------" + "headerKey = " + headerKey + " -----------------");
        System.out.println("--------------------------------------------------------");

        if (Utilities.getInstance().isOnline(context)) {

            makePostJsonRequest(url, jsonRequest);
        } else {
            showNoInternetConnection();
            callBack.onError(requestCode, ErrorType.NO_INTERNET, new JSONObject());
        }
    }

    @Override
    public void makeJsonPutRequest(String url, JSONObject jsonRequest) {
        System.out.println("--------------------------------------------------------");
        System.out.println("------" + "url=  " + url + " --------");
        System.out.println("------" + "jsonRequest= " + jsonRequest + " --------");
        System.out.println("---------------" + "headerKey = " + headerKey + " -----------------");
        System.out.println("--------------------------------------------------------");

        if (Utilities.getInstance().isOnline(context)) {

            makePutJsonRequest(url, jsonRequest);
        } else {
            showNoInternetConnection();
            callBack.onError(requestCode, ErrorType.NO_INTERNET, new JSONObject());
        }
    }

    @Override
    public void makeStringGetRequest(String url) {
        System.out.println("--------------------------------------------------------");
        System.out.println("------" + "url= " + url + " --------");
        System.out.println("--------------------------------------------------------");
        if (Utilities.getInstance().isOnline(context)) {

            makeGetStringRequest(url);
        } else {
            showNoInternetConnection();
            callBack.onError(requestCode, ErrorType.NO_INTERNET, new JSONObject());
        }
    }

    private void makeGetStringRequest(String url) {
//        url = url + "?v=" + System.currentTimeMillis();
        if (Utilities.getInstance().isOnline(context)) {
            StringRequest sr = new StringRequest(Request.Method.GET, url, successStringListener, errorListener);
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            sr.setRetryPolicy(policy);
            MyApplication.getInstance().addToRequestQueue(sr, tag_json_obj);
        } else {
            showNoInternetConnection();
            callBack.onError(requestCode, ErrorType.NO_INTERNET, new JSONObject());
        }
    }

    private void makePostJsonRequest(String url, final JSONObject jsonRequest) {

//        url = url + "?v=" + System.currentTimeMillis();

        if (Utilities.getInstance().isOnline(context)) {

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, successJsonListener, errorListener) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> mHeaders = new HashMap<>();
                    try {
                        for (String key : super.getHeaders().keySet()) {
                            mHeaders.put(key, super.getHeaders().get(key));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mHeaders.put("Content-Type", "application/json; charset=utf-8");
                    mHeaders.put("splalgoval", headerKey);
                    return mHeaders;
                    // return super.getHeaders();
                }
            };

            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjReq.setRetryPolicy(policy);
            jsonObjReq.setTag(requestTag);
            MyApplication.getInstance().addToRequestQueue(jsonObjReq, requestTag);
        } else {
            showNoInternetConnection();
            callBack.onError(requestCode, ErrorType.NO_INTERNET, new JSONObject());
        }
    }

    private void makePutJsonRequest(String url, final JSONObject jsonRequest) {

//        url = url + "?v=" + System.currentTimeMillis();

        if (Utilities.getInstance().isOnline(context)) {

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT, url, jsonRequest, successJsonListener, errorListener) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> mHeaders = new HashMap<>();
                    try {
                        for (String key : super.getHeaders().keySet()) {
                            mHeaders.put(key, super.getHeaders().get(key));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mHeaders.put("Content-Type", "application/json; charset=utf-8");
                    mHeaders.put("splalgoval", headerKey);
                    return mHeaders;
                    // return super.getHeaders();
                }
            };

            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjReq.setRetryPolicy(policy);
            jsonObjReq.setTag(requestTag);
            MyApplication.getInstance().addToRequestQueue(jsonObjReq, requestTag);
        } else {
            showNoInternetConnection();
            callBack.onError(requestCode, ErrorType.NO_INTERNET, new JSONObject());
        }
    }
}
