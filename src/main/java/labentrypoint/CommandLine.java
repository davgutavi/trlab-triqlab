package labentrypoint;

import java.io.File;
import java.io.FileNotFoundException;
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
import input.laboratory.OPTsolBatch;
import input.laboratory.Options;
import input.laboratory.WrongOptionsException;
import input.workflows.LabListRun;
import input.workflows.LabOneSolRun;
import input.workflows.LabOptListRun;
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

	private static Map<String, String> cmdLine = null;

	private static final String COMAND_OPT = "-opt";

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

			// Get first argument and input path
			
			cmdLine = convertToMap(args);

			LOG.debug(cmdLine.toString());

			String first = args[0];

			String pathToInput = args[1];

			// Process pathToOutput (-o) option

			String outPath = cmdLine.get(OPTION_OUTPUT);

			File outFolder = null;

			String outName = null;

			if (outPath == null) {

				File aux = new File(pathToInput);

				outFolder = new File(aux.getParent());

				outName = TextUtilities.getFileNameWithoutExtension(aux.getName());

			} 
			
			else {

				File aux = new File(outPath);

				outFolder = new File(aux.getParent());

				outName = TextUtilities.getFileNameWithoutExtension(aux.getName());

			}
			
			LOG.info("Output folder = " + outFolder.getAbsolutePath());

			LOG.info("Output name = " + outName);

			// Process pathToOptionFile (-op) option

			String optionsPath = cmdLine.get(OPTION_OPTIONS);

			Options options = null;

			if (optionsPath == null) {

				options = new Options(outFolder, outName);

				LOG.info("Default options");

			} else {

				options = InputFacade.buildOptions(optionsPath, outFolder, outName);

				LOG.info("Options path = " + optionsPath);

			}
			
			// Process  operation mode: single mode (-i), opt mode (-opt) and multiple mode (-l)

			switch (first.toLowerCase()) {

			case (COMAND_I): {
				singleMode(pathToInput, options);
				break;
			}

			case (COMAND_OPT): {
				optMode(options);
				break;
			}

			case (COMAND_L): {
				multipleMode(pathToInput, options, outFolder);
				break;
			}

			}

		} catch (WrongOptionsException e) {
			e.printStackTrace();
		} catch (InvalidImplementationException e) {
			e.printStackTrace();
		} catch (WrongContolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		LOG.info("DONE!");

	}
	
	//****************************************Private methods
	
	

	
	//SINGLE MODE PROCESS
	/**
	 * Single mode.
	 * 
	 * @param pathToInput
	 * @param options
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws WrongContolException
	 * @throws InvalidImplementationException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws InterruptedException
	 */
	private static void singleMode(String pathToInput, Options options)
			throws FileNotFoundException, IOException, WrongContolException, InvalidImplementationException,
			InstantiationException, IllegalAccessException, ClassNotFoundException, InterruptedException {

		LOG.info("Single mode");

		LabOneSolRun wf = new LabOneSolRun();

		AnalysisResources res = wf.loadSolution(pathToInput);

		String aOpt = cmdLine.get(OPTION_A);

		Experiment exp = null;

		if (aOpt != null) {

			LOG.info("Analysis");

			exp = Facade.buildAnalysis(res, options, true);

		}

		String fOpt = cmdLine.get(OPTION_F);

		if (fOpt != null) {

			LOG.info("Files");

			Facade.buildFiles(res, options, exp);

		}

	}

	
	//OPT MODE PROCESS
	/**
	 * Opt mode.
	 * @param pathToInput
	 * @param options
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws WrongContolException
	 * @throws InvalidImplementationException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws InterruptedException
	 */
	private static void optMode(Options options)
			throws FileNotFoundException, IOException, WrongContolException, InvalidImplementationException,
			InstantiationException, IllegalAccessException, ClassNotFoundException, InterruptedException {

		LOG.info("OPT mode");

		LabOptListRun wf = new LabOptListRun();
				
		String inputFolder = cmdLine.get("-opt_1");
		String datasetName = cmdLine.get("-opt_2");
		
		LOG.debug("Input folder = "+inputFolder);
		LOG.debug("Dataset name = "+datasetName);

		// OPTsolBatch res = wf.listLoad(pathToInput, datasetName);
		//
		//
		//
		//
		// String aOpt = cmdLine.get(OPTION_A);
		//
		// Experiment exp = null;
		//
		// if (aOpt != null) {
		//
		// LOG.info("Analysis");
		//
		// exp = Facade.buildAnalysis(res, options, true);
		//
		// }
		//
		// String fOpt = cmdLine.get(OPTION_F);
		//
		// if (fOpt != null) {
		//
		// LOG.info("Files");
		//
		// Facade.buildFiles(res, options, exp);
		//
		// }

	}
	
	/**
	 * Multiple mode.
	 * @param pathToInput
	 * @param options
	 * @param outFolder
	 * @throws IOException
	 * @throws WrongContolException
	 * @throws InvalidImplementationException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws InterruptedException
	 * @throws WrongOptionsException
	 */
		private static void multipleMode(String pathToInput, Options options, File outFolder)
				throws IOException, WrongContolException, InvalidImplementationException, InstantiationException,
				IllegalAccessException, ClassNotFoundException, InterruptedException, WrongOptionsException {

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

			List<Experiment> exps = Facade.buildMultipleAnalysis(res, options);

			String fOpt = cmdLine.get(OPTION_F);

			if (fOpt != null) {

				LOG.info("Files");

				Facade.buildEveryFiles(res, exps, outFolder.getAbsolutePath());

			}

		}


		/**
		 * Convert input params to map
		 * @param args
		 * @return
		 */
	private static Map<String, String> convertToMap(String[] args) {

		Map<String, String> r = new HashMap<String, String>();

		int i = 0;

		while (i < args.length) {

			if (args[i].equalsIgnoreCase(OPTION_A) || args[i].equalsIgnoreCase(OPTION_F)
					|| args[i].equalsIgnoreCase(OPTION_BIO_ANALYSIS) || args[i].equalsIgnoreCase(OPTION_COM_ANALYSIS)) {

				r.put(args[i], "yes");

				i++;

			} 
			
			else if(args[i].equalsIgnoreCase(COMAND_OPT)) {
				
				r.put(args[i]+"_1", args[i + 1]);
				
				r.put(args[i]+"_2", args[i + 2]);

				i = i + 3;
				
			}
			else {

				r.put(args[i], args[i + 1]);

				i = i + 2;

			}

		}

		return r;
	}

}
