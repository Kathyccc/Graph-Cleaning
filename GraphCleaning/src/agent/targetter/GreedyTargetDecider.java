package agent.targetter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import static java.util.Collections.reverseOrder;
import static java.util.Comparator.comparing;

import agent.AgentActions;
import agent.ITargetDecider;
import agent.TargetPathAgentStatus;
import agent.common.LitterExistingExpectation;
import core.IGraph;
import core.LitterSpawnPattern;
import core.ObservedData;
import core.Pair;
import core.RobotData;
import core.RobotDataCollection;

public class GreedyTargetDecider implements ITargetDecider
{
	Random _rand;
	List<Integer> _nodes; 
	int _robotID;
	boolean _isAccumulated;
	double _rate;
	LitterExistingExpectation _expectation;
	double _expectationSum;
	int NextTarget;

	public GreedyTargetDecider(int robotID, IGraph map, LitterSpawnPattern pattern, boolean isAccumulated, int seed) 
	{
		_robotID = robotID;
		_nodes = new ArrayList<>(map.getNodes());
		_isAccumulated = isAccumulated;
		_rate = 0.05;
		_rand = new Random(seed);
		NextTarget = _nodes.get(_rand.nextInt(_nodes.size()));
	}
	
	public void setNextTarget(int target)
	{
		NextTarget = target;
	}

	public int NextTarget() 
	{
		return NextTarget;
	}
	
	public boolean IsChargeRequired() {
		return false;
	}

	
	public void Update(TargetPathAgentStatus status) 
	{
        _expectation.Update(status.ObservedData.RobotData, status.ObservedData.Time);

		if(status.Action != AgentActions.Move) 
			return;

		RobotData mydata = status.ObservedData.RobotData._robots.get(_robotID);
						
		//update target
		if(mydata.Position == status.getTargetNode()) 
		{
			List<Pair<Integer, Double>> list = new ArrayList<Pair<Integer, Double>>();
			double sum = 0.0;

			for(int node : _nodes) 
			{
				double exp = _expectation.getExpectation(node);
				
				if(node != mydata.Position) 
				{
					list.add(new Pair<Integer, Double>(node, exp));
					sum += exp;
				}
			}
			
			_expectationSum = sum;
			
					
			final Comparator<Pair<Integer, Double>> sortByValue = reverseOrder(comparing(Pair::getValue));
			final Comparator<Pair<Integer, Double>> sortByKey = reverseOrder(comparing(Pair::getKey));
			
			Collections.sort(list, sortByValue.thenComparing(sortByKey));
			
			Boolean countChecker = list.size() > 5;
			
			int count = countChecker ? 5 : list.size()-1;		
			int index = count < 1 ? 0 : _rand.nextInt(count);
			
			NextTarget = list.get(index).getKey();
		}
	}
	
	public double DiscountExpectation(ObservedData data, int node) 
	{
		return 1.0;
	}	
	
	
	public void setExpectation(LitterExistingExpectation expectation) {
		_expectation = expectation;
	}
	
	public void setAccessibleNodes(List<Integer> nodes) 
	{
		_nodes = nodes;
	}
	
	public void ResetState() {		
	}

	public double getExpectationSum() {
		return _expectationSum;
	}
}


//public double getExpectationSum(int position) 
//{
//	List<Pair<Integer, Double>> list = new ArrayList<Pair<Integer, Double>>();
//	double sum = 0.0;
//	
//	for(int node : _nodes) 
//	{
//		double exp = _expectation.getExpectation(node);
//
//		if(position == node) 
//		{
//			list.add(new Pair<Integer, Double>(node, -Double.MAX_VALUE));
//		}
//		else 
//		{
//			list.add(new Pair<Integer, Double>(node, exp));
//		}
//		sum += exp;
//	}
//	_expectationSum = sum;
//	
//	return _expectationSum;
//}