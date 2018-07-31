package Vectors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RetrieveVectorsKmeans {		

	//Match cluster number to respective posts in the list
	public ArrayList<String> assignClusterNum(ArrayList<String> postIDList, ArrayList<String> temp) {
		ArrayList<String> clusteredPosts = new ArrayList<String>();
		
		for (int i = 0; i < postIDList.size(); i++) {
			for (int j = 0; j < temp.size(); j++) {
				String postID = temp.get(j).substring(0, temp.get(j).indexOf(":"));
				
				if (postID.equals(postIDList.get(i))) {
					clusteredPosts.add(temp.get(j));
				}
			}
		}
		return clusteredPosts;
	}
	
	//Get the post IDs of the posts that have no vectors (for omitting them)
	public ArrayList<String> getPostsWithNoVectors(String fileName) {
		ArrayList<String> result = new ArrayList<String>();
		String postVector = "";

		try {
			File file = new File(fileName);
			FileReader fr = new FileReader(file.getAbsoluteFile());
			BufferedReader br = new BufferedReader(fr);
			
			//REGULAR EXPRESSION
			Pattern p = Pattern.compile("\\d*.txt: Value: \\{\\}");

			while ((postVector = br.readLine()) != null) {
				Matcher m = p.matcher(postVector);

				if (m.find()) {
					String temp = m.group(0);
					
					temp = temp.substring(0, temp.indexOf(".txt"));
					
					result.add(temp);
				}
			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return result;
	}
	
	//Retrieve the post IDs from the vectors file
	public ArrayList<String> retrievePostIDs(String fileName, ArrayList<String> omittedList) {
		ArrayList<String> postIDList = new ArrayList<String>();
		String dataid = "";

		try {
			File file = new File(fileName);
			FileReader fr = new FileReader(file.getAbsoluteFile());
			BufferedReader br = new BufferedReader(fr);
			
			//REGULAR EXPRESSION
			Pattern p = Pattern.compile("\\d*(?=.txt)");
			
			while ((dataid = br.readLine()) != null) {

				Matcher m = p.matcher(dataid);

				if (m.find()) {
					String temp = m.group(0);
					boolean isOmitted = false;

					for (int i = 0; i < omittedList.size(); i++) {
						if (temp.equals(omittedList.get(i))) {
							isOmitted = true;
						}
					}
					
					if (isOmitted != true) 
						postIDList.add(temp);
				}
			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return postIDList;
	}

	//Retrieve the post vectors from the vectors file
	public ArrayList<String> retrievePostVectors(String fileName) {
		ArrayList<String> postVectorList = new ArrayList<String>();
		String postVector = "";

		try {
			File file = new File(fileName);
			FileReader fr = new FileReader(file.getAbsoluteFile());
			BufferedReader br = new BufferedReader(fr);
			
			//REGULAR EXPRESSION
			Pattern p = Pattern.compile("(?<=\\{).*?(?=\\})");

			while ((postVector = br.readLine()) != null) {
				Matcher m = p.matcher(postVector);

				if (m.find()) {
					String temp = m.group(0);
					if (!temp.equals(""))
						postVectorList.add(temp);
				}
			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return postVectorList;
	}

	//Sort the post vectors, to match correct dimensions for each vector
	public ArrayList<Double> sortPostVectors(String vector) {
		ArrayList<String> tempList = new ArrayList<String>();
		ArrayList<Double> sortedVectorsList = new ArrayList<Double>();
		
		String[] vectorsList = vector.split(",");
		
		for (int i = 0; i < vectorsList.length; i++) {
			tempList.add(vectorsList[i]);
		}
		
		Collections.sort(tempList);
		
		for (int i = 0; i < tempList.size(); i++) {
			String temp = tempList.get(i);
						
			String[] temp2 = temp.split(",");
			double temp3 = 0;
			
			for (int j = 0; j < temp2.length; j++) {
				temp3 = Double.parseDouble(temp2[j].substring(temp2[j].indexOf(":") + 1, temp2[j].length()));
				//System.out.println(temp2[j].substring(temp2[j].indexOf(":") + 1, temp2[j].length()));
			}
			
			sortedVectorsList.add(temp3);
		}
		
		return sortedVectorsList;
	}

	//Retrieve the number of dimensions existing
	public int retrieveNumOfDimensions(String fileName) {
		int numOfDim = 0;
		String line;

		try {
			File file = new File(fileName);
			FileReader fr = new FileReader(file.getAbsoluteFile());
			BufferedReader br = new BufferedReader(fr);
			
			//REGULAR EXPRESSION
			Pattern p = Pattern.compile("Count: \\d*");

			while ((line = br.readLine()) != null) {

				Matcher m = p.matcher(line);

				if (m.find()) {
					String temp; 
					temp = m.group(0);
					temp = temp.replace("Count: ", "");
					numOfDim = Integer.parseInt(temp);
				}
			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return numOfDim;
	}
	
	//Fill up the vector values that are supposed to be 0.0
	public String fillUpVectors(String vector, int numOfDims) {				
		ArrayList<Integer> existingVectorValues = new ArrayList<Integer>();
		String[] vectorSplit = vector.split(",");
		
		for (int i = 0; i < vectorSplit.length; i++) {
			int temp = Integer.parseInt(vectorSplit[i].substring(0, vectorSplit[i].indexOf(":")));
			existingVectorValues.add(temp);
		}
						
		for (int i = 0; i < numOfDims; i++) {
			boolean exists = false;

			for (int j = 0; j < existingVectorValues.size(); j++) {				
				if (existingVectorValues.get(j) == i)
					exists = true;
			}
			
			if (exists == false) {
				String temp = "," + i + ":0.0";
				vector += temp;
			}
		}		
		return vector;
	}
}
