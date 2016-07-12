package log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Logger implements Runnable
{
	private static BufferedWriter bw = null;
	private static ArrayList<String> buffer = null;

	public static void init()
	{
		buffer = new ArrayList<String>();
		initBW();
	}

	public static void initBW()
	{
		try
		{
			bw = new BufferedWriter(new FileWriter("logs/log-" + timeFileSafe() + ".txt"));
		}
		catch (FileNotFoundException e)
		{
			File dir = new File("logs/");
			dir.mkdir();
			initBW();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void run()
	{
		try
		{
			bw.write("Begin Logging...\n");
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		while (true)
		{
			while (buffer.size() == 0)
			{
				try
				{
					Thread.sleep(200);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			String temp = buffer.remove(0);
			try
			{
				bw.write(temp);
				bw.newLine();
				bw.flush();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void write(String msg)
	{
		buffer.add("[" + time() + "]" + " from " + func.GetCaller.getCallerCallerClassName() + "("
				+ func.GetCaller.getLineNumber() + "): " + msg);
	}

	public static void writeException(Thread t, Throwable e)
	{
		StackTraceElement temp[] = e.getStackTrace();
		String ret = e.toString() + "\n";
		for (int counter = 0; counter < temp.length; counter++)
		{
			ret += "        " + temp[counter].toString() + "\n";
		}

		write(ret);
	}

	public static String time()
	{
		java.util.Date date = new java.util.Date();
		String ret = "";
		ret = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(date.getTime()));
		return ret;
	}

	public static String timeFileSafe()
	{
		String ret = time();
		ret = ret.replace(' ', '_');
		ret = ret.replace(':', '.');
		return ret;
	}
}
