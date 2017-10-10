package labentrypoint;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import labutils.GeneOntology;
import analysis.AnalysisFactory;
import analysis.Experiment;
import analysis.reports.ExpLevelReports;
import analysis.reports.SignificanceReports;
import analysis.reports.SolutionLevelReports;
import utils.FormatUtils;
import utils.OutTextFile;
import utils.SystemUtilities;
import general.Tricluster;
import input.algorithm.Control;
import input.datasets.Biological;
import input.datasets.Common;
import input.laboratory.AnalysisResources;
import input.laboratory.CommonAnalysisResources;
import input.laboratory.Options;
import input.laboratory.ReducedAnalysisResources;
import input.laboratory.WrongOptionsException;

public class Facade {
	
	
	private static final Logger LOG = LoggerFactory.getLogger(Facade.class);

	
	public static List<Experiment> buildMultipleAnalysis (List<CommonAnalysisResources> resources, Options options) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, InterruptedException{
				
		List<Experiment> r = AnalysisFactory.getAllExperiments(resources, options);
		
		String auxFilePath = (options.getOutFolder()).getAbsolutePath()+SystemUtilities.getFileSep()+options.getOutName();
		
		String csv_ext = SystemUtilities.getSystemProperty("lab_extension");
				
		for (Experiment e: r){
						
			if (e!=null){
				
				LOG.info(e.getExperimentName()+" -> Computing TRIQ analysis");
				
				e.computeAnalysis();
							
				
			}
						
		}		
		
		LOG.info("Building experiment level report");
		
		String eLreport = ExpLevelReports.getELreport(r);
				
		OutTextFile f1 = new OutTextFile(auxFilePath+"_"+SystemUtilities.getSystemProperty("exp_analysis_file")+"."+csv_ext);
		
		f1.print(eLreport);
		
		f1.close();	
		
		LOG.info("Building solution level report");
		
		String sLreport =  ExpLevelReports.getCompleteReport(r,(r.get(0)).getAnalysisType());
		
		OutTextFile f2 = new OutTextFile(auxFilePath+"_"+SystemUtilities.getSystemProperty("sol_analysis_file")+"."+csv_ext);
		
		f2.print(sLreport);
		
		f2.close();	
				
		return r;
				
	}
	

	
	public static Experiment buildAnalysis (AnalysisResources resources, Options options, boolean triqFile) 
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, InterruptedException{
		
		Experiment r = AnalysisFactory.getExperiment(resources.getSolutions(),options,
				(resources.getControl()).getDataset(),options.getOutName());
		
		String auxFilePath = (options.getOutFolder()).getAbsolutePath()+SystemUtilities.getFileSep()+options.getOutName();
		
		String csv_ext = SystemUtilities.getSystemProperty("lab_extension");
		
		if (r!=null){
			
			LOG.info("Computing TRIQ analysis");
			
			r.computeAnalysis();
			
			String triq_string = SolutionLevelReports.getSLSinglefile(r);
			
			if (triqFile){
			
				OutTextFile f1 = new OutTextFile(auxFilePath+"_"+SystemUtilities.getSystemProperty("triq_file")+"."+csv_ext);
				
				f1.print(triq_string);
			
				f1.close();	
				
			}
					
		}
		
		LOG.info("TRIQ done");
		
		return r;
					
	}

	public static void buildFiles (AnalysisResources resources, Options options, Experiment exp) 
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, InterruptedException{
		
		
		
		String auxFilePath = (options.getOutFolder()).getAbsolutePath()+SystemUtilities.getFileSep()+options.getOutName();
		
		String csv_ext = SystemUtilities.getSystemProperty("lab_extension");
		
		String gene_ext = SystemUtilities.getSystemProperty("gene_extension");
		
		LOG.info("Building coordinate and arranged files");
		
		String[] excell_strings = FormatUtils.getExcelStrings(resources.getSolutions(), ((resources.getControl()).getDataset()).getDataset());
		
		printFolder(excell_strings,(options.getOutFolder()).getAbsolutePath(),options.getOutName(),
				SystemUtilities.getSystemProperty("excel_folder"),SystemUtilities.getSystemProperty("excel_file"),csv_ext);
				
		String[] r_strings = FormatUtils.getRstrings(resources.getSolutions(), ((resources.getControl()).getDataset()).getDataset());
		
		printFolder(r_strings,(options.getOutFolder()).getAbsolutePath(),options.getOutName(),
				SystemUtilities.getSystemProperty("r_folder"),SystemUtilities.getSystemProperty("r_file"),csv_ext);
		
		
		 if (((resources.getControl()).getDataset()).getDatasetType() =='b'){
		
			LOG.info("Getting genes files");
			
			String[] genes_strings = GeneOntology.getGeneStrings(resources.getSolutions(), ((Biological)(resources.getControl()).getDataset()).getGeneNames());
			
			printFolder(genes_strings,(options.getOutFolder()).getAbsolutePath(),options.getOutName(),
					SystemUtilities.getSystemProperty("genes_folder"),SystemUtilities.getSystemProperty("genes_file"),gene_ext);
					
			if (exp!=null){
			
				LOG.info("Significance analysis");
				
				String sig_string = SignificanceReports.getSignificanceReportPA(exp);
				
				OutTextFile f2 = new OutTextFile(auxFilePath+"_"+SystemUtilities.getSystemProperty("significance_file")+"."+csv_ext);
				
				f2.print(sig_string);
				
				f2.close();
				
				String sig_stringn = SignificanceReports.getSignificanceReportP(exp);
				
				OutTextFile f2n = new OutTextFile(auxFilePath+"_"+SystemUtilities.getSystemProperty("significance_filen")+"."+csv_ext);
				
				f2n.print(sig_stringn);
				
				f2n.close();
									
			}		
								
		}
			
	}
		
