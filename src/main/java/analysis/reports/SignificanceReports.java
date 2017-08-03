package analysis.reports;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import analysis.Experiment;
import analysis.Solution;
import analysis.biological.GoInterval;
import analysis.biological.GoLevels;
import analysis.biological.GoSignificance;
import analysis.biological.GoSlot;
import analysis.biological.GoStudy;

public class SignificanceReports {
	
	private static final Logger LOG = LoggerFactory.getLogger(SignificanceReports.class);
	
	private static final String LEVEL_HEADER_TAG         = "LEVEL";
	private static final String LEVELWEIGHT_HEADER_TAG   = "WEIGHT";
	private static final String INTERVAL_HEADER_TAG      = "INTERVAL";
	private static final String ITEMS_HEADER_TAG         = "TERMS";
	private static final String CONCENTRATION_HEADER_TAG = "CONCENTRATION";
	private static final String PARTIAL_HEADER_TAG       = "PARTIAL";
	
	
	
public static String getSignificanceReport(List<GoStudy> studies){
		
		return getSignificanceReport(studies,";","pa");
		
	}
	
public static String getSignificanceReport(List<GoStudy> studies, String sep, String type){
		
		String r = "";
		
		GoSignificance significance = GoSignificance.getInstance();
		
		List<GoInterval> intervals = significance.getINTERVALS();
		
		String header = LEVEL_HEADER_TAG+sep+LEVELWEIGHT_HEADER_TAG+sep+INTERVAL_HEADER_TAG+sep;
		
		String headerSufix = "";
		
		
		List<GoLevels> levels = new ArrayList<GoLevels> (studies.size());
		
		for (GoStudy std:studies){
			
			LOG.debug(std.getStudyName());
			GoLevels goLevels = significance.getGoLevels(std);
			significance.computeAllGoSig(goLevels);
			levels.add(goLevels);
			String solName = std.getStudyName();
			headerSufix+=solName+ITEMS_HEADER_TAG+sep+solName+CONCENTRATION_HEADER_TAG+sep+solName+PARTIAL_HEADER_TAG+sep;
		}
		
		header+=headerSufix;
		
		r = header+"\n";
		
		
		

		int i = 0;
		
		//Collections.reverse(intervals);
		
		for(GoInterval in: intervals){
			
			String prefix = in.getReportString(sep);
			
			String sufix = "";
						
			for (GoStudy std:studies){
				
				GoSlot slot = ((levels.get(studies.indexOf(std))).getSlotsByType(type)).get(i);
												
				sufix = slot.getReport(sep);
				
				prefix+=sufix+sep;
				
			}
			
			r+=prefix+"\n";
			
			i++;
			
		}
		
		
		
		return r;
		
	}

	

public static String getSignificanceReport(Experiment exp){
	
	return getSignificanceReport(exp,";","pa");
	
}

	
	
public static String getSignificanceReport(Experiment exp, String sep, String type){
		
		String r = "";
		
		List<Solution> solutions = exp.getSolutions();
		
		List<GoInterval> intervals = (GoSignificance.getInstance()).getINTERVALS();
			
		String header = LEVEL_HEADER_TAG+sep+LEVELWEIGHT_HEADER_TAG+sep+INTERVAL_HEADER_TAG+sep;
		
		String headerSufix = "";
		
		for (Solution sol:solutions){
			String solName = sol.getName();
			headerSufix+=solName+ITEMS_HEADER_TAG+sep+solName+CONCENTRATION_HEADER_TAG+sep+solName+PARTIAL_HEADER_TAG+sep;
		}
		
		header+=headerSufix;
		
		r = header+"\n";
		
		int i = 0;
		
		//Collections.reverse(intervals);
		
		for(GoInterval in: intervals){
			
			String prefix = in.getReportString(sep);
			
			String sufix = "";
			
			for (Solution sol:solutions){
				
				GoSlot slot = (sol.getGoSlots(type)).get(i);
				
				sufix = slot.getReport(sep);
				
				prefix+=sufix+sep;
				
			}
			
			r+=prefix+"\n";
			
			i++;
			
		}
		
		return r;
		
	}

}
