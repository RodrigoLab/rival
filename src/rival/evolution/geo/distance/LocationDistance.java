package rival.evolution.geo.distance;

import rival.io.*;

public class LocationDistance {
	public double[][] distMat;
	public void computeDistMat(ReadLocFile loc) {
		int numLoc = loc.locName.size();
		distMat = new double[numLoc][numLoc];
		for (int i=0; i<numLoc; i++) {
			for (int j=0; j<numLoc; j++) {
				double latDiff = loc.locLat.get(i).doubleValue() -loc.locLat.get(j).doubleValue();
				double longDiff = loc.locLong.get(i).doubleValue() -loc.locLong.get(j).doubleValue();
				distMat[i][j] = Math.sqrt(latDiff*latDiff + longDiff*longDiff);
			}
		}
	}	
}
