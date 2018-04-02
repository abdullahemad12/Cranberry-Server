package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;

import model.Client;
import model.RequestsProcessor;

public class Server implements Callable<Server> {

	
	private RequestsProcessor requestsprocessor;
	private ServerSocket serverSocket;
	private String root;

	public String getRoot()
	{
		return this.root;
	}
	public Server(int port, String root) throws IOException
	{
		requestsprocessor = new RequestsProcessor();
		serverSocket = new ServerSocket(port);
		this.root = root;
		
	}
	
	public void Start()
	{
		// TODO: start listening and passing incoming connection to new threads
	}
	

	public void listen() throws IOException
	{
		System.out.println("Listening on Port: " + this.serverSocket.getLocalPort() + " ...");
		requestsprocessor.start();
		 while(true) { 
			  
	            Socket connectionSocket = serverSocket.accept();
	            Client client = new Client(connectionSocket, this.requestsprocessor, this);
	            client.start();
	            
	      }
	}
	
	public static void main(String[] args)
	{
		if(args.length != 2)
		{
			System.out.println("USAGE: java -jar Cranberry.jar PORT_NUMBER ROOTFOLDER");
			return;
		}
		
		int port = Integer.parseInt(args[0]);
		try
		{
			Server server = new Server(port, args[1]);
			server.listen();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/*Created for the testing*/
	public Server call() throws Exception {
		this.listen();
		return this;
	}
}
