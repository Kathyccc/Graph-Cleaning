package core;

import java.util.ArrayList;
import java.util.List;

public class CommunicationDetails {
	
	Coordinate _myCenterNode;
	
	double _myExperienceWeight;
	
	LitterSpawnPattern _mySpawnPattern;
	
	List<Integer> _searchNodes = new ArrayList<>();;
	
	
	public CommunicationDetails(Coordinate myCenter, double weight, LitterSpawnPattern myPattern, List<Integer> searchNodes) {
		_myCenterNode = myCenter;
		_myExperienceWeight = weight;
		_mySpawnPattern = myPattern;
		_searchNodes = searchNodes;
	}
}
