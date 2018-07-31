package Metrics;

import java.util.ArrayList;

public class CalcS_Dbw {
	//Scatt
	//Variance of a Cluster
	private ArrayList<Double> calcClusterVar(Cluster cluster) {
		ArrayList<Double> result = new ArrayList<Double>();
		ArrayList<ClusterPoint> clusterPoints = cluster.getClusterPointsList();
		int p = 0;
		
		for (int i = 0; i < clusterPoints.size(); i++) {
			ClusterPoint cp = clusterPoints.get(i);
			ArrayList<Double> vector = cp.getVector();
			p = vector.size();
		}
		
		for (int i = 0; i < p; i++) {
			result.add(calcClusterVarForDimP(cluster, i));
		}
		
		return result;
	}
	
	//Variance of a Cluster based on Dimension p
	private double calcClusterVarForDimP(Cluster cluster, int p) {
		double result = 0; 
		ArrayList<ClusterPoint> clusterPoints = cluster.getClusterPointsList();
		double n = clusterPoints.size();
		double f1 = 1 / n;
				
		ArrayList<Double> clusterCentroid = calcClusterCentroid(cluster);
		double v = clusterCentroid.get(p);
				
		for (int k = 0; k < n; k++) {
			ClusterPoint cp = clusterPoints.get(k);
			ArrayList<Double> vector = cp.getVector();
			double x = vector.get(p);
			double temp = x - v;
			temp = temp*temp;
			result += temp;
		}
		
		result = result * f1;

		return result;
	}
	
	//Vector of the Cluster centroid
	public ArrayList<Double> calcClusterCentroid(Cluster cluster) {
		ArrayList<Double> clusterCentroid = new ArrayList<Double>();
		ArrayList<ClusterPoint> clusterPoints = cluster.getClusterPointsList();
		int dims = 0;
				
		for (int i = 0; i < clusterPoints.size(); i++) {
			ClusterPoint cp = clusterPoints.get(i);
			ArrayList<Double> vector = cp.getVector();
			dims = vector.size();
		}
		
		for (int i = 0; i < dims; i++) {
			clusterCentroid.add(0.0);
		}

		for (int i = 0; i < clusterPoints.size(); i++) {
			ClusterPoint cp = clusterPoints.get(i);
			ArrayList<Double> vector = cp.getVector();
			
			for (int j = 0; j < vector.size(); j++) {
				double temp = clusterCentroid.get(j);
				temp += vector.get(j);
				clusterCentroid.remove(j);
				clusterCentroid.add(j, temp);
			}
		}
		
		for (int i = 0; i < clusterCentroid.size(); i++) {
			double temp = clusterCentroid.get(i);
			temp = temp / clusterPoints.size();
			clusterCentroid.remove(i);
			clusterCentroid.add(i, temp);
		}
		
		return clusterCentroid;
	}
	
	// Vector of the Cluster centroid
	public ArrayList<Double> calcClusterCentroidFormatted(Cluster cluster) {
		ArrayList<Double> clusterCentroid = new ArrayList<Double>();
		ArrayList<ClusterPoint> clusterPoints = cluster.getClusterPointsList();
		int dims = 0;

		for (int i = 0; i < clusterPoints.size(); i++) {
			ClusterPoint cp = clusterPoints.get(i);
			ArrayList<Double> vector = cp.getVector();
			dims = vector.size();
		}

		for (int i = 0; i < dims; i++) {
			clusterCentroid.add(0.0);
		}

		for (int i = 0; i < clusterPoints.size(); i++) {
			ClusterPoint cp = clusterPoints.get(i);
			ArrayList<Double> vector = cp.getVector();

			for (int j = 0; j < vector.size(); j++) {
				double temp = clusterCentroid.get(j);
				temp += vector.get(j);
				clusterCentroid.remove(j);
				clusterCentroid.add(j, temp);
			}
		}

		for (int i = 0; i < clusterCentroid.size(); i++) {
			double temp = clusterCentroid.get(i);
			temp = temp / clusterPoints.size();
			clusterCentroid.remove(i);
			clusterCentroid.add(i, temp);
		}

		return clusterCentroid;
	}
	
	//Variance of the Dataset
	private ArrayList<Double> calcDatasetVar(ArrayList<ClusterPoint> vectorPoints) {
		ArrayList<Double> result = new ArrayList<Double>();
		int p = 0;
		
		for (int i = 0; i < vectorPoints.size(); i++) {
			ClusterPoint cp = vectorPoints.get(i);
			ArrayList<Double> vector = cp.getVector();
			p = vector.size();
		}
		
		for (int i = 0; i < p; i++) {
			result.add(calcDatasetVarForDimP(vectorPoints, i));
		}
		
		return result;
	}
	
