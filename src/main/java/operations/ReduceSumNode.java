package operations;

import java.util.List;

import org.apache.commons.math4.linear.MatrixUtils;
import org.apache.commons.math4.linear.RealMatrix;

import gradientTape.GradientTape;
import nodes.Node;

public class ReduceSumNode extends Node {
	
	public void forward(List<Node> inputNodes) {
		// Allow only one input node
		RealMatrix A = inputNodes.get(0).getValue();
		
		double[][] sum = new double[][] {{0.0}};
		for (int i=0; i< A.getRowDimension(); i++) {
			for (int j=0; j< A.getColumnDimension(); j++) {
				sum[0][0] += A.getEntry(i, j);
				
			}	
		}
				
		localgrad(inputNodes);
		
		this.setValue(MatrixUtils.createRealMatrix(sum));
	}
	
	public void localgrad(List<Node> nodes) {
		for (Node node : nodes) {
			RealMatrix result = node.getValue().copy().scalarMultiply(0.0).scalarAdd(1.0);
			
			this.setDvalue(node, result);
		}
		
	}
	
	public void backward(List<Node> inputNodes, List<Node> outputNodes) {
		RealMatrix accum_grad = this.getValue().scalarMultiply(0.0).scalarAdd(1.0);
		if (outputNodes.isEmpty()) {
			GradientTape.getInstance().setDvalue(this, accum_grad);
			
			// Assume one input node
			Node node = inputNodes.get(0);
			
			accum_grad = this.getDvalue(node);
					
			GradientTape.getInstance().setDvalue(node, accum_grad);
						
		} else {
			// Assume one input node
			Node node = inputNodes.get(0);
			
			accum_grad = GradientTape.getInstance().getDvalue(node);
			RealMatrix doutdnode = accum_grad;
					
			GradientTape.getInstance().setDvalue(node, doutdnode);
		}
		
		
	}
	
}
