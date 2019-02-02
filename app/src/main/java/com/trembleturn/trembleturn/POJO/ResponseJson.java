package com.trembleturn.trembleturn.POJO;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ResponseJson implements Parcelable {

   public ArrayList<Routes> routes;

    public ArrayList<Routes> getRoutes() {
        return routes;
    }

    public void setRoutes(ArrayList<Routes> routes) {
        this.routes = routes;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.routes);
    }

    public ResponseJson() {
    }

    protected ResponseJson(Parcel in) {
        this.routes = in.createTypedArrayList(Routes.CREATOR);
    }

    public static final Parcelable.Creator<ResponseJson> CREATOR = new Parcelable.Creator<ResponseJson>() {
        @Override
        public ResponseJson createFromParcel(Parcel source) {
            return new ResponseJson(source);
        }

        @Override
        public ResponseJson[] newArray(int size) {
            return new ResponseJson[size];
        }
    };
}
