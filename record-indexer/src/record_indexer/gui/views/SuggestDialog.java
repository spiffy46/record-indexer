package record_indexer.gui.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class SuggestDialog extends JDialog{
	private static JList<String> _SUGGESTLIST;
	private JButton _USE;
	private JButton _CANCEL;
	private RecordIndexerFrame parent;

	
	public SuggestDialog(final RecordIndexerFrame p, Object[] listData, final int h, final int w){
		super(p, "Suggestions", true);
		
		parent = p;
		
		getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		for(Object s: listData){
			listModel.addElement((String)s);
		}
		
		_SUGGESTLIST = new JList<String>(listModel);
		_SUGGESTLIST.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_SUGGESTLIST.setVisibleRowCount(10);
		_SUGGESTLIST.addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent e) {
				_USE.setEnabled(true);
			}
			
		});
		
		JPanel pan = new JPanel();
		pan.setLayout(new BoxLayout(pan, BoxLayout.PAGE_AXIS));

		

		JScrollPane scroller = new JScrollPane(_SUGGESTLIST);
		scroller.setAlignmentX(CENTER_ALIGNMENT);

		pan.add(scroller);
		pan.add(Box.createRigidArea(new Dimension(0,5)));

		JPanel buttonPan = new JPanel();
		buttonPan.setLayout(new BoxLayout(buttonPan, BoxLayout.LINE_AXIS));
		
		_USE = new JButton("Use Suggestion");
		_USE.setAlignmentX(CENTER_ALIGNMENT);
		_USE.setEnabled(false);
		_USE.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				Dimension holderD = parent.getObserved().getCurrentCell();
				parent.getObserved().changeCurrentCell(new Dimension(w,h));
				String val = _SUGGESTLIST.getSelectedValue();
				parent.getObserved().changeCurrentCellContents(val);
				parent.getObserved().changeCurrentCell(holderD);
				dispose();
				}
			
		});
		buttonPan.add(Box.createRigidArea(new Dimension(10,0)));
		buttonPan.add(_USE);
		buttonPan.add(Box.createHorizontalGlue());
		
		_CANCEL = new JButton("Cancel");
		_CANCEL.setAlignmentX(CENTER_ALIGNMENT);
		_CANCEL.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();				
			}
			
		});
		buttonPan.add(_CANCEL);
		buttonPan.add(Box.createRigidArea(new Dimension(10,0)));

		pan.add(buttonPan);
		pan.add(Box.createRigidArea(new Dimension(0,5)));

		
		
		this.add(pan);
		
		setMinimumSize(new Dimension(275, 300));
		setMaximumSize(new Dimension(275, 300));
		setResizable(false);
		setLocationRelativeTo(parent);
	}
}
