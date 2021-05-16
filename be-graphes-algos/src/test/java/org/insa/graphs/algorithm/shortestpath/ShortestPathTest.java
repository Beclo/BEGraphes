package org.insa.graphs.algorithm.shortestpath;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class ShortestPathTest {
	
	// Graph use for tests
    private static Graph graphHauteGaronne,graphCarre;

    // List of nodes
    private static ArrayList<Node> nodesHauteGaronne,nodesCarre;
    
    // Paths use for tests
    private static Path onenodePathHG, onenodePathC;
    
    // Variables to construct solutions for the tests
    private static ShortestPathSolution infeasibleDijkstra, onenodeDijkstraHG, onenodeDijkstraC;

    // Array of solutions
    private static ShortestPathSolution[] Dijkstrasolutions;
    private static ShortestPathSolution[] Bellmansolutions;
    
	@BeforeClass
	public static void initAll() throws IOException {
		
		// Get Haute-Garonne map
		try {
			String mapHauteGaronne = "/home/berthele/Bureau/BEGraphes/haute-garonne.mapgr";
			GraphReader readerHauteGaronne = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapHauteGaronne))));
			graphHauteGaronne = readerHauteGaronne.read();
		}catch(Exception e) {}
		
		// Get Carre map
		try {
			String mapCarre = "/home/berthele/Bureau/BEGraphes/carre.mapgr";
			GraphReader readerCarre = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapCarre))));
			graphCarre = readerCarre.read();
		}catch(Exception e) {}
		
		
		// Get Nodes of Haute-Garonne map
		nodesHauteGaronne = new ArrayList<>(graphHauteGaronne.getNodes());
		
		// Get Nodes of Carre map
		nodesCarre = new ArrayList<>(graphCarre.getNodes());
		
		
		////////// INFEASIBLE path //////////
		
		//// Construction of the infeasible solution
			//Road map Haute-Garonne
		ShortestPathData infeasibleData = new ShortestPathData(graphHauteGaronne,nodesHauteGaronne.get(120349),nodesHauteGaronne.get(116900),ArcInspectorFactory.getAllFilters().get(0));
		
		//// Dijkstra ////
		DijkstraAlgorithm infeasibleDijkstraAlgo = new DijkstraAlgorithm(infeasibleData);
		infeasibleDijkstra = infeasibleDijkstraAlgo.doRun();
		
		
		////////// ONE NODE path //////////
		
		////Construction of the one node solution
			//Road map Haute-Garonne
		Node onenodeHG = nodesHauteGaronne.get(1);
		onenodePathHG = new Path(graphHauteGaronne,onenodeHG);
		ShortestPathData onenodeDataHG = new ShortestPathData(graphHauteGaronne,onenodeHG,onenodeHG,ArcInspectorFactory.getAllFilters().get(0));
		
			//Non road map Carre
		Node onenodeC = nodesCarre.get(1);
		onenodePathC = new Path(graphCarre,onenodeC);
		ShortestPathData onenodeDataC = new ShortestPathData(graphCarre,onenodeC,onenodeC,ArcInspectorFactory.getAllFilters().get(0));
		
		//// Dijkstra ////
			// Mode Length
		DijkstraAlgorithm onenodeDijkstraAlgoHG = new DijkstraAlgorithm(onenodeDataHG);
		DijkstraAlgorithm onenodeDijkstraAlgoC = new DijkstraAlgorithm(onenodeDataC);
		onenodeDijkstraHG = onenodeDijkstraAlgoHG.doRun();
		onenodeDijkstraC = onenodeDijkstraAlgoC.doRun();
		
		
		////////// RANDOM path //////////
		// Construction of solutions : random pair origin/destination
		
		Dijkstrasolutions = new ShortestPathSolution[50];
		Bellmansolutions = new ShortestPathSolution[50];
		
		int ite = 0;
		while(ite<50) {
			boolean valid = true;
			
			
			
			if(valid) {
				
				ShortestPathData randomData = new ShortestPathData(graphHauteGaronne,onenodeHG,onenodeHG,ArcInspectorFactory.getAllFilters().get(0));
				
				//// Dijkstra ////
					// Road map Haute-Garonne
					// Mode Length
				DijkstraAlgorithm randomDijkstraAlgo = new DijkstraAlgorithm(randomData);
				Dijkstrasolutions[ite] = randomDijkstraAlgo.doRun();
				
					// Non-road map Carré
				
				//// Bellman Ford ////
					// Road map Haute-Garonne
					// Mode Length
				BellmanFordAlgorithm randomBellmanAlgo = new BellmanFordAlgorithm(randomData);
				Bellmansolutions[ite] = randomBellmanAlgo.doRun();
				
				ite++;
			}
			
		}
		
		
	}
	
	//////// TESTS ////////
	
	@Test
	public void testInfeasiblePathDijkstra() {
		assertEquals(AbstractSolution.Status.INFEASIBLE,infeasibleDijkstra.getStatus());
	}
	
	@Test
	public void testOneNodeDijkstra_Length() {
		// Road map
		assertEquals(onenodePathHG.getLength(),onenodeDijkstraHG.getPath().getLength(),1e-6);
		assertTrue(onenodeDijkstraHG.getPath().isValid());
		
		//Non road map
		assertEquals(onenodePathC.getLength(),onenodeDijkstraC.getPath().getLength(),1e-6);
		assertTrue(onenodeDijkstraC.getPath().isValid());
	}
	
	@Test
	public void testRandomDijkstra() {
		for(int i=0; i<50; i++) {
			
		}
	}
	
}
