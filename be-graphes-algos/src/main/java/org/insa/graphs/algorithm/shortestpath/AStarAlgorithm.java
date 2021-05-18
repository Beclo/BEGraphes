package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractInputData;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }
    
    //Redefinition of setLabel()
    public void setLabel(ShortestPathData data) {
    	// Mode Length
    		boolean length = data.getMode().equals(AbstractInputData.Mode.LENGTH);
	    	for (int i=0; i<data.getGraph().size(); i++) {
	    		label[i]=new LabelStar(data.getGraph().get(i),false,Double.POSITIVE_INFINITY,-1,data.getDestination(),length);
	    	}
    }
}
