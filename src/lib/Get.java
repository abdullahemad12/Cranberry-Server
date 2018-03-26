package lib;

import java.net.Socket;

import exceptions.BadRequestException;

public class Get extends Request{

	
	public Get(String request, Socket socket) throws BadRequestException {
		super(request, socket);
		// TODO: parse parameters
	}

	public Object CreateResponse()
	{
		return null;
	}
}
