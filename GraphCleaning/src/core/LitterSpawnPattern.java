package core;

import java.util.HashMap;
import java.util.Map;

public class LitterSpawnPattern 
{
	public Map<Integer, LitterSpawnProbability> _patterns = new HashMap<>();
	
	public double getLitterSpawnProb(int node) {
		return _patterns.get(node).Probability; //is this[int index] necessary?
	}
	
	public void setLitterSpawnProb(int node, int litter, int stepInterval) {
		_patterns.get(node).UpdateProbability(litter, stepInterval);
	}
	
	public LitterSpawnPattern() {
		Map<Integer, LitterSpawnProbability> _patterns = new HashMap<>();
	}
	
	
	public void AddSpawnProbability(int node, LitterSpawnProbability probability) {
		_patterns.put(node, probability);
	}
	
	
	public boolean RemoveSpawnProbability(int node) {
		_patterns.remove(node);
		if(_patterns.containsKey(node)) return false;
		else return true;
	}
	
	
	public Map<Integer, LitterSpawnProbability> getLitterSpawnPatternMap(){
		return _patterns;
	}
	
}

