package labinterface;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.knowm.xchart.BubbleChart;
import org.knowm.xchart.BubbleChartBuilder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.style.markers.SeriesMarkers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import analysis.Experiment;
import analysis.Solution;
import analysis.biological.GoInterval;
import analysis.biological.GoSignificance;
import analysis.biological.GoSlot;
import general.Tricluster;
import input.datasets.Common;
import input.datasets.Real;
import input.laboratory.AnalysisResources;
import labutils.Conversions;
import utils.SystemUtilities;
import utils.TextUtilities;

public class GraphicsBuilder {

	private static final Logger LOG = LoggerFactory.getLogger(GraphicsBuilder.class);

	private static final double CONT_EXP_1 = 0.98;
	private static final int W_EXP_1 = 350;
	private static final int H_EXP_1 = 145;
	
	private static final double CONT_EXP_2 = 0.98;
	private static final int W_EXP_2 = 350;
	private static final int H_EXP_2 = 145;
	
	
	private static final double CONT_EXP_COMP_1 = 0.98;
	private static final int W_EXP_COMP_1 = 1000;
	private static final int H_EXP_COMP_1 = 700;
	
	private static final double CONT_EXP_COMP_2 = 0.85;
	private static final int W_EXP_COMP_2 = 1000;
	private static final int H_EXP_COMP_2 = 800;
	
	
	private static final double CONT_SUMMARY = 0.98;
	private static final int W_SUMMARY = 550;
	private static final int H_SUMMARY = 145;

	private static final double CONT_SIG = 0.8;
	private static final int W_SIG = 580;
	private static final int H_SIG = 300;

	private static final double CONT_GCT = 0.7;
	private static final int W_GCT = 420;
	private static final int H_GCT = 170;

	private static final double CONT_GTC = 0.7;
	private static final int W_GTC = 420;
	private static final int H_GTC = 170;

	private static final double CONT_TGC = 0.7;
	private static final int W_TGC = 400;
	private static final int H_TGC = 170;

	/**
	 * Singleton field.
	 */
	static private GraphicsBuilder singleton = new GraphicsBuilder();

	/**
	 * Void constructor.
	 */
	private GraphicsBuilder() {
	}

	/**
	 * Gets an instance of {@link SystemUtilities}.
	 * 
	 * @return the instance of {@link SystemUtilities}
	 */
	public static GraphicsBuilder getInstance() {
		return singleton;
	}

	public List<BubbleChart> buildSummaryBubbleCharts(List<Solution> solutions) {
		
		List<BubbleChart> charts = new ArrayList<BubbleChart> (2);
		
		double[] grq = new double[solutions.size()];
		
		double[] bioq = new double[solutions.size()];
		
		double[] peq = new double[solutions.size()];
		double[] spq = new double[solutions.size()];
		double[] triq = new double[solutions.size()];
		
		int i = 0;
		
		double shift = 10.0;
		
		for (Solution sol :solutions){
			
			grq[i] = sol.getValue("grq");
			bioq[i] = sol.getValue("bioq");
			peq[i] = sol.getValue("peq");
			spq[i] = sol.getValue("spq");
			triq[i] = sol.getValue("triq")*shift;
			i++;
			
		}
		
		BubbleChart chart1 = getBubbleChart("BIOQ vs GRQ volume TRIQ",W_EXP_1,H_EXP_1,"bioq", "grq",CONT_EXP_1,Color.GREEN);
			chart1.addSeries("bioq-grq", bioq, grq, triq);
			charts.add(chart1);			
			
			BubbleChart chart2 = getBubbleChart("PEQ vs SPQ volume TRIQ",W_EXP_2,H_EXP_2,"peq", "spq",CONT_EXP_2,Color.cyan);
			chart2.addSeries("peq-spq", peq, spq, triq);
			charts.add(chart2);
		
				
		return charts;
	}
	
