package lib;

import exceptions.BadRequestException;

public class Post extends Request{

	public Post(String request) throws BadRequestException {
		super(request);
	}

	public Object CreateResponse()
	{
		return null;
	}
}
