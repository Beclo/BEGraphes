package org.insa.graphs.algorithm.shortestpath;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.weakconnectivity.WeaklyConnectedComponentsAlgorithm;
import org.insa.graphs.algorithm.weakconnectivity.WeaklyConnectedComponentsData;
import org.insa.graphs.algorithm.weakconnectivity.WeaklyConnectedComponentsSolution;
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
    private static ShortestPathSolution infeasibleAStar, onenodeAStarHG, onenodeAStarC;
    
    // Array of solutions
    private static ShortestPathSolution[] Dijkstrasolutions;
    private static ShortestPathSolution[] Bellmansolutions;
    private static ShortestPathSolution[] AStarsolutions;
    
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
		
		//// A* ////
		AStarAlgorithm infeasibleAStarAlgo = new AStarAlgorithm(infeasibleData);
		infeasibleAStar = infeasibleAStarAlgo.doRun();
		
		
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
		
		//// AStar ////
			// Mode Length
		AStarAlgorithm onenodeAStarAlgoHG = new AStarAlgorithm(onenodeDataHG);
		AStarAlgorithm onenodeAStarAlgoC = new AStarAlgorithm(onenodeDataC);
		onenodeAStarHG = onenodeAStarAlgoHG.doRun();
		onenodeAStarC = onenodeAStarAlgoC.doRun();
		
		////////// RANDOM path //////////
		
		// Construction of solutions : random pair origin/destination
		
		Dijkstrasolutions = new ShortestPathSolution[5];
		Bellmansolutions = new ShortestPathSolution[5];
		AStarsolutions = new ShortestPathSolution[5];
		
		int ite = 0;
		while(ite<5) {
			//boolean valid = true;
			boolean originOK = false;
			boolean destinationOK = false;
			
			// Random Nodes
			Random randomnodes = new Random();
			Node origin = nodesHauteGaronne.get(randomnodes.nextInt(graphHauteGaronne.size()));
			Node destination = nodesHauteGaronne.get(randomnodes.nextInt(graphHauteGaronne.size()));
			
			// Verify if the path is feasible and if origin!=destination
			WeaklyConnectedComponentsData componentData = new WeaklyConnectedComponentsData(graphHauteGaronne);
			WeaklyConnectedComponentsAlgorithm componentAlgo = new WeaklyConnectedComponentsAlgorithm(componentData);
			WeaklyConnectedComponentsSolution componentSolution = componentAlgo.run();
			ArrayList<ArrayList<Node>> components = componentSolution.getComponents();
			int numcomponent = -1;
			
			if(!(origin.equals(destination))) {
				for(int i=0;i<components.size();i++) {
					for(int j=0;j<components.get(i).size();j++) {
						if(origin.equals(components.get(i).get(j))) {
							originOK=true;
							numcomponent = i;
							System.out.println("Origine :"+origin.getId());
						}
					}
				}
				
				if(originOK) {
					for(int j=0;j<components.get(numcomponent).size();j++) {
						if(destination.equals(components.get(numcomponent).get(j))) {
							destinationOK=true;
							System.out.println("Destination :"+destination.getId());
						}
					}
				}
			}
			
			if((originOK)&&(destinationOK)) {
				ShortestPathData randomData = new ShortestPathData(graphHauteGaronne,origin,destination,ArcInspectorFactory.getAllFilters().get(0));
				
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
				
				//// A* ////
					// Road map Haute-Garonne
					// Mode Length
				AStarAlgorithm randomAStarAlgo = new AStarAlgorithm(randomData);
				AStarsolutions[ite] = randomAStarAlgo.doRun();
				
				ite++;
			}
			
		}
		
		
		////////// LONG path //////////
		
		
		
	}
	
	//////// TESTS ////////
	
	// Tests INFEASIBLE //
	@Test
	public void testInfeasiblePathDijkstra() {
		assertEquals(AbstractSolution.Status.INFEASIBLE,infeasibleDijkstra.getStatus());
	}
	
	@Test
	public void testInfeasiblePathAStar() {
		assertEquals(AbstractSolution.Status.INFEASIBLE,infeasibleAStar.getStatus());
	}
	
	
	// Tests ONE NODE //
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
	public void testOneNodeAStar_Length() {
		// Road map
		assertEquals(onenodePathHG.getLength(),onenodeAStarHG.getPath().getLength(),1e-6);
		assertTrue(onenodeAStarHG.getPath().isValid());
		
		//Non road map
		assertEquals(onenodePathC.getLength(),onenodeAStarC.getPath().getLength(),1e-6);
		assertTrue(onenodeAStarC.getPath().isValid());
	}
	
	
	// Tests RANDOM //
	@Test
	public void testRandomDijkstra_Length() {
		for(int i=0; i<5; i++) {
			assertEquals(Bellmansolutions[i].getPath().getLength(),Dijkstrasolutions[i].getPath().getLength(),1000);
			assertTrue(Dijkstrasolutions[i].getPath().isValid());
		}
	}
	
	@Test
	public void testRandomAStar_Length() {
		for(int i=0; i<5; i++) {
			assertEquals(Bellmansolutions[i].getPath().getLength(),AStarsolutions[i].getPath().getLength(),1000);
			assertTrue(AStarsolutions[i].getPath().isValid());
		}
	}
	
	
}
