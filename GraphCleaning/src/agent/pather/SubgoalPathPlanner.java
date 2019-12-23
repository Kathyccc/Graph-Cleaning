package agent.pather;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import agent.AgentActions;
import agent.IPathPlanner;
import agent.TargetPathAgentStatus;
import agent.common.LitterExistingExpectation;
import java.util.SortedSet;
import java.util.TreeSet;
import core.IGraph;
import core.LitterSpawnPattern;
import core.Pair;
import core.RobotData;
import core.utilities.DijkstraAlgorithm;
import core.utilities.PotentialCollection;

public class SubgoalPathPlanner implements IPathPlanner
{
	int robotID;
	int target;
	IGraph map;
	
	int myopia;
	double attraction;
	double rover; //deviation factor
	double threshold;  //expected value threshold
	
	LitterExistingExpectation expectation;
	PotentialCollection basePotential;
	PotentialCollection targetPotential;
	IPathPlanner pathPlanner;
	List<Integer> subgoals;
	int subgoal;
	int goalIndex;
	int farthestDistance;
	boolean CanArrive;
	
	
	
	public SubgoalPathPlanner(int _robotID, IGraph _map, LitterSpawnPattern pattern, int baseNode, boolean isAccumulated, int seed) 
	{
		robotID = _robotID;
		map = _map;
		basePotential = new DijkstraAlgorithm(map).Execute(baseNode);
		expectation = new LitterExistingExpectation(pattern, isAccumulated);
		pathPlanner = new ShortestGreedyPathPlanner(_robotID, _map, pattern, baseNode, isAccumulated, seed);
		
		subgoal = baseNode;
		
		myopia = 10;
		attraction = 1.0;
		rover = 1.2;
		threshold = 0.0;
	}
	
	
	public void setSubgoalsNull() 
	{
		subgoals = null;
	}
	
	
	public void setExpectation(LitterExistingExpectation _expectation) 
	{
		expectation = _expectation;
		pathPlanner.setExpectation(_expectation);
	}
	
	
	public int NextNode()
	{ 
		return pathPlanner.NextNode();
	}
	
	
	public void setCanArrive(boolean canArrive)
	{
		CanArrive = canArrive;
	}
	
	
	public boolean CanArrive()
	{
		return CanArrive;
	}
	
	
	public void Update(TargetPathAgentStatus status) 
	{
		RobotData mydata = status.ObservedData.RobotData._robots.get(robotID);
		int position = mydata.Position;
		
		expectation.Update(status.ObservedData.RobotData, status.ObservedData.Time);

		if(target != status.TargetNode || subgoals == null) 
		{
			int consumption = mydata.Spec.BatteryConsumption;
			int remainingBattery = mydata.BatteryLevel;
			
			// change to new target
			target = status.TargetNode;
			
			// create path map
			targetPotential = new DijkstraAlgorithm(map).Execute(target, position);
			
			// evaluate if it's possible to reach target
			if((basePotential.getPotential(target) + targetPotential.getPotential(position)) * consumption > mydata.BatteryLevel) 
			{
				CanArrive = false;
				return;
			}
			
			CanArrive = true;
			
			// create subgoal
			subgoals = new ArrayList<>();
			
			Pair<Integer,Integer> goal = new Pair<Integer,Integer>(position, remainingBattery - basePotential.getPotential(target)*consumption);
			
			do 
			{
				goal = getSubgoal(goal.getKey(), goal.getValue(), consumption);
				subgoals.add(goal.getKey());
			}while(goal.getKey() != target);
			goalIndex = 0;
		}
				
		if(subgoal == position && status.Action == AgentActions.Move) 
		{
			if(goalIndex >= subgoals.size())
				goalIndex = subgoals.size()-1;
			subgoal = subgoals.get(goalIndex);
			goalIndex++;
		}
		
		pathPlanner.Update(new TargetPathAgentStatus(
				status.Action, subgoal, status.ObservedData, status.getVisitCounter(), status.getVisitHistory()));
	}
	
	
	public Pair<Integer, Integer> getSubgoal(int _start, int _battery, int _consumption)
	{
		int stDistance = targetPotential.getPotential(_start);
		
		if(stDistance <= myopia)
			return new Pair<Integer,Integer>(target, _battery - stDistance * _consumption);
		
		List<Integer> nodes = new ArrayList<>();
		SortedSet<Integer> investigated = new TreeSet<>();
		Map<Pair<Integer, Integer>, Double> candidate = new HashMap<>();
		
		nodes.add(_start);
		investigated.add(_start);
		
		for(int i=0; i <= myopia; i++) 
		{
			List<Integer> nexts = new ArrayList<>();
			
			for(Integer node : nodes){
				
				List<Integer> children = map.getChildrenNodes(node);
				
				for(Integer child : children) 
				{
					if(investigated.contains(Integer.valueOf(child))) 
						continue;
					investigated.add(child);
					
					int childDistance = targetPotential.getPotential(child);
					int distance = i + childDistance;
					int level = _battery - distance * _consumption;
					if(level >= 0 && distance <= rover * childDistance) 
					{
						nexts.add(child);
						
						if(expectation.getExpectation(child) >= threshold && (childDistance < attraction * stDistance))
							candidate.put(new Pair<Integer, Integer>(child, _battery - i * _consumption), expectation.getExpectation(child));
					}
				}
			} 
			nodes = nexts;
		}
		
		if(candidate.size() == 0)
			return new Pair<Integer, Integer>(target, _battery - stDistance);
		
		Map<Pair<Integer, Integer>, Double> sorted = new LinkedHashMap<>();
		
		candidate.entrySet()
	    	.stream()
	    	.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) 
	    	.forEachOrdered(x -> sorted.put(x.getKey(), x.getValue()));
		
