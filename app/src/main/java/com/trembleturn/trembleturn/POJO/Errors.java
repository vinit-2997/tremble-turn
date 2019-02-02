package com.trembleturn.trembleturn.POJO;

import android.os.Parcel;
import android.os.Parcelable;

public class Errors implements Parcelable {
    String Message, Exception, ExceptionMessage, StackStrace;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getException() {
        return Exception;
    }

    public void setException(String exception) {
        Exception = exception;
    }

    public String getExceptionMessage() {
        return ExceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        ExceptionMessage = exceptionMessage;
    }

    public String getStackStrace() {
        return StackStrace;
    }

    public void setStackStrace(String stackStrace) {
        StackStrace = stackStrace;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Message);
        dest.writeString(this.Exception);
        dest.writeString(this.ExceptionMessage);
        dest.writeString(this.StackStrace);
    }

    public Errors() {
    }

    protected Errors(Parcel in) {
        this.Message = in.readString();
        this.Exception = in.readString();
        this.ExceptionMessage = in.readString();
        this.StackStrace = in.readString();
    }

    public static final Parcelable.Creator<Errors> CREATOR = new Parcelable.Creator<Errors>() {
        @Override
        public Errors createFromParcel(Parcel source) {
            return new Errors(source);
        }

        @Override
        public Errors[] newArray(int size) {
            return new Errors[size];
        }
    };
}
