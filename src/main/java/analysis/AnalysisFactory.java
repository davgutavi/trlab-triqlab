package analysis;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import analysis.biological.BIOQ;
import analysis.correlation.CorrelationAnalysis;
import analysis.correlation.PEQ;
import analysis.correlation.SPQ;
import analysis.graphical.GRQ;
import analysis.graphical.GRQevaluator;
import analysis.graphical.MslEvaluator;
import general.Tricluster;
import input.datasets.Biological;
import input.datasets.Common;
import input.laboratory.AnalysisResources;
import input.laboratory.CommonAnalysisResources;
import input.laboratory.Options;
import input.laboratory.ReducedAnalysisResources;
import utils.WorkFlowUtilities;

/**
 * AnalysisFactory Class
 * 
 * @author David Gutiérrez-Avilés
 *
 */
public class AnalysisFactory {

	private static final Logger LOG = LoggerFactory.getLogger(AnalysisFactory.class);

	public static List<Experiment> getAllExperiments(List<CommonAnalysisResources> resources, Options options)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {

		List<Experiment> experiments = new ArrayList<Experiment>();

		for (CommonAnalysisResources rs : resources) {

			if (rs instanceof AnalysisResources) {

				AnalysisResources ar = (AnalysisResources) rs;

				Experiment e = getExperiment(rs.getSolutions(), options, (ar.getControl()).getDataset(),
						ar.getExperimentAlias());

				experiments.add(e);

			}

			else if (rs instanceof ReducedAnalysisResources) {

				ReducedAnalysisResources rd = (ReducedAnalysisResources) rs;

				Experiment e = getExperiment(rd.getSolutions(), options, rd.getDataset(), rd.getExperimentAlias());

				experiments.add(e);

			}

		}

		return experiments;

	}

	public static Experiment getExperiment(List<Tricluster> triclusters, Options options, Common dataset,
			String experimentName) throws InstantiationException, IllegalAccessException, ClassNotFoundException {

		Experiment r = null;

		char type = WorkFlowUtilities.getExperimentTypeFromDataset(dataset);

		List<Solution> solutions = buildSolutions(triclusters);

		// GRQ

		GRQevaluator method = new MslEvaluator();

		method.loadParameters(dataset.getDataset());

		GRQ grqAnalysis = new GRQ(method, solutions);

		// PEQ, SPQ

		CorrelationAnalysis correlation = new CorrelationAnalysis(dataset.getDataset());

		PEQ peqAnalysis = new PEQ(correlation, solutions);

		SPQ spqAnalysis = new SPQ(correlation, solutions);

		// BIOQ, TRIQ

		if (type == 'b') {

			BIOQ bioqAnalysis = new BIOQ(solutions, (Biological) dataset, options);

			TRIQ triqAnalysis = new Btriq(solutions, grqAnalysis, peqAnalysis, spqAnalysis, bioqAnalysis,
					options.getGraphical(), options.getPearson(), options.getSpearman(), options.getBiological());

			r = new Experiment(solutions, type, experimentName, dataset, triqAnalysis);

		} else {

			LOG.debug("COMMON ANALYSIS");

			TRIQ triqAnalysis = new Ctriq(solutions, grqAnalysis, peqAnalysis, spqAnalysis, options.getCgrq(),
					options.getCpeq(), options.getCspq());

			r = new Experiment(solutions, type, experimentName, dataset, triqAnalysis);

		}

		return r;

	}

	// OPTricluster: TRIQ analysis functions

	public static Experiment getOPTexperiment(List<Tricluster> triclusters, Options options, Biological dataset,
			String experimentName) throws InstantiationException, IllegalAccessException, ClassNotFoundException {

		Experiment r = null;

		char type = WorkFlowUtilities.getExperimentTypeFromDataset(dataset);

		List<Solution> solutions = buildSolutions(triclusters);

		// GRQ

		GRQevaluator method = new MslEvaluator();

		method.loadParameters(dataset.getDataset());

		GRQ grqAnalysis = new GRQ(method, solutions);

		// PEQ, SPQ

		CorrelationAnalysis correlation = new CorrelationAnalysis(dataset.getDataset());

		PEQ peqAnalysis = new PEQ(correlation, solutions);

		SPQ spqAnalysis = new SPQ(correlation, solutions);

		BIOQ bioqAnalysis = new BIOQ(solutions, dataset, options);

		TRIQ triqAnalysis = new Btriq(solutions, grqAnalysis, peqAnalysis, spqAnalysis, bioqAnalysis,
					options.getGraphical(), options.getPearson(), options.getSpearman(), options.getBiological());

		r = new Experiment(solutions, type, experimentName, dataset, triqAnalysis);

		return r;

	}

	private static List<Solution> buildSolutions(List<Tricluster> triclusters) {

		List<Solution> r = new LinkedList<Solution>();

		int index = 1;

		for (Tricluster tri : triclusters) {

			r.add(new Solution(index, tri));

			index++;

		}

		return r;

	}

}
