package test.rival.evolution.geo;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import rival.evolution.geo.distance.LocationDistance;
import rival.evolution.geo.distance.LongitudeDistance;
import rival.io.ReadLocFile;

public class LocationDistanceTest {

	
	@Test
	public void testLocation() throws Exception {
		
		String userPath = System.getProperty("user.dir");
		String fileName = userPath + "/data/Banza.loc";
		
		ReadLocFile readFile = new ReadLocFile();
		readFile.readFile(fileName);
		
		LocationDistance locDist = new LocationDistance();
		locDist.initByName("readLocFile", readFile, "typeDist", "euclidean");
		
//		
//		double[][] expectedMatirx = new double ???
		double[][] distMat = locDist.getDistMat();
		
		
		for (int i = 0; i < distMat.length; i++) {
			double[] ds = distMat[i];
			System.out.println(Arrays.toString(ds));
			for (int j = 0; j < ds.length; j++) {
				double d = ds[j];
//				assertEquals(expectedMatrix, d);;
			}
		}
		
		
	}
}
