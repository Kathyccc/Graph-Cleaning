package core.environment.controls;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import core.Pair;
import core.environment.Cleaner;
import core.environment.Field;
import core.environment.Litter;
import core.environment.LitterCollection;
import core.environment.Robot;
import core.environment.RobotCollection;

public class CleaningControl 
{
	Field _field;
	LitterCollection _litter;
	RobotCollection _robots;
	
	List<Pair<Cleaner, Litter>> _tasks = new ArrayList<Pair<Cleaner,Litter>>();
	Random _rand;
	List<Integer> _tasksLog = new ArrayList<>();
	
	
	public CleaningControl(Field field, int seed) 
	{
		_field = field;
		_litter = field.Litter;
		_robots = field.Robots;
		_rand = new Random(seed);
	}
	
	
	public void Clean(int id) {
		Robot robot = _robots.getRobot(id);
		_tasks.add(_rand.nextInt(_tasks.size()+1), new Pair<Cleaner, Litter>(
				robot.Cleaner, _litter.getLitter(robot.Position)));
	}
	
	
	public List<Integer> Update()
	{
		_tasksLog.clear();
		for(Pair<Cleaner, Litter> task : _tasks) {
			Cleaner cleaner = task.getKey();
			Litter litter = task.getValue();
			if(litter.Quantity != 0) _tasksLog.add(litter.Position);
			cleaner.Clean(litter);
		}
		_tasks.clear();
		return _tasksLog;
	}	
}
