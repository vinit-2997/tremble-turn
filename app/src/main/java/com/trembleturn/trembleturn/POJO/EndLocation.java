package com.trembleturn.trembleturn.POJO;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
 
import com.google.gson.annotations.SerializedName;

public class EndLocation implements Parcelable {
	
	@SerializedName("lat")
	public double lat;

	@SerializedName("lng")
	public double lng;

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeDouble(this.lat);
		dest.writeDouble(this.lng);
	}

	public EndLocation() {
	}

	protected EndLocation(Parcel in) {
		this.lat = in.readDouble();
		this.lng = in.readDouble();
	}

	public static final Parcelable.Creator<EndLocation> CREATOR = new Parcelable.Creator<EndLocation>() {
		@Override
		public EndLocation createFromParcel(Parcel source) {
			return new EndLocation(source);
		}

		@Override
		public EndLocation[] newArray(int size) {
			return new EndLocation[size];
		}
	};
}