package core;

public class NodeProperty {
	public int ID;
	public String ProbabilityType;
	public boolean Obstacle;
	public int Potential;
	
	public NodeProperty(int id, String probabilityType, boolean obstacle) {
		ID = id;
		ProbabilityType = probabilityType;
//		Obstacle = obstacle;
		Potential = -1;
	}
}
