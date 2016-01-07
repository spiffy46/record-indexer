package record_indexer.client;

import java.net.URL;

import record_indexer.shared.communication.*;

public class ClientFacade {
	private static ClientCommunicator client;
	private static String serverHost;
	private static int serverPort;
	
	
	public ClientFacade(String host, int port){
		serverHost = host;
		serverPort = port;
		
		if (serverHost.length() > 0 && (serverPort >= 0 && serverPort <= 65535)) {	
			client = new ClientCommunicator(serverHost, serverPort);
		}
		else{
				System.out.println("Invalid serverHost or serverPort");
		}
	}
	
	/**
	 * Communicates with the server and returns ValidateUserResult object
	 * @param r A ValidateUserRequest object
	 * @return ValidateUserResult object
	 */
	public ValidateUserResult validateUser(ValidateUserRequest r)throws ClientException
	{
		return (ValidateUserResult)((HttpURLResponse)client.doPost("/ValidateUser", r)).getResponseBody();
	}
	
	/**
	 * Communicates with the server and returns GetProjectsResult object
	 * @param r A GetProjectRequest object
	 * @return GetProjectResult object
	 */
	public GetProjectsResult getProjects(GetProjectsRequest r)throws ClientException
	{
		return (GetProjectsResult)((HttpURLResponse)client.doPost("/GetProjects", r)).getResponseBody();
	}
	
	/**
	 * Communicates with the server and returns GetSampleImageResult object
	 * @param r A GetSampleImageRequest object
	 * @return GetSampleImageResult object
	 */
	public GetSampleImageResult getSampleImage(GetSampleImageRequest r)throws ClientException
	{
		return (GetSampleImageResult)((HttpURLResponse)client.doPost("/GetSampleImage", r)).getResponseBody();
	}
	
	/**
	 * Communicates with the server and returns DownloadBatchResult object
	 * @param r A DownloadBatchRequest object
	 * @return DownloadBatchResult object
	 */
	public DownloadBatchResult downloadBatch(DownloadBatchRequest r)throws ClientException
	{
		return (DownloadBatchResult)((HttpURLResponse)client.doPost("/DownloadBatch", r)).getResponseBody();
	}
	
	/**
	 * Communicates with the server and returns SubmitBatchResult object
	 * @param r A SubmitBatchRequest object
	 * @return SubmitBatchResult object
	 */
	public SubmitBatchResult submitBatch(SubmitBatchRequest r)throws ClientException
	{
		return (SubmitBatchResult)((HttpURLResponse)client.doPost("/SubmitBatch", r)).getResponseBody();
	}
	
	/**
	 * Communicates with the server and returns GetFieldsResult object
	 * @param r A GetFieldsRequest object
	 * @return GetFieldsResult object
	 */
	public GetFieldsResult getFields(GetFieldsRequest r)throws ClientException
	{
		return (GetFieldsResult)((HttpURLResponse)client.doPost("/GetFields", r)).getResponseBody();
	}
	
	/**
	 * Communicates with the server and returns SearchResult object
	 * @param r A SearchRequest object
	 * @return SearchResult object
	 */
	public SearchResult search(SearchRequest r)throws ClientException
	{
		return (SearchResult)((HttpURLResponse)client.doPost("/Search", r)).getResponseBody();
	}
	
	/**
	 * Communicates with the server and returns DownloadFileResult object
	 * @param r A DownloadFileRequest object
	 * @return DownloadFileResult object
	 */
	public byte[] downloadFile(URL r)throws ClientException
	{
		return ((byte[])client.doGet(r));
	}
	
	public static String getHost(){
		return serverHost;
	}
	
	public static int getPort(){
		return serverPort;
	}
}
