package MainProgram;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import Metrics.CalcS_Dbw;
import Metrics.Cluster;
import Metrics.ClusterPoint;
import Vectors.DBControllerKmeans;
import Vectors.RetrieveVectorsKmeans;

public class MainKMeansMth12 {
	public static void main(String[] args) {
		MainKMeansMth12 main = new MainKMeansMth12();

		// Location of the vectors of KMeans for 1 month
		//String KmeansMonth1 = "C:/Users/L335A14/Desktop/KmeansTFIDFVectors/Kmeansmonth1vectorsTFIDF.txt";
		// Location of the dictionary file of the vectors of KMeans for 1 month
		//String KmeansMonth1Dict = "C:/Users/L335A14/Desktop/KmeansTFIDFVectors/Kmeansmonth1vectorsTFIDFdict.txt";

		// Location of the vectors of KMeans for 6 month
		//String KmeansMonth6 = "C:/Users/L335A14/Desktop/KmeansTFIDFVectors/Kmeansmonth6vectorsTFIDF.txt";
		// Location of the dictionary file of the vectors of KMeans for 6 month
		//String KmeansMonth6Dict = "C:/Users/L335A14/Desktop/KmeansTFIDFVectors/Kmeansmonth6vectorsTFIDFdict.txt";

		// Location of the vectors of KMeans for 12 month
		String KmeansMonth12 = "C:/Users/L335A14/Desktop/KmeansTFIDFVectors/Kmeansmonth12vectorsTFIDF.txt";
		// Location of the dictionary file of the vectors of KMeans for 12 month
		String KmeansMonth12Dict = "C:/Users/L335A14/Desktop/KmeansTFIDFVectors/Kmeansmonth12vectorsTFIDFdict.txt";
		
		//System.out.println("KMeans Month 1 S_Dbw: ");
		//main.calcKMeansS_Dbw(KmeansMonth1, KmeansMonth1Dict, 1);
		
		//System.out.println("KMeans Month 6 S_Dbw: ");
		//main.calcKMeansS_Dbw(KmeansMonth6, KmeansMonth6Dict, 6);

		System.out.println("KMeans Month 12 S_Dbw: ");
		main.calcKMeansS_Dbw(KmeansMonth12, KmeansMonth12Dict, 12);
	}
	
	public void calcKMeansS_Dbw(String vectorsFile, String dictFile, int mth) {
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
		RetrieveVectorsKmeans rv = new RetrieveVectorsKmeans();
	
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
		
		//Initialize new DBControllerKmeans
		DBControllerKmeans db = new DBControllerKmeans();
		//Declare new ArrayList to store the cluster numbers; omitting the post IDs with empty vectors
		ArrayList<String> clusterNumList = null;		
		if (mth == 1) {
			clusterNumList = db.getClusterNumsMonth1(omittedList);	
		}
		else if (mth == 6) {
			clusterNumList = db.getClusterNumsMonth6(omittedList);	
		}
		else if (mth == 12) {
			clusterNumList = db.getClusterNumsMonth12(omittedList);	
		}
		
		//Declare new ArrayList to store the clustered post IDs; assigning the cluster numbers to the respective post IDs
		ArrayList<String> clusteredPostIDs = rv.assignClusterNum(postIDList, clusterNumList);
		
		
		/*PROCESS OF INTEGRATING ALL THE ABOVE INTO S_DBW*/
		//Declare new ArrayList to store the clusters 
		ArrayList<Cluster> clusterList = new ArrayList<Cluster>();
		
		//Declare new ArrayList to store the cluster numbers (for identifying which cluster the post belongs to)
		ArrayList<Integer> tempList = new ArrayList<Integer>();

		//2 for loops to iterate through each clustered post ID with assigned vectors and store into respective cluster
		for (int i = 0; i < clusteredPostIDs.size(); i++) {
			int clusterNum = Integer.parseInt(clusteredPostIDs.get(i).substring(clusteredPostIDs.get(i).indexOf(":") + 1, clusteredPostIDs.get(i).length()));

			if (!(tempList.contains(clusterNum))) {
				tempList.add(clusterNum);
			}
		}
		
		for (int i = 0; i < tempList.size(); i++) {
			Cluster c = new Cluster();
			
			for (int j = 0; j < clusteredPostIDs.size(); j++) {
				int clusterNum = Integer.parseInt(clusteredPostIDs.get(j).substring(clusteredPostIDs.get(j).indexOf(":") + 1, clusteredPostIDs.get(j).length()));

				if (tempList.get(i) == clusterNum) {
					ClusterPoint cp = new ClusterPoint();
					ArrayList<Double> vector = sortedVectorsList.get(clusteredPostIDs.indexOf(clusteredPostIDs.get(j)));
					cp.setVectors(vector);
					c.addToClusterPointsList(cp);
				}
			}
			
			clusterList.add(c);
		}
		
		// Test method to check if vectors correspond correctly to post id, to cluster
		/*for (int i = 0; i < clusteredPostIDs.size(); i++) {

			int clusterNum = Integer.parseInt(clusteredPostIDs.get(i).substring(clusteredPostIDs.get(i).indexOf(":") + 1, clusteredPostIDs.get(i).length()));

			if (clusterNum == 299) {
				System.out.println("PostID: " + clusteredPostIDs.get(i) + " | Index position : " + i);
			}			
		}
		
		int negativeCount = 0;

		for (int i = 0; i < numOfDims; i++) {
			double temp = clusterList.get(8).getClusterPointsList().get(9).getVector().get(i);
			double temp2 = (double) sortedVectorsList.get(277).get(i);

			if (temp != temp2) {
				negativeCount++;
			}
		}
		
		System.out.println(negativeCount);*/
		
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
