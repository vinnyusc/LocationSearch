package com.onehaystack.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ArrayList;

import com.onehaystack.db.Mysql;
import com.onehaystack.model.vo.DoctorObject;
import com.onehaystack.model.vo.Node;
import com.onehaystack.model.vo.Result;
import com.onehaystack.util.Process;

public class DoctorResult implements DoctorInterface {

	Process p = new Process();

	public List<DoctorObject> getDoctorInfo(DoctorObject doctor) {

		
		String zip = doctor.zip;
		//String miles = doctor.distance+"";
		String name = doctor.name;
		String practice = doctor.category;
		String lat = doctor.lat+"";
		String lng = doctor.lng+"";
		
		Mysql db = new Mysql();
		
		ResultSet rs = db.getResultSet(zip,name,practice);

		List<DoctorObject> doctorObjList = getDoctorObjectList(rs, lat, lng);
		
		return doctorObjList;

	}

	

	public List<DoctorObject> getDoctorObjectList(ResultSet rs, String lat,
			String lng) {
		List<DoctorObject> doctorObjList = new ArrayList<DoctorObject>();
		
		try {
			if(rs!=null)
			while (rs.next()) {
				if (!rs.getString("Provider_Last_Name_Legal_Name")
						.replace(" ", "").equals("")
						&& !rs.getString(
								"Provider_First_Line_Business_Mailing_Address")
								.replace(" ", "").equals("")) {

					DoctorObject doctor = new DoctorObject();
					doctor.setId(rs.getString("NPI"));
					doctor.setfName(rs.getString("Provider_First_Name"));
					doctor.setlName(rs
							.getString("Provider_Last_Name_Legal_Name"));
					doctor.name = rs.getString("Provider_First_Name")+ " "+rs
							.getString("Provider_Last_Name_Legal_Name");
					String address = rs
							.getString("Provider_First_Line_Business_Mailing_Address");
					String city = rs
							.getString("Provider_Business_Mailing_Address_City_Name");
					String state = rs
							.getString("Provider_Business_Mailing_Address_State_Name");
					String postalCode = rs
							.getString("Provider_Business_Mailing_Address_Postal_Code");

					String fullAddress = address + " " + city + " "
							+ state + " " + postalCode;
					
					fullAddress = fullAddress.replace(" ", "+");
					//System.out.println(fullAddress);
					
					Node n = p.convertZipToGeo(fullAddress);
					//Thread.sleep(50);
					//System.out.println("Formatted_Address: "+n.getAddress());
					doctor.setAddress(n.getAddress());
					doctor.setLat(n.getLat());
					doctor.setLng(n.getLng());
					
					doctor.distance = p.computeDistance(doctor.lat, doctor.lng,
							Double.parseDouble(lat), Double.parseDouble(lng));
					DecimalFormat df = new DecimalFormat("####0.00");
					doctor.distance = Double.parseDouble(df.format(doctor.distance));
					if(doctor.distance>4000)
						continue;
					String phone = rs.getString("Provider_Business_Mailing_Address_Telephone_Number");
					//System.out.println(phone);
					if(phone!=null){
						//System.out.println("I'm in");
						phone = phone.replaceFirst("(\\d{3})(\\d{3})(\\d+)","($1) $2-$3");
						if(phone.equals("")){
							phone = "Ph: N/A";
						}
					}
					doctor.setPhone(phone);
					doctor.category = rs.getString("category");
					doctorObjList.add(doctor);
					// doctor.setLat(lat);
				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return doctorObjList;
	}
}
