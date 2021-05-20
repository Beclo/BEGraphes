package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractInputData;
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
    
    
    Label[] label = new Label[data.getGraph().size()];
    public void setLabel(ShortestPathData data){
    	for (int i=0; i<data.getGraph().size(); i++) {
    		label[i]=new Label(data.getGraph().get(i),false,Double.POSITIVE_INFINITY,-1);
    	}
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        Graph graph = data.getGraph();
        
        // TODO:
        
        /* INITIALIZATION */
        //Associate label to each node
        	//final int nbNodes = graph.size();
        setLabel(data);
        
        //Initialization of the origin (cost=0)
        int origin = data.getOrigin().getId();
        double initCost = 0;
        label[origin].setCost(initCost);
        
        //Initialization of the Binary Heap
        BinaryHeap<Label> heap = new BinaryHeap<>();
        heap.insert(label[origin]);
        
        //Notify all observers that the origin has been processed
        notifyOriginProcessed(data.getOrigin());
        
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
        
        while(!label[data.getDestination().getId()].getMarque()&&!heap.isEmpty()) {
        	Label currentNode;
        	
        	//Extract Min
        	try {
        		currentNode = heap.deleteMin();
        	}catch(EmptyPriorityQueueException e) {
        		break;
        	}
        	
           	//Mark
        	label[currentNode.getCurrentNode().getId()].setMarque(true);
        	
        	//System.out.println("Coût :"+label[currentNode.getCurrentNode().getId()].getTotalCost());
        	
        	//int successorswatch =0;
        	
        	for(Arc successor : graph.get(currentNode.getCurrentNode().getId()).getSuccessors()) {
        		
        		//successorswatch +=1;
        		
        		//Verify if the arc is allowed
        		if (!data.isAllowed(successor)) {
        			continue;
        		}
        		
        		int IdNextNode = successor.getDestination().getId();
        		//If successor not marked
        		if(!label[IdNextNode].getMarque()) {
        			double w = data.getCost(successor)+label[IdNextNode].getExpected();
        			double oldCost = label[IdNextNode].getTotalCost();
        			double newCost = label[currentNode.getCurrentNode().getId()].getCost()+w;
        			
        			//Notify all observers that a node has been reached for the first time.
        			if(Double.isInfinite(oldCost) && Double.isFinite(newCost)) {
        				notifyNodeReached(successor.getDestination());
        			}
        			
        		//Test if the new cost is smaller than the old cost.
        		//If so, the cost of the successor is updated
        			if(oldCost>newCost) {
        				label[IdNextNode].setCost(label[successor.getOrigin().getId()].getCost()+data.getCost(successor));
        				label[IdNextNode].setFather(currentNode.getCurrentNode().getId());
        				
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
        	
        	//int nb_successors = graph.get(currentNode.getIDCurrentNode()).getNumberOfSuccessors();
        	//System.out.println("Nombre de successeurs : "+nb_successors);
            //System.out.println("Nombre de successeurs comptés :"+ successorswatch);
     
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
	        
	        // The destination has been found, notify the observers.
            notifyDestinationReached(data.getDestination());
	        
	        //Find the nodes and add them to the ArrayList
	        while(!current.equals(data.getOrigin())) {
	        	Node father = graph.getNodes().get(label[current.getId()].getFather());
	        	nodes.add(father);
	        	current = father;
	        }
	        
	        //Reverse the ArrayList
	        Collections.reverse(nodes);
	        
	        //Creation of the final solution
	        Path finalPath;
	        
	        //Different modes
	        if(data.getMode().equals(AbstractInputData.Mode.LENGTH)) {
	        	//Solution according to length of the path
	        	finalPath = Path.createShortestPathFromNodes(graph, nodes);
	        }else {
	        	//Solution according to speed of the path
	        	finalPath = Path.createFastestPathFromNodes(graph, nodes);
	        }
	        
	        solution = new ShortestPathSolution(data,Status.OPTIMAL,finalPath);
        }
        
        return solution;
    }

}
