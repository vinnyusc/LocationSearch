package com.onehaystack.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MysqlPopulate {
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
			dbConnection = DriverManager
					.getConnection(
							URL
									+ databaseName
									+ "?useServerPrepStmts=false&rewriteBatchedStatements=true",
							userName, password);

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dbConnection;
	}

	public void writeFromFileToCategoryMapTable(String fileName,
			Pattern pattern, PreparedStatement ps) {
		File f = new File(fileName);

		try {
			BufferedReader buff = new BufferedReader(new FileReader(f));
			String line = buff.readLine();
			Matcher matcher;
			if(pattern == null){
				System.out.println("null pattern");
				buff.close();
				return;
			}
			matcher = pattern.matcher(line);

			
			while((line = buff.readLine())!=null){
				
				matcher = pattern.matcher(line);
				int count = 1;				
				while (matcher.find()) {
					try {
						ps.setString(count, matcher.group(1));						
					} catch (SQLException e) {
						System.out.println("Error in ps.setString for:"
								+ matcher.group(1));
						System.out.println("Hence Skipping the current value...");
						count++;
						continue;
					}
					count++;					
				}
				ps.execute();
			}			
			buff.close();
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found: " + fileName);
			return;
		} catch (IOException e) {
			System.out
					.println("could not read the line from file: " + fileName);
			return;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeFromFiletoDatabase(String source,Pattern pattern, PreparedStatement ps){
		File f = new File(source);
		File writef = new File("");
		ArrayList<String> categories= new ArrayList<String>();
		categories.add("Opth");
		categories.add("Opto");
		categories.add("Alrg");
		categories.add("Anes");
		categories.add("Card");
		categories.add("Crit");
		categories.add("Derm");
		categories.add("Ent");
		categories.add("Emrg");
		categories.add("Endo");
		categories.add("Gast");
		categories.add("Gene");
		categories.add("Geri");
		categories.add("Hcpa");
		categories.add("Infd");
		categories.add("Neph");
		categories.add("Neur");
		categories.add("Nums");
		categories.add("Numd");
		categories.add("Onco");
		categories.add("PnMg");
		categories.add("Patho");
		categories.add("Podi");
		categories.add("PrMd");
		categories.add("Pulm");
		categories.add("Radi");
		categories.add("Slep");
		categories.add("Urol");
		categories.add("CarSg");
		categories.add("Colr");
		categories.add("Gnsrg");
		categories.add("Nusrg");
		categories.add("Orsrg");
		categories.add("Pschi");
	
		try {
			BufferedReader bf = new BufferedReader(new FileReader(f));
			//StringBuilder sb = new StringBuilder();
			String line = bf.readLine();
			//PrintWriter writer = new PrintWriter("", "UTF-8");
			Matcher matcher;
			
			
			matcher = pattern.matcher(line);
			int count = 1;
			while(line!=null){
				
				line = bf.readLine();		
				matcher = pattern.matcher(line);
				int colCount = 1;
				while(matcher.find()){
					if(colCount>35)
						break;
					//System.out.println(matcher.group(1));
					try {
						ps.setString(colCount,matcher.group(1).replace("\"", ""));
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					colCount++;
				}
				ps.setString(36, categories.get((count)%categories.size()));
				try {
					ps.addBatch();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
				}
				if(count%60000==0){
					try {
						ps.executeBatch();
						System.out.println(count);						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				count++;
				if(count>4000000){
					break;
				}
					
			}
			bf.close();
			
			// remaining elements;
			ps.executeBatch();
			//sb.append(")");
			//writer.close();
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public PreparedStatement getPreparedStatementForCategoryMapTable(){
		String query = "INSERT INTO categoryMap(Abbr, Category) values(?,?)";
		PreparedStatement ps = null;
		try {
			 ps = conn.prepareStatement(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ps;
	}
	
	public PreparedStatement getWritePreparedStatement(){
		String query = "INSERT INTO doctordata(NPI, Entity_Type_Code,Replacement_NPI, Employer_Identification_Number_EIN,Provider_Organization_Name_Legal_Business_Name,  Provider_Last_Name_Legal_Name,  Provider_First_Name, Provider_Middle_Name,  Provider_Name_Prefix_Text,  Provider_Name_Suffix_Text,  Provider_Credential_Text,  Provider_Other_Organization_Name,   Provider_Other_Organization_Name_Type_Code,   Provider_Other_Last_Name,   Provider_Other_First_Name,   Provider_Other_Middle_Name,   Provider_Other_Name_Prefix_Text,   Provider_Other_Name_Suffix_Text,   Provider_Other_Credential_Text,   Provider_Other_Last_Name_Type_Code,   Provider_First_Line_Business_Mailing_Address,   Provider_Second_Line_Business_Mailing_Address,   Provider_Business_Mailing_Address_City_Name,   Provider_Business_Mailing_Address_State_Name,   Provider_Business_Mailing_Address_Postal_Code,   Provider_Business_Mailing_Address_Country_Code_If_outside_US,   Provider_Business_Mailing_Address_Telephone_Number,   Provider_Business_Mailing_Address_Fax_Number,   Provider_First_Line_Business_Practice_Location_Address,   Provider_Second_Line_Business_Practice_Location_Address,   Provider_Business_Practice_Location_Address_City_Name,   Provider_Business_Practice_Location_Address_State_Name,   Provider_Business_Practice_Location_Address_Postal_Code,   Provider_Business_Practice_Location_Address_Country_CodeoutUS,   Provider_Business_Practice_Location_Address_Telephone_Number,category) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement ps = null;
		try {
			 ps = conn.prepareStatement(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ps;
	}
	
	public void start(){
		conn = getDatabaseConnection();
		PreparedStatement ps,ps1 = null;
		try {
			conn.setAutoCommit(false);
			ps = getPreparedStatementForCategoryMapTable();
			ps1 = getWritePreparedStatement();
			if(ps==null){
				return;
			}
			Pattern pattern = Pattern.compile("([^,]+)");
			if(pattern==null){
				return;
			}
			//writeFromFileToCategoryMapTable("E:/Internship/category.txt", pattern,ps);
			writeFromFiletoDatabase("C:/Users/Node/Downloads/NPPES_Data_Dissemination_Oct_2013/data.csv", pattern,ps1);
			conn.commit();
			
			//conn.setAutoCommit(true);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}	
	public static void main(String[] args){
		MysqlPopulate mp = new MysqlPopulate();
		mp.start();
	}
}
