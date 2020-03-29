package core.environment.controls;

import java.util.Map;
import java.util.Random;
import core.LitterSpawnPattern;
import core.LitterSpawnProbability;
import core.environment.Field;
import core.environment.LitterCollection;

public class LitterSpawnControl 
{
	Field _field;
	LitterCollection litterCollection;
	LitterSpawnPattern _patterns;
	
	Random _rand;
	
	public LitterSpawnControl(Field field, int seed) {
		_field = field ;
		litterCollection = field.Litter;
		_patterns = field.LitterSpawnPattern;
		
		_rand = new Random(seed);
	}
	
	
	public void setSpawnPattern(LitterSpawnPattern pattern) {
		_patterns = pattern;
	}
	
	public void Update() 
	{
		for(Map.Entry<Integer, LitterSpawnProbability> pattern : _patterns.getLitterSpawnPatternMap().entrySet()) 
		{
			LitterSpawnProbability prob = pattern.getValue();
			
			if(_field.Time % prob.Interval == 0) 
			{
				double n = _rand.nextFloat();

				if(n < prob.Probability) 
				{
					litterCollection.getLitter(pattern.getKey()).Increase(prob.Increment);
//					System.out.println(litterCollection.getLitter(pattern.getKey()).Position + "   " + litterCollection.getLitter(pattern.getKey()).Quantity);
				}
			}
		}
	}	
}
