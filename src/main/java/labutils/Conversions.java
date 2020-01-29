package labutils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import analysis.Solution;
import general.Tricluster;

public class Conversions {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(Conversions.class);

	
	public static double[] fromListOfDoubleToArray (List<Double> list){
		
		double [] r = new double[list.size()];
		
		int index = 0;
		
		for (Double number:list){
			
			r[index] = number.doubleValue();
			
			index++;
		}
				
		return r;
		
	}
	
	public static int[] fromListOfIntegerToArray (List<Integer> list){
		
		int [] r = new int[list.size()];
		
		int index = 0;
		
		for (Integer number:list){
			
			r[index] = number.intValue();
			
			index++;
		}
				
		return r;
		
	}
	
	public static String fromArrayOfStringToString (String[] array, String sep){
		
		String r = "";
			
		for (int i = 0; i<array.length;i++){
					
			if (i==array.length-1){
				r+=array[i];
			}
			else{
				r+=array[i]+sep;
			}
		
		}
				
		return r; 
		
	}
	
	public static List<Tricluster> fromSolutionsToTriclusters (List<Solution> solutions) {
		
		List<Tricluster> r = new ArrayList<Tricluster>(solutions.size());
		
		for (Solution s: solutions){
			
			r.add(s.getTricluster());
		
		}
				
		return r;
	}
	
	
	public static double [] fromListIntegerToArrayDouble (Collection<Integer> list){
				
		double [] r =  new double[list.size()]; 
		
		int i = 0;
		
		for (Integer n: list){
		
//			LOG.debug(n.doubleValue()+"");
			
			r[i] = n.doubleValue();
			
			i++;
			
		}	
		
		return r;
		
	}
	
	
}
