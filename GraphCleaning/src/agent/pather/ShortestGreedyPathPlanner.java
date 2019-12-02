package agent.pather;

import static java.util.Collections.reverseOrder;
import static java.util.Comparator.comparing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import agent.AgentActions;
import agent.IPathPlanner;
import agent.TargetPathAgentStatus;
import agent.common.LitterExistingExpectation;
import core.IGraph;
import core.LitterSpawnPattern;
import core.Pair;
import core.RobotData;
import core.utilities.DijkstraAlgorithm;
import core.utilities.PotentialCollection;

public class ShortestGreedyPathPlanner implements IPathPlanner
{
	int _robotID;
    IGraph _map;
    int _base;
    Random _rand;
    LitterExistingExpectation _expectation;
    PotentialCollection _potentialMap;
    PotentialCollection _pathMap;
    int _target = -1;
    int NextNode;
    boolean CanArrive;
    
    
    
    public void setSubgoalsNull() {}
    
    public ShortestGreedyPathPlanner(int robotID, IGraph map, LitterSpawnPattern pattern, int baseNode, boolean isAccumulated, int seed)
    {
        _robotID = robotID;
        _map = map;
        _base = baseNode;
        _rand = new Random(seed);
        _potentialMap = new DijkstraAlgorithm(_map).Execute(_base);
        _expectation = new LitterExistingExpectation(pattern, isAccumulated);
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
    	
    	// update the observed time
    	_expectation.Update(status.ObservedData.RobotData, status.ObservedData.Time);
    	
    	// Exit if agent is not moving
    	if(status.Action != AgentActions.Move) return;
    	
    	if(_target != status.TargetNode) 
    	{
    		_target = status.TargetNode;
    		
    		_pathMap = new DijkstraAlgorithm(_map).Execute(_target, position);
    		
    		int consumption = mydata.Spec.BatteryConsumption;
    		int remainingBattery = mydata.BatteryLevel;
    		
    		// determine if agent can arrive the target
    		if((_potentialMap.getPotential(_target) + _pathMap.getPotential(position)) * consumption > remainingBattery) 
    		{
    			CanArrive = false;
    			return;
    		}
    		
    		CanArrive = true;
    	}
    	
		List<Pair<Integer, Double>> candidates = new ArrayList<Pair<Integer, Double>>();
    	
    	
    	for(int node : _map.getChildrenNodes(position)) 
    	{
    		if(_pathMap._potentials.get(node) < _pathMap._potentials.get(position)) 
    		{
    			candidates.add(new Pair<Integer, Double>(node, _expectation.getExpectation(node)));
    		}
    	}
    	
    	
		final Comparator<Pair<Integer, Double>> sortByValue = reverseOrder(comparing(Pair::getValue));

		Collections.sort(candidates, sortByValue);
    	
    	if(candidates.size() == 0) return;
    	
    	NextNode = candidates.get(0).getKey();

    }
}




////calculate battery consumption
//int consumption = mydata.Spec.BatteryConsumption;
//int remainingBattery = cycle * consumption - (mydata.Spec.BatteryCapacity - mydata.BatteryLevel);

//Map<Integer, Double> sortedexpec = expec.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
//.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
//
//for(Map.Entry<Integer, Double> nodeAndexpec : sortedexpec.entrySet()) 
//{
//candidates.add(nodeAndexpec.getKey());
//}