	public List<XYChart> buildSummaryScatterCharts(List<Solution> solutions) {
		
		List<XYChart> charts = new ArrayList<XYChart> (2);
		
		double[] grq = new double[solutions.size()];		
		double[] peq = new double[solutions.size()];
		double[] spq = new double[solutions.size()];
		double[] triq = new double[solutions.size()];
		
		int i = 0;		
		double shift = 1.0;
		
		for (Solution sol :solutions){
			
			grq[i] = sol.getValue("grq");
			peq[i] = sol.getValue("peq");
			spq[i] = sol.getValue("spq");
			triq[i] = sol.getValue("triq")*shift;
			i++;
			
		}
		
		XYChart chart1 = getScatterChart("TRIQ-GRQ distribution",W_EXP_1,H_EXP_1,"triq", "grq",CONT_EXP_1,Color.ORANGE);
			chart1.addSeries("triq-grq",  triq, grq);
			charts.add(chart1);			
			
			XYChart chart2 = getScatterChart("PEQ-SPQ distribution",W_EXP_2,H_EXP_2,"peq", "spq",CONT_EXP_2,Color.GREEN);
			chart2.addSeries("peq-spq", peq, spq);
			charts.add(chart2);
		
				
		return charts;
	}
	
	
	
	
	public List<CategoryChart> buildCompCategoryCharts(List<Experiment> experiments) {
		
		List<CategoryChart> charts = new ArrayList<CategoryChart>(3);
		
		List<String> xAxis = new ArrayList<String>(experiments.size());
		
		List<Double> meanValues = new ArrayList<Double>(experiments.size());
		
		List<Double> stdevValues = new ArrayList<Double>(experiments.size());
		
		List<Double> bestTriqValues = new ArrayList<Double>(experiments.size());
		
		for (Experiment e : experiments){			
			xAxis.add(e.getExperimentName());
			meanValues.add((Double)e.getValue("mean"));
			stdevValues.add((Double)e.getValue("stdev"));
			bestTriqValues.add((Double)e.getValue("besttriq"));
		}
				
		CategoryChart meanChart = getCategoryChart("MEAN", W_EXP_COMP_1, H_EXP_COMP_1, "", Color.ORANGE,90);
		meanChart.addSeries("mean", xAxis, meanValues);
		charts.add(meanChart);
		
		CategoryChart stdevChart = getCategoryChart("STDEV", W_EXP_COMP_1, H_EXP_COMP_1, "", Color.MAGENTA,90);
		stdevChart.addSeries("stdev", xAxis, stdevValues);
		charts.add(stdevChart);
			
		CategoryChart bestTriqChart = getCategoryChart("BEST TRIQ", W_EXP_COMP_1, H_EXP_COMP_1, "", Color.CYAN,90);
		bestTriqChart.addSeries("triq values", xAxis, bestTriqValues);
		charts.add(bestTriqChart);
		
		return charts;
	
	}
	
	public BubbleChart buildBubbleChart(List<Experiment> experiments) {
		
		BubbleChart chart = getBubbleChart("SUMMARY",W_EXP_COMP_2,H_EXP_COMP_2,"mean triq", "stdev",CONT_EXP_COMP_2,Color.GREEN);
		
		double[] x = new double[experiments.size()];
		double[] y = new double[experiments.size()];	
		double[] bubble = new double[experiments.size()];
		int i = 0;
		
		double bubbleShift = 10.0;
		
		for (Experiment e : experiments){
			x[i] = ((Double)e.getValue("mean")).doubleValue();
			y[i] = ((Double)e.getValue("stdev")).doubleValue();
			bubble[i] = ((Double)e.getValue("besttriq")).doubleValue()*bubbleShift;
			i++;
		}		
		
	    chart.addSeries("Summary", x, y, bubble);
	    
		return chart;
	}

