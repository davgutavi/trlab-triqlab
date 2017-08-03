package labinterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.io.File;
import java.io.IOException;
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
public class SingleTaskConsole extends JPanel {

	private static final Logger LOG = LoggerFactory.getLogger(SingleTaskConsole.class);
	private JTextArea console;
	private SingleAnalysisTask task;

	public SingleTaskConsole() {
		super(new BorderLayout());
		setUI();
	}

	private void setUI() {

		console = new JTextArea();
		console.setBackground(SystemColor.window);
		console.setForeground(Color.BLUE);
		console.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 14));
		console.setVisible(true);

		JScrollPane spanel = new JScrollPane(console);
		spanel.setPreferredSize(new Dimension(200, 800));

		add(spanel, BorderLayout.CENTER);

	}

	public void launch(String selectedPath) {
		console.setText("");
		task = new SingleAnalysisTask(selectedPath);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				task.execute();

			}
		});
	}

	private class SingleAnalysisTask extends SwingWorker<Experiment, TaskStatus> {

		private String selectedPath;

		public SingleAnalysisTask(String selectedPath) {
			this.selectedPath = selectedPath;
		}

		@Override
		protected Experiment doInBackground() throws Exception {

			int progress = 0;

			LabOneSolRun wf = new LabOneSolRun();

			Experiment r = null;

			try {
				
//				publish(new TaskStatus(progress, "Loading solution file: "+selectedPath));
				
				if (checkSolFile(selectedPath)) {

					AnalysisResources currentSolutions = wf.loadSolution(selectedPath);

					publish(new TaskStatus(progress, "\n["+selectedPath+"] \nSolution loaded \nBuilding default options..."));

					File aux = new File(selectedPath);

					File outFolder = new File(aux.getParent());

					String outName = TextUtilities.getFileNameWithoutExtension(aux.getName());

					Options opts = new Options(outFolder, outName);
					
					LOG.info("Loaded: "+selectedPath);
										
					publish(new TaskStatus(progress, "\n["+selectedPath+"] \nDefault options built\nComputing experiment analysis..."));

					r = Facade.buildAnalysis(currentSolutions, opts, false);

					publish(new TaskStatus(progress, "\n["+selectedPath+"] \nExperiment analysis done\nShowing up the results..."));

				}

				else {

					publish(new TaskStatus(progress, "\n["+selectedPath+"] \nThis is not an experiment file"));

				}

			} catch (InstantiationException e1) {
				e1.printStackTrace();
				publish(new TaskStatus(progress, e1.getMessage()));
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
				publish(new TaskStatus(progress, e1.getMessage()));
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
				publish(new TaskStatus(progress, e1.getMessage()));
			} catch (InterruptedException e1) {
				e1.printStackTrace();
				publish(new TaskStatus(progress, e1.getMessage()));
			} catch (WrongOptionsException e1) {
				e1.printStackTrace();
				publish(new TaskStatus(progress, e1.getMessage()));
			} catch (IOException e1) {
				e1.printStackTrace();
				publish(new TaskStatus(progress, e1.getMessage()));
			} catch (WrongContolException e1) {
				e1.printStackTrace();
				publish(new TaskStatus(progress, e1.getMessage()));
			} catch (InvalidImplementationException e1) {
				e1.printStackTrace();
				publish(new TaskStatus(progress, e1.getMessage()));
			}

			return r;
		}

		@Override
		protected void process(List<TaskStatus> status) {

			TaskStatus st = status.get(status.size() - 1);
			String line = st.getDescription();
			LOG.debug(line);
			console.append(line);
		}

		@Override
		protected void done() {

			Experiment exp;
			try {

				exp = get();

				if (exp != null) {

					if (exp.getAnalysisType() == 'b') {
						LOG.debug("" + exp.getAnalysisType());
						publish(new TaskStatus(100, "\nAnalysis type: biological\n"));
						BioExperimentLevelFrame exp_l = new BioExperimentLevelFrame(exp);
						exp_l.setVisible(true);
					} else if (exp.getAnalysisType() == 'c') {
						LOG.debug("" + exp.getAnalysisType());
						publish(new TaskStatus(100, "\nAnalysis type: common\n"));
						CommonExperimentLevelFrame exp_l = new CommonExperimentLevelFrame(exp);
						exp_l.setVisible(true);
					}
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}

		}

		private boolean checkSolFile(String path) {

			boolean r = false;

			String ext = TextUtilities.getFileExtension(path);

			if (ext.equalsIgnoreCase("sol")) {

				r = true;

			}

			return r;

		}

	}

}
