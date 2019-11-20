package agent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import agent.common.LitterExistingExpectation;
import core.Coordinate;
import core.GridGraph;
import core.LitterSpawnPattern;
import core.LitterSpawnProbability;
import core.ObservedData;
import core.RobotData;
import core.agents.IAgent;

public class TargetPathAgentPDALearning2 implements IAgent
{
	int RobotID;
	AgentActions Action;
	IPathPlanner _pather;
	Random forOrderbyRnd;
	double cycleLearnEpsilon = 0.05;
	LitterExistingExpectation _expectation;
	LitterSpawnPattern _mySpawnPattern;
	GridGraph _graph;
	List<Integer> _nodes;
	int collectedCycleLitter = 0;
	int[] _visitCount;
	List<Integer> _visitHistory;
	int[][] _visitCountMemory;
	int _target;
	
	
	public TargetPathAgentPDALearning2(int robotid, GridGraph graph, LitterSpawnPattern SpawnPattern, List<Integer> excludeNode)
	{
		RobotID = robotid;
		forOrderbyRnd = new Random(robotid + 7);
		Action = AgentActions.Wait;
		_graph = graph; 
		_nodes = new ArrayList<>(_graph.getNodes());
				
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

	
	public void Update(ObservedData data) {
		RobotData mydata = data.getRobotData().getRobotData(RobotID);
		int position = mydata.Position;
		int interval = _expectation.getInterval(position, data.Time);
		int litter = mydata.AccumulatedLitter;
		
		collectedCycleLitter += litter;
		
		Coordinate coordinate = _graph.getCoordinate(position);
		
		_visitCount[position]++;
		_visitHistory.add(position);
		
		_expectation.Update(data.RobotData, data.Time);
		
       if(data.Time == 1000000 || data.Time == 2000000 || data.Time == 2999999)
       {
        	for(int i=0; i < _nodes.size(); i++) 
        	{
        		if(data.Time == 1000000) 
        			_visitCountMemory[0][i] = _visitCount[i];
        		if(data.Time == 2000000) 
        			_visitCountMemory[1][i] = _visitCount[i] - _visitCountMemory[0][i];
        		if(data.Time == 2999999)
        			_visitCountMemory[2][i] = _visitCount[i] - _visitCountMemory[0][i] - _visitCountMemory[1][i];
        	}
        }
       
       
       if(Action == AgentActions.Move) 
       {
//    	   if(position == _target)
       }
	}

	@Override
	public LitterSpawnPattern getMySpawnPattern() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void SearchNumberDecrease(int decrease) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int[][] getVisitCountMemory() { return _visitCountMemory;}
	

}
