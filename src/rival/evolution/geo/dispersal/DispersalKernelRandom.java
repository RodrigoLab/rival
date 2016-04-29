package rival.evolution.geo.dispersal;

import java.util.ArrayList;
import java.util.List;

import beast.core.Input;
import beast.core.parameter.RealParameter;
import beast.evolution.alignment.Alignment;
import beast.evolution.alignment.Taxon;

public class DispersalKernelRandom extends DispersalKernel {

	final public Input<RealParameter> randomParameterInput = new Input<>("param", 
			"semi-random parameter for testing purpose");
	
//	final public Input<List<Taxon>> taxonsetInput = new Input<>("taxon", "list of taxa making up the set",
//			new ArrayList<>());
//
//	final public Input<Alignment> alignmentInput = new Input<>("alignment",
//			"alignment where each sequence represents a taxon");

	RealParameter param;
	

	private double par;
	private double storedPar;
	
	@Override
	public void initAndValidate() {
		
		
		
		distanceMatrix = distanceMatrixInput.get();
		param = randomParameterInput.get();
		
		int dim = distanceMatrix.length;
		//TODO: check row==col
		kernelMatrix = new double[dim][dim];
		
		par = param.getValue();
		calculateKernel();
	}
	
	protected void calculateKernel() {
		par = param.getValue();
		for (int i = 0; i < distanceMatrix.length; i++) {
			for (int j = i; j < distanceMatrix[i].length; j++) {
				kernelMatrix[i][j] = distanceMatrix[i][j] * par;
				kernelMatrix[j][i] = kernelMatrix[i][j];
			}
		}
		
	}

	

	//TODO: implement these three when the island start moving
	@Override
	protected void store(){
		storedPar = par;
		super.store();
	}
	@Override
	protected void restore(){
		par = storedPar;
		param.setValue(storedPar);
		
		super.restore();
	}
	
	@Override
	protected boolean requiresRecalculation(){
		if( randomParameterInput.get().getValue() != storedPar ){
			return true;
		}
//		return super.requiresRecalculation();
		return false;
	}


}
