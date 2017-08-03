package analysis.biological;

import java.util.List;

public class GoLevels {

	

	private List<GoSlot> pa;
	private List<GoSlot> p;
	
	
	
	public GoLevels (){
		
	}
	
	/**
	 * @return the pa
	 */
	public List<GoSlot> getPa() {
		return pa;
	}
	/**
	 * @param pa the pa to set
	 */
	public void setPa(List<GoSlot> pa) {
		this.pa = pa;
	}
	/**
	 * @return the p
	 */
	public List<GoSlot> getP() {
		return p;
	}
	/**
	 * @param p the p to set
	 */
	public void setP(List<GoSlot> p) {
		this.p = p;
	}
	
	
	public List<GoSlot> getSlotsByType (String type){
		 
		
		List<GoSlot> slots = null;
		
		switch(type){
		
		case "pa":
			slots = pa;
			break;
			
		case "p":
			slots = p;
			break;
		
		}
		
		return slots;
	}
	
	
	public String report(String type){
		
		String r = "";
		
		List<GoSlot> slots = null;
		
		switch(type){
		
		case "pa":
			slots = pa;
			break;
			
		case "p":
			slots = p;
			break;
		
		}
		
		for (GoSlot s:slots){
			
			r+=s+"\n";
			
		}
				
		return r;
	}
	
	public String toString(){
		
		return "pa = "+pa.size()+" , p ="+p.size();
		
	}
	
	
	
}
