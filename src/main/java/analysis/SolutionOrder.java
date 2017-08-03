package analysis;

import java.util.Comparator;

public class SolutionOrder implements Comparator<Solution>{
	
	private String parameter;
	
	//l-g, g-l
	private String order;
	
	public SolutionOrder(String parameter, String order){
		
		this.parameter = parameter;
		this.order = order;
		
	}

	@Override
	public int compare(Solution o1, Solution o2) {
		
		double v1 = o1.getValue(parameter);
		double v2 = o2.getValue(parameter);
		
		int r = Double.compare(v1, v2);
		
		if (order.equalsIgnoreCase("g-l"))
			r = r*(-1);	
		
		return r;
	}
	
	
	

}