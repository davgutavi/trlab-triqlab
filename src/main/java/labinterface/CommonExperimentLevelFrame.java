package labinterface;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

import analysis.Experiment;
import analysis.Solution;
import net.miginfocom.swing.MigLayout;
import utils.TextUtilities;

@SuppressWarnings("serial")
public class CommonExperimentLevelFrame extends JFrame {

	private static final int W = 1500;
	private static final int H = 800;
	private Experiment experiment;
	private JPanel contentPane;
	private SolutionLevelFrame solutionLevelFrame;

	/**
	 * Create the frame.
	 */
	public CommonExperimentLevelFrame(Experiment experiment) {
		
		this.experiment = experiment;
		
		initializeGUI();
		
		buildSolutionLevelFrame();
		
		buildSummaryGraphs();

	}
	
	

	private void buildSolutionLevelFrame() {
		solutionLevelFrame = new SolutionLevelFrame(experiment);
		solutionLevelFrame.setVisible(false);
	}



	private void buildSummaryGraphs() {
	
		List<Solution> solutions = experiment.getSolutions();
		
		JTable triqTable = new JTable(new CommonExpTableModel(solutions));
		triqTable.setAutoCreateRowSorter(true);
		triqTable.setAutoResizeMode( JTable.AUTO_RESIZE_ALL_COLUMNS);
		triqTable.setFont(new Font("Monospaced", Font.PLAIN, 14));
		triqTable.setFillsViewportHeight(true);
		
		triqTable.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent me) {
		        JTable table =(JTable) me.getSource();
		        Point p = me.getPoint();
		        int row = table.rowAtPoint(p);
		        int index = table.convertRowIndexToModel(row);
		        if (me.getClickCount() == 2) {
		        	 solutionLevelFrame.displaySolution(index);
		        }
		    }
		});
		
		
		JScrollPane tScroll = new JScrollPane (triqTable);tScroll.validate();
		tScroll.setMaximumSize(new Dimension(550,340));
			
		JTextPane expTag = new JTextPane();

		DecimalFormat format = TextUtilities.getDecimalFormat('.');
		
		String expName = experiment.getExperimentName();
		String datasetName = experiment.getDatasetName();
		String bestSolution = ((Solution) experiment.getValue("bestsolution")).getName();
		double bestTriq = ((Double) experiment.getValue("besttriq")).doubleValue();
		double mean = ((Double) experiment.getValue("mean")).doubleValue();
		double stdev = ((Double) experiment.getValue("stdev")).doubleValue();
	

		String aux = 
				
				"Experiment : "+expName + " , Dataset : " + datasetName + 
				"\n\nBest solution " + bestSolution + " ( TRIQ = " + format.format(bestTriq) + " ) \n\nMean TRIQ = "
				+ format.format(mean) + " , Stdev TRIQ = " + format.format(stdev);

		expTag.setText(aux);
		expTag.setBackground(SystemColor.window);
		expTag.setForeground(SystemColor.controlHighlight);
		expTag.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 15));
		expTag.setEditable(false);
		expTag.setBorder(null);
		
		
		JButton solLevelButton = new JButton ("Solution Level Menu");
		
		solLevelButton.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 14));
		
		solLevelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				solutionLevelFrame.setVisible(true);
				
			}
		});
				
		GraphicsBuilder gr = GraphicsBuilder.getInstance();
		
		List<CategoryChart> lc = gr.buildSummaryGraphs(solutions,experiment.getAnalysisType());
		
		XChartPanel<CategoryChart> p1 = new XChartPanel<CategoryChart>(lc.get(0));
		XChartPanel<CategoryChart> p2 = new XChartPanel<CategoryChart>(lc.get(1));
		XChartPanel<CategoryChart> p3 = new XChartPanel<CategoryChart>(lc.get(2));
		XChartPanel<CategoryChart> p4 = new XChartPanel<CategoryChart>(lc.get(3));
		
		List<XYChart> ls = gr.buildSummaryScatterCharts(solutions);
		XChartPanel<XYChart> p10 = new XChartPanel<XYChart>(ls.get(0));
		XChartPanel<XYChart> p11 = new XChartPanel<XYChart>(ls.get(1));
			
		contentPane.add(p1,"cell 0 0"); contentPane.add(p10,"cell 1 0,align left");             contentPane.add(p11,"cell 2 0,align left");
		contentPane.add(p2,"cell 0 1"); contentPane.add(expTag,"cell 1 1,align left,gaptop 10");          contentPane.add(solLevelButton,"cell 2 1,align left,gaptop 10");
		contentPane.add(p3,"cell 0 2"); contentPane.add(tScroll,"span 2 3,align left");/*contentPane.add(tScroll,"span 1 4");*/
		contentPane.add(p4,"cell 0 3");	
	}

	private void initializeGUI() {

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(5, 0, W, H);
		setMaximumSize(new Dimension(W, H));
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.window);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		JScrollPane scrollPane = new JScrollPane(contentPane);
		getContentPane().add(scrollPane);
		contentPane.setLayout(new MigLayout("", "[]80[]10[]", "[][][][]"));
//		contentPane.setLayout(new MigLayout("", "[]200[grow, center]", "[center][center][center][center][center][center]"));

	}

}
