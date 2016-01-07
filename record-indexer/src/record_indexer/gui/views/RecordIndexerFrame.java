package record_indexer.gui.views;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import record_indexer.gui.controllers.*;
import record_indexer.gui.misc.Sync;
import record_indexer.gui.misc.TableEntryTableModel;
import record_indexer.shared.communication.*;
import record_indexer.shared.model.field;

@SuppressWarnings("serial")
public class RecordIndexerFrame extends JFrame implements Serializable{
	private DownloadBatchDialog batchDia;
	
	private Sync observed;

	private static IController _controller;
	private String _host;
	private String _port;
	private String _user;
	private String _password;
	
	private JMenuBar _menuBar;
	private JMenu _fileMenu;
	private JMenuItem _downloadBatchMenuItem;
	private JMenuItem _logoutMenuItem;
	private JMenuItem _exitMenuItem;
	private BaseImagePanel _imagePanel;
	private JSplitPane _mainWindowPane;
	private JSplitPane _bottomSplitPane;
	private JTabbedPane _leftTabbedPane;
	private JTabbedPane _rightTabbedPane;
	
	
	public RecordIndexerFrame(){
		super();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setTitle("Record Indexer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	getController().save();
		    	System.exit(0);
		    }
		});
		setPreferredSize(new Dimension(1026,800));
		setMinimumSize(getPreferredSize());
		this.setLocation(dim.width/2 - this.getSize().width/2, dim.height/2-this.getSize().height/2);
	}
	
	public void createComponents(){
		observed = new Sync();
		
		batchDia = new DownloadBatchDialog((RecordIndexerFrame)getController().getView());
		
		_menuBar = new JMenuBar();
		
		_fileMenu = new JMenu("File");
		_fileMenu.setMnemonic(KeyEvent.VK_F);
		_menuBar.add(_fileMenu);
		
		_downloadBatchMenuItem = new JMenuItem("Download Batch", KeyEvent.VK_O);
		
		_logoutMenuItem = new JMenuItem("Logout", KeyEvent.VK_L);
		
		_exitMenuItem = new JMenuItem("Exit", KeyEvent.VK_X);
		
		_fileMenu.add(_downloadBatchMenuItem);
		_fileMenu.add(_logoutMenuItem);
		_fileMenu.add(_exitMenuItem);
				
		_imagePanel = new BaseImagePanel();
		_imagePanel.setRoot(this);
		
		_leftTabbedPane = new JTabbedPane();
		
		_leftTabbedPane.addTab("Table Entry", new JScrollPane());
		_leftTabbedPane.addTab("Form Entry", new JScrollPane());
		
		_rightTabbedPane = new JTabbedPane();
		_rightTabbedPane.addTab("Field Help", new JPanel());
		_rightTabbedPane.addTab("Image Navigation", new JPanel());
		
		_bottomSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, _leftTabbedPane, _rightTabbedPane);
		_bottomSplitPane.setDividerLocation(513);
		

		_mainWindowPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, _imagePanel, _bottomSplitPane);
		_mainWindowPane.setPreferredSize(new Dimension(1026,800));
		_mainWindowPane.setDividerLocation(526);
		
		this.addListeners();
		this.setJMenuBar(_menuBar);
		this.add(_mainWindowPane);
		setVisible(false);
		pack();
		
	}
	
	public void loginScreen(){
		setImage(null);
		setTableEntry(null);
		setFormEntry(null);
		setHelpField(null);
		
		this.revalidate();
		
		if(isVisible())
			setVisible(false);
		
		JLoginDialog _loginDialog = new JLoginDialog(this);
		_loginDialog.setVisible(true);
	}
	
	public void downloadBatch(DownloadBatchResult r){
		_downloadBatchMenuItem.setEnabled(false);
		
		List<String> batchResultList = Arrays.asList(r.toString().split("\n"));

		observed = new Sync(Integer.parseInt(batchResultList.get(5)), Integer.parseInt(batchResultList.get(6)));
		setImage(batchResultList);
		setTableEntry(batchResultList);
		setFormEntry(batchResultList);
		setHelpField(batchResultList);
		
		observed.createTries(RecordIndexerFrame.parseFields(batchResultList));
		observed.setBatchResultList(batchResultList);
		observed.changeCurrentCell(new Dimension(0,0));
		observed.setBatchNum(Integer.parseInt(batchResultList.get(0)));
		observed.setProjectNum(Integer.parseInt(batchResultList.get(1)));
		
		
		this.revalidate();
	}
	
	public void restore(Sync o){
		observed = o;
		setImage(o.getBatchResultList());
		setTableEntry(o.getBatchResultList());
		JScrollPane t = (JScrollPane)_leftTabbedPane.getComponentAt(0);
		TableEntryTable table = (TableEntryTable)t.getViewport().getComponent(0);
		table.restore(observed);
		setFormEntry(o.getBatchResultList());
		setHelpField(o.getBatchResultList());		
	}
	
	public void setImage(List<String> batchResultList){
		if(batchResultList == null){
			_imagePanel = new BaseImagePanel();
			_imagePanel.setRoot(this);
			_imagePanel._buttonPanel.enableButtons(false);
			
			_mainWindowPane.setTopComponent(_imagePanel);
			_mainWindowPane.revalidate();
			
		}else{
			_imagePanel.setImage(batchResultList);
			observed.addObserver( _imagePanel._imgPanel);
			_imagePanel._imgPanel.setObserved(observed);
			enableButtons(true);
		}		
	}
	
	public void setTableEntry(List<String> batchResultList){
		if(batchResultList == null){
			JPanel space = new JPanel();
			_leftTabbedPane.setComponentAt(0, space);
			_leftTabbedPane.revalidate();
			return;
		}
		int cols = Integer.parseInt(batchResultList.get(6)) + 1;
		int rows = Integer.parseInt(batchResultList.get(5));
				
		String[] fieldNames = new String[cols];
		fieldNames[0] = "Record Number";
		int arrayIndex = 1;
		
		for(int i = 9; i < batchResultList.size(); i+=2){
			fieldNames[arrayIndex] = batchResultList.get(i);
			
			i+=4;
			if(i < batchResultList.size()){
				if(batchResultList.get(i).charAt(0) == 'h'){
					i++;
				}
			}
			
			arrayIndex++;
		}
				
		TableEntryTableModel myModel = new TableEntryTableModel(rows,fieldNames);

		
		TableEntryTable newTable = new TableEntryTable(myModel);
		
		observed.addObserver(newTable);
		newTable.setObserved(observed);


		JScrollPane scroller = new JScrollPane(newTable);
		
		_leftTabbedPane.setComponentAt(0,scroller);
	}
	
	public void setFormEntry(List<String> batchResultList){
		if(batchResultList == null){
			JPanel space = new JPanel();
			_leftTabbedPane.setComponentAt(1, space);
			_leftTabbedPane.revalidate();
			return;
		}
		DefaultListModel<String> formListModel = new DefaultListModel<String>();
		for(int i = 1; i <= Integer.parseInt(batchResultList.get(5)); i++){
			formListModel.addElement(i+"");
		}
		JList<String> formList = new JList<String>(formListModel);
		formList.setSelectedIndex(0);
		formList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		FormEntryPanel formEntry = new FormEntryPanel(RecordIndexerFrame.parseFields(batchResultList));
		
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints tmpgbc = new GridBagConstraints();
		
		tmpgbc.gridx = 0;
		tmpgbc.gridy = 0;
		tmpgbc.gridwidth = 1;
		tmpgbc.anchor = GridBagConstraints.NORTHWEST;
		tmpgbc.weighty = 1;
		tmpgbc.weightx = 3;
		tmpgbc.fill = GridBagConstraints.BOTH;
		JScrollPane listScroll = new JScrollPane(formList);
		panel.add(listScroll,tmpgbc);
		
		tmpgbc.gridx = 1;
		tmpgbc.gridy = 0;
		tmpgbc.gridwidth = 2;
		tmpgbc.anchor = GridBagConstraints.NORTH;
		tmpgbc.weighty = 1;
		tmpgbc.weightx = 1;
		tmpgbc.fill = GridBagConstraints.BOTH;
		formEntry.setJList(formList);
		panel.add(formEntry,tmpgbc);
		
		JScrollPane scroller = new JScrollPane(panel);
		
		_leftTabbedPane.setComponentAt(1, scroller);
		
		observed.addObserver(formEntry);
		formEntry.setObserved(observed);
	}
	
	public void setHelpField(List<String> batchResultList){
		if(batchResultList == null){
			JPanel space = new JPanel();
			_rightTabbedPane.setComponentAt(0, space);
			_rightTabbedPane.revalidate();
			return;
		}
		
		HelpHtmlPanel helpPanel = new HelpHtmlPanel();
		helpPanel.setFields(RecordIndexerFrame.parseFields(batchResultList));
		
		_rightTabbedPane.setComponentAt(0, helpPanel);
		observed.addObserver(helpPanel);

	}
	
	public void submitBatch() {
		SubmitBatchResult result = getController().submitBatch(observed.getValues());
		
		if(result.toString().equals("FAILED\n")){
			giveErrorMessage("Unable to submit batch!", "Batch Submission");
			return;
		}
		_downloadBatchMenuItem.setEnabled(true);
		observed = new Sync();
		
		setImage(null);
		setTableEntry(null);
		setFormEntry(null);
		setHelpField(null);
			
		this.revalidate();
		
		JOptionPane.showMessageDialog(this, "Successfully Submitted Batch", "Batch Submission" , JOptionPane.INFORMATION_MESSAGE);		
	}
	
	public void setHost(String value) {
		_host = value;
	}

	public String getHost() {
		return _host;
	}

	public void setPort(String value) {
		_port = value;
	}

	public String getPort() {
		return _port;
	}

	public void giveErrorMessage(String message, String title) {
		JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);		
	}

	public String getUser() {
		return _user;
	}

	public void setUser(String value) {
		_user = value;
	}

	public void setPassword(String value) {
		_password = value;
	}

	public String getPassword() {
		return _password;
	}

	public void setController(IController controller) {
		_controller = controller;
	}
	
	public static IController getController() {
		return _controller;
	}
	
	public void setImagePanel(ImagePanel i){
		i.setParent(_imagePanel);
		_imagePanel.setImagePanel(i);
		observed.addObserver( _imagePanel._imgPanel);
		_imagePanel._imgPanel.setObserved(observed);
		enableButtons(true);
	}
	
	public ImagePanel getImagePanel(){
		return _imagePanel.getImagePanel();
	}
	
	public int getHorizontalSplitPos(){
		return _mainWindowPane.getDividerLocation();
	}
	
	public void setHorizontalSplitPos(int i){
		_mainWindowPane.setDividerLocation(i);
	}
	
	public int getVerticalSplitPos(){
		return _bottomSplitPane.getDividerLocation();
	}
	
	public void setVerticalSplitPos(int i){
		_bottomSplitPane.setDividerLocation(i);
	}
	
	public void enableButtons(boolean b){
		_imagePanel._buttonPanel.enableButtons(b);
	}
	
	public static List<field> parseFields(List<String> l){
		List<field> tempList = new ArrayList<field>();
		
		for(int i = 7; i < l.size(); i++){
			field tempField = new field();
			tempField.setField_id(Integer.parseInt(l.get(i)));
			i+=2;
			tempField.setTitle(l.get(i));
			i++;
			tempField.setHelphtml(l.get(i));
			i++;
			tempField.setXcoord(Integer.parseInt(l.get(i)));
			i++;
			tempField.setWidth(Integer.parseInt(l.get(i)));
			i++;
			if(i < l.size()){
				if(l.get(i).charAt(0) == 'h'){
					tempField.setKnowndata(l.get(i));
					i++;
				}
			}
			i--;
			tempList.add(tempField);
		}
		
		return tempList;
	}
	
	public Sync getObserved(){
		return observed;
	}
	
	public void addListeners(){
		_leftTabbedPane.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				int index = _leftTabbedPane.getSelectedIndex();
				JScrollPane p = (JScrollPane)_leftTabbedPane.getComponentAt(index);
				if(p.getViewport().getComponents().length != 0){
					if(index == 0){
						TableEntryTable t = (TableEntryTable)p.getViewport().getComponent(0);
						t.update(observed, observed.getCurrentCell());
					}else{
						JPanel tmpPan = (JPanel)p.getViewport().getComponent(0);
						FormEntryPanel f = (FormEntryPanel)tmpPan.getComponent(1);
						f.update(observed, observed.getCurrentCell());
					}
				}
			}
		});
		
		_exitMenuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				getController().logout();
				getController().quit();
			}
		});
		
		_logoutMenuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				getController().logout();
				_downloadBatchMenuItem.setEnabled(true);
				loginScreen();
			}
		});
		
		_downloadBatchMenuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				GetProjectsResult pResult = getController().getProjects();
				if(pResult.toString().equals("FAILED\n")){
					giveErrorMessage("Unable to connect to server!", "Connection Issues");
				}else{
					List<String> tempList = Arrays.asList(pResult.toString().split("\n"));
					List<String> mergeList = new ArrayList<String>();
					for(int i = 0; i < tempList.size(); i+=2){
						String str = tempList.get(i) + ". " + tempList.get(i+1);
						mergeList.add(str);
					}
					Object[] s = mergeList.toArray();

					batchDia.setProjectOptions(s);
					batchDia.setVisible(true);	
				}
			}
		});
	}

	public void save() {
		getController().save();
		JOptionPane.showMessageDialog(this, "Save Successful", "Saved", JOptionPane.PLAIN_MESSAGE);		
	}
	

}
