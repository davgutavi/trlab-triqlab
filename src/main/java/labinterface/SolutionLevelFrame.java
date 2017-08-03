package labinterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import analysis.Experiment;
import analysis.Solution;

@SuppressWarnings("serial")
public class SolutionLevelFrame extends JFrame {



	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(SolutionLevelFrame.class);

	private JPanel contentPane;
	private JPanel expDigestPanel;
	private JPanel solutionPanel;
	private SolutionButtonMenu buttonMenu;
	
	private Experiment experiment;
	
	
	
	
	/**
	 * Create the frame.
	 */
	public SolutionLevelFrame(Experiment exp) {
		
		experiment = exp;
		
		initializeGUI ();

		
	}
	
	
	private void initializeGUI (){
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		int w = 1500;
		int h = 800;
		
		setBounds(5, 0, w, h);
		
		setMaximumSize(new Dimension(w, h));
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.setMaximumSize(new Dimension(w, h));
		
		JScrollPane scrollPane = new JScrollPane(contentPane);
		scrollPane.setMaximumSize(new Dimension(w, h));
		getContentPane().add(scrollPane);
		
		expDigestPanel = new JPanel();
		contentPane.add(expDigestPanel, BorderLayout.NORTH);
		
		
		int unitW = 300;
		 int unitL = 400;
		solutionPanel = new JPanel();
		
		solutionPanel.setMaximumSize(new Dimension(unitW,unitL));
		contentPane.add(solutionPanel, BorderLayout.CENTER);
		
		 
				
		List<Solution> sols = experiment.getSolutions();
		
		for (Solution s:sols){
			
			if (experiment.getAnalysisType()=='b'){
				BioSolutionPanel sp = new BioSolutionPanel(s,experiment.getDataset());
				solutionPanel.add(sp);
			}
			else if  (experiment.getAnalysisType()=='c'){
				CommonSolutionPanel sp = new CommonSolutionPanel(s,experiment.getDataset());
				solutionPanel.add(sp);
			
			}
									
		}
		
		buttonMenu = new SolutionButtonMenu(solutionPanel,((Solution) experiment.getValue("bestsolution")).getIndex());
		contentPane.add(buttonMenu, BorderLayout.WEST);
		
		
	}
	
	
	public void displaySolution (int i){
		setVisible(true);
		buttonMenu.showSolution(i);
	}

}
