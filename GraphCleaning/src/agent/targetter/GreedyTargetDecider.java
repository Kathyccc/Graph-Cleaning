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
	boolean firstSearch = true;
	LitterExistingExpectation LitterExpectation;
	double _expectationSum;
	int NextTarget;

	public GreedyTargetDecider(int robotID, IGraph map, LitterSpawnPattern pattern, boolean isAccumulated, int seed) 
	{
		_robotID = robotID;
		_nodes = new ArrayList<>(map.getNodes());
		_isAccumulated = isAccumulated;
		_rate = 0.05;
		_rand = new Random(seed);
		_expectation = new LitterExistingExpectation(pattern, isAccumulated);
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
	
	
	public double getExpectationSum(int position) 
	{
		List<Pair<Integer, Double>> list = new ArrayList<Pair<Integer, Double>>();
		int i = 0;
		double sum = 0.0;
		
		for(int node : _nodes) 
		{
			double exp = _expectation.getExpectation(node);
			if(node != position) 
			{
				list.add(i, new Pair<Integer, Double>(node, exp));
				sum += exp;
				i++;
			}
		}
		_expectationSum = sum;
		
		return _expectationSum;
	}
	
	public void Update(TargetPathAgentStatus status) 
	{
		int time = status.ObservedData.Time;
		RobotDataCollection data = status.ObservedData.RobotData;
		
		_expectation.Update(data, time);
		
		if(status.Action != AgentActions.Move) return;
		
		RobotData mydata = status.ObservedData.RobotData._robots.get(_robotID);
		
		if(mydata.Position == status.getTargetNode()) 
		{
			List<Pair<Integer, Double>> list = new ArrayList<Pair<Integer, Double>>();
			int i = 0;
			double sum = 0.0;

			
			for(int node : _nodes) 
			{
				double exp = _expectation.getExpectation(node);

				if(node != mydata.Position) 
				{
					list.add(i, (new Pair<Integer, Double>(node, exp)));
					sum += exp;
					i++;
				}
			}
			
			_expectationSum = sum;
						
			final Comparator<Pair<Integer, Double>> sortByValue = reverseOrder(comparing(Pair::getValue));
			final Comparator<Pair<Integer, Double>> sortByKey = reverseOrder(comparing(Pair::getKey));
			
			Collections.sort(list, sortByValue.thenComparing(sortByKey));
			
			int count = 5;//= (int)(_nodes.Count * _rate);		
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
}

//if(countchecker) 
//{
//	if(firstSearch == true && list.get(count).getValue() == list.get((list.size())-2).getValue()) 
//	{
//		count = list.size()-1;
//		firstSearch = false;
//	}
//	
//	else 
//	{
//		if((count != list.size()-1) && (list.get(count).getValue() == list.get(count+1).getValue()))
//		{
//			double sameValue = list.get(count).getValue();
//			int same = count;
//			int small = list.size() - 2;
//			int check = (same + small)/2;
//			
//			do 
//			{
//				if(list.get(check).getValue() < sameValue) 
//				{
//					small = check;
//					check = (same+small)/2; 
//				}
//				
//				else 
//				{
//					same = check;
//					check = (same+small)/2;
//				}
//			}while(!(check==same || check == small));
//			
//			count = same + 1;
//			
//		}
//	}
//}
//public double getExpectationSum(int position) 
//{
//	List<Pair<Integer, Double>> list = new ArrayList<Pair<Integer, Double>>();
//	int i = 0;
//	double sum = 0.0;
//	
//	for(int node : _nodes) 
//	{
//		double exp = _expectation.getExpectation(node);
//		
//		list.add(i, position == node ? (new Pair<Integer, Double>(node, -(Double.MAX_VALUE))) : (new Pair<Integer, Double>(node, exp)));
//		sum += exp;
//		i++;
//	}
//	
//	_expectationSum = sum;
//	
//	return _expectationSum;
//}
