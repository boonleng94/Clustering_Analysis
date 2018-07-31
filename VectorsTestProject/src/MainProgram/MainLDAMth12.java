package MainProgram;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import Metrics.CalcS_Dbw;
import Metrics.Cluster;
import Metrics.ClusterPoint;
import Vectors.DBControllerLDA;
import Vectors.RetrieveVectorsLDA;

public class MainLDAMth12 {
	public static void main(String[] args) {
		MainLDAMth12 main = new MainLDAMth12();
	
		//Location of the TF vectors of LDA for 1 month
		//String tfVectorsFileMth1 = "C:/Users/L335A14/Desktop/LDATFVectors/LDAmonth1vectorsTF.txt";
		//Location of the dictionary file of the TF vectors of LDA for 1 month
		//String tfDictFileMth1 = "C:/Users/L335A14/Desktop/LDATFVectors/LDAmonth1vectorsTFDict.txt";
		
		//Location of the TF vectors of LDA for 6 month
		//String tfVectorsFileMth6 = "C:/Users/L335A14/Desktop/LDATFVectors/LDAmonth6vectorsTF.txt";
		//Location of the dictionary file of the TF vectors of LDA for 6 month
		//String tfDictFileMth6 = "C:/Users/L335A14/Desktop/LDATFVectors/LDAmonth6vectorsTFDict.txt";
		
		//Location of the TF vectors of LDA for 12 month
		String tfVectorsFileMth12 = "C:/Users/L335A14/Desktop/LDATFVectors/LDAmonth12vectorsTF.txt";
		//Location of the dictionary file of the TF vectors of LDA for 12 month
		String tfDictFileMth12 = "C:/Users/L335A14/Desktop/LDATFVectors/LDAmonth12vectorsTFDict.txt";

		/*System.out.println("LDA with TF Month 1 S_Dbw: ");
		main.calcLDAS_Dbw(tfVectorsFileMth1, tfDictFileMth1, 1, 0);
		
		System.out.println("LDA with TF Month 6 S_Dbw: ");
		main.calcLDAS_Dbw(tfVectorsFileMth6, tfDictFileMth6, 6, 0);*/

		System.out.println("LDA with TF Month 12 S_Dbw: ");
		main.calcLDAS_Dbw(tfVectorsFileMth12, tfDictFileMth12, 12, 0);
	}
	
