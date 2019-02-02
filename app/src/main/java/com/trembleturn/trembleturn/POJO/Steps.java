package com.trembleturn.trembleturn.POJO;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
 
import com.google.gson.annotations.SerializedName;

public class Steps implements Parcelable {

	public Distance distance;

	public Duration duration;

	public EndLocation endlocation;

	@SerializedName("html_instructions")
	public String htmlinstructions;

	@SerializedName("maneuver")
	public String maneuver;

	public Polyline polyline;

	public StartLocation startlocation;

	@SerializedName("travel_mode")
	public String trvelmode;


    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    @Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(this.distance, flags);
		dest.writeParcelable(this.duration, flags);
		dest.writeParcelable(this.endlocation, flags);
		dest.writeString(this.htmlinstructions);
		dest.writeString(this.maneuver);
		dest.writeParcelable(this.polyline, flags);
		dest.writeParcelable(this.startlocation, flags);
		dest.writeString(this.trvelmode);
	}

	public Steps() {
	}

	protected Steps(Parcel in) {
		this.distance = in.readParcelable(Distance.class.getClassLoader());
		this.duration = in.readParcelable(Duration.class.getClassLoader());
		this.endlocation = in.readParcelable(EndLocation.class.getClassLoader());
		this.htmlinstructions = in.readString();
		this.maneuver = in.readString();
		this.polyline = in.readParcelable(Polyline.class.getClassLoader());
		this.startlocation = in.readParcelable(StartLocation.class.getClassLoader());
		this.trvelmode = in.readString();
	}

	public static final Parcelable.Creator<Steps> CREATOR = new Parcelable.Creator<Steps>() {
		@Override
		public Steps createFromParcel(Parcel source) {
			return new Steps(source);
		}

		@Override
		public Steps[] newArray(int size) {
			return new Steps[size];
		}
	};
}