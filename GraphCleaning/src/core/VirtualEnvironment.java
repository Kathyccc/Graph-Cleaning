package core;

import java.util.Random;

public class VirtualEnvironment implements IEnvironment
{
	Field _field;
	
	int _state = 0;
	
	FieldSettingControl _fieldSettingControl;
	BatteryChargeControl _batteryChargeControl;
	CleaningControl _cleaningControl;
	LitterSpawnControl _litterSpawnControl;
	RobotMoveControl _robotMoveControl;
	ObservationControl _observer;
	Random _rand;
	LogWriter
	
}
