package labinterface;

import java.awt.Component;
import java.text.DecimalFormat;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


public class PvalueRender extends DefaultTableCellRenderer {
   
	private static final long serialVersionUID = 1L;
	
	static private PvalueRender singleton = new PvalueRender();
	private PvalueRender() {}
	public static PvalueRender getInstance() {return singleton;}
	
	private final DecimalFormat formatter = new DecimalFormat( "#.##E0" );

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

       // First format the cell value as required

       value = formatter.format((Number)value);

       // And pass it on to parent class

       return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column );
    }
 }