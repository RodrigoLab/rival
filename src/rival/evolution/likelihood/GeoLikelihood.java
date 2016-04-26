package rival.evolution.likelihood;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import beast.core.Description;
import beast.core.Distribution;
import beast.core.Input;
import beast.core.State;
import beast.core.parameter.RealParameter;
import beast.evolution.tree.TraitSet;
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
