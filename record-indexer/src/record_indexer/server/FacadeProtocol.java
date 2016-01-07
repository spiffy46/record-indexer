package record_indexer.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.sql.SQLException;

import com.sun.net.httpserver.HttpExchange;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import record_indexer.server.databaseAccess.DatabaseAccessDriver;
import record_indexer.server.databaseAccess.DatabaseException;
import record_indexer.shared.communication.*;
import record_indexer.shared.model.*;

public class FacadeProtocol {
	static DatabaseAccessDriver db;
	private static XStream xmlStream;
	public static String PATH_WAY = "indexer_data/Records";

	
	public FacadeProtocol(){	
	}
	
	public static HttpExchange validateUser(HttpExchange exchange) throws ServerException {
		xmlStream = new XStream(new DomDriver());
		db = new DatabaseAccessDriver();
		ValidateUserResult r;
		try{
			db.start();
			ValidateUserRequest requestObject = (ValidateUserRequest)xmlStream.fromXML(exchange.getRequestBody());
			user suspect = new user();
			suspect.setUsername(requestObject.getUSER());
			suspect.setPassword(requestObject.getPASSWORD());
			
			r = new ValidateUserResult(db.validateUser(suspect));
			
			db.close(true);
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			xmlStream.toXML(r, exchange.getResponseBody());
			exchange.getResponseBody().close();
		}catch(DatabaseException e){
			try{
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			}catch(IOException i){throw new ServerException();}
			throw new ServerException();
		}catch(IOException e){
			throw new ServerException();
		}catch(SQLException e){
			try{
				db.close(false);
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
				xmlStream.toXML(new ValidateUserResult(), exchange.getResponseBody());
				exchange.getResponseBody().close();
			}catch(IOException i){throw new ServerException();
			}catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new ServerException();
		}
		
		return exchange;
	}
	
	public static HttpExchange downloadBatch(HttpExchange exchange) throws ServerException{
		xmlStream = new XStream(new DomDriver());
		db = new DatabaseAccessDriver();
		DownloadBatchResult r;
		try{
			db.start();
			DownloadBatchRequest requestObject = (DownloadBatchRequest)xmlStream.fromXML(exchange.getRequestBody());
			user suspect = new user();
			suspect.setUsername(requestObject.getUSER());
			suspect.setPassword(requestObject.getPASSWORD());
		
			int userId = db.workingOnBatch(suspect);
			db.close(true);

			if(userId > 0)
			{
				r = new DownloadBatchResult(db.downloadBatch(requestObject.getPROJECT(), userId));
				db.close(true);
			}else{
				r = new DownloadBatchResult();
			}
		
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			xmlStream.toXML(r, exchange.getResponseBody());
			exchange.getResponseBody().close();
		}catch(DatabaseException e){
			try{
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			}catch(IOException i){throw new ServerException();}
			throw new ServerException();
		}catch(IOException e){
			throw new ServerException();
		}catch(SQLException e){
			System.out.println(e.getMessage());
			try{		
				db.close(false);
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
				xmlStream.toXML(new DownloadBatchResult(), exchange.getResponseBody());
				exchange.getResponseBody().close();
			}catch(IOException i){throw new ServerException();} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new ServerException();
		}
		
		return exchange;
	}
	
	public static HttpExchange getFields(HttpExchange exchange) throws ServerException{
		xmlStream = new XStream(new DomDriver());
		db = new DatabaseAccessDriver();
		GetFieldsResult r;
		try{
			db.start();
			GetFieldsRequest requestObject = (GetFieldsRequest)xmlStream.fromXML(exchange.getRequestBody());
			user suspect = new user();
			suspect.setUsername(requestObject.getUSER());
			suspect.setPassword(requestObject.getPASSWORD());
			
			ValidateUserResult isValid = new ValidateUserResult(db.validateUser(suspect));
			db.close(true);
			if(isValid.getOUTPUT() == "TRUE")
			{
				r = new GetFieldsResult(db.getFields(requestObject.getPROJECT()));
				db.close(true);
			}else{
				r = new GetFieldsResult();
			}
			
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			xmlStream.toXML(r, exchange.getResponseBody());
			exchange.getResponseBody().close();
		}catch(DatabaseException e){
			try{
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			}catch(IOException i){throw new ServerException();}
			throw new ServerException();
		}catch(IOException e){
			throw new ServerException();
		}catch(SQLException e){
			try{	
				db.close(false);
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
				xmlStream.toXML(new GetFieldsResult(), exchange.getResponseBody());
				exchange.getResponseBody().close();
			}catch(IOException i){throw new ServerException();} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new ServerException();
		}
		
		return exchange;
	}
	
	public static HttpExchange getProjects(HttpExchange exchange) throws ServerException{
		xmlStream = new XStream(new DomDriver());
		db = new DatabaseAccessDriver();
		GetProjectsResult r;
		try{
			db.start();
			GetProjectsRequest requestObject = (GetProjectsRequest)xmlStream.fromXML(exchange.getRequestBody());
			user suspect = new user();
			suspect.setUsername(requestObject.getUSER());
			suspect.setPassword(requestObject.getPASSWORD());
			
			ValidateUserResult isValid = new ValidateUserResult(db.validateUser(suspect));
			db.close(true);

			if(isValid.getOUTPUT() == "TRUE")
			{
				r = new GetProjectsResult(db.getProjects());
				db.close(true);

			}else{
				r = new GetProjectsResult();
			}
			
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			xmlStream.toXML(r, exchange.getResponseBody());
			exchange.getResponseBody().close();
		}catch(DatabaseException e){
			try{
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			}catch(IOException i){throw new ServerException();}
			throw new ServerException();
		}catch(IOException e){
			throw new ServerException();
		}catch(SQLException e){
			try{	
				db.close(false);
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
				xmlStream.toXML(new GetProjectsResult(), exchange.getResponseBody());
				exchange.getResponseBody().close();
			}catch(IOException i){throw new ServerException();} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new ServerException();
		}
		
		return exchange;
	}
	
