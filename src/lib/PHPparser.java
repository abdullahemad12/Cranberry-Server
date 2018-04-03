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
 * @author Abdullah Emad
 *
 */
public class PHPparser {

	/**
	 * byte[] -> byte[]
	 * parses php script and executes it
	 * @return a buffer containing html generated from a php script with the essential headers
	 */
	public static byte[] runPHP(byte[] rawPHP, ArrayList<Parameter> method_parameter, ArrayList<Parameter> cookies) throws IOException
	{
		ProcessBuilder php_cgi = new ProcessBuilder("php-cgi7.0");
		Process php_process = php_cgi.start();
		
        OutputStream stdin = php_process.getOutputStream(); /*stdin of the php process*/
        InputStream stdout = php_process.getInputStream(); /*stdout of the php process*/
        
        BufferedWriter std_in_writer = new BufferedWriter(new OutputStreamWriter(stdin));

        String php_code = new String(rawPHP, StandardCharsets.UTF_8);
        php_code = setCookiesPHPcode(php_code, method_parameter, cookies);
        std_in_writer.write(php_code);
        std_in_writer.flush();
        std_in_writer.close();
        
        
       
        byte[] generated_php = readBytes(stdout);
        String php_string = new String(generated_php, "UTF-8");
        return generateHttpHeader(php_string);
	}
	


	/**
	 * InputStream -> byte[]
	 * reads all the input from an Input stream and stores it in a buffer
	 * @param in
	 * @return
	 * @throws IOException 
	 */
	public static byte[] readBytes(InputStream in) throws IOException
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

	private static byte[] generateHttpHeader(String rawResponse)
	{
		String[] resp = rawResponse.split("\r\n\r\n");
		int length = 0;
		if(resp.length == 2)
		{
			length = resp[1].length();
		}
		else
		{
			length = rawResponse.length();
		}
		Date dateTemp = new Date();
		String[] data = new String[6];
		data[0] = "HTTP/1.1 200 OK \r\n";
		data[1] = "Date: "+dateTemp.toString()+"\r\n";
		data[2] = "Last-Modified: "+dateTemp.toString()+"\r\n";
		data[3] = "Server: cranberry/1.0\r\n";
		data[4] = "Content-Length: " + length + "\r\n";
		data[5] = "Connection: closed\r\n";
		String tmp = "";
		for (int i = 0; i < data.length; i++) {
			 tmp += data[i];
		}
		String response = tmp + rawResponse;
		return response.getBytes();
	}
	
	/**
	 * String -> String
	 * Adds PHP script that initializes the global variables to a given phpscrip
	 * @return
	 */
	private static String setCookiesPHPcode(String phpstr, ArrayList<Parameter> params,  ArrayList<Parameter> cookies)
	{
	
		String code = "<?php $_SERVER[\"REQUEST_METHOD\"] = \"GET\";";
		if(params != null)
		{
			for(Parameter param : params)
			{
				code = code + String.format("$_GET[\"%s\"] = \"%s\"; ",param.getKey(), param.getValue());
				
			}
		}
		if(cookies != null)
		{
			code = code + "\nsession_start();";			
			for(Parameter param : cookies)
			{
	
				code = code + String.format("$_SESSION['%s'] = '%s'; ",param.getKey(), param.getValue());
	
			}
		}
		code = code + "?>";
		return code + phpstr;
	}
	

}
