package nodes;

import java.util.List;

import org.apache.commons.math4.linear.MatrixUtils;

public class Variable extends Node {
	
	public Variable(double[][] valueMatrix) {
		super();
		
		this.setValue(MatrixUtils.createRealMatrix(valueMatrix));
	}

	@Override
	public void forward(List<Node> inputNodes){}

	@Override
	public void backward(List<Node> inputNodes, List<Node> outputNodes) {}

}
