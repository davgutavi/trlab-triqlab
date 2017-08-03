package labinterface;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.knowm.xchart.BubbleChart;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.XChartPanel;

import analysis.Experiment;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class ExpCompGraphFrame extends JFrame {

	private JPanel contentPane;
	private List<Experiment> experiments;

	
	public ExpCompGraphFrame(List<Experiment> experiments) {
		
		this.experiments = experiments;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(20, 20, 1400, 420);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[fill]10[fill]10[fill]40[fill]", "[fill]"));
		
		buildCharts ();
		
		
	}

	
	private void buildCharts (){
		
		GraphicsBuilder gr = GraphicsBuilder.getInstance();
				
		List<CategoryChart> lc = gr.buildCompCategoryCharts(experiments);
		XChartPanel<CategoryChart> p1 = new XChartPanel<CategoryChart>(lc.get(0));
		XChartPanel<CategoryChart> p2 = new XChartPanel<CategoryChart>(lc.get(1));
		XChartPanel<CategoryChart> p3 = new XChartPanel<CategoryChart>(lc.get(2));
		
		BubbleChart c1 = gr.buildBubbleChart(experiments);
		XChartPanel<BubbleChart> p4 = new XChartPanel<BubbleChart>(c1);
	
		contentPane.add(p1,"cell 0 0");
		contentPane.add(p2,"cell 1 0");
		contentPane.add(p3,"cell 2 0");
		contentPane.add(p4,"cell 3 0");
	
	
	
	
	
	}
	
	
	
}
