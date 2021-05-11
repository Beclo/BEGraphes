package org.insa.graphs.algorithm.shortestpath;

public class LabelStar extends Label{

	double costDest;
	
	public LabelStar(int currentnode, boolean marque, double cost, int father, double costDest) {
		super(currentnode,marque,cost,father);
		this.costDest = costDest;
	}
	
	//Redefinition of getTotalCost
	public double getTotalCost() {
		return this.cost+this.costDest;
	}
	
}
