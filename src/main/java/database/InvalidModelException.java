package database;

public class InvalidModelException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5158979750137041017L;

	public InvalidModelException()
	{
		super();
	}

	public InvalidModelException(String message)
	{
		super(message);
	}

	public InvalidModelException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public InvalidModelException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidModelException(Throwable cause)
	{
		super(cause);
	}

}
