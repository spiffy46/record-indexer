package record_indexer.gui.views;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.*;

@SuppressWarnings("serial")
public class ImageButtonPanel extends JPanel{
	
	public JButton _zoomin;
	public JButton _zoomout;
	public JButton _invert;
	public JButton _toggle;
	public JButton _save;
	public JButton _submit;
	public BaseImagePanel parent;
	
	
	ImageButtonPanel(final BaseImagePanel p){
		parent = p;
		
		GridBagConstraints gbc = new GridBagConstraints();
		this.setLayout(new GridBagLayout());
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.weightx = 1;
		_zoomin = new JButton("Zoom In");
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		this.add(_zoomin,gbc);
		
		_zoomout = new JButton("Zoom Out");
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		this.add(_zoomout,gbc);
		
		_invert = new JButton("Invert Image");
		
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		this.add(_invert,gbc);
		
		_toggle = new JButton("Toggle Highlights");
		
		gbc.gridx = 3;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		this.add(_toggle,gbc);
		
		_save = new JButton("Save");
		gbc.gridx = 4;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		this.add(_save,gbc);
		
		_submit = new JButton("Submit");
		
		gbc.gridx = 5;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		this.add(_submit,gbc);
		
		JLabel filler = new JLabel();
		gbc.gridx = 6;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 1000;
		this.add(filler,gbc);
		
		addListeners();
	}
	
	public void enableButtons(boolean b){
		this._invert.setEnabled(b);
		this._save.setEnabled(b);
		this._submit.setEnabled(b);
		this._toggle.setEnabled(b);
		this._zoomin.setEnabled(b);
		this._zoomout.setEnabled(b);
	}
	
	public void enableZoomIN(boolean b){
		this._zoomin.setEnabled(b);
	}
	
	public void enableZoomOUT(boolean b){
		this._zoomout.setEnabled(b);
	}
	
	private void addListeners(){
		_submit.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				parent.getRoot().submitBatch();
			}
		});
		
		_save.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				parent.getRoot().save();
			}
		});
		
		_toggle.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				parent._imgPanel.toggle();
			}
		});
		
		_invert.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				parent._imgPanel.invert();
			}
		});
		
		_zoomout.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				parent._imgPanel.zoomOUT();
			}
		});
		
		_zoomin.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				parent._imgPanel.zoomIN();
			}
		});
	}
	
	private void readObject(ObjectInputStream in)throws IOException,ClassNotFoundException{
		in.defaultReadObject();
		addListeners();
	}
}
