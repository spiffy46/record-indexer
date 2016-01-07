package record_indexer.gui.views;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import record_indexer.gui.controllers.SearchController;
import record_indexer.shared.model.project;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Map.Entry;


@SuppressWarnings("serial")
public class SearchFrame extends JFrame implements IView{
	private static final int PREF_W = 1250;
	private static final int PREF_H = 950;
	
	private String USER = null;
	private String PASSWORD = null;
	private String PORT = null;
	private String HOST = null;
	private Map<Integer, String> fieldsMap;
	
	private SearchController _controller;
	private JPanel _basePanel;
	private SearchImagePanel _imagePanel;
	private JPanel _resultsDisplayPanel;
	private JPanel _searchBoxPanel;
	private JPanel _portHostPanel;
	private JPanel _loginPanel;
	private JPanel _projectListPanel;
	private JLabel _portLabel;
	private JLabel _hostLabel;
	private JLabel _usernameLabel;
	private JLabel _passwordLabel;
	private JTextField _portTextField;
	private JTextField _hostTextField;
	private JTextField _usernameTextField;
	private JTextField _passwordTextField;
	private JButton _loginButton;
	private JLabel _projectListTitleLabel;
	private JList<String> _projectList;
	private JList<String> _searchListBox;
	private JLabel _searchLabel;
	private JTextField _searchTextField;
	private JButton _searchButton;
	private DefaultListModel<String> projectListModel = new DefaultListModel<String>();
	private DefaultListModel<String> batchListModel = new DefaultListModel<String>();


	
	public SearchFrame(){
		super();
		setTitle("Record Indexer - Search GUI");		
		
		createComponents();
		
	}
	
