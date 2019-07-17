package core;

import java.io.PrintWriter;


public class LogWriter 
{
	Object locker = new Object();
	boolean isDisposed = false;
	PrintWriter writer;
	String[] buffer;
	String[] saveBuffer;
	int counter;
	int capacity;
	String filePath;
	
	public boolean isDisposed() 
	{
		return isDisposed;
	}
	
	
	public LogWriter(String _filePath, int _capacity) 
	{
		buffer = new String[_capacity];
		counter = 0;
		this.capacity = _capacity;
		
		filePath = _filePath;
	}
	
	
	public void WriteLine(String log) 
	{
		if(buffer.length <= counter) {
			saveBuffer = buffer;
			buffer = new String[capacity];
			counter = 0;
		}
		
		buffer[counter] = log;
		counter++;
	}
	
}
