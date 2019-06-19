package core;

public class LitterSpawnProbability {
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
	
	
	public double UpdateProbability(int litter, int stepInterval) {
		_litter = litter;
		_stepInterval = stepInterval;
		
		if(_litter >= 1.0) _litter = 1.0;
		
		if(stepInterval != 0)
		{
			Probability = (1-alpha)*Probability + alpha*(_litter/_stepInterval);
		}
		
		return Probability;
	}
	
	
	public void ForgetProbability(double difference, double normalData) {
		double parameter = 1- (difference/normalData * 0.15);
		
		if(parameter < 0.1) parameter = 0.1;
		
		Probability = Probability * parameter;
	}
}