	public List<CategoryChart> buildSummaryGraphs(List<Solution> solutions, char analisysType) {

		List<CategoryChart> charts = new ArrayList<CategoryChart>(4);

		List<String> xAxis = new ArrayList<String>(solutions.size());

		List<Double> triqValues = new ArrayList<Double>(solutions.size());

		List<Double> bioqValues = null;
		if (analisysType == 'b') {
			bioqValues = new ArrayList<Double>(solutions.size());
		}
		List<Double> grqValues = new ArrayList<Double>(solutions.size());
		List<Double> peqValues = new ArrayList<Double>(solutions.size());
		List<Double> spqValues = new ArrayList<Double>(solutions.size());

		double shift = 1.0;

		for (Solution sol : solutions) {
			xAxis.add("" + sol.getIndex());
			triqValues.add(new Double(sol.getValue("triq") * shift));

			if (analisysType == 'b') {
				bioqValues.add(new Double(sol.getValue("bioq") * shift));
			}

			grqValues.add(new Double(sol.getValue("grq") * shift));
			peqValues.add(new Double(sol.getValue("peq") * shift));
			spqValues.add(new Double(sol.getValue("spq") * shift));
		}

		CategoryChart triqChart = getCategoryChart("TRIQ values", W_SUMMARY, H_SUMMARY, "solutions", Color.BLUE);
		triqChart.addSeries("triq values", xAxis, triqValues);
		charts.add(triqChart);

		if (analisysType == 'b') {
			CategoryChart bioqChart = getCategoryChart("BIOQ values", W_SUMMARY, H_SUMMARY, "solutions", Color.GREEN);
			bioqChart.addSeries("bioq values", xAxis, bioqValues);
			charts.add(bioqChart);
		}

		CategoryChart qrqChart = getCategoryChart("GRQ values", W_SUMMARY, H_SUMMARY, "solutions", Color.ORANGE);
		qrqChart.addSeries("qrq values", xAxis, grqValues);
		charts.add(qrqChart);

		CategoryChart peqChart = getCategoryChart("PEQ values", W_SUMMARY, H_SUMMARY, "solutions", Color.CYAN);
		peqChart.addSeries("peq values", xAxis, peqValues);
		charts.add(peqChart);

		CategoryChart spqChart = getCategoryChart("SPQ values", W_SUMMARY, H_SUMMARY, "solutions", Color.MAGENTA);
		spqChart.addSeries("spq values", xAxis, spqValues);
		charts.add(spqChart);

		return charts;

	}

	public List<CategoryChart> buildSignificanceSolutionGraph(List<GoSlot> significance, int solIndex) {

		List<CategoryChart> charts = new ArrayList<CategoryChart>(2);

		List<GoInterval> intervals = (GoSignificance.getInstance()).getINTERVALS();

		// Collections.sort(intervals);

		List<String> xAxis = new ArrayList<String>(intervals.size());

		for (GoInterval in : intervals) {
			xAxis.add("" + in.getLevel());
		}

		List<Double> concentrationValues = new ArrayList<Double>(significance.size());
		List<Integer> itemValues = new ArrayList<Integer>(significance.size());

		for (GoSlot sl : significance) {

			concentrationValues.add(new Double(sl.getConcentration()));
			itemValues.add(new Integer(sl.getCount()));

		}

		Collections.reverse(xAxis);
		Collections.reverse(concentrationValues);
		Collections.reverse(itemValues);

		LOG.debug(xAxis.toString());
		LOG.debug(concentrationValues.toString());
		LOG.debug(itemValues.toString());

		CategoryChart concentrationChart = getCategoryChart(
				"Solution " + solIndex + " Biological Significance - Concentration", W_SIG, H_SIG, "levels",
				"term concentration", Color.BLUE, false, 0.0, 1.0);
		concentrationChart.addSeries("term concentration", xAxis, concentrationValues);

		CategoryChart itemChart = getCategoryChart("Solution " + solIndex + " Biological Significance - Item Count",
				W_SIG, H_SIG, "levels", "item count", Color.GREEN, true, 0.0, -1.0);
		itemChart.addSeries("item count", xAxis, itemValues);

		charts.add(concentrationChart);
		charts.add(itemChart);

		return charts;
	}

