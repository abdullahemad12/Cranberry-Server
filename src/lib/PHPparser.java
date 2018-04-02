package lib;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

/**
 * Contains static functions that parses PHP and stores it in an 
 * array of bytes
 *  
 * @author abdullah
 *
 */
public class PHPparser {

	/**
	 * byte[] -> byte[]
	 * parses php script and executes it
	 * @return a buffer containing html generated from a php script with the essential headers
	 */
	public static byte[] runPHP(byte[] rawPHP) throws IOException
	{
		ProcessBuilder php_cgi = new ProcessBuilder("php-cgi7.0");
		Process php_process = php_cgi.start();
		
        OutputStream stdin = php_process.getOutputStream(); /*stdin of the php process*/
        InputStream stdout = php_process.getInputStream(); /*stdout of the php process*/
        
        BufferedWriter std_in_writer = new BufferedWriter(new OutputStreamWriter(stdin));

        
        std_in_writer.write(new String(rawPHP, StandardCharsets.UTF_8));
        std_in_writer.flush();
        std_in_writer.close();
        
        
       
        byte[] generated_php = readBytes(stdout);
        String php_string = new String(generated_php, "UTF-8");
        
        return generateHttpHeader(php_string, rawPHP.length);
	}
	


	/**
	 * InputStream -> byte[]
	 * reads all the input from an Input stream and stores it in a buffer
	 * @param in
	 * @return
	 * @throws IOException 
	 */
	private static byte[] readBytes(InputStream in) throws IOException
	{
		byte[] buffer = new byte[8192];
	    int bytesRead;
	    ByteArrayOutputStream output = new ByteArrayOutputStream();
	    while ((bytesRead = in.read(buffer)) != -1)
	    {
	        output.write(buffer, 0, bytesRead);
	    }
	    return output.toByteArray();
	}
	
	/**
	 * Given the generated http response, produces essentail server headers
	 */

	private static byte[] generateHttpHeader(String rawResponse, int bufferLength)
	{
		Date dateTemp = new Date();
		String[] data = new String[6];
		data[0] = "HTTP/1.1 200 OK \r\n";
		data[1] = "Date: "+dateTemp.toString()+"\r\n";
		data[2] = "Last-Modified: "+dateTemp.toString()+"\r\n";
		data[3] = "Server: cranberry/1.0\r\n";
		data[4] = "Content-Length: " + bufferLength + "\r\n";
		data[5] = "Connection: closed\r\n";
		String tmp = "";
		for (int i = 0; i < data.length; i++) {
			 tmp += data[i];
		}
		String response = tmp + rawResponse;
		return response.getBytes();
	}
	
	private static String setCookiesPHPcode()
	{
		return null;
		//String code = "<?php ?>"
	}
	

}
