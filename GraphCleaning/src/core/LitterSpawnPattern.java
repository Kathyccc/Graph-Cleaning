package core;

import java.util.HashMap;
import java.util.Map;

public class LitterSpawnPattern 
{
	public Map<Integer, LitterSpawnProbability> _patterns = new HashMap<>();
	
	public LitterSpawnProbability getLitterSpawnProb(int node) {
		return _patterns.get(node); //is this[int index] necessary?
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
	
	
	public double getProbAverage() {
		double sum = 0.0;
		for(LitterSpawnProbability prob : _patterns.values()) {
			sum += prob.Probability;
		}
		double probAverage = sum / (double)_patterns.size();
		
		return probAverage;
	}
	
	
	public Map<Integer, LitterSpawnProbability> getLitterSpawnPatternMap(){
		return _patterns;
	}
	
}

