package labinterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableColumnModel;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import analysis.Solution;
import analysis.biological.GoSlot;
import input.datasets.Common;
import net.miginfocom.swing.MigLayout;
import utils.TextUtilities;

@SuppressWarnings("serial")
public class BioSolutionPanel extends JPanel {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(BioSolutionPanel.class);

	private JTextField solTag;
	private JTabbedPane solutionAnalysisTab;
	private JPanel graphicAnalysisPanel;
	private JPanel bioAnalysisPanel;

	private Solution solution;
	private Common dataset;


	/**
	 * Create the panel.
	 */
	public BioSolutionPanel(Solution sol, Common data) {

		solution = sol;
		
		dataset = data;

		initializeGUI();

		buildGraphPanels();
		
		buildBioPanels();

	}

	private void buildBioPanels() {
		// TODO Auto-generated method stub
		bioAnalysisPanel.setLayout(new MigLayout("", "[fill]40[]", "[fill][fill]"));
		
		JTable goTable = new JTable(new GoTableModel(solution.getGoStudy()));
		goTable.setAutoCreateRowSorter(true);
		goTable.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
		TableColumnModel columnModel = goTable.getColumnModel();
		columnModel.getColumn(0).setMinWidth(90);
		columnModel.getColumn(1).setMinWidth(320);
		columnModel.getColumn(2).setMinWidth(110);
		
		JScrollPane tScroll = new JScrollPane (goTable);
		tScroll.setMinimumSize(new Dimension(550,500));
//		goTable.setFillsViewportHeight(true);
		
		List<GoSlot> sig = solution.getGoSlots("pa");
		
		GraphicsBuilder gr = GraphicsBuilder.getInstance();
		
		List<CategoryChart> charts = gr.buildSignificanceSolutionGraph(sig, solution.getIndex());
		
		JPanel pnlConcentrationChart = new XChartPanel<CategoryChart>(charts.get(0));
		JPanel pnlItemChart = new XChartPanel<CategoryChart>(charts.get(1));
		
		bioAnalysisPanel.add(pnlConcentrationChart,"cell 0 0");
		bioAnalysisPanel.add(tScroll,"span 1 2");
		bioAnalysisPanel.add(pnlItemChart,"cell 0 1");
		
		
		
		
		
		
	}

	private void initializeGUI() {

		setLayout(new BorderLayout());

		// setBounds(new Rectangle(400,400));

		JPanel digestPanel = new JPanel();
		add(digestPanel, BorderLayout.NORTH);

		solTag = new JTextField();

		DecimalFormat format = TextUtilities.getDecimalFormat('.');
		double triq = solution.getValue("triq");
		double bioq = solution.getValue("bioq");
		double peq = solution.getValue("peq");
		double spq = solution.getValue("spq");

		String aux = "Solution " + solution.getIndex() + " , TRIQ = " + format.format(triq) + " , BIOQ = "
				+ format.format(bioq) + " , PEQ = " + format.format(peq) + " , SPQ = " + format.format(spq);

		solTag.setText(aux);
		solTag.setBackground(SystemColor.window);
		solTag.setForeground(SystemColor.controlHighlight);
		solTag.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 20));
		solTag.setEditable(false);
		solTag.setBorder(null);
		digestPanel.add(solTag);
		solTag.setColumns(40);

		solutionAnalysisTab = new JTabbedPane(JTabbedPane.TOP);
		add(solutionAnalysisTab, BorderLayout.CENTER);
		// solutionAnalysisTab.setMaximumSize(new Dimension(unitW,unitL));

		// solutionAnalysisTab.setMaximumSize(new Dimension(50,50));

		graphicAnalysisPanel = new JPanel();
		solutionAnalysisTab.addTab("Graphical Analysis", null, graphicAnalysisPanel, null);

		bioAnalysisPanel = new JPanel();
		solutionAnalysisTab.addTab("Biological Analysis", null, bioAnalysisPanel, null);

		setVisible(false);

	}

	private void buildGraphPanels() {

		GraphicsBuilder gr = GraphicsBuilder.getInstance();
		TriGraphs graphs = gr.buildTriclusterGraphs(solution.getTricluster(), dataset);

		// int gctSize = graphs.getGCTcharts().size();
		// int gtcSize = graphs.getGTCcharts().size();
		// int tgcSize = graphs.getTGCcharts().size();

		List<Integer> sizes = Arrays.asList(graphs.getGCTcharts().size(), graphs.getGTCcharts().size(),
				graphs.getTGCcharts().size());

		int rows = (sizes.stream().max(Comparator.naturalOrder()).get()).intValue();

		String rc = "";

		for (int i = 0; i < rows; i++) {
			rc += "[]";
		}

		graphicAnalysisPanel.setLayout(new MigLayout("", "[][][]", rc));

		int i = 0;
		for (XYChart c : graphs.getGCTcharts()) {
			JPanel pnlChart = new XChartPanel<XYChart> (c);
			String loc = "cell 0 " + i;
			graphicAnalysisPanel.add(pnlChart, loc);
			i++;
		}

		i = 0;
		for (XYChart c : graphs.getGTCcharts()) {
			JPanel pnlChart = new XChartPanel<XYChart>(c);
			String loc = "cell 1 " + i;
			graphicAnalysisPanel.add(pnlChart, loc);
			i++;
		}

		i = 0;
		for (XYChart c : graphs.getTGCcharts()) {
			JPanel pnlChart = new XChartPanel<XYChart>(c);
			String loc = "cell 2 " + i;
			graphicAnalysisPanel.add(pnlChart, loc);
			i++;
		}

	}

}
