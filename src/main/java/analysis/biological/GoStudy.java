package analysis.biological;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GoStudy {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(GoStudy.class);
	
	private  List<GoTerm> goTerms;
	
	private int popTotal;
	
	private int studyTotal;
	
	private String studyName;
	
	public GoStudy(String studyName){
		
		this.studyName = studyName;
		
		goTerms = new ArrayList<GoTerm>();
			
	}
	
	public void addGoTerm (GoTerm goterm){
		
		goTerms.add(goterm);
		
	}
		
	/**
	 * @return the goTerms
	 */
	public List<GoTerm> getGoTerms() {
		return goTerms;
	}

	/**
	 * @return the popTotal
	 */
	public int getPopTotal() {
		return popTotal;
	}

	/**
	 * @param popTotal the popTotal to set
	 */
	public void setPopTotal(int popTotal) {
		this.popTotal = popTotal;
	}

	/**
	 * @return the studyTotal
	 */
	public int getStudyTotal() {
		return studyTotal;
	}

	/**
	 * @param studyTotal the studyTotal to set
	 */
	public void setStudyTotal(int studyTotal) {
		this.studyTotal = studyTotal;
	}

	/**
	 * @return the studyName
	 */
	public String getStudyName() {
		return studyName;
	}
			
	@Override
	public String toString() {
		
		//Collections.sort(goTerms, new PadjustedComparator());
			
		String r = "GO study "+studyName+" :\n"
				+ "pop total=" + popTotal+"\n"
				+ "study total=" + studyTotal+"\n"+
				"Go terms ("+goTerms.size()+") :\n";
		
		String terms ="";
	
		for (GoTerm t:goTerms){
			
			terms+=t.toString()+"\n";
			
		}
				
		return r+terms;
			
	}
	
	public double[] getVariable (String type){
		
		double[] r = new double[goTerms.size()];
		
		int i = 0;
		
		for (GoTerm go:goTerms){
			
			switch (type){
			
				case "pa":
					
					r[i] = go.getpAdjusted();
					break;
				
				case "p":
					
					r[i] = go.getP();
					break;
				
				case "ps":
					
					r[i] = go.getPs();
					break;
			
			}
			
			i++;
			
		}
				
		return r;
		
	}
	
	public double[] getOrderedVariable(String type){
		
		double[] r = new double[goTerms.size()];
		
		int i = 0;
		
		for (GoTerm go:goTerms){
			
			switch (type){
			
				case "pa":
					
					r[i] = go.getpAdjusted();
					break;
				
				case "p":
					
					r[i] = go.getP();
					break;
			
			}
			
			i++;
			
		}
		
		Arrays.sort(r);
		
		return r;
			
	}
	
}