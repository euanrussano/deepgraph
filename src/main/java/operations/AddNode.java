package operations;

import java.util.List;

import org.apache.commons.math4.linear.RealMatrix;

import nodes.Node;

public class AddNode extends Node{
	
	public void forward(List<Node> inputNodes) {
		// Assume 2 inputs
		RealMatrix node1Matrix = inputNodes.get(0).getValue();
		RealMatrix node2Matrix = inputNodes.get(0).getValue();
		
		RealMatrix sumMatrix = node1Matrix.add(node2Matrix);
		
		localgrad(inputNodes);
		
		this.setValue(sumMatrix);
	}
			
	public void localgrad(List<Node> nodes) {
		for (Node node: nodes) {
			this.setDvalue(node, node.getValue().scalarMultiply(0.0).scalarAdd(1.0));
		}
	}

}
