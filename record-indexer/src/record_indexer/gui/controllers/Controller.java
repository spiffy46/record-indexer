package record_indexer.gui.controllers;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

import record_indexer.client.ClientException;
import record_indexer.client.ClientFacade;
import record_indexer.gui.misc.SaveState;
import record_indexer.gui.misc.Sync;
import record_indexer.gui.views.ImagePanel;
import record_indexer.gui.views.RecordIndexerFrame;
import record_indexer.shared.communication.*;

@SuppressWarnings("serial")
public class Controller implements IController, Serializable{
	
	private RecordIndexerFrame myView;
	public SaveState myState;
	public Map<String, SaveState> SaveStates;
	
	
	public Controller(){
		SaveStates = new HashMap<String, SaveState>();
		return;
	}
	
	public RecordIndexerFrame getView(){
		return myView;
	}
	
	public void setView(RecordIndexerFrame v){
		if(myView != null){
			String host = getView().getHost();
			String port = getView().getPort();
			myView = v;
			initialize(host, port);
		}else{
			myView = v;
		}
	}
	
	public void initialize(String host, String port) {
		getView().setHost(host);
		getView().setPort(port);
	}
	
	@Override
	public void initialize() {
		getView().setHost("localhost");
		getView().setPort("39640");
	}

	
	public ValidateUserResult login() {
		
		String host = getView().getHost();
		String port = getView().getPort();
		String user = getView().getUser();
		String password = getView().getPassword();
		
		ClientFacade facade = new ClientFacade(host, Integer.parseInt(port));
		
		ValidateUserRequest userRequest = new ValidateUserRequest(user,password);
		ValidateUserResult userResult;
		
		try{
			userResult = facade.validateUser(userRequest);
		}catch(ClientException e){
			System.out.println("Controller login() caught ClientException");
			userResult = new ValidateUserResult();
		}
		return userResult;
	}

