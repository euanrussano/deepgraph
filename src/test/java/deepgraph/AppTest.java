package deepgraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math4.linear.MatrixUtils;
import org.apache.commons.math4.linear.RealMatrix;
import org.jgrapht.DirectedGraph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;
import org.junit.Test;

import gradientTape.GradientTape;
import graphs.TF;
import nodes.Node;
import nodes.Variable;
import operations.AddConstantNode;
import operations.AddNode;
import operations.ExpNode;
import operations.InvertNode;
import operations.L2Node;
import operations.MatrixMultiplyNode;
import operations.MultiplyConstantNode;
import operations.MultiplyNode;
import operations.ReduceSumNode;
import operations.SquareNode;

public class AppTest {
		
	/*
	 * Test a vector implementation
	 * slide 65 http://cs231n.stanford.edu/slides/2017/cs231n_2017_lecture4.pdf
	 */
	@Test
	public void test1() {
		Node w = new Variable(new double[][] {{0.1,0.5}, {-0.3,0.8}});
		Node x = new Variable(new double[][] {{0.2, 0.4},{0.3,0.5},{0.3,0.5}});
		
		Node a = new MatrixMultiplyNode();
		
		Node w2 = new Variable(new double[][] {{0.1,0.5}, {-0.3,0.8}});
		
		Node a2 = new MatrixMultiplyNode();
		
		Node out = new ReduceSumNode();
		
		DirectedGraph<Node, DefaultEdge> directedGraph 
		= new DefaultDirectedGraph<Node, DefaultEdge>(DefaultEdge.class);
		directedGraph.addVertex(w);
		directedGraph.addVertex(x);
		directedGraph.addVertex(a);
		directedGraph.addVertex(w2);
		directedGraph.addVertex(a2);
		directedGraph.addVertex(out);
		
		directedGraph.addEdge(x, a);
		directedGraph.addEdge(w, a);
		directedGraph.addEdge(a, a2);
		directedGraph.addEdge(w2, a2);
		directedGraph.addEdge(a2, out);
		
		
		TopologicalOrderIterator<Node, DefaultEdge> toi 
		  = new TopologicalOrderIterator<Node, DefaultEdge>(directedGraph);
		
		ArrayList<Node> nodes = new ArrayList<Node>();
		System.out.println("forward pass");
		while(toi.hasNext()) {
			Node node = (Node)toi.next();
			List<Node> pred = Graphs.predecessorListOf(directedGraph, node);
			if (!pred.isEmpty()){
				node.forward(pred);
			}
			nodes.add(node);
		}
		
		System.out.println("a = "+ a.getValue());
		System.out.println("out = "+ out.getValue());
		System.out.println("doutda2 = "+ out.getDvalue(a2));
		System.out.println("dadx = "+ a.getDvalue(x));
		System.out.println("dadw = "+ a.getDvalue(w));
		System.out.println("da2dw2 = "+ a2.getDvalue(w2));
		
		Collections.reverse(nodes);
		
		System.out.println("\nbackward pass");
		for (Node node : nodes) {
			List<Node> pred = Graphs.predecessorListOf(directedGraph, node);
			List<Node> succ = Graphs.successorListOf(directedGraph, node);
			node.backward(pred, succ);
			
		}
		
		System.out.println("Gx = " + GradientTape.getInstance().getDvalue(x));
		System.out.println("Gw = " + GradientTape.getInstance().getDvalue(w));
		
	}
	
