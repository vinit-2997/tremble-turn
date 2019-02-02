package com.trembleturn.trembleturn.webservice;


/**
 * Created by GladiatoR on 25/11/15.
 */
public interface OnResponseListener {
    void onSuccess(int requestCode, ResponsePacket responsePacket);

    void onError(int requestCode, ErrorType errorType, ResponsePacket responsePacket);

}
