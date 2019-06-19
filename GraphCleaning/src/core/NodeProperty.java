package core;

public class NodeProperty {
	int _id;
	String _probabilityType;
	boolean _obstacle;
	int _potential;
	
	public NodeProperty(int id, String probabilityType, boolean obstacle) {
		_id = id;
		_probabilityType = probabilityType;
		_obstacle = obstacle;
		_potential = -1;
	}
}