	public static void buildCompleteResultsFiles (AnalysisResources results, String pathToFolder) 
			throws WrongOptionsException, InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, InterruptedException{
		
		List<Tricluster> solutions = results.getSolutions();
		
		Control control = results.getControl();
		
		Common dataset = control.getDataset();
		
		//String auxFilePath = pathToFolder +SystemUtilities.getFileSep()+control.getOutName();
								
		String auxFilePath = pathToFolder +SystemUtilities.getFileSep()+control.getOutName();
		
		String csv_ext = SystemUtilities.getSystemProperty("lab_extension");
		
		String gene_ext = SystemUtilities.getSystemProperty("gene_extension");
		
		Options defaultOpt = new Options(control.getOutFolder(), control.getOutName());
			
		Experiment analysis = AnalysisFactory.getExperiment(solutions,defaultOpt,dataset,control.getOutName());
			
					
		if (analysis!=null){
		
			LOG.info("Computing TRIQ analysis");
			
			analysis.computeAnalysis();
			
//			String triq_string = SolutionLevelReports.getSLfile(analysis);
			
			String triq_string = SolutionLevelReports.getSLSinglefile(analysis);
			
			OutTextFile f1 = new OutTextFile(auxFilePath+"_"+SystemUtilities.getSystemProperty("triq_file")+"."+csv_ext);
				
			f1.print(triq_string);
			
			f1.close();
			
			LOG.info("Building coordinate and arranged files");
			
			String[] excell_strings = FormatUtils.getExcelStrings(solutions, dataset.getDataset());
			
			printFolder(excell_strings,pathToFolder,control.getOutName(),
					SystemUtilities.getSystemProperty("excel_folder"),SystemUtilities.getSystemProperty("excel_file"),csv_ext);
					
//			LOG.info("R files");
					
			String[] r_strings = FormatUtils.getRstrings(solutions, dataset.getDataset());
			
			printFolder(r_strings,pathToFolder,control.getOutName(),
					SystemUtilities.getSystemProperty("r_folder"),SystemUtilities.getSystemProperty("r_file"),csv_ext);
			
			
			if (analysis.getAnalysisType() =='b'){
				
				
				LOG.info("Significance analysis");
				
				String sig_string = SignificanceReports.getSignificanceReportPA(analysis);
				
				OutTextFile f2 = new OutTextFile(auxFilePath+"_"+SystemUtilities.getSystemProperty("significance_file")+"."+csv_ext);
				
				f2.print(sig_string);
				
				f2.close();
				
				String sig_stringn = SignificanceReports.getSignificanceReportP(analysis);
				
				OutTextFile f2n = new OutTextFile(auxFilePath+"_"+SystemUtilities.getSystemProperty("significance_filen")+"."+csv_ext);
				
				f2n.print(sig_stringn);
				
				f2n.close();				
				
				LOG.info("Getting genes files");
				
				String[] genes_strings = GeneOntology.getGeneStrings(solutions, ((Biological)dataset).getGeneNames());
				
				printFolder(genes_strings,pathToFolder,control.getOutName(),
						SystemUtilities.getSystemProperty("genes_folder"),SystemUtilities.getSystemProperty("genes_file"),gene_ext);
							
			}		
					
		}
		
	}
	
	
	
