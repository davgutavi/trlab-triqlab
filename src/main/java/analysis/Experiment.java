package analysis;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import input.datasets.Common;


public class Experiment  {
	
//	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(Experiment.class);
	
	private Common resources;
	
	private List<Solution> solutions;
	
	
	// b = biological
	// c = common
	private char analysisType;
	
	private String experimentName;
	
	private TRIQ triqAnalysis;
	
	private Map<String,Object> expVariables;

	public Experiment(List<Solution> solutions, char type, String experimentName, Common resources,TRIQ triqAnalysis) {
		
//		LOG.debug("constructor");
		
		this.solutions = solutions;
		this.resources = resources;
		this.analysisType = type;
		this.experimentName = experimentName;
		this.triqAnalysis = triqAnalysis;
		
		this.expVariables = new HashMap<String, Object>();
		intializeVariables();
	}
	
	private void intializeVariables() {

		double init = -1.0;
		
		expVariables.put("bestsolution", "");
		expVariables.put("besttriq",new Double(init));
		expVariables.put("mean",new Double(init));
		expVariables.put("stdev",new Double(init));
		
		expVariables.put("bestsolutionn","");
		expVariables.put("besttriqn",new Double(init));
		expVariables.put("meann",new Double(init));
		expVariables.put("stdevn",new Double(init));
		
		
	}





	public char getAnalysisType() {
		return analysisType;
	}

	public String getExperimentName() {
		return experimentName;
	}

	public List<Solution> getSolutions() {
		return solutions;
	}

	public String getDatasetName (){
		
		return resources.getDatasetName();
		
	}
	
	public Common getDataset (){
		return resources;
	}
	
	
	public Object getValue (String variable){
		
		return expVariables.get(variable);
		
	}
	
	
	
	
	
	public void computeAnalysis () throws IOException, InterruptedException{
		
		LOG.debug("Solution level");
		
		computeAnalysisSolutionLevel();
		
		computeAnalysisExperimentLevel();
	}
	

	public void computeAnalysisSolutionLevel() throws IOException, InterruptedException{

		triqAnalysis.computeTRIQ();		
	
	}
	
	

	public void computeAnalysisExperimentLevel(){
		
		//BEST SOLUTION
		
		Collections.sort(solutions,new SolutionOrder("triq","g-l"));
		
		expVariables.put("bestsolution", solutions.get(0));
		
		//BEST TRIQ
		
		double value = (solutions.get(0)).getValue("triq");
		
		expVariables.put("besttriq", new Double(value));
		
				
		//SUMMARY
		
		double [] triqs = getAllValues("triq");
		
		//MEAN TRIQ
				
		double mean = StatUtils.mean(triqs);	
				
		expVariables.put("mean", new Double(mean));
		
		//STDEV TRIQ

		StandardDeviation sd = new StandardDeviation();
		
		double stddev = sd.evaluate(triqs);
		
		expVariables.put("stdev", new Double(stddev));
		
		
		
		//****************************************************************************************
		
		if (analysisType=='b') {
		
		//BEST SOLUTION N
		
		Collections.sort(solutions,new SolutionOrder("triqn","g-l"));
				
		expVariables.put("bestsolutionn", solutions.get(0));
		
		//BEST TRIQN
		
		double valuen = (solutions.get(0)).getValue("triqn");
				
		expVariables.put("besttriqn", new Double(valuen));
		
		//SUMMARY
		
		double [] triqsn = getAllValues("triqn");
				
		//MEAN TRIQ
						
		double meann = StatUtils.mean(triqsn);	
						
		expVariables.put("meann", new Double(meann));
				
		//STDEV TRIQ

		StandardDeviation sdn = new StandardDeviation();
				
		double stddevn = sdn.evaluate(triqsn);
				
		expVariables.put("stdevn", new Double(stddevn));
		
		}		
		
		//****************************************************************************************
		
		
		Collections.sort(solutions);
		
		
	}
	

	
	private double [] getAllValues (String variable){
		
		double [] r = new double [solutions.size()];
		
		int i = 0;
		
		for(Solution sol:solutions){
			
			r[i] = sol.getValue(variable);
					
			i++;
			
		}
				
		return r;
	}
	
	
}