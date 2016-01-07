package record_indexer.gui.views;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;

import record_indexer.shared.communication.*;

@SuppressWarnings("serial")
public class DownloadBatchDialog extends JDialog{

	private JLabel _TITLE = new JLabel("Projects: ");
	private JButton _DOWNLOAD = new JButton("Download Batch");
	private JButton _SAMPLE = new JButton("View Sample Image");
	private JButton _CANCEL = new JButton("Cancel");
	private JComboBox<Object> _PROJECTS;
	private Object[] myList = {""};
	private RecordIndexerFrame parent;
	
	public DownloadBatchDialog(final RecordIndexerFrame p){
		super(p, "Project Select", true);
		parent = p;
		
		getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		this.setLayout(new GridBagLayout());
		
		_PROJECTS = new JComboBox<Object>(myList);
		
		JPanel pan = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.ipadx = 25;
		gbc.ipady = 15;
		gbc.insets = new Insets(10,10,10,10);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		_TITLE.setHorizontalAlignment(JLabel.CENTER);
		pan.add(_TITLE,gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.ipady = 0;
		pan.add(_PROJECTS,gbc);
		gbc.ipady = 15;
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		_DOWNLOAD.setHorizontalAlignment(JButton.CENTER);
		_DOWNLOAD.setMaximumSize(new Dimension(130,30));
		_DOWNLOAD.setPreferredSize(new Dimension(130,30));
		pan.add(_DOWNLOAD, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		_SAMPLE.setHorizontalAlignment(JButton.CENTER);
		_SAMPLE.setMaximumSize(new Dimension(150,30));
		_SAMPLE.setPreferredSize(new Dimension(150,30));
		pan.add(_SAMPLE,gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		_CANCEL.setHorizontalAlignment(JButton.CENTER);
		_CANCEL.setMaximumSize(new Dimension(80,30));
		_CANCEL.setPreferredSize(new Dimension(80,30));
		pan.add(_CANCEL,gbc);
		
		addListeners();
		this.add(pan);
		pack();
		setMinimumSize(new Dimension(550, 200));
		setMaximumSize(new Dimension(550, 200));
		setResizable(false);
		setLocationRelativeTo(parent);
	}
	
	public void setProjectOptions(Object[] projectOptions){
		myList = projectOptions;
		_PROJECTS.setModel(new DefaultComboBoxModel<Object>(myList));
	}
	
	private void addListeners(){
		_DOWNLOAD.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				String projectString = (String)_PROJECTS.getSelectedItem();
				
				String projectNum = projectString.split(". ")[0];
				
				DownloadBatchResult bResult = RecordIndexerFrame.getController().downloadBatch(projectNum);
				
				if(bResult.toString().equals("FAILED\n")){
					parent.giveErrorMessage("Unable to connect to server!", "Connection Issues");
				}else{
					parent.downloadBatch(bResult);
					dispose();
				}
			}
		});
		
		_SAMPLE.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				String projectString = (String)_PROJECTS.getSelectedItem();
				
				String projectNum = projectString.split(". ")[0];
				
				GetSampleImageResult iResult = RecordIndexerFrame.getController().downloadSampleImage(projectNum);
			
				List<String> iResultList = Arrays.asList(iResult.toString().split("\n"));
				if(iResultList.get(0).equals("FAILED")){
					parent.giveErrorMessage("Unable to connect to server!", "Connection Issues");
				}else{
					SampleImagePanel tempIPanel = new SampleImagePanel();
					try {
						URL url = new URL(iResultList.get(0));
						tempIPanel.setImage(url);
					} catch (MalformedURLException e1) {
						parent.giveErrorMessage("Invalid URL recieved: " + iResultList.get(0), "Invalid URL");
					} catch (IOException e1) {
						parent.giveErrorMessage("Invalid URL recieved: " + iResultList.get(0), "Invalid URL");
					}
					
					JOptionPane.showMessageDialog(parent, tempIPanel, "Sample Image", JOptionPane.PLAIN_MESSAGE);
				}
				
			}
		});
		
		_CANCEL.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();				
			}
		});
	}
	
}
