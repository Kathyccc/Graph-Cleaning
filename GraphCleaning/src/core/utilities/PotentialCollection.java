package core.utilities;

import java.util.HashMap;
import java.util.Map;

public class PotentialCollection 
{
	public Map<Integer, Integer> _potentials = new HashMap<>();
	
	public int getPotential(int node) {
		return _potentials.get(node);
	}
	
	
	public PotentialCollection(Map<Integer, Integer> potentials){
		_potentials = potentials;
	}
}
