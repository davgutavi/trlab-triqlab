package analysis.biological;

import java.util.Comparator;

public class PAtermOrder implements Comparator<GoTerm> {

	@Override
	public int compare(GoTerm o1, GoTerm o2) {
		
		int r = 0;
		
		double pa1 = o1.getpAdjusted();
		
		double pa2 = o2.getpAdjusted();
			
		r = Double.compare(pa1, pa2);
		
		if (r==0){
			
			double p1 = o1.getP();
			
			double p2 = o2.getP();
				
			r = Double.compare(p1, p2);
			
			if (r==0){
				
				int ps1 = o1.getPs();
				
				int ps2 = o2.getPs();
					
				r = Integer.compare(ps1, ps2);
								
			}
					
		}
				
		return r;
	
	}

}
