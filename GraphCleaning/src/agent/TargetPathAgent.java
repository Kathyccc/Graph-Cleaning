package agent;

import java.util.ArrayList;
import java.util.List;

import agent.common.LitterExistingExpectation;
import core.CommunicationDetails;
import core.Coordinate;
import core.LitterSpawnPattern;
import core.ObservedData;
import core.RobotData;
import core.agents.IAgent;

public class TargetPathAgent implements IAgent
{
	public int RobotID = 0;
	public AgentActions Action = null;
	
	ITargetDecider _targetter;
	IPathPlanner _pather;
	int _baseNode;
	boolean _isChargeRequired = false;
	LitterExistingExpectation _expectation;
	int NextNode;
	int _target;

	
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

	public int NextNode() {
		return _pather.NextNode();
	}

	
	public TargetPathAgent(int robotID, LitterSpawnPattern pattern) 
	{
		RobotID = robotID;
		
		Action = AgentActions.Wait;
		
		_expectation = new LitterExistingExpectation(pattern, true);
	}
	
	public void SetExpectation() 
	{
		_targetter.setExpectation(_expectation);
		_pather.setExpectation(_expectation);
	}

	
	public void setBaseNode(int baseNode) 
	{
		_baseNode = baseNode;
	}
	
	
	public void setTargetDecider(ITargetDecider targetter) 
	{
		_targetter = targetter;
	}
	
	
	public void SetPathPlanner(IPathPlanner pather) 
	{
		_pather = pather;
	}
	
	
	public void Update(ObservedData data) 
	{
		RobotData mydata = data.RobotData.getRobotData(RobotID);
		int position = mydata.Position;
		
		_expectation.Update(data.RobotData, data.Time); //other agents' positions available
		
		if(Action == AgentActions.Move) 
		{
			if(position == _target) 
			{
				if(_isChargeRequired || _targetter.IsChargeRequired()) 
				{
					Action = AgentActions.Charge;
				}
			}
		}
			
		else if(Action == AgentActions.Charge) 
		{
			if(mydata.BatteryLevel == mydata.Spec.BatteryCapacity) 
			{
				Action = AgentActions.Move;
				_isChargeRequired = false;
			}
		}
		
		else if(Action == AgentActions.Wait) 
		{
			Action = AgentActions.Move;
			_target = position;
		}
		
//		TargetPathAgentStatus status = new TargetPathAgentStatus(Action, _target, data);
		
		//update status
//		_targetter.Update(status);
		
		
		if(_target != _targetter.NextTarget() && !_isChargeRequired) 
		{
			_target = _targetter.NextTarget();
			
//			status = new TargetPathAgentStatus(Action, _target, data);
//			_pather.Update(status);
			
			if(_pather.CanArrive()) {
				return;
			}
			
			//if the agent cannot reach the target, it is forced to charge
			_target = _baseNode;
			_isChargeRequired = true;
			
			
			//if the agent is at the base node rn
			if(position == _baseNode) {
				Action = AgentActions.Charge;
				return;
			}
//			status = new TargetPathAgentStatus(Action, _target, data);
		}
		
		if(position == _baseNode && _targetter.IsChargeRequired()) 
		{
			Action = AgentActions.Charge;
			return;
		}
		
//		_pather.Update(status);
	}
	
	
	public LitterSpawnPattern getMySpawnPattern() 
	{
		LitterSpawnPattern _mySpawnPattern;
		_mySpawnPattern = new LitterSpawnPattern();
		return _mySpawnPattern;
	}
	
	
	public CommunicationDetails getCommunicateDetails() 
	{
		List<Integer> a = new ArrayList<>();
		Coordinate b = new Coordinate(0,0);
		LitterSpawnPattern c = new LitterSpawnPattern();
		CommunicationDetails _tmp = new CommunicationDetails(b,0.0,c,a);
		
		return _tmp;
	}
	
	
	public int[][] getVisitCountMemory(){
		int[][] dammy = new int[1][1];
		return dammy;
	}


	public void SearchNumberDecrease(int decrease) {
	}


	public void Update(ObservedData data, int startPhase) {
	}
	
}
	
	
	

