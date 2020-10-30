package operations;

import java.util.List;

import org.apache.commons.math4.linear.RealMatrix;

import nodes.Node;

public class MultiplyConstantNode extends Node {
	
		
	double constant;
	
	public MultiplyConstantNode(double constant) {
		super();
		this.constant = constant;
	}
	
	@Override
	public void forward(List<Node> inputNodes) {
		// Assume one node input
		RealMatrix outMatrix = inputNodes.get(0).getValue().scalarMultiply(constant);
		
		localgrad(inputNodes);
		this.setValue(outMatrix);

	}
	
	
	public void localgrad(List<Node> nodes) {
		// should have only one input
		RealMatrix doutdnode = nodes.get(0).getValue().copy().scalarMultiply(0.0).scalarAdd(constant);
		
		this.setDvalue(nodes.get(0), doutdnode);
		
	}

}
