package labinterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import analysis.Experiment;
import input.algorithm.InvalidImplementationException;
import input.algorithm.WrongContolException;
import input.laboratory.AnalysisResources;
import input.laboratory.Options;
import input.laboratory.WrongOptionsException;
import input.workflows.LabOneSolRun;
import labentrypoint.Facade;
import utils.TextUtilities;

@SuppressWarnings("serial")
public class MultipleTaskConsole extends JPanel {
	
	private static final Logger LOG = LoggerFactory.getLogger(MultipleTaskConsole.class);
	private JTextArea console;
	private MultipleAnalysisTask task ;
	
	
	public MultipleTaskConsole (){
		super(new BorderLayout());
		setUI ();
	}
	
	private void setUI (){
		
		
		
		console  = new JTextArea();
		console.setBackground(SystemColor.window);
		console.setForeground(Color.BLUE);
		console.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 14));
		console.setVisible(true);
			
		JScrollPane spanel = new JScrollPane(console);
		spanel.setPreferredSize(new Dimension(200,800));
		
		add(spanel, BorderLayout.CENTER);
		
	}	
	
	public void launch(String selectedPath) {
		console.setText("");
		task = new MultipleAnalysisTask(selectedPath);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));	
				task.execute();
				
			}
		});
	}
	

	
	
	



	
	private class MultipleAnalysisTask extends SwingWorker<List<List<Experiment>>, TaskStatus> {

		private String selectedPath;

		public MultipleAnalysisTask(String selectedPath) {
			this.selectedPath = selectedPath;
		}

		@Override
		protected List<List<Experiment>> doInBackground() throws Exception {

			File folder = new File(selectedPath);
			
			File[] files = folder.listFiles();
						
			List<List<Experiment>> r = new ArrayList<List<Experiment>>(2);

			List<Experiment> bioExpGroup = new LinkedList<Experiment>();

			List<Experiment> comExpGroup = new LinkedList<Experiment>();
			
			int progress = 0;
			double max = files.length;
			
//			setProgress(progress);
			publish(new TaskStatus(progress,"Loaded folder: "+selectedPath+"\nProcessing files\n\n"));
			
			
			try {

				for (int i = 0; i < files.length; i++) {

					progress = (int) (((i+1)/max)*100.0);
					
					File aux = files[i];

					if (checkSolFile(aux)) {

						LabOneSolRun wf = new LabOneSolRun();
						AnalysisResources cSol = wf.loadSolution(aux.getAbsolutePath());

						File outFolder = new File(aux.getParent());
						String outName = TextUtilities.getFileNameWithoutExtension(aux.getName());
						Options opts = new Options(outFolder, outName);
						
						
//						setProgress(progress);
						publish(new TaskStatus(progress,"File: "+aux.getAbsolutePath()+"\nAnalysing Results #" + i + " Name = " + cSol.getExperimentAlias()+"\n"));
		

						Experiment exp = Facade.buildAnalysis(cSol, opts, false);

						if (cSol.getAnalysisType() == 'b') {
							bioExpGroup.add(exp);
						} else if (cSol.getAnalysisType() == 'c') {
							comExpGroup.add(exp);
						}
						
	
					}
				}

				r.add(bioExpGroup);
				r.add(comExpGroup);

			}

			catch (IOException e1) {
				e1.printStackTrace();
				publish(new TaskStatus(progress,e1.getMessage()));
			} catch (WrongContolException e1) {
				e1.printStackTrace();
				publish(new TaskStatus(progress,e1.getMessage()));
			} catch (InvalidImplementationException e1) {
				e1.printStackTrace();
				publish(new TaskStatus(progress,e1.getMessage()));
			} catch (WrongOptionsException e1) {
				e1.printStackTrace();
				publish(new TaskStatus(progress,e1.getMessage()));
			} catch (InstantiationException e1) {
				e1.printStackTrace();
				publish(new TaskStatus(progress,e1.getMessage()));
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
				publish(new TaskStatus(progress,e1.getMessage()));
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
				publish(new TaskStatus(progress,e1.getMessage()));
			} catch (InterruptedException e1) {
				e1.printStackTrace();
				publish(new TaskStatus(progress,e1.getMessage()));
			}

			return r;
		}
		
		@Override
		protected void process(List<TaskStatus> status) {
			
			TaskStatus st = status.get(status.size()-1);	
			String line = "";
			if (st.getProgress()!=100){
				line = st.getProgress()+" %  ->  "+st.getDescription();
			}
			else{
				line = st.getDescription();
				
			}
			LOG.debug(line);
			
			console.append(line);
		}

		
		@Override
		protected void done() {
			
			List<List<Experiment>> aux;
			try {
				
				aux = get();
				
				List<Experiment> bioExpGroup = aux.get(0);
				
				List<Experiment> comExpGroup = aux.get(1);							
				
				String check = "";
				
				
				check+="\n\nBiological experiments:\n";		
				
				
				for (Experiment res : bioExpGroup) {
					check+="    Bio Results #" + bioExpGroup.indexOf(res) + ": " + res.getExperimentName()+"\n";
				}

				check+="\n\nCommon experiments:\n";

				for (Experiment res : comExpGroup) {
					check+="    Com Results #" + comExpGroup.indexOf(res) + ": " + res.getExperimentName()+"\n";
				}
				
				check+="\nDone";
				
				LOG.debug("Done");
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));				
				publish(new TaskStatus(100,check));
				
				
				
				MultipleAnalysisExpLevelFrame expFrame = new MultipleAnalysisExpLevelFrame(bioExpGroup,comExpGroup);
				
				expFrame.setVisible(true);
			
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					
		}

		private boolean checkSolFile(File f) {

			boolean r = false;

			if (!f.isHidden() && f.isFile()) {

				String ext = TextUtilities.getFileExtension(f.getAbsolutePath());

				if (ext.equalsIgnoreCase("sol")) {

					r = true;
				}
			}

			return r;

		}

	}





	

}
