package analysis.biological;

import general.Tricluster;
import input.datasets.Biological;
import input.laboratory.Options;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import labutils.GeneOntology;



import utils.InTextFile;
import utils.OutTextFile;
import utils.SystemUtilities;
import utils.TextUtilities;

public class BiologicalAnalysis {
	
//	@SuppressWarnings("unused")
//	private static final Logger LOG = LoggerFactory.getLogger(BiologicalAnalysis.class);
	
	private String currentCommand;
	
	//from options
	
	private String appPath;
	private String mtc;
	private String calculation;
	private String dot;
	private String filter;
	private boolean ignore;
	private boolean annotation;
	private String resamplingsteps;
	
	//from resources
	
	private String go;
	private String association;
	private String population;
	
	//from triclusters
	
	private String outdir;
	private String studyset;

	
	//folder management
	private File namesFolder;
	private File tablesFolder;
	
	private File bufferFile;
	
	
	private String[] currentGeneNames;
	
	
	public BiologicalAnalysis (Biological resources, Options options) throws IOException{
						
		this.mtc = options.getGoMtc();
		this.calculation = options.getGoCalculation();
		this.dot = options.getGoDot();
		this.filter = options.getGoFilter();
		this.ignore = options.isGoIgnore();
		this.annotation = options.isGoAnnotation();
		this.resamplingsteps = options.getGOrsteps();
		
		String outName = options.getOutName();
		
		this.namesFolder = new File(options.getOutFolder(),"temp-"+outName+"-GOnames");
		
		namesFolder.mkdir();
		
		this.tablesFolder = new File(options.getOutFolder(),"temp-"+outName+"-GOtables");
		
		tablesFolder.mkdir();
				
		this.bufferFile = new File (options.getOutFolder(),"temp-"+outName+"_buffer.txt");
				
		this.studyset = namesFolder.getAbsolutePath();
		
		this.outdir = tablesFolder.getAbsolutePath();
		
		
		
		String org = (resources.getOrganism()).toLowerCase();
		
		this.appPath = SystemUtilities.getGOappPath();
		
		this.go = SystemUtilities.getGOtermsPath();
		
		this.association = SystemUtilities.getGOassociationPath(org);
		
		this.population = resources.getGenesPath();
		
		currentGeneNames = resources.getGeneNames();
	
		this.currentCommand = "";
							
	}
		
