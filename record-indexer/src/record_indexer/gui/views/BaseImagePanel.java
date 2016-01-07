package record_indexer.gui.views;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.*;

@SuppressWarnings("serial")
public class BaseImagePanel extends JPanel{
	private RecordIndexerFrame root;
	
	public static final int x = 0;
	public static final int y = 0;

	public ImagePanel _imgPanel;
	public JScrollPane _scrollPane;
	public ImageButtonPanel _buttonPanel;
			
	public BaseImagePanel(){
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		_buttonPanel = new ImageButtonPanel(this);
		_buttonPanel.setPreferredSize(new Dimension(1000,25));
		_buttonPanel.enableButtons(false);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		this.add(_buttonPanel,gbc);
		
		_imgPanel = new ImagePanel(this);
		_imgPanel.setPreferredSize(new Dimension(1000, 500));
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 20;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		this.add(_imgPanel,gbc);
		
		setPreferredSize(new Dimension(1026, 525));
		setMinimumSize(new Dimension(1026, 525));
	}
	
	public void setImage(List<String> batchResultList){
		_imgPanel.setImage(batchResultList);
	}
	
	public void setRoot(RecordIndexerFrame r){
		root = r;
		_imgPanel.setRoot(r);
	}
	
	public RecordIndexerFrame getRoot(){
		return root;
	}
	
	public void setImagePanel(ImagePanel i){
		_imgPanel = i;
		this.repaint();
		this.revalidate();
	}
	
	public ImagePanel getImagePanel(){
		return _imgPanel;
	}
	
}
