package labinterface;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

@SuppressWarnings("serial")
public class GuiSolution extends JTabbedPane {

	TriGraphs tricluster;
	
	private JPanel gctPanel;
	
	private JPanel gtcPpanel;
	private JPanel tgcPanel;
	
	/**
	 * Create the frame.
	 */
	public GuiSolution(TriGraphs tri) {
		
		super(JTabbedPane.TOP);
		
		tricluster = tri;
		
		setBounds(100, 100, 523, 457);
		
		int with  = 400;
		int heigh = 200;
		
		gctPanel =  new JPanel();
		gctPanel.setLayout(new BoxLayout(gctPanel, BoxLayout.Y_AXIS));
		
		for (XYChart c:tricluster.getGCTcharts()){
			JPanel pnlChart = new XChartPanel<XYChart>(c);  
			
			pnlChart.setMinimumSize(new Dimension(with,heigh));			
			pnlChart.setMaximumSize(new Dimension(with,heigh));
			gctPanel.add(pnlChart);
			gctPanel.validate();
		}

		add(gctPanel, "GCT view");
		
		
	
		
		gtcPpanel = new JPanel();
		gtcPpanel.setLayout(new BoxLayout(gtcPpanel, BoxLayout.Y_AXIS));
		
		for (XYChart c:tricluster.getGTCcharts()){
			JPanel pnlChart = new XChartPanel<XYChart>(c);  
			pnlChart.setMinimumSize(new Dimension(with,heigh));			
			pnlChart.setMaximumSize(new Dimension(with,heigh));
			gtcPpanel.add(pnlChart);
			gtcPpanel.validate();
		}

		add(gtcPpanel, "GTC view");
		
		tgcPanel= new JPanel();
		tgcPanel.setLayout(new BoxLayout(tgcPanel, BoxLayout.Y_AXIS));
		
		for (XYChart c:tricluster.getTGCcharts()){
			JPanel pnlChart = new XChartPanel<XYChart>(c);  
			pnlChart.setMinimumSize(new Dimension(with,heigh));			
			pnlChart.setMaximumSize(new Dimension(with,heigh));
			tgcPanel.add(pnlChart);
			tgcPanel.validate();
		}

		add(tgcPanel, "TGC view");
		
		
		setVisible(false);
	
	}

}
