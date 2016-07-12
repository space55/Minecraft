package func;

import com.sun.org.apache.xml.internal.resolver.helpers.Debug;

public class GetCaller
{
	public static String getCallerCallerClassName()
	{
		StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
		String callerClassName = null;
		for (int i = 1; i < stElements.length; i++)
		{
			StackTraceElement ste = stElements[i];
			if (!ste.getClassName().equals(Debug.class.getName())
					&& ste.getClassName().indexOf("java.lang.Thread") != 0)
			{
				if (callerClassName == null)
				{
					callerClassName = ste.getClassName();
				}
				else if (!callerClassName.equals(ste.getClassName()) && !ste.getClassName().equals("io.Logger")
						&& !ste.getClassName().equals("io.Output"))
				{
					return ste.getClassName();
				}
			}
		}
		return null;
	}

	public static int getLineNumber()
	{
		StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
		String callerClassName = null;
		for (int i = 1; i < stElements.length; i++)
		{
			StackTraceElement ste = stElements[i];
			if (!ste.getClassName().equals(Debug.class.getName())
					&& ste.getClassName().indexOf("java.lang.Thread") != 0)
			{
				if (callerClassName == null)
				{
					callerClassName = ste.getClassName();
				}
				else if (!callerClassName.equals(ste.getClassName()) && !ste.getClassName().equals("io.Logger")
						&& !ste.getClassName().equals("io.Output"))
				{
					return ste.getLineNumber();
				}
			}
		}
		return 0;
	}
}
