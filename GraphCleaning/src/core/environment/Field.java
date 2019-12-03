package core.environment;

import core.IGraph;
import core.LitterSpawnPattern;

public class Field {

	public int Time;
	
	public IGraph SpatialStructure;
	
	public RobotCollection Robots;
	
	public LitterCollection Litter;
	
	public RobotBaseCollection RobotBases;
	
	public LitterSpawnPattern LitterSpawnPattern = new LitterSpawnPattern();
	
	
	
	public Field(IGraph spatialStructure, LitterSpawnPattern pattern) {
		Time = 0;
		SpatialStructure = spatialStructure;
		LitterSpawnPattern = pattern;
		Robots = new RobotCollection();
		Litter = new LitterCollection();
		RobotBases = new RobotBaseCollection();
		
	}
	
	
	public void AddRobot(Robot robot) 
	{
		Robots.Add(robot);
	}
	
	
	public void AddLitter(Litter litter) {
		this.Litter.Add(litter);
	}
	
	
	public void RemoveLitter(int position) {
		Litter.Remove(Litter.getLitter(position));
	}
	
	
	public LitterCollection getLitter() {
		return Litter;
	}
	
	public void AddRobotBase(RobotBase rb) {
		RobotBases.Add(rb);
	}
	
	
	public void UpdateTime() {
		Time++;
	}
}
