package core;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LitterSpawnControl 
{
	Field _field;
	LitterCollection _litter;
	LitterSpawnPattern _patterns;
	
	Random _rand;
	
	public LitterSpawnControl(Field field, int seed) {
		_field = field ;
		_litter = field.Litter;
		_patterns = field.LitterSpawnPattern;
		
		_rand = new Random(seed);
	}
	
	
	public void setSpawnPattern(LitterSpawnPattern pattern) {
		_patterns = pattern;
	}
	
	public void Update() 
	{
		Map<Integer, LitterSpawnProbability> patterns = new HashMap<>(); 
		patterns = _patterns.getLitterSpawnPatternMap();
		
		
		for(Map.Entry<Integer, LitterSpawnProbability> pattern : patterns.entrySet()) 
		{
			LitterSpawnProbability prob = pattern.getValue();
			if(_field.Time % prob.Interval == 0) 
			{
				double n = _rand.nextDouble();
				if(n < prob.Probability) 
				{
					_litter.getLitter(pattern.getKey()).Increase(prob.Increment);
				}
			}
		}
	}	
}
