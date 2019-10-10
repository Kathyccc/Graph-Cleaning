package core.utilities;

import java.io.FileNotFoundException;
import java.io.PrintWriter;


public class LogWriter 
{
	Object locker = new Object();
	boolean disposed = false;
	
	PrintWriter writer;
	
	LogWriter(String path, int c)
	{
		try {
			writer = new PrintWriter(path);
		} 
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	public void WriteLine(String log) 
	{
		writer.println(log);
	}
	
	
	public void close() 
	{
		writer.close();
	}
	
	public boolean isDisposed() 
	{
		synchronized(locker) {
			return disposed;
		}
	}
}