	public void calcLDAS_Dbw(String vectorsFile, String dictFile, int mth, int tfOrTfidf) {
		/*PROCESS OF RETRIEVING AND FORMATTING OUTPUT PRODUCED BY MAHOUT*/
		//Initialize new ArrayList to store the post IDs
		ArrayList<String> postIDList = new ArrayList<String>();
		//Intialize new ArrayList to store the corresponding post vectors
		ArrayList<String> postVectorList = new ArrayList<String>();
		//Intialize new ArrayList to store the actual sorted vectors (after filling up)
		ArrayList<ArrayList<Double>> sortedVectorsList = new ArrayList<ArrayList<Double>>();
		//Variable to store the number of dimensions of the vector
		int numOfDims;
		//Variable to store start time of the method
		long startTime = System.currentTimeMillis();
		System.out.println("Start time: " + new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
		
		//Intialize new RetrieveVectors class to format the vectors
		RetrieveVectorsLDA rv = new RetrieveVectorsLDA();
		
		//Intialize new ArrayList to store the omitted post IDs with empty vectors
		ArrayList<String> omittedList = rv.getPostsWithNoVectors(vectorsFile);
		//Store the post IDs into the ArrayList after omitting posts with empty vectors
		postIDList = rv.retrievePostIDs(vectorsFile, omittedList);
		//Store the post vectors into the ArrayList after omitting empty vectors
		postVectorList = rv.retrievePostVectors(vectorsFile);
				
		//Store number of dimensions into the variable
		numOfDims = rv.retrieveNumOfDimensions(dictFile);
		
		//Fill up empty dimensions of the vectors and sort accordingly 
		for (int i = 0; i < postVectorList.size(); i++) {
			String actualVector = rv.fillUpVectors(postVectorList.get(i), numOfDims);
			sortedVectorsList.add(rv.sortPostVectors(actualVector));
		}
		
		//Initialize new DBControllerLDA
		DBControllerLDA db = new DBControllerLDA();
		//Declare new ArrayList to store the cluster numbers; omitting the post IDs with empty vectors
		ArrayList<String> clusterNumList = null;
		if (mth == 1 && tfOrTfidf == 0) {
			clusterNumList = db.getClusterNumsMonth1TF(omittedList);	
		}
		else if (mth == 6 && tfOrTfidf == 0) {
			clusterNumList = db.getClusterNumsMonth6TF(omittedList);	
		}
		else if (mth == 12 && tfOrTfidf == 0) {
			clusterNumList = db.getClusterNumsMonth12TF(omittedList);	
		}
		else if (mth == 1 && tfOrTfidf == 1) {
			clusterNumList = db.getClusterNumsMonth1TFIDF(omittedList);	
		}
		else if (mth == 6 && tfOrTfidf == 1) {
			clusterNumList = db.getClusterNumsMonth6TFIDF(omittedList);	
		}
		else if (mth == 12 && tfOrTfidf == 1) {
			clusterNumList = db.getClusterNumsMonth12TFIDF(omittedList);	
		}
		
		//Declare new ArrayList to store the clustered post IDs; assigning the cluster numbers to the respective post IDs
		ArrayList<String> clusteredPostIDs = rv.assignClusterNum(postIDList, clusterNumList);
		
		
		/*PROCESS OF INTEGRATING ALL THE ABOVE INTO S_DBW*/
		//Intialize new integer variable to store the number of clusters 
		int noOfClusters = rv.getNoOfClusters(clusterNumList);
		
		//Declare new ArrayList to store the clusters 
		ArrayList<Cluster> clusterList = new ArrayList<Cluster>();

		//For loop to iterate through each clustered post ID with assigned vectors and store into respective cluster
		//VERIFIED THAT VECTORS CORRESPOND CORRECTLY TO POST ID, CORRESPONDS CORRECTLY TO CLUSTER
		for (int i = 0; i < noOfClusters; i++) {
			//Intialize new Cluster class
			Cluster c = new Cluster();
			
			for (int j = 0; j < clusteredPostIDs.size(); j++) {
				//Get cluster number of that particular postID
				int clusterNum = Integer.parseInt(clusteredPostIDs.get(j).substring(clusteredPostIDs.get(j).indexOf(":") + 1, clusteredPostIDs.get(j).length()));
				
				//If the cluster number matches the current cluster, store its respective vector into the cluster point, store into the cluster
				if (clusterNum == i) {
					ClusterPoint cp = new ClusterPoint();
					ArrayList<Double> vector = sortedVectorsList.get(clusteredPostIDs.indexOf(clusteredPostIDs.get(j)));
					cp.setVectors(vector);
					c.addToClusterPointsList(cp);
				}
			}
			clusterList.add(c);
		}		
		
		/*System.out.println("Clustered post IDs: ");
		
		for (int i = 0; i < clusteredPostIDs.size(); i++) {
			System.out.println(clusteredPostIDs.get(i));
		}
		
		System.out.println("Omitted List: ");

		for (int i = 0; i < omittedList.size(); i++) {
			System.out.println(omittedList.get(i));
		}*/
		
		// Test method to check if vectors correspond correctly to post id, to cluster
		/*for (int i = 0; i < clusteredPostIDs.size(); i++) {

			int clusterNum = Integer.parseInt(clusteredPostIDs.get(i).substring(clusteredPostIDs.get(i).indexOf(":") + 1, clusteredPostIDs.get(i).length()));

			if (clusterNum == 1) {
				System.out.println("PostID: " + clusteredPostIDs.get(i) + " | Index position : " + i);
			}			
		}
		
		int negativeCount = 0;
		
		for (int i = 0; i < numOfDims; i++) {
			double temp = clusterList.get(0).getClusterPointsList().get(0).getVector().get(i);
			double temp2 = (double) sortedVectorsList.get(274).get(i);

			if (temp != temp2) {
				negativeCount++;
			}
		}*/
		
		//Actual class of the S_Dbw index value calculation
		CalcS_Dbw t1 = new CalcS_Dbw();
		double s_dbw = t1.calcS_Dbw(clusterList);
		System.out.println("Calculated S_Dbw:" + s_dbw);
		
		//Variable to store start time of the method
		long endTime = System.currentTimeMillis();
		System.out.println("End time: " + new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
		System.out.println("Time taken: " + (endTime - startTime) + "\n\n");
	}
}
