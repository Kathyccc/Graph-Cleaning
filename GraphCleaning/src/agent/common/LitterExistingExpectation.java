package agent.common;

import java.util.HashMap;
import java.util.Map;

import core.LitterSpawnPattern;
import core.RobotData;
import core.RobotDataCollection;

public class LitterExistingExpectation 
{
	int _time;
	LitterSpawnPattern _pattern = new LitterSpawnPattern();
	Map<Integer, Integer> _visitedTime = new HashMap<>();;
	boolean _isAccumulated;
	boolean IncrementEnabled;
	
	
	public LitterExistingExpectation(LitterSpawnPattern pattern, boolean isAccumulated) 
	{
		_time = 0;
		_pattern = pattern;
		_visitedTime = new HashMap<>();
		
		for(Integer node : _pattern._patterns.keySet()) 
		{
			_visitedTime.put(node, 0);
		}
		
		_isAccumulated = isAccumulated;
		IncrementEnabled = false;
	}
	
	public double getExpectation(int node, int time) 
	{
		double expectation = 0.0;
		int interval = time - _visitedTime.get(node);
		double prob = _pattern._patterns.get(node).Probability;
		
		if(interval > 0) 
		{
			if(_isAccumulated) 
				expectation = prob * interval;
			else 
				expectation = 1.0 - Math.pow(1.0 - prob, interval);
		}
		
		
		return IncrementEnabled ? expectation * _pattern._patterns.get(node).Increment : expectation;
	}
	
	
	public double getExpectation(int node) {
		return getExpectation(node, _time);
	}
	
	
	public void Update(int time) 
	{
		if(_time > time) 
			throw new IllegalArgumentException("The set time is invalid.");
		_time = time;
	}
	
	
	public void Update(RobotDataCollection data, int time) 
	{
		Update(time);
		
		for(RobotData robot : data._robots.values()) 
		{
			_visitedTime.put(robot.Position, time);
		}
	}
	
	
	public int getInterval(int node, int time) 
	{
		int interval = time - _visitedTime.get(node);

		return interval;
	}
	
	
	public int getInterval(int node) 
	{
		return getInterval(node, _time);
	}
	
	
	public void setMySpawnPattern(LitterSpawnPattern mySpawnPattern) 
	{
		_pattern = mySpawnPattern;
	}
	
	
		public void setVisitedTime(Map<Integer, Integer> visitedTime) 
	{
		_visitedTime = visitedTime;
	}
	
	public Map<Integer, Integer> getVisitedTime()
	{
		return _visitedTime;
	}
	
}
