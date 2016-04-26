package test.rival.io;
import org.junit.Test;

import rival.io.ReadLocFile;

public class ReadLocFileTest {

		@Test
		public void testLocFile() throws Exception {
			
			String userPath = System.getProperty("user.dir");
			String fileName = userPath + "/data/Banza.loc";
			
			ReadLocFile readFile = new ReadLocFile();
			readFile.readFile(fileName);
			
			for (int i=0; i<readFile.sampleName.size(); i++) {
				String taxa = readFile.sampleName.get(i);
				System.out.println(taxa + " " + readFile.getLocationFromTaxa(taxa));
			}
			
		}
	

}
