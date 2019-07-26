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
	double maxLitterQuantity = 0;
	LogWriter _logger;
	LogWriter _loggerEachMax;
	int[] _eachNodeMaxQuantity = new int[10201];
	int[][] _eachMaxMemory = new int[10201][31];
	int _eachMaxCounter = 0;
	List<Integer> _high;
	List<Integer> _middle;
	List<Integer> _low;
	int[] _evaluationSumDetails = {0,0,0};
	
	
	public Evaluator(IEnvironment environ, List<Integer> high, List<Integer> middle, List<Integer> low)
	{
		_environment = environ;
		_logger = LogManager.CreateWriter("evaluation-s3600");
        _loggerEachMax = LogManager.CreateWriter("EachNodesMaxQuantity");
        _loggerEachMax.WriteLine("" + "0" + "," + "100000" + "," + "200000" + "," + "300000" + "," + "400000" + "," + "500000" + "," + "600000" + "," + "700000" + "," + "800000" + "," + "900000" + "," + "1000000"
                + "," + "1100000" + "," + "1200000" + "," + "1300000" + "," + "1400000" + "," + "1500000" + "," + "1600000" + "," + "1700000" + "," + "1800000" + "," + "1900000" + "," + "2000000"
                + "," + "2100000" + "," + "2200000" + "," + "2300000" + "," + "2400000" + "," + "2500000" + "," + "2600000" + "," + "2700000" + "," + "2800000" + "," + "2900000" + "," + "3000000");
        
        for(int i=0;i<10201;i++) { _eachNodeMaxQuantity[i] = 0; }
        _high = high;
        _middle = middle;
        _low = low;
	}
	
	
	public int getEvaluation() 
	{
		int time = _environment.GetTime();
		int litterQuantity = _environment.GetLitterQuantity();
		int currentMaxLitter = _environment.GetMaxLitterQuantity();
		LitterDataCollection litterData = _environment.GetLitterDataCollection();
		
		if(maxLitterQuantity < currentMaxLitter) maxLitterQuantity = currentMaxLitter;
		
		int[] eachQuantity = _environment.GetEachLitterQuantity();
		
		for(int i =0; i < 10201; i++) 
		{
			_eachNodeMaxQuantity[i] = Math.max(_eachNodeMaxQuantity[i], eachQuantity[i]);
		}
		
		for(LitterData litter : litterData._litter) 
		{
			if(_high.contains(litter.Position)) 
			{
				_evaluationSumDetails[0] += litter.Quantity;
			}
			
			else if(_middle.contains(litter.Position)) 
			{
				_evaluationSumDetails[1] += litter.Quantity;
			}
			
			else if(_low.contains(litter.Position)) 
			{
				_evaluationSumDetails[2] += litter.Quantity;
			}
		}
		
		_evaluationSum += litterQuantity;
		
		if(time%100000 == 0) 
		{
			for(int k = 0; k <10201 ; k++) 
			{
				_eachMaxMemory[k][_eachMaxCounter] = _eachNodeMaxQuantity[k];
				_eachNodeMaxQuantity[k] = 0;
			}
			_eachMaxCounter++;
		}
		
		if (time % 3600 == 0)
        {
            _logger.WriteLine(time + "," + _evaluationSum + "," + maxLitterQuantity + "," + _evaluationSumDetails[0] + "," + _evaluationSumDetails[1] + "," + _evaluationSumDetails[2]);
            _evaluationSum = 0;
            maxLitterQuantity = 0;
            _evaluationSumDetails[0] = 0;
            _evaluationSumDetails[1] = 0;
            _evaluationSumDetails[2] = 0;
        }
		
		if (time != 0 && time % 3000000 == 0)
        {
            for (int i = 0; i < 10201; i++)
            {
                _loggerEachMax.WriteLine(i + "," + _eachMaxMemory[i][0] + "," + _eachMaxMemory[i][1] + "," + _eachMaxMemory[i][2] + "," + _eachMaxMemory[i][3] + "," + _eachMaxMemory[i][4] + ","
                    + _eachMaxMemory[i][5] + "," + _eachMaxMemory[i][6] + "," + _eachMaxMemory[i][7] + "," + _eachMaxMemory[i][8] + "," + _eachMaxMemory[i][9] + "," + _eachMaxMemory[i][10] + ","
                    + _eachMaxMemory[i][11] + "," + _eachMaxMemory[i][12] + "," + _eachMaxMemory[i][13] + "," + _eachMaxMemory[i][14] + "," + _eachMaxMemory[i][15] + "," + _eachMaxMemory[i][16] + ","
                    + _eachMaxMemory[i][17] + "," + _eachMaxMemory[i][18] + "," + _eachMaxMemory[i][19] + "," + _eachMaxMemory[i][20] + ","
                    + _eachMaxMemory[i][21] + "," + _eachMaxMemory[i][22] + "," + _eachMaxMemory[i][23] + "," + _eachMaxMemory[i][24] + "," + _eachMaxMemory[i][25] + "," + _eachMaxMemory[i][26] + ","
                    + _eachMaxMemory[i][27] + "," + _eachMaxMemory[i][28] + "," + _eachMaxMemory[i][29] + "," + _eachMaxMemory[i][30]);
            }
        }
		return litterQuantity;
	}
	
}
