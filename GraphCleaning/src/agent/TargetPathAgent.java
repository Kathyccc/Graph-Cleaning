package agent;


enum AgentActions{
	Move, 
	Charge, 
	Wait
}

public class TargetPathAgent implements IAgent{
	public int RobotID = 0;
	public AgentActions Action = null;
	
	ITargetDecider _targetter = new ITargetDecider();
	IPathPlanner _pather = new IPathplanner();
	int _baseNode;
	boolean _ChargeRequired = false;
	LitterExistingExpectation _exception = new LitterExistingExpectation();
	
	public void Update(ObeservedData data, int startPhase) {
	}
	
	public void setRobotID(int ID) {
		this.RobotID = ID;
	} 
	
	public int getRobotID(){
		return this.RobotID;
	}
	
	public void setAction(AgentActions action){
		this.Action = action;
	}
	
	public AgentActions getAction() {
		return this.Action;
	}
	
	public int 
	
	public TargetPathAgent(int robotID, LitterSpawnPattern pattern) {
		RobotID = robotID;
		Action = AgentActions.Wait;
		_exception = new LitterExistingExpectation(pattern, true);
	}
	
	public void SetExpectation() {
		_targetter.SetExpectation(_expectation);
		_pather.SetExpectation(_expectation);
	}
	
	
}