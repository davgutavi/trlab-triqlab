package analysis.reports;

import java.text.DecimalFormat;
import java.util.List;

import analysis.Experiment;
import analysis.Solution;
import utils.TextUtilities;

public class ExpLevelReports {
		
		private static final String EXPERIMENT   = "EXPERIMENT";
		private static final String DATASET      = "DATASET";
		private static final String BESTSOLUTION = "BEST SOLUTION";
		private static final String BESTTRIQ     = "BEST TRIQ";
		private static final String MEAN         = "MEAN";
		private static final String SDEV         = "SDEV";
		
		
			
		
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
			
			r = name+dataset+bestsol+besttriq+mean+stdev;
			
			return r;
		}


		public static String getHeader(String sep) {
					
			String r = EXPERIMENT+sep+DATASET+sep+BESTSOLUTION+sep+BESTTRIQ+sep+MEAN+sep+SDEV;
			
			return r;
		}
		
		
		
		

}
