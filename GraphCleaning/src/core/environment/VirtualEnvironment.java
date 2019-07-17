package core.environment;

import java.util.Random;

import core.IEnvironment;
import core.environment.controls.BatteryChargeControl;
import core.environment.controls.CleaningControl;
import core.environment.controls.FieldSettingControl;
import core.environment.controls.LitterSpawnControl;
import core.environment.controls.ObservationControl;
import core.environment.controls.RobotMoveControl;

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
