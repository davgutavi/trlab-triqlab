package analysis.correlation;

import general.Tricluster;

import java.util.List;

import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.TextUtilities;




public class CorrelationAnalysis {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(CorrelationAnalysis.class);
	
	//ideal
	private static final String CORR_1 = "g-c-t";
	private static final String CORR_2 = "g-t-c";
	private static final String CORR_3 = "c-g-t";
	//papers
	private static final String CORR_4 = "c-t-g";
	private static final String CORR_5 = "t-g-c";
	private static final String CORR_6 = "t-c-g";
	
	private double[][][] dataset;
	
	public CorrelationAnalysis (double[][][] dataset){		
		
		this.dataset = dataset;
		
	}
	
	public double[][] getAllIndexes (Tricluster tricluster){
		
		double [][] r = new double [2][6];
		
		double[][] table1 = getVariables(tricluster,CORR_1);
		double pr1 = getCoeffMean(table1,"pearson");
		double sp1 = getCoeffMean(table1,"spearman");
		r[0][0] = pr1;
		r[1][0] = sp1;		
		
//		LOG.debug(tricluster.toString());
//		LOG.debug(TextUtilities.tableOfDoubleToString(table1));
		
		double[][] table2 = getVariables(tricluster,CORR_2);
		double pr2 = getCoeffMean(table2,"pearson");
		double sp2 = getCoeffMean(table2,"spearman");
		r[0][1] = pr2;
		r[1][1] = sp2;
		
		double[][] table3 = getVariables(tricluster,CORR_3);
		double pr3 = getCoeffMean(table3,"pearson");
		double sp3 = getCoeffMean(table3,"spearman");
		r[0][2] = pr3;
		r[1][2] = sp3;
		
		double[][] table4 = getVariables(tricluster,CORR_4);
		
//		LOG.debug("Table 4 values:");
//		LOG.debug(TextUtilities.tableOfDoubleToString(table4,"\t",'.',"#.###"));
		
		double pr4 = getCoeffMean(table4,"pearson");
		double sp4 = getCoeffMean(table4,"spearman");
		r[0][3] = pr4;
		r[1][3] = sp4;
		
		double[][] table5 = getVariables(tricluster,CORR_5);
		double pr5 = getCoeffMean(table5,"pearson");
		double sp5 = getCoeffMean(table5,"spearman");
		r[0][4] = pr5;
		r[1][4] = sp5;
		
		double[][] table6 = getVariables(tricluster,CORR_6);
		double pr6 = getCoeffMean(table6,"pearson");
		double sp6 = getCoeffMean(table6,"spearman");
		r[0][5] = pr6;
		r[1][5] = sp6;
		
		return r;
			
	}
	
	
	//config = variable-factor-vector (g-c/s-t, g-t-c/s, c/s-g-t, c/s-t-g, t-g-c/s, t-c/s-g)
		
	public double[] getIndex (Tricluster tricluster, String config){
		
		double[][] table = getVariables(tricluster,config);
		
//		LOG.debug(tricluster.toString());
//		LOG.info(TextUtilities.tableOfDoubleToString(table,'.',"#.###"));
		
		double pr = getCoeffMean(table,"pearson");
		
		double sp = getCoeffMean(table,"spearman");
		
		double[] r = new double[2];
		
		r[0] = pr;
		
		r[1] = sp;	
		
		return r;
			
	}
		
	public double getIndex (Tricluster tricluster, String coefficient, String config){
		
		double[][] table = getVariables(tricluster,config);
		
		double r = getCoeffMean(table,coefficient);
		
		return r;
			
	}
	
