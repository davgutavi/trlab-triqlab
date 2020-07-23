package analysis.correlation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import analysis.Solution;

public class PEQ {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(PEQ.class);
	
	private static final String TYPE = "pearson";
	
	private static final String CONFIG = "g-c-t";
		
	private CorrelationAnalysis correlation;
	
	private List<Solution> solutions;
	

	
	public PEQ(CorrelationAnalysis correlation, List<Solution> solutions) {
		
		this.correlation = correlation;
		this.solutions = solutions;
		
	}
	
	
	public void computePeq(){
	
		for (Solution sol:solutions){
			
//			LOG.debug("PEQ -> "+sol.getName()+"\n");
			
			computeOneSolution(sol);
			
		}
			
	}


	private void computeOneSolution(Solution sol) {
				
		//GCT
		double peq = computeMember(sol,CONFIG);
			
		sol.putValue("peq",peq);
		
	}
	
	
	private double computeMember(Solution sol, String config){
		
		double co = 0.0;
		
		co = correlation.getIndex(sol.getTricluster(), TYPE, config);
		
		return co;
			
	}
	
	
}
