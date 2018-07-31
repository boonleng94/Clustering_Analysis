import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import Controller.SQLManager;
import Controller.Document;
import Controller.Topic;

public class SQLControllerMth12 {
	SQLManager connection = new SQLManager();
	Connection conn;
	ResultSet rs = null;
	PreparedStatement ps = null;

	//Insert the cluster numbers for each post belongs to into database
	public void InsertCluster(ArrayList<Document> cluster) {

		conn = connection.getConnection();

		try {
			for (int i = 0; i < cluster.size(); i++) {

				ps = conn.prepareStatement("INSERT INTO mahoutldaclustermonth12tfidf (idData, TopicID) VALUES (?,?)");
				ps.setInt(1, cluster.get(i).getDataid());
				ps.setInt(2, cluster.get(i).getTopicid());

				ps.executeUpdate();

			}
			conn.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block

		}

	}

	//Insert the topics generated for each cluster number into database (not really needed for P3)
	public void InsertTopic(ArrayList<Topic> topicdata) {

		conn = connection.getConnection();

		try {
			for (int i = 0; i < topicdata.size(); i++) {

				ps = conn.prepareStatement("INSERT INTO mahoutldatopicmonth12tfidf (TopicID, ClusterTitle, Score) VALUES (?,?,?)");
				ps.setInt(1, topicdata.get(i).getTopicId());
				ps.setString(2, topicdata.get(i).getclustertitle());
				ps.setDouble(3, topicdata.get(i).getScore());

				ps.executeUpdate();
			}
			conn.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block

		}

	}

	//Retrieve index count for total number of clusters
	public ArrayList<Double> IndexCount(int numtopics) {
		conn = connection.getConnection();
		ArrayList<Double> scores = new ArrayList<Double>();
		int count = 0;
		double score = 0;

		try {
			for (int i = 0; i < numtopics; i++) {

				ps = conn.prepareStatement("Select count(*) From mahoutldaclustermonth12tfidf Where TopicID = " + i);

				rs = ps.executeQuery();
				while (rs.next()) {
					count = rs.getInt(1);
				}
				// score=(1662/count);
				score = count;
				scores.add(score);
				System.out.println(score);
			}
			conn.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
		}

		return scores;

	}
	
	//Retrieve the respective postIDs from the database
	public void GetdataId(ArrayList<String> dataid) {

		conn = connection.getConnection();

		try {
			

				ps = conn.prepareStatement("Select idData from mahoutldaclustermonth12tfidf");
		
				rs=ps.executeQuery();

				while(rs.next()){
					dataid.add(rs.getString("idData"));
				}
			conn.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block

		}
	}
}