	//Variance of the Dataset based on Dimension p
	private double calcDatasetVarForDimP(ArrayList<ClusterPoint> vectorPoints, int p) {
		double result = 0; 
		double n = vectorPoints.size();
		double f1 = 1 / n;
		
		ArrayList<Double> datasetMean = calcMeanOfDataSetPoints(vectorPoints);
		double v = datasetMean.get(p);
				
		for (int k = 0; k < n; k++) {
			ClusterPoint cp = vectorPoints.get(k);
			ArrayList<Double> vector = cp.getVector();
			double x = vector.get(p);
			double temp = x - v;
			temp = temp*temp;
			result += temp;
			
		}

		result = result * f1;
		
		return result;
	}
	
	//Mean of the dataset vectors
	ArrayList<Double> calcMeanOfDataSetPoints(ArrayList<ClusterPoint> vectorPoints) {
		ArrayList<Double> result = new ArrayList<Double>();
		int dims = 0;
		
		for (int i = 0; i < vectorPoints.size(); i++) {
			ClusterPoint cp = vectorPoints.get(i);
			ArrayList<Double> vector = cp.getVector();
			dims = vector.size();
		}
		
		for (int i = 0; i < dims; i++) {
			result.add(0.0);
		}
		
		for (int i = 0; i < vectorPoints.size(); i++) {
			ClusterPoint cp = vectorPoints.get(i);
			ArrayList<Double> vector = cp.getVector();
			
			for (int j = 0; j < vector.size(); j++) {
				double temp = result.get(j);
				temp += vector.get(j);
				result.remove(j);
				result.add(j, temp);
			}
		}
		
		for (int i = 0; i < result.size(); i++) {
			double temp = result.get(i);
			temp = temp / vectorPoints.size();
			result.remove(i);
			result.add(i, temp);
		}
		
		return result;
	}
	
	//Intra-cluster variance (Scattering of cluster)
	public double calcScatt(ArrayList<Cluster> clusterList) {
		double n = clusterList.size();
		double f1 = 1 / n;
		double f2 = 0;
		ArrayList<Double> datasetVar = new ArrayList<Double>();
		ArrayList<ClusterPoint> vectorPoints = new ArrayList<ClusterPoint>();
		ArrayList<Double> tempDatasetVarList = new ArrayList<Double>();
		double normedDatasetVar = 0;
		double result = 0;
		double temp = 0;
		double temp2 = 0;
		double normedClusterVar = 0;
		
		for (int i = 0; i < clusterList.size(); i++) {
			Cluster c = clusterList.get(i);
			ArrayList<ClusterPoint> clusterPoints = c.getClusterPointsList();
			
			for (int j = 0; j < clusterPoints.size(); j++) {
				vectorPoints.add(clusterPoints.get(j));
			}
		}
		
		datasetVar = calcDatasetVar(vectorPoints);
		
		for (int i = 0; i < datasetVar.size(); i++) {
			double tempA = datasetVar.get(i);
			tempA = tempA * tempA;
			tempDatasetVarList.add(tempA);
		}
		
		for (int i = 0; i < tempDatasetVarList.size(); i++) {
			temp += tempDatasetVarList.get(i);
		}
		
		normedDatasetVar = Math.sqrt(temp);
				
		for (int i = 0; i < n; i++) {
			ArrayList<Double> clusterVar = calcClusterVar(clusterList.get(i));
			ArrayList<Double> tempClusterVarList = new ArrayList<Double>();
			
			for (int j = 0; j < clusterVar.size(); j++) {
				double temp3 = clusterVar.get(j); 
				temp3 = temp3 * temp3;
				tempClusterVarList.add(temp3);
			}
			
			for (int k = 0; k < tempClusterVarList.size(); k++) {
				temp2 += tempClusterVarList.get(k);
			}			
				
			normedClusterVar = Math.sqrt(temp2);
					
			f2 += normedClusterVar;
		}
			
		result = (f1 * f2) / normedDatasetVar;
		
		return result;
	}

	//Average standard deviation of clusters
	public double calcStdev(ArrayList<Cluster> clusterList) {
		double result = 0;
		double c = clusterList.size();
		double f1 = 1 / c;
		double f2 = 0;
		double normedClusterVar = 0;
		double temp = 0;

		for (int i = 0; i < c; i++) {
			ArrayList<Double> clusterVar = calcClusterVar(clusterList.get(i));
			ArrayList<Double> tempClusterVarList = new ArrayList<Double>();
						
			for (int j = 0; j < clusterVar.size(); j++) {
				double temp2 = clusterVar.get(j);
				temp2 = temp2 * temp+2;
				tempClusterVarList.add(temp2);
			}
						
			for (int k = 0; k < tempClusterVarList.size(); k++) {
				temp += tempClusterVarList.get(k);
			}
			
			normedClusterVar = Math.sqrt(temp);
			
			f2 += normedClusterVar;
		}
		
		result = f1 * Math.sqrt(f2);
		return result;
	}
	