		Pair<Integer, Integer> pair = sorted.entrySet().stream().findFirst().get().getKey();
		
//		Map.Entry<Pair<Integer,Integer>, Double> entry = sorted.entrySet().iterator().next();
		
		return pair;
	}
}
	
	
//	public Map<String, Integer> getSubgoal(Map<String, Integer> parms, int consumption)
//	{
//		int start = parms.get("position");
//		int battery = parms.get("battery");
//		int rbattery = parms.get("rbattery");
//		int stDistance = targetPotential._potentials.get(start);
//		
//		if(stDistance <= myopia) 
//		{
//			Map<String, Integer> newMap = new HashMap<>();
//			newMap.put("position", target);
//			newMap.put("battery", battery - stDistance * consumption);
//			newMap.put("rbattery", rbattery - stDistance * consumption);
//			
//			return newMap;
//		}
//		
//		List<Integer> nodes = new ArrayList<>();
//		SortedSet<Integer> investigated = new TreeSet<>();
//		Map< Map<String, Integer>, Double> candidate = new HashMap<>();
//		
//		
//		nodes.add(start);
//		investigated.add(start);
//		
//		for(int i=1; i<= myopia; i++) 
//		{
//			List<Integer> nexts = new ArrayList<>();
//			
//			for(int node : nodes) 
//			{
//				List<Integer> children = map.getChildrenNodes(node);
//				for(int child : children) 
//				{
//					// Determine if it has been investigated
//					if(investigated.contains(child)) continue;
//					investigated.add(child);
//					
//					int childDistance = targetPotential._potentials.get(child);
//					int distance = i + childDistance;
//					int level = battery - distance * consumption;
//					int rlevel = rbattery - distance * consumption;
//					
//					if(level >= 0 && distance <= rover*childDistance && rlevel >= 0) 
//					{
//						nexts.add(child);
//						
//						if(expectation.getExpectation(child) >= threshold && childDistance < attraction * stDistance) 
//						{
//							Map<String, Integer> map = new HashMap<>();
//							map.put("position", child);
//							map.put("battery", battery - i*consumption);
//							map.put("rbattery", rbattery - i*consumption);
//							candidate.put(map, expectation.getExpectation(child));
//						}
//					}
//				}
//			}
//			nodes = nexts;
//		}
//		
//		if(candidate.size() == 0) 
//		{
//			Map<String, Integer> map2 = new HashMap<>();
//			map2.put("position", target);
//			map2.put("battery", battery - stDistance);
//			map2.put("rbattery", rbattery - stDistance);
//			
//			return map2;	
//		}
//		
//		Map< Map<String, Integer>, Double> sorted = candidate.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
//    	        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
//		
//		Map.Entry<Map<String, Integer>, Double> entry = sorted.entrySet().iterator().next();
//		
//		Map<String, Integer> pair = entry.getKey();
//		
//		return pair;
//		
//	}

//	Map<String, Integer> subgoalParms = new HashMap<String, Integer>();
//	
//	subgoalParms.put("position", position);
//	subgoalParms.put("battery", battery - basePotential._potentials.get(target)*consumption);
//	subgoalParms.put("rbattery", remainingBattery - basePotential._potentials.get(target)*consumption);
//	
//	do 
//	{
//		subgoalParms = getSubgoal(subgoalParms, consumption);
//		subgoals.add(subgoalParms.get("position"));
//	} while(subgoalParms.get("position") != target);
