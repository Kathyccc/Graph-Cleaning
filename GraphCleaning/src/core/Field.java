package core;

public class Field {

	int Time;
	
	IGraph SpacialStructure;
	
	RobotCollection Robots = new RobotCollection();;
	
	LitterCollection Litter = new LitterCollection();
	
	RobotBaseCollection RobotBases;
	
	LitterSpawnPattern LitterSpawnPattern;
	
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
