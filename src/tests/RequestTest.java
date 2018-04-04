package tests;

import static org.junit.Assert.*;


import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;

import lib.Get;
import lib.Parameter;
import lib.Post;
import lib.Request;
import main.Server;
import model.Client;
import model.RequestsProcessor;

import org.junit.Test;

import exceptions.BadRequestException;

public class RequestTest {

	private static String getRequest = "GET / HTTP/1.1\r\nHost: 127.0.0.1:1200\r\nConnection: keep-alive\r\nUpgrade-Insecure-Requests: 1\r\nUser-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36\r\nAccept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\r\nAccept-Encoding: gzip, deflate, br\r\nAccept-Language: en-US,en;q=0.9,ar;q=0.8\r\n";
	private static 	String postRequest = "POST / HTTP/1.1\r\nHost: 127.0.0.1:1200\r\nConnection: keep-alive\r\nUpgrade-Insecure-Requests: 1\r\nUser-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36\r\n";
	private static String getRequest1_0 = "GET /blog.html HTTP/1.0\r\nHost: 127.0.0.1\r\nUpgrade-Insecure-Requests: 1\r\nUser-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36\r\nAccept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\r\nAccept-Encoding: gzip, deflate, br\r\nAccept-Language: en-US,en;q=0.9,ar;q=0.8\r\n";
	private static 	String postRequest1_0 = "POST /form.php HTTP/1.0\r\nHost: 127.0.0.1\r\nUpgrade-Insecure-Requests: 1\r\nUser-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36\r\nCookie: PHPSESSID=rinfjjd4k6lhr4upd23cr7gld0; remember_web_59ba36addc2b2f9401580f014c7f58ea4e30989d=eyJpdiI6Im5xVzM3MFFqWTM4aWNpbEo2M3dTa2c9PSIsInZhbHVlIjoiM1wvUUs5aitTYzNIaUhWaWFHTDl1bXo3N1hYaEZJSSt4aVhpaDJITHY5RTFVQ042VlZJSXRreThnNWlKUkVNQ21TeXRQMXBsSnFRTzU3NmlzK2JmV28xQ3FMdHE3ZGNlSGN0Zyt3b0VDNERzPSIsIm1hYyI6ImJlZWEyMDVmYTM0MmJjMTg2MTliMDhjZTRlMmMwM2RiZGMyNjRjMTlhNmNjNThmNWY5OWNkN2I4YTk1YTIwMzkifQ%3D%3D; io=oiHP_DWV-YejC-g9AAAI; _blog_session=WmZrdGZVRVJNOFdDMFNKSkUyNHdtK2Y4ZG9CenNXMWVlWmxHcDYxaGZ5Mmg4SHY5Q3BxNnBaWk1aS2owMnIyWmFBYVE1VkoybjZXMFhBeFhsSGdFemdiQVh4cXFjYUZOU3hqL2pzcTNoSVgxR0dwYXZ6ZjJyeXVaVmZPbENuLzVvbTVNM0ZBVk5HUzhlcVN1djJTSkZBPT0tLWFQTkxTL2FjTGN6aHdrV1orK2hxY2c9PQ%3D%3D--ef30453531372a8f07070dcd0be01e8fb5d3cf21\r\n";
	private static String badrequest = "GET /blog.html ghdfg HTTP/1.0\r\nHost : 127.0.0.1\r\n Upgra de-Insecure-Requests: 1\r\nUser-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36\r\nAccept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\r\nAccept-Encoding: gzip, deflate, br\r\nAccept-Language: en-US,en;q=0.9,ar;q=0.8\r\n";
	private static String getRequestParams = "GET /test.php?name=abdullah&age=12&friend=habob HTTP/1.1\r\nHost: 127.0.0.1:1200\r\nConnection: keep-alive\r\nUpgrade-Insecure-Requests: 1\r\nUser-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36\r\nAccept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\r\nAccept-Encoding: gzip, deflate, br\r\nAccept-Language: en-US,en;q=0.9,ar;q=0.8\r\n";

