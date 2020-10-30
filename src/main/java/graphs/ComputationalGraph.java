package graphs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.DirectedGraph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;

import nodes.Node;

public class ComputationalGraph{
	
	private static ComputationalGraph singleInstance;
	DirectedGraph<Node, DefaultEdge> directedGraph 
	= new DefaultDirectedGraph<Node, DefaultEdge>(DefaultEdge.class);
	
	private ArrayList<Node> nodes = new ArrayList<Node>();
	
	private ComputationalGraph() {}
	
	public static ComputationalGraph getInstance() {
		if (singleInstance==null) {
			singleInstance = new ComputationalGraph();
		}
		
		return singleInstance;
	}
	
	public void connectNodes(Node node1, Node node2) {
		if (!directedGraph.containsVertex(node1)) {
			directedGraph.addVertex(node1);
		}
		
		if (!directedGraph.containsVertex(node2)) {
			directedGraph.addVertex(node2);
		}
		
		directedGraph.addEdge(node1, node2);
	}
	
	public void forward() {
		
		TopologicalOrderIterator<Node, DefaultEdge> toi 
		  = new TopologicalOrderIterator<Node, DefaultEdge>(directedGraph);
		
		nodes = new ArrayList<Node>();
		
		while(toi.hasNext()) {
			
			Node node = (Node)toi.next();
			List<Node> pred = Graphs.predecessorListOf(directedGraph, node);
			if (!pred.isEmpty()){
				node.forward(pred);
			}
			
			nodes.add(node);
		}
		
		Collections.reverse(nodes);
	}
	
	public void backward() {
		
		for (Node node : nodes) {
			List<Node> pred = Graphs.predecessorListOf(directedGraph, node);
			List<Node> succ = Graphs.successorListOf(directedGraph, node);
			node.backward(pred, succ);
			
		}
	}
	
	
}
