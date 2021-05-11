package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Point;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }
    
    //Redefinition of setLabel()
    public void setLabel(ShortestPathData data) {
    	for (int i=0; i<data.getGraph().size(); i++) {
    		label[i]=new LabelStar(i,false,Double.POSITIVE_INFINITY,-1,Point.distance(data.getGraph().getNodes().get(i).getPoint(), data.getDestination().getPoint()));
    	}
    }
}