	private double getCoeffMean (double[][] table, String coefficient){
		
		double r = 0.0;
		
		double[][] cor = null;
		
		switch(coefficient){
		
		case "spearman":
			
			SpearmansCorrelation spearman = new SpearmansCorrelation();
			
			cor = (spearman.computeCorrelationMatrix(table)).getData();
			
			break;
			
		case "pearson":
		
			PearsonsCorrelation pearson = new PearsonsCorrelation();
			
			cor = (pearson.computeCorrelationMatrix(table)).getData();
		
			break;
		
		}
		
		
		//LOG.debug(TextUtilities.tableOfDoubleToString(cor,"\t",'.',"#.###"));
		
		double [] topDiagonal = getTopDiagonal(cor);
		
			
		double [] values = topDiagonal;
		
		//NaN proccessing
		
		//LOG.debug("NaN proccessing");
		
		for (int i = 0;i<values.length;i++){
						
			if (Double.isNaN(values[i])){
				
				values[i]= 0.0;
				
			}
		
		}
		
		
		absValues(values);
		
		//LOG.info(TextUtilities.vectorOfDoubleToString(values,15,"\t",'.',"#.###"));
		
		r = StatUtils.mean(values);
				
		return r;
		
	}
	
	
	private void absValues (double[] vector){
				
		for (int i=0;i<vector.length;i++){
			
			vector[i] = Math.abs(vector[i]);
			
		}
		
	}
	
	
	private static double[] getTopDiagonal(double[][] table) {
		
		int len = table.length;
		
		int size = ((len-1)*len)/2;
		
		double [] r = new double[size];
		
		int index = 0;
		
		int i = 0;
		
		while (i<len-1){
			
			int j = i+1;
			
			while (j<len){
				
				r[index] = table[i][j];
				
				index++;
				
				j++;
				
			}
						
			i++;
		}
		
		return r;
	}
	
	
	private double[][] getVariables (Tricluster tricluster,String configuration){
		
		double[][] r = null;
		
		List<Integer>[] cfg = getVariableFactorVector(tricluster,configuration);
		
		List<Integer> variable = cfg[0];
		List<Integer> factor   = cfg[1];
		List<Integer> vector   = cfg[2];
		
		r = new double [vector.size()][variable.size()*factor.size()];
		
		//column mayor
		
		//LOG.debug("SIZES: Vector = "+vector.size()+" Var*Fac = "+variable.size()*factor.size());
		
		//LOG.debug(tricluster.completeToString());
		
		int j = 0;
		
		int i = 0;
		
		for(Integer var:variable){
			
			for(Integer fac:factor){
												
				i = 0;
				
				for (Integer vec:vector){
															
					r[i][j] = getValue(var.intValue(),fac.intValue(),vec.intValue(),configuration);
					
					//LOG.debug(i+","+j+" = "+r[i][j]);
					
					i++;
					
				}
				
				j++;
				
			}
					
		}
				
		return r;
	}
	
	
	private double getValue(int var,int fac, int vec, String cnf){
		
		double value = 0.0;
		
		List<String> config = TextUtilities.splitElements(cnf, "-");
		
		String cvar = config.get(0);
		String cfac = config.get(1);
		String cvec = config.get(2);
		
		int gi = 0,ci = 0,ti = 0;
		
		switch(cvar){
		
		case "g":
			gi = var;
			break;
			
		case "c": case "s":
			ci = var;
			break;
			
		case "t":
			ti = var;
			break;
		
		}
		
		switch(cfac){
		
		case "g":
			gi = fac;
			break;
			
		case "c": case "s":
			ci = fac;
			break;
			
		case "t":
			ti = fac;
			break;
		
		}

		switch(cvec){
		
		case "g":
			gi = vec;
			break;
			
		case "c": case "s":
			ci = vec;
			break;
			
		case "t":
			ti = vec;
			break;
		
		}	
		
		
		
		value = dataset[gi][ci][ti];
		
		//LOG.debug("[ "+gi+","+ci+","+ti+" ] = "+value);
			
		return value;
	}
	
	
	private List<Integer>[] getVariableFactorVector (Tricluster tricluster,String configuration){
		
		@SuppressWarnings("unchecked")
		List<Integer>[] r = new List[3];

		List<String> config = TextUtilities.splitElements(configuration, "-");
		
		String cvar = config.get(0);
		String cfac = config.get(1);
		String cvec = config.get(2);
		
		switch(cvar){
		
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
		
		switch(cfac){
		
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

		switch(cvec){
		
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
	
	
	
//	public static List<double[][]> TablasVgFc(Tricluster tri) {
//		
//		List<double [][]> res = new LinkedList<double [][]> ();
//		
//		Iterator<Integer> ic = (tri.getCondiciones()).iterator(); 
//		
//		while (ic.hasNext()){
//			
//			int c = (ic.next()).intValue();
//			
//			double [][] tabla = construyeTablaVgenFcondicion (c, tri); 
//			
//			res.add(tabla);
//						
//		}
//		
//		return res;
//	}
//
//
//	public static List<double[][]> TablasVgFt(Tricluster tri) {
//		
//		List<double [][]> res = new LinkedList<double [][]> ();
//		
//		Iterator<Integer> it = (tri.getTiempos()).iterator(); 
//		
//		while (it.hasNext()){
//			
//			int t = (it.next()).intValue();
//			
//			double [][] tabla = construyeTablaVgenFtiempo (t, tri); 
//			
//			res.add(tabla);
//						
//		}
//		
//		return res;
//		
//	}
//	
//	public static List<double[][]> TablasVcFt(Tricluster tri) {
//
//		List<double [][]> res = new LinkedList<double [][]> ();
//		
//		Iterator<Integer> it = (tri.getTiempos()).iterator(); 
//		
//		while (it.hasNext()){
//			
//			int t = (it.next()).intValue();
//			
//			double [][] tabla = construyeTablaVcondicionFtiempo (t, tri); 
//			
//			res.add(tabla);
//						
//		}
//		
//		return res;
//	}
//
//	public static List<double[][]> TablasVct(Tricluster tri) {
//
//		List<double [][]> res = new LinkedList<double [][]> ();
//		
//		double [][] tabla = construyeTablaVct (tri); 
//			
//		res.add(tabla);
//			
//		return res;
//	}
	
	
	
}
