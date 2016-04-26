package rival.evolution.geo.dispersal;

import java.util.ArrayList;
import java.util.List;

import beast.core.CalculationNode;
import beast.core.Description;
import beast.core.Input;
import beast.core.Input.Validate;
import beast.core.parameter.RealParameter;
import beast.evolution.alignment.Alignment;
import beast.evolution.alignment.Taxon;

@Description("Calculate dispersal kernel using geo-distance and dispersion parameter")
public abstract class AbstractDispersalKernel extends CalculationNode{

	
	final public Input<double[][]> distanceMatrixInput = new Input<>
		("distMatrix", "Distance matrix used perform calculation in the kernel",
				Validate.REQUIRED);

	protected double[][] distanceMatrix;

	protected double[][] kernelMatrix;
	
	
	protected abstract void calculateKernel();
		
	public double[][] getKernel(){
		return kernelMatrix;
		
	}
	

}
