package com.trembleturn.trembleturn.POJO;

import java.util.List;
 
import com.google.gson.annotations.SerializedName;

public class ServerResponse{
	
	public List<GeocodedWaypoints> waypoints;

	public List<Routes> routes;

	@SerializedName("status")
	public String status;
}