	//Dens_bw 
	//Vector of centroid of Cluster U
	private ArrayList<Double> calcUVector(Cluster i, Cluster j) {
		ArrayList<Double> result = new ArrayList<Double>();
		ArrayList<Double> centreI = calcClusterCentroid(i);
		ArrayList<Double> centreJ = calcClusterCentroid(j);
		
		Cluster c = new Cluster(); 
		ClusterPoint cp1 = new ClusterPoint();
		cp1.setVectors(centreI);		
		ClusterPoint cp2 = new ClusterPoint();
		cp2.setVectors(centreJ);
		c.addToClusterPointsList(cp1);
		c.addToClusterPointsList(cp2);
		
		result = calcClusterCentroid(c);
		
		return result;
	}
	
	//Function XU
	private int functionXU(ArrayList<Double> x, ArrayList<Double> u, double stDev) {
		int result = 1;
		ArrayList<Double> tempList = new ArrayList<Double>();
		double value = 0;
		
		for (int dim = 0; dim < x.size(); dim++) {
			double temp = x.get(dim) - u.get(dim);
			temp = temp*temp;
			tempList.add(temp);
		}
		
		for (int k = 0; k < tempList.size(); k++) {
			value += tempList.get(k);
		}
		
		value = Math.sqrt(value);
		
		if (value > stDev) {
			result = 0;
		}
		else {
			result = 1;
		}
		
		return result;
	}
	
	//Density of Uij
	private double calcDensityUij(Cluster i, Cluster j, double stDev) {
		double result = 0;
		ArrayList<ClusterPoint> clusterPointsI = i.getClusterPointsList();
		ArrayList<ClusterPoint> clusterPointsJ = j.getClusterPointsList();
		double numOfClusterPointsI = clusterPointsI.size();
		double numOfClusterPointsJ = clusterPointsJ.size();
		ArrayList<Double> uVector = calcUVector(i, j);
				
		for (int l = 0; l < numOfClusterPointsI; l++) {
			result += functionXU(clusterPointsI.get(l).getVector(), uVector, stDev);
		}
		
		for (int l = 0; l < numOfClusterPointsJ; l++) {
			result += functionXU(clusterPointsJ.get(l).getVector(), uVector, stDev);
		}
		
		return result; 
	}
	
	//Density of single cluster
	private double calcDensity(Cluster i, double stDev) {
		double result = 0;
		ArrayList<ClusterPoint> clusterPointsI = i.getClusterPointsList();
		ArrayList<Double> centreI = calcClusterCentroid(i);
		double numOfClusterPointsI = clusterPointsI.size();
		
		for (int l = 0; l < numOfClusterPointsI; l++) {
			result += functionXU(clusterPointsI.get(l).getVector(), centreI, stDev);
		}
		
		return result;
	}

	//Inter-cluster variance (Dens_bw)
	public double calcDens_bw(ArrayList<Cluster> clusterList) {
		double dens_bw = 0;
		double temp = 0;
		double f2 = 0;
		double c = clusterList.size();
		double f1 = 1 / (c*(c - 1));
		
		System.out.println(c);
		
		double stDev = calcStdev(clusterList);
						
		for (int i = 0; i < c; i++) {
			for (int j = i+1; j < c; j++) {
				if(i != j) {
					double densityUij = calcDensityUij(clusterList.get(i), clusterList.get(j), stDev);
					double densityVi = calcDensity(clusterList.get(i), stDev);
					double densityVj = calcDensity(clusterList.get(j), stDev);
					double max = 0;

					if (densityVi > max) {
						max = densityVi;
					}
					else if (densityVj > max) {
						max = densityVj;
					}
					
					temp = densityUij / max;
					f2 += temp;
				}				
			}	
		}
		dens_bw = f1 * f2;
		
		return dens_bw;
	}

	public double calcS_Dbw(ArrayList<Cluster> clusterList) {
		double dens_bw = calcDens_bw(clusterList);
		double scatt = calcScatt(clusterList);
		double result = scatt + dens_bw;
		
		System.out.println("Calculated Dens_Bw value: " + dens_bw);
		
		System.out.println("Calculated Scatt value: " + scatt);
		
		return result;
	}
}
