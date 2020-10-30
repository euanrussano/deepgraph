package operations;

import java.util.List;

import org.apache.commons.math4.linear.DefaultRealMatrixChangingVisitor;
import org.apache.commons.math4.linear.RealMatrix;

import nodes.Node;

public class ReluNode extends Node {
	
	public class ReluVisitor extends DefaultRealMatrixChangingVisitor {
		
		public double visit(int row, int column, double value) {
			
				return value > 0.0 ? value : 0.0;
		}
	}
	
	public class dReluVisitor extends DefaultRealMatrixChangingVisitor {
		
		public double visit(int row, int column, double value) {
			
				return value > 0.0 ? 1.0 : 0.0;
		}
	}
	
	@Override
	public void forward(List<Node> inputNodes) {
		// Assume one input node
		RealMatrix node1Matrix = inputNodes.get(0).getValue();
		
		RealMatrix reluMatrix = node1Matrix.copy();
		reluMatrix.walkInOptimizedOrder(new ReluVisitor());
		
		this.localgrad(inputNodes);
		
		this.setValue(reluMatrix);

	}
	
	
	public void localgrad(List<Node> nodes) {
		// Assume one input node
		RealMatrix node1Matrix = nodes.get(0).getValue();
		
		RealMatrix reluMatrix = node1Matrix.copy();
		reluMatrix.walkInOptimizedOrder(new dReluVisitor());
		
		this.setDvalue(nodes.get(0), reluMatrix);

	}

}
