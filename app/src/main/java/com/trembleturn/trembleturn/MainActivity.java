package com.trembleturn.trembleturn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.trembleturn.trembleturn.webservice.ErrorType;
import com.trembleturn.trembleturn.webservice.OnResponseListener;
import com.trembleturn.trembleturn.webservice.ResponsePacket;
import com.trembleturn.trembleturn.webservice.Router;
import com.trembleturn.trembleturn.webservice.Routes;

public class MainActivity extends BaseActivity implements OnResponseListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void OnClick(View v)
    {
        switch (v.getId()) {
            case R.id.main_page_button:
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
                break;
            case R.id.call_api:
                getAtoBSteps(new LatLng(19.0269, 72.8553), new LatLng(19.44769, 73.015137));
                break;
        }

    }

    public void getAtoBSteps(LatLng source, LatLng dest) {
        try {
            new Router(this, this, 123, "test")
                    .makeStringGetRequest(Routes.API_URL +
                            "origin=" + String.valueOf(source.latitude) + "," + String.valueOf(source.longitude) + "&" +
                            "destination=" + String.valueOf(dest.latitude) + "," + String.valueOf(dest.longitude) + "&" +
                            "key=" + Routes.API_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(int requestCode, ResponsePacket responsePacket) {

    }

    @Override
    public void onError(int requestCode, ErrorType errorType, ResponsePacket responsePacket) {

    }

    //The back press exit
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finishAffinity();
            return;
        }
    }
}
