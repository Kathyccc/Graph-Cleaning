package agent.targetter;

import static java.util.Collections.reverseOrder;
import static java.util.Comparator.comparing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import agent.AgentActions;
import agent.ITargetDecider;
import agent.TargetPathAgentStatus;
import agent.common.LitterExistingExpectation;
import core.IGraph;
import core.LitterSpawnPattern;
import core.ObservedData;
import core.Pair;
import core.RobotData;

public class ForMyopiaGreedy implements ITargetDecider
{
	Random _rand;
	List<Integer> _nodes;
	int _robotID;
	boolean _isAccumulated;
	double _rate;
	LitterExistingExpectation _expectation;
	boolean firstSearch = true;
	int NextTarget;
	double _expectationSum;
	double UpperLevelRate = _rate;
	
	
	public ForMyopiaGreedy(int ID, IGraph map, LitterSpawnPattern pattern, boolean isAccumulated, int seed, List<Integer> excludeNodes)
	{
		_robotID = ID;
		_nodes = new ArrayList<>(map.getNodes());
		
		for(int node : excludeNodes) 
		{
			_nodes.remove(new Integer(node));
		}
		
		_isAccumulated = isAccumulated;
		_rate = 0.05;
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
					list.add(i, new Pair<Integer, Double>(node, exp));
					sum += exp;
					i++;
				}
			}
			
			_expectationSum = sum;
			
			final Comparator<Pair<Integer, Double>> sortByValue = reverseOrder(comparing(Pair::getValue));
			final Comparator<Pair<Integer, Double>> sortByKey = reverseOrder(comparing(Pair::getKey));
			
			Collections.sort(list, sortByValue.thenComparing(sortByKey));
			
			int count = 5;
			
			if(firstSearch == true && list.get(count).getValue() == list.get(list.size()-2).getValue())
			{
				count = list.size()-1;
				firstSearch = false;
			}
			
			else 
			{
				if(list.get(count).getValue() == list.get(count+1).getValue()) 
				{
					double sameValue = list.get(count).getValue();
					int same = count;
					int small = list.size()-2;
					int check = (same+small)/2;
					
					do 
					{
						if(list.get(check).getValue() < sameValue) 
						{
							small = check;
							check = (small + same)/2;
						}
						
						else
						{
							same = check;
							check = (small + same)/2;
						}
					} while(!(check==same || check == small));
					count = same + 1;
				}
			}
			
			int index = count < 1 ? 0 :_rand.nextInt(count);
			
			NextTarget = list.get(index).getKey();
		}
	}


	public void setExpectation(LitterExistingExpectation expectation) 
	{
		_expectation = expectation;
	}


	public double getUpperLevelRate() 
	{
		return UpperLevelRate;
	}
	
	
	public void setAccessibleNodes(List<Integer> nodes) 
	{
		_nodes = nodes;
	}
	
	
	public double DiscountExpectation(ObservedData data, int node)
	{
		return 1.0;
	}
	
	
	public void ResetState() {}
	
}
