package labinterface;

import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import analysis.Experiment;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class MultipleAnalysisExpLevelFrame extends JFrame {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(MultipleAnalysisExpLevelFrame.class);

	private JPanel contentPane;
	
	private List<Experiment> biological;
	private List<Experiment> common;
	private List<Experiment> allExperiments;
	

	public MultipleAnalysisExpLevelFrame(List<Experiment> bio, List<Experiment> com) {
		
		
		biological = bio;
		common = com;
		allExperiments = new LinkedList<Experiment> ();
		allExperiments.addAll(biological);
		allExperiments.addAll(common);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(50, 50, 1400, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[]10[]10[]", "[][]"));
		
		
		
		JTable bioTable = new JTable(new ExpCompTableModel(biological));
		bioTable.setAutoResizeMode( JTable.AUTO_RESIZE_ALL_COLUMNS);
		bioTable.setFont(new Font("Monospaced", Font.PLAIN, 10));
		bioTable.setFillsViewportHeight(true);
		
		
		
//		TableColumnModel columnModel = bioTable.getColumnModel();
//		columnModel.getColumn(0).setMinWidth(90);
//		columnModel.getColumn(1).setMinWidth(320);
//		columnModel.getColumn(2).setMinWidth(110);
		
		
		bioTable.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent me) {
		        JTable table =(JTable) me.getSource();
		        Point p = me.getPoint();
		        int row = table.rowAtPoint(p);
		        int index = table.convertRowIndexToModel(row);
		        if (me.getClickCount() == 2) {
		        	 BioExperimentLevelFrame bioExpFrame = new BioExperimentLevelFrame(biological.get(index));
		        	 bioExpFrame.setVisible(true);
		        }
		    }
		});
		
		JTable comTable = new JTable(new ExpCompTableModel(common));
		comTable.setAutoResizeMode( JTable.AUTO_RESIZE_ALL_COLUMNS);
		comTable.setFont(new Font("Monospaced", Font.PLAIN, 10));
		comTable.setFillsViewportHeight(true);
		
		comTable.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent me) {
		        JTable table =(JTable) me.getSource();
		        Point p = me.getPoint();
		        int row = table.rowAtPoint(p);
		        int index = table.convertRowIndexToModel(row);
		        
		        if (me.getClickCount() == 2) {
		           	 CommonExperimentLevelFrame comExpFrame = new CommonExperimentLevelFrame(common.get(index));
		        	 comExpFrame.setVisible(true);
		        }
		    }
		});
		
		JTable allTable = new JTable(new ExpCompTableModel(allExperiments));
		allTable.setAutoResizeMode( JTable.AUTO_RESIZE_ALL_COLUMNS);
		allTable.setFont(new Font("Monospaced", Font.PLAIN, 10));
		allTable.setFillsViewportHeight(true);
		
		allTable.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent me) {
		        JTable table =(JTable) me.getSource();
		        Point p = me.getPoint();
		        int row = table.rowAtPoint(p);
		        int index = table.convertRowIndexToModel(row);
		        
		        if (me.getClickCount() == 2) {
		        	
		        	 Experiment e = allExperiments.get(index);		        	 
		        	 
		        	 if (e.getAnalysisType()=='b'){
		        		 BioExperimentLevelFrame bioExpFrame = new BioExperimentLevelFrame(e);
			        	 bioExpFrame.setVisible(true);		        		 
		        	 }
		        	 else if (e.getAnalysisType()=='c'){
		        		 CommonExperimentLevelFrame comExpFrame = new CommonExperimentLevelFrame(e);
			        	 comExpFrame.setVisible(true);
		        	 }		        	 
		        }
		    }
		});
		
		
		
		JButton bioButton = new JButton ("Biological Summary");
		
		bioButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ExpCompGraphFrame gr = new ExpCompGraphFrame(biological);
				gr.setVisible(true);
			}
		});
		
		JButton comButton = new JButton ("Common Summary");
		
		comButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ExpCompGraphFrame gr = new ExpCompGraphFrame(common);
				gr.setVisible(true);
			}
		});
		
		
		JButton allButton = new JButton ("Complete Summary");
		
		allButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ExpCompGraphFrame gr = new ExpCompGraphFrame(allExperiments);
				gr.setVisible(true);
			}
		});
		
		
		contentPane.add(bioButton,"cell 0 0");
		
		contentPane.add(comButton, "cell 1 0");
		
		contentPane.add(allButton, "cell 2 0");
		
		bioTable.setAutoCreateRowSorter(true);
		
		JScrollPane bioTScroll = new JScrollPane (bioTable);
		contentPane.add(bioTScroll,"cell 0 1");
		
		
		comTable.setAutoCreateRowSorter(true);
		
		JScrollPane comTScroll = new JScrollPane (comTable);
		contentPane.add(comTScroll,"cell 1 1");
		
		
		allTable.setAutoCreateRowSorter(true);
		
		JScrollPane allTScroll = new JScrollPane (allTable);
		contentPane.add(allTScroll,"cell 2 1");
		
	}

	
	
	
	
}
