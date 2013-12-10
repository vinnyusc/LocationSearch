package com.onehaystack.model.vo;

import java.util.ArrayList;
import java.util.List;

public class GoogleObject {
	public List<Result> results;
	public String status;	
	public String toString() {
		return "";
	}
		
	// JSON object Location
	public class Location {
		public double lat;
		public double lng;
	}

	// JSON Object Geometry
	public class Geometry {
		public Location location;
	}

	// JSON Object Result
	public class Result {

		public Geometry geometry;
		public String name;
		public String vicinity;
		public String reference;
		public GoogleObjectDetails details = new GoogleObjectDetails();
		public double distance;
		
		public String formatted_address;
		public ArrayList<String> types;

		public String toString() {
			String result = "\nName: " + name;
			//result += "\nVicinity: " + vicinity;
			//result += "\nAddress: " + formatted_address;
			//result += "\nTypes: " + types;
			return result;
		}

	}
}
