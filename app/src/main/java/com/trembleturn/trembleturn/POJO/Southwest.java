package com.trembleturn.trembleturn.POJO;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
 
import com.google.gson.annotations.SerializedName;

public class Southwest implements Parcelable {

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

	public Southwest() {
	}

	protected Southwest(Parcel in) {
		this.lat = in.readDouble();
		this.lng = in.readDouble();
	}

	public static final Parcelable.Creator<Southwest> CREATOR = new Parcelable.Creator<Southwest>() {
		@Override
		public Southwest createFromParcel(Parcel source) {
			return new Southwest(source);
		}

		@Override
		public Southwest[] newArray(int size) {
			return new Southwest[size];
		}
	};
}