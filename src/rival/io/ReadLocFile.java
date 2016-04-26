package rival.io;
import java.io.*;
import java.util.*;

public class ReadLocFile {
	public ArrayList<String> sampleIDs;
	public ArrayList<String> locations;
	
	public ArrayList<String> locName;
	public ArrayList<Double> locLat;
	public ArrayList<Double> locLong;
	
	public void readFile(String fileName) throws IOException {
		sampleIDs = new ArrayList<String>();
		locations = new ArrayList<String>();
		
		locName = new ArrayList<String>();
		locLat = new ArrayList<Double>();
		locLong = new ArrayList<Double>();

		Set<String> locSet = new HashSet<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	line = line.trim();
		    	if (line.length() == 0 || line.charAt(0) == '#')
		    		continue;
		       String[] tokens = line.split("\t");
		    	if (tokens.length == 2) {
			    	// add the sample Id to sampleID
			    	sampleIDs.add(tokens[0]);
			    	// add the sample location to locations
			    	locations.add(tokens[1]);
		    	} else if (tokens.length==3) {
		    		// add the sample location to sampleLoc
		    		locName.add(tokens[0]);
		    		// add the sample latitude to sampleLat
		    		locLat.add(Double.parseDouble(tokens[1]));
		    		// add the sample longitude to sampleLong
		    		locLong.add(Double.parseDouble(tokens[2]));
			    	// also add the sample locations to the set (for validation in the next step)
			    	locSet.add(tokens[0]);
		    	} else {
		    		throw new IOException("The input format of " + fileName + " is not correct");
		    	}
		    }
		    // check whether all items inside the array "locations" appears in "locSet"
		    for (int i=0; i<locations.size(); i++) {
		    	if (!locSet.contains(locations.get(i))) {
		    		throw new IOException("Inside the file " + fileName + ", the location " + locations.get(i) + "");
		    	}
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
