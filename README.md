# Clustering_Analysis
NYP - Final Year Project (2013) 

## Introduction & Objective 
Creating an automated cluster evaluation program that allows users to find out which clustering schema or algorithm is more preferable for documents clustering. 

As a continuation from the previous group, by making use of Mahout, an open-source program that contains many clustering algorithms, the process of clustering the forum posts / documents will be handled by it. 

To automate the evaluation of clustering algorithms’ performance through the use of suitable metrics. 

## Cluster Evaluation 
- Internal Evaluation
-- Evaluating the cluster results based on data that was clustered itself. These methods assign the best score to the algorithm that produces clusters with high intra-cluster similarity and low inter-cluster similarity. 
- External Evaluation 
-- Evaluating the cluster results based on data that was not used for clustering, such as known class labels and external benchmarks. Such benchmarks consist of a set of pre-classified items, and these sets are often created by human (experts). 

### S_Dbw Index
S_Dbw is a validity index based on Scattering and Density between clusters. 

Its objective: A definition of a relative algorithm-independent validity index, for assessing the quality of partitioning for each set of input values. 

Main features of S_Dbw: 
	Evaluates resulting clustering schemes as defined by the algorithm under consideration
	Selects for each algorithm the optimal set of input parameters with regards to specific data set


## Resources
### Operating Systems
•	Microsoft Windows 7 Enterprise (Service Pack 1)
•	Linux

### Servers
•	MySQL Server 5.6

### Computer Languages Used
•	Java 
•	R Programming

### Browsers Used
•	Internet Explorer
•	Google Chrome

### Programs Used
•	Eclipse Juno
•	MySQL Workbench
•	Visual Paradigm
•	Microsoft Office 
•	Cygwin Terminal
•	R Studio

### APIs Used
•	Apache Mahout
