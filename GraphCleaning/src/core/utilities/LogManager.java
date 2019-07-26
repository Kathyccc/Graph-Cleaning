package core.utilities;

import java.util.HashMap;
import java.util.Map;
import java.io.File;


public class LogManager 
{
	static String _logDirectory;
	
	public static String LogDirectory;
	
	static int defaultCapacity = 10500;
	
	static Map<String, LogWriter> writers = new HashMap<>();
	
	
	public static String getLogDirectory() 
	{
		return LogDirectory;
	}
	
	public void setLogDirectory() 
	{
		File directory = new File(_logDirectory);
		if(!directory.exists()) {
			directory.mkdir();
		}
		
		LogDirectory = _logDirectory + "\"";
		writers.clear();
	}
	
	public static LogWriter CreateWriter(String key) 
	{
		if(writers.containsKey(key)) 
		{
			if(writers.get(key).isDisposed == false) 
			{
				return writers.get(key);
			}
			writers.remove(key);
		}
		LogWriter writer = new LogWriter(LogDirectory + key + ".csv", defaultCapacity);
		writers.put(key, writer);
		return writer;
	}
		
}
