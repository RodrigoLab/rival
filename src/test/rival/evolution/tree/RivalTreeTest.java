package test.rival.evolution.tree;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import javax.swing.plaf.synth.SynthSeparatorUI;
import javax.swing.text.TabableView;

import org.junit.Test;

import beast.core.parameter.RealParameter;
import beast.evolution.alignment.Alignment;
import beast.evolution.alignment.Sequence;
import beast.evolution.tree.Node;
import beast.evolution.tree.RandomTree;
import beast.evolution.tree.TraitSet;
import beast.evolution.tree.Tree;
import beast.evolution.tree.coalescent.ConstantPopulation;
import beast.evolution.tree.coalescent.TreeIntervals;
import beast.util.HeapSort;
import beast.util.TreeParser;
//import junit.framework.TestCase;
import rival.evolution.tree.RivalTree;

public class RivalTreeTest {

    public RivalTreeTest() {
        
    }

	
	@Test
	public void TestTree(){
	
        Sequence human = new Sequence("human", "AGAAATATGTCTGATAAAAGAGTTACTTTGATAGAGTAAATAATAGGAGCTTAAACCCCCTTATTTCTACTAGGACTATGAGAATCGAACCCATCCCTGAGAATCCAAAATTCTCCGTGCCACCTATCACACCCCATCCTAAGTAAGGTCAGCTAAATAAGCTATCGGGCCCATACCCCGAAAATGTTGGTTATACCCTTCCCGTACTAAGAAATTTAGGTTAAATACAGACCAAGAGCCTTCAAAGCCCTCAGTAAGTTG-CAATACTTAATTTCTGTAAGGACTGCAAAACCCCACTCTGCATCAACTGAACGCAAATCAGCCACTTTAATTAAGCTAAGCCCTTCTAGACCAATGGGACTTAAACCCACAAACACTTAGTTAACAGCTAAGCACCCTAATCAAC-TGGCTTCAATCTAAAGCCCCGGCAGG-TTTGAAGCTGCTTCTTCGAATTTGCAATTCAATATGAAAA-TCACCTCGGAGCTTGGTAAAAAGAGGCCTAACCCCTGTCTTTAGATTTACAGTCCAATGCTTCA-CTCAGCCATTTTACCACAAAAAAGGAAGGAATCGAACCCCCCAAAGCTGGTTTCAAGCCAACCCCATGGCCTCCATGACTTTTTCAAAAGGTATTAGAAAAACCATTTCATAACTTTGTCAAAGTTAAATTATAGGCT-AAATCCTATATATCTTA-CACTGTAAAGCTAACTTAGCATTAACCTTTTAAGTTAAAGATTAAGAGAACCAACACCTCTTTACAGTGA");
        Sequence chimp = new Sequence("chimp", "AGAAATATGTCTGATAAAAGAATTACTTTGATAGAGTAAATAATAGGAGTTCAAATCCCCTTATTTCTACTAGGACTATAAGAATCGAACTCATCCCTGAGAATCCAAAATTCTCCGTGCCACCTATCACACCCCATCCTAAGTAAGGTCAGCTAAATAAGCTATCGGGCCCATACCCCGAAAATGTTGGTTACACCCTTCCCGTACTAAGAAATTTAGGTTAAGCACAGACCAAGAGCCTTCAAAGCCCTCAGCAAGTTA-CAATACTTAATTTCTGTAAGGACTGCAAAACCCCACTCTGCATCAACTGAACGCAAATCAGCCACTTTAATTAAGCTAAGCCCTTCTAGATTAATGGGACTTAAACCCACAAACATTTAGTTAACAGCTAAACACCCTAATCAAC-TGGCTTCAATCTAAAGCCCCGGCAGG-TTTGAAGCTGCTTCTTCGAATTTGCAATTCAATATGAAAA-TCACCTCAGAGCTTGGTAAAAAGAGGCTTAACCCCTGTCTTTAGATTTACAGTCCAATGCTTCA-CTCAGCCATTTTACCACAAAAAAGGAAGGAATCGAACCCCCTAAAGCTGGTTTCAAGCCAACCCCATGACCTCCATGACTTTTTCAAAAGATATTAGAAAAACTATTTCATAACTTTGTCAAAGTTAAATTACAGGTT-AACCCCCGTATATCTTA-CACTGTAAAGCTAACCTAGCATTAACCTTTTAAGTTAAAGATTAAGAGGACCGACACCTCTTTACAGTGA");
        Sequence bonobo = new Sequence("bonobo", "AGAAATATGTCTGATAAAAGAATTACTTTGATAGAGTAAATAATAGGAGTTTAAATCCCCTTATTTCTACTAGGACTATGAGAGTCGAACCCATCCCTGAGAATCCAAAATTCTCCGTGCCACCTATCACACCCCATCCTAAGTAAGGTCAGCTAAATAAGCTATCGGGCCCATACCCCGAAAATGTTGGTTATACCCTTCCCGTACTAAGAAATTTAGGTTAAACACAGACCAAGAGCCTTCAAAGCTCTCAGTAAGTTA-CAATACTTAATTTCTGTAAGGACTGCAAAACCCCACTCTGCATCAACTGAACGCAAATCAGCCACTTTAATTAAGCTAAGCCCTTCTAGATTAATGGGACTTAAACCCACAAACATTTAGTTAACAGCTAAACACCCTAATCAGC-TGGCTTCAATCTAAAGCCCCGGCAGG-TTTGAAGCTGCTTCTTTGAATTTGCAATTCAATATGAAAA-TCACCTCAGAGCTTGGTAAAAAGAGGCTTAACCCCTGTCTTTAGATTTACAGTCCAATGCTTCA-CTCAGCCATTTTACCACAAAAAAGGAAGGAATCGAACCCCCTAAAGCTGGTTTCAAGCCAACCCCATGACCCCCATGACTTTTTCAAAAGATATTAGAAAAACTATTTCATAACTTTGTCAAAGTTAAATTACAGGTT-AAACCCCGTATATCTTA-CACTGTAAAGCTAACCTAGCATTAACCTTTTAAGTTAAAGATTAAGAGGACCAACACCTCTTTACAGTGA");
        Sequence gorilla = new Sequence("gorilla", "AGAAATATGTCTGATAAAAGAGTTACTTTGATAGAGTAAATAATAGAGGTTTAAACCCCCTTATTTCTACTAGGACTATGAGAATTGAACCCATCCCTGAGAATCCAAAATTCTCCGTGCCACCTGTCACACCCCATCCTAAGTAAGGTCAGCTAAATAAGCTATCGGGCCCATACCCCGAAAATGTTGGTCACATCCTTCCCGTACTAAGAAATTTAGGTTAAACATAGACCAAGAGCCTTCAAAGCCCTTAGTAAGTTA-CAACACTTAATTTCTGTAAGGACTGCAAAACCCTACTCTGCATCAACTGAACGCAAATCAGCCACTTTAATTAAGCTAAGCCCTTCTAGATCAATGGGACTCAAACCCACAAACATTTAGTTAACAGCTAAACACCCTAGTCAAC-TGGCTTCAATCTAAAGCCCCGGCAGG-TTTGAAGCTGCTTCTTCGAATTTGCAATTCAATATGAAAT-TCACCTCGGAGCTTGGTAAAAAGAGGCCCAGCCTCTGTCTTTAGATTTACAGTCCAATGCCTTA-CTCAGCCATTTTACCACAAAAAAGGAAGGAATCGAACCCCCCAAAGCTGGTTTCAAGCCAACCCCATGACCTTCATGACTTTTTCAAAAGATATTAGAAAAACTATTTCATAACTTTGTCAAGGTTAAATTACGGGTT-AAACCCCGTATATCTTA-CACTGTAAAGCTAACCTAGCGTTAACCTTTTAAGTTAAAGATTAAGAGTATCGGCACCTCTTTGCAGTGA");
        Sequence orangutan = new Sequence("orangutan", "AGAAATATGTCTGACAAAAGAGTTACTTTGATAGAGTAAAAAATAGAGGTCTAAATCCCCTTATTTCTACTAGGACTATGGGAATTGAACCCACCCCTGAGAATCCAAAATTCTCCGTGCCACCCATCACACCCCATCCTAAGTAAGGTCAGCTAAATAAGCTATCGGGCCCATACCCCGAAAATGTTGGTTACACCCTTCCCGTACTAAGAAATTTAGGTTA--CACAGACCAAGAGCCTTCAAAGCCCTCAGCAAGTCA-CAGCACTTAATTTCTGTAAGGACTGCAAAACCCCACTTTGCATCAACTGAGCGCAAATCAGCCACTTTAATTAAGCTAAGCCCTCCTAGACCGATGGGACTTAAACCCACAAACATTTAGTTAACAGCTAAACACCCTAGTCAAT-TGGCTTCAGTCCAAAGCCCCGGCAGGCCTTAAAGCTGCTCCTTCGAATTTGCAATTCAACATGACAA-TCACCTCAGGGCTTGGTAAAAAGAGGTCTGACCCCTGTTCTTAGATTTACAGCCTAATGCCTTAACTCGGCCATTTTACCGCAAAAAAGGAAGGAATCGAACCTCCTAAAGCTGGTTTCAAGCCAACCCCATAACCCCCATGACTTTTTCAAAAGGTACTAGAAAAACCATTTCGTAACTTTGTCAAAGTTAAATTACAGGTC-AGACCCTGTGTATCTTA-CATTGCAAAGCTAACCTAGCATTAACCTTTTAAGTTAAAGACTAAGAGAACCAGCCTCTCTTTGCAATGA");
        Sequence siamang = new Sequence("siamang", "AGAAATACGTCTGACGAAAGAGTTACTTTGATAGAGTAAATAACAGGGGTTTAAATCCCCTTATTTCTACTAGAACCATAGGAGTCGAACCCATCCTTGAGAATCCAAAACTCTCCGTGCCACCCGTCGCACCCTGTTCTAAGTAAGGTCAGCTAAATAAGCTATCGGGCCCATACCCCGAAAATGTTGGTTATACCCTTCCCATACTAAGAAATTTAGGTTAAACACAGACCAAGAGCCTTCAAAGCCCTCAGTAAGTTAACAAAACTTAATTTCTGCAAGGGCTGCAAAACCCTACTTTGCATCAACCGAACGCAAATCAGCCACTTTAATTAAGCTAAGCCCTTCTAGATCGATGGGACTTAAACCCATAAAAATTTAGTTAACAGCTAAACACCCTAAACAACCTGGCTTCAATCTAAAGCCCCGGCAGA-GTTGAAGCTGCTTCTTTGAACTTGCAATTCAACGTGAAAAATCACTTCGGAGCTTGGCAAAAAGAGGTTTCACCTCTGTCCTTAGATTTACAGTCTAATGCTTTA-CTCAGCCACTTTACCACAAAAAAGGAAGGAATCGAACCCTCTAAAACCGGTTTCAAGCCAGCCCCATAACCTTTATGACTTTTTCAAAAGATATTAGAAAAACTATTTCATAACTTTGTCAAAGTTAAATCACAGGTCCAAACCCCGTATATCTTATCACTGTAGAGCTAGACCAGCATTAACCTTTTAAGTTAAAGACTAAGAGAACTACCGCCTCTTTACAGTGA");

        Alignment data = new Alignment();
        data.initByName("sequence", human, "sequence", chimp, "sequence", bonobo, "sequence", gorilla, "sequence", orangutan, "sequence", siamang,
                "dataType", "nucleotide"
        );
        
		TreeParser tp = new TreeParser();
        tp.initByName("taxa", data,
				"newick",
				"((((human:0.024003,(chimp:0.010772,bonobo:0.010772):0.013231):0.012035,gorilla:0.036038):0.033087000000000005,orangutan:0.069125):0.030456999999999998,siamang:0.099582);",
				"IsLabelledNewick", true);
		TreeIntervals tpi = new TreeIntervals(tp);
        double[] temp = new double[tp.getNodeCount()];
        tpi.getIntervals(temp);
        System.out.println(Arrays.toString(temp));
        
     
        
//        rt.initByName("initial", tree);
//        rt.initByName("taxa", data,
//                "newick", "((((human:0.024003,(chimp:0.010772,bonobo:0.010772):0.013231):0.012035,gorilla:0.036038):0.033087000000000005,orangutan:0.069125):0.030456999999999998,siamang:0.099582);",
//                "IsLabelledNewick", true);
        
        RivalTree tree = new RivalTree();
//        TraitSet dates = getDates(taxa);
        ConstantPopulation constant = new ConstantPopulation();
        constant.initByName("popSize", new RealParameter("5.0"));
        tree.initByName(
        		"taxa", data,
                "populationModel", constant
//                "trait", dates);
                );
        
        List<Node> externalNodes = tree.getExternalNodes();
        
        Node root = tree.getRoot();
		root.setMetaData("location", 9999);
        int nodeCount = tree.getNodeCount();
        assertEquals(11, nodeCount);
        
        Node[] nodesAsArray = tree.getNodesAsArray();
        for (int i = 0; i < nodesAsArray.length; i++) {
			nodesAsArray[i].setMetaData(RivalTree.LOCATION, 0);
		}
        nodesAsArray[0].setMetaData(RivalTree.LOCATION, 1);
        Node parent = nodesAsArray[0].getParent();
		parent.setMetaData(RivalTree.LOCATION, 1);
        if(!parent.isRoot()){
        	parent.getParent().setMetaData(RivalTree.LOCATION, 1);
        }
//        root.setMetaData(RivalTree.LOCATION, 1);
//        root.getRight().setMetaData(RivalTree.LOCATION, 1);
//        root.getLeft().setMetaData(RivalTree.LOCATION, 1);
//        
//        if( !root.getRight().isLeaf() ){
//        	root.getRight().getRight().setMetaData(RivalTree.LOCATION, 1);
//        	root.getRight().getLeft().setMetaData(RivalTree.LOCATION, 0);
//        	System.out.println("Internal Right");
//        }
//        
//        if( !root.getLeft().isLeaf() ){
//        	root.getLeft().getRight().setMetaData(RivalTree.LOCATION, 1);
//        	root.getLeft().getLeft().setMetaData(RivalTree.LOCATION, 2);
//        	System.out.println("Internal left");
//        }
        
        int[] locationArray = tree.getAllMetaData();
        
        
        TreeIntervals ti = new TreeIntervals(tree);
        System.out.println(ti.getIntervalCount());
        System.out.println(tree.toString());
        double[] intervals = new double[tree.getNodeCount()];
        ti.getIntervals(intervals);
        

        nodeCount = tree.getNodeCount();

        double[] times = new double[nodeCount];
        int[] childCounts = new int[nodeCount];

        collectTimes(tree, times, childCounts);

        int[] indices = new int[nodeCount];
        System.out.println(Arrays.toString(times));
        System.out.println(Arrays.toString(childCounts));
        System.out.println();
        HeapSort.sort(times, indices);
        System.out.println(Arrays.toString(times));
        System.out.println(Arrays.toString(indices));
        
        System.out.println(Arrays.toString(locationArray));
        int maxLocation = 3;
        int[][] locationMatirx = new int[tree.getInternalNodeCount()][maxLocation];
        
        int calculationCount = 0;
        Node node = tree.getNode(indices[nodeCount-1]);
        locationMatirx[calculationCount][locationArray[indices[nodeCount-1]]]++;
        System.out.println(calculationCount +"\t"+ Arrays.toString(locationMatirx[calculationCount]));
        for (int i = (nodeCount -1); i > (tree.getLeafNodeCount()-1) ; i--) {
        	System.out.println("Node: " + i +"\t"+ indices[i]);
        	if(i != tree.getLeafNodeCount()){
        		System.arraycopy(locationMatirx[calculationCount], 0, locationMatirx[++calculationCount], 0, maxLocation);
        	}
        	
//        	System.out.println(calculationCount +"\t"+ Arrays.toString(locationMatirx[calculationCount]));
        	
        	node = tree.getNode(indices[i]);
        	int nodeNr = node.getNr();
        	int left = node.getLeft().getNr();
        	int right  = node.getRight().getNr();
//			System.out.println(times[i] +"\t"+ times[indices[i]] +"\t"+ node.getHeight());
			
			int locationCurrent = locationArray[nodeNr];
			int locationLeft = locationArray[left];
			int locationRight = locationArray[right];
			
			System.out.println(locationCurrent +"\t"+ locationLeft +"\t"+ locationRight);
			
			if(locationCurrent != locationRight){
				locationMatirx[calculationCount][locationRight]++;
			}
			if(locationCurrent != locationLeft){
				locationMatirx[calculationCount][locationLeft]++;
			}
			
			
			double height = tree.getNode(indices[i]).getHeight();
			double deltaHeight = height - tree.getNode(indices[i-1]).getHeight();
			
			System.out.println("NodeHeight: " + height +"\t"+ 
					calculationCount +"\t"+ deltaHeight +"\t"+ Arrays.toString(locationMatirx[calculationCount]));
			System.out.println();
			
		}
        
        
        //TODO: List of all possible count in the child
        //TODO: elipse time for the root is always 0. 
        
//        System.out.println(Arrays.toString(intervals));
//        for (int j = 0; j < intervals.length; j++) {
//        	System.out.println(ti.getLineageCount(j));
//		}
//        
        
//        System.out.println(root.getLength());
//        System.out.println(root.getHeight());
//        System.out.println(root.getLeft().getHeight());
//        System.out.println(root.getRight().getHeight());
//        System.out.println();
//        for (int i = 0; i < externalNodes.size(); i++) {
//			System.out.println(externalNodes.get(i).getHeight());
//		}
        System.out.println();
        List<Node> internalNodes = tree.getInternalNodes();
        for (int i = 0; i < internalNodes.size(); i++) {
			System.out.println(internalNodes.get(i).getHeight());
		}
        System.out.println();

        
        
//        ti.getCoalescentTimes(intervals);
        System.out.println(Arrays.toString(intervals));
        
        double[] ct = new double[tree.getNodeCount()];
        ti.getCoalescentTimes(ct);
        System.out.println(Arrays.toString(ct));
     
        
//        ((5:3.8345580927383667,4:3.8345580927383667)6:2.1642699030246364,((0:0.45241738192271774,3:0.45241738192271774)7:0.012894428733463859,(1:0.021157885660620927,2:0.021157885660620927)8:0.44415392499556067)9:5.533516185106821)10:0.0
//
//        3.8345580927383667
//        0.45241738192271774
//        0.021157885660620927
//        0.4653118106561816
//        5.998827995763003
//interval
//        [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.021157885660620927, 0.4312594962620968, 0.012894428733463859, 3.369246282082185, 2.1642699030246364]
//coalescent time
//        [0.021157885660620927, 0.45241738192271774, 0.4653118106561816, 3.8345580927383667, 5.998827995763003, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]

        
	}
	protected static void collectTimes(Tree tree, double[] times, int[] childCounts) {
        Node[] nodes = tree.getNodesAsArray();
        for (int i = 0; i < nodes.length; i++) {
            Node node = nodes[i];
            times[i] = node.getHeight();
            childCounts[i] = node.isLeaf() ? 0 : 2;
        }
    }
}
