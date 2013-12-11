package com.onehaystack.model.vo;

import java.util.List;

public class GeocodeObject {
	public List<Result> results;
	public String status;
	
	public class Location{
		public double lat;
		public double lng;
	}
	public class Geometry{
		public Location location;
	}
	public class Result{
		public String formatted_address;
		public Geometry geometry;
		public double distance;
		
	}
	public List<Result> getResults(){
		return this.results;
	}
}
