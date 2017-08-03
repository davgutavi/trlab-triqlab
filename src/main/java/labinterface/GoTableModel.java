package labinterface;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import analysis.biological.GoStudy;
import analysis.biological.GoTerm;

@SuppressWarnings("serial")
public class GoTableModel extends AbstractTableModel {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(GoTableModel.class);

	
	private GoStudy study;
	private int rowCount;

	private int colCount = 3;
	
	private String [] columnNames = {"GO Term", "Name" , "P Value Adjusted"};
	
	
	public GoTableModel(GoStudy study) {
		super();
		this.study = study;
		rowCount = (study.getGoTerms()).size();
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
		
		
		List<GoTerm> goTerms = study.getGoTerms();
		
		GoTerm term = goTerms.get(rowIndex);
		
		
		switch (columnIndex){
		
		
		case 0:
			r = term.getId();
			break;
			
		case 1:
			r = term.getName();
			break;
			
				
		case 2:
			r = new Double(term.getpAdjusted());
			break;
				
		}
				
		return r;
	}

}
