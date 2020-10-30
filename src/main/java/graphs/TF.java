package graphs;

import java.util.List;

import org.apache.commons.math4.linear.RealMatrix;

import gradientTape.GradientTape;
import nodes.Node;
import operations.AddConstantNode;
import operations.AddNode;
import operations.ExpNode;
import operations.InvertNode;
import operations.MatrixMultiplyNode;
import operations.MultiplyConstantNode;
import operations.ReduceSumNode;
import operations.ReluNode;

public class TF{
	
	public static Node add(Node node1, Node node2) {
		Node addNode = new AddNode();
		ComputationalGraph.getInstance().connectNodes(node1, addNode);
		ComputationalGraph.getInstance().connectNodes(node2, addNode);
		
		return addNode;
	}
	
	public static Node matmul(Node node1, Node node2) {
		Node matmulNode = new MatrixMultiplyNode();
		ComputationalGraph.getInstance().connectNodes(node1, matmulNode);
		ComputationalGraph.getInstance().connectNodes(node2, matmulNode);
		
		return matmulNode;
	}
	
	public static Node sigmoid(Node node) {
		
		Node multiplyMinusOne = new MultiplyConstantNode(-1.0);
		Node expNode = new ExpNode();
		Node addOne = new AddConstantNode(1.0);
		Node invertNode = new InvertNode();
		
		ComputationalGraph.getInstance().connectNodes(node, multiplyMinusOne);
		ComputationalGraph.getInstance().connectNodes(multiplyMinusOne, expNode);
		ComputationalGraph.getInstance().connectNodes(expNode, addOne);
		ComputationalGraph.getInstance().connectNodes(addOne, invertNode);
				
		return invertNode;
	}
	
	public static Node reduceSum(Node node) {
		Node reduceNode = new ReduceSumNode();
		
		ComputationalGraph.getInstance().connectNodes(node, reduceNode);
		
		return reduceNode;
	}
	
	public static Node relu(Node node) {
		Node reluNode = new ReluNode();
		
		ComputationalGraph.getInstance().connectNodes(node, reluNode);
		
		return reluNode;
	}
	
	public static void run() {
		ComputationalGraph.getInstance().forward();
	}
	
	public static void backprop() {
		ComputationalGraph.getInstance().backward();
	}
	
	public static RealMatrix getGrad(Node node) {
		return GradientTape.getInstance().getDvalue(node);
	}
}
