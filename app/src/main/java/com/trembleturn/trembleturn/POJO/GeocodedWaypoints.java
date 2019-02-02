package com.trembleturn.trembleturn;

import java.util.List;
 
import com.google.gson.annotations.SerializedName;

public class GeocodedWaypoints{

	@SerializedName("geocoder_status")
	public String geocoderstatus;

	@SerializedName("place_id")
	public String placeid;

	public List<String> types;
}
