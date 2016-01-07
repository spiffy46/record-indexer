package record_indexer.server.handlers;

import java.io.IOException;
import java.net.HttpURLConnection;

import record_indexer.server.FacadeProtocol;
import record_indexer.server.ServerException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


public class H6 implements HttpHandler{

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		try{
			exchange = FacadeProtocol.getFields(exchange);
		}catch(ServerException e){
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
			throw new IOException(String.format("getFields handler failed"));
		}
	}
}
