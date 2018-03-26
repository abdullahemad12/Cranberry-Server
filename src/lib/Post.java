package lib;

import java.net.Socket;

import exceptions.BadRequestException;

public class Post extends Request{

	public Post(String request, Socket socket) throws BadRequestException {
		super(request, socket);
	}

	public Object CreateResponse()
	{
		return null;
	}
}
