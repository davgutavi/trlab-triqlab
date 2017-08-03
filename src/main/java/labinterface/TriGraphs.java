package labinterface;

import java.util.List;

import org.knowm.xchart.XYChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import general.Tricluster;

public class TriGraphs {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(TriGraphs.class);
	
	private Tricluster tricluster;
	
	private List<XYChart> GCTcharts;
	private List<XYChart> GTCcharts;
	private List<XYChart> TGCcharts;
	
	
	public TriGraphs (Tricluster tri){
		tricluster = tri;
	}


	public List<XYChart> getGCTcharts() {
		return GCTcharts;
	}


	public void setGCTcharts(List<XYChart> gCTcharts) {
		GCTcharts = gCTcharts;
	}


	public List<XYChart> getGTCcharts() {
		return GTCcharts;
	}


	public void setGTCcharts(List<XYChart> gTCcharts) {
		GTCcharts = gTCcharts;
	}


	public List<XYChart> getTGCcharts() {
		return TGCcharts;
	}


	public void setTGCcharts(List<XYChart> tGCcharts) {
		TGCcharts = tGCcharts;
	}


	public Tricluster getTricluster() {
		return tricluster;
	}

	
	
	
	
	
	
	
}
