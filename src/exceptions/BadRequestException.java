package exceptions;

public class BadRequestException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4683589242203615733L;
	
	public BadRequestException()
	{
		super("The request is badly formatted");
	}
	
}
