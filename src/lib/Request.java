package lib;

import java.net.Socket;
import java.util.ArrayList;

import exceptions.BadRequestException;

abstract public class Request {
	
	private String server_root;
	private Socket socket;
	private String url; 
	private String version;
	private boolean keep_alive;
	private String host;
	private ArrayList<Parameter> cookies;
	private ArrayList<Parameter> method_parameters;
	public Request(String request, Socket socket, String server_root) throws BadRequestException
	{
		this.socket = socket;
		this.keep_alive = false;
		this.server_root = server_root;
		String[] reqLines = request.split("\\r?\\n");
		// extracts information from each line
		for(int i = 0; i < reqLines.length; i++)
		{
			parseRequestLine(reqLines[i]);
		}
	
	}

	
	public Socket getSocket()
	{
		return this.socket;
	}
	/**
	 * String -> void
	 * parses a line from a HTTP request and sets instance variables accordingly
	 * @param str: line to be parsed
	 * @throws BadRequestException 
	 */
	private void parseRequestLine(String str) throws BadRequestException {
		String[] parameters = str.split(" ");
		
		// Method
		if(parameters[0].equals("POST") || parameters[0].equals("GET"))
		{
			// this line should contain the HTTP method, source, Version
			if(parameters.length != 3)
			{
				throw new BadRequestException();
			}
			
			// adjusts the correct relative path
			if(parameters[1].equals("/"))
			{
				this.url = server_root + "/index.php"; 
			}
			else
			{
				this.url = server_root + parameters[1];
			}
			
			this.version = parameters[2];
		}
		// Host
		else if(parameters[0].equals("Host:"))
		{
			if(parameters.length != 2)
			{
				throw new BadRequestException();
			}
			this.host = parameters[1];
		}
		// connection
		else if(parameters[0].equals("Connection:"))
		{
			if(parameters.length != 2)
			{
				throw new BadRequestException();
			}
			if(parameters[1].equals("keep-alive"))
			{
				this.keep_alive = true;
			}
		}
		else if(parameters[0].equals("Cookie:"))
		{
			this.cookies = new ArrayList<Parameter>();
			parseCookies(str);
		}
	}

	/**
	 * String -> void 
	 * parses the cookies 
	 */
	private void parseCookies(String str) throws BadRequestException
	{
		String[] parameters = str.split("; ");
		
		for(int i = 1; i < parameters.length; i++)
		{
			// makes sure it's correctly formatted
			if(!parameters[i].matches("(.*)=(.*)"))
			{
				throw new BadRequestException();
			}
			
			String[] cookiestrs = parameters[i].split("=");
			if(cookiestrs.length != 2)
			{
				throw new BadRequestException();
			}
			Parameter cookie = new Parameter(cookiestrs[0], cookiestrs[1]);
			this.cookies.add(cookie);
		}
	}

	public String getUrl() 
	{
		return url;
	}

	public String getVersion() 
	{
		return version;
	}

	public boolean isKeep_alive() 
	{
		return keep_alive;
	}

	public String getHost() {
		return host;
	}
	public ArrayList<Parameter> getCookies()
	{
		return this.cookies;
	}


	public ArrayList<Parameter> getMethod_parameters() {
		return method_parameters;
	}


	public void setMethod_parameters(ArrayList<Parameter> method_parameters) {
		this.method_parameters = method_parameters;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
