package main.simulations;

import java.util.List;

import core.IEnvironment;
import core.LitterData;
import core.LitterDataCollection;
import core.utilities.LogManager;
import core.utilities.LogWriter;

public class Evaluator 
{
	IEnvironment _environment;
	int _evaluationSum = 0;
	
	LogWriter _logger;
	
	List<Integer> _high;
	List<Integer> _middle;
	List<Integer> _low;
	int[] _evaluationSumDetails = {0,0,0};
	
	public Evaluator(IEnvironment environ, List<Integer> high, List<Integer> middle, List<Integer> low)
	{
		_environment = environ;
		_logger = LogManager.CreateWriter("evaluation-s3600");		

        _high = high;
        _middle = middle;
        _low = low;
  	}
	
	
	public int getEvaluation() 
	{
		int time = _environment.GetTime();
		int litterQuantity = _environment.GetLitterQuantity();
		LitterDataCollection litterData = _environment.GetLitterDataCollection();
		
		_evaluationSum += litterQuantity;

		for(LitterData litter : litterData._litter) 
		{
			if(_high.contains(Integer.valueOf(litter.Position))) 
			{
				_evaluationSumDetails[0] += litter.Quantity;
			}
			
			else if(_middle.contains(Integer.valueOf(litter.Position))) 
			{
				_evaluationSumDetails[1] += litter.Quantity;
			}
			
			else if(_low.contains(Integer.valueOf(litter.Position))) 
			{
				_evaluationSumDetails[2] += litter.Quantity;
			}
		}		

		if (time % 3600 == 0)
        {
            _logger.WriteLine(time + "," + _evaluationSum + "," + _evaluationSumDetails[0] + "," + _evaluationSumDetails[1] + "," + _evaluationSumDetails[2]);

            _evaluationSum = 0;
            _evaluationSumDetails[0] = 0;
            _evaluationSumDetails[1] = 0;
            _evaluationSumDetails[2] = 0;
        }
		return litterQuantity;
	}
}
