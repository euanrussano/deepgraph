package nodes;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.math4.linear.RealMatrix;

import gradientTape.GradientTape;
import graphs.ComputationalGraph;
import operations.AddNode;
import operations.MatrixMultiplyNode;

public abstract class Node {
	
	RealMatrix value;
	HashMap<Node, RealMatrix> dvalue = new HashMap<Node, RealMatrix>();
	
	
	public RealMatrix getDvalue(Node node) {
		RealMatrix zeros = node.value.copy().scalarMultiply(0.0);
		return dvalue.getOrDefault(node, zeros);
	}

	public void setDvalue(Node node, RealMatrix valueMatrix) {
		this.dvalue.put(node, valueMatrix);
	}
	
	public RealMatrix getValue() {
		return this.value;
	}
	
	
	public void setValue(RealMatrix valueMatrix) {
		this.value = valueMatrix;
	}
	
	public abstract void forward(List<Node> inputNodes);
	
	
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
			
			accum_grad = GradientTape.getInstance().getDvalue(this);
			//System.out.println("accum_grad" + accum_grad);
			RealMatrix localGrad = this.getDvalue(node);
			
			RealMatrix doutdnode = accum_grad.copy();
			for (int i = 0; i < accum_grad.getRowDimension(); i++) {
				for (int j = 0; j < accum_grad.getColumnDimension(); j++) {
					
					double dvalue_ = accum_grad.getEntry(i, j) *localGrad.getEntry(i, j);
					doutdnode.setEntry(i, j, dvalue_);
					
				}	
			}
			
			GradientTape.getInstance().setDvalue(node, doutdnode);
		}
		
	}
}
