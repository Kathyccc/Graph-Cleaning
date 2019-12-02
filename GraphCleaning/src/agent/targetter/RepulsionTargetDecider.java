package agent.targetter;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import agent.AgentActions;
import agent.ITargetDecider;
import agent.TargetPathAgentStatus;
import agent.common.LitterExistingExpectation;
import core.IPointMappedGraph;
import core.ObservedData;
import core.RobotData;

public class RepulsionTargetDecider implements ITargetDecider
{
	int _robotID;
	List<Integer> _nodes;
	IPointMappedGraph _map;
	double _rate;
	Random _rand;
	int NextTarget;
	
	
	public RepulsionTargetDecider(int ID, IPointMappedGraph map, int seed) 
	{
		_robotID = ID;
		_map = map;
		_nodes = new ArrayList<Integer>(_map.getNodes());
		_rate = 0.01;
		_rand = new Random(seed);
		
		NextTarget = _nodes.get(_rand.nextInt(_nodes.size()));
	}

	
	public void setNextTarget(int next) 
	{
		NextTarget = next;
	}

	public int NextTarget() 
	{
		return NextTarget;
	}


	public boolean IsChargeRequired()
	{
		return false;
	}


	public void setRandomSelectRate(double rate) 
	{
		_rate = rate;
	}
	
	
	public Point subtract(Point p1, Point p2) {
	    return new Point(p1.x - p2.x, p1.y - p2.y);
	}
	
	
	
	public void Update(TargetPathAgentStatus status) 
	{
		if(status.Action != AgentActions.Move) return;
		
		RobotData mydata = status.ObservedData.RobotData._robots.get(_robotID); 
		
		if(mydata.Position == status.getTargetNode()) 
		{
			ObservedData data = status.ObservedData;
			
			//node selection, distance calculation
			double count = _nodes.size() * _rate;
			count = count > 1 ? count : 1;
			int maxNode = 0;
			double maxsum = -1;
			
			for(int i = 0; i < count; i++) 
			{
				int node;
				
				//repeat do& while in order to get the node that is not at its position
				do 
				{
					node = _nodes.get(_rand.nextInt(_nodes.size()));
				} while(node == mydata.Position);
				
				
				double sum = 0.0;
				Point destination = _map.getPoint(node);
				
				for(RobotData robot : data.RobotData._robots.values()) 
				{
					Point vector = subtract(destination, _map.getPoint(robot.Position));
					sum += Math.hypot(vector.getX(), vector.getY());
				}
				
				if(maxsum < sum) 
				{
					maxsum = sum;
					maxNode = node;
				}
			}
			
			NextTarget = maxNode;
		}
	}
	

	public void setExpectation(LitterExistingExpectation expectation) {	}


	public void ResetState() {}

}
