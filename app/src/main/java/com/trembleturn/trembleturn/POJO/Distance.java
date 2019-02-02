package com.trembleturn.trembleturn.POJO;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
 
import com.google.gson.annotations.SerializedName;

public class Distance implements Parcelable {
	
	@SerializedName("text")
	public String text;

	@SerializedName("value")
	public int value;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.text);
		dest.writeInt(this.value);
	}

	public Distance() {
	}

	protected Distance(Parcel in) {
		this.text = in.readString();
		this.value = in.readInt();
	}

	public static final Parcelable.Creator<Distance> CREATOR = new Parcelable.Creator<Distance>() {
		@Override
		public Distance createFromParcel(Parcel source) {
			return new Distance(source);
		}

		@Override
		public Distance[] newArray(int size) {
			return new Distance[size];
		}
	};
}