	// Building above test1 with sigmoid function (expanded into subnodes)
	@Test
	public void test2() {
		Node w = new Variable(new double[][] {{0.1,0.5}, {-0.3,0.8}});
		Node x = new Variable(new double[][] {{0.2, 0.4},{0.3,0.5},{0.3,0.5}});
		
		Node a = new MatrixMultiplyNode();
		
		Node multiplyMinusOne = new MultiplyConstantNode(-1.0);
		
		Node expNode = new ExpNode();
		
		Node addOne = new AddConstantNode(1.0);
		
		Node invertNode = new InvertNode();
		
		Node w2 = new Variable(new double[][] {{0.1,0.5}, {-0.3,0.8}});
		
		Node a2 = new MatrixMultiplyNode();
		
		Node out = new ReduceSumNode();
		
		DirectedGraph<Node, DefaultEdge> directedGraph 
		= new DefaultDirectedGraph<Node, DefaultEdge>(DefaultEdge.class);
		directedGraph.addVertex(w);
		directedGraph.addVertex(x);
		directedGraph.addVertex(a);
		directedGraph.addVertex(multiplyMinusOne);
		directedGraph.addVertex(expNode);
		directedGraph.addVertex(addOne);
		directedGraph.addVertex(invertNode);
		directedGraph.addVertex(w2);
		directedGraph.addVertex(a2);
		directedGraph.addVertex(out);
		
		directedGraph.addEdge(x, a);
		directedGraph.addEdge(w, a);
		directedGraph.addEdge(a, multiplyMinusOne);
		directedGraph.addEdge(multiplyMinusOne, expNode);
		directedGraph.addEdge(expNode, addOne);
		directedGraph.addEdge(addOne, invertNode);
		directedGraph.addEdge(invertNode, a2);
		directedGraph.addEdge(w2, a2);
		directedGraph.addEdge(a2, out);
		
		
		TopologicalOrderIterator<Node, DefaultEdge> toi 
		  = new TopologicalOrderIterator<Node, DefaultEdge>(directedGraph);
		
		ArrayList<Node> nodes = new ArrayList<Node>();
		System.out.println("forward pass");
		while(toi.hasNext()) {
			Node node = (Node)toi.next();
			List<Node> pred = Graphs.predecessorListOf(directedGraph, node);
			if (!pred.isEmpty()){
				node.forward(pred);
			}
			nodes.add(node);
		}
		
		System.out.println("a = "+ a.getValue());
		System.out.println("invertNode = "+ invertNode.getValue());
		System.out.println("a2 = "+ a2.getValue());
		System.out.println("out = "+ out.getValue());
		System.out.println("doutda2 = "+ out.getDvalue(a2));
		System.out.println("dadx = "+ a.getDvalue(x));
		System.out.println("dadw = "+ a.getDvalue(w));
		System.out.println("da2dw2 = "+ a2.getDvalue(w2));
		
		Collections.reverse(nodes);
		
		System.out.println("\nbackward pass");
		for (Node node : nodes) {
			List<Node> pred = Graphs.predecessorListOf(directedGraph, node);
			List<Node> succ = Graphs.successorListOf(directedGraph, node);
			node.backward(pred, succ);
			
		}
		
		System.out.println("Gw2 = " + GradientTape.getInstance().getDvalue(w2));
		System.out.println("Ga = " + GradientTape.getInstance().getDvalue(a));
		System.out.println("Gx = " + GradientTape.getInstance().getDvalue(x));
		System.out.println("Gw = " + GradientTape.getInstance().getDvalue(w));
		
	}
	
	// Copy of above but with composite operation Sigmoid and TF "interface"
	@Test
	public void test3() {
		Node w = new Variable(new double[][] {{0.1,0.5}, {-0.3,0.8}});
		Node x = new Variable(new double[][] {{0.2, 0.4},{0.3,0.5},{0.3,0.5}});
		
		Node a = TF.matmul(x, w);
		Node h = TF.sigmoid(a);
		
		Node w2 = new Variable(new double[][] {{0.1,0.5}, {-0.3,0.8}});
		Node a2 = TF.matmul(h, w2);
		
		Node out = TF.reduceSum(a2);
		
		TF.run();
		
		System.out.println("a = "+ a.getValue());
		System.out.println("h = "+ h.getValue());
		System.out.println("a2 = "+ a2.getValue());
		System.out.println("out = "+ out.getValue());
		System.out.println("doutda2 = "+ out.getDvalue(a2));
		System.out.println("dadx = "+ a.getDvalue(x));
		System.out.println("dadw = "+ a.getDvalue(w));
		System.out.println("da2dw2 = "+ a2.getDvalue(w2));
		
		TF.backprop();
		
		System.out.println("Gw2 = " + GradientTape.getInstance().getDvalue(w2));
		System.out.println("Ga = " + GradientTape.getInstance().getDvalue(a));
		System.out.println("Gx = " + GradientTape.getInstance().getDvalue(x));
		System.out.println("Gw = " + GradientTape.getInstance().getDvalue(w));
		
	}

}
