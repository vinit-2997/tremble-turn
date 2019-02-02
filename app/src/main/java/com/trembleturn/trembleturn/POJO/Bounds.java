package com.trembleturn.trembleturn.POJO;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
 
import com.google.gson.annotations.SerializedName;

public class Bounds implements Parcelable {

	public Northeast northeast;

	public Southwest southwest;

	public Northeast getNortheast() {
		return northeast;
	}

	public void setNortheast(Northeast northeast) {
		this.northeast = northeast;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(this.northeast, flags);
		dest.writeParcelable(this.southwest, flags);
	}

	public Bounds() {
	}

	protected Bounds(Parcel in) {
		this.northeast = in.readParcelable(Northeast.class.getClassLoader());
		this.southwest = in.readParcelable(Southwest.class.getClassLoader());
	}

	public static final Parcelable.Creator<Bounds> CREATOR = new Parcelable.Creator<Bounds>() {
		@Override
		public Bounds createFromParcel(Parcel source) {
			return new Bounds(source);
		}

		@Override
		public Bounds[] newArray(int size) {
			return new Bounds[size];
		}
	};
}