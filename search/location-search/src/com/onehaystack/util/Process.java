package com.onehaystack.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import com.google.gson.Gson;
import com.onehaystack.model.vo.GeocodeObject;
import com.onehaystack.model.vo.Node;

public class Process {

	// GET JSON RESPONSE AS STRING
	public String getJsonString(String urlString) {
		StringBuilder sb = new StringBuilder();
		URL url;
		try {
			url = new URL(urlString);
			URLConnection conn = url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));

			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			br.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("Is this a correct url??" + urlString);
			return sb.toString();
		}

		return sb.toString();

	}

	
	// Converts zip code to geo co ordinates - creates Node Object
	public Node convertZipToGeo(String zip) {
		String urlString = "http://maps.googleapis.com/maps/api/geocode/json?address=";
		urlString += zip + "&sensor=true";

		String jsonString = getJsonString(urlString);

		// Converting json string to Google Gson object
		Gson gson = new Gson();
		GeocodeObject geocodeObject = gson.fromJson(jsonString,
				GeocodeObject.class);
		List<GeocodeObject.Result> results = null;
		if(geocodeObject!=null){
     		 results= geocodeObject.getResults();
     		 System.out.println(geocodeObject.status);
		}else{
			System.out.println("GeocodeObject couldn't be retreived");
			return new Node(0,0);
		}
		
		
		double lat = 0.0;
		double lng = 0.0;
		String address = null;
		if(results!=null && results.size()>0){
			 lat = results.get(0).geometry.location.lat;
			 lng = results.get(0).geometry.location.lng;
			 address = results.get(0).formatted_address;
			 //System.out.println(address);
		}
		

		Node node = new Node(lat, lng);
		//System.out.println(node.getLat()+ "=-------=" +node.getLng());
		node.setAddress(address);
		return node;
	}

	// Computes distance b/w two geo location co ordinates
	public double computeDistance(double lat1, double lng1, double lat2,
			double lng2) {
		double distance = 0;
		double radius = 3958; // Earth's radius (miles)
		double deg_per_rad = 57.29578; // Number of degrees/radian (for
										// conversion)
		// double pi = 3.14287;
		distance = (radius
				* Math.PI
				* Math.sqrt((lat1 - lat2) * (lat1 - lat2)
						+ Math.cos(lat1 / deg_per_rad) // Convert these
														// to
						* Math.cos(lat2 / deg_per_rad) // radians for
														// cos()
						* (lng1 - lng2) * (lng1 - lng2)) / 180);

		return distance;
	}
}
