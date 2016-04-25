package rival.evolution.tree;

import java.io.PrintStream;
import java.util.ArrayList;
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
	
	public static final String LOCATION = "location";
	
	@Override
    public void initAndValidate() {
		super.initAndValidate();
//		root.SetRivalInfo();
		
		
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
		
		m_nodes[0].setMetaData(LOCATION, 10);
		m_nodes[0].setMetaData("Temp", 100);
        
	}
	public void getAllMetaData(){
		for (int i = 0; i < m_nodes.length; i++) {
			System.out.println(m_nodes[i].getMetaData(LOCATION));
		}
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
