import java.text.DecimalFormat;
import java.util.ArrayList;


public class MainTestClass {
	static DecimalFormat df = new DecimalFormat("####0.00");
	
	public static void main(String[] args) {
		MainTestClass t1 = new MainTestClass();
		
		Cluster cluster1 = new Cluster();
		ClusterPoint cp1 = new ClusterPoint();
		ClusterPoint cp2 = new ClusterPoint();
		ClusterPoint cp3 = new ClusterPoint();
		ArrayList<Double> vector1 = new ArrayList<Double>();
		vector1.add(1.0);
		vector1.add(2.0);
		vector1.add(3.0);		
		ArrayList<Double> vector2 = new ArrayList<Double>();
		vector2.add(1.1);
		vector2.add(2.1);
		vector2.add(3.1);		
		ArrayList<Double> vector3 = new ArrayList<Double>();
		vector3.add(1.2);
		vector3.add(2.2);
		vector3.add(3.2);
		cp1.setVectors(vector1);
		cp2.setVectors(vector2);
		cp3.setVectors(vector3);
		cluster1.addToClusterPointsList(cp1);
		cluster1.addToClusterPointsList(cp2);
		cluster1.addToClusterPointsList(cp3);
		
		Cluster cluster2 = new Cluster();
		ClusterPoint cp4 = new ClusterPoint();
		ClusterPoint cp5 = new ClusterPoint();
		ClusterPoint cp6 = new ClusterPoint();
		ArrayList<Double> vector4 = new ArrayList<Double>();
		vector4.add(2.0);
		vector4.add(3.0);
		vector4.add(4.0);		
		ArrayList<Double> vector5 = new ArrayList<Double>();
		vector5.add(3.0);
		vector5.add(4.2);
		vector5.add(5.3);		
		ArrayList<Double> vector6 = new ArrayList<Double>();
		vector6.add(5.6);
		vector6.add(9.7);
		vector6.add(1.8);
		cp4.setVectors(vector4);
		cp5.setVectors(vector5);
		cp6.setVectors(vector6);
		cluster2.addToClusterPointsList(cp4);
		cluster2.addToClusterPointsList(cp5);
		cluster2.addToClusterPointsList(cp6);
		
		/*Cluster cluster3 = new Cluster();
		ClusterPoint cp7 = new ClusterPoint();
		ClusterPoint cp8 = new ClusterPoint();
		ClusterPoint cp9 = new ClusterPoint();
		ArrayList<Double> vector7 = new ArrayList<Double>();
		vector7.add(2.0);
		vector7.add(3.0);
		vector7.add(4.0);		
		ArrayList<Double> vector8 = new ArrayList<Double>();
		vector8.add(3.0);
		vector8.add(4.0);
		vector8.add(5.0);		
		ArrayList<Double> vector9 = new ArrayList<Double>();
		vector9.add(5.0);
		vector9.add(9.0);
		vector9.add(1.0);
		cp7.setVectors(vector4);
		cp8.setVectors(vector5);
		cp9.setVectors(vector6);
		cluster3.addToClusterPointsList(cp7);
		cluster3.addToClusterPointsList(cp8);
		cluster3.addToClusterPointsList(cp9);*/
		
		ArrayList<Cluster> clusterList = new ArrayList<Cluster>();
		clusterList.add(cluster1);
		clusterList.add(cluster2);
		//clusterList.add(cluster3);
		
		ArrayList<ClusterPoint> vectorPoints = new ArrayList<ClusterPoint>();
		vectorPoints.add(cp1);
		vectorPoints.add(cp2);
		vectorPoints.add(cp3);
		vectorPoints.add(cp4);
		vectorPoints.add(cp5);
		vectorPoints.add(cp6);
		
		ArrayList<Double> datasetMean = t1.calcMeanOfDataSetPoints(vectorPoints);
		System.out.println("Dataset Mean: " + datasetMean + "\n");
		
		ArrayList<Double> varData = t1.calcDatasetVar(vectorPoints);
		System.out.println("Variance of dataset : " + varData + "\n");
		
		ArrayList<Double> clusterCentroid1 = t1.calcClusterCentroid(cluster1);
		System.out.println("Cluster centroid (Cluster 1): " + clusterCentroid1 + "\n");
		
		ArrayList<Double> clusterVar1 = t1.calcClusterVar(cluster1);
		System.out.println("Variance of Cluster (Cluster 1): " + clusterVar1 + "\n");

		ArrayList<Double> clusterCentroid2 = t1.calcClusterCentroid(cluster2);
		System.out.println("Cluster centroid (Cluster 2): " + clusterCentroid2 + "\n");
		
		ArrayList<Double> clusterVar2 = t1.calcClusterVar(cluster2);
		System.out.println("Variance of Cluster (Cluster 2): " + clusterVar2 + "\n");
		
		System.out.println("\n");
		
		double scatt = t1.calcScatt(clusterList);
		System.out.println("Scatt: " + scatt);
		
		double dens_bw = t1.calcDens_bw(clusterList);
		System.out.println("Dens_Bw: " + dens_bw);
		
		double test = t1.calcS_Dbw(clusterList);
		System.out.println("S_Dbw:" + test);
		//int fXu = t1.functionXU(x, u, stDev)
	}
	
	//Scatt
	//Variance of a Cluster		(VERIFIED CORRECT)
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
	
