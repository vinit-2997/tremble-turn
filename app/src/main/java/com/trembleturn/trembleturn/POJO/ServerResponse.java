package com.trembleturn.trembleturn.POJO;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
 
import com.google.gson.annotations.SerializedName;

public class ServerResponse implements Parcelable {
	
	public List<GeocodedWaypoints> waypoints;

	public List<Routes> routes;

	@SerializedName("status")
	public String status;

	public List<GeocodedWaypoints> getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(List<GeocodedWaypoints> waypoints) {
		this.waypoints = waypoints;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(this.waypoints);
		dest.writeTypedList(this.routes);
		dest.writeString(this.status);
	}

	public ServerResponse() {
	}

	protected ServerResponse(Parcel in) {
		this.waypoints = in.createTypedArrayList(GeocodedWaypoints.CREATOR);
		this.routes = in.createTypedArrayList(Routes.CREATOR);
		this.status = in.readString();
	}

	public static final Parcelable.Creator<ServerResponse> CREATOR = new Parcelable.Creator<ServerResponse>() {
		@Override
		public ServerResponse createFromParcel(Parcel source) {
			return new ServerResponse(source);
		}

		@Override
		public ServerResponse[] newArray(int size) {
			return new ServerResponse[size];
		}
	};
}
