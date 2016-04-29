package rival.evolution.likelihood;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import beast.core.Description;
import beast.core.Distribution;
import beast.core.Input;
import beast.core.State;
import beast.core.parameter.RealParameter;
import beast.evolution.tree.TraitSet;
import beast.util.Randomizer;
import rival.evolution.geo.dispersal.DispersalKernel;
import rival.evolution.tree.RivalTree;

@Description("GeoLikelihood based on the phyloland paper")
public class GeoLikelihood extends Distribution {
	
	final public Input<RealParameter> tauInput = new Input<>("tau",  "tau");
	final public Input<RealParameter> lambdaInput = new Input<>("lambda",  "lambda");
//	final public Input<List<RealParameter>> lambdaInput = new Input<>("lambda",  "lambda");
	final public Input<RivalTree> treeInput = new Input<>("tree",  "Rival tree input");
	final public Input<List<DispersalKernel>> KernelInput = new Input<>("kernel",  "Dispersal Kernel",
			new ArrayList<>() );
	
//    final public Input<List<TraitSet>> m_traitList = new Input<>("trait",
//            "trait information for initializing traits (like node dates) in the tree",
//            new ArrayList<>());
	
	double tau;
	double lambda;
	
	
	@Override
	public void initAndValidate() {
		//TODO: Implemste lots of check here
		calculateLogP() ;
    }

	@Override
	public double calculateLogP() {
		
		RivalTree tree = treeInput.get();
		tree.calculateNodeInfo();
		int[][] occupationMatrix = tree.getOccupationMatrix();
		double[] intervals = tree.getIntervals();
		
		tau = tauInput.get().getValue();
		lambda = lambdaInput.get().getValue();
		
		List<DispersalKernel> kernelList = KernelInput.get();
		
		for (int i = 0; i < intervals.length; i++) {
			//Intervals starts at the root
			for (int l = 0; l < kernelList.size(); l++) {
				
				double[][] kernelMatrix = kernelList.get(l).getKernel();
				for (int j = 0; j < kernelMatrix.length; j++) {
					for (int j2 = 0; j2 < kernelMatrix[j].length; j2++) {
						//TODO: Implement the real likelihood function
						
					}
				}
				
			}
		}
//		occupationMatrix
		double sum = 0;
		int[] locations = tree.getAllLocationData();
//		System.out.println("GeoLikelihood: locations: "+ Arrays.toString(locations));
		for (int i = 0; i < locations.length; i++) {
			sum+= locations[i];
		}
//		System.out.println("\n"+ sum +"\t"+ occupationMatrix.length);
//		sum /= occupationMatrix.length;
		
        logP = Math.exp(  -  (tau-0.5)*(tau-0.5) /lambda/lambda  ) * sum;//
        return logP;
    }
	
	@Override
	protected boolean requiresRecalculation(){
		//TODO: Implement this part properly!!
		return super.requiresRecalculation();
	}
	
    @Override
    public void store() {
    	
        super.store();
    }

    @Override
    public void restore() {
        super.restore();
    }
	
	@Override
	public List<String> getArguments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getConditions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sample(State state, Random random) {
		// TODO Auto-generated method stub
		
	}

}
