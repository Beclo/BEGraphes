package org.insa.graphs.algorithm.shortestpath;

public class Label implements Comparable<Label>{
	
	int currentnode;
	boolean marque;
	double cost;
	int father;
	
	public Label(int currentnode, boolean marque, double cost, int father) {
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
	
	public int getIDCurrentNode() {
		return this.currentnode;
	}
	
	public int getFather() {
		return this.father;
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
	

	public int compareTo(Label arg2) {
		return Double.compare(this.cost, arg2.cost);
	}
	
	

}
