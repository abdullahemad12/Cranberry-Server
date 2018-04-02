package tests;

import static org.junit.Assert.*;

import java.io.DataOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;


import lib.Response;
import main.Server;

import org.junit.Test;

import exceptions.NotFoundException;

public class ResponseTest {

		private static String getIndex = "GET / HTTP/1.1\r\nHost: 127.0.0.1:1200\r\nConnection: keep-alive\r\nUpgrade-Insecure-Requests: 1\r\nUser-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36\r\nAccept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\r\nAccept-Encoding: gzip, deflate, br\r\nAccept-Language: en-US,en;q=0.9,ar;q=0.8\r\n";
		private static String getImage = "GET /test.jpg HTTP/1.1\r\nHost: 127.0.0.1:1200\r\nConnection: keep-alive\r\nUpgrade-Insecure-Requests: 1\r\nUser-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36\r\nAccept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\r\nAccept-Encoding: gzip, deflate, br\r\nAccept-Language: en-US,en;q=0.9,ar;q=0.8\r\n";
		private static String getNonExistent = "GET /nonexistentfile.php HTTP/1.1\r\nHost: 127.0.0.1:1200\r\nConnection: keep-alive\r\nUpgrade-Insecure-Requests: 1\r\nUser-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36\r\nAccept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\r\nAccept-Encoding: gzip, deflate, br\r\nAccept-Language: en-US,en;q=0.9,ar;q=0.8\r\n";

		@Test(timeout = 1000)
		public void LoadFileTest() throws IOException, NotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
			
			// Get the private method from the response class
			Response response = new Response("public/index.php", null);
			Class<?> responseClass = response.getClass();
			
			Class<?>[] arr = null;
			
			// invokes the load file method
			Method method =  responseClass.getDeclaredMethod("LoadData", arr);
			method.setAccessible(true);
			method.invoke(response);
	
			// gets the raw buffer in order to compare it to the actaul file
	        Field requestsprocessorField = responseClass.getDeclaredField("rawBuffer");
	        requestsprocessorField.setAccessible(true);
	        byte[] rawBuffer = (byte[]) requestsprocessorField.get(response);
	        
	        
	        String actual = new String(rawBuffer, "US-ASCII");
	        
	        byte[] encoded = Files.readAllBytes(Paths.get("public/index.php"));
	        String expected = new String(encoded, "US-ASCII");
	                
