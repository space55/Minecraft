package world;

public class NotGeneratedException extends RuntimeException
{
	// Because laziness
	private static final long serialVersionUID = -6778597399689551907L;

	public NotGeneratedException()
	{
		super();
	}

	public NotGeneratedException(String message)
	{
		super(message);
	}

	public NotGeneratedException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public NotGeneratedException(Throwable cause)
	{
		super(cause);
	}
}
