package test.rival.evolution.tree;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import beast.core.parameter.RealParameter;
import beast.evolution.alignment.Alignment;
import beast.evolution.alignment.Sequence;
import beast.evolution.tree.Node;
import beast.evolution.tree.Tree;
import beast.evolution.tree.coalescent.ConstantPopulation;
import beast.evolution.tree.coalescent.TreeIntervals;
import beast.util.HeapSort;
import beast.util.Randomizer;
import rival.evolution.geo.distance.LocationDistance;
//import junit.framework.TestCase;
import rival.evolution.tree.RivalTree;
import rival.io.ReadLocFile;

public class RivalTreeTest {

    public RivalTreeTest() {
        
    }

	@Test
	public void TestTreeLocation(){
		String[] allName = new String[] { "DQ649480", "DQ649481", "DQ649482", "DQ649483", "DQ649484", "DQ649485",
				"DQ649486", "DQ649487", "DQ649488", "DQ649491", "DQ649492", "DQ649493", "DQ649479", "DQ649494",
				"DQ649495", "DQ649497", "DQ649498", "DQ649499", "DQ649500", "DQ649501", "DQ649502" };
		Alignment data = new Alignment();
		char[] DNA = new char[]{'A', 'C', 'G', 'T'}; 
		for (int i = 0; i < 21; i++) {
			Sequence s= new Sequence(allName[i], "AGAAATA"+ DNA[i%3]  + DNA[Randomizer.nextInt(3)] + DNA[Randomizer.nextInt(3)]+ DNA[Randomizer.nextInt(3)]+ DNA[Randomizer.nextInt(3)]);
			System.out.println(s.toString());
			data.setInputValue("sequence",  s);
		}
		data.setInputValue("dataType", "nucleotide");
		data.initAndValidate();
		
		

		String userPath = System.getProperty("user.dir");
		String fileName = userPath + "/data/Banza.loc";
		
		ReadLocFile readFile = new ReadLocFile();
		try {
			readFile.readFile(fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LocationDistance locDist = new LocationDistance();
		locDist.initByName("readLocFile", readFile, "typeDist", "euclidean");
		
//		
		

        RivalTree tree = new RivalTree();
//        TraitSet dates = getDates(taxa);
        ConstantPopulation constant = new ConstantPopulation();
        constant.initByName("popSize", new RealParameter("10.0"));
        tree.initByName(
        		"taxa", data,
                "populationModel", constant,
                "locationFile", readFile
//                "trait", dates);
                );
        System.out.println(Arrays.toString(tree.getIntervals() ) );
		int[][] locationMatrix = tree.getLocationMatrix();
		for (int i = 0; i < locationMatrix.length; i++) {
			System.out.println(Arrays.toString(locationMatrix[i]));
		}
		System.out.println("END");
		
		
	}
	
	
	protected static void collectTimes(Tree tree, double[] times, int[] childCounts) {
        Node[] nodes = tree.getNodesAsArray();
        for (int i = 0; i < nodes.length; i++) {
            Node node = nodes[i];
            times[i] = node.getHeight();
            childCounts[i] = node.isLeaf() ? 0 : 2;
        }
    }
	
	protected static boolean checkTreeLocation(RivalTree tree) {
		List<Node> internalNodes = tree.getInternalNodes();
		int[] locationArray = tree.getAllLocationData();
		int nodeNr, left, right ;
	    for (int i = 0; i < internalNodes.size(); i++) {
	    	Node node = internalNodes.get(i);
	    	nodeNr = node.getNr();
	    	left = node.getLeft().getNr();
	    	right  = node.getRight().getNr();
            if ( locationArray[left]!=locationArray[nodeNr] && locationArray[right]!=locationArray[nodeNr] ){
            	return false;
            }
        }
	    return true;
    }
}
