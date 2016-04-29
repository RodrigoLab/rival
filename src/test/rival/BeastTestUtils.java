package test.rival;

import java.util.ArrayList;

import beast.core.Operator;
import beast.core.parameter.RealParameter;
import beast.evolution.operators.DeltaExchangeOperator;
import beast.evolution.operators.Exchange;
import beast.evolution.operators.ScaleOperator;
import beast.evolution.operators.SubtreeSlide;
import beast.evolution.operators.Uniform;
import beast.evolution.operators.WilsonBalding;
import beast.evolution.tree.Tree;

public class BeastTestUtils {

	public static void addDefaultOperators(ArrayList<Operator> opsList, Tree tree, RealParameter... params  ) {

//		addDefaultTreeOperators(opsList, tree);
		addDefaultNonTreeOperators(opsList, params);
        
	}

	public static void addDefaultTreeOperators(ArrayList<Operator> opsList, Tree  tree) {

		Operator op;

        op = new ScaleOperator();
        op.initByName("tree", tree, "scaleFactor", 0.5, "weight", 3.0);
        op.setID("CoalescentConstantTreeScaler.t");
        opsList.add(op);
        
        op = new ScaleOperator();
        op.initByName("tree", tree, "rootOnly", true, "scaleFactor", 0.5, "weight", 3.0);
        op.setID("CoalescentConstantTreeRootScaler.t");
        opsList.add(op);
        
        op = new Uniform();
        op.initByName("tree", tree, "weight", 30.0);
        op.setID("CoalescentConstantUniformOperator.t");
        opsList.add(op);
        
        op = new SubtreeSlide();
        op.initByName("tree", tree, "weight", 15.0);
        op.setID("CoalescentConstantSubtreeSlide.t");
        opsList.add(op);
        
        op = new Exchange();
        op.initByName("tree", tree, "weight", 15.0);
        op.setID("CoalescentConstantNarrow.t");
        opsList.add(op);
        
        op = new Exchange();
        op.initByName("tree", tree, "isNarrow", false, "weight", 3.0);
        op.setID("CoalescentConstantWide.t");
        opsList.add(op);
        
        op = new WilsonBalding();
        op.initByName("tree", tree, "weight", 3.0);
        op.setID("CoalescentConstantWilsonBalding.t");
        opsList.add(op);
        
        
		
	}

	public static void addDefaultNonTreeOperators(ArrayList<Operator> opsList, RealParameter... params) {

		Operator op;
		for (RealParameter realParameter : params) {
			if(realParameter.getID().toLowerCase().contains("kappa") ){
				RealParameter kappa = realParameter;
				op = new ScaleOperator();
		        op.initByName("parameter", kappa, "scaleFactor", 0.5, "weight", 0.1);
		        op.setID("kappaScale.s");
		        opsList.add(op);
			}
			else if(realParameter.getID().toLowerCase().contains("popsize") ){
				RealParameter popSize = realParameter;
		        op = new ScaleOperator();
		        op.initByName("parameter", popSize, "scaleFactor", 0.75, "weight", 3.0);
		        op.setID("PopSizeScaler.t");
		        opsList.add(op);
				
			}
			if(realParameter.getID().toLowerCase().contains("freq") ){
				RealParameter freqParameter = realParameter;
		        op = new DeltaExchangeOperator();
		        op.initByName("parameter", freqParameter, "delta", 0.01, "weight", 0.1);
		        op.setID("FrequenciesExchanger.s");
		        opsList.add(op);        
		        
			}
		}
		
        
        
		
	}

}
