package util.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import beast.app.treeannotator.TreeSetParser;
import beast.evolution.alignment.Alignment;
//import beast.evolution.io.FastaImporter;
//import beast.evolution.io.NewickImporter;
//import beast.evolution.io.NexusImporter;
//import beast.evolution.io.Importer.ImportException;
import beast.evolution.alignment.Sequence;
import beast.evolution.datatype.DataType;
import beast.evolution.tree.Node;
//import beast.evolution.sequence.SequenceList;
//import beast.evolution.sequence.Sequences;
import beast.evolution.tree.Tree;
import beast.util.NexusParser;
import beast.util.TreeParser;


public class DataImporter {

//	private SimpleAlignment alignment;
//	private FlexibleTree tree;
	
//	private static final double MIN_FREQ = SpectraParameter.MIN_FREQ;
	protected String dataDir;
	
	public DataImporter(String dataDir){

		if(dataDir.lastIndexOf(File.separator)!= (dataDir.length()-1) ){
			dataDir += File.separator;
		}
		this.dataDir = dataDir;
	}
	
//	public DataImporter() {
//		this.dataDir = "";
//	}
	public Alignment importShortReads(String fileName) throws Exception{
		
		Alignment alignment = importShortReads(dataDir, fileName);
		return alignment;
	}
	
	public Alignment importAlignment(String fileName) throws Exception{
		
		Alignment alignment;
		if(fileName.indexOf(dataDir)!=-1){
			alignment = importAlignment("", fileName);
		}
		else{
			alignment = importAlignment(dataDir, fileName);
		}
		
		return alignment;
	}
	
	public Tree importTree(String fileName) throws Exception{
		Tree tree;
		if(fileName.indexOf(dataDir)!=-1){
			tree =  importTree("", fileName);
		}
		else{
			tree =  importTree(dataDir, fileName);
		}
		return tree;
	}

//	public Sequences importSequence(String fileName) throws Exception{
//		
//		Sequences seqs =  (Sequences) importSequences(dataDir, fileName);
//		return seqs;
//	}
//	
//	public Sequence importRefSeq(String fileName) throws Exception{
//		Sequence seq = importRefSeq(dataDir, fileName);
//		return seq;
//	}
//	
//	public SpectrumAlignmentModel importPartialSpectrumFile(String partialSpectrumName) throws IOException {
//		
//		return importPartialSpectrumFile(dataDir, partialSpectrumName);
//	}
//	
//	public static SpectrumAlignmentModel importPartialSpectrumFile(
//			String dataDir, String partialSpectrumName) throws IOException {
//	
//		BufferedReader inFile  = new BufferedReader(new FileReader(dataDir+partialSpectrumName));
//		String inline;
//		int length = 0;
//		SpectrumAlignmentModel spectrumModel = null;
//		
//		while((inline = inFile.readLine())!=null){
//			
//			if(inline.startsWith(">")){
//				String taxonName = inline.substring(1, inline.length()).trim();
//				Taxon taxon = new Taxon(taxonName);
////				System.out.println(name);
//				
//				if(length != 0){
//					double[][] freqs = new double[4][length];
//					for (int i = 0; i < 4; i++) {
//						inline = inFile.readLine();
////						StringTokenizer st = new StringTokenizer(inline);
//						String[] result = inline.split("\\s");
//						for (int j = 0; j < length; j++) {
//							freqs[i][j] = Double.parseDouble(result[j]);
//							if(freqs[i][j]< MIN_FREQ){
//								freqs[i][j]= MIN_FREQ;
//							}
//						}
//					}
//					Spectrum spectrum = new Spectrum(freqs);
//					spectrum.setTaxon(taxon);
//					
//					int taxonIndex = spectrumModel.getTaxonIndex(taxonName);
//					if(taxonIndex != -1){
//						spectrumModel.removeSpectrum(taxonIndex);
////						System.err.println("remove "+taxonName +"\t"+ taxonIndex);
//					}
//					spectrumModel.addSpectrum(spectrum);
//					
//					
//				}
//				else{
//					inline = inFile.readLine();
//					String[] result = inline.split("\\s");
//					
//					length = result.length;
//					spectrumModel = new SpectrumAlignmentModel(length);
//					
//					double[][] freqs = new double[4][length];
//					for (int j = 0; j < length; j++) {
//						freqs[0][j] = Double.parseDouble(result[j]);
//						if(freqs[0][j]< MIN_FREQ){
//							freqs[0][j]=MIN_FREQ;
//						}
//					}
//				
//					for (int i = 1; i < 4; i++) {
//						inline = inFile.readLine();
//						result = inline.split("\\s");
//						for (int j = 0; j < length; j++) {
//							freqs[i][j] = Double.parseDouble(result[j]);
//							if(freqs[i][j]< MIN_FREQ){
//								freqs[i][j] = MIN_FREQ;
//							}
//						}
//					}
//					Spectrum spectrum = new Spectrum(freqs);
//					spectrum.setTaxon(taxon);
//					spectrumModel.addSpectrum(spectrum);
//					
//				}
//				 
//				
//			}
//			
////			else if(!inline.equals("")){
////				System.out.println(inline);
////			}
//				
//		}
////		int length = 0;
////		SpectrumAlignmentModel model = new SpectrumAlignmentModel(length);
//		inFile.close();
//		return spectrumModel;
//	}
	
//	public static Sequence importRefSeq(String dataDir, String fileName) throws Exception{
//
//		
//		String filePath = dataDir+fileName;
//		
//		BufferedReader in = new BufferedReader(new FileReader(filePath));
//		FastaImporter importer = new FastaImporter(in, Nucleotides.INSTANCE);
//		SequenceList seqList = importer.importSequences();
//		Sequence seq = seqList.getSequence(0);
//		if(seqList.getSequenceCount()>1){
//			System.err.println("Multiple ref seq\t"+seqList.getSequenceCount());
//		}
//	
//		return seq;
//	}
//
//	public static SequenceList importSequences(String dataDir, String fileName) throws Exception {
//
//		
//		String filePath = dataDir+fileName;
//		
//		BufferedReader in = new BufferedReader(new FileReader(filePath));
//		FastaImporter importer = new FastaImporter(in, Nucleotides.INSTANCE);
//		SequenceList seqs = importer.importSequences();
//
//		return seqs;
//		
//	}

