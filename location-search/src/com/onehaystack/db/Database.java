package com.onehaystack.database;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

public class Database {
	Connection conn;
	public Connection getDatabaseConnection() {
		Connection dbConnection = null;
		String URL = "jdbc:mysql://localhost:3306/";
		String databaseName = "doctors2";
		String userName = "root";
		String password = "hangman";
		String driver = "com.mysql.jdbc.Driver";

		try {
			Class.forName(driver).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			dbConnection = DriverManager
					.getConnection(
							URL
									+ databaseName
									+ "?useServerPrepStmts=false&rewriteBatchedStatements=true",
							userName, password);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return dbConnection;
	}

	public PreparedStatement getPreparedStatement(String zip, String name, String practice) {
		//name = null;
		if(practice.toLowerCase().equals("all")){
			practice = "^[P,N,O]";
		}
		String columns = "NPI, category, Provider_First_Name, Provider_Last_Name_Legal_Name, Provider_First_Line_Business_Mailing_Address ,Provider_Business_Mailing_Address_City_Name,Provider_Business_Mailing_Address_State_Name ,Provider_Business_Mailing_Address_Postal_Code ,Provider_Business_Mailing_Address_Telephone_Number ";
		String queryString = "";
		String zipColumn = "Provider_Business_Mailing_Address_Postal_Code";
		if(zip.matches(".*\\d.*")){
			   // contains a number
			zipColumn = "Provider_Business_Mailing_Address_Postal_Code";
			} else{
			   // does not contain a number
				zipColumn = "Provider_Business_Mailing_Address_City_Name";
				zip = zip.replace("=", " ");
			}
		name = name.replace("="," ");
		String fNameColumn = "Provider_First_Name";
		String lNameColumn = "Provider_Last_Name_Legal_Name";
		String practiceColumn = "";
		if(!zip.equals("null") && !name.equals("null")){
			queryString = "SELECT "+columns+" FROM mytable2"+" WHERE "+zipColumn+" like '"+zip+"%' AND ("+fNameColumn+" like '%"+name+"%' OR "+lNameColumn+" like '%"+name+"%') AND category REGEXP '"+practice+"' limit 30";
		}else if(!zip.equals("null") && name.equals("null")){
			queryString = "SELECT "+columns+" FROM mytable2"+" WHERE "+zipColumn+" like '"+zip+"%' AND ("+fNameColumn+" REGEXP '[A-Z]' OR "+lNameColumn+" REGEXP '[A-Z]') AND category REGEXP '"+practice+"' limit 30";

		}else if(zip.equals("null") && !name.equals("null")){
			queryString = "SELECT "+columns+" FROM mytable2"+" WHERE "+fNameColumn+" like '%"+name+"%' OR "+lNameColumn+" like '%"+name+"%' AND category REGEXP '"+practice+"' limit 30";

		}
		PreparedStatement pS = null;
		try {
			pS = conn.prepareStatement(queryString);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(queryString);
		return pS;
	}
	
	public ResultSet getResultSet(String zip,String name, String practice){
		conn = getDatabaseConnection();
		PreparedStatement ps = getPreparedStatement(zip,name,practice);
		ResultSet rs = null;
		try {
			rs = ps.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
}
