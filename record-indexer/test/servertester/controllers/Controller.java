package servertester.controllers;

import java.util.*;

import record_indexer.client.ClientException;
import record_indexer.client.ClientFacade;
import record_indexer.shared.communication.*;
import servertester.views.*;

public class Controller implements IController {

	private IView _view;
	
	public Controller() {
		return;
	}
	
	public IView getView() {
		return _view;
	}
	
	public void setView(IView value) {
		_view = value;
	}
	
	// IController methods
	//
	
	@Override
	public void initialize() {
		getView().setHost("localhost");
		getView().setPort("39640");
		operationSelected();
	}

	@Override
	public void operationSelected() {
		ArrayList<String> paramNames = new ArrayList<String>();
		paramNames.add("User");
		paramNames.add("Password");
		
		switch (getView().getOperation()) {
		case VALIDATE_USER:
			break;
		case GET_PROJECTS:
			break;
		case GET_SAMPLE_IMAGE:
			paramNames.add("Project");
			break;
		case DOWNLOAD_BATCH:
			paramNames.add("Project");
			break;
		case GET_FIELDS:
			paramNames.add("Project");
			break;
		case SUBMIT_BATCH:
			paramNames.add("Batch");
			paramNames.add("Record Values");
			break;
		case SEARCH:
			paramNames.add("Fields");
			paramNames.add("Search Values");
			break;
		default:
			assert false;
			break;
		}
		
		getView().setRequest("");
		getView().setResponse("");
		getView().setParameterNames(paramNames.toArray(new String[paramNames.size()]));
	}

	@Override
	public void executeOperation() {
		switch (getView().getOperation()) {
		case VALIDATE_USER:
			validateUser();
			break;
		case GET_PROJECTS:
			getProjects();
			break;
		case GET_SAMPLE_IMAGE:
			getSampleImage();
			break;
		case DOWNLOAD_BATCH:
			downloadBatch();
			break;
		case GET_FIELDS:
			getFields();
			break;
		case SUBMIT_BATCH:
			submitBatch();
			break;
		case SEARCH:
			search();
			break;
		default:
			assert false;
			break;
		}
	}
	
	private void validateUser() {
		String host = getView().getHost();
		String port = getView().getPort();
		
		String[] pValues = getView().getParameterValues();
		
		ClientFacade facade = new ClientFacade(host, Integer.parseInt(port));
		
		ValidateUserRequest request = new ValidateUserRequest(pValues[0],pValues[1]);
		ValidateUserResult result;
		try {
			result = facade.validateUser(request);
		} catch (ClientException e) {
			System.out.println("Controller validateUser() caught ClientException");
			result = new ValidateUserResult();
			e.printStackTrace();
		}
		
		getView().setRequest(request.toString());
		getView().setResponse(result.toString());
	}
	
	private void getProjects() {
		String host = getView().getHost();
		String port = getView().getPort();
		
		String[] pValues = getView().getParameterValues();
		
		ClientFacade facade = new ClientFacade(host, Integer.parseInt(port));
		
		GetProjectsRequest request = new GetProjectsRequest(pValues[0], pValues[1]);
		GetProjectsResult result;
		
		try {
			result = facade.getProjects(request);
		} catch (ClientException e) {
			System.out.println("Controller getProjects() caught ClientException");
			result = new GetProjectsResult();
			e.printStackTrace();
		}
		
		getView().setRequest(request.toString());
		getView().setResponse(result.toString());
	}
	
	private void getSampleImage() {
		String host = getView().getHost();
		String port = getView().getPort();
		
		String[] pValues = getView().getParameterValues();
		
		ClientFacade facade = new ClientFacade(host, Integer.parseInt(port));
		
		GetSampleImageRequest request = new GetSampleImageRequest(pValues[0], pValues[1], Integer.parseInt(pValues[2]));
		GetSampleImageResult result;
		
		try {
			result = facade.getSampleImage(request);
		} catch (ClientException e) {
			System.out.println("Controller getSampleImage() caught ClientException");
			result = new GetSampleImageResult();
			e.printStackTrace();
		}
		
		getView().setRequest(request.toString());
		getView().setResponse(result.toString());
		
	}
	
	private void downloadBatch() {
		String host = getView().getHost();
		String port = getView().getPort();
		
		String[] pValues = getView().getParameterValues();
		
		ClientFacade facade = new ClientFacade(host, Integer.parseInt(port));
		
		DownloadBatchRequest request = new DownloadBatchRequest(pValues[0], pValues[1], Integer.parseInt(pValues[2]));
		DownloadBatchResult result;
		
		try {
			result = facade.downloadBatch(request);
		} catch (ClientException e) {
			System.out.println("Controller downloadBatch() caught ClientException");
			result = new DownloadBatchResult();
			e.printStackTrace();
		}
		
		getView().setRequest(request.toString());
		getView().setResponse(result.toString());
		
	}
	
	private void getFields() {
		String host = getView().getHost();
		String port = getView().getPort();
		
		String[] pValues = getView().getParameterValues();
		
		ClientFacade facade = new ClientFacade(host, Integer.parseInt(port));
		
		GetFieldsRequest request;
		if(!pValues[2].equals(""))
			request = new GetFieldsRequest(pValues[0], pValues[1], Integer.parseInt(pValues[2]));
		else
			request = new GetFieldsRequest(pValues[0], pValues[1]);

		GetFieldsResult result;
		
		try {
			result = facade.getFields(request);
		} catch (ClientException e) {
			System.out.println("Controller getFields() caught ClientException");
			result = new GetFieldsResult();
			e.printStackTrace();
		}
		
		getView().setRequest(request.toString());
		getView().setResponse(result.toString());
	}
	
	private void submitBatch() {
		String host = getView().getHost();
		String port = getView().getPort();
		
		String[] pValues = getView().getParameterValues();
		
		ClientFacade facade = new ClientFacade(host, Integer.parseInt(port));
		
		SubmitBatchRequest request = new SubmitBatchRequest(pValues[0], pValues[1], Integer.parseInt(pValues[2]), pValues[3]);
		
		SubmitBatchResult result;
		try{
			result = facade.submitBatch(request);
		}catch(ClientException e){
			System.out.println("Controller submitBatch() caught ClientException");
			result = new SubmitBatchResult();
			e.printStackTrace();
		}
		
		getView().setRequest(request.toString());
		getView().setResponse(result.toString());
	}
	
	private void search() {
		String host = getView().getHost();
		String port = getView().getPort();
		
		String[] pValues = getView().getParameterValues();
		
		ClientFacade facade = new ClientFacade(host, Integer.parseInt(port));
		
		SearchRequest request = new SearchRequest(pValues[0], pValues[1], pValues[2], pValues[3]);
		
		SearchResult result;
		try{
			result = facade.search(request);
		}catch(ClientException e){
			System.out.println("Controller search() caught ClientException");
			result = new SearchResult();
			e.printStackTrace();
		}
		
		getView().setRequest(request.toString());
		getView().setResponse(result.toString());
	}

}

