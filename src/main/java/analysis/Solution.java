package analysis;

import general.Tricluster;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import analysis.biological.GoLevels;
import analysis.biological.GoSlot;
import analysis.biological.GoStudy;

public class Solution implements Comparable<Solution>{
	
	private int index;
	
	private String name;
	
	private Tricluster tricluster;
	
	private Map<String,Double> variables;
	
	private GoLevels goLevels;
	
	private GoStudy goStudy;

	public Solution(int index, Tricluster tricluster) {
		this.index = index;
		this.tricluster = tricluster;
		this.variables = new HashMap<String, Double>();
		intializeVariables();
		this.name = "TRI_{"+this.index+"}";
	}

	private void intializeVariables() {
		
		double init = -1.0;
		
		variables.put("grq",        new Double (init));		

		variables.put("peq"        ,new Double (init));		

		variables.put("spq"        ,new Double (init));
	
		variables.put("bioq", new Double (init));		
		
		//BIOQN
		variables.put("bioqn", new Double (init));
		
		variables.put("triqn", new Double (init));

		variables.put("triq", new Double (init));
				
	}

	public int getIndex() {
		return index;
	}

	public Tricluster getTricluster() {
		return tricluster;
	}
	
	public String getName() {
		return name;
	}

	public double getValue (String variable){
		return (variables.get(variable)).doubleValue();
	}
	
	public double putValue (String variable, double value){
		return variables.put(variable,new Double (value));
	}
	
	public void setGoLevels(GoLevels goLevels) {
		this.goLevels = goLevels;
	}
	
	

	public List<GoSlot> getGoSlots (String type){
		
		List<GoSlot> slots = null;
		
		switch(type){
		
		case "pa":
			slots = goLevels.getPa();
			break;
			
		case "p":
			slots = goLevels.getP();
			break;
		
		}
		
		return slots;
		
	}

	public int compareTo(Solution o) {
		
		int i1 = this.index;
		int i2 = o.getIndex();
		
		return Integer.compare(i1, i2);
	}

	public GoStudy getGoStudy() {
		return goStudy;
	}

	public void setGoStudy(GoStudy goStudy) {
		this.goStudy = goStudy;
	}
	
}