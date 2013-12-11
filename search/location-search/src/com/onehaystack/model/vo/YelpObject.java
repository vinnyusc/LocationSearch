package com.onehaystack.model.vo;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class YelpObject {

	@SerializedName("businesses")
	public List<Result> results;
	
	public class Location{
		public String[] display_address;
		
		public String getAddress(){
			String address = "";
			for(String each: this.display_address){
				address = address+" "+each;
			}
			return address;
		
		}
	}

	public class Result {
		public String name;
		public double distance;
		public String address1;
		public String address2;
		public String address3;
		public Location location;
		public String phone = null;
		public int review_count;
		public double rating;

		public String toString() {
			String result = "\nName: " + name;
			//result+= "\nAddress: " + address1+" " + address2 + " " + address3;
			result+= "\nDistance: " + distance;
			//result+= "\nRating: " + avg_rating;
			return result;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}
		
		
	}

}
