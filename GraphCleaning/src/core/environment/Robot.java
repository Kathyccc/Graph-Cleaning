package core.environment;

import core.IGraph;
import core.RobotSpec;


public class Robot 
{
	public int ID;
	IGraph _map;
	public int Position;;
	RobotSpec _spec;
	Battery Battery;
	public Cleaner Cleaner;
	int BatteryConsum;
	public int BatteryLevel;
	public int AccumulatedLitter;
	public int Litter;
	public RobotSpec Spec;
	RobotStates State;
	
	
	
	//constructors//////////////////////////////////////////////

	public Robot(RobotSpec spec, int id) 
	{
		setID(id);
		setBattery(spec);
		setBatteryConsum(spec.BatteryConsumption);
		setCleaner();
		setRobotState();
		setRobotSpec(spec);
		BatteryLevel = Battery.Level;
	}
	

	//properties//////////////////////////////////////////////

	public int getBatteryLevel() 
	{
		return Battery.Level;
	}
	
	public int getAccumulatedLitter() {
		return Cleaner.AccumulatedLitter;
	}
	
	public void setID(int id) {
		this.ID = id;
	}
	
	public void setPosition(int value) {
		Position = value;
	}
	
	public void setBattery(RobotSpec spec) {
		Battery = new Battery(spec.BatteryCapacity);
	}
	
	public Battery getBattery() {
		return Battery;
	}
	
	public void setCleaner() {
		Cleaner = new Cleaner();
	}
	
	public void setBatteryConsum(int consumption) {
		BatteryConsum = consumption;
	}
	
	public int getBatteryConsum() {
		return BatteryConsum;
	}
	
	public void setRobotSpec(RobotSpec spec) {
		Spec = spec;
	}
	
	public void setRobotState() {
		State = RobotStates.Inactive;
	}
	

	//Methods for robot moving//////////////////////////////////////////////

	public void Move(int position) 
	{
		if(State != RobotStates.Active) throw new IllegalStateException("Robot is not in Active state.");
		
		if(_map.getChildrenNodes(Position).contains(Integer.valueOf(position))) 
		{
			if(getBatteryLevel() == 0) return;
			Position = position;
			Battery.Discharge(BatteryConsum);
		}
	}
	
	
	//Methods for robot states//////////////////////////////////////////////
	
	public void Activate() {
		if(State != RobotStates.Inactive) throw new IllegalStateException("Robot has already been avtivated.");
		
		if(_map == null) throw new IllegalStateException("FieldData is null.");
		
		State = RobotStates.Active;
	}
	
	
	public void ConnectBase() {
		State = RobotStates.Docking;
	}
	
	
	public void DisconnectBase() {
		State = RobotStates.Active;
	}
	
	
	//Methods for initialization//////////////////////////////////////////////

	public void SetPosition(int position) {
		if(State != RobotStates.Inactive) throw new IllegalStateException("Robot state is not Inactive.");
		
		Position = position;
	}
	
	
	public void setFieldStructure(IGraph map) {
		if(State != RobotStates.Inactive) throw new IllegalStateException("Robot state is not Inactive.");
		
		_map = map;
	}
}
