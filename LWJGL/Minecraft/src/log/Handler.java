package log;

public class Handler implements Thread.UncaughtExceptionHandler
{
	public void uncaughtException(Thread t, Throwable e)
	{
		e.printStackTrace();
		Logger.writeException(t, e);
	}
}
