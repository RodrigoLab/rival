package rival.evolution.tree;

import java.util.Arrays;
import java.util.List;

import beast.core.Description;
import beast.core.Input;
import beast.evolution.tree.Node;
import beast.evolution.tree.RandomTree;
import beast.util.HeapSort;
import rival.io.ReadLocFile;

@Description("Rival tree")

public class RivalTree extends RandomTree {

	final public Input<ReadLocFile> locationInfoInput = new Input<>("locationFile", "all location info");

	private int[] locationInfo;

	private int maxLocation;

	private double[] intervals;
	private double[] storedIntervals;

	private int[][] locationMatrix;
	private int[][] storedLocationMatirx;

	private int[] indices;

	public static final String LOCATION = "location";

	@Override
	public void initAndValidate() {
		super.initAndValidate();

		String[] taxaNames = getTaxaNames();
		// root.SetRivalInfo();
		// Parse ReadLocFile and map sample to location
		ReadLocFile readLocation = locationInfoInput.get();

		locationInfo = new int[nodeCount];
		Arrays.fill(locationInfo, -1);
		maxLocation = -1;
		// TODO, check readLocationTaxaName matches taxon

		for (int i = 0; i < taxaNames.length; i++) {
			String taxa = taxaNames[i];
			System.out.println(getNode(i).getID() +"\t"+ taxa);
			locationInfo[i] = readLocation.getLocationFromTaxa(taxa);
			getNode(i).setMetaData(LOCATION, locationInfo[i]);
			if (locationInfo[i] > maxLocation) {
				maxLocation = locationInfo[i];
			}
		}
		maxLocation++;
//		maxLocation = 3;
		System.out.println(maxLocation);
		System.out.println(Arrays.toString(locationInfo));
		locationMatrix = new int[internalNodeCount][maxLocation];
		storedLocationMatirx = new int[internalNodeCount][maxLocation];

		intervals = new double[internalNodeCount];
		storedIntervals = new double[internalNodeCount];

		// TODO: Add this back
		// LocationInfo locationInfo = locationInfoInput.get();
		// if( taxa.length != locationInfo.getLength() ){
		// throw new IllegalArgumentException("Location information count
		// doesn't match with number of taxa ");
		// }
		initInternalNode();
		calculateNodeInfo();
	}

	public void calculateNodeInfo() {

		int[] locationArray = getAllLocationData();
		System.out.println(Arrays.toString(locationArray));
		// TreeIntervals ti = new TreeIntervals(this);
		// System.out.println(ti.getIntervalCount());
		// System.out.println(toString());
		// double[] intervals = new double[nodeCount];
		// ti.getIntervals(intervals);

		double[] allNodeHeight = collectIntervals();
		indices = new int[nodeCount];
		HeapSort.sort(allNodeHeight, indices);

//		System.out.println(Arrays.toString(allNodeHeight));
//		System.out.println(Arrays.toString(indices));
//		System.out.println(Arrays.toString(locationArray));

		int calculationStep = 0;
		Arrays.fill(locationMatrix[calculationStep], 0);
		Node node = getNode(indices[nodeCount - 1]);
		locationMatrix[calculationStep][locationArray[indices[nodeCount - 1]]]++;

		// System.out.println("NodeHeight: " +calculationStep +"\t"+
		// allNodeHeight[indices[nodeCount-1]] +"\t"+
		// Arrays.toString(locationMatirx[calculationStep]) + "\t"+
		// Arrays.toString(intervals) + "\n");

		for (int i = (nodeCount - 1); i > (leafNodeCount); i--) {

			node = getNode(indices[i]);
			int nodeNr = node.getNr();
			int left = node.getLeft().getNr();
			int right = node.getRight().getNr();
			// System.out.println(times[i] +"\t"+ times[indices[i]] +"\t"+
			// node.getHeight());

			int locationCurrent = locationArray[nodeNr];
			int locationLeft = locationArray[left];
			int locationRight = locationArray[right];

			// System.out.println(locationCurrent +"\t"+ locationLeft +"\t"+
			// locationRight);
			// if(i != getLeafNodeCount()){
			System.arraycopy(locationMatrix[calculationStep], 0, locationMatrix[++calculationStep], 0, maxLocation);

			if (locationCurrent != locationRight) {
				locationMatrix[calculationStep][locationRight]++;
			} else if (locationCurrent != locationLeft) {
				locationMatrix[calculationStep][locationLeft]++;
			} else if (locationLeft == locationRight) {
				// Migrate to the same place
				locationMatrix[calculationStep][locationLeft]++;
			}
			intervals[calculationStep] = allNodeHeight[indices[i]] - allNodeHeight[indices[i - 1]];
			// }

//			System.out.println("NodeEND: calc: " + calculationStep + "\t" + allNodeHeight[indices[i]] + "\t"
//					+ Arrays.toString(locationMatrix[calculationStep]) + "\t" +	 Arrays.toString(intervals) + "\n");
		}

	}

