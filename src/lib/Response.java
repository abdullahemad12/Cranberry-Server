package lib;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;

import exceptions.NotFoundException;

/**
 * This represent a response for an http request
 * 
 * This class maybe used more then one time for many connections in case of
 * 
 * @author abdullah
 *
 */
public class Response {

	String url;
	ArrayList<Parameter> cookies;
	String mimeType; /* mime type of the requested resource */
	byte[] rawBuffer; /* holds the raw content read from directly from the file */
	byte[] buffer; /* holds the response that gets sent to the user */

	public Response(String url, ArrayList<Parameter> cookies) throws IOException, NotFoundException {
		this.url = url;
		this.cookies = cookies;
		this.LoadData();

	}

	/**
	 * void -> void This function loads The file that is going to be sent with the
	 * response
	 * 
	 * Disclaimer: this Method was created based on:
	 * https://dzone.com/articles/determining-file-types-java
	 * 
	 * @throws IOException
	 * @throws NotFoundException
	 * 
	 */
	private void LoadData() throws IOException, NotFoundException {
		// check that the file exists
		File file = new File(this.url);
		if (!file.exists()) {
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
	 * void -> void uses the url and the cookies to generate an HTTP request The
	 * result gets stored in the buffer
	 * 
	 * 
	 */
	private void generateResponse() {

		Date dateTemp = new Date();
		String[] data = new String[7];
		data[0] = "HTTP/1.1 200 OK\r\n";
		data[1] = "Date: "+dateTemp.toString()+"\r\n";
		data[2] = "Last-Modified: "+dateTemp.toString()+"\r\n";
		data[3] = "Server: cranberry/1.0\r\n";
		data[4] = "Content-Length: " + rawBuffer.length + "\r\n";
		data[5] = "Connection: closed\r\n";
		data[6] = "Content-Type: " + mimeType + "\r\n\r\n";
		String tmp = "";
		for (int i = 0; i < data.length; i++) {
			 tmp += data[i];
		}
		byte[] tmpByte = tmp.getBytes();
		buffer = new byte[tmpByte.length+rawBuffer.length];
		for (int i = 0; i < buffer.length; i++) {
			if ( i < tmpByte.length )
			buffer[i] = tmpByte[i];
			else buffer[i] = rawBuffer[i-(tmpByte.length)] ;
		}
		
		new String(buffer, StandardCharsets.UTF_8);
	}

	/**
	 * Socket -> void 
	 * Sends a Generated Response to the destination socket
	 * @param socket:the destination TCP socket
	 * @throws IOException
	 */
	public void sendResponse(Socket socket) throws IOException {
		
		generateResponse();
		socket.getOutputStream().write(buffer);
		buffer = null;
	}
	
	/**
	 * Socket -> void
	 * Sends 404 Not found to the client
	 * @param Socket: the destination TCP socket
	 *  @throws IOException
	 */
	public void sendNotFoundError(Socket socket)
	{
		//TODO: Moghazi
	}
	
	

}
