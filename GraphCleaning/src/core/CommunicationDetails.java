package core;

import java.util.ArrayList;
import java.util.List;

public class CommunicationDetails {
	
	public Coordinate _myCenterNode;
	
	public double _myExperienceWeight;
	
	public LitterSpawnPattern _mySpawnPattern;
	
	public List<Integer> _searchNodes = new ArrayList<>();;
	
	
	public CommunicationDetails(Coordinate myCenter, double weight, LitterSpawnPattern myPattern, List<Integer> searchNodes) {
		_myCenterNode = myCenter;
		_myExperienceWeight = weight;
		_mySpawnPattern = myPattern;
		_searchNodes = searchNodes;
	}
}
