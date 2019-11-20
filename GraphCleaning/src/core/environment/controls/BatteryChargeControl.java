package core.environment.controls;

import java.util.HashMap;
import java.util.Map;

import core.environment.Field;
import core.environment.Robot;
import core.environment.RobotBase;
import core.environment.RobotBaseCollection;
import core.environment.RobotCollection;

public class BatteryChargeControl 
{
	Field _field;
    RobotCollection _robots;
    RobotBaseCollection bases;

    
    public BatteryChargeControl(Field field) {
    	_field = field;
    	_robots = _field.Robots;
    	bases = _field.RobotBases;
    }
    
    
    public void Connect(int id) {
    	Robot robot = _robots.getRobot(id);
    	RobotBase rb = bases.getRobotBase(robot.Position);
    	rb.Connect(robot);
    }
    
    
    public void Disconnect(int id) {
    	Robot robot = _robots.getRobot(id);
    	RobotBase rb = bases.getRobotBase(robot.Position);
    	rb.Disconnect(robot);
    }
    
    public void Update() 
    {
    	RobotBaseCollection rbc = new RobotBaseCollection();
    	Map<Integer, RobotBase> bases = new HashMap<>();
    	bases = rbc.getBasesMap();
    	
    	for(RobotBase robotbase: bases.values()) 
    	{
    		robotbase.Charge();
    	}		
    }
}
    
    
    
    
    
    
    
    
    