	/**
	 * Checks if the Client class parses the requests correctly 
	 */
	@Test(timeout = 100)
	public void ParseRequestReturnTypeTest()
	{
		Server server = null;
		try {
			server = new Server(1200, "public");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Client client = new Client(null, null, server);
		Class<?> clientclass = client.getClass();
		try {

			Class<?>[] arr = new Class<?>[3];/*to suppress warning*/
			arr[0] = String.class;
			arr[1] = Socket.class;
			arr[2] = Server.class;
			Method method =  clientclass.getDeclaredMethod("parseRequest", arr);

			method.setAccessible(true);
			
			// checks that it will reject arbitrary string
			Request request = (Request) method.invoke(client, "blblbl", null,server);
			
			
			assertNull("Expects a NULL request", request);
			 
			//checks that it will return a Get object
			request = (Request) method.invoke(client, getRequest, null, server);
			assertTrue("Expects a Get Object", request instanceof Get);

			// checks that it will return a Get object
			request = (Request) method.invoke(client, postRequest, null, server);
			assertTrue("Expects a Get Object", request instanceof Post);

			
		} catch (Exception e)
		{
			e.printStackTrace();
			fail("An Exception was thrown");
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
	public void GETRequestParseTest() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{

		Server server = null;
		try {
			server = new Server(1220, "public");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Client client = new Client(null, null, server);
		Class<?> clientclass = client.getClass();

			Class<?>[] arr = new Class<?>[3];/*to suppress warning*/
			arr[0] = String.class;
			arr[1] = Socket.class;
			arr[2] = Server.class;
			Method method =  clientclass.getDeclaredMethod("parseRequest", arr);

			method.setAccessible(true);
						 
			/*
			 *  Creates a Get object and checks it's parameters for correct values
			 *  
			 */
			Request request = (Request) method.invoke(client, getRequest, null, server);
			assertTrue("The expected Url is \"public/index.php\" got " + request.getUrl()
					+"instead" , request.getUrl().equals("public/index.php")); /*checks the URL*/
			
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
			request = (Request) method.invoke(client, getRequest1_0, null, server);
			assertTrue("The expected Url is \"public/blog.html\" got " + request.getUrl()
					+"instead" , request.getUrl().equals("public/blog.html")); /*checks the URL*/
			
			assertTrue("The expected Version is \"HTTP/1.0\" got " +
					request.getUrl() +"instead" , request.getVersion().equals("HTTP/1.0")); /*checks the http version*/

			assertTrue("The expected Keep-alive is \"False\" got " +
					request.isKeep_alive() +"instead" , !request.isKeep_alive()); /*checks the http keep alive*/
			assertTrue("The expected Host is \"127.0.0.1\" got " +
					request.getHost() +"instead" , request.getHost().equals("127.0.0.1")); /*checks the http host*/
		
	}
	
	
	/**
	 * Checks that the Request class parses the http post request correctly
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	@Test(timeout = 100)
	public void PostRequestParseTest() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{

		Server server = null;
		try {
			server = new Server(1203, "public");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Client client = new Client(null, null, server);
		Class<?> clientclass = client.getClass();

			Class<?>[] arr = new Class<?>[3];/*to suppress warning*/
			arr[0] = String.class;
			arr[1] = Socket.class;
			arr[2] = Server.class;
			Method method =  clientclass.getDeclaredMethod("parseRequest", arr);

			method.setAccessible(true);
		
		/*
		 * Creates Post and checks it's parameters for correct values
		 * 
		 *  
		 */
		Request request = (Request) method.invoke(client, postRequest, null, server);
		assertTrue("The expected Url is \"public/index.php\" got " + request.getUrl()
				+"instead" , request.getUrl().equals("public/index.php")); /*checks the URL*/
		
		assertTrue("The expected Version is \"HTTP/1.1\" got " +
				request.getUrl() +"instead" , request.getVersion().equals("HTTP/1.1")); /*checks the http version*/

		assertTrue("The expected Keep-alive is \"True\" got " +
				request.isKeep_alive() +"instead" , request.isKeep_alive()); /*checks the http keep alive*/
		assertTrue("The expected Host is \"127.0.0.1:1200\" got " +
				request.getHost() +"instead" , request.getHost().equals("127.0.0.1:1200")); /*checks the http host*/
		
		

		/*
		 * Creates post request HTTP 1.0 and checks it's parameters for correct values
		 * 
		 *  
		 */
		request = (Request) method.invoke(client, postRequest1_0, null, server);
		assertTrue("The expected Url is \"public/form.php\" got " + request.getUrl()
				+"instead" , request.getUrl().equals("public/form.php")); /*checks the URL*/
		
		assertTrue("The expected Version is \"HTTP/1.0\" got " +
				request.getUrl() +"instead" , request.getVersion().equals("HTTP/1.0")); /*checks the http version*/

		assertTrue("The expected Keep-alive is \"False\" got " +
				request.isKeep_alive() +"instead" , !request.isKeep_alive()); /*checks the http keep alive*/
		assertTrue("The expected Host is \"127.0.0.1\" got " +
				request.getHost() +"instead" , request.getHost().equals("127.0.0.1")); /*checks the http host*/
	}
	
	
	/** 
	 * Checks that the Request Class throws exception on bad requests
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	@Test(timeout = 100)
	public void BadRequestParseTest() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{

		/*
		 * bad request checks that it throw an exception
		 * 
		 *  
		 */
		try
		{
			@SuppressWarnings("unused")
			Request request = new Get(badrequest, null, "public");
			// this line shouldn't be reached
			fail("excepts it to throw a bad request exception");
		}
		catch(Exception e)
		{
			e.printStackTrace();
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
		Server server = new Server(1205, "public");
		GenericThread serverThread = new GenericThread(server);
		serverThread.start();
		
		// opens 10 connections to the server
        Socket[] clientSocket = new Socket[10]; 
        for(int i = 0; i < clientSocket.length; i++)
        {
        	clientSocket[i] = new Socket("127.0.0.1", 1205); 
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
        	Thread.sleep(5);

	       
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
	

	/**
	 * Makes sure the get Request parses the parameter correctly
	 */
	@Test (timeout = 1000) 
	public void getParametersTest()
	{
		try {
			Request request = new Get(getRequestParams, null, "public");
			ArrayList<Parameter> params = request.getMethod_parameters();
			assertEquals("Expected 3 paramters got " + params.size(), 3, params.size());
			ArrayList<Parameter> expected_parameters = new ArrayList<Parameter>();
			expected_parameters.add(new Parameter("name", "abdullah"));
			expected_parameters.add(new Parameter("age", "12"));
			expected_parameters.add(new Parameter("friend", "habob"));

			for(Parameter param : params)
			{
				boolean valid = (param.getKey().equals("name") && param.getValue().equals("abdullah"))
						||  (param.getKey().equals("age") && param.getValue().equals("12"))
						||  (param.getKey().equals("friend") && param.getValue().equals("habob"));
				assertTrue("The parameters did not match the expected ones", valid);
			}
			
					
		} catch (BadRequestException e) {
			e.printStackTrace();
			fail("Unexpected Exception was thrown");
		}
	}



	
}
