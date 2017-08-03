package labinterface;

import java.text.DecimalFormat;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import analysis.Solution;
import utils.TextUtilities;

@SuppressWarnings("serial")
public class BioExpTableModel extends AbstractTableModel {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(BioExpTableModel.class);

	
	private List<Solution> solutions;
	private int rowCount;

	private int colCount = 6;
	
	private String [] columnNames = {"Solution", "TRIQ" , "BIOQ", "GRQ", "PEQ", "SPQ"};
	
	
	public BioExpTableModel(List<Solution> solutions) {
		super();
		this.solutions = solutions;
		rowCount = solutions.size();
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
		Solution sol = solutions.get(rowIndex);
		DecimalFormat format = null;
		
		
		switch (columnIndex){
		
		
		case 0:
			r = sol.getName();
			break;
			
		case 1:
			format = TextUtilities.getDecimalFormat('.');
			r = format.format(sol.getValue("triq"));
			break;
			
		case 2:
			format = TextUtilities.getDecimalFormat('.');
			r = format.format(sol.getValue("bioq"));
			break;
			
		case 3:
			format = TextUtilities.getDecimalFormat('.');
			r = format.format(sol.getValue("grq"));
			break;
		
		case 4:
			format = TextUtilities.getDecimalFormat('.');
			r = format.format(sol.getValue("peq"));
			break;
		
		case 5:
			format = TextUtilities.getDecimalFormat('.');
			r = format.format(sol.getValue("spq"));
			break;
				
		}
				
		return r;
	}

}
