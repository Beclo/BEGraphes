package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.*;

public class LabelStar extends Label{

	double costDest;
	
	public LabelStar(Node currentnode, boolean marque, double cost, int father, Node destination, boolean length) {
		super(currentnode,marque,cost,father);
		
		if(length) {
			this.costDest = Point.distance(currentnode.getPoint(), destination.getPoint());
		}else {
			this.costDest = Point.distance(currentnode.getPoint(), destination.getPoint())/25;
		}
	}
	
	
	public double getExpected() {
		return this.costDest;
	}
	
	//Redefinition of getTotalCost
	public double getTotalCost() {
		return this.cost+this.costDest;
	}
	
}
