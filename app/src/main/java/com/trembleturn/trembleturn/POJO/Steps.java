package com.trembleturn.trembleturn;

import java.util.List;
 
import com.google.gson.annotations.SerializedName;

public class Steps{

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
}