package operations;

import java.util.List;

import org.apache.commons.math4.linear.DefaultRealMatrixChangingVisitor;
import org.apache.commons.math4.linear.RealMatrix;

import nodes.Node;

public class InvertNode extends Node {
	
	public class InvertVisitor extends DefaultRealMatrixChangingVisitor {
		
		String type;
		public double visit(int row, int column, double value) {
			if (type == "forward") {
				return 1.0/value;
			}else {
				return -1.0/Math.pow(value, 2.0);
			}
		}
	}
	
	InvertVisitor visitor = new InvertVisitor();
	
	public void forward(List<Node> inputNodes) {
		// should have only one input
		visitor.type = "forward";
		
		RealMatrix nodeMatrix = inputNodes.get(0).getValue();
		RealMatrix outMatrix = nodeMatrix.copy();
		
		outMatrix.walkInOptimizedOrder(visitor);
		
		
		localgrad(inputNodes);
		
		this.setValue(outMatrix);
	}
	
	public void localgrad(List<Node> nodes) {
		// should have only one input
		visitor.type = "backward";
		
		RealMatrix nodeMatrix = nodes.get(0).getValue();
		RealMatrix outMatrix = nodeMatrix.copy();
		outMatrix.walkInOptimizedOrder(visitor);
		
		this.setDvalue(nodes.get(0), outMatrix);
		
	}
}
