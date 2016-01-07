package record_indexer.client;

import java.io.*;
import java.net.*;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import record_indexer.shared.communication.*;

public class ClientCommunicator {

	
	private String serverHost;
	private int serverPort;
	private XStream xmlStream;
	
	/**
	 * Submits requests to the server
	 */
	public ClientCommunicator(String serverHost, int serverPort)
	{
		this.serverPort = serverPort;
		this.serverHost = serverHost;
		this.xmlStream = new XStream(new DomDriver());
	}
	
	/**
	 * Make an HTTP POST request for url
	 * @param urlPath url passed on to the HTTP POST request
	 * @throws ClientException If unable to perform server request
	 */
	public Object doPost(String urlPath, Object postData) throws ClientException{
		
		HttpURLResponse result = new HttpURLResponse();
		
		String finalUrl = "http://" + serverHost + ":" + serverPort + urlPath;
		try{
			URL url = new URL(finalUrl);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.connect();	
			
			xmlStream.toXML(postData, connection.getOutputStream());
			connection.getOutputStream().close();
			
			result.setResponseCode(connection.getResponseCode());
			result.setResponseLength(connection.getContentLength());
			
			if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
				result.setResponseBody(xmlStream.fromXML(connection.getInputStream()));
			}else{
				throw new ClientException(String.format("doPost failed: %s (http code %d)", urlPath, connection.getResponseCode()));
			}
		}catch(IOException e){
			throw new ClientException(String.format("doPost failed: %s", e.getMessage()), e);
		}
		
		return result;
	}
	
	public Object doGet(URL urlPath) throws ClientException{
		HttpURLResponse result = new HttpURLResponse();
		
		try{
			URL url = urlPath;
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			
			connection.setRequestMethod("GET");
			connection.connect();	
			
			result.setResponseCode(connection.getResponseCode());
			result.setResponseLength(connection.getContentLength());
			
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				if(connection.getContentLength() == 0) {
					result.setResponseBody(xmlStream.fromXML(connection.getInputStream()));
				}
			} else {
				throw new ClientException(String.format("doGet failed: %s (http code %d)", urlPath, connection.getResponseCode()));
			}
			
		}catch(IOException e){
			throw new ClientException(String.format("doGet failed: %s", e.getMessage()), e);
		}
		return result;
	}
}