	public void createComponents(){
		GridBagConstraints gbc = new GridBagConstraints();

		_basePanel = new JPanel(new GridBagLayout());
		_basePanel.setBackground(Color.DARK_GRAY);

		_imagePanel = new SearchImagePanel();
		_imagePanel.setPreferredSize(new Dimension(900, 675));
		_imagePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		
		_resultsDisplayPanel = new JPanel(new GridBagLayout());
		_resultsDisplayPanel.setPreferredSize(new Dimension(900, 168));
		_resultsDisplayPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		_searchListBox = new JList<String>(batchListModel);
		_searchListBox.setPreferredSize(new Dimension(850, 150));
		_searchListBox.addListSelectionListener(new ListSelectionListener(){
			@Override
			public void valueChanged(ListSelectionEvent e) {
				getController().displayBatch(_imagePanel, _searchListBox.getSelectedValue());
			}
		});

		setGbc(gbc, 0, 0, 1, 1);
		_resultsDisplayPanel.add(_searchListBox, gbc);
		
		_searchBoxPanel = new JPanel(new GridBagLayout());
		_searchBoxPanel.setPreferredSize(new Dimension(900, 56));
		_searchBoxPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		_searchLabel = new JLabel("Search for: ");
		_searchLabel.setPreferredSize(new Dimension(100,20));
		
		_searchTextField = new JTextField();
		_searchTextField.setEnabled(false);
		_searchTextField.setPreferredSize(new Dimension(600,20));
		
		_searchButton = new JButton("Search");
		_searchButton.setPreferredSize(new Dimension(150,20));
		_searchButton.setEnabled(false);
		_searchButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				getController().search(_projectList.getSelectedValuesList(), _searchTextField.getText());
			}
			
		});

		setGbc(gbc, 0, 0, 1, 1);
		_searchBoxPanel.add(_searchLabel, gbc);
		
		setGbc(gbc, 1, 0, 1, 1);
		_searchBoxPanel.add(_searchTextField, gbc);
		
		setGbc(gbc, 2, 0, 1, 1);
		_searchBoxPanel.add(_searchButton, gbc);
		
		_portHostPanel = new JPanel(new GridBagLayout());
		_portHostPanel.setPreferredSize(new Dimension(300, 225));
		_portHostPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		_portLabel = new JLabel("Port");
		_portLabel.setPreferredSize(new Dimension(150,20));
		
		_portTextField = new JTextField();
		_portTextField.setPreferredSize(new Dimension(150,20));
		
		_hostLabel = new JLabel("Host");
		_hostLabel.setPreferredSize(new Dimension(150,20));
		
		_hostTextField = new JTextField();
		_hostTextField.setPreferredSize(new Dimension(150,20));
		
		setGbc(gbc, 0, 0, 1, 1);
		_portHostPanel.add(_portLabel, gbc);
		
		setGbc(gbc, 0, 1, 1, 1);
		_portHostPanel.add(_portTextField, gbc);
		
		setGbc(gbc, 0, 3, 1, 1);
		_portHostPanel.add(_hostLabel, gbc);
		
		setGbc(gbc, 0, 4, 1, 1);
		_portHostPanel.add(_hostTextField, gbc);

		_loginPanel = new JPanel(new GridBagLayout());
		_loginPanel.setPreferredSize(new Dimension(300, 225));
		_loginPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		_usernameLabel = new JLabel("Username");
		_usernameLabel.setPreferredSize(new Dimension(150,20));
		
		_passwordLabel = new JLabel("Password");
		_passwordLabel.setPreferredSize(new Dimension(150,20));
		
		_usernameTextField = new JTextField();
		_usernameTextField.setPreferredSize(new Dimension(100,20));
		
		_passwordTextField = new JTextField();
		_passwordTextField.setPreferredSize(new Dimension(100,20));
		
		_loginButton = new JButton("Login");
		_loginButton.setPreferredSize(new Dimension(150,20));
		
		_loginButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				setPort(_portTextField.getText());
				setHost(_hostTextField.getText());
				setUser(_usernameTextField.getText());
				setPassword(_passwordTextField.getText());

				getController().login();
			}
			
		});

		setGbc(gbc, 0, 0, 1, 1);
		_loginPanel.add(_usernameLabel, gbc);
		
		setGbc(gbc, 2, 0, 1, 1);
		_loginPanel.add(_usernameTextField, gbc);
		
		setGbc(gbc, 0, 2, 1, 1);
		_loginPanel.add(_passwordLabel, gbc);
		
		setGbc(gbc, 2, 2, 1, 1);
		_loginPanel.add(_passwordTextField, gbc);
		
		setGbc(gbc, 0, 4, 3, 1);
		_loginPanel.add(_loginButton, gbc);
		
		_projectListPanel = new JPanel(new GridBagLayout());
		_projectListPanel.setPreferredSize(new Dimension(300, 450));
		_projectListPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		_projectListTitleLabel = new JLabel("Projects:");
		_projectListTitleLabel.setPreferredSize(new Dimension(150,20));
		
		_projectList = new JList<String>(projectListModel);
		_projectList.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		_projectList.setPreferredSize(new Dimension(275, 400));
		_projectList.setSelectionModel(new DefaultListSelectionModel(){
			@Override
			public void setSelectionInterval(int index0, int index1){
				if(super.isSelectedIndex(index0)){
					super.removeSelectionInterval(index0, index1);
				}else{
					super.addSelectionInterval(index0, index1);
				}
			}
		});
		
		setGbc(gbc, 1, 0, 1, 1);
		_projectListPanel.add(_projectListTitleLabel, gbc);
		
		setGbc(gbc, 0, 1, 3, 1);
		_projectListPanel.add(_projectList, gbc);
		
		//Add subpanels to basepanel;
		setGbc(gbc, 0, 0, 48, 36);
		_basePanel.add(_imagePanel, gbc);
		
		setGbc(gbc, 0, 36, 48, 9);
		_basePanel.add(_resultsDisplayPanel, gbc);
		
		setGbc(gbc, 0, 45, 48, 3);
		_basePanel.add(_searchBoxPanel, gbc);
		
		setGbc(gbc, 48, 0, 16, 12);
		_basePanel.add(_portHostPanel, gbc);
		
		setGbc(gbc, 48, 12, 16, 12);
		_basePanel.add(_loginPanel, gbc);
		
		setGbc(gbc, 48, 24, 16, 40);
		_basePanel.add(_projectListPanel, gbc);
		
		this.add(_basePanel);
	}
	
	private static GridBagConstraints _gbc = new GridBagConstraints();
	
	private void initGbc(GridBagConstraints gbc){
		gbc.gridx = _gbc.gridx;
    	gbc.gridy = _gbc.gridy;
    	gbc.gridwidth = _gbc.gridwidth;
    	gbc.gridheight = _gbc.gridheight;
    	gbc.weightx = _gbc.weightx;
    	gbc.weighty = _gbc.weighty;
    	gbc.anchor = _gbc.anchor;
    	gbc.fill = _gbc.fill;
    	gbc.insets = _gbc.insets;
    	gbc.ipadx = _gbc.ipadx;
    	gbc.ipady = _gbc.ipady;
	}
	
	public void setGbc(GridBagConstraints gbc, int gridx, int gridy, int gridwidth, int gridheight){
		initGbc(gbc);
		gbc.gridx = gridx;
		gbc.gridy = gridy;
    	gbc.gridwidth = gridwidth;
    	gbc.gridheight = gridheight;
	}
	
	/*private void setGbcWeight(GridBagConstraints gbc, int weightx, int weighty) {
    	gbc.weightx = weightx;
    	gbc.weighty = weighty;
    }
    
    private void setGbcFillAnchor(GridBagConstraints gbc, int fill, int anchor) {
    	gbc.fill = fill;
    	gbc.anchor = anchor;
    }
    
    private void setGbcPadding(GridBagConstraints gbc, int top, int left, 
    							int bottom, int right, int ipadx, int ipady) {
    	gbc.insets = new Insets(top, left, bottom, right);
    	gbc.ipadx = ipadx;
    	gbc.ipady = ipady;
    }*/
    
    @SuppressWarnings("unused")
	private WindowAdapter windowAdapter = new WindowAdapter() {
    	
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    };
	
	public SearchController getController() {
		return _controller;
	}
	
	public void setController(SearchController value) {
		_controller = value;
		//_settingsPanel.setController(value);
		//_paramPanel.setController(value);
		//_requestPanel.setController(value);
		//_responsePanel.setController(value);
	}
	
	public void giveErrorMessage(String error){
		JOptionPane.showMessageDialog(_basePanel, error);
	}
	
	@Override
	public void setPassword(String value){
		_passwordTextField.setText(value);
		PASSWORD = value;
	}
	
	@Override
	public String getPassword(){
		return PASSWORD;
	}
	
	@Override
	public void setUser(String value){
		_usernameTextField.setText(value);
		USER = value;
	}
	
	@Override
	public String getUser(){
		return USER;
	}
	
	@Override
	public void setHost(String value) {
		_hostTextField.setText(value);
		HOST = value;
	}

	@Override
	public String getHost() {
		return HOST;
	}

	@Override
	public void setPort(String value) {
		_portTextField.setText(value);
		PORT = value;
	}

	@Override
	public String getPort() {
		return PORT;
	}
	
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(PREF_W, PREF_H);
	}

	@Override
	public void setProjectList(String projects, String fields) {
		Map<project, List<String>> myMap = new HashMap<project, List<String>>();
		List<String> pList = Arrays.asList(projects.split("\n"));
		List<String> fList = Arrays.asList(fields.split("\n"));
		List<String> tempList = new ArrayList<String>();

		
		for(int i = 0; i < pList.size(); i+=2){
			project p = new project();
			p.setId(Integer.parseInt(pList.get(i)));
			p.setTitle(pList.get(i+1));
			
			tempList = new ArrayList<String>();
			for(int j = 0; j < fList.size(); j+=3){
				if(p.getId() == Integer.parseInt(fList.get(j))){
					tempList.add(fList.get(j+1) + " " + fList.get(j+2));
				}
			}
			List<String> t = new ArrayList<String>();
			myMap.put(p, new ArrayList<String>());
			t = myMap.get(p);
			t = tempList;
			myMap.put(p, t);
		}

		createProjectList(myMap);
		
		
		
		_searchButton.setEnabled(true);
		_searchTextField.setEnabled(true);
	}

	private void createProjectList(Map<project, List<String>> m){
		fieldsMap = new HashMap<Integer, String>();
		String projectStr = "";
		for(Entry<project, List<String>> entry: m.entrySet()){
			projectStr = entry.getKey().getTitle();
			for(int i = 0; i < entry.getValue().size(); i++){
				List<String> splitList = Arrays.asList(entry.getValue().get(i).split(" "));
				fieldsMap.put(Integer.parseInt(splitList.get(0)), splitList.get(1));
				String tempStr = splitList.get(0) + "   " + projectStr + "   " + splitList.get(1);
				projectListModel.addElement(tempStr);
			}
		}

	}

	@Override
	public void setBatchList(String string) {
		batchListModel.clear();
		List<String> splitList = Arrays.asList(string.split("\n"));
		for(int i = 1; i < splitList.size(); i+=4){
			batchListModel.addElement(splitList.get(i));
		}
	}
}
