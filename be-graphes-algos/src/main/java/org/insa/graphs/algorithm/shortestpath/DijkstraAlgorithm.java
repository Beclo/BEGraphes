package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.algorithm.utils.ElementNotFoundException;
import org.insa.graphs.algorithm.utils.EmptyPriorityQueueException;
import org.insa.graphs.model.*;

import java.util.ArrayList;
import java.util.Collections;


public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        Graph graph = data.getGraph();
        
        // TODO:
        
        /* INITIALIZATION */
        //Associate label to each node
        final int nbNodes = graph.size();
        Label[] label = new Label[nbNodes];
        for (int i=0; i<nbNodes; i++) {
        	label[i]=new Label(i,false,Double.POSITIVE_INFINITY,-1);
        }
        
        //Initialization of the origin (cost=0)
        int origin = data.getOrigin().getId();
        double initCost = 0;
        label[origin].setCost(initCost);
        
        //Initialization of the Binary Heap
        BinaryHeap<Label> heap = new BinaryHeap<>();
        heap.insert(label[origin]);
        
        /*//Notify all observers that the origin has been processed
        notifyOriginProcessed(data.getOrigin());*/
        
        /* ITERATIONS */
        //While il existe des sommets non marqués
        //	x<--ExtractMin(Tas)
        //	Mark(x)<--true
        //	For tous les y successeurs de x
        //		If not Mark(y) then
        //			Cost(y)<--Min(Cost(y),Cost(x)+W(x,y))
        //			If Cost(y) a été mis à jour then
        //				Placer(y, Tas)
        //				Father(y)<--x
        //			end if
        //		end if
        //	end for
        //end while
        
        while(!label[data.getDestination().getId()].getMarque()) {
        	Label currentNode;
        	
        	//Extract Min
        	try {
        		currentNode = heap.findMin();
        	}catch(EmptyPriorityQueueException e) {
        		break;
        	}
        	
        	//Mark
        	label[currentNode.getIDCurrentNode()].setMarque(true);
        	
        	try {
        		heap.remove(currentNode);
        	}catch(ElementNotFoundException e) {}
        	
        	for(Arc successor : graph.get(currentNode.getIDCurrentNode()).getSuccessors()) {
        		
        		//Verify if the arc is allowed
        		if (!data.isAllowed(successor)) {
        			continue;
        		}
        		
        		int IdNextNode = successor.getDestination().getId();
        		//If successor not marked
        		if(!label[IdNextNode].getMarque()) {
        			double w = data.getCost(successor);
        			double oldCost = label[IdNextNode].getCost();
        			double newCost = label[currentNode.getIDCurrentNode()].getCost()+w;
        			
        			/*if(Double.isInfinite(oldCost) && Double.isFinite(newCost)) {
        				notifyNodeReached(successor.getDestination());
        			}*/
        			
        		//Test if the new cost is smaller than the old cost.
        		//If so, the cost of the successor is updated
        			if(oldCost>newCost) {
        				label[IdNextNode].setCost(newCost);
        				label[IdNextNode].setFather(currentNode.getIDCurrentNode());
        				
        				//remove it from the heap if already exists
        				//if not, insert it
        				try {
        					heap.remove(label[IdNextNode]);
        					heap.insert(label[IdNextNode]);
        				}catch(ElementNotFoundException e) {
        					heap.insert(label[IdNextNode]);
        				}
        			}
        			
        		}
        		
        	}
     
        }
        
        /* SOLUTION */
        ShortestPathSolution solution = null;
        
        //Infeasible
        if(!label[data.getDestination().getId()].getMarque()) {
        	solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        }else {
	        //Creation of an ArrayList which will contain the list of nodes of the solution
	        ArrayList<Node> nodes = new ArrayList<Node>();
	        nodes.add(data.getDestination());
	        Node current = data.getDestination();
	        
	        //Find the nodes and add them to the ArrayList
	        while(!current.equals(data.getOrigin())) {
	        	Node father = graph.getNodes().get(label[current.getId()].getFather());
	        	nodes.add(father);
	        	current = father;
	        }
	        
	        //Reverse the ArrayList
	        Collections.reverse(nodes);
	        
	        //Creation of the final solution
	        Path finalPath = Path.createShortestPathFromNodes(graph, nodes);
	        solution = new ShortestPathSolution(data,Status.OPTIMAL,finalPath);
        }
        
        return solution;
    }

}
