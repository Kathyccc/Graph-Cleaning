package core;

public class RobotData {
	int ID;
	public int BatteryLevel;
	public int Position;
	int AccumulatedLitter;
	public int Litter;
	public RobotSpec Spec;
	
	public RobotData(int id, int batterylevel, int position, int accumulation, int litter, RobotSpec spec) {
		ID = id;
		BatteryLevel = batterylevel;
		Position = position;
		AccumulatedLitter = accumulation;
		Spec = spec;
	}
}