	public List<GoStudy> getAnalysis(List<Tricluster> triclusters, boolean persistent) throws IOException, InterruptedException{
		
		List<GoStudy> gostudies = new ArrayList<GoStudy>();
		
		//CREATE NAMES AND TABLES FOLDER
		
//		namesFolder.mkdir();
//		tablesFolder.mkdir();
		
		//CREATE TEMPORAL FOLDER
		
//		LOG.debug("genes = "+currentGeneNames.length);
		
		List<String []> tags = GeneOntology.getAllGeneNames(triclusters,currentGeneNames);
		
		//CREATE STUDY SET (GENE FILES)
		
//		LOG.info("Building Go Study sets");
		
		
//		LOG.debug("TS = "+tags.size());
		createStudySets(tags);
		
		//LAUNCH GO APP
		
//		LOG.info("Launching Go App");
		
		launchGOApp ();
		
//		LOG.info("Loading Go analysis");
		
		////////////////////////////////////////////////////////////////////////////////////////////////LOAD RESULT FILES
		
		File[] tableFiles = tablesFolder.listFiles();
		
		Arrays.sort(tableFiles,new FileNameComparator());
		
//		LOG.debug("Table files length = "+tableFiles.length);
		
		for (int i = 0; i < tableFiles.length; i++) {

			File table = tableFiles[i];

			InTextFile f = new InTextFile(table);

			GoStudy st = new GoStudy("sol_" + (i + 1));

			int j = 0;

			for (String line : f) {

				if (j != 0) {

					List<String> ls = TextUtilities.splitElements(line, "\t");
					
					if (ls.size()>1) {
						if (j == 1) {

							int popTotal = Integer.parseInt(ls.get(1));

							int studyTotal = Integer.parseInt(ls.get(3));

							st.setPopTotal(popTotal);

							st.setStudyTotal(studyTotal);

						}
						
						String id = ls.get(0);
						
						//substring -> remove quotes
						String name = (ls.get(8)).substring(1, (ls.get(8)).length()-1);
						
						double p = Double.parseDouble(ls.get(5));
						
						double pAdjusted = Double.parseDouble(ls.get(6));
						
						double pMin = Double.parseDouble(ls.get(7));
						
						int popTerm = Integer.parseInt(ls.get(2));
						
						int studyTerm = Integer.parseInt(ls.get(4));
						
						GoTerm gt = new GoTerm(id, name, p, pAdjusted, pMin, popTerm, studyTerm);
						
						//LOG.debug(j+" : "+gt.toString());
						
						st.addGoTerm(gt);
						
					}

				}

				j++;

			}

			f.close();

			gostudies.add(st);

		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////LOAD RESULT FILES
		
		
		
		
		//Check persistence
		
		if(!persistent){
			deleteFoler(namesFolder);
			deleteFoler(tablesFolder);	
			bufferFile.delete();
		}
					
		return gostudies;
		
	}

	/**
	 * @return the appPath
	 */
	public String getAppPath() {
		return appPath;
	}


	/**
	 * @param appPath the appPath to set
	 */
	public void setAppPath(String appPath) {
		this.appPath = appPath;
	}


	/**
	 * @return the mtc
	 */
	public String getMtc() {
		return mtc;
	}


	/**
	 * @param mtc the mtc to set
	 */
	public void setMtc(String mtc) {
		this.mtc = mtc;
	}


	/**
	 * @return the calculation
	 */
	public String getCalculation() {
		return calculation;
	}


	/**
	 * @param calculation the calculation to set
	 */
	public void setCalculation(String calculation) {
		this.calculation = calculation;
	}


	/**
	 * @return the dot
	 */
	public String getDot() {
		return dot;
	}


	/**
	 * @param dot the dot to set
	 */
	public void setDot(String dot) {
		this.dot = dot;
	}


	/**
	 * @return the filter
	 */
	public String getFilter() {
		return filter;
	}


	/**
	 * @param filter the filter to set
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}


	/**
	 * @return the ignore
	 */
	public boolean getIgnore() {
		return ignore;
	}


	/**
	 * @param ignore the ignore to set
	 */
	public void setIgnore(boolean ignore) {
		this.ignore = ignore;
	}


	/**
	 * @return the annotation
	 */
	public boolean getAnnotation() {
		return annotation;
	}


	/**
	 * @param annotation the annotation to set
	 */
	public void setAnnotation(boolean annotation) {
		this.annotation = annotation;
	}


	/**
	 * @return the resamplingsteps
	 */
	public String getResamplingsteps() {
		return resamplingsteps;
	}


	/**
	 * @param resamplingsteps the resamplingsteps to set
	 */
	public void setResamplingsteps(String resamplingsteps) {
		this.resamplingsteps = resamplingsteps;
	}


	
	//PRIVATE METHODS
	
	private void createStudySets (List<String []> geneNames) throws FileNotFoundException{
				
		int index = 1;
		
		for (String[] geneList:geneNames){
			
			String filePath = namesFolder.getAbsolutePath()+"/genes_"+index+".txt";
			
//			LOG.debug(filePath);
			
			OutTextFile f = new OutTextFile(filePath);
			
//			LOG.debug("GL = "+geneList.length);
			
			for (int i = 0;i<geneList.length;i++){
				
				f.println(geneList[i]);
				
			}
			
			f.close();			
			
			index++;
		}
				
	}
	
	
	private void launchGOApp () throws IOException, InterruptedException{
		buildCurrentCommand();
		runCurrentCommand();
	}
	
	
	private void buildCurrentCommand(){
			
		currentCommand="java -jar "+appPath+" -g "+go+" -a "+association+" -p "+population+" -s "+studyset+" -o "+outdir
				+" -m "+mtc+" -c "+calculation;
				
		if (!filter.equalsIgnoreCase(""))
			currentCommand+=" -f "+filter;
		
		if (ignore)
			currentCommand+=" -i";
		
		if (annotation)
			currentCommand+=" -n";
		
		if (!resamplingsteps.equalsIgnoreCase(""))
			currentCommand+=" -r "+resamplingsteps;
		
		
//		String pipe = " 2> /Users/davgutavi/Desktop/pipe";
//		
//		currentCommand+=pipe;
//		
//		LOG.debug(currentCommand);
		
	}
	
	private void runCurrentCommand() throws IOException, InterruptedException {

		Runtime rt = Runtime.getRuntime();

		OutTextFile buffer = new OutTextFile(bufferFile);

		try {

			//LOG.debug("executing " + currentCommand);

			Process p = rt.exec(currentCommand);

			InputStream stderr = p.getErrorStream();

			InputStreamReader isr = new InputStreamReader(stderr);

			BufferedReader br = new BufferedReader(isr);

			String line = null;

			while ((line = br.readLine()) != null) {

				//LOG.info(line);
				buffer.print(line);
			}
			
			p.waitFor();
			
			buffer.close();
			
		}

		catch (Throwable t) {
			t.printStackTrace();
		}
	}
               
	
	
	private void deleteFoler (File folder){
		
		 File[] files = folder.listFiles();
		 
		 for (int i=0;i<files.length;i++){
			 
			 if (files[i].isDirectory()) 
				 deleteFoler(files[i]);
			 
			 files[i].delete();
			 
		 }
		 
		 folder.delete();
			
	}
	
	private class FileNameComparator implements Comparator<File> {
	    
		public int compare(File f1, File f2)
	    {       	
								
			int i1 = TextUtilities.getIndexFromGOfileName(f1.getName());
			int i2 = TextUtilities.getIndexFromGOfileName(f2.getName());
								
			return Integer.compare(i1, i2);
	    }
	
	}
	
	
	
	

}
