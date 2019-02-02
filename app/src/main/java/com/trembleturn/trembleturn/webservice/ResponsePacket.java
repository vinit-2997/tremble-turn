package com.trembleturn.trembleturn.webservice;

import android.os.Parcel;
import android.os.Parcelable;

import com.trembleturn.trembleturn.POJO.Errors;
import com.trembleturn.trembleturn.POJO.DirectionResult;

import java.util.ArrayList;


public class ResponsePacket implements Parcelable {

    public static final Creator<ResponsePacket> CREATOR = new Creator<ResponsePacket>() {
        @Override
        public ResponsePacket createFromParcel(Parcel in) {
            return new ResponsePacket(in);
        }

        @Override
        public ResponsePacket[] newArray(int size) {
            return new ResponsePacket[size];
        }
    };
    ArrayList<DirectionResult> result;
    Errors errors;
    private String responsePacket;
    private String status;
    private int errorCode;
    private String message;

    public ResponsePacket() {
    }

    public ResponsePacket(String status, int errorCode) {
        this.status = status;
        this.errorCode = errorCode;
    }

    protected ResponsePacket(Parcel in) {
        responsePacket = in.readString();
        status = in.readString();
        errorCode = in.readInt();
        message = in.readString();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }

    public ArrayList<DirectionResult> getResult() {
        return result;
    }

    public void setResult(ArrayList<DirectionResult> result) {
        this.result = result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(responsePacket);
        dest.writeString(status);
        dest.writeInt(errorCode);
        dest.writeString(message);
    }

    public String getResponsePacket() {
        return responsePacket;
    }

    public void setResponsePacket(String responsePacket) {
        this.responsePacket = responsePacket;
    }

}