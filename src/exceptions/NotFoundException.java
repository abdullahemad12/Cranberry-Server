package exceptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Date;

public class NotFoundException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Socket socket;
	public NotFoundException(String message, Socket socket)
	{
		super(message + " Was Not Found\n");
		this.socket = socket;
		try {
			this.sendNotfound();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**void -> void
	 * Loads the notfound html file to the rawbuffer
	 * @throws IOException 
	 * 
	 */
	private void sendNotfound() throws IOException
	{
		// just load the NOT found html file and return

				
		InputStream in = getClass().getResourceAsStream("/notfound.php"); 
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String notfoundhtml = "";
		String tmp;
		while((tmp = reader.readLine()) != null)
		{
			notfoundhtml = notfoundhtml + tmp + "\n";
		}
		byte[] rawBuffer = notfoundhtml.getBytes();
		
		Date dateTemp = new Date();
		String[] data = new String[7];
		data[0] = "HTTP/1.1 404 Not Found\r\n";
		data[1] = "Date: "+dateTemp.toString()+"\r\n";
		data[2] = "Last-Modified: "+dateTemp.toString()+"\r\n";
		data[3] = "Server: cranberry/1.0\r\n";
		data[4] = "Content-Length: " + rawBuffer.length + "\r\n";
		data[5] = "Connection: closed\r\n";
		data[6] = "Content-Type: text/html\r\n\r\n";
		tmp = "";
		for (int i = 0; i < data.length; i++) {
			 tmp += data[i];
		}
		byte[] tmpByte = tmp.getBytes();
		byte[] buffer = new byte[tmpByte.length+ notfoundhtml.length()];
		for (int i = 0; i < buffer.length; i++) {
			if ( i < tmpByte.length )
			buffer[i] = tmpByte[i];
			else buffer[i] = rawBuffer[i-(tmpByte.length)] ;
		}
		socket.getOutputStream().write(buffer);
	}

}
