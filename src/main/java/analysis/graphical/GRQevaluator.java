package analysis.graphical;

import general.Tricluster;

public interface GRQevaluator {
	
	
	public void loadParameters (double[][][] dataset);
	
	public double computeFitness(Tricluster tricluster);
	
	public double getHighestValue ();

}
