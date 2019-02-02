package com.trembleturn.trembleturn.POJO;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
 
import com.google.gson.annotations.SerializedName;

public class Duration implements Parcelable {
	
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

	public Duration() {
	}

	protected Duration(Parcel in) {
		this.text = in.readString();
		this.value = in.readInt();
	}

	public static final Parcelable.Creator<Duration> CREATOR = new Parcelable.Creator<Duration>() {
		@Override
		public Duration createFromParcel(Parcel source) {
			return new Duration(source);
		}

		@Override
		public Duration[] newArray(int size) {
			return new Duration[size];
		}
	};
}