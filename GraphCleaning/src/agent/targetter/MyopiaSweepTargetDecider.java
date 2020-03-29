package agent.targetter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import agent.AgentActions;
import agent.ITargetDecider;
import agent.TargetPathAgentStatus;
import agent.common.LitterExistingExpectation;
import core.IGraph;
import core.LitterSpawnPattern;
import core.RobotData;
import core.utilities.DijkstraAlgorithm;
import core.utilities.PotentialCollection;

public class MyopiaSweepTargetDecider implements ITargetDecider
{
	int _robotID;
	int _myopia;
	IGraph _map;
//	ForMyopiaGreedy _decider;
	GreedyTargetDecider _decider;
	List<Integer> _possibleNodes;
	double _threshold;
	int _state;
	double _alpha = 0.05;
	Random _rand;
	double _epsilon = 0.05;
	int _area;
	List<Integer> _excludeNodes;
	private int NextTarget;
	
	public MyopiaSweepTargetDecider(int ID, IGraph map, LitterSpawnPattern pattern, boolean isAccumulated, int seed)
	{
		_robotID = ID;
		_map = map;
//		_decider = new ForMyopiaGreedy(ID, map, pattern, isAccumulated, seed);
		_decider = new GreedyTargetDecider(ID, map, pattern, isAccumulated, seed);
		_possibleNodes = new ArrayList<>(map.getNodes());
	
		_myopia = 15;
		_rand = new Random(seed);
	}

	
	public void ResetState() 
	{
		_state = 0;
	}
	
	
	public void setExpectation(LitterExistingExpectation expectation) 
	{
		_decider.setExpectation(expectation);
	}

	
	public int NextTarget() 
	{
		NextTarget = _decider.NextTarget();
		return NextTarget;
	}

	
	public boolean IsChargeRequired() 
	{
		return false;
	}

	
//	public List<Integer> getNearbyNodes(int start)
//	{
//		List<Integer> nodes = new ArrayList<>();
//		SortedSet<Integer> investigated = new TreeSet<>();
//		nodes.add(start);
//		investigated.add(start);
//		
//		for(int i = 1; i < _myopia; i++) 
//		{
//			List<Integer> nexts = new ArrayList<>();
//			
//			for(int node : nodes) 
//			{
//				List<Integer> children = _map.getChildrenNodes(node);
//				for(int child : children) 
//				{
//					if(investigated.contains(child)) continue;
//					if(!_excludeNodes.contains(child)) investigated.add(child);
//					nexts.add(child);
//				}
//			}
//			nodes = nexts;
//		}
//		List<Integer> list = new ArrayList<>(investigated);
//		
//		return list;
//	}
	
	
	public List<Integer> getRealNearbyNodes(int start)
	{
		PotentialCollection myopiaPotential = new DijkstraAlgorithm(_map).Execute(start);
		List<Integer> realInvestigated = new ArrayList<>();
		
		for(Map.Entry<Integer, Integer> node : myopiaPotential._potentials.entrySet()) 
		{
			if(node.getValue() < _myopia) 
				realInvestigated.add(node.getKey());
		}
		
		return realInvestigated;
	}
	
	
	public void Update(TargetPathAgentStatus status) 
	{
		RobotData mydata = status.ObservedData.RobotData._robots.get(_robotID);
		
		if(mydata.Position == status.getTargetNode() && status.Action == AgentActions.Move) 
		{
			if(_state == 0) 
			{
				List<Integer> nodes = getRealNearbyNodes(mydata.Position);
				_decider.setAccessibleNodes(nodes);
				_area = nodes.size();
				_state = 1;
			}
			
			else if(_state == 1) 
			{
//				double value = _decider.getExpectationSum(mydata.Position);
				double value = _decider.getExpectationSum();
				
				if(_threshold != 0) 
				{
                    _threshold = _alpha * value / _area + (1 - _alpha) * _threshold;
				}
				
				else 
				{
					_threshold = value / _area;
				}
				
				_state = 2;
			}
			
			else if(_state == 2) 
			{
				double exp = _decider.getExpectationSum()  / _area;
//				double exp = _decider.getExpectationSum(mydata.Position) / _area;
				if(exp < _threshold || _rand.nextDouble() < _epsilon) 
				{
					_decider.setAccessibleNodes(_possibleNodes);
					_state = 0;
				}
			}
		}
		_decider.Update(status);
	}
}
