package core;

import java.util.HashMap;
import java.util.Map;

public class RobotDataCollection {
	
	Map<Integer, RobotData> _robots;
	boolean ReadOnly;
	
	public RobotDataCollection() {
		ReadOnly = false;
		_robots = new HashMap<>();
	}
	
	
	public RobotDataCollection(RobotDataCollection collection, boolean readonly) {
		ReadOnly = readonly;
		_robots = collection._robots;
	}
	
	
	public void Add(RobotData robot) {
		if(ReadOnly==false) _robots.put(robot.ID, robot);
		else throw new IllegalStateException("The collection is Read-Only.");
	}
	
	
	public void Remove(RobotData robot) {
		if(ReadOnly==false) _robots.remove(robot.ID);
		else throw new IllegalStateException("The collection is Read-Only.");
	}
	
	
	public RobotData getRobotData(int id) {
		return _robots.get(id);
	}	
}
