package lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import exceptions.BadRequestException;

public class Client extends Thread {

	private Socket socket;
	private RequestsProcessor requestprocessor;
	public Client(Socket socket, RequestsProcessor requestprocessor)
	{
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
			BufferedReader inFromClient = null;
			try 
			{	
				inFromClient = new BufferedReader(new
				        InputStreamReader(socket.getInputStream()));
				
				String request = "";
				String holder  = "";
				while((holder = inFromClient.readLine()) != null)
				{
					if(holder.length() ==  0)
					{
						break;
					}
					request = request + holder + "\n";
				}

				requestprocessor.enqueueRequest(parseRequest(request));
			} 
			catch (IOException e1) 
			{
				e1.printStackTrace();
				return;
			}
		
			
		}
	}
	
	/**
	 * String -> void
	 * Parses a given string and returns the correct Method object
	 * @param req
	 * @return
	 */
	private static Request parseRequest(String req)
	{
		Request request = null;
		if(req.matches("GET (.|\n|\r)*"))
		{
			try 
			{
				request = new Get(req);
			} 
			catch (BadRequestException e)
			{
				System.out.println(e.getMessage());
				// TODO: send badly formatted request error code
			}
		}
		else if(req.matches("POST (.|\n|\r)*"))
		{
			try 
			{
				request = new Post(req);
			}
			catch (BadRequestException e) 
			{
				System.out.println(e.getMessage());
				// TODO: send badly formatted request error code

			}
		}

		return request;
	}
}
