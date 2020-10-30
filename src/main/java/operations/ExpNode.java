package operations;

import java.util.List;

import org.apache.commons.math4.linear.DefaultRealMatrixChangingVisitor;
import org.apache.commons.math4.linear.RealMatrix;

import nodes.Node;

public class ExpNode extends Node {
	
	public class ExpVisitor extends DefaultRealMatrixChangingVisitor {
		
		public double visit(int row, int column, double value) {
			
				return Math.exp(value);
		}
	}
	
	ExpVisitor visitor = new ExpVisitor();
	
	public void forward(List<Node> inputNodes) {
		// should have only one input
		RealMatrix nodeMatrix = inputNodes.get(0).getValue();
		
		RealMatrix outMatrix = nodeMatrix.copy();
		outMatrix.walkInOptimizedOrder(visitor);
		
		localgrad(inputNodes);
		
		this.setValue(outMatrix);
	}
	
	public void localgrad(List<Node> nodes) {
		// should have only one input
		RealMatrix nodeMatrix = nodes.get(0).getValue();
		
		RealMatrix outMatrix = nodeMatrix.copy();
		outMatrix.walkInOptimizedOrder(visitor);
		
		this.setDvalue(nodes.get(0),outMatrix);
		
	}
	
	
}
