package labinterface;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTextField;

import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import analysis.Solution;
import input.datasets.Common;
import net.miginfocom.swing.MigLayout;
import utils.TextUtilities;

@SuppressWarnings("serial")
public class CommonSolutionPanel extends JPanel {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(CommonSolutionPanel.class);

	private JTextField solTag;
	private JPanel graphicAnalysisPanel;

	private Solution solution;
	private Common dataset;


	/**
	 * Create the panel.
	 */
	public CommonSolutionPanel(Solution sol, Common data) {

		solution = sol;
		
		dataset = data;

		initializeGUI();

		buildGraphPanels();
		

	}



	private void initializeGUI() {

		setLayout(new BorderLayout());

		// setBounds(new Rectangle(400,400));

		JPanel digestPanel = new JPanel();
		add(digestPanel, BorderLayout.NORTH);

		solTag = new JTextField();

		DecimalFormat format = TextUtilities.getDecimalFormat('.');
		double triq = solution.getValue("triq");
		double peq = solution.getValue("peq");
		double spq = solution.getValue("spq");

		String aux = "Solution " + solution.getIndex() + " , TRIQ = " + format.format(triq) + 
				" , PEQ = " + format.format(peq) + " , SPQ = " + format.format(spq);

		solTag.setText(aux);
		solTag.setBackground(SystemColor.window);
		solTag.setForeground(SystemColor.controlHighlight);
		solTag.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 20));
		solTag.setEditable(false);
		solTag.setBorder(null);
		digestPanel.add(solTag);
		solTag.setColumns(40);
		
		graphicAnalysisPanel = new JPanel();	
		add(graphicAnalysisPanel, BorderLayout.CENTER);
		setVisible(false);

	}

	private void buildGraphPanels() {

		GraphicsBuilder gr = GraphicsBuilder.getInstance();
		TriGraphs graphs = gr.buildTriclusterGraphs(solution.getTricluster(), dataset);

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
