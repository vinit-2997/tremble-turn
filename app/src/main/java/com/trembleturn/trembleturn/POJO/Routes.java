package com.trembleturn.trembleturn;

import java.util.List;
 
import com.google.gson.annotations.SerializedName;

public class Routes{

	public Bounds bound;

	@SerializedName("copyrights")
	public String copyrights;

	public List<Legs> legs;

	public Polyline polyline;

	@SerializedName("summary")
	public String summary;

	public List<Warnings> warnings;

	public List<Waypointorder> waypointorder;
}