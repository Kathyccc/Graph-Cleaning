package core;

public class Field {

	int Time;
	
	IGraph SpacialStructure;
	
	RobotCollection Robots;
	
	LitterCollection Litter = new LitterCollection();
	
	RobotBaseCollection RobotBases;
	
	LitterSpawnPattern LitterSpawnPattern;
	
	public Field(IGraph spacialStructure, LitterSpawnPattern pattern) {
		Time = 0;
		SpacialStructure = spacialStructure;
		LitterSpawnPattern = pattern;
		Robots = new RobotCollection();
		
	}
}
