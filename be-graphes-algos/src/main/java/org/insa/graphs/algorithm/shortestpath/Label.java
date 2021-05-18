package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.*;

public class Label implements Comparable<Label>{
	
	Node currentnode;
	boolean marque;
	double cost;
	int father;
	
	public Label(Node currentnode, boolean marque, double cost, int father) {
		this.currentnode=currentnode;
		this.marque=marque;
		this.cost=cost;
		this.father=father;
	}
	
	//Getter
	public double getCost() {
		return this.cost;
	}
	
	public boolean getMarque() {
		return this.marque;
	}
	
	public Node getCurrentNode() {
		return this.currentnode;
	}
	
	public int getFather() {
		return this.father;
	}	
	
	public double getTotalCost() {
		return this.cost;
	}
	
	public double getExpected() {
		return 0;
	}
	
	//Setter
	public void setCost(double cost) {
		this.cost = cost;
	}
	
	public void setMarque(boolean marque) {
		this.marque=marque;
	}
	
	public void setFather(int father) {
		this.father=father;
	}
	
	@Override
	public int compareTo(Label arg2) {
		return Double.compare(this.getTotalCost(), arg2.getTotalCost());
	}
	
	

}