	public TriGraphs buildTriclusterGraphs(Tricluster tricluster, Common dataset) {

		TriGraphs guiTri = new TriGraphs(tricluster);

		LOG.debug("g-c-t");
		guiTri.setGCTcharts(buildChartsCathegory(guiTri.getTricluster(), dataset, "g-c-t"));
		LOG.debug("g-t-c");
		guiTri.setGTCcharts(buildChartsCathegory(guiTri.getTricluster(), dataset, "g-t-c"));
		LOG.debug("t-g-c");
		guiTri.setTGCcharts(buildChartsTGC(guiTri.getTricluster(), dataset));

		return guiTri;

	}

	// 0->
	public List<TriGraphs> buildGuiSolutions(List<Tricluster> triclusters, Common dataset) {

		List<TriGraphs> r = new ArrayList<TriGraphs>(triclusters.size());

		int i = 0;
		for (Tricluster tri : triclusters) {

			TriGraphs guiTri = new TriGraphs(tri);

			LOG.debug("Tricluster " + i);
			LOG.debug("g-c-t");
			guiTri.setGCTcharts(buildChartsCathegory(guiTri.getTricluster(), dataset, "g-c-t"));
			LOG.debug("g-t-c");
			guiTri.setGTCcharts(buildChartsCathegory(guiTri.getTricluster(), dataset, "g-t-c"));
			LOG.debug("t-g-c");
			guiTri.setTGCcharts(buildChartsTGC(guiTri.getTricluster(), dataset));

			r.add(guiTri);

			i++;

		}

		return r;

	}

	public List<GuiSolution> buildGuiSolutions(AnalysisResources currentSolutions) {

		List<Tricluster> sols = currentSolutions.getSolutions();

		Common dataset = (currentSolutions.getControl()).getDataset();

		List<GuiSolution> guiSolutions = new ArrayList<GuiSolution>(sols.size());

		int i = 0;

		for (Tricluster tri : sols) {

			TriGraphs guiTri = new TriGraphs(tri);

			LOG.debug("Tricluster " + i);
			LOG.debug("g-c-t");
			guiTri.setGCTcharts(buildChartsCathegory(guiTri.getTricluster(), dataset, "g-c-t"));
			LOG.debug("g-t-c");
			guiTri.setGTCcharts(buildChartsCathegory(guiTri.getTricluster(), dataset, "g-t-c"));
			LOG.debug("t-g-c");
			guiTri.setTGCcharts(buildChartsTGC(guiTri.getTricluster(), dataset));

			guiSolutions.add(new GuiSolution(guiTri));

		}

		return guiSolutions;

	}

