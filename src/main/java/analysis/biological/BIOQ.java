package analysis.biological;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import analysis.Solution;
import general.Tricluster;
import input.datasets.Biological;
import input.laboratory.Options;
import labutils.Conversions;

public class BIOQ {
		
//	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(BIOQ.class);
	
	private List<Solution> solutions;
	private Biological resources;
	private Options options;
		
	public BIOQ(List<Solution> solutions, Biological resources,Options options){
		
		this.solutions = solutions;
		this.resources = resources;
		this.options = options;
		
	
	}
		
	public void computeBioq () throws IOException, InterruptedException{
		
		List<Tricluster> triclusters = Conversions.fromSolutionsToTriclusters(solutions);
		
		BiologicalAnalysis goApp =  new BiologicalAnalysis(resources, options);
		
		List<GoStudy> studies = goApp.getAnalysis(triclusters, options.isGoResources());
		
		GoSignificance significance = GoSignificance.getInstance();
		
		significance.setConfiguration(options.getThreshold(), options.getBase(), options.getStep(),options.getDifference(), options.getBase());
		
		for (Solution sol:solutions){
						
			LOG.debug("BIOQ -> "+sol.getName());
			
			computeOneSolution(sol,studies,significance);
			
		}
				
	}
	
	private void computeOneSolution(Solution sol, List<GoStudy> studies, GoSignificance significance){
		
		GoStudy goStudy = studies.get(sol.getIndex()-1);
						
		GoLevels goLevels = significance.getGoLevels(goStudy);
		
		sol.setGoLevels(goLevels);
		
		sol.setGoStudy(goStudy);
		
		double [] sig       = significance.computeAllGoSig(goLevels);
		
		//CÁLCULO DE SIGNIFICANCIA EN BASE A P-VALUE AJUSTADO (BIOQ)
		double 	  maxpasig  = sig[1];
		
		double pasig    = sig[0];
		
		double npasig   = pasig/maxpasig;
		
		sol.putValue("bioq", npasig);
		
		//CÁLCULO DE SIGNIFICANCIA EN BASE A P-VALUE NORMAL (BIOQN)
		double 	  maxpsig  = sig[3];
						
		double psig    = sig[2];
				
		double npsig   = psig/maxpsig;
				
		sol.putValue("bioqn", npsig);
				
	}

	
}