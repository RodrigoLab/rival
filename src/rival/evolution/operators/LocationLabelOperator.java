package rival.evolution.operators;

import java.util.Arrays;

import beast.core.Input;
import beast.core.Operator;
import beast.evolution.tree.Node;
import rival.evolution.tree.RivalTree;
import beast.core.Input.Validate;
import beast.util.Randomizer;

public class LocationLabelOperator extends Operator{

	final public Input<RivalTree> treeInput = new Input<>("tree", "Rival tree with location info", Validate.REQUIRED);
	private int extNodeCount;

	
	@Override
	public void initAndValidate() {

		extNodeCount = treeInput.get().getLeafNodeCount();
		
	}

	@Override
	public double proposal() {
		RivalTree tree = treeInput.get();
		tree.startEditing(this);//All BEAST tree operator call this
//		System.out.println(Arrays.toString(tree.getAllLocationData()));
		boolean isInvalid = true;
		do {
			int rng = Randomizer.nextInt(tree.getInternalNodeCount());
			Node parent = tree.getNode(rng+extNodeCount);
			
			int[] locations = getThreeLocation(parent);
			if(locations[0] == locations[1] && locations[0] == locations[2]){
				isInvalid = true;
			}
			else if(locations[0] != locations[1] && locations[0] == locations[2]){
				isInvalid = false;
				parent.setMetaData(RivalTree.LOCATION, locations[1]);
			}
			else if(locations[0] == locations[1] && locations[0] != locations[2]){
				isInvalid = false;
				parent.setMetaData(RivalTree.LOCATION, locations[2]);
			}
//			System.out.println(Arrays.toString(locations));
//			if(!tree.checkTreeLocation()){
				//Call this in the operator if we only want valid move.
			   //Call the check in the likelihood function if we want to auto reject the move
//				isInvalid = true;
//			}
		} while (isInvalid);
		
		
		return 0;
	}

	private int[] getThreeLocation(Node parent) {
		int[] locations = new int[3];
		locations[0] = (int) parent.getMetaData(RivalTree.LOCATION);
		locations[1] = (int) parent.getLeft().getMetaData(RivalTree.LOCATION);
		locations[2] = (int) parent.getRight().getMetaData(RivalTree.LOCATION);
		return locations;
	}

}
