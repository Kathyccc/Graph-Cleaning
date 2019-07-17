package agent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import core.DijkstraAlgorithm;
import core.IGraph;
import core.LitterSpawnPattern;
import core.PotentialCollection;
import core.RobotData;

public class ShortestGreedyPathPlanner implements IPathPlanner
{
	int _robotID;
    IGraph _map;
    int _base;
    Random _rand;
    LitterExistingExpectation _expectation;
    PotentialCollection _potentialMap;
    int _target = -1;
    PotentialCollection _pathMap;
    List<Integer> _excludeNodes = new ArrayList<>();
    int NextNode;
    boolean CanArrive;
    
    
    
    public void setSubgoalsNull() {
    
    }
    
    public ShortestGreedyPathPlanner(int robotID, IGraph map, LitterSpawnPattern pattern, int baseNode, boolean isAccumulated, int seed, List<Integer> excludeNodes)
    {
        _robotID = robotID;
        _map = map;
        _base = baseNode;
        _rand = new Random(seed);
        _excludeNodes = excludeNodes;
        _potentialMap = new DijkstraAlgorithm(_map).Execute(_base);
    }
    
    
    public void setExpectation(LitterExistingExpectation expectation) 
    {
    	_expectation = expectation;
    }
    
    
    public void setNextNode(int node) 
    {
    	NextNode = node;
    }
    
    public int NextNode() 
    {
    	return NextNode;
    }
    
    public void setCanArrive(boolean can) 
    {
    	CanArrive = can;
    }
    
    public boolean CanArrive() 
    {
    	return CanArrive;
    }
    
    
    public void Update(TargetPathAgentStatus status) 
    {
    	RobotData mydata = status.ObservedData.RobotData._robots.get(_robotID);
    	int position = mydata.Position;
    	int cycle = status.MyCycle;
    	
    	// Exit if agent is not moving
    	if(status.Action != AgentActions.Move) return;
    	
    	if(_target != status.TargetNode) 
    	{
    		_target = status.TargetNode;
    		
    		_pathMap = new DijkstraAlgorithm(_map).Execute(_target, position);
    		
    		// calculate battery consumption
    		int consumption = mydata.Spec.BatteryConsumption;
    		int remainingBattery = cycle * consumption - (mydata.Spec.BatteryCapacity - mydata.BatteryLevel);
    		
    		// determine if agent can arrive the target
    		if((_potentialMap._potentials.get(_target) + _pathMap._potentials.get(position)) * consumption > remainingBattery) 
    		{
    			CanArrive = false;
    			return;
    		}
    		
    		CanArrive = true;
    	}
    	
    	//int time = status.ObservedData.Time;
    	
    	List<Integer> candidates = new ArrayList<>();
		Map<Integer, Double> expec = new HashMap<>();

    	
    	for(int node : _map.getChildrenNodes(position)) 
    	{
    		if(_pathMap._potentials.get(node) < _pathMap._potentials.get(position)) 
    		{
        		expec.put(node, _expectation.getExpectation(node));
    		}
    	}
    	
    	Map<Integer, Double> sortedexpec = expec.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
    	        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    	
    	for(Map.Entry<Integer, Double> nodeAndexpec : sortedexpec.entrySet()) 
    	{
    		candidates.add(nodeAndexpec.getKey());
    	}
    	
    	if(candidates.size() == 0) return;
    	
    	NextNode = candidates.get(0);

    }
}
