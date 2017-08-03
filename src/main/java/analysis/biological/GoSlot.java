package analysis.biological;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GoSlot {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(GoSlot.class);
	
	private int    totalTerms;

	private int    count;
	
	private double concentration;
	
	private double partialScore;
	
	private double minExponent;
	
	private boolean haveBonus;
	
	
	public GoSlot(int totalTerms) {

		this.totalTerms = totalTerms;

		count = 0;

		concentration = 0.0;

	}

	public void countAndSetConcentration(double value) {
	
			count++;
			
			double auxNum = count;

			double auxDen = totalTerms;
					
			concentration = auxNum/auxDen;
	}
	
	
	
	
	
	
	
	

	/**
	 * @return the haveBonus
	 */
	public boolean haveBonus() {
		return haveBonus;
	}

	/**
	 * @param haveBonus the haveBonus to set
	 */
	public void setHaveBonus(boolean haveBonus) {
		this.haveBonus = haveBonus;
	}

	/**
	 * @return the exponent
	 */
	public double getMinExponent() {
		return minExponent;
	}

	/**
	 * @param exponent the exponent to set
	 */
	public void setMinExponent(double exponent) {
		
		if (exponent<minExponent)
			minExponent = exponent;
		
	}

	public int getCount() {
		return count;
	}

	public double getPartialScore() {
		return partialScore;
	}

	public void setPartialScore(double partialScore) {
		this.partialScore = partialScore;
	}
	
	public void addBonus(double bonus){
		
		this.partialScore += bonus;
	}
	
	
	public double getConcentration() {
		return concentration;
	}
	

	public String toString() {

		String r = "";

	
		return r;

	}
	
	
	public String getReport(String sep) {
	
		String r = "";
		
		
		r = count+sep+concentration+sep+partialScore;
		
		
		return r;
	
	
	}
	
	

//	public String getReport(String sep, char decimalSeparator, String decimalPattern) {
//
//		String r = "";
//
//		DecimalFormat fr = TextUtilities.getDecimalFormat(decimalSeparator,decimalPattern);
//
//		String lv = level+sep;
//		
//		String we = levelWeight+sep;
//		String sp = fr.format(sup)+sep;
//		String in = fr.format(inf)+sep;
//		String co = count+sep;
//		String cn = concentration+sep;
//		String sc = fr.format(partialScore);
//		
//		r = lv+we+sp+in+co+cn+sc;
//
//		return r;
//
//	}


	
	
	

}
