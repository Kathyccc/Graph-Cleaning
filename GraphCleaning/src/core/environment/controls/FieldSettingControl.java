package core.environment.controls;

import core.RobotSpec;
import core.environment.Field;
import core.environment.Litter;
import core.environment.Robot;
import core.environment.RobotBase;

public class FieldSettingControl 
{
	Field _field;
	int _robotIDs;
	
	public FieldSettingControl(Field field) 
	{
		_field = field;
	}

	
	public int CreateRobot(RobotSpec spec, int position) 
	{
		
		Robot robot = new Robot(spec, _robotIDs++);

		robot.setFieldStructure(_field.SpatialStructure);
		robot.setPosition(position);
		
		_field.AddRobot(robot);
		
		robot.Activate();
		
		return robot.ID;
	}
	
	
	public int CreateRobotBase(int chargeValue, int position) 
	{
		RobotBase rb = new RobotBase(chargeValue);
		rb.Place(position);
		
		_field.AddRobotBase(rb);
		
		return rb.ID;
	}
	
	
	public void CreateLitter(String type, int position, boolean isAccumulated) 
	{
		_field.AddLitter(new Litter(type, position, isAccumulated));
	}
	
	
	public void Update() 
	{
		_field.UpdateTime();
	}
}
