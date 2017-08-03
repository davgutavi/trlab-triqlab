package analysis.graphical;

import general.Tricluster;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.TextUtilities;


public class OriginalTransformationSupport implements Transformation {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(OriginalTransformationSupport.class);
	
	//config = x-o-p (g-c/s-t, g-t-c/s, c/s-g-t, c/s-t-g, t-g-c/s, t-c/s-g)
	
	//array = [o,x,p]
	//**
	private static final String C1 = "g-c-t"; //************************0, array = [c,g,t] 
	//**
	private static final String C2 = "g-t-c"; //************************1, array = [t,g,c]
	private static final String C3 = "c-g-t"; //2, array = [g,c,t]
	private static final String C4 = "c-t-g"; //3, array = [t,c,g]
	//**
	private static final String C5 = "t-g-c"; //************************4, array = [g,t,c]
	private static final String C6 = "t-c-g"; //5, array = [c,t,g]	
		
	private double [][][] dataset;
	
	public OriginalTransformationSupport(double[][][] dataset) {
		this.dataset = dataset;
	}


	public List<double [][][]> transform (Tricluster tricluster){
				
		List<double [][][]> r = new ArrayList<double [][][]> (6); 
		
		double [][][] t1 = getCube (tricluster,C1);//0
		double [][][] t2 = getCube (tricluster,C2);//1
		double [][][] t3 = getCube (tricluster,C3);
		double [][][] t4 = getCube (tricluster,C4);
		double [][][] t5 = getCube (tricluster,C5);//4
		double [][][] t6 = getCube (tricluster,C6);
		
		r.add(t1);
		r.add(t2);
		r.add(t3);
		r.add(t4);
		r.add(t5);
		r.add(t6);
		
//		LOG.debug("\n"+TextUtilities.cubeOfDoubleToString(t1,'.',"#.#####"));
				
		return r;
				
	}
	
	
	private double [][][] getCube (Tricluster tricluster,String configuration){
		
		double[][][] r = null;
		
		List<Integer>[] cfg = getXaxisOutputPanel(tricluster,configuration);
		
		List<Integer> xaxis  = cfg[0];
		List<Integer> output = cfg[1];
		List<Integer> panel  = cfg[2];
		
		r = new double [output.size()][xaxis.size()][panel.size()];
		
//		LOG.debug(tricluster.toString());
//		LOG.debug("["+output.size()+","+xaxis.size()+","+panel.size()+"]");
		
		int i = 0;
		int j = 0;
		int k = 0;
		
		for(Integer pan:panel){
			
			i = 0;
			
			for(Integer out:output){
												
				j = 0;
				
				for (Integer x:xaxis){
															
					r[i][j][k] = getValue(x.intValue(),out.intValue(),pan.intValue(),configuration);
					
//					LOG.debug(i+","+j+","+k+" = "+r[i][j][k]);
					
					j++;
					
				}
				
				i++;
				
			}
			
			k++;
					
		}
		
		return r;
		
	}
	
	


private double getValue(int x,int o, int p, String cnf){
	
	double value = 0.0;
	
	List<String> config = TextUtilities.splitElements(cnf, "-");
	
	String xaxis = config.get(0);
	String output = config.get(1);
	String panel = config.get(2);
	
	int gi = 0,ci = 0,ti = 0;
	
	switch(xaxis){
	
	case "g":
		gi = x;
		break;
		
	case "c": case "s":
		ci = x;
		break;
		
	case "t":
		ti = x;
		break;
	
	}
	
	switch(output){
	
	case "g":
		gi = o;
		break;
		
	case "c": case "s":
		ci = o;
		break;
		
	case "t":
		ti = o;
		break;
	
	}

	switch(panel){
	
	case "g":
		gi = p;
		break;
		
	case "c": case "s":
		ci = p;
		break;
		
	case "t":
		ti = p;
		break;
	
	}	

	//LOG.debug("[ "+gi+","+ci+","+ti+" ]");
	
	value = dataset[gi][ci][ti];
	
	//LOG.debug("[ "+gi+","+ci+","+ti+" ] = "+value);
		
	return value;
}

private List<Integer>[] getXaxisOutputPanel (Tricluster tricluster,String configuration){
	
	@SuppressWarnings("unchecked")
	List<Integer>[] r = new List[3];

	List<String> config = TextUtilities.splitElements(configuration, "-");
	
	String xaxis = config.get(0);
	String output = config.get(1);
	String panel = config.get(2);
	
	switch(xaxis){
	
	case "g":
		r[0] = tricluster.getGenes();
		break;
		
	case "c": case "s":
		r[0] = tricluster.getSamples();
		break;
		
	case "t":
		r[0] = tricluster.getTimes();
		break;
	
	}
	
	switch(output){
	
	case "g":
		r[1] = tricluster.getGenes();
		break;
		
	case "c": case "s":
		r[1] = tricluster.getSamples();
		break;
		
	case "t":
		r[1] = tricluster.getTimes();
		break;
	
	}

	switch(panel){
	
	case "g":
		r[2] = tricluster.getGenes();
		break;
		
	case "c": case "s":
		r[2] = tricluster.getSamples();
		break;
		
	case "t":
		r[2] = tricluster.getTimes();
		break;
	
	}	
	
	return r;
			
}
	

}