	private List<XYChart> buildChartsCathegory(Tricluster tri, Common dataset, String config) {

		double[][][] data = dataset.getDataset();

		// data = [g,s,t]
		// x = g, o = t, p = c
		// LOG.debug(tri.completeToString());
		// LOG.debug("" + data.length + "," + data[0].length + "," +
		// data[0][0].length);

		List<Integer> xCoor = null;
		List<Integer> oCoor = null;
		List<Integer> pCoor = null;

		int nx = 0;
		int no = 0;
		int np = 0;

		String[] panelLabels = null;
		String[] outputLabels = null;
		String panelPrefix = "";

		String xAxisTittle = "genes";
		String yAxisTittle = "expression level";

		int w = 0;
		int l = 0;
		double csize = 0.0;

		switch (config) {

		case "g-c-t":// x = g, o = c, p = t
			panelPrefix = "GCT";
			panelLabels = ((Real) dataset).getTimeNames();
			outputLabels = ((Real) dataset).getSampleNames();
			xCoor = tri.getGenes();
			oCoor = tri.getSamples();
			pCoor = tri.getTimes();
			nx = xCoor.size();
			no = oCoor.size();
			np = pCoor.size();
			w = W_GCT;
			l = H_GCT;
			csize = CONT_GCT;
			break;

		case "g-t-c":// x = g, o = t, p = c
			panelPrefix = "GTC";
			panelLabels = ((Real) dataset).getSampleNames();
			outputLabels = ((Real) dataset).getTimeNames();
			xCoor = tri.getGenes();
			oCoor = tri.getTimes();
			pCoor = tri.getSamples();
			nx = xCoor.size();
			no = oCoor.size();
			np = pCoor.size();
			w = W_GTC;
			l = H_GTC;
			csize = CONT_GTC;
			break;

		}

		List<XYChart> charts = new ArrayList<XYChart>(np);

		double[] xAxis = new double[nx];

		for (int i = 0; i < nx; i++) {
			xAxis[i] = i + 1;
		}

		List<double[]> series = null;

		// panel = c
		for (Integer c_ip : pCoor) {

			int ip = c_ip.intValue();

			// outputs = t
			series = new ArrayList<double[]>(no);

			for (Integer c_io : oCoor) {

				int io = c_io.intValue();

				double[] serie = new double[nx];

				int i = 0;

				for (Integer c_ix : xCoor) {

					int ix = c_ix.intValue();

					switch (config) {

					case "g-c-t":// x = g, o = c, p = t

						serie[i] = data[ix][io][ip];

						break;

					case "g-t-c":// x = g, o = t, p = c

						serie[i] = data[ix][ip][io];

						break;

					}

					i++;

				}

				series.add(serie);

			}

			@SuppressWarnings("unused")
			String debug = "\n";

			XYChart chart = getXYchart(panelPrefix + " - " + panelLabels[ip], w, l, xAxisTittle, yAxisTittle, csize,
					false, false);

			chart.getStyler().setXAxisTicksVisible(false);

			int sn = 0;
			for (double[] serie : series) {

				debug += "Panel " + panelLabels[ip] + " Serie " + sn + " : "
						+ TextUtilities.vectorOfDoubleToString(serie, 0) + "\n";

				XYSeries s = chart.addSeries(outputLabels[sn], xAxis, serie);

				s.setMarker(SeriesMarkers.NONE);

				sn++;
			}

			charts.add(chart);

			// LOG.debug(debug);

		}

		return charts;

	}

	private List<XYChart> buildChartsTGC(Tricluster tri, Common dataset) {

		// data = [g,s,t]

		double[][][] data = dataset.getDataset();

		String[] panelLabels = ((Real) dataset).getSampleNames();

		List<Integer> xCoor = null;
		List<Integer> oCoor = null;
		List<Integer> pCoor = null;

		int nx = 0;
		int no = 0;
		int np = 0;

		String xAxisTittle = "time points";
		String panelPrefix = "TGC";

		xCoor = tri.getTimes();
		oCoor = tri.getGenes();
		pCoor = tri.getSamples();
		nx = xCoor.size();
		no = oCoor.size();
		np = pCoor.size();

		List<XYChart> charts = new ArrayList<XYChart>(np);
		double[] xAxis = Conversions.fromListIntegerToArrayDouble(xCoor);

		List<double[]> series = null;

		// panel = c
		for (Integer c_ip : pCoor) {

			int ip = c_ip.intValue();

			// outputs = t
			series = new ArrayList<double[]>(no);

			for (Integer c_io : oCoor) {

				int io = c_io.intValue();

				double[] serie = new double[nx];

				int i = 0;
				for (Integer c_ix : xCoor) {

					int ix = c_ix.intValue();

					serie[i] = data[io][ip][ix];

					i++;

				}

				series.add(serie);

			}

			@SuppressWarnings("unused")
			String debug = "\n";

			// Create Chart

			XYChart chart = getXYchart(panelPrefix + " - " + panelLabels[ip], W_TGC, H_TGC, xAxisTittle,
					"expresion Level", CONT_TGC, false, true);

			int sn = 0;
			for (double[] serie : series) {

				debug += "Panel " + panelLabels[ip] + " Serie " + sn + " : "
						+ TextUtilities.vectorOfDoubleToString(serie, 0) + "\n";
				XYSeries s = chart.addSeries("gen " + sn, xAxis, serie);
				s.setMarker(SeriesMarkers.NONE);

				sn++;
			}

			charts.add(chart);

			// LOG.debug(debug);

		}

		return charts;

	}
	
