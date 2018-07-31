package Metrics;
import java.util.ArrayList;


public class Cluster {
	ArrayList<ClusterPoint> clusterPointsList = new ArrayList<ClusterPoint>();
	
	public void addToClusterPointsList(ClusterPoint clusterPoint) { 
		this.clusterPointsList.add(clusterPoint);
	}
	
	public ArrayList<ClusterPoint> getClusterPointsList() {
		return this.clusterPointsList;
	}
}
