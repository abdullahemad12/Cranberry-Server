package tests;

import static org.junit.Assert.*;


import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;

import lib.Client;
import lib.Get;
import lib.Post;
import lib.Request;
import lib.RequestsProcessor;
import main.Server;

import org.junit.Test;

import exceptions.BadRequestException;

public class RequestTest {

	private static String getRequest = "GET / HTTP/1.1\r\nHost: 127.0.0.1:1200\r\nConnection: keep-alive\r\nUpgrade-Insecure-Requests: 1\r\nUser-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36\r\nAccept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\r\nAccept-Encoding: gzip, deflate, br\r\nAccept-Language: en-US,en;q=0.9,ar;q=0.8\r\n";
	private static 	String postRequest = "POST / HTTP/1.1\r\nHost: 127.0.0.1:1200\r\nConnection: keep-alive\r\nUpgrade-Insecure-Requests: 1\r\nUser-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36\r\n";
	private static String getRequest1_0 = "GET /blog.html HTTP/1.0\r\nHost: 127.0.0.1\r\nUpgrade-Insecure-Requests: 1\r\nUser-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36\r\nAccept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\r\nAccept-Encoding: gzip, deflate, br\r\nAccept-Language: en-US,en;q=0.9,ar;q=0.8\r\n";
	private static 	String postRequest1_0 = "POST /form.php HTTP/1.0\r\nHost: 127.0.0.1\r\nUpgrade-Insecure-Requests: 1\r\nUser-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36\r\n";
	private static String badrequest = "GET /blog.html ghdfg HTTP/1.0\r\nHost : 127.0.0.1\r\n Upgra de-Insecure-Requests: 1\r\nUser-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36\r\nAccept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\r\nAccept-Encoding: gzip, deflate, br\r\nAccept-Language: en-US,en;q=0.9,ar;q=0.8\r\n";

	
	/**
	 * Checks if the Client class parses the requests correctly 
	 */
	@Test(timeout = 100)
	public void ParseRequestReturnTypeTest()
	{
		Client client = new Client(null, null);
		Class<?> clientclass = client.getClass();
		try {

			
			Class<?>[] arr = new Class<?>[1];/*to suppress warning*/
			arr[0] = String.class;
			Method method =  clientclass.getDeclaredMethod("parseRequest", arr);

			method.setAccessible(true);
			
			// checks that it will reject arbitrary string
			Request request = (Request) method.invoke(client, "blblbl");
			
			
			assertNull("Expects a NULL request", request);
			 
			//checks that it will return a Get object
			request = (Request) method.invoke(client, getRequest);
			assertTrue("Expects a Get Object", request instanceof Get);

			// checks that it will return a Get object
			request = (Request) method.invoke(client, postRequest);
			assertTrue("Expects a Get Object", request instanceof Post);

			
		} catch (Exception e)
		{
			fail("An Exception was thrown");
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks that the Request class parses the http request correctly
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	@Test(timeout = 100)
	public void RequestParseTest() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		Client client = new Client(null, null);
		Class<?> clientclass = client.getClass();

			Class<?>[] arr = new Class<?>[1];/*to suppress warning*/
			arr[0] = String.class;
			Method method =  clientclass.getDeclaredMethod("parseRequest", arr);

			method.setAccessible(true);
						 
			/*
			 *  Creates a Get object and checks it's parameters for correct values
			 *  
			 */
			Request request = (Request) method.invoke(client, getRequest);
			assertTrue("The expected Url is \"/\" got " + request.getUrl()
					+"instead" , request.getUrl().equals("/")); /*checks the URL*/
			
			assertTrue("The expected Version is \"HTTP/1.1\" got " +
					request.getUrl() +"instead" , request.getVersion().equals("HTTP/1.1")); /*checks the http version*/

			assertTrue("The expected Keep-alive is \"True\" got " +
					request.isKeep_alive() +"instead" , request.isKeep_alive()); /*checks the http keep alive*/
			assertTrue("The expected Host is \"127.0.0.1:1200\" got " +
					request.getHost() +"instead" , request.getHost().equals("127.0.0.1:1200")); /*checks the http host*/
			
			/*
			 * Creates Post and checks it's parameters for correct values
			 * 
			 *  
			 */
			request = (Request) method.invoke(client, postRequest);
			assertTrue("The expected Url is \"/\" got " + request.getUrl()
					+"instead" , request.getUrl().equals("/")); /*checks the URL*/
			
			assertTrue("The expected Version is \"HTTP/1.1\" got " +
					request.getUrl() +"instead" , request.getVersion().equals("HTTP/1.1")); /*checks the http version*/

			assertTrue("The expected Keep-alive is \"True\" got " +
					request.isKeep_alive() +"instead" , request.isKeep_alive()); /*checks the http keep alive*/
			assertTrue("The expected Host is \"127.0.0.1:1200\" got " +
					request.getHost() +"instead" , request.getHost().equals("127.0.0.1:1200")); /*checks the http host*/
			
			/*
			 * Creates get request HTTP 1.0 and checks it's parameters for correct values
			 * 
			 *  
			 */
			request = (Request) method.invoke(client, getRequest1_0);
			assertTrue("The expected Url is \"/blog.html\" got " + request.getUrl()
					+"instead" , request.getUrl().equals("/blog.html")); /*checks the URL*/
			
			assertTrue("The expected Version is \"HTTP/1.0\" got " +
					request.getUrl() +"instead" , request.getVersion().equals("HTTP/1.0")); /*checks the http version*/

			assertTrue("The expected Keep-alive is \"False\" got " +
					request.isKeep_alive() +"instead" , !request.isKeep_alive()); /*checks the http keep alive*/
			assertTrue("The expected Host is \"127.0.0.1\" got " +
					request.getHost() +"instead" , request.getHost().equals("127.0.0.1")); /*checks the http host*/

			
			/*
			 * Creates post request HTTP 1.0 and checks it's parameters for correct values
			 * 
			 *  
			 */
			request = (Request) method.invoke(client, postRequest1_0);
			assertTrue("The expected Url is \"/form.php\" got " + request.getUrl()
					+"instead" , request.getUrl().equals("/form.php")); /*checks the URL*/
			
			assertTrue("The expected Version is \"HTTP/1.0\" got " +
					request.getUrl() +"instead" , request.getVersion().equals("HTTP/1.0")); /*checks the http version*/

			assertTrue("The expected Keep-alive is \"False\" got " +
					request.isKeep_alive() +"instead" , !request.isKeep_alive()); /*checks the http keep alive*/
			assertTrue("The expected Host is \"127.0.0.1\" got " +
					request.getHost() +"instead" , request.getHost().equals("127.0.0.1")); /*checks the http host*/
			
			
			/*
			 * bad request checks that it throw an exception
			 * 
			 *  
			 */
			try
			{
				request = new Get(badrequest);
				// this line shouldn't be reached
				fail("excepts it to throw a bad request exception");
			}
			catch(BadRequestException e)
			{

			}
			
	}
	
	/**
	 * checks if the correct requests gets added to the queue in order
	 * @throws IOException 
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws InterruptedException 
	 */
	@Test(timeout = 5000)
	public void RequestsQueueTest() throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InterruptedException {
		
		// starts up the server on port 1200
		Server server = new Server(1200);
		GenericThread serverThread = new GenericThread(server);
		serverThread.start();
		
		// opens 10 connections to the server
        Socket[] clientSocket = new Socket[10]; 
        for(int i = 0; i < clientSocket.length; i++)
        {
        	clientSocket[i] = new Socket("127.0.0.1", 1200); 
        	Thread.sleep(10);
        }
        
        // sends 10 request alternating types
        for(int i = 0; i < clientSocket.length; i++)
        {
        	
	        DataOutputStream outToServer = 
				      new DataOutputStream(clientSocket[i].getOutputStream());
	        if(i % 2 == 0)
	        {
	        	outToServer.writeBytes(getRequest + '\n');
	        }
	        else
	        {
	        	outToServer.writeBytes(postRequest + '\n');	
	        }
        	Thread.sleep(100);

	       
        }
     
        
        Class<?> serverClass = server.getClass();
        Field requestsprocessorField = serverClass.getDeclaredField("requestsprocessor");
        requestsprocessorField.setAccessible(true);
        RequestsProcessor requestsprocessor = (RequestsProcessor) requestsprocessorField.get(server);
        
        
        Class<?> procClass = requestsprocessor.getClass();
        Field listField = procClass.getDeclaredField("queue");
        listField.setAccessible(true);
        
        @SuppressWarnings("unchecked")
		ArrayList<Request> requestsList = (ArrayList<Request>) listField.get(requestsprocessor);
        
        int i = 0;
        // checks the queue of the server
        for(Request req : requestsList)
        {
        	if(i % 2 == 0 && !(req instanceof Get))
        	{
        		fail("The Queue does not have the correct order");
        	}
        	else if(i % 2 != 0 && !(req instanceof Post))
        	{
        		fail("The Queue does not have the correct order");

        	}
        	i++;
        }
        	
	
	
	}
	

	

	
}
