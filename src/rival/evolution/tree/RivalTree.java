package rival.evolution.tree;

import java.util.Arrays;
import java.util.List;

import beast.core.Description;
import beast.core.Input;
import beast.evolution.tree.Node;
import beast.evolution.tree.RandomTree;
import beast.util.HeapSort;
import beast.util.Randomizer;
import rival.io.ReadLocFile;

@Description("Rival tree")

public class RivalTree extends RandomTree {

	final public Input<ReadLocFile> locationInfoInput = new Input<>("locationFile", "all location info");

	private int[] locationInfo;

	private int maxLocationCount;

	private double[] intervals;
	private double[] storedIntervals;

	private int[][] occupationMatrix;
	private int[][] storedOccupationMatrix;

	

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
		maxLocationCount = -1;
		// TODO, check readLocationTaxaName matches taxon

		for (int i = 0; i < taxaNames.length; i++) {
			String taxa = taxaNames[i];
			System.out.println(getNode(i).getID() +"\t"+ taxa);
			locationInfo[i] = readLocation.getLocationFromTaxa(taxa);
			getNode(i).setMetaData(LOCATION, locationInfo[i]);
			if (locationInfo[i] > maxLocationCount) {
				maxLocationCount = locationInfo[i];
			}
		}
		maxLocationCount++;
//		maxLocation = 3;
		System.out.println("Location count: "+ maxLocationCount);
		System.out.println(Arrays.toString(locationInfo));
		occupationMatrix = new int[internalNodeCount][maxLocationCount];
		storedOccupationMatrix = new int[internalNodeCount][maxLocationCount];

		intervals = new double[internalNodeCount];
		storedIntervals = new double[internalNodeCount];

		 
		if (nodeCount != locationInfo.length) {
			throw new IllegalArgumentException("Location information count doesn't match with number of taxa ");
		}
		initInternalNode();
		calculateNodeInfo();
	}

	public void calculateNodeInfo() {

		int[] locationArray = getAllLocationData();
//		System.out.println(Arrays.toString(locationArray));
		// TreeIntervals ti = new TreeIntervals(this);
		// System.out.println(ti.getIntervalCount());
		// System.out.println(toString());
		// double[] intervals = new double[nodeCount];
		// ti.getIntervals(intervals);

		double[] allNodeHeight = collectIntervals();
		int[] indices = new int[nodeCount];
		HeapSort.sort(allNodeHeight, indices);

//		System.out.println(Arrays.toString(allNodeHeight));
//		System.out.println(Arrays.toString(indices));
//		System.out.println(Arrays.toString(locationArray));

		int calculationStep = 0;
		Arrays.fill(occupationMatrix[calculationStep], 0);
		Node node = getNode(indices[nodeCount - 1]);
		occupationMatrix[calculationStep][locationArray[indices[nodeCount - 1]]]++;

		// System.out.println("NodeHeight: " +calculationStep +"\t"+
		// allNodeHeight[indices[nodeCount-1]] +"\t"+
		// Arrays.toString(locationMatrix[calculationStep]) + "\t"+
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
			System.arraycopy(occupationMatrix[calculationStep], 0, occupationMatrix[++calculationStep], 0, maxLocationCount);

			if (locationCurrent != locationRight) {
				occupationMatrix[calculationStep][locationRight]++;
			} else if (locationCurrent != locationLeft) {
				occupationMatrix[calculationStep][locationLeft]++;
			} else if (locationLeft == locationRight) {
				// Migrate to the same place
				occupationMatrix[calculationStep][locationLeft]++;
			}
			intervals[calculationStep] = allNodeHeight[indices[i]] - allNodeHeight[indices[i - 1]];
			// }
		}

	}

	

	public double[] getIntervals() {
		return intervals;
	}

	public int[][] getOccupationMatrix() {
		return occupationMatrix;
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

		double[] allNodeHeight = collectIntervals();
		int[] indices = new int[nodeCount];
		HeapSort.sort(allNodeHeight, indices);
		
		for (int i = leafNodeCount; i < nodeCount; i++) {
			Node node = getNode(indices[i]);
			if(Randomizer.nextBoolean()){
				node.setMetaData(LOCATION, node.getRight().getMetaData(LOCATION));
			}
			else{
				node.setMetaData(LOCATION, node.getLeft().getMetaData(LOCATION));
			}
			
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
		for (int i = 0; i < occupationMatrix.length; i++) {
			System.arraycopy(occupationMatrix[i], 0, storedOccupationMatrix[i], 0, maxLocationCount);
		}
		System.arraycopy(intervals, 0, storedIntervals, 0, intervals.length);
		
//		System.out.println("In rivalTree store()");
        for (int i = 0; i < m_nodes.length; i++) {
			m_storedNodes[i].setMetaData(LOCATION, m_nodes[i].getMetaData(LOCATION));
//			m_storedNodes[i].setMetaData(LOCATION, 10);;
//			System.out.print(m_storedNodes[i].getMetaData("location") +" ");
		}
//        System.out.println();
//        for (Node node : m_nodes) {
//			System.out.print(node.getMetaData("location") +" ");
//		}System.out.println();

	}

	@Override
	public void restore() {
		super.restore();
		
		for (int i = 0; i < occupationMatrix.length; i++) {
			int[] temp = storedOccupationMatrix[i];
			storedOccupationMatrix[i] = occupationMatrix[i];
			occupationMatrix[i] = temp;
		}
		double[] temp = storedIntervals;
		storedIntervals = intervals;
		intervals = temp;
	
//		System.out.println("in RivalTree restore()");
//        for (Node node : m_storedNodes) {
//			System.out.print(node.getMetaData("location") +" ");
//		}System.out.println();
//        for (Node node : m_nodes) {
//			System.out.print(node.getMetaData("location") +" ");
//		}System.out.println();
		
//		for (int i = 0; i < m_nodes.length; i++) {
//			m_storedNodes[i].setMetaData(LOCATION, m_nodes[i].getMetaData(LOCATION));
//		
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
