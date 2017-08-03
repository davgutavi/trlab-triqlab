package labinterface;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import interfaceutils.PathPicker;
import net.miginfocom.swing.MigLayout;

public class MultipleAnalysisApp {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(MultipleAnalysisApp.class);

	public JFrame frame;
	private JTextField txtLaboratoryApp;
	

	private MultipleTaskConsole console;
	
	private PathPicker inputFolder;
	private JButton btnAnalyse;

	/**
	 * Create the application.
	 */
	public MultipleAnalysisApp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 770, 700);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new MigLayout("", "[800.00,fill]", "[fill][50.00,fill][]"));

		txtLaboratoryApp = new JTextField();
		txtLaboratoryApp.setPreferredSize(new Dimension(50, 50));
		txtLaboratoryApp.setBorder(null);
		txtLaboratoryApp.setForeground(SystemColor.controlHighlight);
		txtLaboratoryApp.setHorizontalAlignment(SwingConstants.CENTER);
		txtLaboratoryApp.setBackground(SystemColor.window);
		txtLaboratoryApp.setEditable(false);
		txtLaboratoryApp.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 30));
		txtLaboratoryApp.setText("TrLab - Multiple Analysis");
		txtLaboratoryApp.setColumns(10);
		frame.getContentPane().add(txtLaboratoryApp, "cell 0 0");

		inputFolder = new PathPicker(true, "Select a results folder");
		inputFolder.setBorder(null);
		frame.getContentPane().add(inputFolder, "cell 0 1");

		btnAnalyse = new JButton("Analyse");
		btnAnalyse.setMaximumSize(new Dimension(70, 40));
		btnAnalyse.setFont(new Font("Arial", Font.PLAIN, 12));
		inputFolder.add(btnAnalyse);

		
		console = new MultipleTaskConsole ();
		JScrollPane scrollMessage = new JScrollPane(console);
		frame.getContentPane().add(scrollMessage, "cell 0 3");
		
		
		
		btnAnalyse.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				
				console.launch(inputFolder.getSelectedPath());
				

			}

		});

	}



}
