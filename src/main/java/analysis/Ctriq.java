package analysis;

import java.util.List;

import analysis.correlation.PEQ;
import analysis.correlation.SPQ;
import analysis.graphical.GRQ;

public class Ctriq implements TRIQ{
	
	private List<Solution> solutions;
	private GRQ grq;
	private PEQ peq;
	private SPQ spq;
	private double wGrq;
	private double wPeq;
	private double wSpq;
	
	
	public Ctriq(List<Solution> solutions,GRQ grq, PEQ peq, SPQ spq,double wGrq, double wPeq, double wSpq) {
		this.solutions = solutions;
		this.grq = grq;
		this.peq = peq;
		this.spq = spq;
		this.wGrq = wGrq;
		this.wPeq = wPeq;
		this.wSpq = wSpq;
	}
	
	public void computeTRIQ(){
		
		grq.computeGrq();
		peq.computePeq();
		spq.computeSpq();
		compute();
	}

	private void compute(){
		
		for (Solution sol:solutions){
												
			computeOneSolution(sol);
			
		}
	}	
	
	private void computeOneSolution(Solution sol){
		
		double triq = 0.0;
		
		double grq  = sol.getValue("grq");
		double peq  = sol.getValue("peq");
		double spq  = sol.getValue("spq");
					
		double wgrq  = grq*wGrq;
		double wpeq  = peq*wPeq;
		double wspq  = spq*wSpq;
		
//		sol.putValue("wgrq", wgrq);
//		sol.putValue("wpeq", wpeq);
//		sol.putValue("wspq", wspq);
		
		double num = wgrq+wpeq+wspq;
		
		double den = wGrq+wPeq+wSpq;
					
		triq = num/den;
		
		sol.putValue("triq", triq);
	}
	
	

}
