package com.trembleturn.trembleturn.POJO;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
 
import com.google.gson.annotations.SerializedName;

public class Routes implements Parcelable {

	public Bounds bound;

	@SerializedName("copyrights")
	public String copyrights;

	public List<Legs> legs;

	public Polyline polyline;

	@SerializedName("summary")
	public String summary;

    public Bounds getBound() {
        return bound;
    }

    public void setBound(Bounds bound) {
        this.bound = bound;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.bound, flags);
        dest.writeString(this.copyrights);
        dest.writeList(this.legs);
        dest.writeParcelable(this.polyline, flags);
        dest.writeString(this.summary);
    }

    public Routes() {
    }

    protected Routes(Parcel in) {
        this.bound = in.readParcelable(Bounds.class.getClassLoader());
        this.copyrights = in.readString();
        this.legs = new ArrayList<Legs>();
        in.readList(this.legs, Legs.class.getClassLoader());
        this.polyline = in.readParcelable(Polyline.class.getClassLoader());
        this.summary = in.readString();
    }

    public static final Parcelable.Creator<Routes> CREATOR = new Parcelable.Creator<Routes>() {
        @Override
        public Routes createFromParcel(Parcel source) {
            return new Routes(source);
        }

        @Override
        public Routes[] newArray(int size) {
            return new Routes[size];
        }
    };
}