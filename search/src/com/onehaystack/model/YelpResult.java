package com.onehaystack.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import com.google.gson.Gson;
import com.onehaystack.model.vo.Node;
import com.onehaystack.model.vo.Result;
import com.onehaystack.model.vo.YelpObject;
import com.onehaystack.util.Process;

public class YelpResult implements CommonResult {

	Process p = new Process();
	double latitude, longitude;
	Node source = null;
	String jsonString = null;
	OAuthService service;
	Token accessToken;
	
	List<String> categories = Arrays.asList("pharmacy","dentists","cafes");

	public YelpObject getYelpObject(String yelpJson) {

		Gson gson = new Gson();
		YelpObject response = gson.fromJson(yelpJson, YelpObject.class);

		return response;
	}

	public List<Result> getList(YelpObject y, String query) {

		List<Result> resultList = new ArrayList<Result>();
		List<YelpObject.Result> results = y.results;
		// CommonResultObject c = new CommonResultObject();
		boolean flag = false;
		if(categories.contains(query.toLowerCase()))
			flag = true;
		for (YelpObject.Result each : results) {
			
			if(each.getPhone()==null){
				each.setPhone("00000000");
			}
			if(flag==false && each.name.toLowerCase().contains(query.toLowerCase())==false){
				//System.out.println(each.name);
				continue;
			}
				
			Process p = new Process();
			String addr = each.location.getAddress().replace(" ", "+");
			//System.out.println(addr);
			Node n  = p.convertZipToGeo(each.location.getAddress().replace(" ", "+"));
			Result r = new Result();
			r.address = each.location.getAddress();
			//r.distance = each.distance/1600;
			DecimalFormat df = new DecimalFormat("####0.00");
			r.distance = Double.parseDouble(df.format(each.distance/1600));
			r.name = each.name;
			r.rating = (float) each.rating;
			r.phone = each.phone.replaceFirst("(\\d{3})(\\d{3})(\\d+)","($1) $2-$3");
			r.lat = n.getLat();
			r.lng = n.getLng();
			// replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3")
			resultList.add(r);
		}

		return resultList;

	}

	@Override
	public List<Result> getCommonResult(String zip, String miles, String query,
			String lat, String lng) {

		// Taking miles as default if not mentioned
		if (miles == null) {
			miles = Integer.toString(CommonResult.miles);
		}
		
		 double meters = Double.parseDouble(miles)* 1600;
		miles = meters+"";
		 /*
		 * if (lat == null || lng == null || zip != null) { source =
		 * p.convertZipToGeo(Integer.parseInt(zip)); latitude = source.getLat();
		 * longitude = source.getLng(); } else { latitude =
		 * Double.parseDouble(lat); longitude = Double.parseDouble(lng); }
		 */
		// String url = "http://api.yelp.com/business_review_search?";
		String url = "http://api.yelp.com/v2/search?";
		url += "term=" + query;
		url += "&lat=" + lat + "&long=" + lng;
		url += "&radius=" + miles;
		url += "&limit=20";
		url += "&ywsid=mD5dWViFBB0JyenJnZRCXg";
		url += "dZ8OL4mrtQFs8V7Htr2VqQ";

		String consumerKey = "dZ8OL4mrtQFs8V7Htr2VqQ";
		String consumerSecret = "yIkysGuRKS8L0vpB5rdsPQuC2lQ";
		String token = "8Cpx2puSBH2XM_Kg0KkHMKZCDlfu8IP0";
		String tokenSecret = "JHFnHx7ui0Zghm_TTpaaOd8VKW0";
		
		Yelp yelp = new Yelp(consumerKey, consumerSecret, token, tokenSecret);
	    String response = yelp.search(query,Double.parseDouble(lat),Double.parseDouble(lng),Double.parseDouble(miles));

		//jsonString = p.getJsonString(url);
	    //System.out.println(response);
		YelpObject y = getYelpObject(response);
		List<Result> list = getList(y,query);
		//System.out.println(list.size());
		return list;

	}

	/**
	 * Setup the Yelp API OAuth credentials.
	 * 
	 * OAuth credentials are available from the developer site, under Manage API
	 * access (version 2 API).
	 * 
	 * @param consumerKey
	 *            Consumer key
	 * @param consumerSecret
	 *            Consumer secret
	 * @param token
	 *            Token
	 * @param tokenSecret
	 *            Token secret
	 */
	
	/*public YelpResult(String consumerKey, String consumerSecret, String token,
			String tokenSecret) {
		this.service = new ServiceBuilder().provider(YelpApi2.class)
				.apiKey(consumerKey).apiSecret(consumerSecret).build();
		this.accessToken = new Token(token, tokenSecret);
	}*/

	/**
	 * Search with term and location.
	 * 
	 * @param term
	 *            Search term
	 * @param latitude
	 *            Latitude
	 * @param longitude
	 *            Longitude
	 * @return JSON string response
	 */
	public String search(String term, double latitude, double longitude,String consumerKey, String consumerSecret,String token,String tokenSecret) {
		this.service = new ServiceBuilder().provider(YelpApi2.class)
				.apiKey(consumerKey).apiSecret(consumerSecret).build();
		this.accessToken = new Token(token, tokenSecret);
		OAuthRequest request = new OAuthRequest(Verb.GET,
				"http://api.yelp.com/v2/search");
		request.addQuerystringParameter("term", term);
		request.addQuerystringParameter("ll", latitude + "," + longitude);
		String limit = "10";
		request.addQuerystringParameter("limit", limit);
		this.service.signRequest(this.accessToken, request);
		Response response = request.send();
		return response.getBody();
	}

}
