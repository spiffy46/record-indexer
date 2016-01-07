package record_indexer.gui.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import record_indexer.gui.misc.Sync;
import record_indexer.shared.model.field;

@SuppressWarnings("serial")
public class FormEntryPanel extends JPanel implements Observer{
	private JLabel label;
	private JTextField textfield;
	private JList<String> associateJList;
	private List<JTextField> JTextFieldList;
	private Dimension currentCell;
	private Sync observed;

	
	public FormEntryPanel(List<field> fieldList){
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createLineBorder(Color.GRAY));
		addFields(fieldList);
		
	}
	
	private void addFields(List<field> fieldList){
		JTextFieldList = new ArrayList<JTextField>();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(5,5,5,5);
		gbc.anchor = GridBagConstraints.CENTER;
		field f;
		for(int i = 0; i < fieldList.size(); i++){
			gbc.gridy = i;
			f = fieldList.get(i);
			label = new JLabel(f.getTitle());
			textfield = new JTextField("",20);
			textfield.addFocusListener(new FocusListener(){

				@Override
				public void focusGained(FocusEvent e) {
					int index = JTextFieldList.indexOf((JTextField)e.getSource());
					Dimension d = new Dimension(index, currentCell.height);
					if(!currentCell.equals(d)){
						observed.changeCurrentCell(d);	
						currentCell = d;
					}
				}

				@Override
				public void focusLost(FocusEvent e) {
					
				}
				
			});
			
			textfield.addKeyListener(new KeyListener(){

				@Override
				public void keyTyped(KeyEvent e) {
					JTextField tmp = (JTextField)e.getSource();
					observed.changeCurrentCellContents(tmp.getText());
				}

				@Override
				public void keyPressed(KeyEvent e) {					
				}

				@Override
				public void keyReleased(KeyEvent e) {	
					JTextField tmp = (JTextField)e.getSource();
					observed.changeCurrentCellContents(tmp.getText());
				}
				
			});
			
			textfield.addMouseListener(mouseAdapter);
			
			JTextFieldList.add(textfield);

			
			gbc.gridx = 0;
			this.add(label,gbc);
			
			gbc.gridx = 1;
			this.add(textfield,gbc);
		}
	}
	
	public void changeSelection(int rowIndex){
		
		associateJList.setSelectedIndex(rowIndex);
		
		for(int i = 0; i < JTextFieldList.size(); i++){
			String data = observed.getData(rowIndex, i);
			boolean b = observed.getStatusAt(rowIndex, i);
			JTextFieldList.get(i).setText(data);
			if(b){
				JTextFieldList.get(i).setBackground(Color.white);
			}else{
				JTextFieldList.get(i).setBackground(Color.red);
			}
		}
	}
	
	public void setObserved(Sync o){
		observed = o;
		currentCell = new Dimension(-1,-1);
	}

	@Override
	public void update(Observable o, Object arg) {
		if(arg.getClass() == Dimension.class){
			Dimension d = (Dimension)arg;
			if(currentCell.equals(d)){
				JTextFieldList.get(currentCell.width).requestFocus();
				return;
			}
			if(currentCell.height != d.height){
				currentCell = d;
				changeSelection(d.height);
			}
			currentCell = d;
			JTextFieldList.get(currentCell.width).requestFocus();
		}else if(arg.getClass() == String.class){
			String data = (String)arg;
			JTextFieldList.get(currentCell.width).setText(data);
		}else if(arg.getClass() == Boolean.class){
			if((Boolean)arg == true){
				JTextFieldList.get(currentCell.width).setBackground(Color.white);
			}else{
				JTextFieldList.get(currentCell.width).setBackground(Color.red);
			}
			
		}
	}
	
	public void setJList(JList<String> myList){
		associateJList = myList;
		associateJList.addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent e) {
				int rowIndex = associateJList.getSelectedIndex();
				Dimension d = new Dimension(currentCell.width, rowIndex);
				if(!currentCell.equals(d)){
					observed.changeCurrentCell(d);
					currentCell.equals(d);
				}
			}
			
		});
		
	}
	
	private transient MouseAdapter mouseAdapter = new MouseAdapter(){
		
		@Override 
		public void mouseClicked(MouseEvent e){

		}
		
		@Override 
		public void mousePressed(MouseEvent e){
			if(e.isPopupTrigger()){
				JTextField tmp = (JTextField)e.getSource();
				int col = JTextFieldList.indexOf(tmp);
				int row = associateJList.getSelectedIndex();
				if(!observed.getStatusAt(row, col))
					doPop(e, row, col);
			}
		}
		
		private void doPop(MouseEvent e, final int h, final int w){
			JPopupMenu pop = new JPopupMenu();
			JMenuItem suggest = new JMenuItem("Get Suggestions");
			suggest.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					Object[] suggestions = observed.suggestSimilar(h, w);
					RecordIndexerFrame tmp = (RecordIndexerFrame)RecordIndexerFrame.getController().getView();
					SuggestDialog suggest = new SuggestDialog(tmp,suggestions,h,w);
					suggest.setVisible(true);
				}
			});
			pop.add(suggest);
			pop.show(e.getComponent(), e.getX(), e.getY());
		}
	};
	
	private void addListeners(){
		for(JTextField t: JTextFieldList){
			t.addFocusListener(new FocusListener(){

				@Override
				public void focusGained(FocusEvent e) {
					int index = JTextFieldList.indexOf((JTextField)e.getSource());
					Dimension d = new Dimension(index, currentCell.height);
					if(!currentCell.equals(d)){
						observed.changeCurrentCell(d);	
						currentCell = d;
					}
				}

				@Override
				public void focusLost(FocusEvent e) {
				
				}
			
			});
			
			t.addMouseListener(mouseAdapter);

		}
	}
	
	private void readObject(ObjectInputStream in)throws IOException,ClassNotFoundException{
		in.defaultReadObject();
		setJList(associateJList);
		addListeners();
	}

}
