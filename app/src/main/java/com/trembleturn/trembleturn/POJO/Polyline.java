package com.trembleturn.trembleturn.POJO;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
 
import com.google.gson.annotations.SerializedName;

public class Polyline implements Parcelable {
	
	@SerializedName("points")
	public String points;


	public String getPoints() {
		return points;
	}

	public void setPoints(String points) {
		this.points = points;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.points);
	}

	public Polyline() {
	}

	protected Polyline(Parcel in) {
		this.points = in.readString();
	}

	public static final Parcelable.Creator<Polyline> CREATOR = new Parcelable.Creator<Polyline>() {
		@Override
		public Polyline createFromParcel(Parcel source) {
			return new Polyline(source);
		}

		@Override
		public Polyline[] newArray(int size) {
			return new Polyline[size];
		}
	};
}