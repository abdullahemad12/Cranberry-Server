package lib;

import java.net.Socket;


import exceptions.BadRequestException;

public class Post extends Request{

	public Post(String request, Socket socket, String server_root) throws BadRequestException {
		super(request, socket, server_root);	
	}
	
}
