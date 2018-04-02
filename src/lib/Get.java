package lib;

import java.net.Socket;

import exceptions.BadRequestException;

public class Get extends Request{

	
	public Get(String request, Socket socket, String server_root) throws BadRequestException {
		super(request, socket, server_root);
		// TODO: parse parameters
	}

	public Object CreateResponse()
	{
		return null;
	}
}
