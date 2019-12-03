package agent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import agent.common.LitterExistingExpectation;
import core.GridGraph;
import core.LitterSpawnPattern;
import core.LitterSpawnProbability;
import core.ObservedData;
import core.RobotData;
import core.agents.IAgent;

public class TargetPathAgentPDALearning implements IAgent
{
	int RobotID;
	AgentActions Action;
	IPathPlanner _pather;
	Random forOrderbyRnd;
	LitterExistingExpectation _expectation;
	LitterSpawnPattern _mySpawnPattern;
	GridGraph _graph;
	List<Integer> _nodes;
	int[] _visitCount;
	List<Integer> _visitHistory;
	int[][] _visitCountMemory;
	int _target;
	boolean _isChargeRequired = false;
	ITargetDecider _targetter;
	int _baseNode;
	
	
	public TargetPathAgentPDALearning(int robotid, GridGraph graph, LitterSpawnPattern SpawnPattern)
	{
		RobotID = robotid;
		forOrderbyRnd = new Random(robotid + 7);
		Action = AgentActions.Wait;
		_graph = graph; 
		_nodes = new ArrayList<>(_graph.getNodes());
				
		_visitCount =  new int[_graph.getNodes().size()];
		
		for(int i=0; i<_graph.getNodes().size();i++) 
		{
			_visitCount[i] = 1;
		} 
		
		_visitCountMemory = new int[3][_graph.getNodes().size()];
		_visitHistory = new ArrayList<>();
		
		
		InitializeMySpawnPattern();
		
		_expectation = new LitterExistingExpectation(_mySpawnPattern, true);
		
	}
	
	
	public void InitializeMySpawnPattern() 
	{
		_mySpawnPattern = new LitterSpawnPattern();
		for(Integer node : _graph.getNodes()) 
		{
			_mySpawnPattern.AddSpawnProbability(node, new LitterSpawnProbability(1,  0.00,  1));
		}
	}
	
	
	public void setRobotID(int id) { RobotID = id; }
	public int getRobotID() { return RobotID; }

	public void setAction(AgentActions action) { Action = action;}
	public AgentActions getAction() { return Action; }

	
	public int NextNode() { return _pather.NextNode(); } 

	
	public int[][] getVisitCountMemory()
	{
		return _visitCountMemory;
	}
	
	
	public void setExpectation() 
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
	
	
	public void setPathPlanner(IPathPlanner pather) 
	{
		_pather = pather;
	}
	
	
	public LitterSpawnPattern getMySpawnPattern() 
	{
		return _mySpawnPattern;
	}
	
	
	public void Update(ObservedData data) {
		RobotData mydata = data.getRobotData().getRobotData(RobotID);
		int position = mydata.Position;
		int interval = _expectation.getInterval(position, data.Time);
		int litter = mydata.AccumulatedLitter;
		
		_visitCount[position]++;
		_visitHistory.add(position);
		
		_expectation.Update(data.RobotData, data.Time);
      
       if(Action == AgentActions.Move) {
    	   if(position == _target){
    		   if(_isChargeRequired || _targetter.IsChargeRequired()){
    			   Action = AgentActions.Charge;
    			   _isChargeRequired = false;
    		   }
    	   }
       }
       
       else if(Action == AgentActions.Charge) {
    	   // when the battery is fully charged
    	   if(mydata.BatteryLevel == mydata.Spec.BatteryCapacity) {
    		   //finish charging and move
    		   Action = AgentActions.Move;
    		   _isChargeRequired = false;
    		   
    	   }
       }
       
       else if(Action == AgentActions.Wait) {
    	   Action = AgentActions.Move;
    	   _target = position;
       }
        
       // Update its own pattern map
       _mySpawnPattern.setLitterSpawnProb(position, litter, interval);
       
       TargetPathAgentStatus status = new TargetPathAgentStatus(Action, _target, data, _visitCount, _visitHistory);
       
       _targetter.Update(status);
       
//	   System.out.println("agent's next target:  " + RobotID + "    " + _targetter.NextTarget());

       
       if(_target != _targetter.NextTarget() && !_isChargeRequired) 
       {
    	   _target = _targetter.NextTarget();

    	   status = new TargetPathAgentStatus(Action, _target, data, _visitCount, _visitHistory);
    	   _pather.Update(status);
    	   
    	   if(_pather.CanArrive()) {
    		   return;
    	   }
    	   
    	   else {
    		   _target = _baseNode;
    		   _isChargeRequired = true;
    	   }
    	   
    	   if(position == _baseNode) {
    		   Action = AgentActions.Charge;
    		   return;
    	   }
    	   status = new TargetPathAgentStatus(Action, _target, data, _visitCount, _visitHistory);
       }
       
       if(position == _baseNode && _targetter.IsChargeRequired()) {
    	   Action = AgentActions.Charge;
    	   return;
       }
       
       _pather.Update(status);
	}
}
















