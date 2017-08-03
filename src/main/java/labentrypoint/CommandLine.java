package labentrypoint;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import analysis.Experiment;
import input.InputFacade;
import input.algorithm.InvalidImplementationException;
import input.algorithm.WrongContolException;
import input.laboratory.AnalysisResources;
import input.laboratory.CommonAnalysisResources;
import input.laboratory.Options;
import input.laboratory.WrongOptionsException;
import input.workflows.LabListRun;
import input.workflows.LabOneSolRun;
import utils.TextUtilities;

/**
 * CommandLine is a class which provides an entry point to <b>TriGen
 * Framework</b> by a command line manner.
 * 
 * @author David Gutiérrez-Avilés
 * @see Gui
 */

public class CommandLine {

	private static final Logger LOG = LoggerFactory.getLogger(CommandLine.class);

	private static final String COMAND_I = "-i";

	private static final String COMAND_L = "-l";

	private static final String OPTION_A = "-a";

	private static final String OPTION_F = "-f";

	private static final String OPTION_OUTPUT = "-o";

	private static final String OPTION_OPTIONS = "-op";

	private static final String OPTION_BIO_ANALYSIS = "-b";

	private static final String OPTION_COM_ANALYSIS = "-c";

	/**
	 * Main method for command line execution method.
	 * 
	 * @param args
	 *            Call arguments for command line execution method.
	 */
	public static void main(String[] args) {

			LOG.info("START ANALYSIS");

			try {

				Map<String, String> cmdLine = convertToMap(args);

				LOG.debug(cmdLine.toString());

				String first = args[0];

				String pathToInput = args[1];

				// -o

				String outPath = cmdLine.get(OPTION_OUTPUT);

				File outFolder = null;

				String outName = null;

				if (outPath == null) {

					File aux = new File(pathToInput);

					outFolder = new File(aux.getParent());

					outName = TextUtilities.getFileNameWithoutExtension(aux.getName());

				} else {

					File aux = new File(outPath);

					outFolder = new File(aux.getParent());

					outName = TextUtilities.getFileNameWithoutExtension(aux.getName());

				}

				LOG.info("Output folder = " + outFolder.getAbsolutePath());
				LOG.info("Output name = " + outName);

				// -op

				String opPath = cmdLine.get(OPTION_OPTIONS);

				Options opts = null;

				if (opPath == null) {

					opts = new Options(outFolder, outName);

					LOG.info("Default options");

				} else {

					opts = InputFacade.buildOptions(opPath, outFolder, outName);

					LOG.info("Options path = " + opPath);

				}

				if (first.equalsIgnoreCase(COMAND_I)) {

					LOG.info("Single mode");

					LabOneSolRun wf = new LabOneSolRun();

					AnalysisResources res = wf.loadSolution(pathToInput);

					String aOpt = cmdLine.get(OPTION_A);

					Experiment exp = null;

					if (aOpt != null) {

						LOG.info("Analysis");

						exp = Facade.buildAnalysis(res, opts, true);

					}

					String fOpt = cmdLine.get(OPTION_F);

					if (fOpt != null) {

						LOG.info("Files");

						Facade.buildFiles(res, opts, exp);

					}

				} else if (first.equalsIgnoreCase(COMAND_L)) {

					LOG.info("Multiple mode");

					char anType = 'b';

					boolean checked = false;

					String bOpt = cmdLine.get(OPTION_BIO_ANALYSIS);

					if (bOpt != null && !checked) {

						checked = true;

						anType = 'b';

					}

					String cOpt = cmdLine.get(OPTION_COM_ANALYSIS);

					if (cOpt != null && !checked) {

						checked = true;

						anType = 'c';

					}

					LabListRun wf = new LabListRun();

					List<CommonAnalysisResources> res = wf.listLoad(pathToInput, anType);

					List<Experiment> exps = Facade.buildMultipleAnalysis(res, opts);

					String fOpt = cmdLine.get(OPTION_F);

					if (fOpt != null) {

						LOG.info("Files");

						Facade.buildEveryFiles(res, exps, outFolder.getAbsolutePath());

					}

				}

			} catch (IOException | WrongContolException | InvalidImplementationException | WrongOptionsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			LOG.info("DONE!");

		

	}

	private static Map<String, String> convertToMap(String[] args) {

		Map<String, String> r = new HashMap<String, String>();

		int i = 0;

		while (i < args.length) {

			if (args[i].equalsIgnoreCase(OPTION_A) || args[i].equalsIgnoreCase(OPTION_F)
					|| args[i].equalsIgnoreCase(OPTION_BIO_ANALYSIS) || args[i].equalsIgnoreCase(OPTION_COM_ANALYSIS)) {

				r.put(args[i], "yes");

				i++;
			} else {

				r.put(args[i], args[i + 1]);

				i = i + 2;

			}

		}

		return r;
	}

}
