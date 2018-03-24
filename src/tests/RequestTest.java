package tests;

import static org.junit.Assert.*;


import java.lang.reflect.Method;

import lib.Client;
import lib.Get;
import lib.Post;
import lib.Request;

import org.junit.Test;

public class RequestTest {

	private static String getRequest = "GET / HTTP/1.1\r\nHost: 127.0.0.1:1200\r\nConnection: keep-alive\r\nUpgrade-Insecure-Requests: 1\r\nUser-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36\r\nAccept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\r\nAccept-Encoding: gzip, deflate, br\r\nAccept-Language: en-US,en;q=0.9,ar;q=0.8\r\n";
	private static 	String postRequest = "POST / HTTP/1.1\r\nHost: 127.0.0.1:1200\r\nConnection: keep-alive\r\nUpgrade-Insecure-Requests: 1\r\nUser-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36\r\n";
 
	
	
	/*
	 * Checks if the Client class parses the requests correctly 
	 */
	@Test(timeout = 100)
	public void ParseRequest()
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
	 * checks if the correct requests gets added to the queue in order
	 */
	@Test(timeout = 1000)
	public void RequestsQueueTest() {
		
		// requests that will be sent to the server
		fail();
	
	
	
	}
	

	

	
}
