package lib;

import exceptions.BadRequestException;

public class Get extends Request{

	
	public Get(String request) throws BadRequestException {
		super(request);
		// TODO: parse parameters
	}

	public Object CreateResponse()
	{
		return null;
	}
}
