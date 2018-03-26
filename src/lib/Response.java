package lib;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.UnsupportedAddressTypeException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import exceptions.NotFoundException;
import exceptions.UnsupportedMediaTypeException;

/**
 * This represent a response for an http request
 * 
 * This class maybe used more then one time for many connections in case of 
 * @author abdullah
 *
 */
public class Response {

	String url; 
	ArrayList<Parameter> cookies;
	String mimeType; /*mime type of the requested resource*/
	byte[] rawBuffer; /*holds the raw content read from directly from the file*/
	byte[] Buffer; /*holds the response that gets sent to the user*/ 
	public Response(String url, ArrayList<Parameter> cookies)
	{
		this.url = url;
		this.cookies = cookies;
	}
	
	
	/** 
	 * Void -> void 
	 * @throws IOException 
	 * @throws NotFoundException 
	 * 
	 */
	private void LoadData() throws IOException, NotFoundException
	{
		// check that the file exists
		File file = new File(this.url);
		if(!file.exists())
		{
			throw new NotFoundException(this.url);
		}
		
		// sets the Type 
		final URL url = file.toURI().toURL();
	    final URLConnection connection = url.openConnection();
	    this.mimeType = connection.getContentType();
	    
	    
	    // loads the file as to the raw buffer
	    this.rawBuffer = Files.readAllBytes(Paths.get(this.url));
	    
	}
	
	/**
	 *  void -> void
	 *  uses the url and the cookies to generate an HTTP request 
	 *  The result gets stored in the buffer
	 *  
	 *  Disclaimer: this Method was created based on: 
	 *  https://dzone.com/articles/determining-file-types-java
	 */
	private void generateResponse()
	{ 
	     
		   
	}
	
	/**
	 * Socket -> void
	 * Sends a Generated Response to the destination socket
	 * 
	 * @param socket: the destination TCP socket
	 */
	public void sendResponse(Socket socket)
	{
		
	}

}
