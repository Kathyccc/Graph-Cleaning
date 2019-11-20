package core;

public class RobotData {
	public int ID;
	public int BatteryLevel; // remaining power of battery
	public int Position; // position of the robot
	public int AccumulatedLitter; //amount of trash collected by  robot
	public RobotSpec Spec;
//	public int Litter;
	
	public RobotData(int id, int batterylevel, int position, int accumulation, int litter, RobotSpec spec) {
		ID = id;
		BatteryLevel = batterylevel;
		Position = position;
		AccumulatedLitter = accumulation;
//		Litter = litter;
		Spec = spec;
	}
}
