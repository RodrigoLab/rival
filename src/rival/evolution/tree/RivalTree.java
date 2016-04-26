package rival.evolution.tree;

import java.util.Arrays;

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
	
	private int[][] locationMatirx;
	private int[][] storedLocationMatirx;

	public static final String LOCATION = "location";

	@Override
	public void initAndValidate() {
		super.initAndValidate();
		
		String[] taxaNames = getTaxaNames();
		// root.SetRivalInfo();
		// Parse ReadLocFile and map sample to location
		ReadLocFile readLocation = locationInfoInput.get();

		locationInfo = new int[nodeCount];
		maxLocation = -1;
		// TODO, check readLocationTaxaName matches taxon
		for (int i = 0; i < taxaNames.length; i++) {
			String taxa = taxaNames[i];
			// TODO: implement these
			// ReadLocFile.getLocationFromTaxa(taxa);
			// populate node/location with metadata/location
			int location = 0;
			if(location >  maxLocation){
				maxLocation = location;
			}
			System.out.println(taxa);
			
		}
		 maxLocation = 3;
		 
		 locationMatirx = new int[internalNodeCount][maxLocation];
		 storedLocationMatirx = new int[internalNodeCount][maxLocation];
		 
		 intervals = new double[internalNodeCount];
		 storedIntervals = new double[internalNodeCount];
		

		// TODO: Add this back
		// LocationInfo locationInfo = locationInfoInput.get();
		// if( taxa.length != locationInfo.getLength() ){
		// throw new IllegalArgumentException("Location information count
		// doesn't match with number of taxa ");
		// }

	}
	
	
	public void calculateNodeInfo(){
		
        int[] locationArray = getAllLocationData();
        
        
//        TreeIntervals ti = new TreeIntervals(this);
//        System.out.println(ti.getIntervalCount());
//        System.out.println(toString());
//        double[] intervals = new double[nodeCount];
//        ti.getIntervals(intervals);
        

        double[] allNodeHeight = collectIntervals();
        int[] indices = new int[nodeCount];
        HeapSort.sort(allNodeHeight, indices);
        
        System.out.println(Arrays.toString(allNodeHeight));
        System.out.println(Arrays.toString(indices));
        System.out.println(Arrays.toString(locationArray));
        
        int calculationStep = 0;
        Arrays.fill(locationMatirx[calculationStep], 0);
        Node node = getNode(indices[nodeCount-1]);
        locationMatirx[calculationStep][locationArray[indices[nodeCount-1]]]++;
        
        
//        System.out.println("NodeHeight: " +calculationStep +"\t"+ allNodeHeight[indices[nodeCount-1]]  +"\t"+ 
//				Arrays.toString(locationMatirx[calculationStep]) + "\t"+ Arrays.toString(intervals) + "\n");
        
        for (int i = (nodeCount -1); i > (leafNodeCount) ; i--) {
        	
        	node = getNode(indices[i]);
        	int nodeNr = node.getNr();
        	int left = node.getLeft().getNr();
        	int right  = node.getRight().getNr();
//			System.out.println(times[i] +"\t"+ times[indices[i]] +"\t"+ node.getHeight());
			
			int locationCurrent = locationArray[nodeNr];
			int locationLeft = locationArray[left];
			int locationRight = locationArray[right];
			
			
//			System.out.println(locationCurrent +"\t"+ locationLeft +"\t"+ locationRight);
//        	if(i != getLeafNodeCount()){
        		System.arraycopy(locationMatirx[calculationStep], 0, locationMatirx[++calculationStep], 0, maxLocation);
	        	
				if(locationCurrent != locationRight){
					locationMatirx[calculationStep][locationRight]++;
				}
				else if(locationCurrent != locationLeft){
					locationMatirx[calculationStep][locationLeft]++;
				}
				else if(locationLeft == locationRight){
					//Migrate to the same place
					locationMatirx[calculationStep][locationLeft]++;
				}
				intervals[calculationStep] = allNodeHeight[indices[i]] - allNodeHeight[indices[i-1]];
//        	}
        	
        	
//			System.out.println("NodeEND: calc: " +calculationStep +"\t"+ allNodeHeight[indices[i]]  +"\t"+ 
//					Arrays.toString(locationMatirx[calculationStep]) + "\t"+ Arrays.toString(intervals) + "\n");
        }
			
		
	}


	public double[] getIntervals() {
		return intervals;
	}


	public int[][] getLocationMatirx() {
		return locationMatirx;
	}


	
	
	public int[] getAllLocationData() {

		for (int i = 0; i < m_nodes.length; i++) {
			locationInfo[i] = (int) m_nodes[i].getMetaData(LOCATION);
		}
//		System.out.println("GetAllMetaData: " + Arrays.toString(locationInfo));
		return locationInfo;
	}

	@Override
	protected void store() {
		super.store();
		for (int i = 0; i < locationMatirx.length; i++) {
			System.arraycopy(locationMatirx[i], 0, storedLocationMatirx[i], 0, maxLocation);
		}
		System.arraycopy(intervals, 0, storedIntervals, 0, intervals.length);
	}

	@Override
	public void restore() {
		super.restore();
		for (int i = 0; i < locationMatirx.length; i++) {
			int[] temp = storedLocationMatirx[i];
			storedLocationMatirx[i] = locationMatirx[i];
			locationMatirx[i] = temp;
		}
		double[] temp = storedIntervals;
		storedIntervals = intervals;
		intervals = temp;
	}

	
	protected double[] collectIntervals(){//, int[] childCounts) {
        Node[] nodes = getNodesAsArray();
        double[] intervals = new double[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            intervals[i] = nodes[i].getHeight();
        }
        return intervals;
    }
}
