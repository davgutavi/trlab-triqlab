package analysis;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import analysis.biological.BIOQ;
import analysis.correlation.PEQ;
import analysis.correlation.SPQ;
import analysis.graphical.GRQ;

public class Btriq implements TRIQ{
	
//	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(Btriq.class);
	
	
	private List<Solution> solutions;
	private GRQ grq;
	private PEQ peq;
	private SPQ spq;
	private BIOQ bioq;
	private double wGrq;
	private double wPeq;
	private double wSpq;
	private double wBioq;
	
	
	public Btriq(List<Solution> solutions, GRQ grq, PEQ peq, SPQ spq, BIOQ bioq,double wGrq, double wPeq, double wSpq, double wBioq) {
		
		this.solutions = solutions;
		this.grq = grq;
		this.peq = peq;
		this.spq = spq;
		this.bioq = bioq;
		this.wGrq = wGrq;
		this.wPeq = wPeq;
		this.wSpq = wSpq;
		this.wBioq = wBioq;
		
	}
	
	public void computeTRIQ() throws IOException, InterruptedException{
		
		LOG.debug("Computing GRQ");
		grq.computeGrq();
		LOG.debug("Computing PEQ");
		peq.computePeq();
		LOG.debug("Computing SPQ");
		spq.computeSpq();
		LOG.debug("Computing BIOQ");
		bioq.computeBioq();
		compute();
		
		
	}
	
	private void compute(){
		
		for (Solution sol:solutions){
						
			computeOneSolution(sol);
			
		}
	}	
	
	private void computeOneSolution(Solution sol){
		
		double triq = 0.0;
		
		//triq no ajustado
		double triqn = 0.0;
		
		double grq  = sol.getValue("grq");
		double peq  = sol.getValue("peq");
		double spq  = sol.getValue("spq");
		double bioq  = sol.getValue("bioq");
		
		//bioq no ajustado
		double bioqn  = sol.getValue("bioqn");
		
					
		double wgrq  = grq*wGrq;
		double wpeq  = peq*wPeq;
		double wspq  = spq*wSpq;
		double wbioq = bioq*wBioq;
		
		//wbioq no ajustado
		double wbioqn = bioqn*wBioq;
		
//		sol.putValue("wgrq", wgrq);
//		sol.putValue("wpeq", wpeq);
//		sol.putValue("wspq", wspq);
//		sol.putValue("wbioq", wbioq);
			
		double num = wgrq+wpeq+wspq+wbioq;
		
		//numerador no ajustado
		double numn = wgrq+wpeq+wspq+wbioqn;
			
		double den = wGrq+wPeq+wSpq+wBioq;
		
		//denominador no ajustado
		double denn = wGrq+wPeq+wSpq+wBioq;
						
		triq = num/den;
		
		//triq no ajustado
		triqn = numn/denn;
		
		sol.putValue("triq", triq);
		
		sol.putValue("triqn", triqn);
	
	}
	
}