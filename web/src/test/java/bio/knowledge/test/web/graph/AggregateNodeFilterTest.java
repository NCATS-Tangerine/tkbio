package bio.knowledge.test.web.graph;

//package bio.knowledge.test.graph;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertSame;
//import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.assertEquals;
//
//import java.io.Serializable;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//import org.apache.commons.collections.CollectionUtils;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import bio.knowledge.graph.ConceptMapDisplay;
//import bio.knowledge.graph.jsonmodels.AggregateNodeFilter;
//import bio.knowledge.graph.jsonmodels.Edge;
//import bio.knowledge.graph.jsonmodels.EdgeData;
//import bio.knowledge.graph.jsonmodels.Edges;
//import bio.knowledge.graph.jsonmodels.Elements;
//import bio.knowledge.graph.jsonmodels.Node;
//import bio.knowledge.graph.jsonmodels.NodeData;
//import bio.knowledge.graph.jsonmodels.Nodes;
//
///*
// * DONE ignores edges not in subgraph connected to queried nodes
// * DONE selects subgraph of nodes
// * DONE name nodes based on semgroup while leaving Id invariant
// * DONE assign parent to semgroup
// * TODO 
// */
//public class AggregateNodeFilterTest {
//	
//	Elements elements;
//	ConceptMapDisplay cmd;
//	
//	@Before
//	public void initValues() {
//		Elements elements = new Elements();
//		ConceptMapDisplay cmd = new ConceptMapDisplay(elements);
//	};
//	
//	// test set 1: empty elements
//	// Query: empty, name=a
//	// Elements.Nodes: {}
//	// Elements.Edges: {}
//	@Test
//	public void testFilterOnEmptyElements() {
//		Elements elements = new Elements();
//		ConceptMapDisplay cmd = new ConceptMapDisplay(elements);
//		
////		NodeData nd = new NodeData("a", "a", "CHEM", "");
//		elements.getNodes().addNode(new Node(nd));		
//		// empty query
//		AggregateNodeFilter anf = cmd.createAggregateNodeFilter("", "");
//		assertNotNull(anf);	
//		assertEquals(false, anf.match());
//
//		// no match
//		AggregateNodeFilter anf1 = cmd.createAggregateNodeFilter("name", "a");
//		assertNotNull(anf1);
//		assertEquals(false, anf.match());
//		
//	};
//	
//	// test set 2: single node
//	@Test
//	public void testFilterOnSingleNode() {
//		Elements elements = new Elements();
//		ConceptMapDisplay cmd = new ConceptMapDisplay(elements);
//
//		// create node
////		NodeData nd = new NodeData("a", "a", "CHEM", "");
//		elements.getNodes().addNode(new Node(nd));
//		
//		// empty query
//		AggregateNodeFilter anf = cmd.createAggregateNodeFilter("", "");
//		assertNotNull(anf);	
//		System.out.println("2 empty "+anf.getAggregatedElements().toString());
//		assertEquals(false, anf.match());
//
//		// no match
//			// no nodes
//		AggregateNodeFilter anf1 = cmd.createAggregateNodeFilter("name", "b");
//		assertNotNull(anf1);
//		System.out.println("2 no match "+anf1.getAggregatedElements().toString());
//		assertEquals(false, anf1.match());
//		// invert all resulting modifications to elements
//		
//		// unique match
//			// one node
//			// no parent node
//		AggregateNodeFilter anf2 = cmd.createAggregateNodeFilter("name", "a");
//		assertNotNull(anf2);
//		System.out.println("2 match "+ anf2.getAggregatedElements().toString());
//		assertEquals(true, anf2.match());
//		// invert all resulting modifications to elements
//		
//		// many matches
//			// in this case only one unique match
//			// no parent node
//		AggregateNodeFilter anf3 = cmd.createAggregateNodeFilter("semgroup", "CHEM");
//		assertNotNull(anf3);
//		System.out.println("2 "+anf3.getAggregatedElements().toString());
//		assertEquals(true, anf3.match());
//		// invert all resulting modifications to elements
//
//	};
//		
//	// test set 3: some nodes
//	@Test
//	public void testFilterOnSomeNodesWithoutEdges() {
//		
//		Elements elements = new Elements();
//		ConceptMapDisplay cmd = new ConceptMapDisplay(elements);
//		
//		// create nodes
////		NodeData nd1 = new NodeData("a", "a", "CHEM", "");
//		elements.getNodes().addNode(new Node(nd1));
////		NodeData nd2 = new NodeData("b", "b", "CHEM", "");
//		elements.getNodes().addNode(new Node(nd2));
//		Nodes cmpr = new Nodes();
//		
//		// empty query
//		AggregateNodeFilter anf = cmd.createAggregateNodeFilter("", "");
//		assertNotNull(anf);	
//		System.out.println("3 "+anf.getAggregatedElements().toString());
//		assertEquals(false, anf.match());
//		System.out.println(cmpr);
//		assertEquals(0, anf.getAggregatedElements().getNodes().size());
//		assertEquals(0, anf.getAggregatedElements().getEdges().size());
//		
//		// no match
//			// no nodes
//			// no edges
//		AggregateNodeFilter anf1 = cmd.createAggregateNodeFilter("name", "c");
//		assertNotNull(anf1);
//		System.out.println("3 "+anf1.getAggregatedElements().toString());
//		assertEquals(false, anf1.match());
//		assertEquals(0, anf1.getAggregatedElements().getNodes().size());
//		assertEquals(0, anf1.getAggregatedElements().getEdges().size());
//		// invert all resulting modifications to elements
//
//		// unique match
//			// one node
//			// one parent node
//			// no edges
//		cmpr.addNode(new Node(nd1));
//		AggregateNodeFilter anf2 = cmd.createAggregateNodeFilter("name", "a");
//		assertNotNull(anf2);
//		System.out.println("3 unique "+anf2.getAggregatedElements().toString());
//		assertEquals(true, anf2.match());
//		assertEquals(cmpr.size()+1, anf2.getAggregatedElements().getNodes().size());
//		assertEquals(new Edges().size(), anf2.getAggregatedElements().getEdges().size());
//		// invert all resulting modifications to elements
//		
//		// many matches
//			// two nodes
//			// one parent node
//			// no edges
//		cmpr.addNode(new Node(nd2));
//		AggregateNodeFilter anf3 = cmd.createAggregateNodeFilter("semgroup", "CHEM");
//		assertNotNull(anf3);
//		System.out.println("3 before "+cmpr.getNodes());
//		System.out.println("3 many "+anf3.getAggregatedElements().toString());
//		assertEquals(true, anf3.match());
//		assertEquals(cmpr.getNodes().size()+1, anf3.getAggregatedElements().getNodes().size());
//		assertEquals(new Edges().size(), anf3.getAggregatedElements().getEdges().size());
//		// invert all resulting modifications to elements
//		
//	};
//	
//	// test set 4: some nodes and edges
//	@Test
//	public void testFilterOnSomeNodesWithEdges() {
//		
//		Elements elements = new Elements();		
//		ConceptMapDisplay cmd = new ConceptMapDisplay(elements);
//
//		// create edges
//		NodeData nd1 = new NodeData("a", "a", "CHEM", "");
//		elements.getNodes().addNode(new Node(nd1));
//		NodeData nd2 = new NodeData("b", "b", "CHEM", "");
//		elements.getNodes().addNode(new Node(nd2));
//		EdgeData ed1 = new EdgeData("a","b");
//		elements.getEdges().addEdge(new Edge(ed1));
//		EdgeData ed2 = new EdgeData("c","b");
//		elements.getEdges().addEdge(new Edge(ed2));
//		Nodes cmprN = new Nodes();
//		Edges cmprE = new Edges();
//
//		// empty query
//		AggregateNodeFilter anf = cmd.createAggregateNodeFilter("", "");
//		assertNotNull(anf);	
//		System.out.println("4 "+anf.getAggregatedElements().toString());
//		assertEquals(false, anf.match());
//		System.out.println(cmprN);
//		assertEquals(0, anf.getAggregatedElements().getNodes().size());
//		assertEquals(0, anf.getAggregatedElements().getEdges().size());
//		
//		// no match
//			// no nodes
//			// no edges
//		AggregateNodeFilter anf1 = cmd.createAggregateNodeFilter("name", "c");
//		assertNotNull(anf1);
//		System.out.println("4 "+anf1.getAggregatedElements().toString());
//		assertEquals(false, anf1.match());
//		assertEquals(0, anf1.getAggregatedElements().getNodes().size());
//		assertEquals(0, anf1.getAggregatedElements().getEdges().size());
//		// invert all resulting modifications to elements
//
//		// unique match
//			// one node
//			// one parent node
//			// one edge
//		cmprN.add(new Node(nd1));
//		AggregateNodeFilter anf2 = cmd.createAggregateNodeFilter("name", "a");
//		assertNotNull(anf2);
//		System.out.println("4 unique "+anf2.getAggregatedElements().toString());
//		assertEquals(true, anf2.match());
//		assertEquals(cmprN.size()+1, anf2.getAggregatedElements().getNodes().size());
//		assertEquals(1, anf2.getAggregatedElements().getEdges().size());
//		assert(anf2.getAggregatedElements().getEdges().contains(new Edge(ed1)));
//		// invert all resulting modifications to elements
//
//		
//		// many matches
//			// two nodes
//			// one parent node
//			// two edges
//		// TODO
//		cmprN.addNode(new Node(nd2));
////		AggregateNodeFilter anf3 = cmd.createAggregateNodeFilter("semgroup", "CHEM");
////		assertNotNull(anf3);
////		System.out.println("4 many target {nodes="+cmprN.toString()+", edges="+cmprE.toString()+"}");
////		System.out.println("4 many actual "+anf3.getAggregatedElements().toString());
////		assertEquals(true, anf3.match());
////		assertEquals(cmprN.size()+1, anf3.getAggregatedElements().getNodes().size());
////		assertEquals(2, anf3.getAggregatedElements().getEdges().size());
////		assert(anf3.getAggregatedElements().getEdges().contains(new Edge(edp1)));
////		assert(anf3.getAggregatedElements().getEdges().contains(new Edge(edp2)));
//
//		// invert all resulting modifications to elements
//
//	};
//	
//	@Test
//	public void testUnifySameNamedNodes() {
//		Elements elements = new Elements();		
//		ConceptMapDisplay cmd = new ConceptMapDisplay(elements);
//
//		// create edges
//		NodeData nd1 = new NodeData("1", "a", "CHEM", "");
//		elements.getNodes().addNode(new Node(nd1));
//		NodeData nd2 = new NodeData("2", "a", "PROC", "");
//		elements.getNodes().addNode(new Node(nd2));
//		EdgeData ed1 = new EdgeData("1","2");
//		elements.getEdges().addEdge(new Edge(ed1));
//		EdgeData ed2 = new EdgeData("3","2");
//		elements.getEdges().addEdge(new Edge(ed2));
//		EdgeData ed3 = new EdgeData("5","4");
//		elements.getEdges().addEdge(new Edge(ed3));
//		
//		Nodes cmprN = new Nodes();
//		Edges cmprE = new Edges();
//		
//		AggregateNodeFilter anf = cmd.createAggregateNodeFilter("name", "a");
////		assertArrayEquals(cmprN, anf.getAggregatedElements().getNodes());
////		assertArrayEquals(cmprE, anf.getAggregatedElements().getEdges());
//		// get membership before conversion
//		System.out.println("5 before nodes " + elements.getNodes());
//		System.out.println("5 actual nodes " + anf.getAggregatedElements().getNodes());
//		System.out.println("5 before edges " + elements.getEdges());
//		System.out.println("5 actual edges " + anf.getAggregatedElements().getEdges());
//
//		assertEquals(elements.getNodes().size() + 1, anf.getAggregatedElements().getNodes().size());
//		assertEquals(elements.getEdges().size() - 1, anf.getAggregatedElements().getEdges().size());
//		assert(anf.getAggregatedElements().getEdges().contains(new Edge(ed1)));
//		assert(anf.getAggregatedElements().getEdges().contains(new Edge(ed2)));
//		
//	};
//	
//	@Test
//	public void testValues() {
//		
//		Elements elements = new Elements();		
//		ConceptMapDisplay cmd = new ConceptMapDisplay(elements);
//		
//		NodeData nd1 = new NodeData("1", "a", "CHEM", "");
//		elements.getNodes().addNode(new Node(nd1));
//		NodeData nd2 = new NodeData("2", "a", "PROC", "");
//		elements.getNodes().addNode(new Node(nd2));
//		EdgeData ed1 = new EdgeData("1","2");
//		elements.getEdges().addEdge(new Edge(ed1));
//		EdgeData ed2 = new EdgeData("3","2");
//		elements.getEdges().addEdge(new Edge(ed2));
//		EdgeData ed3 = new EdgeData("5","4");
//		elements.getEdges().addEdge(new Edge(ed3));
//		
//		/* input
//		 * ("a")
//		 * ("a")
//		 * (a -> a)
//		 * output
//		 * ("chem")
//		 * ("proc")
//		 * ("chem" -> "proc")
//		 * ("proc" -> "3")
//		 */
//		
//		AggregateNodeFilter anf = cmd.createAggregateNodeFilter("name", "a");
//		System.out.println("Old: "+elements);
//		System.out.println("Transformed: "+anf.getAggregatedElements());
//		System.out.println("Reversed: "+anf.getOldElements());
//
//	}
//	
//	@Test
//	public void testMergeAndReverse() {
//		
//		Elements elements = new Elements();		
//		ConceptMapDisplay cmd = new ConceptMapDisplay();
//		
//		NodeData nd1 = new NodeData("1", "a", "CHEM", "");
//		elements.getNodes().addNode(new Node(nd1));
//		NodeData nd2 = new NodeData("2", "a", "PROC", "");
//		elements.getNodes().addNode(new Node(nd2));
//		EdgeData ed1 = new EdgeData("1","2");
//		elements.getEdges().addEdge(new Edge(ed1));
//		EdgeData ed2 = new EdgeData("3","2");
//		elements.getEdges().addEdge(new Edge(ed2));
//		EdgeData ed3 = new EdgeData("5","4");
//		elements.getEdges().addEdge(new Edge(ed3));
//		
//		cmd.setElements(elements);
//		
//		/* input
//		 * ("a")
//		 * ("a")
//		 * (a -> a)
//		 * output
//		 * ("chem")
//		 * ("proc")
//		 * ("chem" -> "proc")
//		 * ("proc" -> "3")
//		 */
//		AggregateNodeFilter anf = cmd.createAggregateNodeFilter("name", "a");
//		cmd.mergeElements(anf.getAggregatedElements());
//		
//	}
//	
//	@After
//	public void closeValues() {
//		initValues();
//	};
//
//}
