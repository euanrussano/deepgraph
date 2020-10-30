package operations;

import java.util.List;

import org.apache.commons.math4.linear.MatrixUtils;
import org.apache.commons.math4.linear.RealMatrix;

import nodes.Node;

public class L2Node extends Node {
			
	public void forward(List<Node> inputNodes) {
		// Assume one input Node
		RealMatrix node1Matrix =inputNodes.get(0).getValue(); 
		
		RealMatrix l2Matrix = MatrixUtils.createRealMatrix(new double[][] {{0.0}});
		for (int i = 0; i < node1Matrix.getRowDimension(); i++) {
			for (int j = 0; j < node1Matrix.getColumnDimension(); j++) {
				double value = Math.pow(node1Matrix.getEntry(i, j), 2.0);
				l2Matrix.setEntry(0, 0, value);
			}	
		}
		
		localgrad(inputNodes);
		
		this.setValue(l2Matrix);
	}
	
	
	public void localgrad(List<Node> nodes) {
		// Assume one input Node
		RealMatrix node1Matrix =nodes.get(0).getValue(); 
		
		RealMatrix l2Matrix = MatrixUtils.createRealMatrix(new double[][] {{0.0}});
		for (int i = 0; i < node1Matrix.getRowDimension(); i++) {
			for (int j = 0; j < node1Matrix.getColumnDimension(); j++) {
				double value = 2.0*node1Matrix.getEntry(i, j);
				l2Matrix.setEntry(0, 0, value);
			}	
		}
		
		this.setDvalue(nodes.get(0), l2Matrix);
	}
	
}
