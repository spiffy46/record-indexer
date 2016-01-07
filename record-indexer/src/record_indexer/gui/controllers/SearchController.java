package record_indexer.gui.controllers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import record_indexer.client.ClientException;
import record_indexer.client.ClientFacade;
import record_indexer.gui.views.*;
import record_indexer.shared.communication.*;

public class SearchController{

	private IView myView;

	
	public SearchController(){
		return;
	}
	
	public IView getView(){
		return myView;
	}
	
	public void setView(IView v){
		myView = v;
	}
	
	public void initialize() {
		getView().setHost("localhost");
		getView().setPort("8080");
	}

	public void search(List<String> fields, String values) {
		String host = getView().getHost();
		String port = getView().getPort();
		String user = getView().getUser();
		String password = getView().getPassword();
		
		String fieldsList = parseFList(fields);
		
		ClientFacade facade = new ClientFacade(host, Integer.parseInt(port));
	
		SearchRequest request = new SearchRequest(user, password, fieldsList, values);

		SearchResult result;
		try{
			result = facade.search(request);
		}catch(ClientException e){
			System.out.println("Controller search() caught ClientException");
			result = new SearchResult();
			e.printStackTrace();
		}
		
		if(result.toString().equals("FAILED\n")){
			getView().giveErrorMessage("Unable to search for batches");
			return;
		}
		
		getView().setBatchList(result.toString());
	}

	public boolean login() {
		
		String host = getView().getHost();
		String port = getView().getPort();
		String user = getView().getUser();
		String password = getView().getPassword();
				
		ClientFacade facade = new ClientFacade(host, Integer.parseInt(port));
		
		GetProjectsRequest projectsRequest = new GetProjectsRequest(user, password);
		GetProjectsResult projectsResult;
		try {
			projectsResult = facade.getProjects(projectsRequest);
		} catch (ClientException e) {
			System.out.println("Controller validateUser() caught ClientException");
			projectsResult = new GetProjectsResult();
			e.printStackTrace();
		}
		
		if(projectsResult.toString().equals("FAILED\n")){
			getView().giveErrorMessage("Unable to log in");
			return false;
		}

		GetFieldsRequest fieldsRequest = new GetFieldsRequest(user, password);
		GetFieldsResult fieldsResult;
		try {
			fieldsResult = facade.getFields(fieldsRequest);
		} catch (ClientException e) {
			System.out.println("Controller validateUser() caught ClientException");
			fieldsResult = new GetFieldsResult();
			e.printStackTrace();
		}
		
		if(fieldsResult.toString().equals("FAILED\n")){
			getView().giveErrorMessage("Unable to log in");
			return false;
		}
		
		getView().setProjectList(projectsResult.toString(), fieldsResult.toString());
		return true;
	}
	
	private String parseFList(List<String> f){
		String tempStr = "";
		for(String s: f){
			tempStr = tempStr + s.split(" ")[0] + ",";
		}
		return tempStr;
	}

	public void displayBatch(SearchImagePanel p, String selectedValue) {
		try {
			URL url = new URL(selectedValue);
			p.setImage(url);
		} catch (MalformedURLException e) {
			System.out.println("Invalid url: " + selectedValue);
		}
	}

	public void quit() {
		// TODO Auto-generated method stub
		
	}

}
