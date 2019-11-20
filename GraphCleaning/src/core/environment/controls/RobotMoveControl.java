package core.environment.controls;

import java.util.ArrayList;
import java.util.List;

import core.Pair;
import core.environment.Field;
import core.environment.RobotCollection;

public class RobotMoveControl 
{
	Field _field;
	RobotCollection _robots;
	
	List<Pair<Integer, Integer>> _destinations = new ArrayList<Pair<Integer, Integer>>();
	
	public RobotMoveControl(Field field) 
	{
		_field = field; 
		_robots = field.Robots;
		_destinations = new ArrayList<Pair<Integer, Integer>>();
	}
	
	
	public void Move(int id, int destination) {
		Pair<Integer, Integer>  move = new Pair<>(id, destination);
		_destinations.add(move);
	}
	
	
	public void Update() {
		for(Pair<Integer, Integer> pp : _destinations) 
		{
			_robots.getRobot(pp.getKey()).Move(pp.getValue());
		}
		
		_destinations.clear();
	}
}