	public static HttpExchange getSampleImage(HttpExchange exchange) throws ServerException{
		xmlStream = new XStream(new DomDriver());
		db = new DatabaseAccessDriver();
		GetSampleImageResult r;
		try{
			db.start();
			GetSampleImageRequest requestObject = (GetSampleImageRequest)xmlStream.fromXML(exchange.getRequestBody());
			user suspect = new user();
			suspect.setUsername(requestObject.getUSER());
			suspect.setPassword(requestObject.getPASSWORD());
			
			ValidateUserResult isValid = new ValidateUserResult(db.validateUser(suspect));
			db.close(true);
			if(isValid.getOUTPUT() == "TRUE")
			{
				r = new GetSampleImageResult(db.getSampleImage(requestObject.getPROJECT()));
				db.close(true);
			}else{
				r = new GetSampleImageResult();
			}
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			xmlStream.toXML(r, exchange.getResponseBody());
			exchange.getResponseBody().close();
		}catch(DatabaseException e){
			try{
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			}catch(IOException i){throw new ServerException();}
			throw new ServerException();
		}catch(IOException e){
			throw new ServerException();
		}catch(SQLException e){
			try{	
				db.close(false);
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
				xmlStream.toXML(new GetSampleImageResult(), exchange.getResponseBody());
				exchange.getResponseBody().close();
			}catch(IOException i){throw new ServerException();} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new ServerException();
		}
		
		return exchange;
	}
	
	public static HttpExchange search(HttpExchange exchange) throws ServerException{
		xmlStream = new XStream(new DomDriver());
		db = new DatabaseAccessDriver();
		SearchResult r;
		try{
			db.start();
			SearchRequest requestObject = (SearchRequest)xmlStream.fromXML(exchange.getRequestBody());
			user suspect = new user();
			suspect.setUsername(requestObject.getUSER());
			suspect.setPassword(requestObject.getPASSWORD());
			
			boolean success = db.assertSearch(suspect, requestObject.getFIELDS(), requestObject.getVALUES());
			db.close(true);
			
			if(success){
				r = new SearchResult(db.search(requestObject.getFIELDS(), requestObject.getVALUES()));
				db.close(true);
			}else{
				r = new SearchResult();
			}
			
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			xmlStream.toXML(r, exchange.getResponseBody());
			exchange.getResponseBody().close();
		}catch(DatabaseException e){
			try{
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			}catch(IOException i){throw new ServerException();}
			throw new ServerException();
		}catch(IOException e){
			throw new ServerException();
		}
		catch(SQLException e){
			try{	
				db.close(false);
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
				xmlStream.toXML(new SearchResult(), exchange.getResponseBody());
				exchange.getResponseBody().close();
			}catch(IOException i){throw new ServerException();} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new ServerException();
		}
		return exchange;
	}
	
	public static HttpExchange submitBatch(HttpExchange exchange) throws ServerException{
		xmlStream = new XStream(new DomDriver());
		db = new DatabaseAccessDriver();
		SubmitBatchResult r;
		try{
			db.start();
			SubmitBatchRequest requestObject = (SubmitBatchRequest)xmlStream.fromXML(exchange.getRequestBody());
			user suspect = new user();
			suspect.setUsername(requestObject.getUSER());
			suspect.setPassword(requestObject.getPASSWORD());
			
			if(requestObject.isVALIDINPUT()){
				boolean success = db.assertSubmit(suspect, requestObject.getBATCH(), requestObject.getNUMRECORDS(), requestObject.getNUMFIELDS());
				db.close(true);

				if(success)
				{
					db.submitBatch(suspect, requestObject.getBATCH(), requestObject.getNUMRECORDS(), requestObject.getNUMFIELDS(), requestObject.getVALUES());
					db.close(true);
					r = new SubmitBatchResult(true);
				}else{
					r = new SubmitBatchResult(false);
				}
			}else{
				r = new SubmitBatchResult(false);
			}
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			xmlStream.toXML(r, exchange.getResponseBody());
			exchange.getResponseBody().close();
		}catch(DatabaseException e){
			try{
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			}catch(IOException i){throw new ServerException();}
			throw new ServerException();
		}catch(IOException e){
			throw new ServerException();
		}catch(SQLException e){
			try{	
				db.close(false);
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
				xmlStream.toXML(new GetSampleImageResult(), exchange.getResponseBody());
				exchange.getResponseBody().close();
			}catch(IOException i){throw new ServerException();} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new ServerException();
		}
		
		return exchange;
	}

	public static HttpExchange download(HttpExchange exchange) throws ServerException{
		xmlStream = new XStream(new DomDriver());
		String path = PATH_WAY + exchange.getRequestURI().getPath();
		File f = new File(path);
		
		try{
			if(!f.exists() || !f.isFile()){
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
			}else{
			
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, f.length());
			
				OutputStream os = exchange.getResponseBody();
			
				FileInputStream fs = new FileInputStream(f);
			
				final byte[] buffer = new byte[1024];
				int count = 0;
				while((count = fs.read(buffer)) >= 0){
					os.write(buffer,0,count);
				}
			
				os.flush();
				fs.close();
				os.close();
			}
		}catch(IOException e){
			System.out.println("Problem with download");
		}
		
		return exchange;
	}
}
