package tests;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.concurrent.Callable;

import main.Server;

/**
 * 
 * @author Abdullah Emad
 * 
 * This class contains public static helper functions that are 
 * commonly used in the testing methods
 *
 */
public class Helpers {

	/*=============================================================================================================
	 * Helper Functions
	 *============================================================================================================*/
	
	/*
	 * Server -> void
	 * Tells the server to start listening on a new Thread 
	 */
	public static GenericThread runServer(Server server)
	{
		try 
		{
			GenericThread serverThread = new GenericThread((Callable<Server>)server);
			serverThread.start();
			return serverThread;
		} 
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
		
	}
	
	/*
	 * String -> void
	 * Creates a client socket connected to the server
	 * and sends a string to the server
	 * 
	 */
	public static void sendToServer(String request)
	{
		try 
		{	
			// Starts a client socket
			Socket clientSocket = new Socket("127.0.0.1", 1200); 
			DataOutputStream outToServer = 
				      new DataOutputStream(clientSocket.getOutputStream());
	        outToServer.writeBytes(request + "\n"); 
	        clientSocket.close();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * bytes[], bytes[], int -> boolean
	 * returns true if the first n bytes of each array are the same
	 * @return
	 */
	public static boolean bytesCmp(byte[] arr1, byte arr2[], int n)
	{
		if(arr1.length < n || arr2.length < n)
		{
			return false;
		}
		
		for(int i = 0; i < n; i++)
		{
			if(arr1[i] != arr2[i])
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * String -> Boolean
	 * @throws UnsupportedEncodingException 
	 */
	public static boolean validateHeader(byte[] res, String expected_content_type,
			int expected_content_length) throws UnsupportedEncodingException
	{
		String header = new String(res, "UTF-8");
		
		String[] lines = header.split("\\r\\n");
		int count = 0;
		for(int i = 0; i < lines.length; i++)
		{
			String[] str = lines[i].split(" ");
			if(str[0].equals("HTTP/1.1") || str[0].equals("Date:") || str[0].equals("Server:") 
					|| str[0].equals("Last-Modified:") || str[0].equals("Content-Length:")
					|| str[0].equals("Content-Type:") || str[0].equals("Connection:"))
			{
				count++;

			}
			else if(str[0].equals("Server:"))
			{
				if(!str[1].equals("cranberry/1.0"))
				{
					return false;
				}
				
			}
			else if(str[0].equals("Content-Length:"))
			{
				if(Integer.parseInt(str[1]) != expected_content_length)
				{
					return false;
				}	
			}
			else if (str[0].equals("Content-Type:"))
			{
				if(!str[1].equals(expected_content_type))
				{
					return false;
				}
			}
			else if(str[0].equals("Connection:"))
			{
				if(!str[1].equals("keep-alive") && !str[1].equals("closed"))
				{
					return false;
				}
			}
			

		}
		
		return count >= 4;
	}
}