	public int[] getIndices() {
		return indices;
	}

	public double[] getIntervals() {
		return intervals;
	}

	public int[][] getLocationMatrix() {
		return locationMatrix;
	}

	public boolean checkTreeLocation() {
		List<Node> internalNodes = getInternalNodes();
		int[] locationArray = getAllLocationData();
		int nodeNr, left, right;
		for (int i = 0; i < internalNodes.size(); i++) {
			Node node = internalNodes.get(i);
			nodeNr = node.getNr();
			left = node.getLeft().getNr();
			right = node.getRight().getNr();
			if (locationArray[left] != locationArray[nodeNr] && locationArray[right] != locationArray[nodeNr]) {
				return false;
			}
		}
		return true;
	}

	

	private boolean initInternalNode() {
//		List<Node> internalNodes = getInternalNodes();
//		int[] locationArray = getAllLocationData();
		
		double[] allNodeHeight = collectIntervals();
		int[] indices = new int[nodeCount];
		HeapSort.sort(allNodeHeight, indices);
		
		int nodeNr, left, right;
		System.out.println(Arrays.toString(allNodeHeight));
		System.out.println(Arrays.toString(indices));
		for (int i = leafNodeCount; i < nodeCount; i++) {
			Node node = getNode(indices[i]);
			nodeNr = node.getNr();
			left = node.getLeft().getNr();
			right = node.getRight().getNr();
			node.setMetaData(LOCATION, node.getRight().getMetaData(LOCATION));
			System.out.println(nodeNr +"\t"+ left +"\t"+ right +"\t"+  node.getRight().getMetaData(LOCATION) );
//			if (locationArray[left] != locationArray[nodeNr] && locationArray[right] != locationArray[nodeNr]) {
//				return false;
//			}
		}
		if(!checkTreeLocation()){
			throw new  IllegalArgumentException("Incorrect internal node location");
		}
		
		return true;
	}
	public int[] getAllLocationData() {

		for (int i = 0; i < m_nodes.length; i++) {
			locationInfo[i] = (int) m_nodes[i].getMetaData(LOCATION);
		}
		// System.out.println("GetAllMetaData: " +
		// Arrays.toString(locationInfo));
		return locationInfo;
	}

	@Override
	protected void store() {
		super.store();
		for (int i = 0; i < locationMatrix.length; i++) {
			System.arraycopy(locationMatrix[i], 0, storedLocationMatirx[i], 0, maxLocation);
		}
		System.arraycopy(intervals, 0, storedIntervals, 0, intervals.length);
	}

	@Override
	public void restore() {
		super.restore();
		for (int i = 0; i < locationMatrix.length; i++) {
			int[] temp = storedLocationMatirx[i];
			storedLocationMatirx[i] = locationMatrix[i];
			locationMatrix[i] = temp;
		}
		double[] temp = storedIntervals;
		storedIntervals = intervals;
		intervals = temp;
	}

	protected double[] collectIntervals() {// , int[] childCounts) {
		Node[] nodes = getNodesAsArray();
		double[] intervals = new double[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			intervals[i] = nodes[i].getHeight();
		}
		return intervals;
	}
}