	//Variance of a Cluster based on Dimension p	(VERIFIED CORRECT)
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
			temp = Double.parseDouble(df.format(temp));
			result += temp;
		}
						
		result = Double.parseDouble(df.format(result * f1));
		
		return result;
	}
	
	//Vector of the Cluster centroid	(VERIFIED CORRECT)
	private ArrayList<Double> calcClusterCentroid(Cluster cluster) {
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
				temp = Double.parseDouble(df.format(temp));
				clusterCentroid.remove(j);
				clusterCentroid.add(j, temp);
			}
		}
		
		for (int i = 0; i < clusterCentroid.size(); i++) {
			double temp = clusterCentroid.get(i);
			temp = temp / clusterPoints.size();
			clusterCentroid.remove(i);
			temp = Double.parseDouble(df.format(temp));
			clusterCentroid.add(i, temp);
		}
		
		return clusterCentroid;
	}
	
	//Variance of the Dataset	(VERIFIED CORRECT)
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
	
	//Variance of the Dataset based on Dimension p	(VERIFIED CORRECT)
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
			temp = Double.parseDouble(df.format(temp));
			result += temp;
			
		}

		result = Double.parseDouble(df.format(result * f1));
		
		return result;
	}
	
	//Mean of the dataset vectors	(VERIFIED CORRECT)
	public ArrayList<Double> calcMeanOfDataSetPoints(ArrayList<ClusterPoint> vectorPoints) {
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
				temp = Double.parseDouble(df.format(temp));
				result.remove(j);
				result.add(j, temp);
			}
		}
		
		for (int i = 0; i < result.size(); i++) {
			double temp = result.get(i);
			temp = temp / vectorPoints.size();
			result.remove(i);
			temp = Double.parseDouble(df.format(temp));
			result.add(i, temp);
		}
		
		return result;
	}
	
	//Intra-cluster variance (Scattering of cluster)	(VERIFIED CORRECT)
	public double calcScatt(ArrayList<Cluster> clusterList) {
		double n = clusterList.size();
		double f1 = 1 / n;
		double f2 = 0;
		ArrayList<Double> datasetVar = new ArrayList<Double>();
		ArrayList<Double> clusterVar = new ArrayList<Double>();
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
			tempA = Double.parseDouble(df.format(tempA));
			tempDatasetVarList.add(tempA);
		}
		
		for (int i = 0; i < tempDatasetVarList.size(); i++) {
			temp += tempDatasetVarList.get(i);
		}
		
		normedDatasetVar = Math.sqrt(temp);
		normedDatasetVar = Double.parseDouble(df.format(normedDatasetVar));
		
		System.out.println("Normed dataset variance: " + normedDatasetVar);
		
		for (int i = 0; i < n; i++) {
			clusterVar = calcClusterVar(clusterList.get(i));
						
			ArrayList<Double> tempClusterVarList = new ArrayList<Double>();
			
			for (int j = 0; j < clusterVar.size(); j++) {
				double temp3 = clusterVar.get(j); 
				temp3 = temp3 * temp3;
				tempClusterVarList.add(temp3);
			}
			
			for (int k = 0; k < tempClusterVarList.size(); k++) {
				temp2 += tempClusterVarList.get(i);
			}			
			
			normedClusterVar = Math.sqrt(temp2);
			normedClusterVar = Double.parseDouble(df.format(normedClusterVar));
			
			f2 += normedClusterVar;
		}
		
		System.out.println("Normed cluster variance: " + f2);
		
		result = (f1 * f2) / normedDatasetVar;
		result = Double.parseDouble(df.format(result));		
		
		return result;
	}

	//Average standard deviation of clusters	(VERIFIED CORRECT)
	public double calcStdev(ArrayList<Cluster> clusterList) {
		double result = 0;
		double c = clusterList.size();
		double f1 = 1 / c;
		double f2 = 0;
		
		for (int i = 0; i < c; i ++) {
			ArrayList<Double> clusterVar = calcClusterVar(clusterList.get(i));
			ArrayList<Double> tempClusterVarList = new ArrayList<Double>();
			double normedClusterVar = 0;
			
			for (int j = 0; j < clusterVar.size(); j++) {
				double temp = clusterVar.get(i); 
				temp = temp * temp;
				tempClusterVarList.add(temp);
			}
			
			for (int k = 0; k < tempClusterVarList.size(); k++) {
				double temp = 0; 
				temp += tempClusterVarList.get(i);
				normedClusterVar = Math.sqrt(temp);
			}
			
			f2 += normedClusterVar;
		}
		
		result = f1 * Math.sqrt(f2);
		result = Double.parseDouble(df.format(result));
		
		return result;
	}
	
	//Dens_bw 
	//Vector of centroid of Cluster U	(VERIFIED CORRECT)
	public ArrayList<Double> calcUVector(Cluster i, Cluster j) {
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
	public int functionXU(ArrayList<Double> x, ArrayList<Double> u, double stDev) {
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
	public double calcDensityUij(Cluster i, Cluster j, double stDev) {
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
	
	public double calcDensity(Cluster i, double stDev) {
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
		
		double stDev = calcStdev(clusterList);
		
		for (int i = 0; i < c; i++) {
			for (int j = 0; j < c; j++) {
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
				}
				
				f2 += temp;
			}	
		}
		
		dens_bw = f1 * f2;
		
		return dens_bw;
	}

	public double calcS_Dbw(ArrayList<Cluster> clusterList) {
		double dens_bw = calcDens_bw(clusterList);
		double scatt = calcScatt(clusterList);
		double result = scatt + dens_bw;
		
		result = Double.parseDouble(df.format(result));

		return result;
	}
}
