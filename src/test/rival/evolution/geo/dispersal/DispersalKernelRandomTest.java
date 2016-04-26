package test.rival.evolution.geo.dispersal;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import beast.core.parameter.RealParameter;
import rival.evolution.geo.dispersal.DispersalKernelRandom;
import rival.evolution.geo.distance.LocationDistance;
import rival.evolution.geo.distance.LongitudeDistance;
import rival.io.ReadLocFile;

public class DispersalKernelRandomTest {

	
	@Test
	public void testRandom() throws Exception {
		
		String userPath = System.getProperty("user.dir");
		String fileName = userPath + "/data/Banza.loc";
		
		ReadLocFile readFile = new ReadLocFile();
		readFile.readFile(fileName);
		
		LocationDistance locDist = new LocationDistance();
		locDist.initByName("readLocFile", readFile, "typeDist", "euclidean");
//		
//		double[][] expectedMatirx = new double ???
		double[][] distMat = locDist.getDistMat();
		

		DispersalKernelRandom dkr = new DispersalKernelRandom();
		dkr.setInputValue("distMatrix", distMat);
		
		RealParameter rnd = new RealParameter(new Double[]{0.5});
		dkr.setInputValue("param", rnd);
		
		dkr.initAndValidate();
		
		
		
		double[][] kernel = dkr.getKernel();
		for (int i = 0; i < distMat.length; i++) {
			double[] ds = distMat[i];
			System.out.println("Distance: "+ Arrays.toString(ds));
			System.out.println("Kernel  : "+Arrays.toString(kernel[i]));
			System.out.println();
			
		}
		
		
		
		
	}
}
