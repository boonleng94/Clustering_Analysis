package Vectors;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DBControllerKmeans {
	SQLManager connection = new SQLManager();
	Connection conn;
	ResultSet rs = null;
	PreparedStatement ps = null;
	
	//Retrieve the clusters that the posts belong to for Month1
	public ArrayList<String> getClusterNumsMonth1(ArrayList<String> omittedList) {
		ArrayList<String> clusteredDocs = new ArrayList<String>();
		
		conn = connection.getConnection();

		try {
			ps = conn.prepareStatement("Select idData, TopicID from MahoutKmeanClusterMonth1");

			rs = ps.executeQuery();

			while (rs.next()) {
				boolean isOmitted = false;
				for (int i = 0; i < omittedList.size(); i++) {
					if (rs.getString("idData").equals(omittedList.get(i))) {
						isOmitted = true;
					}
				}
				
				if (isOmitted == false) {
					clusteredDocs.add(rs.getString("idData")+ ":" + rs.getString("TopicID"));
				}
			}
			
			conn.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		
		return clusteredDocs;
	}
	
	//Retrieve the clusters that the posts belong to for Month6
	public ArrayList<String> getClusterNumsMonth6(ArrayList<String> omittedList) {
		ArrayList<String> clusteredDocs = new ArrayList<String>();
		
		conn = connection.getConnection();

		try {
			ps = conn.prepareStatement("Select idData, TopicID from MahoutKmeanClusterMonth6");

			rs = ps.executeQuery();

			while (rs.next()) {
				boolean isOmitted = false;
				for (int i = 0; i < omittedList.size(); i++) {
					if (rs.getString("idData").equals(omittedList.get(i))) {
						isOmitted = true;
					}
				}
				
				if (isOmitted == false) {
					clusteredDocs.add(rs.getString("idData")+ ":" + rs.getString("TopicID"));
				}
			}
			
			conn.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		
		return clusteredDocs;
	}
	
	//Retrieve the clusters that the posts belong to for Month12
	public ArrayList<String> getClusterNumsMonth12(ArrayList<String> omittedList) {
		ArrayList<String> clusteredDocs = new ArrayList<String>();
		
		conn = connection.getConnection();

		try {
			ps = conn.prepareStatement("Select idData, TopicID from MahoutKmeanClusterMonth12");

			rs = ps.executeQuery();

			while (rs.next()) {
				boolean isOmitted = false;
				for (int i = 0; i < omittedList.size(); i++) {
					if (rs.getString("idData").equals(omittedList.get(i))) {
						isOmitted = true;
					}
				}
				
				if (isOmitted == false) {
					clusteredDocs.add(rs.getString("idData")+ ":" + rs.getString("TopicID"));
				}
			}
			
			conn.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		
		return clusteredDocs;
	}
}
