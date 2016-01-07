package record_indexer.gui.views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;

import record_indexer.gui.misc.Sync;
import record_indexer.gui.misc.TableEntryTableModel;


@SuppressWarnings("serial")
public class TableEntryTable extends JTable implements Observer{

	private Sync observed;
	Dimension currentCell;
	
	
	public TableEntryTable(TableEntryTableModel model){
		super();
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setCellSelectionEnabled(true);
		this.getSelectionModel().addListSelectionListener(new RowListener());
		this.getColumnModel().getSelectionModel().addListSelectionListener(new ColumnListener());
		this.setModel(model);
		this.getModel().addTableModelListener(new TableModelListener(){
			@Override
			public void tableChanged(TableModelEvent e) {
				int row = e.getFirstRow();
				int col = e.getColumn();
				TableEntryTableModel model = (TableEntryTableModel)e.getSource();
				String data = (String)model.getValueAt(row, col);
				observed.changeCurrentCellContents(data);
			}
		});
		
		this.addMouseMotionListener(mouseAdapter);
		this.addMouseListener(mouseAdapter);
		this.getTableHeader().setReorderingAllowed(false);
	}
	
	public void setObserved(Sync o){
		observed = o;
		currentCell = new Dimension(-1,-1);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if(arg.getClass() == Dimension.class){
			Dimension d = (Dimension)arg;
			if(currentCell.equals(d))
				return;
			currentCell = d;
			this.changeSelection(d.height, d.width + 1, false, false);
			this.requestFocus();
		}else if(arg.getClass() == String.class){
			if(((String)arg).equals(getModel().getValueAt((int)currentCell.getHeight(), (int)currentCell.getWidth()+1)))
				return;
			getModel().setValueAt(arg, (int)currentCell.getHeight(), (int)currentCell.getWidth()+1);
		}else if(arg.getClass() == Boolean.class){
			getColumnModel().getColumn(currentCell.width+1).setCellRenderer(new CustomCellRenderer());;
			this.revalidate();
		}
	}
	
	public void restore(Sync o){
		String[][] data = o.getValues();
		TableEntryTableModel t = (TableEntryTableModel)getModel();
		t.setAllData(data);
		setModel(t);
		for(int i = 0; i < data[0].length; i++){
			getColumnModel().getColumn(i+1).setCellRenderer(new CustomCellRenderer());
		}
	}
	
	class RowListener implements ListSelectionListener{
		@Override
		public void valueChanged(ListSelectionEvent e) {			
			if(e.getValueIsAdjusting()){
				return;
			}
			
			Dimension d = new Dimension(getColumnModel().getSelectionModel().getLeadSelectionIndex()-1,getSelectionModel().getLeadSelectionIndex());
		
			if(currentCell.equals(d))
				return;
			
			
			if(d.height > -1 && d.width > -1){
				currentCell = d;
				observed.changeCurrentCell(currentCell);
			}
		}
	}

	class ColumnListener implements ListSelectionListener{
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if(getColumnModel().getSelectionModel().isSelectedIndex(0))
				getColumnModel().getSelectionModel().setSelectionInterval(1,1);
				
			if(e.getValueIsAdjusting()){
				return;
			}
			
			Dimension d = new Dimension(getColumnModel().getSelectionModel().getLeadSelectionIndex()-1,getSelectionModel().getLeadSelectionIndex());
			
			if(currentCell.equals(d))
				return;
			
			
			if(d.height > -1 && d.width > -1){
				currentCell = d;
				observed.changeCurrentCell(currentCell);
			}
		}
	}
	
	class CustomCellRenderer extends DefaultTableCellRenderer{
		
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
			JLabel l = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
			
			if(observed.getStatusAt(row,col-1)){
				l.setBackground(Color.white);
			}else{
				l.setBackground(Color.red);
			}
			
			return l;
		}
	}
	
	private transient MouseAdapter mouseAdapter = new MouseAdapter(){
		
		
		@Override
		public void mouseDragged(MouseEvent e){
			JTable target = (JTable)e.getSource();
			int row = target.getSelectedRow();
			int col = target.getSelectedColumn() -1;
			Dimension d = new Dimension(col, row);
			if(d.equals(currentCell) || col == -1)
				return;
			
			currentCell = d;
			observed.changeCurrentCell(currentCell);
		}
				
		@Override 
		public void mouseClicked(MouseEvent e){
			
		}
		
		@Override 
		public void mousePressed(MouseEvent e){
			if(e.isPopupTrigger()){
				JTable target = (JTable)e.getSource();
				int col = target.columnAtPoint(e.getPoint()) - 1;
				int row = target.rowAtPoint(e.getPoint());
				if(!observed.getStatusAt(row, col))
					doPop(e, target);
			}
		}
		
		private void doPop(final MouseEvent f, final JTable target){
			JPopupMenu pop = new JPopupMenu();
			JMenuItem suggest = new JMenuItem("Get Suggestions");
			suggest.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					Object[] suggestions = observed.suggestSimilar(target.rowAtPoint(f.getPoint()), target.columnAtPoint(f.getPoint())-1);
					RecordIndexerFrame tmp = (RecordIndexerFrame)RecordIndexerFrame.getController().getView();
					SuggestDialog suggest = new SuggestDialog(tmp,suggestions,target.rowAtPoint(f.getPoint()),target.columnAtPoint(f.getPoint())-1);
					suggest.setVisible(true);
				}
			});
			pop.add(suggest);
			pop.show(f.getComponent(), f.getX(), f.getY());
		}
	};
}



