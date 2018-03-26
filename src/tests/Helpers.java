package tests;

import java.io.DataOutputStream;
import java.io.IOException;
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
	public boolean bytesCmp(byte[] arr1, byte arr2[], int n)
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
}