	public static Alignment importAlignment(String dataDir, String fileName) throws Exception{
		
		
		String filePath = dataDir+fileName;
		Alignment alignment;
		if(fileName.contains("nex")){
			final NexusParser parser = new NexusParser();
            parser.parseFile(new File(filePath));
            alignment = parser.m_alignment;
//			Alignment alignment = getFASTAData(new File(filePath));
		}
		else{
			alignment = getFASTAData(new File(filePath));
		}

		
		return alignment;
	}
	
	public static Alignment importShortReads(String dataDir, String fileName) throws Exception{
		//HACK!
//		String filePath = dataDir+fileName;
//		
//		BufferedReader in = new BufferedReader(new FileReader(filePath));
//		ShortReadImporter importer = new ShortReadImporter(in, ShortReads.INSTANCE);
//		Alignment alignment = importer.importAlignment();
		Alignment alignment = importAlignment(dataDir, fileName);
		return alignment;
	}
	
		
	public static Tree importTree(String dataDir, String fileName) throws Exception{
		
		String filePath = dataDir+fileName;

		BufferedReader in = new BufferedReader(new FileReader(filePath));
//		NewickImporter importer = new NewickImporter(in);
//		NexusImporter importer = new NexusImporter(in);
//		Tree tree = importer.importTree(null);
		
		TreeSetParser parser = new TreeSetParser(0, false);
      	Node [] roots = parser.parseFile(filePath);
      	
      	TreeParser tree = new TreeParser();
        tree.initByName(
                "newick", "((((human:0.024003,(chimp:0.010772,bonobo:0.010772):0.013231):0.012035,gorilla:0.036038):0.033087000000000005,orangutan:0.069125):0.030456999999999998,siamang:0.099582);",
                "IsLabelledNewick", true);

//      	Tree tree = null;
		return tree;
	}
	
