package analysis.biological;

import java.text.Format;

import utils.TextUtilities;


public  class GoInterval implements Comparable<GoInterval>{
	
	private int    level;
	private double levelWeight;
	private double inf;
	private double sup;
	
	public GoInterval(int level, double levelWeight, double inf, double sup) {
		this.level = level;
		this.levelWeight = levelWeight;
		this.inf = inf;
		this.sup = sup;
	}
	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}
	/**
	 * @return the levelWeight
	 */
	public double getLevelWeight() {
		return levelWeight;
	}
	/**
	 * @return the inf
	 */
	public double getInf() {
		return inf;
	}
	/**
	 * @return the sup
	 */
	public double getSup() {
		return sup;
	}
	
	public boolean isIntoInterval(double value) {

		return (value <= sup) && (value > inf);

	}
	
	public String toString(){
		
		Format f = TextUtilities.getDecimalFormat('.',"0.0E00" );
		
		return level+"    "+levelWeight+"    "+"("+f.format(inf)+","+f.format(sup)+"]";
	
	
	}
	
	public String getReportString (String sep){
		
		Format f = TextUtilities.getDecimalFormat('.',"0.0E00" );
			
		return 	level+sep+levelWeight+sep+"("+f.format(inf)+","+f.format(sup)+"]"+sep;
		
	}
	
	
	@Override
	public int compareTo(GoInterval o) {
		
		double i1 = this.getInf();
		
		double i2 = o.getInf();
					
		return Double.compare(i1,i2);
	}
			
}
