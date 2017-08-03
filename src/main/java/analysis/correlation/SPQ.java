package analysis.correlation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import analysis.Solution;

public class SPQ {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(SPQ.class);
	
	private static final String TYPE = "spearman";
	
	private static final String CONFIG = "g-c-t";
		
	private CorrelationAnalysis correlation;
	
	private List<Solution> solutions;

	
	public SPQ(CorrelationAnalysis correlation, List<Solution> solutions) {
		this.correlation = correlation;
		this.solutions = solutions;
	}
	
	
	public void computeSpq(){
	
		for (Solution sol:solutions){
								
			computeOneSolution(sol);
			
		}
			
	}


	private void computeOneSolution(Solution sol) {
	
		double spq = computeMember(sol, CONFIG);
		
		sol.putValue("spq",spq);
		
	}
	
	
	private double computeMember(Solution sol, String config){
		
		double co = 0.0;
		
		co = correlation.getIndex(sol.getTricluster(), TYPE, config);
					
		return co;
			
	}
	
	
}
