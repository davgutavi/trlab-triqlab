package analysis.graphical;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import analysis.Solution;

public class GRQ {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(GRQ.class);
	
	//private static final double HIGHEST_LSL_MULTI = 2.0*Math.PI;
	
	private GRQevaluator method;
	private List<Solution> solutions;
	
	public GRQ (GRQevaluator method, List<Solution> solutions){
		this.method = method;
		this.solutions = solutions;
	}

	public void setMethod(MslEvaluator method) {
		this.method = method;
	}

	public void setSolutions(List<Solution> solutions) {
		this.solutions = solutions;
	}
	
	public void computeGrq (){
				
		
		for (Solution sol:solutions){
				
//			LOG.debug("GRQ -> "+sol.getName()+"\n");
			computeOneSolution(sol);
			
		}
				
	}
	
	private void computeOneSolution(Solution sol){

		double multi = method.computeFitness(sol.getTricluster());
		
		double nmulti = multi/method.getHighestValue();
						
		double grq = 1.0 - nmulti;
		
		sol.putValue("grq", grq);
			
	}
		
}