	public static void buildEveryFiles (List<CommonAnalysisResources> resources, List<Experiment> experiments, String pathToFolder) throws InstantiationException, IllegalAccessException, ClassNotFoundException, WrongOptionsException, IOException, InterruptedException {
		
		
		for (CommonAnalysisResources r: resources){
			
			File res_dir = new File (pathToFolder,r.getExperimentAlias());
			
			res_dir.mkdirs();
			
			LOG.info(r.getExperimentAlias()+" -> building files in "+res_dir.getAbsolutePath());
						
			buildAllResultFiles(r,pathToFolder+SystemUtilities.getFileSep()+r.getExperimentAlias());
			
		}
		
		for (Experiment e: experiments){
			
			buildAllAnalysisFiles(e,pathToFolder+SystemUtilities.getFileSep()+e.getExperimentName());
			
		}
				
	}
	
	public static void buildAllAnalysisFiles (Experiment experiment, String pathToFolder) 
			throws WrongOptionsException, InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, InterruptedException{
		
		if (experiment!=null){
											
			String csv_ext = SystemUtilities.getSystemProperty("lab_extension");
									
			String triq_string = SolutionLevelReports.getSLfile(experiment);
			
			OutTextFile f1 = new OutTextFile(pathToFolder+SystemUtilities.getFileSep()+experiment.getExperimentName()+"_"+
					SystemUtilities.getSystemProperty("triq_file")+"."+csv_ext);
				
			f1.print(triq_string);
			
			f1.close();			
					
			if (experiment.getAnalysisType() =='b'){		
				
				String sig_string = SignificanceReports.getSignificanceReportPA(experiment);
				
				OutTextFile f2 = new OutTextFile(pathToFolder+SystemUtilities.getFileSep()+experiment.getExperimentName()+"_"+
						SystemUtilities.getSystemProperty("significance_file")+"."+csv_ext);
				
				f2.print(sig_string);
				
				f2.close();
				
				String sig_stringn = SignificanceReports.getSignificanceReportP(experiment);
				
				OutTextFile f2n = new OutTextFile(pathToFolder+SystemUtilities.getFileSep()+experiment.getExperimentName()+"_"+
						SystemUtilities.getSystemProperty("significance_filen")+"."+csv_ext);
				
				f2n.print(sig_stringn);
				
				f2n.close();
				
				
											
			}		
					
		}
		
	}
	
	public static void buildAllResultFiles (CommonAnalysisResources resource, String pathToFolder) 
			throws WrongOptionsException, InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, InterruptedException{
		

		List<Tricluster> solutions = resource.getSolutions();
		
		Common dataset = null;
		
		if (resource instanceof AnalysisResources){
				
			AnalysisResources ar = (AnalysisResources)resource;
				
			dataset = (ar.getControl()).getDataset();
				
		}
			
		else if (resource instanceof ReducedAnalysisResources){
				
			ReducedAnalysisResources rd = (ReducedAnalysisResources)resource;
				
			dataset = rd.getDataset();
				
		}
			
		String csv_ext = SystemUtilities.getSystemProperty("lab_extension");
		
		String gene_ext = SystemUtilities.getSystemProperty("gene_extension");
		
		String[] excell_strings = FormatUtils.getExcelStrings(solutions, dataset.getDataset());
		
		printFolder(excell_strings,pathToFolder,resource.getExperimentAlias(),
				SystemUtilities.getSystemProperty("excel_folder"),SystemUtilities.getSystemProperty("excel_file"),csv_ext);
				
		String[] r_strings = FormatUtils.getRstrings(solutions, dataset.getDataset());
		
		printFolder(r_strings,pathToFolder,resource.getExperimentAlias(),
				SystemUtilities.getSystemProperty("r_folder"),SystemUtilities.getSystemProperty("r_file"),csv_ext);
		
		if (resource.getAnalysisType() =='b'){
			
			String[] genes_strings = GeneOntology.getGeneStrings(solutions, ((Biological)dataset).getGeneNames());
			
			printFolder(genes_strings,pathToFolder,resource.getExperimentAlias(),
					SystemUtilities.getSystemProperty("genes_folder"),SystemUtilities.getSystemProperty("genes_file"),gene_ext);
						
		}		
		
			
	}
	
	
	
	private static void printFolder (String[] strings, String outFolderPath,String outName, String folderName,String fileName, String extension) 
			throws FileNotFoundException{
		
		File excel_dir = new File (outFolderPath,folderName);
		
		excel_dir.mkdirs();
		
		OutTextFile f = null;
		
		for (int i = 0; i<strings.length;i++){
			
			String e = strings[i];
			
			File aux = new File (excel_dir,outName+"_"+fileName+"_"+(i+1)+"."+extension);
			
			f = new OutTextFile(aux);
			
			f.print(e);
			
			f.close();
			
		}			
		
		
	}
	
	

}
