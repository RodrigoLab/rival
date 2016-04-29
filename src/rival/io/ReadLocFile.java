package rival.io;
import java.io.*;
import java.util.*;

public class ReadLocFile {
	public ArrayList<String> sampleName;
	public ArrayList<String> sampleLoc;
	
	public ArrayList<String> locName;
	public ArrayList<Double> locLat;
	public ArrayList<Double> locLong;
	
	private HashMap<String, Integer> sampleName2ID;
	private HashMap<String, Integer> locName2ID; 
	
	public int getLocationFromTaxa(String taxa) {//throws Exception {
//		if (!sampleName2ID.containsKey(taxa))
//			throw new IOException("Error! Taxa " + taxa + " does not exist!");
		int sampleID = sampleName2ID.get(taxa).intValue();
		return locName2ID.get(sampleLoc.get(sampleID)).intValue();
	}
	
	public void readFile(String fileName) throws IOException {
		sampleName = new ArrayList<String>();
		sampleLoc = new ArrayList<String>();
		
		locName = new ArrayList<String>();
		locLat = new ArrayList<Double>();
		locLong = new ArrayList<Double>();
		
		sampleName2ID = new HashMap<String, Integer>();
		locName2ID = new HashMap<String, Integer>();

		BufferedReader br = new BufferedReader(new FileReader(fileName));
		
	    String line;
	    while ((line = br.readLine()) != null) {
	    	line = line.trim();
	    	if (line.length() == 0 || line.charAt(0) == '#')
	    		continue;
	       String[] tokens = line.split("\t");
	    	if (tokens.length == 2) {
	    		// check whether the sample name appear before
	    		if (sampleName2ID.containsKey(tokens[0]))
	    			throw new IOException("Inside the file " + fileName + ", sample name: " + tokens[0] + " appears more than once in the first table.");
		    	// update the HashMap - sampleName2ID
	    		sampleName2ID.put(tokens[0], new Integer(sampleName.size()));
	    		// add the sample name to sampleName
		    	sampleName.add(tokens[0]);
		    	// add the sample location to sampleLoc
		    	sampleLoc.add(tokens[1]);
	    	} else if (tokens.length==3) {
		    	// check whether the location name appear before
	    		if (locName2ID.containsKey(tokens[0]))
	    			throw new IOException("Inside the file " + fileName + ", location name: " + tokens[0] + " appears more than once in the second table.");
		    	// also update the HashMap - locName2ID
	    		locName2ID.put(tokens[0], new Integer(locName.size()));
	    		// add the sample location to sampleLoc
	    		locName.add(tokens[0]);
	    		// add the sample latitude to sampleLat
	    		locLat.add(Double.parseDouble(tokens[1]));
	    		// add the sample longitude to sampleLong
	    		locLong.add(Double.parseDouble(tokens[2]));
	    	} else {
	    		throw new IOException("The input format of " + fileName + " is not correct");
	    	}
	    }
	    // check whether all items inside the array "sampleLoc" appears in "locName2ID"
	    for (int i=0; i<sampleLoc.size(); i++) {
	    	if (!locName2ID.containsKey(sampleLoc.get(i))) {
	    		throw new IOException("Inside the file " + fileName + ", the location " + sampleLoc.get(i) + "");
	    	}
	    }
	}
}
