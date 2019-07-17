package core;

import java.util.ArrayList;
import java.util.List;

public class RobotBase {
	
	int _id = 0;
	
	List<Robot> _robots;
	BatteryCharger _charger;
	int ID;
	int Position;
	
	
	public RobotBase(int chargeValue) {
		ID = _id++;
		_robots = new ArrayList<>();
		_charger = new BatteryCharger(chargeValue);
	}
	
	public void setID(int id) {
		this.ID = id;
	}
	
	public int getID() {
		return this.ID;
	}
	
	public void setPosition(int position) {
		this.Position = position;
	}
	
	public int getPosition() {
		return this.Position;
	}
	
	public void Place(int position) {
		Position = position;
	}
	
	public void Connect(Robot robot) {
		_robots.add(robot);
		robot.ConnectBase();
		_charger.Connect(robot.Battery);
	}
	
	public void Disconnect(Robot robot) {
		_robots.remove(robot);
		robot.DisconnectBase();
	}
	
	public void Charge() {
		_charger.Charge();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
