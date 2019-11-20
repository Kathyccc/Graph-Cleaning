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

	public Robot(RobotSpec spec, int id) {
		setID(id);
		setBattery(spec);
		setBatteryConsum(spec.BatteryConsumption);
		setCleaner();
		setRobotState();
		setRobotSpec(spec);
	}
	

	//properties//////////////////////////////////////////////

	public void setID(int id) {
		this.ID = id;
	}
	
	public int getID() {
		return this.ID;
	}
	
	public void setPosition(int value) {
		Position = value;
	}
	
	public int getPosition() {
		return Position;
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
	
	public Cleaner getCleaner() {
		return Cleaner;
	}
	
	public void setBatteryConsum(int consumption) {
		BatteryConsum = consumption;
	}
	
	public int getBatteryConsum() {
		return BatteryConsum;
	}
	
	public void setBatteryLevel() {
		BatteryLevel = getBattery().getLevel();
	}
	
	public int getBatteryLevel() {
		return BatteryLevel;
	}
	
	public void setAccumulatedLitter() {
		AccumulatedLitter = getCleaner().getAccumulatedLitter();
	}
	
	public int getAccumulatedLitter() {
		return AccumulatedLitter;
	}
	
	public void setLitter() {
		AccumulatedLitter = getCleaner().getLitter();
	}
	
	public int getLitter() {
		return this.Litter;
	}
	
	public void setRobotSpec(RobotSpec spec) {
		Spec = spec;
	}
	
	public RobotSpec getSpec() {
		return Spec;
	}
	
	public void setRobotState() {
		State = RobotStates.Inactive;
	}
	
	public RobotStates getRobotState() {
		return State;
	}
	
	
	
	//Methods for robot moving//////////////////////////////////////////////

	public void Move(int position) 
	{
		if(State != RobotStates.Active) throw new IllegalStateException("Robot is not in Active state.");
		
		if(_map.getChildrenNodes(Position).contains(position)) 
		{
			if(BatteryLevel == 0) return;
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
