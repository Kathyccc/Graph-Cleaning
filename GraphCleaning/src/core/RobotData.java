package core;

public class RobotData {
	int ID;
	int BatteryLevel;
	int Position;
	int AccumulatedLitter;
	int Litter;
	RobotSpec Spec;
	
	public RobotData(int id, int batterylevel, int position, int accumulation, int litter, RobotSpec spec) {
		ID = id;
		BatteryLevel = batterylevel;
		Position = position;
		AccumulatedLitter = accumulation;
		Spec = spec;
	}
}
