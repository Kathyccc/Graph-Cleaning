package core.utilities;

import java.util.HashMap;
import java.util.Map;
import java.io.File;


public class LogManager 
{
	static String logDirectory;
	
	static String absolutePath = "/Users/kathy/Graph-Cleaning/";
	
	static int defaultCapacity = 1;
	
	static Map<String, LogWriter> writers = new HashMap<String, LogWriter>();
	
	
	
	public static void setLogDirectory(String path) 
	{
		File directory = new File(absolutePath + path);
		if(!directory.exists()) 
			directory.mkdir();
		//System.out.println(path);
		
		
		logDirectory = absolutePath + path + "/";
		writers.clear();
	}
	
	//Create LogWriter
	public static LogWriter CreateWriter(String key) 
	{
		if(writers.containsKey(key)) 
		{
			if(writers.get(key).isDisposed() == false) 
				return writers.get(key);
			writers.remove(key);
		}
		LogWriter writer = new LogWriter(logDirectory + key + ".csv", defaultCapacity);
		writers.put(key, writer);
		return writer;
	}
	
	public static void closeWriters() 
	{
		for(LogWriter writer : writers.values())
			writer.close();
	}
		
}
