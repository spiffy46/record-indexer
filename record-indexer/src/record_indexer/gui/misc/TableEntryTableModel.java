package record_indexer.gui.misc;

import java.io.Serializable;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class TableEntryTableModel extends AbstractTableModel implements Serializable{

	private int rowCount;
	private String[] columnNames;
	private String[][] data;
	
	public TableEntryTableModel(int rows, String[] names){
		rowCount = rows;
		columnNames = names;
		data = new String[rowCount][columnNames.length];
		for(int i = 0; i < rows; i++){
			setValueAt(""+(i+1),i,0);
		}
	}
	
	public void setAllData(String[][] d){
		for(int i = 0; i < d.length; i++){
			for(int j = 0; j < d[0].length; j++){
				data[i][j+1] = d[i][j];
			}
		}
	}
	
	@Override
	public Class<?> getColumnClass(int c){
		return String.class;
	}
	
	@Override
	public void setValueAt(Object value, int row, int col){
		data[row][col] = (String)value;
		fireTableCellUpdated(row,col);
	}
	
	@Override
	public String getColumnName(int col){
		return columnNames[col];
	}
	
	@Override
	public boolean isCellEditable(int row, int col){
		if(col == 0)
			return false;
		return true;
	}
	
	@Override
	public int getRowCount() {
		return rowCount;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data[rowIndex][columnIndex];
	}

}
