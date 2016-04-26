package rival.evolution.geo.distance;

import beast.core.CalculationNode;
import beast.core.Description;
import beast.core.Input;
import beast.evolution.alignment.Alignment;
import rival.io.*;

@Description("Calculate location distance from input files")
public class LocationDistance extends CalculationNode{
	
	final public Input<ReadLocFile> readLocFileInput = new Input<>
		("readLocFile", "Read location file");
	
	final public Input<String> typeOfDistanceInput = new Input<>("typeDist", "Type of distance for different classes");
    
	protected ReadLocFile locFile;
	protected String distType;

	private double[][] distMat;
	
	@Override
	public void initAndValidate() {
		if(readLocFileInput != null){
			locFile = readLocFileInput.get();
		}
		else{
			//TODO: handel error
		}
		
		if(typeOfDistanceInput != null){
			distType = typeOfDistanceInput.get();
			
		}
		else{
			distType = "euclidean-default";
		}
		System.out.println(distType +"\t"+  locFile.locLat.size());
		computeDistMat();
	}	
	
	public void computeDistMat() {
		//TODO: check distType
		int numLoc = locFile.locName.size();
		distMat = new double[numLoc][numLoc];
		for (int i=0; i<numLoc; i++) {
			for (int j=0; j<numLoc; j++) {
				double latDiff = locFile.locLat.get(i).doubleValue() -locFile.locLat.get(j).doubleValue();
				double longDiff = locFile.locLong.get(i).doubleValue() -locFile.locLong.get(j).doubleValue();
				distMat[i][j] = Math.sqrt(latDiff*latDiff + longDiff*longDiff);
			}
		}
	}

	public double[][] getDistMat() {
		return distMat;
	}

	
	//TODO: implement these three when the island start moving
	@Override
	protected void store(){
		super.store();
	}
	@Override
	protected void restore(){
		super.restore();
	}
	@Override
	protected boolean requiresRecalculation(){
		return super.requiresRecalculation();
	}

	
}
