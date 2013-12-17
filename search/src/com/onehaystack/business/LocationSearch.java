package com.onehaystack.business;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.onehaystack.model.CommonResult;
import com.onehaystack.model.GoogleResult;
import com.onehaystack.model.vo.CommonResultObject;
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

		System.out.println("====" + request.getParameter("name"));
		
		Process p = new Process();
		Node source = new Node();
		String latitude;
		String longitude;

		if (srcLat.equals("null") || srcLng.equals("null")
				|| zipCode.equals("null") == false) {
			source = p.convertZipToGeo((zipCode));
			latitude = Double.toString(source.getLat());
			longitude = Double.toString(source.getLng());
		} else {
			latitude = srcLat;
			longitude = srcLng;
			System.out.println(latitude);
			System.out.println(longitude);
		}

		// /////////////////////////////////////////
		CommonResult googleResult = new GoogleResult();
		
		// remove duplicates based on phone number
		//HashSet<Result> resultsSet = new HashSet<Result>();
		

		List<Result> googleList = googleResult.getCommonResult(zipCode, miles,
				query, latitude, longitude);
		// System.out.println("Google result: " + googleList.size());
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
