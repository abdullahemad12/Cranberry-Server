package exceptions;

public class UnsupportedMediaTypeException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4223610630224953996L;

	public UnsupportedMediaTypeException(String message)
	{
		super(message + "is of unsupported media type");
	}
}