	//XYCHART

	private XYChart getXYchart(String title, int width, int height, String xAxisTitle, String yAxisTitle,
			double plotContentSize, boolean legend, boolean xAxisTicksVisible) {

		return getXYchart(title, width, height, xAxisTitle, yAxisTitle, SystemColor.window, Color.BLACK, 1, false,
				plotContentSize, legend, 0, new Font("Monospaced", Font.PLAIN, 8), xAxisTicksVisible);

	}

	private XYChart getXYchart(String title, int width, int height, String xAxisTitle, String yAxisTitle,
			Color backgorundColor, Color borderColor, int charPadding, boolean annotations, double plotContentSize,
			boolean legend, int xAxisRotation, Font axisLabelFont, boolean xAxisTicksVisible) {

		XYChart chart = new XYChartBuilder().build();

		configureCommons(chart, title, width, height, xAxisTitle, yAxisTitle, backgorundColor, borderColor, charPadding,
				annotations, plotContentSize, legend);

		configureXY(chart, xAxisRotation, axisLabelFont, xAxisTicksVisible);

		return chart;

	}

	private void configureXY(XYChart chart, int xAxisRotation, Font axisLabelFont, boolean xAxisTicksVisible) {

		chart.getStyler().setXAxisLabelRotation(xAxisRotation);
		chart.getStyler().setAxisTickLabelsFont(axisLabelFont);
		chart.getStyler().setXAxisTicksVisible(xAxisTicksVisible);

	}
	
	
	
	//BUBBLECHART

	private BubbleChart getBubbleChart(String title, int width, int height, String xAxisTitle, String yAxisTitle, double plotContentSize
			,Color seriesColor) {

		BubbleChart chart = new BubbleChartBuilder().build();
		
		chart.setWidth(width);
		chart.setHeight(height);
		chart.setTitle(title);
		chart.setXAxisTitle(xAxisTitle);
		chart.setYAxisTitle(yAxisTitle);

		chart.getStyler().setChartBackgroundColor( SystemColor.window);
		chart.getStyler().setPlotBorderColor(Color.BLACK);

		chart.getStyler().setChartPadding(1);
		chart.getStyler().setHasAnnotations(true);
		chart.getStyler().setPlotContentSize(plotContentSize);
		chart.getStyler().setLegendVisible(false);	

		Color[] sc = { seriesColor  };
		chart.getStyler().setSeriesColors(sc);
		chart.getStyler().setAxisTickLabelsFont(new Font("Monospaced", Font.PLAIN, 8));
		chart.getStyler().setXAxisLabelRotation(0);
		chart.getStyler().setYAxisMax(1.0);
		chart.getStyler().setYAxisMin(0.0);
		chart.getStyler().setXAxisMax(1.0);
		chart.getStyler().setXAxisMin(0.0);


		return chart;
	}
	
	
	//CATEGORYCHART
	

	// SIGNIFICANCE
	private CategoryChart getCategoryChart(String title, int width, int height, String xAxisTitle, String yAxisTitle,
			Color seriesColor, boolean annotations, double yMin, double yMax) {
	
		
		return getCategoryChart(title, width, height, xAxisTitle, yAxisTitle, SystemColor.window, Color.BLACK, 1,
				annotations, CONT_SIG, false, seriesColor, 0.4, new Font("Monospaced", Font.PLAIN, 8), 90, yMin, yMax);

	}
	
	// COMP
	
	
	private CategoryChart getCategoryChart(String title, int width, int height, String xAxisTitle, Color seriesColor,int xAxisLabelRotation) {

		return getCategoryChart(title, width, height, xAxisTitle, "", SystemColor.window, Color.BLACK, 1, false,
				CONT_EXP_COMP_1, false, seriesColor, 0.4, new Font("Monospaced", Font.PLAIN, 8), xAxisLabelRotation, 0.0, 1.0);

	}
	
	// SUMMARY
	
