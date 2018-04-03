package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import lib.Get;
import lib.Post;
import lib.Request;
import main.Server;

import exceptions.BadRequestException;

public class Client extends Thread {

	private Socket socket;
	private RequestsProcessor requestprocessor;
	private Server server;
	public Client(Socket socket, RequestsProcessor requestprocessor, Server server)
	{
		this.server = server;
		this.socket = socket;
		this.requestprocessor = requestprocessor;
	}

	public Socket getSocket() {
		return socket;
	}
	
	public void run()
	{
		while(socket.isConnected())
		{

				

			String request = "";
			try {
				request = readRequest(socket);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try 
			{
				requestprocessor.getSemaphore().acquire();
				Request req = parseRequest(request, socket, server);
				if(req != null)
				{
					requestprocessor.enqueueRequest(req);
				}
				requestprocessor.getSemaphore().release();
			}
			catch (BadRequestException e) 
			{
				System.out.println(e.getMessage());
				e.printStackTrace();
				// TODO: send badly formatted request error code
			}
			catch(InterruptedException e)
			{
				System.out.println(e.getMessage());
			}
		
		
			
		}
	}
	
	/**
	 * String -> void
	 * Parses a given string and returns the correct Method object
	 * @param req
	 * @return
	 * @throws BadRequestException 
	 */
	private static Request parseRequest(String req, Socket socket, Server server) throws BadRequestException
	{
		Request request = null;
		if(req.matches("GET (.|\n|\r)*"))
		{
			
			request = new Get(req, socket, server.getRoot());
		}
		else if(req.matches("POST (.|\n|\r)*"))
		{
			
			request = new Post(req, socket, server.getRoot());
		}

		return request;
	}
	
	
	private String readRequest(Socket socket) throws IOException
	{
		
		BufferedReader inFromClient = new BufferedReader(new
		        InputStreamReader(socket.getInputStream()));
		
		String request = "";
		String holder  = "";
		while((holder = inFromClient.readLine()) != null)
		{
			if(holder.length() ==  0 || holder.equals("\n") || holder.equals(" ") || holder.equals(""))
			{
				break;
			}
			request = request + holder + "\n";
		}

		return request;
	}
	
	
}
