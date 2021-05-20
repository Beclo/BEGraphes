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
import org.insa.graphs.model.io.BinaryPathReader;
import org.insa.graphs.model.io.GraphReader;
import org.insa.graphs.model.io.PathReader;
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
    private static Path onenodePathHG, onenodePathC, longPath, longPathTime;
    
    // Variables to construct solutions for the tests
    private static ShortestPathSolution infeasibleDijkstra, onenode0DijkstraHG, onenode0DijkstraC, onenode2DijkstraHG, onenode2DijkstraC, longpath0Dijkstra, longpath2Dijkstra, triangODDijkstra, triangOSDijkstra, triangSDDijkstra;
    private static ShortestPathSolution infeasibleAStar, onenode0AStarHG, onenode0AStarC, onenode2AStarHG, onenode2AStarC, longpath0AStar, longpath2AStar, triangODAStar, triangOSAStar, triangSDAStar;
    
    // Array of solutions
    private static ShortestPathSolution[] Dijkstrasolutions0,Dijkstrasolutions2;
    private static ShortestPathSolution[] Bellmansolutions0,Bellmansolutions2;
    private static ShortestPathSolution[] AStarsolutions0,AStarsolutions2;
    
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
		
		// Get Insa-Bikini path (long path)
		try {
			String pathInsa = "/home/berthele/Bureau/BEGraphes/path_fr31_insa_bikini_canal.path";
			PathReader readerInsa = new BinaryPathReader(new DataInputStream(new BufferedInputStream(new FileInputStream(pathInsa))));
			longPath = readerInsa.readPath(graphHauteGaronne);
		}catch(Exception e) {}
		
		// Get Insa-Aeroport path time (long path)
				try {
					String pathAero = "/home/berthele/Bureau/BEGraphes/path_fr31_insa_aeroport_time.path";
					PathReader readerAero = new BinaryPathReader(new DataInputStream(new BufferedInputStream(new FileInputStream(pathAero))));
					longPathTime = readerAero.readPath(graphHauteGaronne);
				}catch(Exception e) {}
		
		
		// Get Nodes of Haute-Garonne map
		nodesHauteGaronne = new ArrayList<>(graphHauteGaronne.getNodes());
		
		// Get Nodes of Carre map
		nodesCarre = new ArrayList<>(graphCarre.getNodes());
		
		
		/////////////////////////////////////
		////////// INFEASIBLE path //////////
		////////////////////////////////////
		
		boolean originOK = false;
		boolean destinationOK = false;
		
		Random randomnodes = new Random();
		Node origin;
		Node destination;
		int originid = -1;
		int destinationid = -1;
		
		while(!destinationOK) {
			
			destinationOK=true;
			
			// Random Nodes
			origin = nodesHauteGaronne.get(randomnodes.nextInt(graphHauteGaronne.size()));
			destination = nodesHauteGaronne.get(randomnodes.nextInt(graphHauteGaronne.size()));
			originid = origin.getId();
			destinationid = destination.getId();
			
			// Verify if the path is feasible and if origin!=destination
			WeaklyConnectedComponentsData componentData = new WeaklyConnectedComponentsData(graphHauteGaronne);
			WeaklyConnectedComponentsAlgorithm componentAlgo = new WeaklyConnectedComponentsAlgorithm(componentData);
			WeaklyConnectedComponentsSolution componentSolution = componentAlgo.run();
			ArrayList<ArrayList<Node>> components = componentSolution.getComponents();
			int numcomponent = -1;
	
			for(int i=0;i<components.size();i++) {
				for(int j=0;j<components.get(i).size();j++) {
					if(origin.equals(components.get(i).get(j))) {
						originOK=true;
						numcomponent = i;
					}
				}
			}
			
			if(originOK) {
				for(int j=0;j<components.get(numcomponent).size();j++) {
					if(destination.equals(components.get(numcomponent).get(j))) {
						destinationOK=false;
					}
				}
			}
		}
		
		//// Construction of the infeasible solution
			//Road map Haute-Garonne
		//ShortestPathData infeasibleData = new ShortestPathData(graphHauteGaronne,nodesHauteGaronne.get(120349),nodesHauteGaronne.get(116900),ArcInspectorFactory.getAllFilters().get(0));
			ShortestPathData infeasibleData = new ShortestPathData(graphHauteGaronne,graphHauteGaronne.get(originid),graphHauteGaronne.get(destinationid),ArcInspectorFactory.getAllFilters().get(0));
			
			//// Dijkstra ////
			DijkstraAlgorithm infeasibleDijkstraAlgo = new DijkstraAlgorithm(infeasibleData);
			infeasibleDijkstra = infeasibleDijkstraAlgo.doRun();
			
			//// A* ////
			AStarAlgorithm infeasibleAStarAlgo = new AStarAlgorithm(infeasibleData);
			infeasibleAStar = infeasibleAStarAlgo.doRun();
		
		
		///////////////////////////////////
		////////// ONE NODE path //////////
		//////////////////////////////////
		
		////Construction of the one node solution
			//Road map Haute-Garonne//ShortestPathData infeasibleData = new ShortestPathData(graphHauteGaronne,nodesHauteGaronne.get(120349),nodesHauteGaro
		Node onenodeHG = nodesHauteGaronne.get(1);
		onenodePathHG = new Path(graphHauteGaronne,onenodeHG);
		ShortestPathData onenode0DataHG = new ShortestPathData(graphHauteGaronne,onenodeHG,onenodeHG,ArcInspectorFactory.getAllFilters().get(0));
		ShortestPathData onenode2DataHG = new ShortestPathData(graphHauteGaronne,onenodeHG,onenodeHG,ArcInspectorFactory.getAllFilters().get(2));
		
			//Non road map Carre
		Node onenodeC = nodesCarre.get(1);
		onenodePathC = new Path(graphCarre,onenodeC);
		ShortestPathData onenode0DataC = new ShortestPathData(graphCarre,onenodeC,onenodeC,ArcInspectorFactory.getAllFilters().get(0));
		ShortestPathData onenode2DataC = new ShortestPathData(graphCarre,onenodeC,onenodeC,ArcInspectorFactory.getAllFilters().get(2));
		
		//// Dijkstra ////
			// Mode Length
		DijkstraAlgorithm onenode0DijkstraAlgoHG = new DijkstraAlgorithm(onenode0DataHG);
		DijkstraAlgorithm onenode0DijkstraAlgoC = new DijkstraAlgorithm(onenode0DataC);
		onenode0DijkstraHG = onenode0DijkstraAlgoHG.doRun();
		onenode0DijkstraC = onenode0DijkstraAlgoC.doRun();
		
			// Mode Time
		DijkstraAlgorithm onenode2DijkstraAlgoHG = new DijkstraAlgorithm(onenode2DataHG);
		DijkstraAlgorithm onenode2DijkstraAlgoC = new DijkstraAlgorithm(onenode2DataC);
		onenode2DijkstraHG = onenode2DijkstraAlgoHG.doRun();
		onenode2DijkstraC = onenode2DijkstraAlgoC.doRun();
		
		//// AStar ////
			// Mode Length
		AStarAlgorithm onenode0AStarAlgoHG = new AStarAlgorithm(onenode0DataHG);
		AStarAlgorithm onenode0AStarAlgoC = new AStarAlgorithm(onenode0DataC);
		onenode0AStarHG = onenode0AStarAlgoHG.doRun();
		onenode0AStarC = onenode0AStarAlgoC.doRun();
		
			// Mode Time
		AStarAlgorithm onenode2AStarAlgoHG = new AStarAlgorithm(onenode2DataHG);
		AStarAlgorithm onenode2AStarAlgoC = new AStarAlgorithm(onenode2DataC);
		onenode2AStarHG = onenode2AStarAlgoHG.doRun();
		onenode2AStarC = onenode2AStarAlgoC.doRun();
		
		
		/////////////////////////////////
		////////// RANDOM path //////////
		////////////////////////////////
		
		// Construction of solutions : random pair origin/destination
		
		Dijkstrasolutions0 = new ShortestPathSolution[50];
		Bellmansolutions0 = new ShortestPathSolution[50];
		AStarsolutions0 = new ShortestPathSolution[50];
		Dijkstrasolutions2 = new ShortestPathSolution[50];
		Bellmansolutions2 = new ShortestPathSolution[50];
		AStarsolutions2 = new ShortestPathSolution[50];
		
		int ite = 0;
		while(ite<50) {
			
			// Random Nodes
			randomnodes = new Random();
			origin = nodesCarre.get(randomnodes.nextInt(graphCarre.size()));
			destination = nodesCarre.get(randomnodes.nextInt(graphCarre.size()));
			
			
			if(!(origin.equals(destination))) {
				ShortestPathData randomData0 = new ShortestPathData(graphCarre,origin,destination,ArcInspectorFactory.getAllFilters().get(0));
				ShortestPathData randomData2 = new ShortestPathData(graphCarre,origin,destination,ArcInspectorFactory.getAllFilters().get(2));
				
				//// Dijkstra ////
					// Non-road map Carré
					// Mode Length
				DijkstraAlgorithm random0DijkstraAlgo = new DijkstraAlgorithm(randomData0);
				Dijkstrasolutions0[ite] = random0DijkstraAlgo.doRun();
				
					// Mode Time
				DijkstraAlgorithm random2DijkstraAlgo = new DijkstraAlgorithm(randomData2);
				Dijkstrasolutions2[ite] = random2DijkstraAlgo.doRun();
				
				//// Bellman Ford ////
					// Non-road map Carré
					// Mode Length
				BellmanFordAlgorithm random0BellmanAlgo = new BellmanFordAlgorithm(randomData0);
				Bellmansolutions0[ite] = random0BellmanAlgo.doRun();
				
					// Mode Time
				BellmanFordAlgorithm random2BellmanAlgo = new BellmanFordAlgorithm(randomData2);
				Bellmansolutions2[ite] = random2BellmanAlgo.doRun();

				//// A* ////
					// Non-road map Carré
					// Mode Length
				AStarAlgorithm random0AStarAlgo = new AStarAlgorithm(randomData0);
				AStarsolutions0[ite] = random0AStarAlgo.doRun();
				
					// Mode Time
				AStarAlgorithm random2AStarAlgo = new AStarAlgorithm(randomData2);
				AStarsolutions2[ite] = random2AStarAlgo.doRun();

				ite++;
			}
				
				
		}
			
		
		///////////////////////////////
		////////// LONG path //////////
		//////////////////////////////
		
		//// Construction of the long path solution
				//Road map Haute-Garonne
					// Path Insa-Bikini (Length)
		ShortestPathData longpath0Data = new ShortestPathData(graphHauteGaronne,longPath.getOrigin(),longPath.getDestination(),ArcInspectorFactory.getAllFilters().get(0));
					// Path Insa-Aeroport (Time)
		ShortestPathData longpath2Data = new ShortestPathData(graphHauteGaronne,longPathTime.getOrigin(),longPathTime.getDestination(),ArcInspectorFactory.getAllFilters().get(2));
		
		//// Dijkstra ////
			// Mode Length
		DijkstraAlgorithm longpath0DijkstraAlgo = new DijkstraAlgorithm(longpath0Data);
		longpath0Dijkstra = longpath0DijkstraAlgo.doRun();
			
			// Mode Time
		DijkstraAlgorithm longpath2DijkstraAlgo = new DijkstraAlgorithm(longpath2Data);
		longpath2Dijkstra = longpath2DijkstraAlgo.doRun();
				
		//// A* ////
			// Mode Length
		AStarAlgorithm longpath0AStarAlgo = new AStarAlgorithm(longpath0Data);
		longpath0AStar = longpath0AStarAlgo.doRun();
		
			// Mode Time
		AStarAlgorithm longpath2AStarAlgo = new AStarAlgorithm(longpath2Data);
		longpath2AStar = longpath2AStarAlgo.doRun();
		
		
		///////////////////////////////////////////
		////////// TRIANGULAR INEQUALITY //////////
		//////////////////////////////////////////
		
		// Random Nodes
		
		Node origine = nodesHauteGaronne.get(randomnodes.nextInt(graphHauteGaronne.size())); // Point O
		Node dest = nodesHauteGaronne.get(randomnodes.nextInt(graphHauteGaronne.size())); // Point D
		Node stop = nodesHauteGaronne.get(randomnodes.nextInt(graphHauteGaronne.size())); // Point S
		
		//// Construction of the long path solution
			// Road map Haute-Garonne
		ShortestPathData ODData = new ShortestPathData(graphHauteGaronne,origine,dest,ArcInspectorFactory.getAllFilters().get(0));
		ShortestPathData OSData = new ShortestPathData(graphHauteGaronne,origine,stop,ArcInspectorFactory.getAllFilters().get(0));
		ShortestPathData SDData = new ShortestPathData(graphHauteGaronne,stop,dest,ArcInspectorFactory.getAllFilters().get(0));
		
		//// Dijkstra ////
			// Mode Length
		DijkstraAlgorithm ODDijkstraAlgo = new DijkstraAlgorithm(ODData);
		triangODDijkstra = ODDijkstraAlgo.doRun();
		
		DijkstraAlgorithm OSDijkstraAlgo = new DijkstraAlgorithm(OSData);
		triangOSDijkstra = OSDijkstraAlgo.doRun();
		
		DijkstraAlgorithm SDDijkstraAlgo = new DijkstraAlgorithm(SDData);
		triangSDDijkstra = SDDijkstraAlgo.doRun();
	
		//// A* ////
			// Mode Length
		AStarAlgorithm ODAStarAlgo = new AStarAlgorithm(ODData);
		triangODAStar = ODAStarAlgo.doRun();
		
		AStarAlgorithm OSAStarAlgo = new AStarAlgorithm(OSData);
		triangOSAStar = OSAStarAlgo.doRun();
		
		AStarAlgorithm SDAStarAlgo = new AStarAlgorithm(SDData);
		triangSDAStar = SDAStarAlgo.doRun();

	}
	
	///////////////////////
	//////// TESTS ////////
	//////////////////////
	
	//////////////////////
	// Tests INFEASIBLE //
	/////////////////////
	@Test
	public void testInfeasiblePathDijkstra() {
		assertEquals(AbstractSolution.Status.INFEASIBLE,infeasibleDijkstra.getStatus());
	}
	
	@Test
	public void testInfeasiblePathAStar() {
		assertEquals(AbstractSolution.Status.INFEASIBLE,infeasibleAStar.getStatus());
	}
	
	////////////////////
	// Tests ONE NODE //
	///////////////////
	
	//---------------//
	//// DIJKSTRA ////
	//--------------//
	@Test
	public void testOneNodeDijkstra_Length() {
		// Road map
		assertEquals(onenodePathHG.getLength(),onenode0DijkstraHG.getPath().getLength(),1e-6);
		assertTrue(onenode0DijkstraHG.getPath().isValid());
		
		//Non road map
		assertEquals(onenodePathC.getLength(),onenode0DijkstraC.getPath().getLength(),1e-6);
		assertTrue(onenode0DijkstraC.getPath().isValid());
	}
	
	@Test
	public void testOneNodeDijkstra_Time() {
		// Road map
		assertEquals(onenodePathHG.getMinimumTravelTime(),onenode2DijkstraHG.getPath().getMinimumTravelTime(),1e-6);
		assertTrue(onenode2DijkstraHG.getPath().isValid());
		
		//Non road map
		assertEquals(onenodePathC.getMinimumTravelTime(),onenode2DijkstraC.getPath().getMinimumTravelTime(),1e-6);
		assertTrue(onenode2DijkstraC.getPath().isValid());
	}
	
	//-----------//
	//// ASTAR ////
	//----------//
	@Test
	public void testOneNodeAStar_Length() {
		// Road map
		assertEquals(onenodePathHG.getLength(),onenode0AStarHG.getPath().getLength(),1e-6);
		assertTrue(onenode0AStarHG.getPath().isValid());
		
		//Non road map
		assertEquals(onenodePathC.getLength(),onenode0AStarC.getPath().getLength(),1e-6);
		assertTrue(onenode0AStarC.getPath().isValid());
	}
	
	@Test
	public void testOneNodeAStar_Time() {
		// Road map
		assertEquals(onenodePathHG.getMinimumTravelTime(),onenode2AStarHG.getPath().getMinimumTravelTime(),1e-6);
		assertTrue(onenode2AStarHG.getPath().isValid());
		
		//Non road map
		assertEquals(onenodePathC.getMinimumTravelTime(),onenode2AStarC.getPath().getMinimumTravelTime(),1e-6);
		assertTrue(onenode2AStarC.getPath().isValid());
	}
	
	//////////////////
	// Tests RANDOM //
	/////////////////
	
	//--------------//
	//// DIJKSTRA ////
	//-------------//
	@Test
	public void testRandomDijkstra_Length() {
		for(int i=0; i<50; i++) {
			assertEquals(Bellmansolutions0[i].getPath().getLength(),Dijkstrasolutions0[i].getPath().getLength(),100);
			assertTrue(Dijkstrasolutions0[i].getPath().isValid());
		}
	}
	
	@Test
	public void testRandomDijkstra_Time() {
		for(int i=0; i<50; i++) {
			assertEquals(Bellmansolutions2[i].getPath().getMinimumTravelTime(),Dijkstrasolutions2[i].getPath().getMinimumTravelTime(),100);
			assertTrue(Dijkstrasolutions2[i].getPath().isValid());
		}
	}
	
	//-----------//
	//// ASTAR ////
	//----------//
	@Test
	public void testRandomAStar_Length() {
		for(int i=0; i<50; i++) {
			assertEquals(Bellmansolutions2[i].getPath().getLength(),AStarsolutions2[i].getPath().getLength(),100);
			assertTrue(AStarsolutions2[i].getPath().isValid());
		}
	}
	
	@Test
	public void testRandomAStar_Time() {
		for(int i=0; i<50; i++) {
			assertEquals(Bellmansolutions2[i].getPath().getMinimumTravelTime(),AStarsolutions2[i].getPath().getMinimumTravelTime(),100);
			assertTrue(AStarsolutions2[i].getPath().isValid());
		}
	}
	
	/////////////////////
	// Tests LONG PATH //
	////////////////////
	
	//----------//
	// DIJKSTRA //
	//---------//
	@Test
	public void testLongPathDijkstra_Length() {
		assertEquals(longPath.getLength(),longpath0Dijkstra.getPath().getLength(),100);
		assertTrue(longpath0Dijkstra.getPath().isValid());
	}
	
	@Test
	public void testLongPathDijkstra_Time() {
		assertEquals(longPathTime.getMinimumTravelTime(),longpath2Dijkstra.getPath().getMinimumTravelTime(),100);
		assertTrue(longpath2Dijkstra.getPath().isValid());
	}
	
	//-------//
	// ASTAR //
	//------//
	@Test
	public void testLongPathAStar_Length() {
		assertEquals(longPath.getLength(),longpath0AStar.getPath().getLength(),100);
		assertTrue(longpath0AStar.getPath().isValid());
	}
	
	@Test
	public void testLongPathAStar_Time() {
		assertEquals(longPathTime.getMinimumTravelTime(),longpath2AStar.getPath().getMinimumTravelTime(),100);
		assertTrue(longpath2AStar.getPath().isValid());
	}
	
	/////////////////////////////////
	// Tests TRIANGULAR INEQUALITY //
	////////////////////////////////
	
	//----------//
	// DIJKSTRA //
	//---------//
	@Test
	public void testtriangPathDijkstra_Length() {
		assertTrue(triangODDijkstra.getPath().getLength()<=(triangOSDijkstra.getPath().getLength()+triangSDDijkstra.getPath().getLength()));
	}
	
	//-------//
	// ASTAR //
	//------//
	@Test
	public void testtriangPathAStar_Length() {
		assertTrue(triangODAStar.getPath().getLength()<=(triangOSAStar.getPath().getLength()+triangSDAStar.getPath().getLength()));
	}
}
