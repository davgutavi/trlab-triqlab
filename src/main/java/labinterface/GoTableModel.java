package labinterface;

import java.util.List;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import analysis.biological.GoStudy;
import analysis.biological.GoTerm;

@SuppressWarnings("serial")
//public class GoTableModel extends AbstractTableModel {
	public class GoTableModel extends DefaultTableModel {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(GoTableModel.class);

	
	private GoStudy study;
	private int rowCount;

	private int colCount = 4;
	
	private String [] columnNames = {"GO Term", "Name" ,"PV", "APV"};
	
	
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
			r = new Double(term.getP());
			break;
			
		case 3:
			r = new Double(term.getpAdjusted());
			break;
				
		}
				
		return r;
	}
	
	public Class getColumnClass(int columnIndex) {
		
		Class r = null;
		
		switch (columnIndex){
		
		
		case 0:
			r = String.class;
			break;
			
		case 1:
			r = String.class;
			break;
			
				
		case 2:
			r = Double.class;
			break;
			
		case 3:
			r = Double.class;
			break;
				
		}
				
		return r;
		
	}

}