	// ******************************
	// Methods copied from beast.app.beauti.BeautiAlignmentProvider
	// **********************************	

	private static Alignment getFASTAData(File file) {
    	try {
    		// grab alignment data
        	Map<String, StringBuilder> seqMap = new HashMap<>();
        	List<String> taxa = new ArrayList<>();
        	String currentTaxon = null;
			BufferedReader fin = new BufferedReader(new FileReader(file));
	        String missing = "?";
	        String gap = "-";
	        int totalCount = 4;
//	        UserDataType usd = new UserDataType();
//	        usd.i
	        String datatype = "nucleotide"; //HACK HERE!!!
	        // According to http://en.wikipedia.org/wiki/FASTA_format lists file formats and their data content
			// .fna = nucleic acid
			// .ffn = nucleotide coding regions
			// .frn = non-coding RNA
			// .ffa = amino acid
    		boolean mayBeAminoacid = !(file.getName().toLowerCase().endsWith(".fna") || file.getName().toLowerCase().endsWith(".ffn") || file.getName().toLowerCase().endsWith(".frn"));
    		
			while (fin.ready()) {
				String line = fin.readLine();
				if (line.startsWith(";")) {
					// it is a comment, ignore
				} else 	if (line.startsWith(">")) {
					// it is a taxon
					currentTaxon = line.substring(1).trim();
					// only up to first space
					currentTaxon = currentTaxon.replaceAll("\\s.*$", "");
				} else {
					// it is a data line
					if (currentTaxon == null) {
						fin.close();
						throw new RuntimeException("Expected taxon defined on first line");
					}
					if (seqMap.containsKey(currentTaxon)) {
						StringBuilder sb = seqMap.get(currentTaxon);
						sb.append(line);
					} else {
						StringBuilder sb = new StringBuilder();
						seqMap.put(currentTaxon, sb);
						sb.append(line);
						taxa.add(currentTaxon);
					}
				}
			}
			fin.close();
			
			int charCount = -1;
			Alignment alignment = new Alignment();
	        for (final String taxon : taxa) {
	            final StringBuilder bsData = seqMap.get(taxon);
	            String data = bsData.toString();
	            data = data.replaceAll("\\s", "");
	            seqMap.put(taxon, new StringBuilder(data));

	            if (charCount < 0) {charCount = data.length();}
	            if (data.length() != charCount) {
	                throw new IllegalArgumentException("Expected sequence of length " + charCount + " instead of " + data.length() + " for taxon " + taxon);
	            }
	            // map to standard missing and gap chars
	            data = data.replace(missing.charAt(0), DataType.MISSING_CHAR);
	            data = data.replace(gap.charAt(0), DataType.GAP_CHAR);

	            if (mayBeAminoacid && datatype.equals("nucleotide") && !data.matches("[ACGTUXNacgtuxn?_-]+")) {
	            	datatype = "aminoacid";
	            	totalCount = 20;
	            	for (Sequence seq : alignment.sequenceInput.get()) {
	            		seq.totalCountInput.setValue(totalCount, seq);
	            	}
	            }
	            
	            final Sequence sequence = new Sequence();
	            data = data.replaceAll("[Xx\\.]", "?");
	            sequence.init(totalCount, taxon, data);
	            sequence.setID(NexusParser.generateSequenceID(taxon));
	            alignment.sequenceInput.setValue(sequence, alignment);
	        }
	        String ID = file.getName();
	        ID = ID.substring(0, ID.lastIndexOf('.')).replaceAll("\\..*", "");
	        alignment.setID(ID);
			alignment.dataTypeInput.setValue(datatype, alignment);
	        alignment.initAndValidate();
	        return alignment;
    	} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Loading of " + file.getName() + " failed: " + e.getMessage());
    	}
		return null;
	}

	
}
