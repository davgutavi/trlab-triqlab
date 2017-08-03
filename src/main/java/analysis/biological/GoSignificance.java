package analysis.biological;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GoSignificance {

	private static final Logger LOG = LoggerFactory.getLogger(GoSignificance.class);

	static private GoSignificance singleton = new GoSignificance();
	
	private double THRESHOLD = 1e-40;

	private double BASE      = 10.0;

	private double STEP      = 1.0;
	
	
	private double BONUS     = 0.0;
	
	private double DIF       = 10.0;
	
	private List<GoInterval> INTERVALS;
	
	private GoSignificance() {
		
//		setConfiguration (1e-40, 10.0,  1.0, 15.0);
				
		buildIntervals();
		
		//LOG.debug("\n"+toString());
		
	}
	
	public static GoSignificance getInstance() {return singleton;}
	
	public  void setConfiguration (double threshold, double base, double step, double dif, double bonus){
		THRESHOLD = threshold;
		BASE	  = base;
		STEP      = step;
		DIF       = dif;
		BONUS     = bonus;
	}
	
	
	public String toString(){
		
		String r = "";
		
		for(GoInterval in:INTERVALS){
			
			r+=in+"\n";		
			
		}
				
		return r;
	}
	
	
	
	/**
	 * @return the tHRESHOLD
	 */
	public  double getTHRESHOLD() {
		return THRESHOLD;
	}

	/**
	 * @return the bASE
	 */
	public  double getBASE() {
		return BASE;
	}

	/**
	 * @return the sTEP
	 */
	public  double getSTEP() {
		return STEP;
	}

	/**
	 * @return the dIF
	 */
	public  double getDIF() {
		return DIF;
	}
	
	/**
	 * @return the bONUS
	 */
	public double getBONUS() {
		return BONUS;
	}

	/**
	 * @return the iNTERVALS
	 */
	public  List<GoInterval> getINTERVALS() {
		return INTERVALS;
	}

	private  void buildIntervals() {

		INTERVALS = new ArrayList<GoInterval>();
		
		int level = 1;

		double sup = 1.0;

		double inf = STEP / (Math.pow(BASE, level));

		double levelWeight = 1.0;
		
		while (inf >= THRESHOLD) {

			INTERVALS.add(new GoInterval(level,levelWeight,inf,sup));
			
			level++;

			sup = inf;

			inf = STEP / (Math.pow(BASE, level));
			
			levelWeight = levelWeight + DIF;

		}

		sup = STEP / (Math.pow(BASE, level - 1));
		
		inf = 0.0;

		INTERVALS.add(new GoInterval(level,levelWeight,inf,sup));
		
		Collections.sort(INTERVALS);

	}
	
	
	//#############################################################BUILD SLOTS

	public  GoLevels getGoLevels(GoStudy goStudy) {

		GoLevels goLevels = new GoLevels();

		buildGoLevels(goStudy.getGoTerms(), goLevels);
		
		//LOG.debug(""+goLevels);

		return goLevels;

	}

	private void buildGoLevels(List<GoTerm> goTerms, GoLevels goLevels) {

		List<GoSlot> paSlots = new ArrayList<GoSlot>(INTERVALS.size());
		
		for (int i = 0; i < INTERVALS.size(); i++) {
			paSlots.add(new GoSlot (goTerms.size()));
		}

		fillLevels(paSlots, goTerms, "pa");

		//LOG.debug("pa slots = "+paSlots.size());
		
		goLevels.setPa(paSlots);

		List<GoSlot> pSlots = new ArrayList<GoSlot>(INTERVALS.size());

		for (int i = 0; i < INTERVALS.size(); i++) {
			pSlots.add(new GoSlot (goTerms.size()));
		}
				
		fillLevels(pSlots, goTerms, "p");

		goLevels.setP(pSlots);

	}

	private  void fillLevels(List<GoSlot> slots, List<GoTerm> goTerms, String type) {

		switch (type) {

		case "pa":

			Collections.sort(goTerms, new PAtermOrder());
			fill(slots, goTerms, "pa");
			break;

		case "p":

			Collections.sort(goTerms, new PtermOrder());
			fill(slots, goTerms, "p");
			break;

		}

	}

	private  void fill(List<GoSlot> slots, List<GoTerm> terms, String type) {

		int index = 0;

		for (GoTerm item : terms) {

			double value = 0.0;

			int indexOfItem = terms.indexOf(item);
			
			switch (type) {

			case "pa":

				value = item.getpAdjusted();
				break;

			case "p":

				value = item.getP();
				break;

			}

			boolean enc = false;

			int i = index;

			while (i<slots.size()&&!enc) {
			
				GoSlot slot =  slots.get(i);
				
				GoInterval interval = INTERVALS.get(i);
				
				enc = interval.isIntoInterval(value);
				
				if (enc) {
					
					index = i;
					
					slot.countAndSetConcentration(value);
					
					double exponent = Math.getExponent(value);
					
					slot.setMinExponent(exponent);
										
					if (indexOfItem==0)
						slot.setHaveBonus(true);				
					
				} 
				else {
					
					i++;
					
				}

			}
			
			if (!enc)
				LOG.info("value = "+value+" ignored");
			
			
			
		}

	}

	//#############################################################BUILD SLOTS
		
	//#############################################################COMPUTE SIGNIFICANCE
	
	public  double [] computeAllGoSig (GoLevels significances){
		
		//r[0]: pa score, r[1]: pa max score, r[2]: p score, r[3]: p max score
		
		 double[] r = new double[4];
		 
		 double[] pa = computeGoSig(significances.getPa());
		 
		 double[] p = computeGoSig(significances.getP());
			
		 r[0] = pa[0];
		 r[1] = pa[1];
		 r[2] = p[0];
		 r[3] = p[1];
		 
		 return r;
	}
	
	public  double[] computeGoSig(List<GoSlot> slots) {

		//Collections.reverse(INTERVALS);
		
		double[] r = new double[2];

		for (int i = 0;i<INTERVALS.size();i++) {
			
			GoSlot slot = slots.get(i);
		
			GoInterval interval = INTERVALS.get(i);

			//double partial = function1(slot.getConcentration()) * function2(interval.getLevel(),interval.getLevelWeight());
			
			//double partial = slot.getConcentration() * interval.getLevelWeight();
			
			double partial = slot.getConcentration() * interval.getLevelWeight() * interval.getLevel();
			
			double bonus = 0.0;
			
			if (slot.haveBonus()){
			
				//NOW:
				
				bonus = bonusFunction(interval.getLevel(),slot.getMinExponent(),BONUS);
				
				//BEFORE:
				
				//bonus = interval.getLevel()+BONUS;
			
			}
			
			partial += bonus;
			
			
			slot.setPartialScore(partial);
			
			//LOG.debug("Sol "+(i+1));
			
			
			
			r[0] += partial;
			

		}

		int    maxLevel = (INTERVALS.get(0)).getLevel();
		
		double maxScore = (INTERVALS.get(0)).getLevelWeight();
			
		double maxPercentage = 1.0;
		
		double higherExponent = -41.0;
		
		double maxValue = (function1(maxPercentage)*function2(maxLevel,maxScore)) + bonusFunction(maxLevel,higherExponent,BONUS);

//		
//		LOG.debug("r[0] = "+r[0]);
//		
		
		
		r[1] = maxValue;
		
	//	LOG.debug("Max PASIG = "+r[1]);

		return r;

	}
	
	
	private  double bonusFunction (int level, double exponent, double bonus){
		
		//double vabs = Math.abs(exponent);
				
		//double r = level+(bonus*vabs);
		
		double r = level+bonus;
		
		//double r = bonus*vabs;
		
		
		//LOG.debug("level = "+level+" exponent = "+exponent+" bonus = "+bonus);
		
		return r;
		
	}
	
	
	private  double function1 (double percentage){
		
		//double r = Math.exp(percentage);
		
		double r = percentage;
		
		return r;
		
	}
	
	private  double function2(int level, double score) {
		
		//double x = level;
		
		double x = level*score;

		//double f = Math.pow(10,x);

		double f = x;
		
		//double f = Math.exp(x);

		double r = f;

		return r;

	}


	

	

	

}
