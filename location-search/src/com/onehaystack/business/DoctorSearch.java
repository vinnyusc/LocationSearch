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
import com.onehaystack.model.DoctorResult;
import com.onehaystack.model.vo.CommonResultObject;
import com.onehaystack.model.vo.DoctorObject;
import com.onehaystack.model.vo.Result;

/**
 * Servlet implementation class LocationSearch
 */
@WebServlet("/search/doctorsearch")
public class DoctorSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DoctorSearch() {
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
		//System.out.println("===="+request.getParameter("name"));
		
		///////////////////////////////////////////
		if (srcLat.equals("null") || srcLng.equals("null")) {
			// do not return the results
			out.println("Location service not activated");
		} 	

		if(request.getParameter("name")!=null){
			String practice= request.getParameter("practice");
			DoctorObject doctor = new DoctorObject();
			DoctorResult doctorResult = new DoctorResult();
			doctor.zip = zipCode;
			doctor.distance = Double.parseDouble(miles);
			doctor.lat = Double.parseDouble(srcLat);
			doctor.lng = Double.parseDouble(srcLng);
			doctor.category = practice;
			doctor.name = query;
			List<DoctorObject> doctorList = doctorResult.getDoctorInfo(doctor);
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
