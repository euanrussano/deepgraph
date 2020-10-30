package gradientTape;

import java.util.HashMap;

import org.apache.commons.math4.linear.RealMatrix;

import nodes.Node;

public class GradientTape {
	
	
	HashMap<Node, RealMatrix> dvalueMatrix = new HashMap<Node, RealMatrix>();
	private static GradientTape singleInstance = null;
	
	private GradientTape() {
		
	}
	
	public static GradientTape getInstance() {
		if (singleInstance==null) {
			singleInstance = new GradientTape();
		}
		
		return singleInstance;
	}
	
	
	public void setDvalue(Node node, RealMatrix accum_grad) {
		this.dvalueMatrix.put(node, accum_grad);
		
	}

	public RealMatrix getDvalue(Node outputNode) {

		return this.dvalueMatrix.get(outputNode);
	}

}
