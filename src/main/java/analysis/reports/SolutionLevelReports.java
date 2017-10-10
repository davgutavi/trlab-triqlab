package analysis.reports;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import analysis.Experiment;
import analysis.Solution;

public class SolutionLevelReports {
	
	//GENERAL
	
	private static final String SOLUTION  = "SOLUTION";		
	private static final String TRIQ = "TRIQ";
	
	//TRIQ
	private static final String BIOQ = "BIOQ";
	private static final String TRIQN = "TRIQN";
	private static final String BIOQN = "BIOQN";
	private static final String GRQ  = "GRQ";
	private static final String PEQ  = "PEQ";
	private static final String SPQ  = "SPQ";
	
	
	public static String getSLcompletefile (List<Experiment> exps){
		
		String r = "";
		
		r=getHeader('b',";")+"\n";
		
		for (Experiment exp:exps){
						
			r+=getSLreport(exp,";",false)+"\n";
			
		}
		
		return r;
		
	}
	
	
	public static String getSLSinglefile (Experiment exp){
		
		List<Experiment> aux = new LinkedList<Experiment>();
		
		aux.add(exp);
		
		String r = ExpLevelReports.getELreport(aux);
		
		r+="\n;\n";
		
		r+= getSLreport(exp,";",true);
		
		return r;
		
	}
	
	
	
	public static String getSLfile (Experiment exp){
		
		return getSLreport(exp,";",true);
		
	}
		
	
	public static String getSLreport (Experiment exp, String sep, boolean header, DecimalFormat ... decimalFormat){
		
		String r = "";
		
		if (header){
			r+=getHeader(exp.getAnalysisType(),sep)+"\n";
		}
		
		for(Solution sol:exp.getSolutions()){
			
			r+=getSolutionLine(sol,exp.getAnalysisType(),sep,decimalFormat)+"\n";		
			
		}
				
		return r;
	}
	
	//**********************************************************SOLUTION LINE

	protected static String getSolutionLine(Solution sol, char expType, String sep, DecimalFormat... decimalFormat) {
		
		String r = "";
	
		r = lineL1(sol,expType,sep,decimalFormat);	
		
		return r;
	
	}
	
	private static String lineL1(Solution sol, char expType, String sep,DecimalFormat... decimalFormat) {
		

		String r = "";
		
		if (expType=='b'){
		
		String name     = sol.getName()+sep; 
		double triq     = sol.getValue("triq");
		double bioq     = sol.getValue("bioq");
		
		double triqn     = sol.getValue("triqn");
		double bioqn     = sol.getValue("bioqn");
		
		double grq      = sol.getValue("grq");
		double peq      = sol.getValue("peq");
		double spq      = sol.getValue("spq");
				
		String s_triq     = "";		
		String s_bioq     = "";
		
		String s_triqn     = "";		
		String s_bioqn     = "";
		
		String s_grq      = "";
		String s_peq      = "";
		String s_spq      = "";
				
		if (decimalFormat.length==0){
			
			 s_triq     = triq+sep;
			 s_bioq     = bioq+sep;
			 
			 s_triqn     = triqn+sep;
			 s_bioqn     = bioqn+sep;
			 
			 s_grq      = grq+sep;
			 s_peq      = peq+sep;
			 s_spq      = spq+sep;
				
		}
		
		else{
			
			DecimalFormat fr = decimalFormat[0];
			
			 	
			 s_triq     = fr.format(triq)+sep;
			 s_bioq     = fr.format(bioq)+sep;
			 
			 s_triqn     = fr.format(triqn)+sep;
			 s_bioqn     = fr.format(bioqn)+sep;
			 
			 s_grq      = fr.format(grq)+sep;
			 s_peq      = fr.format(peq)+sep;
			 s_spq      = fr.format(spq)+sep;
			
		}
		
		r = name+s_triq+	s_bioq+s_triqn+s_bioqn+s_grq+s_peq+s_spq;
	
		}
		else{
			
			String name     = sol.getName()+sep; 
			double triq     = sol.getValue("triq");
			
			double grq      = sol.getValue("grq");
			double peq      = sol.getValue("peq");
			double spq      = sol.getValue("spq");
			
			
			String s_triq     = "";
			
			String s_grq      = "";
			String s_peq      = "";
			String s_spq      = "";
			
			if (decimalFormat.length==0){
				
				 s_triq     = triq+sep;
				 s_grq      = grq+sep;
				 s_peq      = peq+sep;
				 s_spq      = spq+sep;
					
			}
			
			else{
				
				DecimalFormat fr = decimalFormat[0];
				
				 	
				 s_triq     = fr.format(triq)+sep;
				 s_grq      = fr.format(grq)+sep;
				 s_peq      = fr.format(peq)+sep;
				 s_spq      = fr.format(spq)+sep;
				
			}
			
			r = name+s_triq+	s_grq+s_peq+s_spq;
		}
			
		return r;
	}



	
	protected static String getHeader(char expType, String sep) {
		
		String r = "";
		
		r = headerL1(expType,sep);	
				
		return r;
		
	}

	private static String headerL1(char expType, String sep) {
		
		String r = "";
		
		r = SOLUTION+sep+TRIQ+sep;
		
		if (expType=='b'){
			
			r+=BIOQ+sep+TRIQN+sep+BIOQN+sep+GRQ+sep+PEQ+sep+SPQ;
					
		}
		else{
			
			r+=GRQ+sep+PEQ+sep+SPQ;
					
		}
							
		return r;
	}
	
}