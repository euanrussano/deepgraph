package operations;

import java.util.List;

import org.apache.commons.math4.linear.RealMatrix;

import nodes.Node;
import gradientTape.GradientTape;

public class MatrixMultiplyNode extends Node {
		
	public void forward(List<Node> inputNodes) {
		// Allow only two input nodes
		RealMatrix A = inputNodes.get(0).getValue();
		RealMatrix B = inputNodes.get(1).getValue();
		
		RealMatrix prodMatrix = A.multiply(B);
				
		localgrad(inputNodes);
		
		this.setValue(prodMatrix);
	}
	
	
	public void localgrad(List<Node> nodes) {
		// Assume only 2 inputs
		RealMatrix node1Matrix = nodes.get(0).getValue();
		RealMatrix node2Matrix = nodes.get(1).getValue();
		
		RealMatrix onesMatrix = node1Matrix.copy().scalarMultiply(0.0).scalarAdd(1.0);
		// der wrt node2
		RealMatrix dy1 = onesMatrix.multiply(node2Matrix.transpose());
		RealMatrix dy2 = node1Matrix.transpose().multiply(onesMatrix);
		
		this.setDvalue(nodes.get(0), dy1);
		this.setDvalue(nodes.get(1), dy2);
		
		
	}
	
	public void backward(List<Node> inputNodes, List<Node> outputNodes) {
		RealMatrix accum_grad = this.getValue().scalarMultiply(0.0).scalarAdd(1.0);
		if (outputNodes.isEmpty()) {
			GradientTape.getInstance().setDvalue(this, accum_grad);
		}
		Node node1 = inputNodes.get(0);
		Node node2 = inputNodes.get(1);
		
		accum_grad = GradientTape.getInstance().getDvalue(this);
		RealMatrix doutdnode1 = accum_grad.multiply(node2.getValue().transpose());
		RealMatrix doutdnode2 = node1.getValue().transpose().multiply(accum_grad);
		
		GradientTape.getInstance().setDvalue(node1, doutdnode1);
		GradientTape.getInstance().setDvalue(node2, doutdnode2);
	}
}
