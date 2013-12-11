package com.onehaystack.business;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.onehaystack.db.Mysql;
import com.onehaystack.model.CommonResult;
import com.onehaystack.model.DoctorResult;
import com.onehaystack.model.GoogleResult;
import com.onehaystack.model.YelpResult;
import com.onehaystack.model.vo.CommonResultObject;
import com.onehaystack.model.vo.DoctorObject;
import com.onehaystack.model.vo.Node;
import com.onehaystack.model.vo.Result;
import com.onehaystack.util.Process;

/**
 * Servlet implementation class LocationSearch
 */
@WebServlet("/search/locationsearch")
public class LocationSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LocationSearch() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();

		String zipCode = request.getParameter("zip");

		String miles = request.getParameter("miles");
		String query = request.getParameter("query");
		String srcLat = request.getParameter("lat");
		String srcLng = request.getParameter("lng");
		query = query.replace(' ', '+');
		// StringBuilder result = new StringBuilder();
		

		System.out.println("===="+request.getParameter("name"));
		/*YelpObject yelpJson = yelpApi(source, miles, query);
		GoogleObject googleJson =  googleApi(source, miles, query);
		//add objects to the result array to send to final build 
		// of common result
		ArrayList<Object> resultObjects = new ArrayList<Object>();
		resultObjects.add(yelpJson);
		resultObjects.add(googleJson);
		
		CommonResultObject commonResult = getCommonResult(resultObjects);
		commonResult.sourceLat = source.getLat();
		commonResult.sourceLng = source.getLng();
		
		Gson g = new Gson();
		String r  = g.toJson(commonResult, CommonResultObject.class);
		//String googleJson = googleApi(source, miles, query);

		//out.println(googleJson.toString());
		 * 
		 */
		///////////////////////////////////////////
		Process p = new Process();
		Node source = new Node();
		String latitude;
		String longitude;
		
		if (srcLat.equals("null") || srcLng.equals("null") || zipCode.equals("null")==false) {
			source = p.convertZipToGeo((zipCode));
			latitude = Double.toString(source.getLat());
			longitude = Double.toString(source.getLng());
		} else {
			latitude = srcLat;
			longitude = srcLng;	
			System.out.println(latitude);
			System.out.println(longitude);
		}
		
		///////////////////////////////////////////
		CommonResult googleResult = new GoogleResult();
	//	List<Result> googleList = googleResult.getCommonResult(zipCode, miles, query, latitude, longitude);
	//	System.out.println("Google result: " + googleList.size());
		
		CommonResult yelpResult = new YelpResult();
