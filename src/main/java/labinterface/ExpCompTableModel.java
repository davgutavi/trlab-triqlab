package labinterface;

import java.text.DecimalFormat;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import analysis.Experiment;
import analysis.Solution;
import utils.TextUtilities;

@SuppressWarnings("serial")
public class ExpCompTableModel extends AbstractTableModel {

	List<Experiment> experiments; 
	
	private int rowCount;

	private int colCount = 6;
	
	private String [] columnNames = {"EXPERIMENT", "DATASET" , "BEST SOL", "BEST TRIQ", "MEAN TRIQ", "STDEV TRIQ"};
	
	
	public ExpCompTableModel (List<Experiment> exps){
		super();
		experiments = exps;
		rowCount = experiments.size();
	}
	
	public String getColumnName(int col) {
	      return columnNames[col];
	}
	
	@Override
	public int getRowCount() {
		return rowCount;
	}

	@Override
	public int getColumnCount() {
		return colCount;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		Object r = null;
		Experiment exp = experiments.get(rowIndex);
		DecimalFormat format = null;
		Solution sol = null;
				
		switch (columnIndex){
		
		
		case 0:
			r = exp.getExperimentName();
			break;
			
		case 1:
			r = exp.getDatasetName();
			break;
					
		case 2:
			sol = (Solution)exp.getValue("bestsolution");
			r = sol.getName();			
			break;
		
		case 3:
			format = TextUtilities.getDecimalFormat('.');
			r = format.format((Double)exp.getValue("besttriq"));
			break;
		
		case 4:
			format = TextUtilities.getDecimalFormat('.');
			r = format.format((Double)exp.getValue("mean"));
			break;
			
		case 5:
			format = TextUtilities.getDecimalFormat('.');
			r = format.format((Double)exp.getValue("stdev"));
			break;
				
		}
				
		return r;
	}

}
