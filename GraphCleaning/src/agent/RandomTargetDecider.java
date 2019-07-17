package agent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import core.IGraph;
import core.RobotData;

public class RandomTargetDecider implements ITargetDecider
{
	Random _rand;
	List<Integer> _nodes;
	int _robotID;
	int NextTarget;
	
	
	public RandomTargetDecider(int robotID, IGraph map, int seed, List<Integer> excludeNodes) 
	{
		_rand = new Random(seed);
		_nodes = new ArrayList<>(map.getNodes());
		_robotID = robotID;
		
		for(int node : excludeNodes) 
		{
			_nodes.remove(node);
		}
		
		NextTarget = _nodes.get(_rand.nextInt(_nodes.size()));
	}
	
	public void setNextTarget(int target) 
	{
		NextTarget = target;
	}
	
	public int NextTarget() {
		return NextTarget;
	}

	public boolean IsChargeRequired() {
		return false;
	}

	public void Update(TargetPathAgentStatus status) {
		if(status.Action != AgentActions.Move) return;
		
		RobotData mydata = status.ObservedData.RobotData._robots.get(_robotID);
		
		if(mydata.Position == status.TargetNode) 
		{
			int target;
			do 
			{
				target = _nodes.get(_rand.nextInt(_nodes.size()));
			} while(target == mydata.Position);
			NextTarget = target;
		}
	}

	public void setExpectation(LitterExistingExpectation expectation) {
	}

	public void ResetState() {
	}

}