	        assertTrue("The Two Strings Didn't Match", actual.equals(expected));
		}
		
		@Test(timeout = 1000)
		public void mimeTypeTest() throws IOException, NotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		{
			// Get the private method from the response class
			Response response = new Response("public/index.php", null);
			Class<?> responseClass = response.getClass();
			
			Class<?>[] arr = null;
			
			// invokes the load file method
			Method method =  responseClass.getDeclaredMethod("LoadData", arr);
			method.setAccessible(true);
			method.invoke(response);
	
			// gets the raw buffer in order to compare it to the actaul file
	        Field requestsprocessorField = responseClass.getDeclaredField("mimeType");
	        requestsprocessorField.setAccessible(true);
	        String mimeType = (String) requestsprocessorField.get(response);
	        
	                
	        assertTrue("Expected text/html got" + mimeType, mimeType.equals("text/html"));	
	        
	        
	        
	        // tries loading an image
	        Response response1 = new Response("public/test.jpg", null);
			Class<?> responseClass1 = response1.getClass();
			
			
			// invokes the load file method
			Method method1 =  responseClass1.getDeclaredMethod("LoadData", arr);
			method1.setAccessible(true);
			method1.invoke(response);
	
			// gets the raw buffer in order to compare it to the actaul file
	        Field requestsprocessorField1 = responseClass1.getDeclaredField("mimeType");
	        requestsprocessorField1.setAccessible(true);
	        String mimeType1 = (String) requestsprocessorField1.get(response1);
	        
	        assertTrue("Expected image/jpg got " + mimeType1, mimeType1.equals("image/jpeg"));	
	        
		}
		
	}
		
	@Test(timeout = 1000)
	public void HTTPresponseTest() throws IOException, InterruptedException
	{
		
		// starts up the server on port 1200
		Server server = new Server(1202, "public");
		GenericThread serverThread = new GenericThread(server);
		serverThread.start();
		
		// opens 5 connections to the server
        Socket[] clientSocket = new Socket[5]; 
        for(int i = 0; i < clientSocket.length; i++)
        {
        	clientSocket[i] = new Socket("127.0.0.1", 1202); 
        	Thread.sleep(10);
        }
    
        
        
      //  String httpreq = "HTTP/1.1 200 OK\r\nDate: Mon, 27 Jul 2009 12:28:53 GMT\r\nServer: Cranberry/1.1\r\nLast-Modified: Wed, 22 Jul 2009 19:15:56 GMT\r\nContent-Length: 88\r\nContent-Type: text/html\r\nConnection: Closed\r\n";
        //byte[] test = httpreq.getBytes();
       
        
        //assertTrue("Doesn't match",  Helpers.validateHeader(test));
        /*TODO: load the expected response Response*/
        byte[] indexPage = Files.readAllBytes(Paths.get("public/index.php"));
        byte[] imageFile = Files.readAllBytes(Paths.get("public/test.jpg"));
        
        for(int i = 0; i < clientSocket.length; i++)
        {
        	if(i % 2 == 0)
        	{
		        DataOutputStream outToServer = 
					      new DataOutputStream(clientSocket[i].getOutputStream());
		        	outToServer.writeBytes(getIndex + '\n'); 
		        	  InputStream stream = clientSocket[i].getInputStream();
		        	  byte[] data = new byte[1000];
		        	  stream.read(data);
		        	  assertTrue("Incorrect Header Fields", Helpers.validateHeader(data));
		        	  
		        	  // compares the body
		      		  String response = new String(data, "UTF-8");
		      		  String body = response.split("\\r\\n\\r\\n")[1];
		      		  
		      		  byte[] bodyBuffer = body.getBytes();
		        	  // compare the expected responses if false return false
		      		  boolean cmpType = Helpers.bytesCmp(bodyBuffer, indexPage, indexPage.length);
		      		  assertTrue("Incorrect Buffer", cmpType);
        	}
        	else
        	{

		        DataOutputStream outToServer = 
					      new DataOutputStream(clientSocket[i].getOutputStream());
		        	outToServer.writeBytes(getImage + '\n'); 
		        	  InputStream stream = clientSocket[i].getInputStream();
		        	  byte[] data = new byte[30000];
		        	  
		        	  stream.read(data);
		              
		        	  assertTrue("Incorrect Header Fields", Helpers.validateHeader(data));
		        	  
		        	  // compares the body
		      		  String response = new String(data, "UTF-8");
		      		  String body = response.split("\\r\\n\\r\\n")[1];
		      		  byte[] bodyBuffer = body.getBytes();
		        	  // compare the expected responses if false return false
		      		  boolean cmpType = Helpers.bytesCmp(bodyBuffer, imageFile, imageFile.length);

		      		  assertTrue("Incorrect Buffer", !cmpType);      
		      }
        }
	}
	
	@Test (timeout = 1000)
	public void NotFoundTest() throws IOException
	{
        
    	// starts up the server on port 1200
		Server server = new Server(1201, "public");
		GenericThread serverThread = new GenericThread(server);
		serverThread.start();
		
        Socket clientSocket = new Socket("127.0.0.1", 1201); 
        
        byte[] FileNotFound = Files.readAllBytes(Paths.get("html/notfound.php"));
        
        DataOutputStream outToServer = 
			      new DataOutputStream(clientSocket.getOutputStream());

    	outToServer.writeBytes(getNonExistent + '\n'); 

    	InputStream stream = clientSocket.getInputStream();
   	  	byte[] data = new byte[1000];
   	  	stream.read(data);
   	  	assertTrue("Incorrect Header Fields", Helpers.validateHeader(data));
   	  	
   	  	  // compares the body
		  String response = new String(data, "UTF-8");
		  String body = response.split("\\r\\n\\r\\n")[1];
		  
		  byte[] bodyBuffer = body.getBytes();
		  // compare the expected responses if false return false
		  boolean cmpType = Helpers.bytesCmp(bodyBuffer, FileNotFound, FileNotFound.length);
		  assertTrue("Incorrect Buffer", cmpType);  
		  clientSocket.close();
   	  
	}
	
 
}
