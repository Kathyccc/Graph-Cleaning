package core.environment;

import java.util.HashMap;
import java.util.Map;

public class RobotCollection 
{
	public Map<Integer, Robot> _robots;
	
	public RobotCollection() 
	{
		_robots = new HashMap<>();
	}
	
	public void Add(Robot robot) 
	{
		_robots.put(robot.ID, robot);
	}
	
	public void Remove(Robot robot) 
	{
		_robots.remove(robot.ID);
	}
	
	public Robot getRobot(int id) 
	{
		return _robots.get(id);
	}
}