	private CategoryChart getCategoryChart(String title, int width, int height, String xAxisTitle, Color seriesColor) {

		return getCategoryChart(title, width, height, xAxisTitle, "", SystemColor.window, Color.BLACK, 1, false,
				CONT_SUMMARY, false, seriesColor, 0.4, new Font("Monospaced", Font.PLAIN, 8), 0, 0.0, 1.0);

	}

	private CategoryChart getCategoryChart(String title, int width, int height, String xAxisTitle, String yAxisTitle,
			Color backgorundColor, Color borderColor, int charPadding, boolean annotations, double plotContentSize,
			boolean legend, Color seriesColor, double aviableSpaceFill, Font axisLabelFont, int xAxisLabelRotation,
			double yMin, double yMax) {

		CategoryChart chart = new CategoryChartBuilder().build();

		configureCommons(chart, title, width, height, xAxisTitle, yAxisTitle, backgorundColor, borderColor, charPadding,
				annotations, plotContentSize, legend);

		configureCategory(chart, seriesColor, aviableSpaceFill, axisLabelFont, xAxisLabelRotation, yMin, yMax);

		return chart;

	}

	private void configureCommons(@SuppressWarnings("rawtypes") Chart chart, String title, int width, int height,
			String xAxisTitle, String yAxisTitle, Color backgorundColor, Color borderColor, int charPadding,
			boolean annotations, double plotContentSize, boolean legend) {

		chart.setWidth(width);
		chart.setHeight(height);
		chart.setXAxisTitle(xAxisTitle);
		chart.setTitle(title);

		chart.getStyler().setChartBackgroundColor(backgorundColor);
		chart.getStyler().setPlotBorderColor(borderColor);

		chart.getStyler().setChartPadding(charPadding);
		chart.getStyler().setHasAnnotations(annotations);
		chart.getStyler().setPlotContentSize(plotContentSize);
		chart.getStyler().setLegendVisible(legend);

		chart.setYAxisTitle(yAxisTitle);

	}

	private void configureCategory(CategoryChart chart, Color seriesColor, double aviableSpaceFill, Font axisLabelFont,
			int xAxisLabelRotation, double yMin, double yMax) {

		Color[] sc = { seriesColor };
		chart.getStyler().setSeriesColors(sc);
		chart.getStyler().setAvailableSpaceFill(aviableSpaceFill);
		chart.getStyler().setAxisTickLabelsFont(axisLabelFont);
		chart.getStyler().setXAxisLabelRotation(xAxisLabelRotation);

		if (yMax != -1.0)
			chart.getStyler().setYAxisMax(yMax);

		if (yMin != -1.0)
			chart.getStyler().setYAxisMin(yMin);

	}
	
	//SCATTER
	
	private XYChart getScatterChart(String title, int width, int height, String xAxisTitle, String yAxisTitle, double plotContentSize,
			Color seriesColor ) {

		XYChart chart = new XYChartBuilder().build();
		
		chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Scatter);
	    	
		chart.setWidth(width);
		chart.setHeight(height);
		chart.setTitle(title);
		chart.setXAxisTitle(xAxisTitle);
		chart.setYAxisTitle(yAxisTitle);

		chart.getStyler().setChartBackgroundColor( SystemColor.window);
		chart.getStyler().setPlotBorderColor(Color.BLACK);

		chart.getStyler().setChartPadding(1);
		chart.getStyler().setHasAnnotations(true);
		chart.getStyler().setPlotContentSize(plotContentSize);
		chart.getStyler().setLegendVisible(false);	

		Color[] sc = { seriesColor  };
		chart.getStyler().setSeriesColors(sc);
		chart.getStyler().setAxisTickLabelsFont(new Font("Monospaced", Font.PLAIN, 8));
		chart.getStyler().setXAxisLabelRotation(0);
		chart.getStyler().setYAxisMax(1.0);
		chart.getStyler().setYAxisMin(0.0);
		chart.getStyler().setXAxisMax(1.0);
		chart.getStyler().setXAxisMin(0.0);


		return chart;
	}

}
