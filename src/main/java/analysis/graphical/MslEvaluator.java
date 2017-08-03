package analysis.graphical;

import general.Tricluster;

import java.util.ArrayList;
import java.util.List;

import labutils.Conversions;

import org.apache.commons.math3.stat.StatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MslEvaluator implements GRQevaluator{

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(MslEvaluator.class);
	
	
	private static final double HIGHEST_VALUE = 2.0*Math.PI;
	
	private double[][][] dataset;
	
	public MslEvaluator () {
	}
	
	
	public void loadParameters  (double[][][] dataset) {
		this.dataset = dataset;
	}
	
	public double getHighestValue(){
		return HIGHEST_VALUE;
		
	}
	

	
public double computeFitness(Tricluster tricluster) {
		
		double r = 0.0;
		
		Transformation transformer = new OriginalTransformationSupport(dataset);
		
		List<double[][][]> trs = transformer.transform(tricluster);
		
		List<Double> wholeDiff = new ArrayList<Double>();
		
		List<Double> wholeMeans = new ArrayList<Double>();
				
		//GCT 	
		
		double[][][] ang0 = buildAnglesCube(trs.get(0));
		List<Double> diff0 = buildDifferencesTable(ang0);
		wholeDiff.addAll(diff0);
			
		double gctMean = StatUtils.mean(Conversions.fromListOfDoubleToArray(diff0));
		wholeMeans.add(new Double (gctMean));
		
		//GTC
		
		double[][][] ang1 = buildAnglesCube(trs.get(1));
		List<Double> diff1 = buildDifferencesTable(ang1);
		wholeDiff.addAll(diff1);
			
		double gtcMean = StatUtils.mean(Conversions.fromListOfDoubleToArray(diff1));
		wholeMeans.add(new Double (gtcMean));
		
		//TGC
		
		double[][][] ang4 = buildAnglesCube(trs.get(4));
		List<Double> diff4 = buildDifferencesTable(ang4);
		wholeDiff.addAll(diff4);
			
		double tgcMean = StatUtils.mean(Conversions.fromListOfDoubleToArray(diff4));
		wholeMeans.add(new Double (tgcMean));
	
		
		double [] means = Conversions.fromListOfDoubleToArray(wholeMeans);
		r = StatUtils.mean(means);
		
			
		return r;
	}
	

	
	
	private double [][][] buildAnglesCube (double[][][] tr){
		
		int npanels = tr[0][0].length;
		int noutlines = tr.length;
		int naxis = tr[0].length;
		
		//CREAR CUBO DE ANGULOS de "nseries" filas, "ngraficos" columnas y "nejex-1" de profundidad
		
		double [][][] angles = new double[noutlines][npanels][naxis-1];
		
		//Para cada grafico
		
		for (int panel = 0;panel<npanels;panel++){
		
			//Para cada serie
					
			for (int outline = 0;outline<noutlines;outline++){
						
				//y para cada par de valores del eje X
												
				for (int xpoint = 0;xpoint<(naxis-1);xpoint++){
							
					//Calcular pendiente 
							
					double x1 = (double) (xpoint);
					double x2 = (double) (xpoint + 1);

					double y1 = 0.0;
					double y2 = 0.0;
					
					y1 = tr[outline][xpoint][panel];
					y2 = tr[outline][xpoint+1][panel];
					
					double gradient = (y2 - y1) / (x2 - x1);
							
					//Calcular arco tangente
					double atan = Math.atan(gradient);
							
					
					double spin = angleTransformation(atan);
					
//					double spin = atan;
//							
//					if (atan<0) spin = atan + (2*Math.PI);
							
					//Almacenar en tabla de angulos
					angles[outline][panel][xpoint] = spin;
							
				}//For
					
			}
					
		}
		
		return angles;
		
	}//construteCuboAngulos
	
	private static double angleTransformation(double angle){
		
		double r = angle;
	
		if (angle<0) r = angle + (2*Math.PI);
		
		return r;
	}

	private  List<Double> buildDifferencesTable (double [][][] angles){
		
		List<Double> r = new ArrayList<Double>();
		
		for (int i = 0;i<angles.length;i++){
						
			for (int j = 0;j<angles[i].length;j++){
									
					double diff = 0.0f;
					
					for(int column = (j+1);column<angles[i].length;column++){
						
						diff = computeDifference(angles,i,j,i,column);
						
						r.add(new Double(diff));
						
					}
					
										
					for(int row = (i+1);row<angles.length;row++){
						
						diff = computeDifference(angles,i,j,row,j);
						
						r.add(new Double(diff));
						
					}
					
				
			}
	
		}
		
		return r;
		
	}
	
	private double computeDifference (double[][][] angles, int aRow, int aColumn, int bRow, int bColumn) {
		
		double r = 0.0;
			
		double [] differences = new double [angles[0][0].length];
		
		for (int i = 0;i<angles[0][0].length;i++){
			
			double max = Math.max(angles[aRow][aColumn][i], angles[bRow][bColumn][i]);
			
			double min = Math.min(angles[aRow][aColumn][i], angles[bRow][bColumn][i]);
			
			differences[i] = max - min;
						
		}
			
		r = StatUtils.mean(differences);
		
		return r;
	}
	
}