	@Override
	public void search(List<String> fields, String values) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayBatch(ImagePanel _imagePanel, String selectedValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void quit() {
		System.exit(0);
	}

	public void save(){
		String fileName = "indexer_data/Users/save.ser";
		File saveFile = new File(fileName);
		try{
			updateSaveState();
			saveFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(saveFile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			SaveStates.put(getView().getUser()+getView().getPassword(), myState);
			oos.writeObject(SaveStates);
			oos.close();
		}catch(IOException e){
			e.printStackTrace();
			System.out.println("Error writing to file");
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void load(){
		String fileName = "indexer_data/Users/save.ser";
		File saveFile = new File(fileName);
		if(saveFile.exists()){
			try{
				FileInputStream fis = new FileInputStream(saveFile);
				ObjectInputStream ois = new ObjectInputStream(fis);
				SaveStates = (HashMap<String,SaveState>)ois.readObject();
				ois.close();
			}catch(IOException e){
				e.printStackTrace();
				System.out.println("Error reading from file");
			}catch(ClassNotFoundException c){
				c.printStackTrace();
				System.out.println("Error reading from file");
			}
		}else{
			SaveStates = new HashMap<String, SaveState>();
		}
		
		myState = SaveStates.get(getView().getUser()+getView().getPassword());
		
		getView().dispose();
		String name = getView().getUser();
		String pass = getView().getPassword();
		RecordIndexerFrame newWindow = new RecordIndexerFrame();
		setView(newWindow);
		getView().setController(this);
		newWindow.createComponents();
		newWindow.setUser(name);
		newWindow.setPassword(pass);
		
		if(myState != null){
			newWindow.setLocation(myState.getWINDOW_POS());
			newWindow.setSize(new Dimension(myState.getWINDOW_W(), myState.getWINDOW_H()));
			newWindow.setVerticalSplitPos(myState.getVERTICAL_SPLIT_POS());
			newWindow.setHorizontalSplitPos(myState.getHORIZONTAL_SPLIT_POS());
			if(myState.getBATCH_ID() != -1){
				Sync o = new Sync();
				o.setBatchResultList(myState.getBATCHRESULTLIST());
				o.createTries(RecordIndexerFrame.parseFields(o.getBatchResultList()));
				o.setValues(myState.getBATCH_VALUES());
				o.setStatuses(myState.getBATCH_STATUSES());
				getView().restore(o);

				getView().getImagePanel().setHighlighted(myState.isHIGHTLIGHTED());
				getView().getImagePanel().setInverted(myState.isINVERTED());
				getView().getImagePanel().setZoom(myState.getZOOM_LEVEL());
				getView().getImagePanel().setImageLoc(myState.getIMAGE_LOCATION());

				
				getView().getObserved().setBatchNum(myState.getBATCH_ID());
				getView().getObserved().setProjectNum(myState.getPROJECT_ID());
				getView().getObserved().changeCurrentCell(myState.getCURRENT_LOCATION());
			}

		}else{
			myState = new SaveState();
		}
		getView().setVisible(true);
	}
	
	public void updateSaveState(){
		myState.setWINDOW_POS(getView().getLocation());
		myState.setWINDOW_W(getView().getWidth());
		myState.setWINDOW_H(getView().getHeight());
		myState.setHORIZONTAL_SPLIT_POS(getView().getHorizontalSplitPos());
		myState.setVERTICAL_SPLIT_POS(getView().getVerticalSplitPos());
		if(getView().getObserved().getBatchNum() == -1){
			return;
		}else{
			myState.setCURRENT_LOCATION(getView().getObserved().getCurrentCell());

			myState.setZOOM_LEVEL(getView().getImagePanel().getZoom());
			myState.setINVERTED(getView().getImagePanel().getInverted());
			myState.setHIGHTLIGHTED(getView().getImagePanel().getHighlighted());
			myState.setIMAGE_LOCATION(getView().getImagePanel().getImageLoc());
			
			myState.setBATCH_VALUES(getView().getObserved().getValues());
			myState.setBATCH_STATUSES(getView().getObserved().getStatuses());
			myState.setPROJECT_ID(getView().getObserved().getProjectNum());
			myState.setBATCH_ID(getView().getObserved().getBatchNum());
			myState.setBATCHRESULTLIST(getView().getObserved().getBatchResultList());
		}
	}
	
	@Override
	public void logout() {
		save();
	}

	@Override
	public DownloadBatchResult downloadBatch(String projectNum) {
		String host = getView().getHost();
		String port = getView().getPort();
		String user = getView().getUser();
		String password = getView().getPassword();
		
		ClientFacade facade = new ClientFacade(host, Integer.parseInt(port));

		DownloadBatchRequest request = new DownloadBatchRequest(user, password, Integer.parseInt(projectNum));
		DownloadBatchResult result;
		
		try{
			result = facade.downloadBatch(request);
		}catch(ClientException e){
			System.out.println("Controller downloadBatch() caught ClientException");
			result = new DownloadBatchResult();
		}
		
		
		return result;
	}

	@Override
	public GetProjectsResult getProjects() {
		String host = getView().getHost();
		String port = getView().getPort();
		String user = getView().getUser();
		String password = getView().getPassword();
		
		ClientFacade facade = new ClientFacade(host, Integer.parseInt(port));

		GetProjectsRequest request = new GetProjectsRequest(user, password);
		GetProjectsResult projectResult;
		
		try{
			projectResult = facade.getProjects(request);
		}catch(ClientException e){
			System.out.println("Controller getProjects() caught ClientException");
			projectResult = new GetProjectsResult();
		}
		
		return projectResult;
	}

	@Override
	public GetSampleImageResult downloadSampleImage(String projectNum){
		String host = getView().getHost();
		String port = getView().getPort();
		String user = getView().getUser();
		String password = getView().getPassword();
				
		ClientFacade facade = new ClientFacade(host, Integer.parseInt(port));
		
		GetSampleImageRequest request = new GetSampleImageRequest(user, password, Integer.parseInt(projectNum));
		GetSampleImageResult result;
		
		try {
			result = facade.getSampleImage(request);
		} catch (ClientException e) {
			System.out.println("Controller getSampleImage() caught ClientException");
			result = new GetSampleImageResult();
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public SaveState getSaveState(){
		return myState;
	}

	@Override
	public SubmitBatchResult submitBatch(String[][] myValues) {
		String host = getView().getHost();
		String port = getView().getPort();
		String user = getView().getUser();
		String password = getView().getPassword();
		
		String valString = "";
		
		for(int i = 0; i < myValues.length; i++){
			for(int j = 0; j < myValues[i].length; j++){
				if(myValues[i][j] != null){
					valString = valString + myValues[i][j];
				}else{
					valString = valString + " ";
				}
				if(j != myValues[i].length - 1){
					valString = valString + ",";
				}else{
					valString = valString + ";";
				}
			}
		}
		
		ClientFacade facade = new ClientFacade(host, Integer.parseInt(port));

		SubmitBatchRequest request = new SubmitBatchRequest(user, password, getView().getObserved().getBatchNum(), valString);
		SubmitBatchResult result;
		
		try {
			result = facade.submitBatch(request);
		} catch (ClientException e) {
			System.out.println("Controller getSampleImage() caught ClientException");
			result = new SubmitBatchResult();
			e.printStackTrace();
		}
		return result;
	}
}
