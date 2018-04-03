package model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

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
			InputStream in;
			byte[] buffer = new byte[819222];

			try {
				in = socket.getInputStream();
				int bytesRead = in.read(buffer);
			    ByteArrayOutputStream output = new ByteArrayOutputStream();
		        output.write(buffer, 0, bytesRead);
		        buffer = output.toByteArray();

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String request = new String(buffer, StandardCharsets.UTF_8);
			
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
		System.out.println(req);
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
	
	
	
}
