package core;

public class LitterSpawnProbability {
	private double alphaInitial = 0.00001;
	double alpha = 0.1;
	double tmpalpha;
	double _litter = 0.0;
	double _stepInterval = 0.0;
	public int Interval;
	public double Probability;
	public int Increment; 
	 
	public LitterSpawnProbability(int interval, double probability, int increment) {
		Interval = interval;
		Probability = probability;
		Increment = increment;
	}
	
	
	public double UpdateProbability(int litter, int stepInterval) 
	{
		// convert to double type
		_litter = litter;
		_stepInterval = stepInterval;
		
//		System.out.println("LitterSpawnProb  " + _litter);
		
		if(_litter >= 1.0) _litter = 1.0;		
		
		if(stepInterval != 0)
		{
			tmpalpha = alphaInitial * stepInterval;
			
			if(tmpalpha > 0.1)
				tmpalpha = 0.1; // alphaInitial<tmpalpha<alphaMax
			
			Probability = (1 - tmpalpha) * Probability + tmpalpha * (_litter/stepInterval);
		}
		
		return Probability;
	}
}