//		List<Result> yelpList = yelpResult.getCommonResult(zipCode, miles, query, latitude, longitude);
//		System.out.println("Yelp Result: "+yelpList.size());
		//googleList.addAll(yelpList);
		
		//DoctorResult doctorResult = new DoctorResult();
		//List<DoctorObject> doctorList = doctorResult.getCommonResult(zipCode, miles, query, latitude, longitude,practice);
		
		//remove duplicates based on phone number
		HashSet<Result> resultsSet = new HashSet<Result>();
		//resultsSet.addAll(doctorList);
		//resultsSet.addAll(yelpList);
		
		if(request.getParameter("name")!=null){
			String practice= request.getParameter("practice");
			DoctorResult doctorResult = new DoctorResult();
			List<DoctorObject> doctorList = doctorResult.getCommonResult(zipCode, miles, query, srcLat, srcLng,practice);
			List<Result> result = new ArrayList<Result>(doctorList);
			Collections.sort(result);
			
			CommonResultObject c = new CommonResultObject();
			c.results = result;
			c.sourceLat = Double.parseDouble(srcLat);
			c.sourceLng = Double.parseDouble(srcLng);
			
			Gson g = new Gson();
			String output = g.toJson(c, c.getClass());
			out.println(output);
		}
		else{
			List<Result> googleList = googleResult.getCommonResult(zipCode, miles, query, latitude, longitude);
			//	System.out.println("Google result: " + googleList.size());
			List<Result> result = new ArrayList<Result>(googleList);
			Collections.sort(result);
			
			CommonResultObject c = new CommonResultObject();
			c.results = result;
			c.sourceLat = Double.parseDouble(srcLat);
			c.sourceLng = Double.parseDouble(srcLng);
			
			Gson g = new Gson();
			String output = g.toJson(c, c.getClass());
			out.println(output);
		}
		

	}
	
	/*public CommonResultObject getCommonResult(ArrayList<Object> resultObjects){
		
		CommonResultObject commonResult = new CommonResultObject();
		
		ArrayList<CommonResultObject.Result> resultList =  new ArrayList<CommonResultObject.Result>();
		for(Object o: resultObjects){
			if(o.getClass().equals(YelpObject.class)){
				YelpObject y = (YelpObject) o;
				List<YelpObject.Result> results = y.results;
				
				for(YelpObject.Result each: results){
					CommonResultObject.Result r = commonResult.new Result();
					r.address = each.address1 + each.address2 + each.address3;
					r.distance = each.distance;
					r.name = each.name;
					resultList.add(r);
				}
				
			}else if(o.getClass().equals(GoogleObject.class)){
				GoogleObject g = (GoogleObject) o;
				List<GoogleObject.Result> googleResults = g.results;
				
				for(GoogleObject.Result each: googleResults){
					CommonResultObject.Result r = commonResult.new Result();
					r.address = each.vicinity;
					r.lat = each.geometry.location.lat;
					r.lng = each.geometry.location.lng;
					r.distance = each.distance;
					r.name = each.name;
					resultList.add(r);
				}
			}
		}
		Collections.sort(resultList);
		
		commonResult.setResult(resultList);
		return commonResult;
	}
	// YELP API -----------------------------------------------------------
	// http://api.yelp.com/business_review_search?
	// term=yelp
	// &lat=37.788022
	// &long=-122.399797
	// &radius=10
	// &limit=5
	// &ywsid=XXXXXXXXXXXXXXXXXX
	// --------------------------------------------------------------------
	public YelpObject yelpApi(Node source, double distanceRange, String query) {
		// yelp key
		// mD5dWViFBB0JyenJnZRCXg
		double lat = source.getLat();
		double lng = source.getLng();
		String url = "http://api.yelp.com/business_review_search?";
		url += "term=" + query;
		url += "&lat=" + lat + "&long=" + lng;
		url += "&radius=" + distanceRange;
		url += "&limit=20";
		url += "&ywsid=mD5dWViFBB0JyenJnZRCXg";

		String jsonResponse = getJsonString(url);
		
		System.out.println(jsonResponse);
		Gson gson = new Gson();
		YelpObject response = gson.fromJson(jsonResponse, YelpObject.class);
		
		//String jsonResult = gson.toJson(response);
		
		return response;
	}

	// Google API

	// -----------------------------------------------------------------
	// Google Places API URL
	// https://maps.googleapis.com/maps/api/place/nearbysearch/json?
	// location=-33.8670522,151.1957362
	// &radius=500
	// &types=food
	// &name=harbour
	// &sensor=false
	// &key=AIzaSyAzdUAifHriHzrL4rNJANRvqrg7ibJ50nM

	// ------------------------------------------------------------------
	public GoogleObject googleApi(Node source, double distanceRange, String query) {

		double lat = source.getLat();
		double lng = source.getLng();
		String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"

				+ "location="
				+ Double.toString(lat)
				+ ","
				+ Double.toString(lng)
				 + "&radius="
				 + distanceRange*1600
				// + "&types="
				// +"&name=physician"
				+ "&keyword="
				+ query
				//+ "&rankby=distance"
				// + "&query="
				// + query
				+ "&sensor=true"
				+ "&key=AIzaSyAEzoA7z11qqRiOXGdL0_UQX-F7Cl4GAoA";

		String jsonResponse = getJsonString(url);

		Gson gson = new Gson();
		GoogleObject response = gson.fromJson(jsonResponse, GoogleObject.class);
		List<GoogleObject.Result> results = response.results;
		for (GoogleObject.Result eachResult : results) {
			double lat1 = eachResult.geometry.location.lat;
			double lng1 = eachResult.geometry.location.lng;
			double dist = computeDistance(source.getLat(), source.getLng(),
					lat1, lng1);

			DecimalFormat df = new DecimalFormat("####0.00");
			eachResult.distance = Double.parseDouble(df.format(dist));

			// eachResult.source.location.lat = source.getLat();
			// eachResult.source.location.lng = source.getLng();
		}

		// response.source.geometry.location.lng = 22.22;

		//response.sourceLat = source.getLat();
		//response.sourceLng = source.getLng();
		String jsonResult = gson.toJson(response);
		
		
		return response;

	}

	// Converts zip code to geo co ordinates - creates Node Object
	public Node convertZipToGeo(int zip) {
		String urlString = "http://maps.googleapis.com/maps/api/geocode/json?address="
				+ zip + "&sensor=true";

		String jsonString = getJsonString(urlString);

		// Converting json string to Google Gson object
		Gson gson = new Gson();
		GeocodeObject geocodeObject = gson.fromJson(jsonString,
				GeocodeObject.class);

		List<GeocodeObject.Result> results = geocodeObject.results;

		double lat = results.get(0).geometry.location.lat;
		double lng = results.get(0).geometry.location.lng;

		Node node = new Node(lat, lng);

		return node;
	}

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
			e.printStackTrace();
		}

		return sb.toString();

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
	*/

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
