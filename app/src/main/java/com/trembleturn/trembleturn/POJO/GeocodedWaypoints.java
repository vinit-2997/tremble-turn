package com.trembleturn.trembleturn.POJO;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
 
import com.google.gson.annotations.SerializedName;

public class GeocodedWaypoints implements Parcelable {

	@SerializedName("geocoder_status")
	public String geocoderstatus;

	@SerializedName("place_id")
	public String placeid;

	public List<String> types;


    public String getGeocoderstatus() {
        return geocoderstatus;
    }

    public void setGeocoderstatus(String geocoderstatus) {
        this.geocoderstatus = geocoderstatus;
    }

    @Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.geocoderstatus);
		dest.writeString(this.placeid);
		dest.writeStringList(this.types);
	}

	public GeocodedWaypoints() {
	}

	protected GeocodedWaypoints(Parcel in) {
		this.geocoderstatus = in.readString();
		this.placeid = in.readString();
		this.types = in.createStringArrayList();
	}

	public static final Parcelable.Creator<GeocodedWaypoints> CREATOR = new Parcelable.Creator<GeocodedWaypoints>() {
		@Override
		public GeocodedWaypoints createFromParcel(Parcel source) {
			return new GeocodedWaypoints(source);
		}

		@Override
		public GeocodedWaypoints[] newArray(int size) {
			return new GeocodedWaypoints[size];
		}
	};
}
