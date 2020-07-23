package analysis.reports;

import java.text.DecimalFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import analysis.Experiment;
import analysis.Solution;
import utils.TextUtilities;

public class ExpLevelReports {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(ExpLevelReports.class);
		
		private static final String EXPERIMENT   = "EXPERIMENT";
		private static final String DATASET      = "DATASET";
		private static final String BESTSOLUTION = "BEST SOLUTION";
		private static final String BESTTRIQ     = "BEST TRIQ";
		private static final String MEAN         = "MEAN";
		private static final String SDEV         = "SDEV";
		private static final String BESTSOLUTIONN = "BEST SOLUTIONN";
		private static final String BESTTRIQN     = "BEST TRIQN";
		private static final String MEANN         = "MEANN";
		private static final String SDEVN         = "SDEVN";
		
		
			
		
		public static String getCompleteReport (List<Experiment> exps, char analysisType){
			
			return getCompleteReport(exps,analysisType,";",true);
			
		}
		
	
		
		public static String getCompleteReport (List<Experiment> exps,char analysisType, String sep, boolean header, DecimalFormat ... decimalFormat){
							
			String r = "";
			
			if (header){
				r+=getCompleteHeader(sep,analysisType)+"\n";
			}
			
			for(Experiment exp:exps){
				
				String prefix = exp.getDatasetName()+sep+exp.getExperimentName()+sep;
								
				for (Solution sol:exp.getSolutions()){
				
					r+=prefix+SolutionLevelReports.getSolutionLine(sol,analysisType,sep, decimalFormat)+"\n";		
				
				}
								
			}
					
			return r;
						
		}
				
		private static String getCompleteHeader (String sep, char analysisType){
			
			String r = "";
			
			r = DATASET+sep+EXPERIMENT+sep;
			
			r+=SolutionLevelReports.getHeader(analysisType, sep);
					
			return r;
			
		}
				
		
		
		public static String getELreport (List<Experiment> exps){
			
			return getELreport (exps, ";", true, TextUtilities.getDecimalFormat('.',"0.####"));
			
		}
		
		public static String getELreport (List<Experiment> exps, String sep, boolean header, DecimalFormat ... decimalFormat){
			
			String r = "";
			
			if (header){
				r+=getHeader(sep)+"\n";
			}
			
			for(Experiment exp:exps){
				
				r+=getExpLine(exp,sep, decimalFormat)+"\n";		
				
			}
					
			return r;
		}


		private static String getExpLine(Experiment exp, String sep,DecimalFormat... decimalFormat) {
			
			String r = "";
			
			String name = exp.getExperimentName()+sep;
			String dataset = exp.getDatasetName()+sep;
			String bestsol = ((Solution)exp.getValue("bestsolution")).getName()+sep;
			String besttriq = ((Double)exp.getValue("besttriq"))+sep;
			String mean = ((Double)exp.getValue("mean"))+sep;
			String stdev = ((Double)exp.getValue("stdev"))+sep;
			
//			LOG.debug(exp.getValue("bestsolution").getClass().getCanonicalName());
//			LOG.debug(exp.getValue("bestsolutionn").getClass().getCanonicalName());
//			LOG.debug(exp.getValue("besttriqn").getClass().getCanonicalName());
//			LOG.debug(exp.getValue("meann").getClass().getCanonicalName());
//			LOG.debug(exp.getValue("stdevn").getClass().getCanonicalName());
			
			String bestsoln = "";
			String besttriqn = "";
			String meann =  "";
			String stdevn =  "";
			
			
			if (exp.getAnalysisType()=='b') {
				bestsoln = ((Solution)exp.getValue("bestsolutionn")).getName()+sep;
				besttriqn = ((Double)exp.getValue("besttriqn"))+sep;
				meann = ((Double)exp.getValue("meann"))+sep;
				stdevn = ((Double)exp.getValue("stdevn"))+sep;
			}
						
			r = name+dataset+bestsol+besttriq+mean+stdev+bestsoln+besttriqn+meann+stdevn;
			
			return r;
		}


		public static String getHeader(String sep) {
					
			String r = EXPERIMENT+sep+DATASET+sep+BESTSOLUTION+sep+BESTTRIQ+sep+MEAN+sep+SDEV+sep+BESTSOLUTIONN+sep+BESTTRIQN+sep+MEANN+sep+SDEVN;
			
			return r;
		}
		
		
		
		

}
