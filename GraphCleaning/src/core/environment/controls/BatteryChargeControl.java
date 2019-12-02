package core.environment.controls;



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
    	for(RobotBase robotbase: bases.getBasesMap().values()) 
    	{
    		robotbase.Charge();
    	}		
    }
}
    
    
    
    
    
    
    
    
    

