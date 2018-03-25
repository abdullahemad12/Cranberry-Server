package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;

import lib.Client;
import lib.RequestsProcessor;

public class Server implements Callable<Server> {

	
	private RequestsProcessor requestsprocessor;
	private ServerSocket serverSocket;


	public Server(int port) throws IOException
	{
		requestsprocessor = new RequestsProcessor();
		serverSocket = new ServerSocket(port);
		
	}
	
	public void Start()
	{
		// TODO: start listening and passing incoming connection to new threads
	}
	

	public void listen() throws IOException
	{
		System.out.println("Listening on Port: " + this.serverSocket.getLocalPort() + " ...");
		  while(true) { 
			  
	            Socket connectionSocket = serverSocket.accept();
	            Client client = new Client(connectionSocket, this.requestsprocessor);
	            client.start();
	            
	      }
	}
	
	public static void main(String[] args)
	{
		if(args.length != 1)
		{
			System.out.println("USAGE: java -jar Cranberry.jar PORT_NUMBER");
			return;
		}
		
		int port = Integer.parseInt(args[0]);
		try
		{
			Server server = new Server(port);
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
