package core;

public class ObservedData 
{
	public int Time;
	public RobotDataCollection RobotData;
	
	
	public ObservedData(int time, RobotDataCollection data) {
		Time = time;
		RobotData = data;
	}
	
	public void setTime(int time) {
		Time = time;
	}
	
	public int getTime() {
		return Time;
	}
	
	public void setRobotData(RobotDataCollection data) {
		RobotData = data;
	}
	
	public RobotDataCollection getRobotData() {
		return RobotData;
	}

}
