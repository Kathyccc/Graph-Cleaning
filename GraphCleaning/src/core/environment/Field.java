package core.environment;

import core.IGraph;
import core.LitterSpawnPattern;

public class Field {

	public int Time;
	
	public IGraph SpacialStructure;
	
	public RobotCollection Robots = new RobotCollection();;
	
	public LitterCollection Litter = new LitterCollection();
	
	public RobotBaseCollection RobotBases;
	
	public LitterSpawnPattern LitterSpawnPattern;
	
	public Field(IGraph spacialStructure, LitterSpawnPattern pattern) {
		Time = 0;
		SpacialStructure = spacialStructure;
		LitterSpawnPattern = pattern;
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
	
	
	public void AddRobotBase(RobotBase rb) {
		RobotBases.Add(rb);
	}
	
	
	public void UpdateTime() {
		Time++;
	}
}
