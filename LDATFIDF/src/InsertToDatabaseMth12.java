
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Controller.Document;
import Controller.Topic;

public class InsertToDatabaseMth12 {

	static ArrayList<Topic> topicdata = new ArrayList<Topic>();
	static ArrayList<Document> cluster = new ArrayList<Document>();
	static ArrayList<String> dataids = new ArrayList<String>();
	static ArrayList<String> topicids = new ArrayList<String>();
	static ArrayList<String> probs = new ArrayList<String>();
	static ArrayList<Double> scores = new ArrayList<Double>();

	public static void main(String[] agrs) {
		InsertToDatabaseMth12 c = new InsertToDatabaseMth12();
        //Use to get the cluster mapping
		c.getDataID();
		c.getProb();
		c.getCluster();

		System.err.println(dataids.size());
		System.err.println(topicids.size());
		
		if (dataids.size() == topicids.size()) {
			for (int i = 0; i < topicids.size(); i++) {
				cluster.add(new Document(Integer.parseInt((dataids.get(i))), Integer.parseInt(topicids.get(i))));
			}

			SQLControllerMth12 database = new SQLControllerMth12();
            
			//Insert Cluster mapping to database
			database.InsertCluster(cluster);
            
			//Get the Score of each topic 
			c.Score(50);
			System.err.println(scores.size());
			
			//Use to get the topic names 
			c.getTopics(50);
			System.err.println(topicdata.size());
			
			//Inserts the score, topicid and the topic name to the database
			database.InsertTopic(topicdata);
		}
	}

	//Extract the generated topics out and store into an ArrayList
	public void getTopics(int num) {
		for (int topicnum = 0; topicnum < num; topicnum++) {
			try {
				String ctext = "";
				String term = "";
				File file = new File("C:/Users/L335a14/Desktop/Cluster/LDA (with tf-idf)/month12/results/topic_" + topicnum);
				FileReader fw = new FileReader(file.getAbsoluteFile());
				BufferedReader bw = new BufferedReader(fw);
				//REGULAR EXPERSSION 
				Pattern p = Pattern.compile("\\w*\\s");
				term = "";
				while ((ctext = bw.readLine()) != null) {

					Matcher m = p.matcher(ctext);

					if (m.find()) {
						System.out.println(m.group(0));
						term += m.group(0);
					}

				}
				topicdata.add(new Topic(topicnum, term, scores.get(topicnum)));
				bw.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	//Extract the postIDs out and store into an ArrayList
	public void getDataID() {
		String dataid = "";

		try {
			File file = new File("C:/Users/L335a14/Desktop/Cluster/LDA (with tf-idf)/month12/doctopic.txt");
			FileReader fw = new FileReader(file.getAbsoluteFile());
			BufferedReader bw = new BufferedReader(fw);
			//REGUALR EXPERSSION
			Pattern p = Pattern.compile("\\d*(?=.txt)");

			while ((dataid = bw.readLine()) != null) {

				Matcher m = p.matcher(dataid);

				if (m.find()) {
					//System.out.println(m.group(0));
					dataids.add(m.group(0));
				}

			}

			bw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	//Extract the probability value of the best cluster of the postID and store into an ArrayList
	public void getProb() {
		String prob;
		String one = "";
		try {
			File file = new File("C:/Users/L335a14/Desktop/Cluster/LDA (with tf-idf)/month12/doctopic.txt");
			FileReader fw = new FileReader(file.getAbsoluteFile());
			BufferedReader bw = new BufferedReader(fw);
			//REGUALR EXPERSSION
			Pattern p2 = Pattern.compile("(?<=:).*(?=})");

			while ((prob = bw.readLine()) != null) {

				Matcher m = p2.matcher(prob);

				if (m.find()) {
					one = m.group(0);
					//System.out.println(one);
					probs.add(one);
				}

			}
			bw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	//Extract the cluster number of the postID based on the extracted probability value
	public void getCluster() {
		String topicid;
		String TopicID2 = "";
		String TopicID3 = "";
		try {
			File file1 = new File("C:/Users/L335a14/Desktop/Cluster/LDA (with tf-idf)/month12/docstopics.txt");
			FileReader fw1 = new FileReader(file1.getAbsoluteFile());
			BufferedReader bw1 = new BufferedReader(fw1);

			try {

				for (int t = 0; t < probs.size(); t++) {
					//REGUALR EXPERSSION
					Pattern p = Pattern.compile(probs.get(t));
					int count = 0;

					while ((topicid = bw1.readLine()) != null) {
						// System.out.println(topicid);
						Matcher m2 = p.matcher(topicid);

						if (m2.find()) {
							count = m2.start();
							// System.out.println(m2.start());
							int counter = 0;

							for (int i = count; i < topicid.length(); i--) {
								if (i > 0) {
									if (topicid.charAt(i) == ',') {
										counter = 1;
									}
									if (counter != 1) {
										TopicID2 += topicid.charAt(i);
									} else
										i = topicid.length() + 1;
								} else
									break;
							}
							for (int i = TopicID2.length() - 1; i >= 0; i--) {
								if (TopicID2.charAt(i) == ':') {

								} else if (i == 0) {

								}

								else if (TopicID2.charAt(i) == '{') {
									TopicID3 = "0";
									break;
								}

								else {
									TopicID3 += TopicID2.charAt(i);
								}
							}
							topicids.add(TopicID3);
							System.out.println(TopicID3);
							TopicID2 = "";
							TopicID3 = "";
							break;
						}
					}
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bw1.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void Score(int topicnum) {
		SQLControllerMth12 Index = new SQLControllerMth12();
		scores = Index.IndexCount(topicnum);
	}
}
