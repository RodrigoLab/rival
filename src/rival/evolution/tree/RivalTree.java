package rival.evolution.tree;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import beast.core.Description;
import beast.core.Input;
import beast.core.Operator;
import beast.core.StateNode;
import beast.core.StateNodeInitialiser;
import beast.core.util.Log;
import beast.evolution.alignment.TaxonSet;
import beast.evolution.tree.Node;
import beast.evolution.tree.RandomTree;
import beast.evolution.tree.TraitSet;
import beast.evolution.tree.Tree;
import beast.evolution.tree.TreeInterface;
import beast.util.TreeParser;
import rival.evolution.geo.LocationInfo;

@Description("Rival tree")

public class RivalTree extends RandomTree {

	final public Input<LocationInfo> locationInfoInput = new Input<>("location", "all location info");

	private int[] locationInfo;
	
	public static final String LOCATION = "location";
	
	@Override
    public void initAndValidate() {
		super.initAndValidate();
//		root.SetRivalInfo();
		locationInfo = new int[nodeCount]; 
		
		String [] taxa = getTaxaNames();
		for (int i = 0; i < taxa.length; i++) {
			String string = taxa[i];
			System.out.println(string);
		}
		
		//TODO: Add this back
//		LocationInfo locationInfo = locationInfoInput.get();
//		if( taxa.length != locationInfo.getLength() ){
//			throw new IllegalArgumentException("Location information count doesn't match with number of taxa ");
//		}
		
		
		
        
	}
	public int[] getAllMetaData(){
		
		for (int i = 0; i < m_nodes.length; i++) {
			
			locationInfo[i] = (int) m_nodes[i].getMetaData(LOCATION);
		}
		System.out.println("GetAllMetaData: "+ Arrays.toString(locationInfo));
		return locationInfo;
	}
	
	@Override
	protected void store() {
		super.store();
	}

	@Override
	public void restore() {
		super.restore();
	}

}
