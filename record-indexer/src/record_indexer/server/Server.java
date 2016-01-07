package record_indexer.server;

import java.io.*;
import java.net.*;

import record_indexer.server.handlers.*;

import com.sun.net.httpserver.HttpServer;

public class Server {
	private static int PORT_NUMBER;
	private static String HOST_IP = "localhost";
	private static final int MAX_WAIT = 10;
	private HttpServer server;
	
	/**
	 * Creates and runs a new Server
	 * @param args
	 */
	public static void main(String[] args)
	{
		new Server(args[0]).run();
	}
	
	/**
	 * Default constructor
	 */
	public Server(String port){
		Server.PORT_NUMBER = Integer.parseInt(port);
		return;
	}

		
	/**
	 * Attempts to initialize and run the server
	 */
	public void run(){		
		try{
			server = HttpServer.create(new InetSocketAddress(PORT_NUMBER), MAX_WAIT);
		}catch(IOException e){
			System.out.println("Could not create HttpServer");
			e.printStackTrace();
		}
		server.setExecutor(null);
		server.createContext("/ValidateUser", new H1());
		server.createContext("/GetProjects", new H2());
		server.createContext("/GetSampleImage", new H3());
		server.createContext("/DownloadBatch", new H4());
		server.createContext("/SubmitBatch", new H5());
		server.createContext("/GetFields", new H6());
		server.createContext("/Search", new H7());
		server.createContext("/", new H8());
				
		server.start();
		
	}
	
	public void stop(){
		server.stop(1);
	}
	
	public static String getHost()
	{return HOST_IP;}
	
	public static int getPort()
	{return PORT_NUMBER;}
}
